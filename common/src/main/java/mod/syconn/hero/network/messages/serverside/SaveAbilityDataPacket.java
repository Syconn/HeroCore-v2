package mod.syconn.hero.network.messages.serverside;

import dev.architectury.networking.NetworkManager;
import mod.syconn.hero.feature.heros.interfaces.IHeroHolder;
import mod.syconn.hero.feature.heros.interfaces.IServerSynced;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;
import java.util.function.Supplier;

public class SaveAbilityDataPacket {

    private final UUID target;
    private final ResourceLocation heroType;
    private final ResourceLocation ability;
    private final CompoundTag data;
    private final boolean serverData;

    public SaveAbilityDataPacket(UUID target, ResourceLocation heroType, ResourceLocation ability, CompoundTag data, boolean serverData) {
        this.target = target;
        this.heroType = heroType;
        this.ability = ability;
        this.data = data;
        this.serverData = serverData;
    }

    public SaveAbilityDataPacket(FriendlyByteBuf buf) {
        this.target = buf.readUUID();
        this.heroType = buf.readResourceLocation();
        this.ability = buf.readResourceLocation();
        this.data = buf.readNbt();
        this.serverData = buf.readBoolean();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUUID(this.target);
        buf.writeResourceLocation(this.heroType);
        buf.writeResourceLocation(this.ability);
        buf.writeNbt(this.data);
        buf.writeBoolean(this.serverData);
    }

    public void apply(Supplier<NetworkManager.PacketContext> context) {
        context.get().queue(() -> {
            var sender = (ServerPlayer) context.get().getPlayer();
            var target = sender.server.getPlayerList().getPlayer(this.target);
            if (target == null) return;
            if (!(target instanceof IHeroHolder holder)) return;

            var type = holder.hero$getManager().getType(this.heroType);
            if (type == null) return;
            var ability = type.getAbility(this.ability);
            if (ability == null) return;

            if (!serverData) ability.readData(target, this.data);
            if (serverData && ability instanceof IServerSynced synced) synced.readAdditionalSync(sender, this.data);
        });
    }
}
