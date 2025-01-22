package mod.syconn.hero.mixin;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mod.syconn.hero.common.item.IronmanArmorItem;
import mod.syconn.hero.util.data.SuitSettings;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.Map;

@SuppressWarnings("unchecked")
@Mixin(HumanoidArmorLayer.class)
public class HumanoidArmorLayerMixin<T extends LivingEntity, M extends HumanoidModel<T>, A extends HumanoidModel<T>> {
    @Unique
    private static final Map<String, ResourceLocation> ARMOR_LOCATION_CACHE = Maps.newHashMap();

    @Inject(at = @At("HEAD"), method = "renderArmorPiece", cancellable = true)
    public void renderArmorPiece(PoseStack poseStack, MultiBufferSource buffer, T livingEntity, EquipmentSlot slot, int packedLight, A model, CallbackInfo ci) {
        ItemStack itemStack = livingEntity.getItemBySlot(slot);
        Item bl = itemStack.getItem();
        if (livingEntity instanceof Player player && bl instanceof IronmanArmorItem armorItem && armorItem.getEquipmentSlot() == slot && slot == EquipmentSlot.HEAD) {
            ((HumanoidArmorLayer<T, M, A>) (Object) this).getParentModel().copyPropertiesTo(model);
            this.heroCore$setPartVisibility(model, slot);
            this.heroCore$renderModel(poseStack, buffer, packedLight, armorItem, model, SuitSettings.from(player).isLifted());
            ci.cancel();
        }
    }

    @Unique
    protected void heroCore$setPartVisibility(A model, EquipmentSlot slot) {
        model.setAllVisible(false);
        switch (slot) {
            case HEAD:
                model.head.visible = true;
                model.hat.visible = true;
                break;
            case CHEST:
                model.body.visible = true;
                model.rightArm.visible = true;
                model.leftArm.visible = true;
                break;
            case LEGS:
                model.body.visible = true;
                model.rightLeg.visible = true;
                model.leftLeg.visible = true;
                break;
            case FEET:
                model.rightLeg.visible = true;
                model.leftLeg.visible = true;
        }
    }

    @Unique
    private void heroCore$renderModel(PoseStack poseStack, MultiBufferSource buffer, int packedLight, ArmorItem armorItem, A model, boolean lifted) {
        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.armorCutoutNoCull(this.heroCore$getArmorLocation(armorItem, lifted)));
        model.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0F);
    }

    @Unique
    private ResourceLocation heroCore$getArmorLocation(ArmorItem armorItem, boolean lifted) {
        return ARMOR_LOCATION_CACHE.computeIfAbsent("textures/models/armor/" + armorItem.getMaterial().getName() + "_layer_1" + (lifted ? "_lifted" : "") + ".png", ResourceLocation::new);
    }
}
