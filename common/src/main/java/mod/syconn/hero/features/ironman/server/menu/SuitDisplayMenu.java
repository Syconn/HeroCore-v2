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

        this.addSlot(new SpecificEquipmentSlot(blockEntity.getContainer(), 0, 26, 17, IronmanArmorItem.class, EquipmentSlot.HEAD));
        for(int l = 0; l < 3; ++l) for(int j1 = 0; j1 < 9; ++j1) this.addSlot(new Slot(inventory, j1 + l * 9 + 9, 8 + j1 * 18, 84 + l * 18));
        for(int i1 = 0; i1 < 9; ++i1) this.addSlot(new Slot(inventory, i1, 8 + i1 * 18, 142));
    }

    @Override
    public @NotNull ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY;
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
