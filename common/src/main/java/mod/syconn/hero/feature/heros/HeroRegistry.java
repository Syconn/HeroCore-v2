package mod.syconn.hero.feature.heros;

import mod.syconn.hero.feature.heros.interfaces.IHeroHolder;
import mod.syconn.hero.feature.ironman.Ironman;

public class HeroRegistry {

    public static void registerBuiltinHeroes() {
        IHeroHolder.register(new Ironman());
    }
}
