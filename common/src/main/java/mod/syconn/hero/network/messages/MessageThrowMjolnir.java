package mod.syconn.hero.network.messages;

import dev.architectury.networking.NetworkManager;
import mod.syconn.hero.common.item.MjolnirItem;
import mod.syconn.hero.util.AbilityUtil;
import net.minecraft.network.FriendlyByteBuf;

import java.util.function.Supplier;

public class MessageThrowMjolnir {

    public MessageThrowMjolnir() { }

    public MessageThrowMjolnir(FriendlyByteBuf buf) {
        this();
    }

    public void encode(FriendlyByteBuf buf) { }

    public void apply(Supplier<NetworkManager.PacketContext> context) {
        context.get().queue(() -> MjolnirItem.throwHammer(context.get().getPlayer(), AbilityUtil.getHolding(context.get().getPlayer())));
    }
}
