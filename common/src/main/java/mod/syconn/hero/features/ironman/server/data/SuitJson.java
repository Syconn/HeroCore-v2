package mod.syconn.hero.features.ironman.server.data;

import com.google.gson.JsonObject;
import mod.syconn.hero.core.ModItems;
import mod.syconn.hero.utils.generic.MapUtil;
import mod.syconn.hero.utils.interfaces.ISerializable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record SuitJson(ResourceLocation model, int version, int color) implements ISerializable<CompoundTag> {

    public SuitTag toTag() {
        return new SuitTag(this.model.withPath("suits/" + this.model.getPath()), this.version, this.color);
    }

    public Map<EquipmentSlot, ItemStack> toItems() {
        return MapUtil.make(new HashMap<>(), map -> {
            map.put(EquipmentSlot.HEAD, toTag().change(new ItemStack(ModItems.IRONMAN_HELMET.get())));
            map.put(EquipmentSlot.CHEST, toTag().change(new ItemStack(ModItems.IRONMAN_CHESTPLATE.get())));
            map.put(EquipmentSlot.LEGS, toTag().change(new ItemStack(ModItems.IRONMAN_LEGGINGS.get())));
            map.put(EquipmentSlot.FEET, toTag().change(new ItemStack(ModItems.IRONMAN_BOOTS.get())));
        });
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
