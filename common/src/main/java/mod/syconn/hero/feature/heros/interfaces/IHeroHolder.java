package mod.syconn.hero.feature.heros.interfaces;

import mod.syconn.hero.feature.heros.errors.DuplicateHeroException;
import mod.syconn.hero.feature.heros.HeroManager;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public interface IHeroHolder {

    Map<Class<? extends IHeroType>, IHeroType> CLASS_MAP = new HashMap<>();
    Map<ResourceLocation, IHeroType> ID_MAP = new HashMap<>();

    HeroManager hero$getManager();

    static void register(IHeroType type) {
        if (CLASS_MAP.containsKey(type.getClass())) throw new DuplicateHeroException(type.getClass());
        else if (ID_MAP.containsKey(type.id())) throw new DuplicateHeroException(type.id());

        type.register();
        CLASS_MAP.putIfAbsent(type.getClass(), type);
        ID_MAP.putIfAbsent(type.id(), type);
    }
}
