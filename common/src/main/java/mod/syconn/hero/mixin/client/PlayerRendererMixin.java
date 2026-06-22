package mod.syconn.hero.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import mod.syconn.hero.client.HeroClient;
import mod.syconn.hero.client.particle.TrailParticleOptions;
import mod.syconn.hero.core.ModParticles;
import mod.syconn.hero.feature.heros.interfaces.IHeroHolder;
import mod.syconn.hero.feature.ironman.Ironman;
import mod.syconn.hero.feature.ironman.abilities.FlightAbility;
import mod.syconn.hero.feature.ironman.server.data.SuitTag;
import mod.syconn.hero.item.IronmanArmorItem;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.core.particles.DustColorTransitionOptions;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
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
    private void renderThrusters(AbstractClientPlayer entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, CallbackInfo ci) {
        if (entity instanceof IHeroHolder holder && entity.getItemBySlot(EquipmentSlot.FEET).getItem() instanceof IronmanArmorItem) {
            var flightController = holder.hero$getManager().getType(Ironman.class).getAbility(FlightAbility.class);

            if (flightController.isFlying() || flightController.getFlyingTicks() > 0 || flightController.getSlowFallingTicks() > 0) {
                var sneak = entity.isShiftKeyDown();
                var sideOffset = flightController.isFlying() ? -0.2 : 0.3;
                var forwardOffset = flightController.isFlying() ? 1.5 : sneak ? -0.2f : 0.025;
                var upOffset = flightController.isFlying() ? -2 : sneak ? -0.9f : -1;
                var forward = Vec3.directionFromRotation(0, entity.yBodyRot);
                var right = forward.cross(new Vec3(0, 1, 0)).normalize();
                var up = right.cross(forward).normalize();
                var zRotLeft = model.leftArm.zRot;
                var cos = Math.cos(zRotLeft);
                var sin = Math.sin(zRotLeft);
                var rotatedRight = right.scale(cos).add(up.scale(sin));
                var rotatedUp = up.scale(cos).add(right.scale(-sin));
                var leftHand = entity.getEyePosition().add(rotatedRight.scale(-sideOffset)).add(forward.scale(forwardOffset)).add(rotatedUp.scale(upOffset));
                var zRotRight = model.rightArm.zRot;
                cos = Math.cos(zRotRight);
                sin = Math.sin(zRotRight);
                rotatedRight = right.scale(cos).add(up.scale(sin));
                rotatedUp = up.scale(cos).add(right.scale(-sin));
                var rightHand = entity.getEyePosition().add(rotatedRight.scale(+sideOffset)).add(forward.scale(forwardOffset)).add(rotatedUp.scale(upOffset));

                var feetSideOffset = 0.1;
                var feetUpOffset = sneak ? -0.2f : -0.05f;
                var feetForwardOffset = sneak ? -0.2f : 0;
                var feetRight = entity.position().add(up.scale(feetUpOffset)).add(right.scale(-feetSideOffset)).add(forward.scale(feetForwardOffset));
                var feetLeft = entity.position().add(up.scale(feetUpOffset)).add(right.scale(feetSideOffset)).add(forward.scale(feetForwardOffset));

                var slowFalling = flightController.getMode() == FlightAbility.FlightMode.HOVER | flightController.getSlowFallingTicks() > 0;
                var mov = entity.getDeltaMovementLerped(HeroClient.getTickDelta()).multiply(-1, -1, -1);
                var dx = slowFalling ? mov.x : 0;
                var dy = flightController.getMode() == FlightAbility.FlightMode.HOVER ? -1 : entity.getDeltaMovementLerped(HeroClient.getTickDelta()).y; // -1
                var dz = slowFalling ? mov.z : 0;
                var speed = entity.getDeltaMovement().horizontalDistance();
                var control = slowFalling ? Math.max(Math.min(0.5f, (float)(speed * 2.0)), 0.3f) : 0.0f;
                float verticalControl;
                if (flightController.getSlowFallingTicks() > 0) verticalControl = 1f + (1f - (flightController.getSlowFallingTicks() / 8f));
                else if (mov.y > 0) verticalControl = 1f;
                else if (mov.y < 0) verticalControl = 0.15f;
                else verticalControl = control;
                var slowFallOffset = flightController.getSlowFallingTicks() > 0 ? Math.max(0f, 1.5f - (flightController.getSlowFallingTicks() / 8f)) : 0;

                var color = SuitTag.getOrCreate(entity.getItemBySlot(EquipmentSlot.FEET)).color;
                var start = new Vector3f(FastColor.ARGB32.red(color) / 255f, FastColor.ARGB32.green(color) / 255f, FastColor.ARGB32.blue(color) / 255f);
                var end = new Vector3f(start.x * 0.35f, start.y * 0.35f, start.z * 0.35f);
                entity.level().addParticle(new TrailParticleOptions(start, end), rightHand.x, rightHand.y - slowFallOffset, rightHand.z, dx * control, dy * verticalControl, dz * control);
                entity.level().addParticle(new TrailParticleOptions(start, end), leftHand.x, leftHand.y - slowFallOffset, leftHand.z, dx * control, dy * verticalControl, dz * control);
                entity.level().addParticle(new TrailParticleOptions(start, end), feetRight.x, feetRight.y, feetRight.z, dx * control, dy * verticalControl, dz * control);
                entity.level().addParticle(new TrailParticleOptions(start, end), feetLeft.x, feetLeft.y, feetLeft.z, dx * control, dy * verticalControl, dz * control);
            }
        }
    }
}
