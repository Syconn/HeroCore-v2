package mod.syconn.hero.network.messages.serverside;

import dev.architectury.networking.NetworkManager;
import mod.syconn.hero.feature.heros.interfaces.IHeroHolder;
import mod.syconn.hero.feature.heros.interfaces.IServerSynced;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public class SaveAbilityDataPacket {

    private final ResourceLocation heroType;
    private final ResourceLocation ability;
    private final CompoundTag data;
    private final CompoundTag additional;

    public SaveAbilityDataPacket(ResourceLocation heroType, ResourceLocation ability, CompoundTag data, CompoundTag additional) {
        this.heroType = heroType;
        this.ability = ability;
        this.data = data;
        this.additional = additional;
    }

    public SaveAbilityDataPacket(FriendlyByteBuf buf) {
        this.heroType = buf.readResourceLocation();
        this.ability = buf.readResourceLocation();
        this.data = buf.readNbt();
        this.additional = buf.readNbt();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeResourceLocation(this.heroType);
        buf.writeResourceLocation(this.ability);
        buf.writeNbt(this.data);
        buf.writeNbt(this.additional);
    }

    public void apply(Supplier<NetworkManager.PacketContext> context) {
        context.get().queue(() -> {
            var ability =  IHeroHolder.ID_MAP.get(this.heroType).getAbility(this.ability);
            ability.readData(context.get().getPlayer(), this.data);
            if (ability instanceof IServerSynced synced) synced.readAdditionSync(this.additional);
        });
    }
}
