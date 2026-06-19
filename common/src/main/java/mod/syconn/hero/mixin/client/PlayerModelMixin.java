package mod.syconn.hero.mixin.client;

import mod.syconn.hero.feature.heros.interfaces.IHeroHolder;
import mod.syconn.hero.feature.ironman.Ironman;
import mod.syconn.hero.feature.ironman.abilities.FlightAbility;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerModel.class)
public class PlayerModelMixin {

    @Inject(method = "setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V", at = @At("HEAD"))
    private void heroCore$zeroLegSwing(LivingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
        if (entity instanceof IHeroHolder holder && holder.hero$getManager().getType(Ironman.class).getAbility(FlightAbility.class).isFlying()) entity.walkAnimation.setSpeed(0.0F);
    }
}
