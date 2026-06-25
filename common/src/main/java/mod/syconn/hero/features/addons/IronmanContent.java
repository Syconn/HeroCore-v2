package mod.syconn.hero.features.addons;

import mod.syconn.hero.features.ironman.server.data.SuitJson;
import mod.syconn.hero.utils.Constants;
import mod.syconn.hero.utils.server.JsonResourceReloader;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class IronmanContent {

    public static final JsonResourceReloader<SuitJson> SUITS = new JsonResourceReloader<>(Constants.withId("suits"), "suits", SuitJson::fromJson, SuitJson::readTag, "models/item/suits");

    public static List<ItemStack> createArmorTypes() {
        var list = new ArrayList<ItemStack>();
        SUITS.sets().forEach(entry -> list.addAll(entry.getValue().toItems().values()));
        return list;
    }

    public static Map<EquipmentSlot, ItemStack> createSuitMap(ResourceLocation model) {
        return SUITS.get(model).toItems();
    }
}
