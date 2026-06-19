package mod.syconn.hero.network.messages.serverside;

import dev.architectury.networking.NetworkManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.function.Supplier;

public class FlightTravelPacket {

    private final int up;
    private final int forward;
    private final int side;

    public FlightTravelPacket(int up, int forward, int side) {
        this.up = up;
        this.forward = forward;
        this.side = side;
    }

    public FlightTravelPacket(FriendlyByteBuf buf) {
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
            boolean boost = player.isSprinting();

            // Flight values
            double maxSpeed = boost ? 4.5 : 2.5;
            double thrust = boost ? 0.18 : 0.12;
            double drag = boost ? 0.995 : 0.985;
            var adjustForGravity = 0.08f;

            // Current motion
            Vec3 motion = player.getDeltaMovement();

            // Cancel vanilla gravity
            motion = new Vec3(motion.x, motion.y + adjustForGravity, motion.z);

            // Rotation
            float yaw = player.getYRot() * ((float) Math.PI / 180F);
            float pitch = player.getXRot() * ((float) Math.PI / 180F);

            // Head direction
            Vec3 look = new Vec3(-Mth.sin(yaw) * Mth.cos(pitch), -Mth.sin(pitch), Mth.cos(yaw) * Mth.cos(pitch)).normalize();

            // Make pitch less aggressive
            Vec3 flightLook = new Vec3(look.x, look.y * 0.6, look.z).normalize();

            // Horizontal vectors
            Vec3 horizontalForward = new Vec3(-Mth.sin(yaw), 0, Mth.cos(yaw)).normalize();
            Vec3 right = new Vec3(horizontalForward.z, 0, -horizontalForward.x);

            // Build movement direction
            Vec3 direction = Vec3.ZERO;

            // W/S
            if (forward > 0) direction = direction.add(flightLook.scale(forward));

            // A/D
            direction = direction.add(right.scale(side));
            if (direction.lengthSqr() > 0) direction = direction.normalize();

            // Up/down controls acceleration
            double throttle = 1.0;

            // Apply thrust
            if (direction.lengthSqr() > 0) motion = motion.add(direction.scale(thrust * throttle));
            if (Math.abs(look.y) < 0.2F) motion = new Vec3(motion.x, motion.y * 0.85, motion.z);
            motion = motion.scale(drag);

            if (motion.length() > maxSpeed) motion = motion.normalize().scale(maxSpeed);
            if (forward > 0 || side != 0) player.setDeltaMovement(motion);
            player.fallDistance = 0;
            player.hurtMarked = true;
        });
    }
}
