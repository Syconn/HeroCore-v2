package mod.syconn.hero.common;

import mod.syconn.hero.extra.core.Events;
import mod.syconn.hero.extra.data.attachment.SuperPower;
import mod.syconn.hero.extra.platform.Services;
import mod.syconn.hero.extra.util.Helpers;
import mod.syconn.hero.registrar.AttachmentRegistrar;
import mod.syconn.hero.registrar.ItemRegistrar;
import net.minecraft.world.entity.player.Player;

public class CommonHandler {
    
    public static Events.LivingEntityFallEvent playerFallEvent(Events.LivingEntityFallEvent event) {
        if (event.entity() instanceof Player player) 
            return new Events.LivingEntityFallEvent(event.entity(), event.distance(), event.damageMultiplier(), Helpers.isWearingIronManSuit(player) || Helpers.isHolding(player, ItemRegistrar.MJOLNIR.get()));
        return event;
    }

    public static void playerTickEvent(Events.PlayerTickEvent event) {
        Player player = event.player();
        SuperPower powers = Services.ATTACHED_DATA.get(AttachmentRegistrar.SUPER_POWER, player);
        
//        if (powers.flying())
//            Helpers.lowerSuitEnergy(player);
    }
}
