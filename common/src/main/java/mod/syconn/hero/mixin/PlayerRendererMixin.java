package mod.syconn.hero.mixin;

import mod.syconn.hero.util.Helpers;
import mod.syconn.hero.util.SuitSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerModel.class)
public class PlayerRendererMixin<T extends LivingEntity> {

    @SuppressWarnings("unchecked")
    @Inject(at = @At("TAIL"), method = "setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V")
    private void test(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
        if(!(entity instanceof Player player))
            return;

        PlayerModel<T> model = (PlayerModel<T>) (Object) this;
        if (!Minecraft.getInstance().options.getCameraType().isFirstPerson()) {
            if (Helpers.isWearingIronManSuit(player)) {
                SuitSettings settings = SuitSettings.from(player);

                if (settings.getFlightMode() == SuitSettings.FlightMode.HOVER && player.level().getBlockState(player.getOnPos()).isAir()) {
                    model.leftArm.zRot = (float) Math.toRadians(-15);
                    model.rightArm.zRot = (float) Math.toRadians(15);
                    heroCore$copyAngles(model);
                } else if (settings.getFlightMode() == SuitSettings.FlightMode.FLY) {
                    model.leftLeg.xRot = (float) Math.toRadians(90);
                    model.rightLeg.xRot = (float) Math.toRadians(90);
                    System.out.println(model.rightLeg.y);
//                    model.leftLeg.y += 12;
//                    model.rightLeg.y += 12;
//                    model.leftLeg.y -= 12;
                    model.rightLeg.z = 5;
                    model.rightLeg.y = -3;
//                    model.head.z += 4;
                    model.body.xRot = (float) Math.toRadians(90);
                    model.rightArm.xRot = (float) Math.toRadians(90);
                    model.leftArm.xRot = (float) Math.toRadians(90);
                    model.rightArm.yRot = (float) Math.toRadians(-15);
                    model.leftArm.yRot = (float) Math.toRadians(15);

                    heroCore$copyAngles(model);
                }
            }
        }
    }

    @Unique
    private void heroCore$copyAngles(PlayerModel<T> model){
        heroCore$copyAngles(model.rightArm, model.rightSleeve);
        heroCore$copyAngles(model.leftArm, model.leftSleeve);
        heroCore$copyAngles(model.body, model.jacket);
        heroCore$copyAngles(model.rightLeg, model.rightPants);
        heroCore$copyAngles(model.leftLeg, model.leftPants);
    }

    @Unique
    private static void heroCore$copyAngles(ModelPart source, ModelPart target) {
        target.xRot = source.xRot;
        target.yRot = source.yRot;
        target.zRot = source.zRot;
    }
}
