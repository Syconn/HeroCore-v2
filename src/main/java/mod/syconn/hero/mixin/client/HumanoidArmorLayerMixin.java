package mod.syconn.hero.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mod.syconn.hero.utils.interfaces.ICustomArmor;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidArmorLayer.class)
public abstract class HumanoidArmorLayerMixin<T extends LivingEntity, M extends HumanoidModel<T>, A extends HumanoidModel<T>> extends RenderLayer<T, M> {

    public HumanoidArmorLayerMixin(RenderLayerParent<T, M> renderer) {
        super(renderer);
    }

    @Shadow protected abstract void renderGlint(PoseStack poseStack, MultiBufferSource buffer, int packedLight, A model);

    @Shadow protected abstract void setPartVisibility(A model, EquipmentSlot slot);

    @Inject(at = @At("HEAD"), method = "renderArmorPiece", cancellable = true)
    public void renderArmorPiece(PoseStack poseStack, MultiBufferSource buffer, T livingEntity, EquipmentSlot slot, int packedLight, A model, CallbackInfo ci) {
        ItemStack itemStack = livingEntity.getItemBySlot(slot);
        Item bl = itemStack.getItem();
        if (bl instanceof ICustomArmor armor) {
            this.getParentModel().copyPropertiesTo(model);
            this.setPartVisibility(model, slot);
            this.heroCore$renderModel(poseStack, buffer, packedLight, livingEntity, armor, slot, model);
            if (itemStack.hasFoil()) this.renderGlint(poseStack, buffer, packedLight, model);
            ci.cancel();
        }
    }

    @Unique
    private void heroCore$renderModel(PoseStack poseStack, MultiBufferSource buffer, int packedLight, LivingEntity entity, ICustomArmor armorItem, EquipmentSlot slot, A model) {
        final var location = armorItem.getRenderLocation(entity, slot);
        if (location.isPresent()) {
            VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.armorCutoutNoCull(location.get()));
            model.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0F);
        }
    }
}
