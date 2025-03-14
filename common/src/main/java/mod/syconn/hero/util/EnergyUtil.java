package mod.syconn.hero.util;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class EnergyUtil {
    
    private static final String ENERGY = "energy";
    private static final String MAX = "max_energy";

    public static int getEnergy(ItemStack stack) {
        if (!stack.getOrCreateTag().contains(ENERGY))
            stack.getOrCreateTag().putInt(ENERGY, getMax(stack));
        return stack.getOrCreateTag().getInt(ENERGY);
    }

    public static int getEnergy(Player player, InteractionHand hand) {
        return getEnergy(player.getItemInHand(hand));
    }

    public static int getMax(ItemStack stack) {
        if (!stack.getOrCreateTag().contains(MAX))
            stack.getOrCreateTag().putInt(MAX, 100);
        return stack.getOrCreateTag().getInt(MAX);
    }

    public static int getMax(Player player, InteractionHand hand) {
        return getEnergy(player.getItemInHand(hand));
    }
    
    public static int modifyEnergy(ItemStack stack, int amount) {
        stack.getOrCreateTag().putInt(ENERGY, getEnergy(stack) + amount);
        return getEnergy(stack);
    }

    public static int modifyEnergy(Player player, InteractionHand hand, int amount) {
        return modifyEnergy(player.getItemInHand(hand), amount);
    }
    
    public static void setMax(ItemStack stack, int max) {
        stack.getOrCreateTag().putInt(MAX, max);
    }

    public static void setMax(Player player, InteractionHand hand, int max) {
        setMax(player.getItemInHand(hand), max);
    }

    public static void lowerSuitEnergy(Player player) {
        modifyEnergy(player.getItemBySlot(EquipmentSlot.HEAD), -1);
        modifyEnergy(player.getItemBySlot(EquipmentSlot.CHEST), -1);
        modifyEnergy(player.getItemBySlot(EquipmentSlot.LEGS), -1);
        modifyEnergy(player.getItemBySlot(EquipmentSlot.FEET), -1);
    }
    
    public static int getSuitEnergy(Player player) {
        return getEnergy(player.getItemBySlot(EquipmentSlot.HEAD)) + getEnergy(player.getItemBySlot(EquipmentSlot.CHEST)) +
                getEnergy(player.getItemBySlot(EquipmentSlot.LEGS)) + getEnergy(player.getItemBySlot(EquipmentSlot.FEET));
    }

    public static int getSuitEnergyMax(Player player) {
        return getMax(player.getItemBySlot(EquipmentSlot.HEAD)) + getMax(player.getItemBySlot(EquipmentSlot.CHEST)) +
                getMax(player.getItemBySlot(EquipmentSlot.LEGS)) + getMax(player.getItemBySlot(EquipmentSlot.FEET));
    }

    public static int getSuitPercentage(Player player) {
        return (int) ((float) getSuitEnergy(player) / getSuitEnergyMax(player) * 100);
    }
}
