package mod.syconn.hero.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import mod.syconn.hero.features.ironman.client.renderers.entity.IronmanArmorRenderer;
import mod.syconn.hero.utils.interfaces.ICustomArmor;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidArmorLayer.class)
public abstract class HumanoidArmorLayerMixin<T extends LivingEntity, M extends HumanoidModel<T>, A extends HumanoidModel<T>> extends RenderLayer<T, M> { // TODO HOVER ACTUALLY BREAK UNDER GROUND

    public HumanoidArmorLayerMixin(RenderLayerParent<T, M> renderer) {
        super(renderer);
    }

    @Shadow protected abstract void renderGlint(PoseStack poseStack, MultiBufferSource buffer, int packedLight, A model);

    @Shadow protected abstract void setPartVisibility(A model, EquipmentSlot slot);

    @Inject(at = @At("HEAD"), method = "renderArmorPiece", cancellable = true)
    public void renderArmorPiece(PoseStack poseStack, MultiBufferSource buffer, T livingEntity, EquipmentSlot slot, int packedLight, A model, CallbackInfo ci) {
        var itemStack = livingEntity.getItemBySlot(slot);
        var bl = itemStack.getItem();
        if (bl instanceof ICustomArmor armor) {
            this.getParentModel().copyPropertiesTo(model);
            this.setPartVisibility(model, slot);
            IronmanArmorRenderer.renderModel(poseStack, buffer, packedLight, livingEntity, armor, slot, model);
            if (itemStack.hasFoil()) this.renderGlint(poseStack, buffer, packedLight, model);
            ci.cancel();
        }
    }
}
