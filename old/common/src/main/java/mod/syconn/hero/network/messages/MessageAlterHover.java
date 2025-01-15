package mod.syconn.hero.network.messages;

import dev.architectury.networking.NetworkManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.function.Supplier;

public class MessageAlterHover {

    private final boolean higher;

    public MessageAlterHover(boolean higher) {
        this.higher = higher;
    }

    public MessageAlterHover(FriendlyByteBuf buf) {
        this(buf.readBoolean());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBoolean(this.higher);
    }

    public void apply(Supplier<NetworkManager.PacketContext> context) {
        context.get().queue(() -> {
            Player player = context.get().getPlayer();
            Vec3 delta = player.getDeltaMovement();
            player.setDeltaMovement(delta.x, higher ? 0.6 : -0.6, delta.z);
            player.hurtMarked = true;
        });
    }
}
