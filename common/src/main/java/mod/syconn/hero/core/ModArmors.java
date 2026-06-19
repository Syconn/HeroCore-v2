package mod.syconn.hero.core;

import mod.syconn.hero.utils.server.ArmorType;
import net.minecraft.Util;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;

public class ModArmors {

    public static final ArmorMaterial MARK_2 = new ArmorType(6, SoundEvents.ARMOR_EQUIP_IRON, Ingredient.of(Items.IRON_INGOT), "mark_2");
    public static final ArmorMaterial MARK_5 = new ArmorType(10, SoundEvents.ARMOR_EQUIP_IRON, Ingredient.of(Items.IRON_BLOCK), "mark_5");
    public static final ArmorMaterial MARK_42 = new ArmorType(15, SoundEvents.ARMOR_EQUIP_IRON, Ingredient.of(Items.GOLD_BLOCK), "mark_42");
    public static final ArmorMaterial MARK_43 = new ArmorType(17, SoundEvents.ARMOR_EQUIP_IRON, Ingredient.of(Items.IRON_INGOT), "mark_43");
    public static final ArmorMaterial WAR_MACHINE = new ArmorType(23, SoundEvents.ARMOR_EQUIP_IRON, Ingredient.of(Items.IRON_INGOT), "war_machine");
}
