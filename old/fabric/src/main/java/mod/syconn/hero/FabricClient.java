package mod.syconn.hero;

import mod.syconn.hero.client.ClientHandler;
import mod.syconn.hero.extra.core.Keys;
import mod.syconn.hero.network.Network;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;

public class FabricClient implements ClientModInitializer {

    public void onInitializeClient() {
        Network.registerMessages();
        Network.C2SPayloads();
        ClientHandler.registerRenderers(EntityRendererRegistry::register, BlockEntityRenderers::register);
        Keys.registerMapping(KeyBindingHelper::registerKeyBinding);
        ClientTickEvents.END_CLIENT_TICK.register(client -> ClientHandler.onClientPlayerTick(client.player));
    }
}
