package mod.syconn.hero.feature.heros.interfaces;

import mod.syconn.hero.network.Network;
import mod.syconn.hero.network.messages.serverside.SaveAbilityDataPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface IHeroAbility {

    ResourceLocation heroType();
    ResourceLocation id();
    void clientTick(Player player);
    CompoundTag writeData(Player player);
    void readData(Player player, CompoundTag tag);

    default boolean usable(Player player) {
        return false;
    }

    default CompoundTag getStackTag(ItemStack stack) {
        return stack.getOrCreateTag().getCompound(this.id().getPath());
    }

    default void putStackTag(ItemStack stack, CompoundTag tag) {
        stack.getOrCreateTag().put(this.id().getPath(), tag);
    }

    default void sendAllData(Player player) {
        Network.CHANNEL.sendToServer(new SaveAbilityDataPacket(player.getUUID(), this.heroType(), this.id(), this.writeData(player), false));
    }

    default void sendSpecificData(Player player, CompoundTag data) {
        Network.CHANNEL.sendToServer(new SaveAbilityDataPacket(player.getUUID(), this.heroType(), this.id(), data, false));
    }
}
