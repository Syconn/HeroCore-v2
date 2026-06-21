package mod.syconn.hero.feature.ironman.server.data;

import mod.syconn.hero.feature.addons.IronmanContent;
import mod.syconn.hero.utils.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;

public class SuitTag {

    private static final String ID = "suitData";

    public ResourceLocation model;
    public TagKey<Item> tagKey;
    public int version;
    public int color;

    public SuitTag(ResourceLocation model, TagKey<Item> tagKey, int version, int color) {
        this.model = model;
        this.tagKey = tagKey;
        this.version = version;
        this.color = color;
    }

    public SuitTag(CompoundTag tag) {
        this.model = new ResourceLocation(tag.getString("model"));
        this.tagKey = TagKey.create(Registries.ITEM, new ResourceLocation(tag.getString("tag")));
        this.version = tag.getInt("version");
        this.color = tag.getInt("color");
        updateData(tag.getInt("version"));
    }

    private void updateData(int value) {
        this.version = value;
        var saved = IronmanContent.SUITS.get(this.model);
        if (saved == null) System.out.println("Invalid Lightsaber Tag for " + this.tagKey);
        else if (this.version != saved.version()) {
            var tag = saved.toTag().save();
            this.model = tag.contains("model") ? new ResourceLocation(tag.getString("model")): Constants.withId("mark_2");
            this.tagKey = TagKey.create(Registries.ITEM, new ResourceLocation(tag.getString("tag")));
            this.color = tag.getInt("color");
            this.version = saved.version();
        }
    }

    public ItemStack change(ItemStack stack) {
        stack.getOrCreateTag().put(ID, save());
        return stack;
    }

    public CompoundTag save() {
        var tag = new CompoundTag();
        tag.putString("model", this.model.toString());
        tag.putString("tag", tagKey.location().toString());
        tag.putInt("version", this.version);
        tag.putInt("color", this.color);
        return tag;
    }

    public static SuitTag getOrCreate(ItemStack stack) {
        if (!stack.getOrCreateTag().contains(ID)) return create(stack);
        return new SuitTag(stack.getOrCreateTag().getCompound(ID));
    }

    public static ItemStack update(ItemStack stack, Consumer<SuitTag> consumer) {
        var lT = getOrCreate(stack);
        consumer.accept(lT);
        return lT.change(stack);
    }

    private static SuitTag create(ItemStack stack) {
        var lT = IronmanContent.SUITS.get(Constants.withId("suits/mark_2")).toTag();
        lT.change(stack);
        return lT;
    }
}
