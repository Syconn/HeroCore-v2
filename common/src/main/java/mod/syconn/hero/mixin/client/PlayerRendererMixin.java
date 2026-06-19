package mod.syconn.hero.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import mod.syconn.hero.feature.heros.interfaces.IHeroHolder;
import mod.syconn.hero.feature.ironman.Ironman;
import mod.syconn.hero.feature.ironman.abilities.FlightAbility;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
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

                float g = flightController.getFlyingTicks() + partialTicks; // NO fallFlyingTicks dependency
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
}
