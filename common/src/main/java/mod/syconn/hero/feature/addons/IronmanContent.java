package mod.syconn.hero.feature.addons;

import mod.syconn.hero.feature.ironman.server.data.SuitJson;
import mod.syconn.hero.utils.Constants;
import mod.syconn.hero.utils.server.JsonResourceReloader;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class IronmanContent {

    public static final JsonResourceReloader<SuitJson> SUITS = new JsonResourceReloader<>(Constants.withId("suits"), "suits", SuitJson::fromJson, SuitJson::readTag, "models/item/suits");

    public static List<ItemStack> createArmorTypes() {
        var list = new ArrayList<ItemStack>();
        SUITS.sets().forEach(entry -> list.addAll(entry.getValue().toItems()));
        return list;
    }
}
