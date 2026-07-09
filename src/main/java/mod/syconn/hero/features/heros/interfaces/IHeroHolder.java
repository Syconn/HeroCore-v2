package mod.syconn.hero.features.heros.interfaces;

import mod.syconn.hero.features.heros.HeroManager;
import mod.syconn.hero.features.heros.errors.DuplicateHeroException;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public interface IHeroHolder {

    Map<Class<? extends IHeroType>, Supplier<? extends IHeroType>> CLASS_MAP = new HashMap<>();
    Map<ResourceLocation, Supplier<? extends IHeroType>> ID_MAP = new HashMap<>();

    HeroManager hero$getManager();

    static void register(Supplier<? extends IHeroType> type) {
        if (CLASS_MAP.containsKey(type.get().getClass())) throw new DuplicateHeroException(type.get().getClass());
        else if (ID_MAP.containsKey(type.get().id())) throw new DuplicateHeroException(type.get().id());
        CLASS_MAP.putIfAbsent(type.get().getClass(), type);
        ID_MAP.putIfAbsent(type.get().id(), type);
    }
}
