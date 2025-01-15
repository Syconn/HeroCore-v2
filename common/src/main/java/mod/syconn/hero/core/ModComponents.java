package mod.syconn.hero.core;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import mod.syconn.hero.common.components.EnergyComponent;
import mod.syconn.hero.common.components.SuitComponent;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;

import static mod.syconn.hero.Constants.MOD_ID;

public class ModComponents {

    public static final DeferredRegister<DataComponentType<?>> COMPONENTS = DeferredRegister.create(MOD_ID, Registries.DATA_COMPONENT_TYPE);

    public static final RegistrySupplier<DataComponentType<EnergyComponent>> ENERGY = COMPONENTS.register("energy", () -> DataComponentType.<EnergyComponent>builder()
            .networkSynchronized(EnergyComponent.STREAM_CODEC).persistent(EnergyComponent.CODEC).build());
    public static final RegistrySupplier<DataComponentType<SuitComponent>> SUIT_SETTINGS = COMPONENTS.register("suit_settings", () -> DataComponentType.<SuitComponent>builder()
            .networkSynchronized(SuitComponent.STREAM_CODEC).persistent(SuitComponent.CODEC).build());
}
