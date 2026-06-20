package mod.syconn.hero.feature.heros.interfaces;

import mod.syconn.hero.network.Network;
import mod.syconn.hero.network.messages.clientside.SyncClientPacket;
import mod.syconn.hero.network.messages.serverside.SaveAbilityDataPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public interface IServerSynced {

    CompoundTag writeAdditionalSync();

    default void syncPlayer(Player player) {}
    default void serverTick(Player player) {}

    default void additionalSync(Player player, CompoundTag tag) {
        sendTrackingData(player);
    }

    default void readAdditionalSync(Player player, CompoundTag tag) {
        additionalSync(player, tag);
    }

    default void sendClientSyncData(Player player) {
        if (this instanceof IHeroAbility type) Network.CHANNEL.sendToServer(new SaveAbilityDataPacket(player.getUUID(), type.heroType(), type.id(), writeAdditionalSync(), true));
    }

    default void sendTrackingData(Player player) {
        if (player instanceof ServerPlayer sp) syncToTrackingClients(sp);
    }

    default void syncToTrackingClients(ServerPlayer sp) {
        if (this instanceof IHeroAbility type) {
            ServerLevel level = (ServerLevel) sp.level();
            for (ServerPlayer viewer : level.players()) Network.CHANNEL.sendToPlayer(viewer, new SyncClientPacket(sp.getUUID(), type.heroType(), type.id(), writeAdditionalSync()));
        }
    }
}
