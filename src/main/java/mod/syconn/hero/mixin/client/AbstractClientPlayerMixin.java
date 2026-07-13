package mod.syconn.hero.mixin.client;

import com.mojang.authlib.GameProfile;
import mod.syconn.hero.utils.animation.AnimationSubStack;
import mod.syconn.hero.utils.animation.IAnimatablePlayer;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

//? if 1.20.1 {
import mod.syconn.hero.utils.generic.AnimationUtil;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.HumanoidArm;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.modifier.AbstractFadeModifier;
import dev.kosmx.playerAnim.core.util.Ease;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
//? } else {
/*import com.zigythebird.playeranim.api.PlayerAnimationAccess;
import com.zigythebird.playeranimcore.animation.layered.modifier.AbstractFadeModifier;
import com.zigythebird.playeranimcore.easing.EasingType;
import com.zigythebird.playeranim.api.PlayerAnimationAccess;
import com.zigythebird.playeranimcore.animation.layered.IAnimation;
import com.zigythebird.playeranimcore.animation.layered.modifier.AbstractFadeModifier;
import net.minecraft.resources.ResourceLocation;
import mod.syconn.hero.utils.animation.HeroAnimationController;
import mod.syconn.hero.utils.Constants;
*///? }

@Mixin(AbstractClientPlayer.class)
public abstract class AbstractClientPlayerMixin extends Player implements IAnimatablePlayer {

    public AbstractClientPlayerMixin(Level level, BlockPos pos, float yRot, GameProfile gameProfile) {
        super(level, pos, yRot, gameProfile);
    }

    //? if 1.20.1 {
    @Unique
    private final AnimationSubStack<IAnimation> swm$playerAnimation = new AnimationSubStack<>();

    @Inject(method = "<init>", at = @At("TAIL"))
    private void postInit(ClientLevel clientLevel, GameProfile gameProfile, CallbackInfo ci) {
        PlayerAnimationAccess.getPlayerAnimLayer((AbstractClientPlayer) (Object) this).addAnimLayer(100, swm$playerAnimation.base);
    }
    //? }

    @Override
    public void hero$playAnimation(String name, float speed, int fadeIn, /*? >=1.21.1 >> */ /*EasingType*/ /*? 1.20.1 >> */ Ease ease) {
        //? if 1.20.1 {
        var animation = AnimationUtil.getAnimation(name);
        swm$playerAnimation.mirror.setEnabled(this.getMainArm() == HumanoidArm.LEFT);
        swm$playerAnimation.speed.speed = speed;
        swm$playerAnimation.base.replaceAnimationWithFade(AbstractFadeModifier.standardFadeIn(fadeIn, ease), new KeyframeAnimationPlayer(animation));
        //? } else {
        /*var animator = (HeroAnimationController) PlayerAnimationAccess.getPlayerAnimationLayer((AbstractClientPlayer) (Object) this, Constants.ANIM_LAYER);
        if (animator != null) animator.replaceAnimationWithFade(AbstractFadeModifier.standardFadeIn(fadeIn, ease), Constants.withId(name));
        *///? }
    }

    @Override
    public void hero$stopAnimation(int fadeOut, /*? >=1.21.1 >> */ /*EasingType*/ /*? 1.20.1 >> */ Ease ease) {
        //? if 1.20.1 {
        IAnimation currentAnimation = swm$playerAnimation.base.getAnimation();
        if (currentAnimation instanceof KeyframeAnimationPlayer)
            swm$playerAnimation.base.replaceAnimationWithFade(AbstractFadeModifier.standardFadeIn(fadeOut, Ease.INOUTSINE), null);
        //? } else {
        /*var animator = (HeroAnimationController) PlayerAnimationAccess.getPlayerAnimationLayer((AbstractClientPlayer) (Object) this, Constants.ANIM_LAYER);
        if (animator != null) animator.replaceAnimationWithFade(AbstractFadeModifier.standardFadeIn(fadeOut, ease), (ResourceLocation) null);
        *///? }
    }
}