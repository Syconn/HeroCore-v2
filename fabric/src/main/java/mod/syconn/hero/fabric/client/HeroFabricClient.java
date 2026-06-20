package mod.syconn.hero.fabric.client;

import mod.syconn.hero.client.HeroClient;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class HeroFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        HeroClient.init();
        ClientTickEvents.END_CLIENT_TICK.register(client -> HeroClient.onClientTick(client.player));
    }
}
