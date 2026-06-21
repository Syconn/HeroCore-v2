package mod.syconn.hero.utils.interfaces;

import dev.kosmx.playerAnim.core.util.Ease;

public interface IAnimatablePlayer {

    default void hero$playAnimation(String name, float speed, int fadeIn, Ease ease) {}
    default void hero$stopAnimation(int fadeOut, Ease ease) {}
}
