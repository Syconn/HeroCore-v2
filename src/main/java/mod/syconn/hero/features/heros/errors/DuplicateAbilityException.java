package mod.syconn.hero.features.heros.errors;

import mod.syconn.hero.features.heros.interfaces.IHeroAbility;
import net.minecraft.resources.ResourceLocation;

public class DuplicateAbilityException extends RuntimeException {
    public DuplicateAbilityException(Class<? extends IHeroAbility> heroType) {
        super("Illegal Duplicate Ability Class -" + heroType + "- registered");
    }

    public DuplicateAbilityException(ResourceLocation id) {
        super("Illegal Duplicate Ability Id -" + id + "- registered");
    }
}
