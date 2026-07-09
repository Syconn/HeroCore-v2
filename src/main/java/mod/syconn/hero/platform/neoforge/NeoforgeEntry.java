package mod.syconn.hero.platform.neoforge;//? if neoforge {
/*package mod.syconn.swm.loaders.neoforge;

import mod.syconn.swm.StarWars;
import mod.syconn.swm.api.registry.Registrar;
import mod.syconn.swm.utils.Constants;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.RegisterEvent;

@Mod(Constants.MOD)
public class NeoforgeEntrypoint {

    public NeoforgeEntrypoint(IEventBus context) {
        StarWars.initialize();
        context.addListener(this::onRegister);
    }

    public void onRegister(RegisterEvent event) {
        Registrar.get(event.getRegistryKey()).forEach(entry -> entry.register(event::register));
    }
}
*///?}
