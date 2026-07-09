package mod.syconn.hero.server.container;

import mod.syconn.hero.utils.generic.MapUtil;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class NonNullEnumMap {

    private final Map<EquipmentSlot, ItemStack> map;

    public NonNullEnumMap() {
        this.map = MapUtil.make(new HashMap<>(), map -> {
            map.put(EquipmentSlot.FEET, ItemStack.EMPTY);
            map.put(EquipmentSlot.LEGS, ItemStack.EMPTY);
            map.put(EquipmentSlot.CHEST, ItemStack.EMPTY);
            map.put(EquipmentSlot.HEAD, ItemStack.EMPTY);
        });
    }

    public boolean isEmpty() {
        for (var stack : map.values()) if (!stack.isEmpty()) return false;
        return true;
    }

    public void setAll(Map<EquipmentSlot, ItemStack> map) {
        if (!map.isEmpty()) {
            this.clear();
            this.map.putAll(map);
        }
    }

    public void set(EquipmentSlot id, ItemStack stack) {
        this.map.put(id, stack);
    }

    public ItemStack remove(EquipmentSlot id) {
        return this.map.put(id, ItemStack.EMPTY);
    }

    public ItemStack get(EquipmentSlot id) {
        return this.map.get(id);
    }

    public Map<EquipmentSlot, ItemStack> values() {
        return this.map;
    }

    public void clear() {
        this.map.clear();
    }
}
