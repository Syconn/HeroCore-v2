package mod.syconn.hero.utils.generic;

import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.core.util.Ease;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import mod.syconn.hero.network.Network;
import mod.syconn.hero.network.messages.PlayAnimationPacket;
import mod.syconn.hero.utils.Constants;
import net.minecraft.server.level.ServerPlayer;

public class AnimationUtil {

    public static KeyframeAnimation getAnimation(String name) {
        return PlayerAnimationRegistry.getAnimation(Constants.withId(name));
    }

    public static void notifyAndPlay(ServerPlayer serverPlayer, String animation, float speed, int fadeIn, Ease ease) {
        Network.CHANNEL.sendToPlayers(serverPlayer.serverLevel().players(), new PlayAnimationPacket(serverPlayer.getUUID(), animation, speed, fadeIn, ease));
    }
}