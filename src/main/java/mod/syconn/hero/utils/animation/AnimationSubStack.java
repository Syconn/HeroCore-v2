//? if 1.20.1 {
package mod.syconn.hero.utils.animation;


import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.api.layered.modifier.MirrorModifier;
import dev.kosmx.playerAnim.api.layered.modifier.SpeedModifier;

public class AnimationSubStack<T extends IAnimation> {

    public final SpeedModifier speed = new SpeedModifier();
    public final MirrorModifier mirror = new MirrorModifier();
    public final ModifierLayer<T> base = new ModifierLayer<>();

    public AnimationSubStack() {
        mirror.setEnabled(false);
        base.addModifier(speed, 0);
        base.addModifier(mirror, 0);
    }
}
//? }