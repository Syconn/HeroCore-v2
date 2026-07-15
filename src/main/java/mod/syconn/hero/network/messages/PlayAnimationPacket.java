package mod.syconn.hero.network.messages;


import dev.architectury.networking.NetworkManager;
import mod.syconn.hero.utils.generic.AnimationUtil;
import mod.syconn.hero.utils.animation.IAnimatablePlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import java.util.UUID;
import java.util.function.Supplier;

//? 1.20.1
import dev.kosmx.playerAnim.core.util.Ease;
//? >=1.21.1
//import com.zigythebird.playeranimcore.easing.EasingType;

public class PlayAnimationPacket {

    private final UUID uuid;
    private final String animation;
    private final float speed;
    private final int fadeIn;
    private final /*? >=1.21.1 >> */ /*EasingType*/ /*? 1.20.1 >> */ Ease ease;

    public PlayAnimationPacket(UUID uuid, String animation, float speed, int fadeIn, /*? >=1.21.1 >> */ /*EasingType*/ /*? 1.20.1 >> */ Ease ease) {
        this.uuid = uuid;
        this.animation = animation;
        this.speed = speed;
        this.fadeIn = fadeIn;
        this.ease = ease;
    }

    public PlayAnimationPacket(FriendlyByteBuf buf) {
        this.uuid = buf.readUUID();
        this.animation = buf.readUtf();
        this.speed = buf.readFloat();
        this.fadeIn = buf.readInt();
        this.ease = buf.readEnum(/*? >=1.21.1 >> */ /*EasingType.class*/ /*? 1.20.1 >> */ Ease.class);
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUUID(this.uuid);
        buf.writeUtf(this.animation);
        buf.writeFloat(this.speed);
        buf.writeInt(this.fadeIn);
        buf.writeEnum(this.ease);
    }

    public void apply(Supplier<NetworkManager.PacketContext> context) {
        context.get().queue(() -> {
            if (context.get().getPlayer() instanceof ServerPlayer player) AnimationUtil.notifyAndPlay(player, this.animation, this.speed, this.fadeIn, this.ease);
            else if (context.get().getPlayer().level().getPlayerByUUID(this.uuid) instanceof IAnimatablePlayer player) player.hero$playAnimation(this.animation, this.speed, this.fadeIn, this.ease);
        });
    }
}
