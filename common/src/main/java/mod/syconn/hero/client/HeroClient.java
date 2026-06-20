package mod.syconn.hero.client;

import dev.architectury.event.events.client.ClientGuiEvent;
import dev.architectury.event.events.client.ClientLifecycleEvent;
import dev.architectury.event.events.client.ClientPlayerEvent;
import dev.architectury.event.events.common.TickEvent;
import dev.architectury.registry.client.keymappings.KeyMappingRegistry;
import me.fzzyhmstrs.fzzy_config.api.ConfigApi;
import mod.syconn.hero.client.screen.overlays.IronmanOverlay;
import mod.syconn.hero.core.ModKeys;
import mod.syconn.hero.feature.heros.interfaces.IHeroHolder;
import mod.syconn.hero.utils.Constants;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;

@Environment(EnvType.CLIENT)
public class HeroClient {

    public static void init() {
        ModKeys.KEYS.forEach(KeyMappingRegistry::register);

        ClientLifecycleEvent.CLIENT_SETUP.register(HeroClient::setupEvent);
        ClientPlayerEvent.CLIENT_PLAYER_JOIN.register(HeroClient::onClientJoin);
        ClientGuiEvent.RENDER_HUD.register(IronmanOverlay::renderOverlay);
    }

    public static void setupEvent(Minecraft minecraft) {}

    public static void onClientJoin(LocalPlayer player) {
        Constants.TRACKER.clientPlayerJoined(player);
    }

    public static void onClientTick(LocalPlayer player) {
        if (player instanceof IHeroHolder holder) holder.hero$getManager().clientTick(player);
        if (ModKeys.EDIT_SETTINGS.consumeClick()) ConfigApi.INSTANCE.openScreen("hero");
    }

    public static float getTickDelta() {
        return Minecraft.getInstance().getDeltaFrameTime();
    }
}
