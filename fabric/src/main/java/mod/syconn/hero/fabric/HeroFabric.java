package mod.syconn.hero.fabric;

import net.fabricmc.api.ModInitializer;

import mod.syconn.hero.HeroCore;

public final class HeroFabric implements ModInitializer {
    
    @Override
    public void onInitialize() {
        HeroCore.init();
    }
}
