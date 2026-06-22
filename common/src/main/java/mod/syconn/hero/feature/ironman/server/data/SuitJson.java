package mod.syconn.hero.feature.ironman.server.data;

import com.google.gson.JsonObject;
import mod.syconn.hero.core.ModItems;
import mod.syconn.hero.utils.interfaces.ISerializable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public record SuitJson(ResourceLocation model, int version, int color) implements ISerializable<CompoundTag> {

    public SuitTag toTag() {
        return new SuitTag(this.model.withPath("suits/" + this.model.getPath()), this.version, this.color);
    }

    public List<ItemStack> toItems() {
        var set = new ArrayList<ItemStack>();
        set.add(toTag().change(new ItemStack(ModItems.IRONMAN_HELMET.get())));
        set.add(toTag().change(new ItemStack(ModItems.IRONMAN_CHESTPLATE.get())));
        set.add(toTag().change(new ItemStack(ModItems.IRONMAN_LEGGINGS.get())));
        set.add(toTag().change(new ItemStack(ModItems.IRONMAN_BOOTS.get())));
        return set;
    }

    @Override
    public CompoundTag writeTag() {
        CompoundTag tag = new CompoundTag();
        tag.putString("model", this.model.toString());
        tag.putInt("version", this.version);
        tag.putInt("color", this.color);
        return tag;
    }

    public static SuitJson readTag(CompoundTag tag) {
        return new SuitJson(new ResourceLocation(tag.getString("model")), tag.getInt("version"), tag.getInt("color"));
    }

    @Override
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("model", this.model.toString());
        json.addProperty("version", this.version);
        json.addProperty("color", String.format("%06X", this.color));
        return json;
    }

    public static SuitJson fromJson(JsonObject json) {
        return new SuitJson(new ResourceLocation(json.get("model").getAsString()), json.get("version").getAsInt(), Integer.parseInt(json.get("color").getAsString(), 16));
    }
}
