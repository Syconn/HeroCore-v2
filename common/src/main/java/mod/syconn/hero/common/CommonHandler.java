package mod.syconn.hero.common;

import dev.architectury.event.EventResult;
import mod.syconn.hero.core.ModItems;
import mod.syconn.hero.util.Helpers;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class CommonHandler {
    
    public static EventResult entityHurtEvent(LivingEntity entity, DamageSource source, float amount) {
        if (entity instanceof Player player) 
            if (source.is(DamageTypes.FALL) && (Helpers.isWearingIronManSuit(player) || Helpers.isHolding(player, ModItems.MJOLNIR.get())))
                return EventResult.interruptFalse();
        return EventResult.pass();
    }
}
