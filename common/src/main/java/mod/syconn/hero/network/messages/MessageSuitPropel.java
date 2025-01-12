package mod.syconn.hero.network.messages;

import dev.architectury.networking.NetworkManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.function.Supplier;

public class MessageSuitPropel {

    public MessageSuitPropel() {
        
    }

    public MessageSuitPropel(FriendlyByteBuf buf) {
        this();
    }

    public void encode(FriendlyByteBuf buf) {
        
    }

    public void apply(Supplier<NetworkManager.PacketContext> context) {
        context.get().queue(() -> {
            Player player = context.get().getPlayer();
//            float f7 = player.getYRot();
//            float f1 = player.getXRot();
//            float f2 = -Mth.sin(f7 * (float) (Math.PI / 180.0)) * Mth.cos(f1 * (float) (Math.PI / 180.0));
//            float f3 = -Mth.sin(f1 * (float) (Math.PI / 180.0));
//            float f4 = Mth.cos(f7 * (float) (Math.PI / 180.0)) * Mth.cos(f1 * (float) (Math.PI / 180.0));
//            float f5 = Mth.sqrt(f2 * f2 + f3 * f3 + f4 * f4);
//            f2 *= 1 / f5;
//            f3 *= 1 / f5;
//            f4 *= 1 / f5;
//            player.push(f2, f3, f4);
            if (player.onGround()) player.move(MoverType.SELF, new Vec3(0.0, 1.1999999F, 0.0));
            player.hurtMarked = true;
        });
    }
}
