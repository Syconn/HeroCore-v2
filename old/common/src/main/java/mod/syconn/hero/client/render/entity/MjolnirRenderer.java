package mod.syconn.hero.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import mod.syconn.hero.entity.ThrownMjolnir;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.ThrownItemRenderState;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;

public class MjolnirRenderer extends EntityRenderer<ThrownMjolnir, ThrownItemRenderState> {
    private final ItemModelResolver itemModelResolver;
    
    public MjolnirRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
        itemModelResolver = pContext.getItemModelResolver();
    }

    public void extractRenderState(ThrownMjolnir pEntity, ThrownItemRenderState pState, float pPartialTicks) {
        super.extractRenderState(pEntity, pState, pPartialTicks);
        this.itemModelResolver.updateForNonLiving(pState.item, pEntity.getWeaponItem(), ItemDisplayContext.GROUND, pEntity);
    }

    public ThrownItemRenderState createRenderState() {
        return new ThrownItemRenderState();
    }

    public void render(ThrownItemRenderState pState, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight) {
        pPoseStack.pushPose();
        pPoseStack.translate(0, 0.5f, 0);
        pPoseStack.mulPose(Axis.XP.rotationDegrees(180f));
        pState.item.render(pPoseStack, pBufferSource, pPackedLight, OverlayTexture.NO_OVERLAY);
        pPoseStack.popPose();
        super.render(pState, pPoseStack, pBufferSource, pPackedLight);
    }
}