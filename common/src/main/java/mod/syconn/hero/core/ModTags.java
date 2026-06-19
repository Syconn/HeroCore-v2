package mod.syconn.hero.core;

import mod.syconn.hero.utils.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ModTags {
    
    public static TagKey<Item> IRONMAN_ARMOR = TagKey.create(Registries.ITEM, Constants.withId("ironman"));
    public static TagKey<Item> MARK_2 = TagKey.create(Registries.ITEM, Constants.withId("mark_2"));
    public static TagKey<Item> MARK_5 = TagKey.create(Registries.ITEM, Constants.withId("mark_5"));
    public static TagKey<Item> MARK_42 = TagKey.create(Registries.ITEM, Constants.withId("mark_42"));
    public static TagKey<Item> MARK_43 = TagKey.create(Registries.ITEM, Constants.withId("mark_43"));
    public static TagKey<Item> WAR_MACHINE = TagKey.create(Registries.ITEM, Constants.withId("war_machine"));
}
