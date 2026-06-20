package mod.syconn.hero.feature.ironman.client.renderers;

import mod.syconn.hero.feature.heros.interfaces.IHeroHolder;
import mod.syconn.hero.feature.ironman.Ironman;
import mod.syconn.hero.feature.ironman.abilities.FlipHelmetAbility;
import mod.syconn.hero.utils.Constants;
import mod.syconn.hero.utils.generic.ImageUtil;
import mod.syconn.hero.utils.generic.MapUtil;
import mod.syconn.hero.utils.generic.ResourceUtil;
import mod.syconn.hero.utils.interfaces.ICustomArmor;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class IronmanArmorRenderer {

    private static final Map<String, Map<Byte, ResourceLocation>> ARMOR = new HashMap<>();

    public static Optional<ResourceLocation> getRenderLocation(LivingEntity entity, ICustomArmor armor, EquipmentSlot slot) {
        createHelmetForModel(armor);
        if (slot == EquipmentSlot.LEGS) return Optional.of(IronmanArmorRenderer.armorPath(armor, slot));
        return Optional.of(getOrDefault(entity, armor));
    }

    private static ResourceLocation getOrDefault(LivingEntity entity, ICustomArmor armor) {
        if (entity instanceof IHeroHolder holder && entity instanceof Player player) {
            var state = holder.hero$getManager().getType(Ironman.class).getAbility(FlipHelmetAbility.class).getRenderFrame(player);
            if (ARMOR.containsKey(armor.getName()) && ARMOR.get(armor.getName()).containsKey(state)) return ARMOR.get(armor.getName()).get(state);
        }
        return IronmanArmorRenderer.armorPath(armor, EquipmentSlot.HEAD);
    }

    private static String getLayer(EquipmentSlot slot) {
        if (slot == EquipmentSlot.LEGS) return "layer_2";
        return "layer_1";
    }

    private static void createHelmetForModel(ICustomArmor armor) {
        if (!ARMOR.containsKey(armor.getName())) {
            final var helmetLayer = ResourceUtil.loadResource(armorPath(armor, EquipmentSlot.HEAD));
            if (helmetLayer.isPresent()) {
                if (!ARMOR.containsKey(armor.getName())) {
                    for (byte i = 0; i < 7; i++) {
                        var modify = helmetLayer.get().mappedCopy(op -> op);
                        ImageUtil.translate(modify, 9, 9, 6, 6, 0, -i);
                        ImageUtil.translate(modify, 41, 9, 6, 6, 0, -i);
                        var location = ResourceUtil.registerOrGet(armor.getName() + i, new DynamicTexture(modify));
                        ARMOR.put(armor.getName(), MapUtil.add(i, location, ARMOR.get(armor.getName())));
                    }
                }
            }
        }
    }

    private static ResourceLocation armorPath(ICustomArmor armor, EquipmentSlot slot) {
        return Constants.withId("textures/armor/" + armor.getName() + "/" + getLayer(slot) + ".png");
    }
}
