package mod.syconn.hero.features.ironman.client.renderers.entity;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Axis;
import mod.syconn.hero.features.ironman.item.IronmanArmorItem;
import mod.syconn.hero.features.ironman.server.data.SuitTag;
import mod.syconn.hero.utils.Constants;
import mod.syconn.hero.utils.interfaces.ICustomArmor;
import net.minecraft.client.model.HumanoidArmorModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class IronmanArmorRenderer {

    private static RenderType getGlowArmor(ResourceLocation texture) {
        return RenderType.create(Constants.withId("ironman_glow").toString(), DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, true,
                RenderType.CompositeState.builder().setShaderState(RenderStateShard.RENDERTYPE_ENTITY_TRANSLUCENT_EMISSIVE_SHADER).setTextureState(new RenderStateShard.TextureStateShard(texture, false, false)).setWriteMaskState(RenderStateShard.COLOR_WRITE)
                        .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY).setLightmapState(RenderStateShard.NO_LIGHTMAP).setOverlayState(RenderStateShard.OVERLAY).setDepthTestState(RenderStateShard.LEQUAL_DEPTH_TEST).createCompositeState(false)
        );
    }

    private final HumanoidArmorModel<Player> innerModel;
    private final HumanoidArmorModel<Player> outerModel;
    private Map<EquipmentSlot, ItemStack> gear;

    public IronmanArmorRenderer(Function<ModelLayerLocation, ModelPart> baker) {
        this.innerModel = new HumanoidArmorModel<>(baker.apply(ModelLayers.PLAYER_SLIM_INNER_ARMOR));
        this.outerModel = new HumanoidArmorModel<>(baker.apply(ModelLayers.PLAYER_SLIM_OUTER_ARMOR));
        this.gear = new HashMap<>();
    }

    public void setGear(Map<EquipmentSlot, ItemStack> gear) {
        this.gear = gear;
    }

    public void openCloseSuit(boolean open) {
        var stack = getClientRenderStack(this.gear);
        if (stack == null) return;
        SuitTag.update(stack, t -> {
            if (t.open != open) t.openCloseSuit();
        });
    }

    @Nullable
    public static ItemStack getClientRenderStack(@Nullable Map<EquipmentSlot, ItemStack> gear) {
        if (gear == null) return null;
        for (var slot : EquipmentSlot.values()) {
            if (slot.isArmor() && gear.getOrDefault(slot, ItemStack.EMPTY).getItem() instanceof IronmanArmorItem) return gear.get(slot);
        }
        return null;
    }

    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.mulPose(Axis.XP.rotationDegrees(180f));
        poseStack.translate(0f, -1.5f, 0f);
        this.renderArmorPiece(poseStack, buffer, EquipmentSlot.CHEST, packedLight, this.getArmorModel(EquipmentSlot.CHEST));
        this.renderArmorPiece(poseStack, buffer, EquipmentSlot.LEGS, packedLight, this.getArmorModel(EquipmentSlot.LEGS));
        this.renderArmorPiece(poseStack, buffer, EquipmentSlot.FEET, packedLight, this.getArmorModel(EquipmentSlot.FEET));
        this.renderArmorPiece(poseStack, buffer, EquipmentSlot.HEAD, packedLight, this.getArmorModel(EquipmentSlot.HEAD));
    }

    private void renderArmorPiece(PoseStack poseStack, MultiBufferSource buffer, EquipmentSlot slot, int packedLight, HumanoidArmorModel<Player> model) {
        var stack = this.gear.get(slot);
        var dataStack = getClientRenderStack(this.gear);
        this.setPartVisibility(model, slot);
        if (stack != null && stack.getItem() instanceof ICustomArmor armor && dataStack != null) renderModel(poseStack, buffer, packedLight, stack, dataStack, armor, slot, model);
    }

    public static void renderModel(PoseStack poseStack, MultiBufferSource buffer, int packedLight, LivingEntity entity, ICustomArmor armorItem, EquipmentSlot slot, HumanoidModel<?> model) {
        var locations = armorItem.getRenderLocation(entity, slot);

        // Armor Layer
        locations.get(0).ifPresent(location -> {
            var vertexConsumer = buffer.getBuffer(RenderType.armorCutoutNoCull(location));
            model.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0F);
        });

        // Glow Layer
        poseStack.scale(1.01f, 1.01f, 1.01f);
        locations.get(1).ifPresent(location -> {
            var glowConsumer = buffer.getBuffer(getGlowArmor(location));
            float pulse = 0.9f + Mth.sin(entity.tickCount * 0.2f) * 0.1f;
            model.renderToBuffer(poseStack, glowConsumer, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, pulse, pulse, pulse, 1f);
        });
    }

    private void renderModel(PoseStack poseStack, MultiBufferSource buffer, int packedLight, ItemStack stack, ItemStack dataStack, ICustomArmor armorItem, EquipmentSlot slot, HumanoidModel<?> model) {
        var locations = armorItem.getRenderLocation(stack, slot, dataStack);

        // Armor Layer
        locations.get(0).ifPresent(location -> {
            var vertexConsumer = buffer.getBuffer(RenderType.armorCutoutNoCull(location));
            model.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0F);
        });

        // Glow Layer
        poseStack.scale(1.01f, 1.01f, 1.01f);
        locations.get(1).ifPresent(location -> {
            var glowConsumer = buffer.getBuffer(getGlowArmor(location));
            model.renderToBuffer(poseStack, glowConsumer, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0F);
        });
    }

    private HumanoidArmorModel<Player> getArmorModel(EquipmentSlot slot) {
        return slot == EquipmentSlot.LEGS ? this.innerModel : this.outerModel;
    }

    protected void setPartVisibility(HumanoidArmorModel<Player> model, EquipmentSlot slot) {
        model.setAllVisible(false);
        model.young = false;
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
}
