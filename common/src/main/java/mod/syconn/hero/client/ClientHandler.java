package mod.syconn.hero.client;

import mod.syconn.hero.network.Network;
import mod.syconn.hero.network.messages.MessageAlterHover;
import mod.syconn.hero.network.messages.MessageUpdateSuitSettings;
import mod.syconn.hero.network.messages.MessageSuitPropel;
import mod.syconn.hero.util.AnimationUtil;
import mod.syconn.hero.util.ItemUtil;
import mod.syconn.hero.util.data.SuitSettings;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;

public class ClientHandler {

    private static SuitSettings settings = null;
    private static boolean updateAnimations = true;
    private static boolean grounded = true;
    private static boolean held = false;
    private static int ticksToClear1;
    private static int ticks;

    public static void onClientPlayerTick(Minecraft minecraft) {
        LocalPlayer player = minecraft.player;
        
        if (player != null) {
            handleMappings(player);

            if (updateAnimations) {
                SuitSettings settings = SuitSettings.from(player);
                if (!player.level().getBlockState(player.getOnPos()).isAir()) {
                    AnimationUtil.stop(player);
                } else if (settings.getFlightMode() == SuitSettings.FlightMode.HOVER && !AnimationUtil.isAnimated(player)) {
                    AnimationUtil.play(player, "start_hover");
                    AnimationUtil.play(player, "hover", 20);
                } else if (settings.getFlightMode() == SuitSettings.FlightMode.FLY && !AnimationUtil.isAnimated(player)) {
                    AnimationUtil.play(player, "flight");
                } else if (settings.getFlightMode() == SuitSettings.FlightMode.WALK) AnimationUtil.stop(player);
                updateAnimations = false;
            }

            if (!grounded && !player.level().getBlockState(player.getOnPos()).isAir()) {
                grounded = true;
                updateAnimations = true;
            }
        }
    }

    private static void handleMappings(LocalPlayer player) {
        if (settings != null) ticksToClear1++;
        if (ticksToClear1 > 100) {
            ticksToClear1 = 0;
            settings = null;
        }

        if (held) ticks++;
        if (ticks > 20) {
            ticks = 0;
            System.out.println("Done");
        }

        if (Minecraft.getInstance().options.keyJump.isDown()) {
            if (ItemUtil.isWearingIronManSuit(player) && SuitSettings.from(player).getFlightMode() == SuitSettings.FlightMode.FLY) {
                updateAnimations = true;
                grounded = false;
                Network.CHANNEL.sendToServer(new MessageSuitPropel(Minecraft.getInstance().options.keySprint.isDown()));
            }
        }

        if (Minecraft.getInstance().options.keyJump.isDown() || Minecraft.getInstance().options.keyShift.isDown()) {
            if (ItemUtil.isWearingIronManSuit(player) && SuitSettings.from(player).getFlightMode() == SuitSettings.FlightMode.HOVER) {
                updateAnimations = true;
                grounded = false;
                Network.CHANNEL.sendToServer(new MessageAlterHover(Minecraft.getInstance().options.keyJump.isDown()));
            }
        }

        while (KeyBindings.ABILITY1.consumeClick()) {
            if (ItemUtil.canInteractWithIronManSuit(player)) {
                if (settings == null) settings = SuitSettings.from(player);
                settings.cycleMode();
                player.displayClientMessage(Component.literal("Flight: " + settings.getFlightMode() + " CONFIRM: " + KeyBindings.key(KeyBindings.ABILITIES_MENU)).withStyle(ChatFormatting.GOLD), true);
            }
        }

        while (KeyBindings.ABILITY2.consumeClick()) {
            if (ItemUtil.canInteractWithIronManSuit(player)) {
                Network.CHANNEL.sendToServer(new MessageUpdateSuitSettings(SuitSettings.from(player).flipHelmet()));
                player.displayClientMessage(Component.literal("Helmet " + (SuitSettings.from(player).isLifted() ? "Lifted" : "Lowered")).withStyle(ChatFormatting.GOLD), true);
            }
        }

        held = KeyBindings.ABILITY3.isDown();

        while (KeyBindings.ABILITIES_MENU.consumeClick()) {
            if (ItemUtil.canInteractWithIronManSuit(player)) {
                if (settings != null) {
                    Network.CHANNEL.sendToServer(new MessageUpdateSuitSettings(settings));
                    updateAnimations = true;
                    settings = null;
                }
            }
        }
    }
}
