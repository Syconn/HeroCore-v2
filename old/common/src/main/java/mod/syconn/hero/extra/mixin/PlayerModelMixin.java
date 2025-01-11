package mod.syconn.hero.extra.mixin;

import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.PlayerRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerModel.class)
public class PlayerModelMixin {

    @SuppressWarnings("all")
    @Inject(at = @At("TAIL"), method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/PlayerRenderState;)V")
    private void setupAnim(PlayerRenderState renderState, CallbackInfo info) {
//        if (!(renderState instanceof Player player))
//            return;

//        PlayerModel<T> model = (PlayerModel<T>) (Object) this;
//        if (!Minecraft.getInstance().options.getCameraType().isFirstPerson()) {
//            if (player.getItemInHand(InteractionHand.MAIN_HAND).is(ItemRegistrar.MJOLNIR.get())) {
//                model.leftArm.zRot = (float) Math.toRadians(90.0F);
//            }
//        }
    }

    private void copyArmAngles(PlayerModel model){
        copyModelAngles(model.rightArm, model.rightSleeve);
        copyModelAngles(model.leftArm, model.leftSleeve);
    }

    private static void copyModelAngles(ModelPart source, ModelPart target) {
        target.xRot = source.xRot;
        target.yRot = source.yRot;
        target.zRot = source.zRot;
    }
}
