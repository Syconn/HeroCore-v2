package mod.syconn.hero.core;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import mod.syconn.hero.features.ironman.block.SuitDisplayBlock;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.function.BiFunction;
import java.util.function.Supplier;

import static mod.syconn.hero.utils.Constants.MOD;

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(MOD, Registries.BLOCK);

    public static final RegistrySupplier<Block> SUIT_DISPLAY = register("suit_display", SuitDisplayBlock::new);

    private static <T extends Block> RegistrySupplier<T> register(String id, Supplier<T> supplier) {
        return register(id, supplier, BlockItem::new);
    }

    private static <B extends Block, I extends BlockItem> RegistrySupplier<B> register(String id, Supplier<B> blockSupplier, BiFunction<B, Item.Properties, I> itemSupplier) {
        RegistrySupplier<B> registeredBlock = BLOCKS.register(id, blockSupplier);
        ModItems.ITEMS.register(id, () -> itemSupplier.apply(registeredBlock.get(), new Item.Properties().arch$tab(ModItems.TAB)));
        return registeredBlock;
    }
}
