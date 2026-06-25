package mod.syconn.hero.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import mod.syconn.hero.features.addons.IronmanContent;
import mod.syconn.hero.utils.interfaces.ICustomArmor;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class IronmanArmorRenderer {

    private final ModelPart innerModel;
    private final ModelPart outerModel;
    private Map<EquipmentSlot, ItemStack> gear;

    public IronmanArmorRenderer(Function<ModelLayerLocation, ModelPart> baker) {
        this.innerModel = baker.apply(ModelLayers.PLAYER_SLIM_INNER_ARMOR);
        this.outerModel = baker.apply(ModelLayers.PLAYER_SLIM_OUTER_ARMOR);
        this.gear = new HashMap<>();
    }

    public void setGear(ResourceLocation model) {
        this.gear = IronmanContent.createSuitMap(model);
    }

    public void setGear(Map<EquipmentSlot, ItemStack> gear) {
        this.gear = gear;
    }

    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.mulPose(Axis.XP.rotationDegrees(180f));
        poseStack.translate(0f, -1.5f, 0f);
        this.renderArmorPiece(poseStack, buffer, EquipmentSlot.CHEST, packedLight, this.getArmorModel(EquipmentSlot.CHEST));
        this.renderArmorPiece(poseStack, buffer, EquipmentSlot.LEGS, packedLight, this.getArmorModel(EquipmentSlot.LEGS));
        this.renderArmorPiece(poseStack, buffer, EquipmentSlot.FEET, packedLight, this.getArmorModel(EquipmentSlot.FEET));
        this.renderArmorPiece(poseStack, buffer, EquipmentSlot.HEAD, packedLight, this.getArmorModel(EquipmentSlot.HEAD));
    }

    private void renderArmorPiece(PoseStack poseStack, MultiBufferSource buffer, EquipmentSlot slot, int packedLight, ModelPart model) {
        var stack = this.gear.get(slot);
        if (stack != null && stack.getItem() instanceof ICustomArmor armor)
            armor.getRenderLocation(stack, slot).ifPresent(texture -> model.render(poseStack, buffer.getBuffer(RenderType.armorCutoutNoCull(texture)), packedLight, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0F));
    }

    private ModelPart getArmorModel(EquipmentSlot slot) {
        return slot == EquipmentSlot.LEGS ? this.innerModel : this.outerModel;
    }
}
