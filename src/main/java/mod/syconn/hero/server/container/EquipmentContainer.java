package mod.syconn.hero.server.container;

import mod.syconn.hero.utils.generic.NBTUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.Consumer;

public class EquipmentContainer implements Container {

    private final NonNullEnumMap gear = new NonNullEnumMap();
    private Consumer<Container> listener = null;

    public Map<EquipmentSlot, ItemStack> getGear() {
        return this.gear.values();
    }

    public boolean isGearFull() {
        return gear.values().values().stream().noneMatch(ItemStack::isEmpty);
    }

    public boolean isGearEmpty() {
        return gear.values().values().stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    public int getContainerSize() {
        return 4;
    }

    @Override
    public boolean isEmpty() {
        return this.gear.isEmpty() ;
    }

    @Override
    public @NotNull ItemStack getItem(int slot) {
        return this.gear.get(fromId(slot));
    }

    @Override
    public @NotNull ItemStack removeItem(int slot, int amount) {
        var stack = this.removeItemNoUpdate(slot);
        if (!stack.isEmpty()) this.setChanged();
        return stack;
    }

    @Override
    public @NotNull ItemStack removeItemNoUpdate(int slot) {
        var stack = this.gear.remove(fromId(slot));
        if (!stack.isEmpty()) this.setChanged();
        return stack;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        this.gear.set(fromId(slot), stack);
        this.setChanged();
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public void clearContent() {
        this.gear.clear();
        this.setChanged();
    }

    @Override
    public void setChanged() {
        if (listener != null) this.listener.accept(this);
    }

    public void addListener(Consumer<Container> listener) {
        this.listener = listener;
    }

    public void read(CompoundTag tag) {
        this.gear.setAll(NBTUtil.getMap(tag.getCompound("gear"), t -> NBTUtil.getEnum(EquipmentSlot.class, t), ItemStack::of));
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.put("gear", NBTUtil.putMap(this.gear.values(), NBTUtil::putEnum, s -> NBTUtil.convert(s::save)));
        return tag;
    }

    private EquipmentSlot fromId(int slot) {
        return EquipmentSlot.byTypeAndIndex(EquipmentSlot.Type.ARMOR, slot);
    }
}
