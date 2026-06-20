package mod.syconn.hero.feature.ironman;

import mod.syconn.hero.feature.heros.interfaces.IHeroAbility;
import mod.syconn.hero.feature.heros.interfaces.IHeroType;
import mod.syconn.hero.feature.ironman.abilities.FlightAbility;
import mod.syconn.hero.feature.ironman.abilities.FlipHelmetAbility;
import mod.syconn.hero.feature.ironman.abilities.IronmanVisorAbility;
import mod.syconn.hero.utils.Constants;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Ironman implements IHeroType {

    private final Map<ResourceLocation, IHeroAbility> abilities = new HashMap<>();
    private final Map<Class<? extends IHeroAbility>, ResourceLocation> ids = new HashMap<>();

    @Override
    public void initializeAbilities() {
        final FlipHelmetAbility flipHelmet = new FlipHelmetAbility(this);
        IHeroType.initializeAbilities(this.abilities, this.ids, flipHelmet);
        IHeroType.initializeAbilities(this.abilities, this.ids, new IronmanVisorAbility(this, flipHelmet));
        IHeroType.initializeAbilities(this.abilities, this.ids, new FlightAbility(this));
    }

    @Override
    public Component getName() {
        return Component.literal("Ironman");
    }

    @Override
    public ResourceLocation id() {
        return Constants.withId("ironman");
    }

    @Override
    public List<IHeroAbility> getAbilities() {
        return this.abilities.values().stream().toList();
    }

    @Override
    public IHeroAbility getAbility(ResourceLocation id) {
        return this.abilities.get(id);
    }

    @Override
    public <T extends IHeroAbility> T getAbility(Class<T> clazz) {
        return getAbilityHelper(clazz, this.abilities, this.ids);
    }
}
