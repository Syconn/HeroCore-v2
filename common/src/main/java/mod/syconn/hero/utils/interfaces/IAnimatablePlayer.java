package mod.syconn.hero.utils.interfaces;

import dev.kosmx.playerAnim.core.util.Ease;

public interface IAnimatablePlayer {

    default void swm$playAnimation(String name, int fadeIn, Ease ease) {}
    default void swm$stopAnimation(int fadeOut, Ease ease) {}
}
