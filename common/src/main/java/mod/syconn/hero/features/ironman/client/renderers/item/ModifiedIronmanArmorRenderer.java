package mod.syconn.hero.features.ironman.client.renderers.item;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.PoseStack;
import mod.syconn.hero.features.ironman.server.data.SuitTag;
import mod.syconn.hero.utils.Constants;
import mod.syconn.hero.utils.generic.ImageUtil;
import mod.syconn.hero.utils.generic.MapUtil;
import mod.syconn.hero.utils.generic.ResourceUtil;
import mod.syconn.hero.utils.interfaces.ICustomArmor;
import mod.syconn.hero.utils.interfaces.IModifiedItemRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ModifiedIronmanArmorRenderer implements IModifiedItemRenderer {

    private static final Map<String, Map<Byte, ResourceLocation>> ARMOR = new HashMap<>();

    @Override
    public boolean render(LivingEntity entity, ItemStack stack, ItemDisplayContext renderMode, boolean leftHanded, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay, BakedModel backupModel) {
        poseStack.pushPose();

        Lighting.setupForFlatItems();
        var type = stack.getItem() instanceof ArmorItem armor ? armor.getType().getName() : "";
        var model = IModifiedItemRenderer.getModel(SuitTag.getOrCreate(stack).model.withSuffix("_" + type), backupModel);
        var transforms = model.getTransforms();
        transforms.getTransform(renderMode).apply(leftHanded, poseStack);
        renderItemModel(stack, poseStack, buffer, light, overlay, model);

        poseStack.popPose();
        return true;
    }

    public static Optional<ResourceLocation> getRenderLocation(ICustomArmor armor, ItemStack stack, EquipmentSlot slot) {
        createHelmetForModel(stack, armor);
        if (slot == EquipmentSlot.LEGS) return Optional.of(ModifiedIronmanArmorRenderer.armorPath(armor, stack, slot));
        return Optional.of(getOrDefault(stack, armor));
    }

    private static ResourceLocation getOrDefault(ItemStack stack, ICustomArmor armor) {
        if (stack.getItem() instanceof ICustomArmor) {
            var state = SuitTag.getOrCreate(stack).getRenderFrame();
            if (ARMOR.containsKey(armor.getMaterialName(stack)) && ARMOR.get(armor.getMaterialName(stack)).containsKey(state)) return ARMOR.get(armor.getMaterialName(stack)).get(state);
        }
        return ModifiedIronmanArmorRenderer.armorPath(armor, stack, EquipmentSlot.HEAD);
    }

    private static String getLayer(EquipmentSlot slot) {
        return slot == EquipmentSlot.LEGS ? "layer_2" : "layer_1";
    }

    private static void createHelmetForModel(ItemStack stack, ICustomArmor armor) {
        if (!ARMOR.containsKey(armor.getMaterialName(stack))) {
            final var helmetLayer = ResourceUtil.loadResource(armorPath(armor, stack, EquipmentSlot.HEAD));
            if (helmetLayer.isPresent()) {
                if (!ARMOR.containsKey(armor.getMaterialName(stack))) {
                    for (byte i = 0; i < 7; i++) {
                        var modify = helmetLayer.get().mappedCopy(op -> op);
                        ImageUtil.translate(modify, 9, 9, 6, 6, 0, -i);
                        ImageUtil.translate(modify, 41, 9, 6, 6, 0, -i);
                        var location = ResourceUtil.registerOrGet(armor.getMaterialName(stack) + i, new DynamicTexture(modify));
                        ARMOR.put(armor.getMaterialName(stack), MapUtil.add(i, location, ARMOR.get(armor.getMaterialName(stack))));
                    }
                }
            }
        }
    }

    private static ResourceLocation armorPath(ICustomArmor armor, ItemStack stack, EquipmentSlot slot) {
        return Constants.withId("textures/armor/" + armor.getMaterialName(stack) + "/" + getLayer(slot) + ".png");
    }
}
