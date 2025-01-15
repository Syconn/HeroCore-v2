package mod.syconn.hero.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import mod.syconn.hero.common.entity.ThrownMjolnir;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;

public class MjolnirRenderer extends EntityRenderer<ThrownMjolnir> {
    
    private final ItemRenderer itemRenderer;
    
    public MjolnirRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
        itemRenderer = pContext.getItemRenderer();
    }

    public void render(ThrownMjolnir entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        poseStack.translate(0, 0.5f, 0);
        poseStack.mulPose(Axis.XP.rotationDegrees(180f));
        BakedModel bakedModel = this.itemRenderer.getModel(entity.getMjonirItem(), entity.level(), null, entity.getId());
        this.itemRenderer.render(entity.getMjonirItem(), ItemDisplayContext.GROUND, false, poseStack, buffer, packedLight, OverlayTexture.NO_OVERLAY, bakedModel);
        poseStack.popPose();
        super.render(entity, entityYaw, partialTick, poseStack, buffer, packedLight);
    }

    public ResourceLocation getTextureLocation(ThrownMjolnir entity) {
        return InventoryMenu.BLOCK_ATLAS;
    }
}