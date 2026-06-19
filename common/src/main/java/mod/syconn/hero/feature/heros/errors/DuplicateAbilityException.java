package mod.syconn.hero.feature.heros.errors;

import mod.syconn.hero.feature.heros.interfaces.IHeroAbility;
import net.minecraft.resources.ResourceLocation;

public class DuplicateAbilityException extends RuntimeException {
    public DuplicateAbilityException(Class<? extends IHeroAbility> heroType) {
        super("Illegal Duplicate Ability Class -" + heroType + "- registered");
    }

    public DuplicateAbilityException(ResourceLocation id) {
        super("Illegal Duplicate Ability Id -" + id + "- registered");
    }
}
