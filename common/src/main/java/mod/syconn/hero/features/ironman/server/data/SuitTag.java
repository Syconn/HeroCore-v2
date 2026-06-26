package mod.syconn.hero.features.ironman.server.data;

import mod.syconn.hero.core.ModArmors;
import mod.syconn.hero.features.addons.IronmanContent;
import mod.syconn.hero.utils.Constants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;
import java.util.function.Function;

public class SuitTag {

    private static final String ID = "suitData";
    private static final byte TRANSITION_TICKS = 6;

    public ResourceLocation model;
    public int version;
    public int color;
    public boolean lifted;
    public byte liftTransition;
    public boolean open;
    public byte openTransition;

    public SuitTag(ResourceLocation model, int version, int color) {
        this.model = model;
        this.version = version;
        this.color = color;
    }

    public SuitTag(CompoundTag tag) {
        this.model = new ResourceLocation(tag.getString("model"));
        this.version = tag.getInt("version");
        this.color = tag.getInt("color");
        updateData(tag.getInt("version"));
        this.lifted = tag.getBoolean("lifted");
        this.liftTransition = tag.getByte("liftTransition");
        this.open = tag.getBoolean("open");
        this.openTransition = tag.getByte("openTransition");
    }

    private void updateData(int value) {
        this.version = value;
        var saved = IronmanContent.SUITS.get(this.model);
        if (saved == null) System.out.println("Invalid Suit Tag for " + this.model);
        else if (this.version != saved.version()) {
            var tag = saved.toTag().save();
            this.model = tag.contains("model") ? new ResourceLocation(tag.getString("model")) : Constants.withId("suits/mark_2");
            this.color = tag.getInt("color");
            this.version = saved.version();
        }
    }

    public void tick() {
        if (this.liftTransition != 0) {
            if (this.liftTransition > 0) this.liftTransition--;
            if (this.liftTransition < 0) this.liftTransition++;
        }
        if (this.openTransition != 0) {
            if (this.openTransition > 0) this.openTransition--;
            if (this.openTransition < 0) this.openTransition++;
        }
    }

    public void openCloseHelmet() {
        if (this.liftTransition != 0) return;
        this.liftTransition = TRANSITION_TICKS;
        this.lifted = !this.lifted;
    }

    public byte getHelmetFrame() {
        if (liftTransition == 0) return this.lifted ? TRANSITION_TICKS : 0;
        return this.lifted ? (byte) (6 - liftTransition) : liftTransition;
    }

    public void openCloseSuit() {
        if (this.openTransition != 0) return;
        this.openTransition = TRANSITION_TICKS;
        this.open = !this.open;
    }

    public byte getOpenSuit() {
        if (openTransition == 0) return !this.open ? TRANSITION_TICKS : 0;
        return !this.open ? (byte) (6 - openTransition) : openTransition;
    }

    public ArmorMaterial getMaterial() {
        return ModArmors.IRONMAN_TYPES.getOrDefault(this.model, ModArmors.MARK_2);
    }

    public ItemStack change(ItemStack stack) {
        stack.getOrCreateTag().put(ID, save());
        return stack;
    }

    public CompoundTag save() {
        var tag = new CompoundTag();
        tag.putString("model", this.model.toString());
        tag.putInt("version", this.version);
        tag.putInt("color", this.color);
        tag.putBoolean("lifted", this.lifted);
        tag.putByte("liftTransition", this.liftTransition);
        tag.putBoolean("open", this.open);
        tag.putByte("openTransition", this.openTransition);
        return tag;
    }

    public static SuitTag getOrCreate(ItemStack stack) {
        if (!stack.getOrCreateTag().contains(ID)) return create(stack);
        return new SuitTag(stack.getOrCreateTag().getCompound(ID));
    }

    public static void update(ItemStack stack, Consumer<SuitTag> consumer) {
        var lT = getOrCreate(stack);
        consumer.accept(lT);
        lT.change(stack);
    }

    public static void updateIf(ItemStack stack, Function<SuitTag, Boolean> condition, Consumer<SuitTag> consumer) {
        var lT = getOrCreate(stack);
        if (condition.apply(lT)) {
            consumer.accept(lT);
            lT.change(stack);
        }
    }

    private static SuitTag create(ItemStack stack) {
        var lT = IronmanContent.SUITS.get(Constants.withId("suits/mark_2")).toTag();
        lT.change(stack);
        return lT;
    }
}
