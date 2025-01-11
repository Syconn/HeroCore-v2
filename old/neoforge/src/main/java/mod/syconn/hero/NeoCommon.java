package mod.syconn.hero;

import mod.syconn.hero.client.ClientHandler;
import mod.syconn.hero.common.CommonHandler;
import mod.syconn.hero.extra.core.Events;
import net.minecraft.client.player.LocalPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingFallEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

public class NeoCommon {
    
    @SubscribeEvent
    public static void clientTickEvent(final PlayerTickEvent.Post event) {
        if (event.getEntity() instanceof LocalPlayer lp) ClientHandler.onClientPlayerTick(lp);
        CommonHandler.playerTickEvent(new Events.PlayerTickEvent(event.getEntity()));
    }

    @SubscribeEvent
    public static void onFallEvent(LivingFallEvent event) {
        Events.LivingEntityFallEvent result = CommonHandler.playerFallEvent(new Events.LivingEntityFallEvent(event.getEntity(), event.getDistance(), event.getDamageMultiplier(), false));
        event.setDistance(result.distance());
        event.setDamageMultiplier(result.damageMultiplier());
        event.setCanceled(result.cancel());
    }
}
