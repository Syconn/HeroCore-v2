package mod.syconn.hero;

import mod.syconn.hero.common.CommonHandler;
import mod.syconn.hero.events.EntityEvents;
import mod.syconn.hero.extra.core.Events;
import mod.syconn.hero.network.Network;
import net.fabricmc.api.ModInitializer;

public class HeroFabric implements ModInitializer {
    
    public void onInitialize() {
        EntityEvents.FALL_EVENT.register(((livingEntity, distance, damageMultiplier, cancelled) -> CommonHandler.playerFallEvent(new Events.LivingEntityFallEvent(livingEntity, distance, damageMultiplier, cancelled))));
        EntityEvents.PLAYER_TICK.register(entity -> CommonHandler.playerTickEvent(new Events.PlayerTickEvent(entity)));
        
        HeroCore.init();
        Network.S2CPayloads();
    }
}
