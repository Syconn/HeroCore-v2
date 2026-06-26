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

    private static final Map<String, Map<Byte, ResourceLocation>> HELMET_OPEN = new HashMap<>();
    private static final Map<String, Map<Byte, ResourceLocation>> SUIT_OPEN_BACK = new HashMap<>();
    private static final Map<String, Map<Byte, ResourceLocation>> SUIT_OPEN_BACK_PANTS = new HashMap<>();

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
        createModels(armorPath(stack, armor, slot), armor.getMaterialName(stack), slot);
        if (slot == EquipmentSlot.LEGS) return Optional.of(getOrDefaultPants(stack, armor));
        return Optional.of(getOrDefault(stack, armor, slot));
    }

    private static ResourceLocation getOrDefault(ItemStack stack, ICustomArmor armor, EquipmentSlot slot) {
        if (stack.getItem() instanceof ICustomArmor) {
            var suitOpen = SuitTag.getOrCreate(stack).getOpenSuit();
            if (suitOpen != 0 && SUIT_OPEN_BACK.containsKey(armor.getMaterialName(stack)) && SUIT_OPEN_BACK.get(armor.getMaterialName(stack)).containsKey(suitOpen)) return SUIT_OPEN_BACK.get(armor.getMaterialName(stack)).get(suitOpen);

            var helmetFrame = SuitTag.getOrCreate(stack).getHelmetFrame();
            if (HELMET_OPEN.containsKey(armor.getMaterialName(stack)) && HELMET_OPEN.get(armor.getMaterialName(stack)).containsKey(helmetFrame)) return HELMET_OPEN.get(armor.getMaterialName(stack)).get(helmetFrame);
        }
        return armorPath(stack, armor, slot);
    }

    private static ResourceLocation getOrDefaultPants(ItemStack stack, ICustomArmor armor) {
        var suitOpen = SuitTag.getOrCreate(stack).getOpenSuit();
        if (stack.getItem() instanceof ICustomArmor) if (SUIT_OPEN_BACK_PANTS.containsKey(armor.getMaterialName(stack)) && SUIT_OPEN_BACK_PANTS.get(armor.getMaterialName(stack)).containsKey(suitOpen)) return SUIT_OPEN_BACK_PANTS.get(armor.getMaterialName(stack)).get(suitOpen);
        return armorPath(stack, armor, EquipmentSlot.LEGS);
    }

    private static void createModels(ResourceLocation path, String name, EquipmentSlot slot) {
        if (!HELMET_OPEN.containsKey(name) || !SUIT_OPEN_BACK.containsKey(name) || !SUIT_OPEN_BACK_PANTS.containsKey(name)) {
            ResourceUtil.loadResource(path).ifPresent(texture -> {
                if (!HELMET_OPEN.containsKey(name) && slot == EquipmentSlot.HEAD) {
                    for (byte i = 0; i < 7; i++) {
                        var modify = texture.mappedCopy(op -> op);
                        ImageUtil.translate(modify, 9, 9, 6, 6, 0, -i);
                        ImageUtil.translate(modify, 41, 9, 6, 6, 0, -i);
                        HELMET_OPEN.put(name, MapUtil.add(i, ResourceUtil.registerOrGet(name + "open" + i, new DynamicTexture(modify)), HELMET_OPEN.get(name)));
                    }
                }

                if (!SUIT_OPEN_BACK.containsKey(name) && slot != EquipmentSlot.LEGS) {
                    var image = texture.mappedCopy(op -> op);
                    for (byte i = 0; i < 7; i++) {
                        // Helmet
                        ImageUtil.translateBoundedCap(image, 24, 9, 4, 7, -i, 0, 3);
                        ImageUtil.translateBoundedCap(image, 28, 9, 4, 7, i, 0, 3);
                        ImageUtil.translateBoundedCap(image, 16, 0, 8, 8, 0, i, 5);
                        ImageUtil.translateBoundedCap(image, 56, 8, 8, 6, 0, -i, 4);
                        ImageUtil.cut(image, 9, 10, 6, 3);

                        // Chestplate
                        ImageUtil.translateBoundedCap(image, 32, 20, 4, 12, -i, 0, 3);
                        ImageUtil.translateBoundedCap(image, 36, 20, 4, 12, i, 0, 3);
                        ImageUtil.translateBoundedCap(image, 52, 20, 2, 12, -i, 0, 1);
                        if (i > 1) ImageUtil.cut(image, 53, 20, 2, 12);
                        if (i > 2) ImageUtil.cut(image, 52, 20, 4, 12);

                        // Feet
                        if (i != 0) ImageUtil.cut(image, 12, 26, 4, 5);
                        SUIT_OPEN_BACK.put(name, MapUtil.add(i, ResourceUtil.registerOrGet(name + "suit" + i, new DynamicTexture(image)), SUIT_OPEN_BACK.get(name)));
                    }
                }

                if (!SUIT_OPEN_BACK_PANTS.containsKey(name) && slot == EquipmentSlot.LEGS) { // TODO NOT RIGHT TEXTURE
                    var image = texture.mappedCopy(op -> op);
                    for (byte i = 0; i < 7; i++) {
                        ImageUtil.translateBoundedCap(image, 32, 27, 4, 5, -i, 0, 2);
                        ImageUtil.translateBoundedCap(image, 36, 27, 4, 5, i, 0, 2);
                        if (i > 1) ImageUtil.cut(image, 13, 20, 2, 8);
                        if (i > 2) ImageUtil.cut(image, 12, 20, 4, 8);
                        SUIT_OPEN_BACK_PANTS.put(name, MapUtil.add(i, ResourceUtil.registerOrGet(name + "legs" + i, new DynamicTexture(image)), SUIT_OPEN_BACK_PANTS.get(name)));
                    }
                }
            });
        }
    }

    private static ResourceLocation armorPath(ItemStack stack, ICustomArmor armor, EquipmentSlot slot) {
        return Constants.withId("textures/armor/" + armor.getMaterialName(stack) + "/" + getLayer(slot) + ".png");
    }

    private static String getLayer(EquipmentSlot slot) {
        return slot == EquipmentSlot.LEGS ? "layer_2" : "layer_1";
    }
}
