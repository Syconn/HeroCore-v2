package mod.syconn.hero.mixin.client;

import mod.syconn.hero.client.HeroClient;
import mod.syconn.hero.feature.heros.interfaces.IHeroHolder;
import mod.syconn.hero.feature.ironman.Ironman;
import mod.syconn.hero.feature.ironman.abilities.FlightAbility;
import mod.syconn.hero.utils.interfaces.IModifiedPoseRenderer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerModel.class)
public abstract class PlayerModelMixin<T extends LivingEntity> extends HumanoidModel<T> {

    @Shadow
    @Final
    public ModelPart leftSleeve;

    @Shadow
    @Final
    public ModelPart rightSleeve;

    public PlayerModelMixin(ModelPart root) {
        super(root);
    }

    @Inject(method = "setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V", at = @At("HEAD"))
    private void walkAnimation(LivingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
        if (entity instanceof IHeroHolder holder && holder.hero$getManager().getType(Ironman.class).getAbility(FlightAbility.class).isFlying()) {
            entity.walkAnimation.setSpeed(0.0F);
        }
    }

    @Inject(method = "setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V", at = @At("TAIL"))
    private void animateHead(LivingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
        if (entity instanceof IHeroHolder holder) {
            var flightController = holder.hero$getManager().getType(Ironman.class).getAbility(FlightAbility.class);
            float progress = Mth.clamp(Math.max(flightController.getFlyingTicks() / 15F, flightController.getSlowFallingTicks() / 8F), 0F, 1F);
            boolean hover = flightController.getMode() == FlightAbility.FlightMode.HOVER && !entity.onGround();
            boolean flyFalling = flightController.isFlying() || flightController.getSlowFallingTicks() > 0;
            float headTarget = flightController.isFlying() ? (float) Math.toRadians(-90 * (flightController.getFlyingTicks() / 15f)) : 0F;
            float leftArmTarget = (flyFalling || hover) ? (float) Math.toRadians(-25) : 0F;
            float rightArmTarget = (flyFalling || hover) ? (float) Math.toRadians(25) : 0F;

            if (flightController.getMode() != FlightAbility.FlightMode.HOVER && flightController.isFlying()) {
                this.head.xRot += (headTarget - this.head.xRot) * progress;
                this.hat.xRot = this.head.xRot;
            }

            this.leftArm.zRot += (leftArmTarget - this.leftArm.zRot) * progress;
            this.leftSleeve.zRot = this.leftArm.zRot;
            this.rightArm.zRot += (rightArmTarget - this.rightArm.zRot) * progress;
            this.rightSleeve.zRot = this.rightArm.zRot;
        }
    }

    @Inject(method = "setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V", at = @At(value = "TAIL"))
    public void setAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
        if (entity.isSwimming()) return;

        for (var hand : InteractionHand.values()) {
            var stack = entity.getItemInHand(hand);
            if (!stack.isEmpty()) {
                final IModifiedPoseRenderer pose = IModifiedPoseRenderer.REGISTRY.get(stack.getItem().getClass());
                if (pose != null) {
                    pose.modifyPose(entity, hand, stack, this, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, HeroClient.getTickDelta());
                    break;
                }
            }
        }
    }
}
