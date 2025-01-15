package mod.syconn.hero.network.messages;

import dev.architectury.networking.NetworkManager;
import mod.syconn.hero.common.components.SuitComponent;
import net.minecraft.network.FriendlyByteBuf;

import java.util.function.Supplier;

public class MessageFlightMode {

    private final SuitComponent.FlightMode mode;

    public MessageFlightMode(SuitComponent.FlightMode mode) {
        this.mode = mode;
    }

    public MessageFlightMode(FriendlyByteBuf buf) {
        this(buf.readEnum(SuitComponent.FlightMode.class));
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeEnum(mode);
    }

    public void apply(Supplier<NetworkManager.PacketContext> context) {
        context.get().queue(() -> SuitComponent.update(context.get().getPlayer(), settings -> settings.setFlightMode(mode)));
    }
}
