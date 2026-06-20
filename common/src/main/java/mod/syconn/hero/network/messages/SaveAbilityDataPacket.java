package mod.syconn.hero.network.messages;

import dev.architectury.networking.NetworkManager;
import mod.syconn.hero.feature.heros.interfaces.IHeroHolder;
import mod.syconn.hero.feature.heros.interfaces.IServerSynced;
import mod.syconn.hero.network.Network;
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
    private final CompoundTag additional;

    public SaveAbilityDataPacket(UUID target, ResourceLocation heroType, ResourceLocation ability, CompoundTag data, CompoundTag additional) {
        this.target = target;
        this.heroType = heroType;
        this.ability = ability;
        this.data = data;
        this.additional = additional;
    }

    public SaveAbilityDataPacket(FriendlyByteBuf buf) {
        this.target = buf.readUUID();
        this.heroType = buf.readResourceLocation();
        this.ability = buf.readResourceLocation();
        this.data = buf.readNbt();
        this.additional = buf.readNbt();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUUID(this.target);
        buf.writeResourceLocation(this.heroType);
        buf.writeResourceLocation(this.ability);
        buf.writeNbt(this.data);
        buf.writeNbt(this.additional);
    }

    public void apply(Supplier<NetworkManager.PacketContext> context) {
        context.get().queue(() -> {
            var target = context.get().getPlayer().level().getPlayerByUUID(this.target);
            if (target instanceof IHeroHolder holder) {
                var ability = holder.hero$getManager().getType(this.heroType).getAbility(this.ability);
                ability.readData(target, this.data);
                if (ability instanceof IServerSynced synced) synced.readAdditionSync(this.additional);
                if (context.get().getPlayer() instanceof ServerPlayer sp) updateClients(sp, this);
            }
        });
    }

    private static void updateClients(ServerPlayer serverPlayer, SaveAbilityDataPacket packet) { // TODO MAYBE IGNORE SENDING PLAYER
//        Network.CHANNEL.sendToPlayers(serverPlayer.server.getPlayerList().getPlayers(), new SaveAbilityDataPacket(packet.target, packet.heroType, packet.ability, packet.data, packet.additional));
    }
}
