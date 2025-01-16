package mod.syconn.hero.network.messages;

import dev.architectury.networking.NetworkManager;
import mod.syconn.hero.Constants;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public record MessageAlterHover(boolean rise) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<MessageAlterHover> TYPE = new CustomPacketPayload.Type<>(Constants.withId("alter_hover"));
    public static final StreamCodec<RegistryFriendlyByteBuf, MessageAlterHover> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.BOOL, MessageAlterHover::rise, MessageAlterHover::new);

    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(MessageAlterHover message, NetworkManager.PacketContext context) {
        context.queue(() -> {
            Player player = context.getPlayer();
            Vec3 delta = player.getDeltaMovement();
            player.setDeltaMovement(delta.x, message.rise ? 0.6 : -0.6, delta.z);
            player.hurtMarked = true;
        });
    }
}
