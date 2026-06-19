package mod.syconn.hero.feature.heros.interfaces;

import mod.syconn.hero.feature.heros.errors.AbilityRegistrationError;
import mod.syconn.hero.feature.heros.errors.DuplicateAbilityException;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public interface IHeroType {

    Component getName();
    ResourceLocation id();
    void register();
    List<IHeroAbility> getAbilities();
    IHeroAbility getAbility(ResourceLocation id);
    <T extends IHeroAbility> T getAbility(Class<T> clazz);

    default void clientTick(Player player) {
        for (var ability : this.getAbilities()) ability.clientTick(player);
    }

    default void serverTick(Player player) {
        for (var ability : this.getAbilities()) {
            if (ability instanceof IServerSynced sync) {
                sync.syncPlayer(player);
                sync.serverTick(player);
            }
        }
    }

    default boolean enabled() {
        return true;
    }

    default <T extends IHeroAbility> T getAbilityHelper(Class<T> clazz, Map<ResourceLocation, IHeroAbility> abilities, Map<Class<? extends IHeroAbility>, ResourceLocation> ids) {
        var loc = ids.get(clazz);
        if (loc == null) throw new AbilityRegistrationError(clazz, this);
        var ability = abilities.get(loc);
        if (!clazz.isInstance(ability)) throw new ClassCastException("Registered ability for " + clazz + " is not of expected type: " + ability.getClass());
        return clazz.cast(ability);
    }

    static void register(Map<ResourceLocation, IHeroAbility> abilityRegistry, Map<Class<? extends IHeroAbility>, ResourceLocation> idRegistry, IHeroAbility... abilities) {
        for (IHeroAbility ability : abilities) {
            if (abilityRegistry.containsKey(ability.id())) throw new DuplicateAbilityException(ability.id());
            else if (idRegistry.containsKey(ability.getClass())) throw new DuplicateAbilityException(ability.getClass());

            abilityRegistry.put(ability.id(), ability);
            idRegistry.put(ability.getClass(), ability.id());
        }
    }
}
