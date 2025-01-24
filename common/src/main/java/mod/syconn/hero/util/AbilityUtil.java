package mod.syconn.hero.util;

import mod.syconn.hero.core.ModItems;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class AbilityUtil {

    public static boolean canUseIronManPowers(Player player) {
        NonNullList<ItemStack> inv = player.getInventory().armor;
        return inv.get(0).is(ModItems.MARK_42_BOOTS.get()) && inv.get(1).is(ModItems.MARK_42_LEGGINGS.get()) && inv.get(2).is(ModItems.MARK_42_CHESTPLATE.get()) && inv.get(3).is(ModItems.MARK_42_HELMET.get());
    }

    public static boolean canInteractWithIronManHelmet(Player player) {
        return player.getItemBySlot(EquipmentSlot.HEAD).is(ModItems.MARK_42_HELMET.get());
    }

    public static List<ItemStack> missingIronManItems(Player player) {
        List<ItemStack> missing = new ArrayList<>();
        NonNullList<ItemStack> inv = player.getInventory().armor;
        if (!inv.get(0).is(ModItems.MARK_42_BOOTS.get())) missing.add(new ItemStack(ModItems.MARK_42_BOOTS.get()));
        if (!inv.get(1).is(ModItems.MARK_42_LEGGINGS.get())) missing.add(new ItemStack(ModItems.MARK_42_LEGGINGS.get()));
        if (!inv.get(2).is(ModItems.MARK_42_CHESTPLATE.get())) missing.add(new ItemStack(ModItems.MARK_42_CHESTPLATE.get()));
        if (!inv.get(3).is(ModItems.MARK_42_HELMET.get())) missing.add(new ItemStack(ModItems.MARK_42_HELMET.get()));
        return missing;
    }

    public static boolean canUseThorPowers(Player player) {
        return player.getInventory().contains(new ItemStack(ModItems.MJOLNIR.get()));
    }

    public static List<ItemStack> missingThorItems(Player player) {
        return canUseThorPowers(player) ? List.of() : List.of(new ItemStack(ModItems.MJOLNIR.get()));
    }

    public static boolean isHolding(Player player, Item item) {
        return player.getItemInHand(InteractionHand.MAIN_HAND).is(item);
    }

    public static HeroTypes getHeroType(Player player) {
        return HeroTypes.read(((PersistentData) player).getPersistentData());
    }

    public static HeroTypes setHeroType(Player player, HeroTypes heroType) {
        ((PersistentData) player).updatePersistentData(player, heroType::write);
        return heroType;
    }

    public static boolean useSpecificPower(Player player, HeroTypes power) {
        canUseOrUpdatePower(player);
        return getHeroType(player) == power;
    }

    public static boolean canUseOrUpdatePower(Player player) {
        HeroTypes selectedPower = getHeroType(player);
        if (selectedPower.canUse(player)) return true;
        player.displayClientMessage(Component.literal("Missing Required Gear to use Power"), true);
        setHeroType(player, HeroTypes.NONE);
        return false;
    }
}
