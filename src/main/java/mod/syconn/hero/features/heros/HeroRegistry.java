package mod.syconn.hero.features.heros;

import mod.syconn.hero.features.heros.interfaces.IHeroHolder;
import mod.syconn.hero.features.ironman.Ironman;

public class HeroRegistry { // TODO ARCH REGISTRY

    public static void registerBuiltinHeroes() {
        IHeroHolder.register(Ironman::new);
    }
}
