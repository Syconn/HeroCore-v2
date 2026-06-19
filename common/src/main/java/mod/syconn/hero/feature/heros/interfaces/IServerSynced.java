package mod.syconn.hero.feature.heros.interfaces;

import mod.syconn.hero.network.Network;
import mod.syconn.hero.network.messages.serverside.SaveAbilityDataPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;

public interface IServerSynced {

    CompoundTag writeAdditionalSync();
    void additionalSync(CompoundTag tag);

    default void syncPlayer(Player player) {}
    default void serverTick(Player player) {}

    default void readAdditionSync(CompoundTag tag) {
        if (!tag.isEmpty()) additionalSync(tag);
    }

    default void sendSyncData() {
        if (this instanceof IHeroAbility type)
            Network.CHANNEL.sendToServer(new SaveAbilityDataPacket(type.heroType(), type.id(), new CompoundTag(), writeAdditionalSync()));
    }
}
