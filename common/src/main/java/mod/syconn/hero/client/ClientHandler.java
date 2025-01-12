package mod.syconn.hero.client;

import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.api.layered.modifier.AbstractFadeModifier;
import dev.kosmx.playerAnim.core.util.Ease;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import mod.syconn.hero.Constants;
import mod.syconn.hero.network.Network;
import mod.syconn.hero.network.messages.MessageAlterHover;
import mod.syconn.hero.network.messages.MessageFlightMode;
import mod.syconn.hero.network.messages.MessageSuitPropel;
import mod.syconn.hero.util.AnimationUtil;
import mod.syconn.hero.util.Helpers;
import mod.syconn.hero.util.SuitSettings;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class ClientHandler {

    private static SuitSettings settings = null;
    private static int ticksToClear;

    public static void onClientPlayerTick(Minecraft minecraft) {
        LocalPlayer player = minecraft.player;
        
        if (player != null) {
            handleMappings(player);
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
            if (Helpers.isWearingIronManSuit(player) &&SuitSettings.from(player).getFlightMode() == SuitSettings.FlightMode.HOVER)
                Network.CHANNEL.sendToServer(new MessageAlterHover(Minecraft.getInstance().options.keyJump.isDown()));

        while (KeyBindings.ABILITY1.consumeClick()) {
            AnimationUtil.stop(player);
            if (settings == null) settings = SuitSettings.from(player);
            settings.cycleMode();
            player.displayClientMessage(Component.literal("Flight: " + settings.getFlightMode() + " CONFIRM: " + KeyBindings.key(KeyBindings.ABILITIES_MENU)).withStyle(ChatFormatting.GOLD), true);
        }

        while (KeyBindings.ABILITIES_MENU.consumeClick()) {
            AnimationUtil.play(player, "hover_start");
            AnimationUtil.play(player, "hover", 20);

            if (settings != null) {
                Network.CHANNEL.sendToServer(new MessageFlightMode(settings.getFlightMode()));
                settings = null;
            }
        }
    }
}
