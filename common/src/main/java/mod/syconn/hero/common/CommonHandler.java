package mod.syconn.hero.common;

import dev.architectury.event.EventResult;
import mod.syconn.hero.core.ModItems;
import mod.syconn.hero.util.Helpers;
import mod.syconn.hero.util.SuitSettings;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class CommonHandler {
    
    public static EventResult entityHurtEvent(LivingEntity entity, DamageSource source, float amount) {
        if (entity instanceof Player player) 
            if (source.is(DamageTypes.FALL) && (Helpers.isWearingIronManSuit(player) || Helpers.isHolding(player, ModItems.MJOLNIR.get())))
                return EventResult.interruptFalse();
        return EventResult.pass();
    }

    public static void onPlayerTick(Player player) {
        if (player != null && Helpers.isWearingIronManSuit(player)) {
            SuitSettings settings = SuitSettings.from(player);
            if (settings.getFlightMode() == SuitSettings.FlightMode.HOVER) {
                Vec3 pos = player.getEyePosition();
                if (player.level().getBlockState(player.getOnPos()).isAir()) player.level().addParticle(ParticleTypes.CLOUD, pos.x, pos.y - 2, pos.z, 0, -0.085, 0);
                Vec3 delta = player.getDeltaMovement();
                if (player.level().getBlockState(player.getOnPos()).isAir() && (delta.y > 0.62 || delta.y < 0.58) && (delta.y < -0.62 || delta.y > -0.58))
                    player.setDeltaMovement(delta.x, 0, delta.z);
            } else if (settings.getFlightMode() == SuitSettings.FlightMode.FLY) {
                if (player.level().getBlockState(player.getOnPos()).isAir()) {
                    Vec3 pos = player.getEyePosition();
                    float xo = Mth.sin(Mth.wrapDegrees(player.yBodyRot) * (float) (Math.PI / 180.0));
                    float yo = Mth.sin(Mth.wrapDegrees(player.getXRot()) / 2 * (float) (Math.PI / 180.0));
                    float zo = Mth.cos(Mth.wrapDegrees(player.yBodyRot) * (float) (Math.PI / 180.0));
                    player.level().addParticle(ParticleTypes.CLOUD, pos.x + xo, pos.y - 1, pos.z - zo, xo / 3, yo, -zo / 3);
                }
            }
        }
    }
}
