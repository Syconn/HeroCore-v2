package mod.syconn.hero.features.heros;

import mod.syconn.hero.features.heros.interfaces.IHeroHolder;
import mod.syconn.hero.features.heros.interfaces.IHeroType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.Map;

public class HeroManager {

    private final Map<Class<? extends IHeroType>, IHeroType> types = new HashMap<>();
    private final Map<ResourceLocation, IHeroType> ids = new HashMap<>();

    public HeroManager() {
        for (var entry : IHeroHolder.CLASS_MAP.entrySet()) {
            IHeroType type = entry.getValue().get();
            type.initializeAbilities();
            types.put(type.getClass(), type);
            ids.put(type.id(), type);
        }
    }

    public void clientTick(Player player) {
        for (var type : types.values()) type.clientTick(player);
    }

    public void serverTick(Player player) {
        for (var type : types.values()) type.serverTick(player);
    }

    @SuppressWarnings("unchecked")
    public <T extends IHeroType> T getType(Class<T> clazz) {
        return (T) types.get(clazz);
    }

    public IHeroType getType(ResourceLocation id) {
        return ids.get(id);
    }
}
