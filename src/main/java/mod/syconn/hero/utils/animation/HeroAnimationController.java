//? if >=1.21.1 {
/*package mod.syconn.hero.utils.animation;

import com.zigythebird.playeranim.animation.PlayerAnimationController;
import com.zigythebird.playeranimcore.animation.layered.modifier.MirrorModifier;
import com.zigythebird.playeranimcore.animation.layered.modifier.SpeedModifier;
import net.minecraft.client.player.AbstractClientPlayer;

public class HeroAnimationController extends PlayerAnimationController {
    private final SpeedModifier speedModifier;
    private final MirrorModifier mirrorModifier;

    public HeroAnimationController(AbstractClientPlayer player, AnimationStateHandler animationHandler) {
        super(player, animationHandler);
        speedModifier = new SpeedModifier(1.0f);
        mirrorModifier = new MirrorModifier();
        addModifierLast(speedModifier).addModifierBefore(mirrorModifier);
    }

    public void setSpeed(float speed) {
        if (!Float.isFinite(speed) || speed <= 0f) speed = 1.0f;
        this.speedModifier.speed = speed;
    }

    public void setMirror(boolean mirror) {
        this.mirrorModifier.enabled = mirror;
    }

    public int getAnimationTick() {
        return this.tick;
    }
}
*///? }
