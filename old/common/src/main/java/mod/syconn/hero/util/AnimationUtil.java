package mod.syconn.hero.util;

import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.api.layered.modifier.AbstractFadeModifier;
import dev.kosmx.playerAnim.core.util.Ease;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import mod.syconn.hero.Constants;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import java.util.Objects;

@SuppressWarnings("unchecked")
public class AnimationUtil {

    public static ModifierLayer<IAnimation> getAnimator(AbstractClientPlayer player) {
        return (ModifierLayer<IAnimation>) PlayerAnimationAccess.getPlayerAssociatedData(player).get(new ResourceLocation(Constants.MOD_ID, "animation"));
    }

    public static void play(AbstractClientPlayer player, String animation) {
        var animator = getAnimator(player);
        if (animator != null && valid(animation)) animator.setAnimation(new KeyframeAnimationPlayer(Objects.requireNonNull(
                PlayerAnimationRegistry.getAnimation(new ResourceLocation(Constants.MOD_ID, animation)))));
    }

    public static void play(AbstractClientPlayer player, String animation, int delay) {
        var animator = getAnimator(player);
        if (animator != null && valid(animation)) animator.replaceAnimationWithFade(AbstractFadeModifier.standardFadeIn(delay, Ease.CONSTANT),
                new KeyframeAnimationPlayer(Objects.requireNonNull(PlayerAnimationRegistry.getAnimation(new ResourceLocation(Constants.MOD_ID, animation)))));
    }

    public static void play(AbstractClientPlayer player, String animation, int delay, Ease ease) {
        var animator = getAnimator(player);
        if (animator != null && valid(animation)) animator.replaceAnimationWithFade(AbstractFadeModifier.standardFadeIn(delay, ease),
                new KeyframeAnimationPlayer(Objects.requireNonNull(PlayerAnimationRegistry.getAnimation(new ResourceLocation(Constants.MOD_ID, animation)))));
    }

    public static void stop(AbstractClientPlayer player) {
        var animator = getAnimator(player);
        if (animator != null) animator.setAnimation(null);
    }

    public static boolean isAnimated(AbstractClientPlayer player) {
        return getAnimator(player).isActive();
    }

    private static boolean valid(String animation) {
        return PlayerAnimationRegistry.getAnimation(new ResourceLocation(Constants.MOD_ID, animation)) != null;
    }
}
