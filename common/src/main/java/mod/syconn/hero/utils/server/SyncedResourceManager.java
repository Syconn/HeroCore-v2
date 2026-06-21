package mod.syconn.hero.utils.server;

import mod.syconn.hero.network.Network;
import mod.syconn.hero.network.messages.clientside.SyncResourceDataPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashMap;
import java.util.Map;

public class SyncedResourceManager {

    private static final Map<ResourceLocation, ISyncedData> SYNCED_DATA = new HashMap<>();

    public static void register(ISyncedData syncResources) {
        SYNCED_DATA.putIfAbsent(syncResources.getId(), syncResources);
    }

    public static void handleJoin(ServerPlayer player) {
        SYNCED_DATA.forEach((id, data) -> Network.CHANNEL.sendToPlayer(player, new SyncResourceDataPacket(id, data.writeData())));
    }

    public static ISyncedData getLoginDataSupplier(ResourceLocation id) {
        return SYNCED_DATA.get(id);
    }

    public interface ISyncedData {
        ResourceLocation getId();
        CompoundTag writeData();
        void readData(CompoundTag tag);
    }
}
