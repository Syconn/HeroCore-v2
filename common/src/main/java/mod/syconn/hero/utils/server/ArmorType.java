package mod.syconn.hero.utils.server;

import mod.syconn.hero.utils.generic.MapUtil;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.HashMap;

public class ArmorType implements ArmorMaterial {

    private final int durabilityModifier;
    private final SoundEvent equipSound;
    private final Ingredient repair;
    private final String name;

    public ArmorType(int durabilityModifier, SoundEvent equipSound, Ingredient repair, String name) {
        this.durabilityModifier = durabilityModifier;
        this.equipSound = equipSound;
        this.repair = repair;
        this.name = name;
    }

    @Override
    public int getDurabilityForType(ArmorItem.Type type) {
        return ((int) MapUtil.makeAndGet(new HashMap<>(), map -> {
            map.put(ArmorItem.Type.BOOTS, 13);
            map.put(ArmorItem.Type.LEGGINGS, 15);
            map.put(ArmorItem.Type.CHESTPLATE, 16);
            map.put(ArmorItem.Type.HELMET, 11);
        }, type)) * this.durabilityModifier;
    }

    @Override
    public int getDefenseForType(ArmorItem.Type type) {
        return switch (type) {
            case HELMET, BOOTS -> 2;
            case CHESTPLATE -> 6;
            case LEGGINGS -> 5;
        };
    }

    @Override
    public int getEnchantmentValue() {
        return 9;
    }

    @Override
    public SoundEvent getEquipSound() {
        return this.equipSound;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return this.repair;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public float getToughness() {
        return 0f;
    }

    @Override
    public float getKnockbackResistance() {
        return 0f;
    }
}
