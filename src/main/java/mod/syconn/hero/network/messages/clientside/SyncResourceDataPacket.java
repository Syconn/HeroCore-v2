package mod.syconn.hero.network.messages.clientside;

import dev.architectury.networking.NetworkManager;
import mod.syconn.hero.utils.server.SyncedResourceManager;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import java.util.function.Supplier;

//? if >=1.21.1 {
/*import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
*///? }

//? if 1.20.1 {
public class SyncResourceDataPacket {

    private final ResourceLocation id;
    private final CompoundTag data;

    public SyncResourceDataPacket(ResourceLocation id, CompoundTag data) {
        this.id = id;
        this.data = data;
    }

    public SyncResourceDataPacket(FriendlyByteBuf buf) {
        this.id = buf.readResourceLocation();
        this.data = buf.readNbt();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeResourceLocation(this.id);
        buf.writeNbt(this.data);
    }

    public void apply(Supplier<NetworkManager.PacketContext> context) {
        context.get().queue(() -> {
            if (context.get().getPlayer() != null) {
                SyncedResourceManager.ISyncedData sync = SyncedResourceManager.getLoginDataSupplier(this.id);
                sync.readData(this.data);
            }
        });
    }
}
//? } else {
/*public record SyncResourceDataPacket(ResourceLocation id, CompoundTag data) {
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncResourceDataPacket> STREAM_CODEC =
            StreamCodec.composite(
                    ResourceLocation.STREAM_CODEC,
                    SyncResourceDataPacket::id,
                    ByteBufCodecs.COMPOUND_TAG,
                    SyncResourceDataPacket::data,
                    SyncResourceDataPacket::new
            );
}
*///? }
