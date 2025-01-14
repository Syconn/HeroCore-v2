package mod.syconn.hero.util;

import mod.syconn.hero.core.ModItems;
import net.minecraft.core.NonNullList;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ItemUtil {

    public static boolean isWearingIronManSuit(Player player) {
        NonNullList<ItemStack> inv = player.getInventory().armor;
        return inv.get(0).is(ModItems.MARK_42_BOOTS.get()) && inv.get(1).is(ModItems.MARK_42_LEGGINGS.get()) && inv.get(2).is(ModItems.MARK_42_CHESTPLATE.get()) && inv.get(3).is(ModItems.MARK_42_HELMET.get());
    }

    public static boolean isHolding(Player player, Item item) {
        return player.getItemInHand(InteractionHand.MAIN_HAND).is(item) || player.getItemInHand(InteractionHand.OFF_HAND).is(item);
    }
}
