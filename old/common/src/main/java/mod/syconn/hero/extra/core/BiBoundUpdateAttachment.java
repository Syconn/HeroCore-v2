package mod.syconn.hero.extra.core;

import mod.syconn.hero.extra.platform.Services;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;

public record BiBoundUpdateAttachment(String id, CompoundTag tag) {

    public static final StreamCodec<RegistryFriendlyByteBuf, BiBoundUpdateAttachment> CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, BiBoundUpdateAttachment::id, ByteBufCodecs.COMPOUND_TAG, BiBoundUpdateAttachment::tag, BiBoundUpdateAttachment::new);

    public static void handle(BiBoundUpdateAttachment message, Player player) {
        Services.ATTACHED_DATA.set(message.id, Services.ATTACHED_DATA.get(message.id, player).readSyncedData(message.tag), player);
    }
}
