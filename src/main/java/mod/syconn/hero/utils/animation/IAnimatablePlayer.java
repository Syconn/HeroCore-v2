package mod.syconn.hero.utils.animation;

//? 1.20.1
import dev.kosmx.playerAnim.core.util.Ease;
//? >=1.21.1
//import com.zigythebird.playeranimcore.easing.EasingType;

public interface IAnimatablePlayer {

    void hero$playAnimation(String name, float speed, int fadeIn, /*? >=1.21.1 >> */ /*EasingType*/ /*? 1.20.1 >> */ Ease ease);
    void hero$stopAnimation(int fadeOut, /*? >=1.21.1 >> */ /*EasingType*/ /*? 1.20.1 >> */ Ease ease);
}
