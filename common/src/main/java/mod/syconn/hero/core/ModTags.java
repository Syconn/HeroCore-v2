package mod.syconn.hero.core;

import mod.syconn.hero.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ModTags {
    
    public static TagKey<Item> TITANIUM_PLATE = TagKey.create(Registries.ITEM, Constants.withId("titanium_plate"));
    
}
