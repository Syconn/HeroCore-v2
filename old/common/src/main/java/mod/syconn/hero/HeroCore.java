package mod.syconn.hero;

import mod.syconn.hero.network.Network;
import mod.syconn.hero.core.AttachmentRegistrar;
import mod.syconn.hero.core.ComponentRegistrar;
import mod.syconn.hero.core.EntityRegistrar;
import mod.syconn.hero.core.ItemRegistrar;

public class HeroCore {

    public static void init() {
        Network.registerMessages();
        EntityRegistrar.init();
        ItemRegistrar.init();
        AttachmentRegistrar.init();
        ComponentRegistrar.init();
    }
}