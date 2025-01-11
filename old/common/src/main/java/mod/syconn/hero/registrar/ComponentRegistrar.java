package mod.syconn.hero.registrar;

import mod.syconn.hero.extra.data.components.EnergyComponent;
import mod.syconn.hero.extra.platform.Services;
import net.minecraft.core.component.DataComponentType;
import java.util.function.Supplier;

public class ComponentRegistrar {
    
    public static Supplier<DataComponentType<EnergyComponent>> ENERGY_COMPONENT = register("energy_component", () ->
            DataComponentType.<EnergyComponent>builder().persistent(EnergyComponent.CODEC).networkSynchronized(EnergyComponent.STREAM_CODEC).build());

    public static void init() {}

    private static <T> Supplier<DataComponentType<T>> register(String id, Supplier<DataComponentType<T>> componentSupplier) {
        return Services.REGISTRAR.registerDataComponent(id, componentSupplier);
    }
}
