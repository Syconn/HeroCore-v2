package mod.syconn.hero;

import mod.syconn.hero.client.ClientHandler;
import mod.syconn.hero.common.CommonHandler;
import mod.syconn.hero.extra.core.Events;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ForgeCommon {

    @SubscribeEvent
    public static void clientTickEvent(TickEvent.PlayerTickEvent event) {
        if (event.player instanceof LocalPlayer lp) ClientHandler.onClientPlayerTick(lp);
        CommonHandler.playerTickEvent(new Events.PlayerTickEvent(event.player));
    }

    @SubscribeEvent
    public static void onFallEvent(LivingFallEvent event) {
        Events.LivingEntityFallEvent result = CommonHandler.playerFallEvent(new Events.LivingEntityFallEvent(event.getEntity(), event.getDistance(), event.getDamageMultiplier(), false));
        event.setDistance(result.distance());
        event.setDamageMultiplier(result.damageMultiplier());
        event.setCanceled(result.cancel());
    }
}
