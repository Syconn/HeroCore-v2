package mod.syconn.hero.network.messages.serverside;

import dev.architectury.networking.NetworkManager;
import mod.syconn.hero.utils.Constants;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;

import java.util.function.Supplier;

public class HoverPacket {

    private final int up;
    private final int forward;
    private final int side;

    public HoverPacket(int up, int forward, int side) {
        this.up = up;
        this.forward = forward;
        this.side = side;
    }

    public HoverPacket(FriendlyByteBuf buf) {
        this(buf.readInt(), buf.readInt(), buf.readInt());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(this.up);
        buf.writeInt(this.forward);
        buf.writeInt(this.side);
    }

    public void apply(Supplier<NetworkManager.PacketContext> context) {
        context.get().queue(() -> {
            Player player = context.get().getPlayer();
            var speed = player.isSprinting() ? 0.35 : 0.25;
            Vec3 motion = player.getDeltaMovement();
            double verticalAccel = 0.065;
            double damping = 0.9;

            double y = motion.y * damping;
            if (up == 1) y += verticalAccel;
            else if (up == -1) y -= verticalAccel;
            if (up == 0) y *= 0.5;
            y = Mth.clamp(y, -speed * 5, speed * 5);

            double maxY = player.level().getHeight(Heightmap.Types.MOTION_BLOCKING, player.getBlockX(), player.getBlockZ()) + Constants.CONFIG.serverSettings.maxHoverHeight.get();
            double playerY = player.getBlockY();
            double overflow = playerY - maxY;
            if (overflow > 0) y = -Math.min(0.3, 0.05 + overflow * 0.2);
            else if (playerY >= maxY - 0.1 && y > 0) y = 0;

            float yaw = player.getYRot() * ((float) Math.PI / 180F);
            var forwardVec = new Vec3(-Mth.sin(yaw), 0, Mth.cos(yaw));
            var rightVec = new Vec3(forwardVec.z, 0, -forwardVec.x);
            var horizontal = forwardVec.scale(forward).add(rightVec.scale(side));
            var newHorizontal = Vec3.ZERO;
            if (horizontal.lengthSqr() > 0) newHorizontal = horizontal.normalize().scale(speed);
            player.setDeltaMovement(newHorizontal.x, y, newHorizontal.z);

            player.fallDistance = 0;
            player.hurtMarked = true;
        });
    }
}
