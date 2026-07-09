package mod.syconn.hero;

import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.ReloadListenerRegistry;
import dev.architectury.utils.Env;
import dev.architectury.utils.EnvExecutor;
import eu.midnightdust.lib.config.MidnightConfig;
import mod.syconn.hero.client.HeroClient;
import mod.syconn.hero.core.*;
import mod.syconn.hero.features.addons.IronmanContent;
import mod.syconn.hero.features.heros.HeroRegistry;
import mod.syconn.hero.network.Network;
import mod.syconn.hero.server.HeroServer;
import mod.syconn.hero.utils.HeroConfig;
import mod.syconn.hero.utils.Constants;
import mod.syconn.hero.utils.server.SyncedResourceManager;
import net.minecraft.server.packs.PackType;

public final class HeroCore {

    @SuppressWarnings("UnstableApiUsage")
    public static void init() {
        ModItems.TABS.register();
        ModBlocks.BLOCKS.register();
        ModItems.ITEMS.register();
        ModEntities.ENTITIES.register();
        ModBlockEntities.BLOCK_ENTITIES.register();
        ModMenus.MENUS.register();
        ModSounds.SOUNDS.register();
        ModParticles.PARTICLES.register();

        Network.init();
        CreativeTabRegistry.modify(ModItems.TAB, ModItems::addCreative);
        ReloadListenerRegistry.register(PackType.SERVER_DATA, IronmanContent.SUITS, Constants.withId("suit_data"));
        SyncedResourceManager.register(IronmanContent.SUITS);
        MidnightConfig.init(Constants.MOD, HeroConfig.class);

        HeroRegistry.registerBuiltinHeroes();
        EnvExecutor.runInEnv(Env.CLIENT, () -> HeroClient::init);
        LifecycleEvent.SETUP.register(HeroServer::init);
    }
}
