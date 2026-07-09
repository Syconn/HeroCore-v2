package mod.syconn.hero.core;

import mod.syconn.hero.utils.Constants;
import mod.syconn.hero.utils.server.ArmorType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.HashMap;
import java.util.Map;

public class ModArmors {

    public static final Map<ResourceLocation, ArmorMaterial> IRONMAN_TYPES = new HashMap<>(); // TODO PROBABLY COULD DATA SYSTEM THIS

    public static final ArmorMaterial MARK_2 = registerIronman(6, SoundEvents.ARMOR_EQUIP_IRON, Ingredient.of(Items.IRON_INGOT), "mark_2");

    static {
        registerIronman(10, SoundEvents.ARMOR_EQUIP_IRON, Ingredient.of(Items.IRON_BLOCK), "mark_5");
        registerIronman(15, SoundEvents.ARMOR_EQUIP_IRON, Ingredient.of(Items.GOLD_BLOCK), "mark_42");
        registerIronman(17, SoundEvents.ARMOR_EQUIP_IRON, Ingredient.of(Items.IRON_INGOT), "mark_43");
        registerIronman(23, SoundEvents.ARMOR_EQUIP_IRON, Ingredient.of(Items.IRON_INGOT), "war_machine");
    }

    private static ArmorMaterial registerIronman(int durabilityModifier, SoundEvent equipSound, Ingredient repair, String name) {
        var material = new ArmorType(durabilityModifier, equipSound, repair, name);
        IRONMAN_TYPES.put(Constants.withId("suits/" + name), material);
        return material;
    }
}
