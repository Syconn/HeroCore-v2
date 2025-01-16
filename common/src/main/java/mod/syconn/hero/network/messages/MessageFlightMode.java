package mod.syconn.hero.network.messages;

import dev.architectury.networking.NetworkManager;
import mod.syconn.hero.Constants;
import mod.syconn.hero.common.components.SuitComponent;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record MessageFlightMode(SuitComponent.FlightMode mode) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<MessageFlightMode> TYPE = new CustomPacketPayload.Type<>(Constants.withId("flight_mode"));
    public static final StreamCodec<RegistryFriendlyByteBuf, MessageFlightMode> STREAM_CODEC = StreamCodec.composite(SuitComponent.FlightMode.STREAM_CODEC, MessageFlightMode::mode, MessageFlightMode::new);

    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(MessageFlightMode message, NetworkManager.PacketContext context) {
        context.queue(() -> SuitComponent.update(context.getPlayer(), settings -> settings.setFlightMode(message.mode)));
    }
}
