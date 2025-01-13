package mod.syconn.hero.client;

import mod.syconn.hero.network.Network;
import mod.syconn.hero.network.messages.MessageAlterHover;
import mod.syconn.hero.network.messages.MessageFlightMode;
import mod.syconn.hero.network.messages.MessageSuitPropel;
import mod.syconn.hero.util.AnimationUtil;
import mod.syconn.hero.util.Helpers;
import mod.syconn.hero.util.SuitSettings;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;

public class ClientHandler {

    private static SuitSettings settings = null;
    private static boolean updateAnimations = true;
    private static int ticksToClear;

    public static void onClientPlayerTick(Minecraft minecraft) {
        LocalPlayer player = minecraft.player;
        
        if (player != null) {
            handleMappings(player);

            if (updateAnimations) {
                SuitSettings settings = SuitSettings.from(player);

                if (settings.getFlightMode() == SuitSettings.FlightMode.HOVER) {
                    AnimationUtil.play(player, "start_hover");
                    AnimationUtil.play(player, "hover", 20);
                }
                updateAnimations = false;
            }
        }
    }

    private static void handleMappings(LocalPlayer player) {
        if (settings != null) ticksToClear++;
        if (ticksToClear > 100) {
            ticksToClear = 0;
            settings = null;
        }

        if (Minecraft.getInstance().options.keyJump.isDown())
            if (Helpers.isWearingIronManSuit(player) && SuitSettings.from(player).getFlightMode() == SuitSettings.FlightMode.FLY)
                Network.CHANNEL.sendToServer(new MessageSuitPropel(Minecraft.getInstance().options.keySprint.isDown()));

        if (Minecraft.getInstance().options.keyJump.isDown() || Minecraft.getInstance().options.keyShift.isDown())
            if (Helpers.isWearingIronManSuit(player) && SuitSettings.from(player).getFlightMode() == SuitSettings.FlightMode.HOVER)
                Network.CHANNEL.sendToServer(new MessageAlterHover(Minecraft.getInstance().options.keyJump.isDown()));

        while (KeyBindings.ABILITY1.consumeClick()) {
            if (settings == null) settings = SuitSettings.from(player);
            settings.cycleMode();
            player.displayClientMessage(Component.literal("Flight: " + settings.getFlightMode() + " CONFIRM: " + KeyBindings.key(KeyBindings.ABILITIES_MENU)).withStyle(ChatFormatting.GOLD), true);
        }

        while (KeyBindings.ABILITIES_MENU.consumeClick()) {
            AnimationUtil.play(player, "flight");
            if (settings != null) {
                Network.CHANNEL.sendToServer(new MessageFlightMode(settings.getFlightMode()));
                updateAnimations = true;
                settings = null;
            }
        }
    }
}
