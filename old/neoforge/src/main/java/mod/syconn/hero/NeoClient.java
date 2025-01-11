package mod.syconn.hero;

import mod.syconn.hero.client.ClientHandler;
import mod.syconn.hero.extra.core.Keys;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;

@EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class NeoClient {

    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        ClientHandler.registerRenderers(event::registerEntityRenderer, event::registerBlockEntityRenderer);
    }
    
    @SubscribeEvent
    public static void registerBindings(RegisterKeyMappingsEvent event) {
        Keys.registerMapping(event::register);
    }
}
