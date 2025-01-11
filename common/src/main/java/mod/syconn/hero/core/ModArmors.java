package mod.syconn.hero.core;

import net.minecraft.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.EnumMap;

public class ModArmors {

    public static final MARK_42_Material MARK_42 = new MARK_42_Material();

    public static class MARK_42_Material implements ArmorMaterial {
        public int getDurabilityForType(ArmorItem.Type type) {
            return HEALTH_FUNCTION_FOR_TYPE.get(type) * 15;
        }

        public int getDefenseForType(ArmorItem.Type type) {
            return switch (type) {
                case HELMET, BOOTS -> 2;
                case CHESTPLATE -> 6;
                case LEGGINGS -> 5;
            };
        }

        public int getEnchantmentValue() {
            return 9;
        }

        public SoundEvent getEquipSound() {
            return SoundEvents.ARMOR_EQUIP_IRON;
        }

        public Ingredient getRepairIngredient() {
            return Ingredient.of(ModTags.TITANIUM_PLATE);
        }

        public String getName() {
            return "mark_42";
        }

        public float getToughness() {
            return 0.0F;
        }

        public float getKnockbackResistance() {
            return 0.0F;
        }
    }
    
    @SuppressWarnings("unchecked")
    private static final EnumMap<ArmorItem.Type, Integer> HEALTH_FUNCTION_FOR_TYPE = Util.make(new EnumMap(ArmorItem.Type.class), enumMap -> {
        enumMap.put(ArmorItem.Type.BOOTS, 13);
        enumMap.put(ArmorItem.Type.LEGGINGS, 15);
        enumMap.put(ArmorItem.Type.CHESTPLATE, 16);
        enumMap.put(ArmorItem.Type.HELMET, 11);
    });
}
