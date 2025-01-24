package mod.syconn.hero.client;

import mod.syconn.hero.client.screen.HeroSelectorScreen;
import mod.syconn.hero.common.data.SuitSettings;
import mod.syconn.hero.core.ModKeyBindings;
import mod.syconn.hero.network.Network;
import mod.syconn.hero.network.messages.MessageAlterHover;
import mod.syconn.hero.network.messages.MessageSuitPropel;
import mod.syconn.hero.network.messages.MessageUpdateSuitSettings;
import mod.syconn.hero.util.AbilityUtil;
import mod.syconn.hero.util.AnimationUtil;
import mod.syconn.hero.util.HeroTypes;
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
            handleAnimation(player);
        }
    }

    private static void handleMappings(LocalPlayer player) { // TODO FLIGHT ANIMATION ISSUE BETWEEN HOVER AND FLIGHT WHEN ALREADY HOVERING
        if (settings != null) ticksToClear1++; // TODO NOT SAVING DATA
        if (ticksToClear1 > 100) {
            ticksToClear1 = 0;
            settings = null;
        }

        if (held) ticks++;
        if (ticks > 20) {
            ticks = 0;
            System.out.println("Done"); // TODO IRONMAN COMBAT SYSTEM
        }

        if (Minecraft.getInstance().options.keyJump.isDown()) {
            if (AbilityUtil.useSpecificPower(player, HeroTypes.IRON_MAN) && SuitSettings.from(player).getFlightMode() == SuitSettings.FlightMode.FLY) {
                updateAnimations = true;
                grounded = false;
                Network.CHANNEL.sendToServer(new MessageSuitPropel(Minecraft.getInstance().options.keySprint.isDown()));
            }
        }

        if (Minecraft.getInstance().options.keyJump.isDown() || Minecraft.getInstance().options.keyShift.isDown()) {
            if (AbilityUtil.useSpecificPower(player, HeroTypes.IRON_MAN) && SuitSettings.from(player).getFlightMode() == SuitSettings.FlightMode.HOVER) {
                updateAnimations = true;
                grounded = false;
                Network.CHANNEL.sendToServer(new MessageAlterHover(Minecraft.getInstance().options.keyJump.isDown()));
            }
        }

        while (ModKeyBindings.ABILITY1.consumeClick()) { // TODO GOING TO CONFLICT WITH OTHER POWER IN FUTURE
            if (AbilityUtil.useSpecificPower(player, HeroTypes.IRON_MAN)) {
                if (settings == null) settings = SuitSettings.from(player);
                settings.cycleMode();
                player.displayClientMessage(Component.literal("Flight: " + settings.getFlightMode() + " CONFIRM: " + ModKeyBindings.key(ModKeyBindings.ABILITIES_MENU)).withStyle(ChatFormatting.GOLD), true);
            }
        }

        while (ModKeyBindings.ABILITY2.consumeClick()) {
            if (AbilityUtil.canInteractWithIronManHelmet(player)) {
                SuitSettings.set(player, SuitSettings.from(player).flipHelmet());
                Network.CHANNEL.sendToServer(new MessageUpdateSuitSettings(SuitSettings.from(player)));
                player.displayClientMessage(Component.literal("Helmet " + (SuitSettings.from(player).isLifted() ? "Lowered" : "Lifted")).withStyle(ChatFormatting.GOLD), true);
            }
        }

        held = ModKeyBindings.ABILITY3.isDown();

        while (ModKeyBindings.ABILITIES_MENU.consumeClick()) {
            if (AbilityUtil.useSpecificPower(player, HeroTypes.IRON_MAN) && settings != null) {
                Network.CHANNEL.sendToServer(new MessageUpdateSuitSettings(settings));
                updateAnimations = true;
                settings = null;
            } else {
                updateAnimations = true;
                Minecraft.getInstance().setScreen(new HeroSelectorScreen(player));
            }
        }
    }

    private static void handleAnimation(LocalPlayer player) {
        if (updateAnimations) {
            if (AbilityUtil.useSpecificPower(player, HeroTypes.NONE) || AbilityUtil.useSpecificPower(player, HeroTypes.THOR)) AnimationUtil.stop(player);

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
