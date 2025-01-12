package mod.syconn.hero.client;

import mod.syconn.hero.network.Network;
import mod.syconn.hero.network.messages.MessageSuitPropel;
import mod.syconn.hero.util.Helpers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;

public class ClientHandler {

    public static void onClientPlayerTick(Minecraft minecraft) {
        LocalPlayer player = minecraft.player;
        
        if (player != null) handleMappings(player);
    }
    
    private static void handleMappings(LocalPlayer player) {
        if (Minecraft.getInstance().options.keyJump.isDown() && Helpers.isWearingIronManSuit(player))
            Network.CHANNEL.sendToServer(new MessageSuitPropel());
    }
}
