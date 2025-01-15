package mod.syconn.hero.network.messages;

import dev.architectury.networking.NetworkManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

import java.util.function.Supplier;

public class MessageSuitPropel {
    private final boolean sprinting;

    public MessageSuitPropel(boolean sprinting) {
        this.sprinting = sprinting;
    }

    public MessageSuitPropel(FriendlyByteBuf buf) {
        this(buf.readBoolean());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBoolean(this.sprinting);
    }

    public void apply(Supplier<NetworkManager.PacketContext> context) {
        context.get().queue(() -> {
            Player player = context.get().getPlayer();
            float f7 = player.getYRot();
            float f1 = player.getXRot();
            float f2 = -Mth.sin(f7 * (float) (Math.PI / 180.0)) * Mth.cos(f1 * (float) (Math.PI / 180.0));
            float f3 = -Mth.sin(f1 * (float) (Math.PI / 180.0));
            float f4 = Mth.cos(f7 * (float) (Math.PI / 180.0)) * Mth.cos(f1 * (float) (Math.PI / 180.0));
            if (sprinting) player.setDeltaMovement(f2, f3, f4);
            else player.setDeltaMovement(f2 * 2 / 3, f3 * 2 / 3, f4 * 2 / 3);
            player.hurtMarked = true;
        });
    }
}
