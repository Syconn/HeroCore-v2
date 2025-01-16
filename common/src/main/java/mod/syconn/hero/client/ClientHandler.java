package mod.syconn.hero.client;

import dev.architectury.networking.NetworkManager;
import mod.syconn.hero.common.components.SuitComponent;
import mod.syconn.hero.network.messages.MessageAlterHover;
import mod.syconn.hero.network.messages.MessageFlightMode;
import mod.syconn.hero.network.messages.MessageSuitPropel;
import mod.syconn.hero.util.AnimationUtil;
import mod.syconn.hero.util.ItemUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;

public class ClientHandler {

    private static SuitComponent settings = null;
    private static boolean updateAnimations = true;
    private static boolean grounded = true;
    private static int ticksToClear;

    public static void onClientPlayerTick(Minecraft minecraft) {
        LocalPlayer player = minecraft.player;
        
        if (player != null) {
            handleMappings(player);

            if (updateAnimations) {
                SuitComponent settings = SuitComponent.from(player);
                if (!player.level().getBlockState(player.getOnPos()).isAir()) {
                    AnimationUtil.stop(player);
                } else if (settings.flightMode() == SuitComponent.FlightMode.HOVER && !AnimationUtil.isAnimated(player)) {
                    AnimationUtil.play(player, "start_hover");
                    AnimationUtil.play(player, "hover", 20);
                } else if (settings.flightMode() == SuitComponent.FlightMode.FLY && !AnimationUtil.isAnimated(player)) {
                    AnimationUtil.play(player, "flight");
                } else if (settings.flightMode() == SuitComponent.FlightMode.WALK) AnimationUtil.stop(player);
                updateAnimations = false;
            }

            if (!grounded && !player.level().getBlockState(player.getOnPos()).isAir()) {
                grounded = true;
                updateAnimations = true;
            }
        }
    }

    private static void handleMappings(LocalPlayer player) {
        if (settings != null) ticksToClear++;
        if (ticksToClear > 100) {
            ticksToClear = 0;
            settings = null;
        }

        if (Minecraft.getInstance().options.keyJump.isDown()) {
            if (ItemUtil.isWearingIronManSuit(player) && SuitComponent.from(player).flightMode() == SuitComponent.FlightMode.FLY) {
                updateAnimations = true;
                grounded = false;
                NetworkManager.sendToServer(new MessageSuitPropel(Minecraft.getInstance().options.keySprint.isDown()));
            }
        }

        if (Minecraft.getInstance().options.keyJump.isDown() || Minecraft.getInstance().options.keyShift.isDown()) {
            if (ItemUtil.isWearingIronManSuit(player) && SuitComponent.from(player).flightMode() == SuitComponent.FlightMode.HOVER) {
                updateAnimations = true;
                grounded = false;
                NetworkManager.sendToServer(new MessageAlterHover(Minecraft.getInstance().options.keyJump.isDown()));
            }
        }

        while (KeyBindings.ABILITY1.consumeClick()) {
            if (settings == null) settings = SuitComponent.from(player);
            settings = settings.cycleMode();
            player.displayClientMessage(Component.literal("Flight: " + settings.flightMode() + " CONFIRM: " + KeyBindings.key(KeyBindings.ABILITIES_MENU)).withStyle(ChatFormatting.GOLD), true);
        }

        while (KeyBindings.ABILITIES_MENU.consumeClick()) {
            if (settings != null) {
                NetworkManager.sendToServer(new MessageFlightMode(settings.flightMode()));
                updateAnimations = true;
                settings = null;
            }
        }
    }
}
