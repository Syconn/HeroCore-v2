package mod.syconn.hero.features.heros.core;

import mod.syconn.hero.features.heros.interfaces.IHeroHolder;
import mod.syconn.hero.features.ironman.Ironman;

public class HeroRegistry {

    public static void registerBuiltinHeroes() {
        IHeroHolder.register(Ironman::new);
    }
}
