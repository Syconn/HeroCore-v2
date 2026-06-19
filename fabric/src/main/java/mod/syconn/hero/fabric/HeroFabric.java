package mod.syconn.hero.fabric;

import mod.syconn.hero.HeroCore;
import net.fabricmc.api.ModInitializer;

public final class HeroFabric implements ModInitializer {
    
    public void onInitialize() {
        HeroCore.init();
    }
}
