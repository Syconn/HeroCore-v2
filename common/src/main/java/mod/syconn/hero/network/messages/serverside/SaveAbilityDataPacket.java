package mod.syconn.hero.network.messages.serverside;

import dev.architectury.networking.NetworkManager;
import mod.syconn.hero.feature.heros.interfaces.IHeroHolder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public class SaveAbilityDataPacket {

    private final ResourceLocation heroType;
    private final ResourceLocation ability;
    private final CompoundTag data;

    public SaveAbilityDataPacket(ResourceLocation heroType, ResourceLocation ability, CompoundTag data) {
        this.heroType = heroType;
        this.ability = ability;
        this.data = data;
    }

    public SaveAbilityDataPacket(FriendlyByteBuf buf) {
        this.heroType = buf.readResourceLocation();
        this.ability = buf.readResourceLocation();
        this.data = buf.readNbt();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeResourceLocation(this.heroType);
        buf.writeResourceLocation(this.ability);
        buf.writeNbt(this.data);
    }

    public void apply(Supplier<NetworkManager.PacketContext> context) {
        context.get().queue(() -> IHeroHolder.ID_MAP.get(this.heroType).getAbility(this.ability).readData(context.get().getPlayer(), this.data));
    }
}
