package mod.syconn.hero.common;

import dev.architectury.event.EventResult;
import mod.syconn.hero.core.ModItems;
import mod.syconn.hero.util.Helpers;
import mod.syconn.hero.util.SuitSettings;
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
        SuitSettings settings = SuitSettings.from(player);
        if (settings.getFlightMode() == SuitSettings.FlightMode.HOVER) {
            Vec3 delta = player.getDeltaMovement();
            if ((delta.y > 0.62 || delta.y < 0.58) && (delta.y < -0.62 || delta.y > -0.58)) player.setDeltaMovement(delta.x, 0, delta.z);
        }
    }
}
