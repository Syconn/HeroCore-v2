package mod.syconn.hero.registrar;

import mod.syconn.hero.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.equipment.EquipmentAsset;

import static net.minecraft.world.item.equipment.EquipmentAssets.ROOT_ID;

public class TagRegistrar {
    
    public static TagKey<Item> TITANIUM_PLATE = TagKey.create(Registries.ITEM, Constants.withId("titanium_plate"));

    public static final ResourceKey<EquipmentAsset> MARK_42 = ResourceKey.create(ROOT_ID, Constants.withId("mark_42"));
}
