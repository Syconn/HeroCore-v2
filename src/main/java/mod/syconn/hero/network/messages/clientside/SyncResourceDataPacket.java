package mod.syconn.hero.network.messages.clientside;

import dev.architectury.networking.NetworkManager;
import mod.syconn.hero.utils.server.SyncedResourceManager;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

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
