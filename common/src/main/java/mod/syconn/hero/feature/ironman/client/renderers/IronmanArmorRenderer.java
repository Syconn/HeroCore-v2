package mod.syconn.hero.feature.ironman.client.renderers;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import mod.syconn.hero.feature.heros.interfaces.IHeroHolder;
import mod.syconn.hero.feature.ironman.Ironman;
import mod.syconn.hero.feature.ironman.abilities.FlipHelmetAbility;
import mod.syconn.hero.feature.ironman.server.data.SuitTag;
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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class IronmanArmorRenderer implements IModifiedItemRenderer {

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

    public static Optional<ResourceLocation> getRenderLocation(LivingEntity entity, ICustomArmor armor, EquipmentSlot slot) {
        var stack = entity.getItemBySlot(slot);
        createHelmetForModel(stack, armor);
        if (slot == EquipmentSlot.LEGS) return Optional.of(IronmanArmorRenderer.armorPath(armor, stack, slot));
        return Optional.of(getOrDefault(entity, stack, armor));
    }

    private static ResourceLocation getOrDefault(LivingEntity entity, ItemStack stack, ICustomArmor armor) {
        if (entity instanceof IHeroHolder holder && entity instanceof Player player) {
            var state = holder.hero$getManager().getType(Ironman.class).getAbility(FlipHelmetAbility.class).getRenderFrame(player);
            if (ARMOR.containsKey(armor.getMaterialName(stack)) && ARMOR.get(armor.getMaterialName(stack)).containsKey(state)) return ARMOR.get(armor.getMaterialName(stack)).get(state);
        }
        return IronmanArmorRenderer.armorPath(armor, stack, EquipmentSlot.HEAD);
    }

    private static String getLayer(EquipmentSlot slot) {
        if (slot == EquipmentSlot.LEGS) return "layer_2";
        return "layer_1";
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
