package mod.syconn.hero.features.ironman.client.renderers.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import mod.syconn.hero.client.HeroClient;
import mod.syconn.hero.features.heros.interfaces.IHeroHolder;
import mod.syconn.hero.features.ironman.Ironman;
import mod.syconn.hero.features.ironman.abilities.FlightAbility;
import mod.syconn.hero.features.ironman.item.IronmanArmorItem;
import mod.syconn.hero.features.ironman.server.data.SuitTag;
import mod.syconn.hero.utils.client.shaders.FlameRenderer;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;

public class IronmanFlightLayer<T extends Player, M extends PlayerModel<T>> extends RenderLayer<T, M> {

    private float fire;
    private int spin;

    public IronmanFlightLayer(RenderLayerParent<T, M> renderLayerParent) {
        super(renderLayerParent);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int light, T entity, float f, float g, float h, float j, float k, float l) {
        var controller = IHeroHolder.getAbility(entity, Ironman.class, FlightAbility.class);
        if (controller != null && entity.getItemBySlot(EquipmentSlot.FEET).getItem() instanceof IronmanArmorItem) {
            if (controller.isFlying() || controller.getFlyingTicks() > 0 || controller.getSlowFallingTicks() > 0) {

                final float progress = Mth.clamp(Math.max(controller.getFlyingTicks() / 15F, controller.getSlowFallingTicks() / 8F), 0F, 1F);
                final var color = SuitTag.getOrCreate(entity.getItemBySlot(EquipmentSlot.FEET)).color;
                fire += 0.25f;
                spin += 25;
                renderHandThruster(poseStack, bufferSource, HumanoidArm.LEFT, light, color, progress);
                renderHandThruster(poseStack, bufferSource, HumanoidArm.RIGHT, light, color, progress);
                renderFeetThruster(poseStack, bufferSource, HumanoidArm.LEFT, light, color, progress);
                renderFeetThruster(poseStack, bufferSource, HumanoidArm.RIGHT, light, color, progress);
            }
        }
    }

    private void renderHandThruster(PoseStack poseStack, MultiBufferSource bufferSource, HumanoidArm arm, int light, int color, float progress) {
        poseStack.pushPose();
        this.getParentModel().translateToHand(arm, poseStack);
        poseStack.translate((arm == HumanoidArm.RIGHT ? -1 : 1) / 16.0F, 0.70F, 0.0F);
        // TODO SCALE EFFECT + SCALE FOR SLIM
        poseStack.mulPose(Axis.YP.rotationDegrees(spin));
        poseStack.mulPose(Axis.XP.rotationDegrees(-90.0F));
        poseStack.scale(0.65f, 0.65f, Mth.lerp(progress, 0.25f, 0.85f));
        FlameRenderer.renderFlame(poseStack, bufferSource, fire, light, OverlayTexture.NO_OVERLAY, color);
        poseStack.popPose();
    }

    private void renderFeetThruster(PoseStack poseStack, MultiBufferSource bufferSource, HumanoidArm arm, int light, int color, float progress) {
        poseStack.pushPose();
        if (arm == HumanoidArm.LEFT) this.getParentModel().leftLeg.translateAndRotate(poseStack);
        else this.getParentModel().rightLeg.translateAndRotate(poseStack);

        poseStack.translate(0, 0.9f, 0);
//        poseStack.translate((arm == HumanoidArm.RIGHT ? -1 : 1) / 16.0F, 0.70F, 0.0F);
        // TODO SCALE EFFECT + SCALE FOR SLIM
        poseStack.mulPose(Axis.YP.rotationDegrees(spin));
        poseStack.mulPose(Axis.XP.rotationDegrees(-90.0F));
        poseStack.scale(1f, 1f, Mth.lerp(progress, 0.25f, 1f));
        FlameRenderer.renderFlame(poseStack, bufferSource, fire, light, OverlayTexture.NO_OVERLAY, color);
        poseStack.popPose();
    }
}
