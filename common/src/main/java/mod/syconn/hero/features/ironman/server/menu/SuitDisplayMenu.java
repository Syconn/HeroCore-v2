package mod.syconn.hero.features.ironman.server.menu;

import dev.architectury.registry.menu.MenuRegistry;
import mod.syconn.hero.core.ModBlockEntities;
import mod.syconn.hero.core.ModMenus;
import mod.syconn.hero.features.ironman.blockentity.SuitDisplayBlockEntity;
import mod.syconn.hero.features.ironman.item.IronmanArmorItem;
import mod.syconn.hero.server.container.slot.SpecificEquipmentSlot;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.NotNull;

public class SuitDisplayMenu extends AbstractContainerMenu {

    private final SuitDisplayBlockEntity blockEntity;

    public SuitDisplayMenu(int containerId, Inventory inventory, FriendlyByteBuf data) {
        this(containerId, inventory, data.readBlockPos());
    }

    public SuitDisplayMenu(int containerId, Inventory inventory, BlockPos pos){
        super(ModMenus.SUIT_DISPLAY.get(), containerId);
        this.blockEntity = inventory.player.level().getBlockEntity(pos, ModBlockEntities.SUIT_DISPLAY.get()).orElseThrow();

        this.addSlot(new SpecificEquipmentSlot(blockEntity.getContainer(), 3, 26, 17, IronmanArmorItem.class, EquipmentSlot.HEAD));
        this.addSlot(new SpecificEquipmentSlot(blockEntity.getContainer(), 2, 26, 53, IronmanArmorItem.class, EquipmentSlot.CHEST));
        this.addSlot(new SpecificEquipmentSlot(blockEntity.getContainer(), 1, 134, 17, IronmanArmorItem.class, EquipmentSlot.LEGS));
        this.addSlot(new SpecificEquipmentSlot(blockEntity.getContainer(), 0, 134, 53, IronmanArmorItem.class, EquipmentSlot.FEET));
        for(int l = 0; l < 3; ++l) for(int j1 = 0; j1 < 9; ++j1) this.addSlot(new Slot(inventory, j1 + l * 9 + 9, 8 + j1 * 18, 84 + l * 18));
        for(int i1 = 0; i1 < 9; ++i1) this.addSlot(new Slot(inventory, i1, 8 + i1 * 18, 142));
    }

    @Override
    public @NotNull ItemStack quickMoveStack(Player player, int index) {
        var empty = ItemStack.EMPTY;
        var slot = this.slots.get(index);
        if (!slot.hasItem()) return empty;

        var stack = slot.getItem();
        var original = stack.copy();
        if (index < 4 && !this.moveItemStackTo(stack, 4, this.slots.size(), true)) return ItemStack.EMPTY;
        else {
            if (stack.getItem() instanceof IronmanArmorItem armor && armor.getEquipmentSlot() == EquipmentSlot.HEAD && this.moveItemStackTo(stack, 0, 1, false)) return empty;
            if (stack.getItem() instanceof IronmanArmorItem armor && armor.getEquipmentSlot() == EquipmentSlot.CHEST && this.moveItemStackTo(stack, 1, 2, false)) return empty;
            if (stack.getItem() instanceof IronmanArmorItem armor && armor.getEquipmentSlot() == EquipmentSlot.LEGS && this.moveItemStackTo(stack, 2, 3, false)) return empty;
            if (stack.getItem() instanceof IronmanArmorItem armor && armor.getEquipmentSlot() == EquipmentSlot.FEET && this.moveItemStackTo(stack, 3, 4, false)) return empty;
            if (!this.moveItemStackTo(stack, 4, this.slots.size(), false)) return ItemStack.EMPTY;
        }

        if (stack.isEmpty()) slot.set(ItemStack.EMPTY);
        else slot.setChanged();
        if (stack.getCount() == original.getCount()) return ItemStack.EMPTY;
        slot.onTake(player, stack);
        return original;
    }

    @Override
    public boolean stillValid(Player player) {
        return blockEntity.getBlockState().getValue(BlockStateProperties.OPEN);
    }

    public SuitDisplayBlockEntity getBlockEntity() {
        return blockEntity;
    }

    public static void openMenu(Player player, BlockPos pos) {
        if (player instanceof ServerPlayer sp) MenuRegistry.openExtendedMenu(sp, menu(pos), buf -> buf.writeBlockPos(pos));
    }

    private static MenuProvider menu(BlockPos pos) {
        return new MenuProvider() {

            public @NotNull Component getDisplayName() {
                return Component.literal("Suit Display");
            }

            public @NotNull AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
                return new SuitDisplayMenu(i, inventory, pos);
            }
        };
    }
}
