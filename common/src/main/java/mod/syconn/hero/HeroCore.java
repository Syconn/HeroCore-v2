package mod.syconn.hero;

import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.event.events.common.EntityEvent;
import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.event.events.common.TickEvent;
import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import dev.architectury.utils.Env;
import dev.architectury.utils.EnvExecutor;
import mod.syconn.hero.client.ClientHandler;
import mod.syconn.hero.client.KeyBindings;
import mod.syconn.hero.client.render.entity.MjolnirRenderer;
import mod.syconn.hero.common.CommonHandler;
import mod.syconn.hero.core.ModEntities;
import mod.syconn.hero.core.ModItems;
import mod.syconn.hero.network.Network;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public final class HeroCore {

    public static void init() {
        ModItems.TABS.register();
        ModItems.ITEMS.register();
        ModEntities.ENTITIES.register();
        
        EntityEvent.LIVING_HURT.register(CommonHandler::entityHurtEvent);
        TickEvent.PLAYER_PRE.register(CommonHandler::onPlayerTick);

        KeyBindings.registerMappings();
        Network.init();
        EnvExecutor.runInEnv(Env.CLIENT, () -> HeroCore.Client::initClient);
    }

    @Environment(EnvType.CLIENT)
    public static class Client {
        
        @Environment(EnvType.CLIENT)
        public static void initClient() {
            ClientTickEvent.CLIENT_PRE.register(ClientHandler::onClientPlayerTick);
            
            EntityRendererRegistry.register(ModEntities.MJOLNIR, MjolnirRenderer::new);
        }
    }
}
