package mod.syconn.hero.utils.generic;

import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.core.util.Ease;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import mod.syconn.hero.network.Network;
import mod.syconn.hero.network.messages.PlayAnimationPacket;
import mod.syconn.hero.utils.Constants;
import net.minecraft.server.level.ServerPlayer;

public class AnimationUtil {

    public static float inCubic(float t) {
        return t * t * t;
    }

    public static float outCubic(float t) {
        t--;
        return (t * t * t + 1);
    }

    public static float inOutCubic(float t) {
        t *= 2;
        if (t < 1) return t * t * t / 2;
        t -= 2;
        return (t * t * t + 2) / 2;
    }

    public static KeyframeAnimation getAnimation(String name) {
        return PlayerAnimationRegistry.getAnimation(Constants.withId(name));
    }

    public static void notifyAndPlay(ServerPlayer serverPlayer, String animation, float speed, int fadeIn, Ease ease) {
        Network.CHANNEL.sendToPlayers(serverPlayer.serverLevel().players(), new PlayAnimationPacket(serverPlayer.getUUID(), animation, speed, fadeIn, ease));
    }
}