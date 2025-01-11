package mod.syconn.hero;

import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.event.events.common.EntityEvent;
import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import mod.syconn.hero.client.ClientHandler;
import mod.syconn.hero.client.KeyBindings;
import mod.syconn.hero.client.render.entity.MjolnirRenderer;
import mod.syconn.hero.common.CommonHandler;
import mod.syconn.hero.core.ModEntities;
import mod.syconn.hero.core.ModItems;
import mod.syconn.hero.network.Network;

public final class HeroCore {

    public static void init() {
        ModItems.TABS.register();
        ModItems.ITEMS.register();
        ModEntities.ENTITIES.register();

        ClientTickEvent.CLIENT_PRE.register(ClientHandler::onClientPlayerTick);
        EntityEvent.LIVING_HURT.register(CommonHandler::entityHurtEvent);

        EntityRendererRegistry.register(ModEntities.MJOLNIR_ENTITY_TYPE, MjolnirRenderer::new);

        KeyBindings.registerMappings();
        Network.init();
    }
}
