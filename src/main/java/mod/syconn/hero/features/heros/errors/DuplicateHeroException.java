package mod.syconn.hero.features.heros.errors;

import mod.syconn.hero.features.heros.interfaces.IHeroType;
import net.minecraft.resources.ResourceLocation;

public class DuplicateHeroException extends RuntimeException {
    public DuplicateHeroException(Class<? extends IHeroType> heroType) {
        super("Illegal Duplicate Hero Class -" + heroType + "- registered");
    }

    public DuplicateHeroException(ResourceLocation id) {
        super("Illegal Duplicate Hero Id -" + id + "- registered");
    }
}
