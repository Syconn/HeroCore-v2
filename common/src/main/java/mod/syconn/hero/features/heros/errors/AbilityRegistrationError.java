package mod.syconn.hero.features.heros.errors;

import mod.syconn.hero.features.heros.interfaces.IHeroAbility;
import mod.syconn.hero.features.heros.interfaces.IHeroType;

public class AbilityRegistrationError extends RuntimeException {
    public AbilityRegistrationError(Class<? extends IHeroAbility> ability, IHeroType type) {
        super("Ability " + ability + " is not registered for " + type);
    }
}
