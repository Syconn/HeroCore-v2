package mod.syconn.hero.server.container.slot;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class SpecificItemSlot extends Slot {

    private final Class<? extends Item> type;

    public SpecificItemSlot(Container container, int index, int xPosition, int yPosition, Class<? extends Item> type) {
        super(container, index, xPosition, yPosition);
        this.type = type;
    }

    public boolean mayPlace(@NotNull ItemStack stack) {
        return type.isInstance(stack.getItem());
    }
}
