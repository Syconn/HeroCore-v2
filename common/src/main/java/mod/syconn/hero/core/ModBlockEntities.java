package mod.syconn.hero.core;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import mod.syconn.hero.blockentities.SuitDisplayBlockEntity;
import mod.syconn.hero.utils.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ModBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Constants.MOD, Registries.BLOCK_ENTITY_TYPE);

    public static final RegistrySupplier<BlockEntityType<SuitDisplayBlockEntity>> SUIT_DISPLAY = BLOCK_ENTITIES.register("suit_display",
            () -> BlockEntityType.Builder.of(SuitDisplayBlockEntity::new, ModBlocks.SUIT_DISPLAY.get()).build(null));
}
