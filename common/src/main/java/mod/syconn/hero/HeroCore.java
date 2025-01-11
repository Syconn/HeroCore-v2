package mod.syconn.hero;

import mod.syconn.hero.client.KeyBindings;
import mod.syconn.hero.core.ModEntities;
import mod.syconn.hero.core.ModItems;
import mod.syconn.hero.network.Network;

public final class HeroCore {

    public static void init() {
        ModItems.TABS.register();
        ModItems.ITEMS.register();
        ModEntities.ENTITIES.register();

        KeyBindings.registerMappings();
        Network.init();
    }
}
