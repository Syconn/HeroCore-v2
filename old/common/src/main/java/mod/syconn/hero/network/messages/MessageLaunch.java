package mod.syconn.hero.network.messages;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public record MessageLaunch(boolean setFly) {

    public static StreamCodec<RegistryFriendlyByteBuf, MessageLaunch> CODEC = StreamCodec.composite(ByteBufCodecs.BOOL, MessageLaunch::setFly, MessageLaunch::new);

    public static void handle(MessageLaunch msg, Player player) {
        float f7 = player.getYRot();
        float f1 = player.getXRot();
        float f2 = -Mth.sin(f7 * (float) (Math.PI / 180.0)) * Mth.cos(f1 * (float) (Math.PI / 180.0));
        float f3 = -Mth.sin(f1 * (float) (Math.PI / 180.0));
        float f4 = Mth.cos(f7 * (float) (Math.PI / 180.0)) * Mth.cos(f1 * (float) (Math.PI / 180.0));
        float f5 = Mth.sqrt(f2 * f2 + f3 * f3 + f4 * f4);
        f2 *= 1 / f5;
        f3 *= 1 / f5;
        f4 *= 1 / f5;
        player.push(f2, f3, f4);
        if (player.onGround()) player.move(MoverType.SELF, new Vec3(0.0, 1.1999999F, 0.0));
        player.hurtMarked = true;
    }
}
