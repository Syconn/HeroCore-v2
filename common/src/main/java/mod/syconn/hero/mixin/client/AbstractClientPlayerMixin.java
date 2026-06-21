package mod.syconn.hero.mixin.client;

import com.mojang.authlib.GameProfile;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.modifier.AbstractFadeModifier;
import dev.kosmx.playerAnim.core.util.Ease;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import mod.syconn.hero.utils.client.AnimationSubStack;
import mod.syconn.hero.utils.generic.AnimationUtil;
import mod.syconn.hero.utils.interfaces.IAnimatablePlayer;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractClientPlayer.class)
public abstract class AbstractClientPlayerMixin extends Player implements IAnimatablePlayer {

    @Unique
    private final AnimationSubStack<IAnimation> swm$playerAnimation = new AnimationSubStack<>();

    public AbstractClientPlayerMixin(Level level, BlockPos pos, float yRot, GameProfile gameProfile) {
        super(level, pos, yRot, gameProfile);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void postInit(ClientLevel clientLevel, GameProfile gameProfile, CallbackInfo ci) {
        PlayerAnimationAccess.getPlayerAnimLayer((AbstractClientPlayer) (Object) this).addAnimLayer(100, swm$playerAnimation.base);
    }

    @Override
    public void hero$playAnimation(String name, float speed, int fadeIn, Ease ease) {
        var animation = AnimationUtil.getAnimation(name);
        var mirror = this.getMainArm() == HumanoidArm.LEFT;
        swm$playerAnimation.mirror.setEnabled(mirror);
        swm$playerAnimation.speed.speed = speed;
        swm$playerAnimation.base.replaceAnimationWithFade(AbstractFadeModifier.standardFadeIn(fadeIn, ease), new KeyframeAnimationPlayer(animation));
    }

    @Override
    public void hero$stopAnimation(int fadeOut, Ease ease) {
        IAnimation currentAnimation = swm$playerAnimation.base.getAnimation();
        if (currentAnimation instanceof KeyframeAnimationPlayer) swm$playerAnimation.base.replaceAnimationWithFade(AbstractFadeModifier.standardFadeIn(fadeOut, Ease.INOUTSINE), null);
    }
}