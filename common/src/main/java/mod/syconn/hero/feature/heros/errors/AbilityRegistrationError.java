package mod.syconn.hero.feature.heros.errors;

import mod.syconn.hero.feature.heros.interfaces.IHeroAbility;
import mod.syconn.hero.feature.heros.interfaces.IHeroType;

public class AbilityRegistrationError extends RuntimeException {
    public AbilityRegistrationError(Class<? extends IHeroAbility> ability, IHeroType type) {
        super("Ability " + ability + " is not registered for " + type);
    }
}
