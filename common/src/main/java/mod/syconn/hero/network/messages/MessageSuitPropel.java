package mod.syconn.hero.network.messages;

import dev.architectury.networking.NetworkManager;
import mod.syconn.hero.Constants;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

public record MessageSuitPropel(boolean sprinting) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<MessageSuitPropel> TYPE = new CustomPacketPayload.Type<>(Constants.withId("suit_propel"));
    public static final StreamCodec<RegistryFriendlyByteBuf, MessageSuitPropel> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.BOOL, MessageSuitPropel::sprinting, MessageSuitPropel::new);

    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(MessageSuitPropel message, NetworkManager.PacketContext context) {
        context.queue(() -> {
            Player player = context.getPlayer();
            float f7 = player.getYRot();
            float f1 = player.getXRot();
            float f2 = -Mth.sin(f7 * (float) (Math.PI / 180.0)) * Mth.cos(f1 * (float) (Math.PI / 180.0));
            float f3 = -Mth.sin(f1 * (float) (Math.PI / 180.0));
            float f4 = Mth.cos(f7 * (float) (Math.PI / 180.0)) * Mth.cos(f1 * (float) (Math.PI / 180.0));
            if (message.sprinting) player.setDeltaMovement(f2, f3, f4);
            else player.setDeltaMovement(f2 * 2 / 3, f3 * 2 / 3, f4 * 2 / 3);
            player.hurtMarked = true;
        });
    }
}
