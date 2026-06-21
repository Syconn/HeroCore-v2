package mod.syconn.hero.feature.ironman.server.data;

import com.google.gson.JsonObject;
import mod.syconn.hero.utils.interfaces.ISerializable;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public record SuitJson(ResourceLocation model, TagKey<Item> tagKey, int version, int color) implements ISerializable<CompoundTag> {

    public SuitTag toTag() {
        return new SuitTag(this.model, this.tagKey, this.version, this.color);
    }

//    public ItemStack toItem() {
//        var stack = new ItemStack(ModItems.LIGHTSABER.get());
//        return toTag().change(stack);
//    }

    @Override
    public CompoundTag writeTag() {
        CompoundTag tag = new CompoundTag();
        tag.putString("model", this.model.toString());
        tag.putString("tag", tagKey.location().toString());
        tag.putInt("version", this.version);
        tag.putInt("color", this.color);
        return tag;
    }

    public static SuitJson readTag(CompoundTag tag) {
        return new SuitJson(new ResourceLocation(tag.getString("model")), TagKey.create(Registries.ITEM, new ResourceLocation(tag.getString("tag"))), tag.getInt("version"), tag.getInt("color"));
    }

    @Override
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("model", this.model.toString());
        json.addProperty("tag", this.tagKey.location().toString());
        json.addProperty("version", this.version);
        json.addProperty("color", String.format("%06X", this.color));
        return json;
    }

    public static SuitJson fromJson(JsonObject json) {
        return new SuitJson(new ResourceLocation(json.get("model").getAsString()), TagKey.create(Registries.ITEM, new ResourceLocation(json.get("tag").getAsString())), json.get("version").getAsInt(),
                Integer.parseInt(json.get("color").getAsString(), 16));
    }
}
