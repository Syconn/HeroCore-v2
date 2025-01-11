package mod.syconn.hero.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;

public class ClientHandler {

    public static void onClientPlayerTick(Minecraft minecraft) {
        LocalPlayer player = minecraft.player;
        
        if (player != null) handleMappings(player);
    }
    
    private static void handleMappings(LocalPlayer player) {
//        while (CUSTOM_KEYMAPPING.consumeClick()) {
//            // Do action here
//        }
    }
}
