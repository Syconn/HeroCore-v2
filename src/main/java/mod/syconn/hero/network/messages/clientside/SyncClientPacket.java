package mod.syconn.hero.network.messages.clientside;

import dev.architectury.networking.NetworkManager;
import mod.syconn.hero.features.heros.interfaces.IHeroHolder;
import mod.syconn.hero.features.heros.interfaces.IServerSynced;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.UUID;
import java.util.function.Supplier;

public class SyncClientPacket {

    private final UUID target;
    private final ResourceLocation heroType;
    private final ResourceLocation ability;
    private final CompoundTag data;

    public SyncClientPacket(UUID target, ResourceLocation heroType, ResourceLocation ability, CompoundTag data) {
        this.target = target;
        this.heroType = heroType;
        this.ability = ability;
        this.data = data;
    }

    public SyncClientPacket(FriendlyByteBuf buf) {
        this.target = buf.readUUID();
        this.heroType = buf.readResourceLocation();
        this.ability = buf.readResourceLocation();
        this.data = buf.readNbt();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUUID(this.target);
        buf.writeResourceLocation(this.heroType);
        buf.writeResourceLocation(this.ability);
        buf.writeNbt(this.data);
    }

    public void apply(Supplier<NetworkManager.PacketContext> context) {
        context.get().queue(() -> {
            var level = Minecraft.getInstance().level;
            if (level == null) return;
            var player = level.getPlayerByUUID(target);
            if (player == null) return;
            if (!(player instanceof IHeroHolder holder)) return;

            var type = holder.hero$getManager().getType(this.heroType);
            if (type == null) return;
            var ability = type.getAbility(this.ability);
            if (ability == null) return;

//            System.out.println(context.get().getPlayer() + " received packet for " + player + " " + data);
            if (ability instanceof IServerSynced synced) synced.readAdditionalSync(player, this.data);
        });
    }
}
