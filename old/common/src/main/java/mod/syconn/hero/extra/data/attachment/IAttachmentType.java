package mod.syconn.hero.extra.data.attachment;

import com.mojang.serialization.Codec;
import mod.syconn.hero.extra.core.BiBoundUpdateAttachment;
import mod.syconn.hero.network.Network;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public interface IAttachmentType<T> {
    
    Codec<T> codec();
    CompoundTag writeSyncedData();
    T readSyncedData(CompoundTag nbt);
    default String id() {
        return getClass().getSimpleName().toLowerCase();
    }
    default boolean copyOnDeath() {
        return true;
    }
    default void sync(Player player) {
        BiBoundUpdateAttachment packet = new BiBoundUpdateAttachment(id(), writeSyncedData());
        if (player instanceof ServerPlayer serverPlayer) Network.sendToPlayer(packet, serverPlayer);
        else Network.sendToServer(packet);
    }
}
