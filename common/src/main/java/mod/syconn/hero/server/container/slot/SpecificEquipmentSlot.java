package mod.syconn.hero.server.container.slot;

import com.mojang.datafixers.util.Pair;
import mod.syconn.hero.utils.Constants;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SpecificEquipmentSlot extends SpecificItemSlot {

    private final EquipmentSlot slot;

    public SpecificEquipmentSlot(Container container, int index, int xPosition, int yPosition, Class<? extends Item> type, EquipmentSlot slot) {
        super(container, index, xPosition, yPosition, type);
        this.slot = slot;
    }

    @Override
    public @Nullable Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
        return switch (slot) {
            case FEET -> Pair.of(InventoryMenu.BLOCK_ATLAS, InventoryMenu.EMPTY_ARMOR_SLOT_BOOTS);
            case LEGS -> Pair.of(InventoryMenu.BLOCK_ATLAS, InventoryMenu.EMPTY_ARMOR_SLOT_LEGGINGS);
            case CHEST -> Pair.of(InventoryMenu.BLOCK_ATLAS, Constants.withId("custom/chestplate"));
            default -> Pair.of(InventoryMenu.BLOCK_ATLAS, Constants.withId("custom/helmet"));
        };
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return super.mayPlace(stack) && stack.getItem() instanceof Equipable equipable && equipable.getEquipmentSlot().equals(slot);
    }
}
