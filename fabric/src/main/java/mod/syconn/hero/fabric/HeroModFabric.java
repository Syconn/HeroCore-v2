package mod.syconn.hero.fabric;

import mod.syconn.hero.HeroCore;
import net.fabricmc.api.ModInitializer;

public final class HeroModFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        HeroCore.init();
    }
}
