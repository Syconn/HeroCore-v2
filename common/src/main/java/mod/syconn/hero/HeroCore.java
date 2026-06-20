package mod.syconn.hero;

import dev.architectury.event.events.common.LifecycleEvent;
import mod.syconn.hero.core.ModEntities;
import mod.syconn.hero.core.ModItems;
import mod.syconn.hero.core.ModSounds;
import mod.syconn.hero.feature.heros.HeroRegistry;
import mod.syconn.hero.feature.heros.interfaces.IHeroHolder;
import mod.syconn.hero.feature.ironman.Ironman;
import mod.syconn.hero.network.Network;
import mod.syconn.hero.server.HeroServer;

public final class HeroCore {

    public static void init() {
        ModItems.TABS.register();
        ModItems.ITEMS.register();
        ModEntities.ENTITIES.register();
        ModSounds.SOUNDS.register();

        HeroRegistry.registerBuiltinHeroes();
        Network.init();
        LifecycleEvent.SETUP.register(HeroServer::init);
    }
}
