package mod.syconn.hero.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import mod.syconn.hero.client.HeroClient;
import mod.syconn.hero.client.particle.TrailParticleOptions;
import mod.syconn.hero.features.heros.interfaces.IHeroHolder;
import mod.syconn.hero.features.ironman.Ironman;
import mod.syconn.hero.features.ironman.abilities.FlightAbility;
import mod.syconn.hero.features.ironman.item.IronmanArmorItem;
import mod.syconn.hero.features.ironman.server.data.SuitTag;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerRenderer.class)
public abstract class PlayerRendererMixin extends LivingEntityRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    public PlayerRendererMixin(EntityRendererProvider.Context context, PlayerModel<AbstractClientPlayer> model, float shadowRadius) {
        super(context, model, shadowRadius);
    }

    @Inject(method = "setupRotations(Lnet/minecraft/client/player/AbstractClientPlayer;Lcom/mojang/blaze3d/vertex/PoseStack;FFF)V", at = @At("HEAD"), cancellable = true)
    private void heroCore$customSetupRotations(AbstractClientPlayer entityLiving, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTicks, CallbackInfo ci) {
        if (entityLiving instanceof IHeroHolder holder) {
            var flightController = holder.hero$getManager().getType(Ironman.class).getAbility(FlightAbility.class);
            if (flightController.isFlying()) {
                super.setupRotations(entityLiving, poseStack, ageInTicks, rotationYaw, partialTicks);
                float g = flightController.getFlyingTicks() + partialTicks;
                float h = Mth.clamp(g * g / 100.0F, 0.0F, 1.0F);

                if (!entityLiving.isAutoSpinAttack()) poseStack.mulPose(Axis.XP.rotationDegrees(h * (-90.0F - entityLiving.getXRot())));
                Vec3 look = entityLiving.getViewVector(partialTicks);
                Vec3 motion = entityLiving.getDeltaMovementLerped(partialTicks);
                double d = motion.horizontalDistanceSqr();
                double e = look.horizontalDistanceSqr();
                if (d > 0.0 && e > 0.0) {
                    double i = (motion.x * look.x + motion.z * look.z) / Math.sqrt(d * e);
                    double j = motion.x * look.z - motion.z * look.x;
                    poseStack.mulPose(Axis.YP.rotation((float)(Math.signum(j) * Math.acos(i))));
                }
                ci.cancel();
            }
        }
    }

    @Inject(method = "render(Lnet/minecraft/client/player/AbstractClientPlayer;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At("TAIL"))
    private void renderThrusters(AbstractClientPlayer abstractClientPlayer, float f, float g, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, CallbackInfo ci) {
        if (abstractClientPlayer instanceof IHeroHolder holder && abstractClientPlayer.getItemBySlot(EquipmentSlot.FEET).getItem() instanceof IronmanArmorItem) {
            var flightController = holder.hero$getManager().getType(Ironman.class).getAbility(FlightAbility.class);

            if (flightController.isFlying() || flightController.getFlyingTicks() > 0) {
                var sneak = abstractClientPlayer.isShiftKeyDown();
                var forward = Vec3.directionFromRotation(0, abstractClientPlayer.yBodyRot);
                var right = forward.cross(new Vec3(0, 1, 0)).normalize();
                var up = right.cross(forward).normalize();
                var feetSideOffset = 0.1;
                var feetUpOffset = sneak ? -0.2f : -0.05f;
                var feetForwardOffset = sneak ? -0.2f : 0;
                var feetRight = abstractClientPlayer.position().add(up.scale(feetUpOffset)).add(right.scale(-feetSideOffset)).add(forward.scale(feetForwardOffset));
                var feetLeft = abstractClientPlayer.position().add(up.scale(feetUpOffset)).add(right.scale(feetSideOffset)).add(forward.scale(feetForwardOffset));
                var slowFalling = flightController.getMode() == FlightAbility.FlightMode.HOVER | flightController.getSlowFallingTicks() > 0;
                var mov = abstractClientPlayer.getDeltaMovementLerped(HeroClient.getTickDelta()).multiply(-1, -1, -1);
                var dx = slowFalling ? mov.x : 0;
                var dy = flightController.getMode() == FlightAbility.FlightMode.HOVER ? -1 : abstractClientPlayer.getDeltaMovementLerped(HeroClient.getTickDelta()).y; // -1
                var dz = slowFalling ? mov.z : 0;
                var speed = abstractClientPlayer.getDeltaMovement().horizontalDistance();
                var control = slowFalling ? Math.max(Math.min(0.5f, (float)(speed * 2.0)), 0.3f) : 0.0f;
                float verticalControl;

                if (flightController.getSlowFallingTicks() > 0) verticalControl = 1f + (1f - (flightController.getSlowFallingTicks() / 8f));
                else if (mov.y > 0) verticalControl = 1f;
                else if (mov.y < 0) verticalControl = 0.15f;
                else verticalControl = control;

                var color = SuitTag.getOrCreate(abstractClientPlayer.getItemBySlot(EquipmentSlot.FEET)).color;
                var start = new Vector3f(FastColor.ARGB32.red(color) / 255f, FastColor.ARGB32.green(color) / 255f, FastColor.ARGB32.blue(color) / 255f);
                var end = new Vector3f(start.x * 0.35f, start.y * 0.35f, start.z * 0.35f);
                abstractClientPlayer.level().addParticle(new TrailParticleOptions(start, end), feetRight.x, feetRight.y, feetRight.z, dx * control, dy * verticalControl, dz * control);
                abstractClientPlayer.level().addParticle(new TrailParticleOptions(start, end), feetLeft.x, feetLeft.y, feetLeft.z, dx * control, dy * verticalControl, dz * control);
            }
        }
    }
}
