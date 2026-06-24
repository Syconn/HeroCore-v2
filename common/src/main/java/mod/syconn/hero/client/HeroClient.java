package mod.syconn.hero.client;

import dev.architectury.event.events.client.ClientGuiEvent;
import dev.architectury.event.events.client.ClientLifecycleEvent;
import dev.architectury.event.events.client.ClientPlayerEvent;
import dev.architectury.registry.client.keymappings.KeyMappingRegistry;
import dev.architectury.registry.client.level.entity.EntityModelLayerRegistry;
import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import dev.architectury.registry.client.rendering.ColorHandlerRegistry;
import dev.architectury.registry.client.rendering.RenderTypeRegistry;
import me.fzzyhmstrs.fzzy_config.api.ConfigApi;
import mod.syconn.hero.blockentities.SuitDisplayBlockEntity;
import mod.syconn.hero.client.model.DisplayDoorModel;
import mod.syconn.hero.client.render.block.SuitDisplayRenderer;
import mod.syconn.hero.client.screen.overlays.IronmanOverlay;
import mod.syconn.hero.core.ModBlockEntities;
import mod.syconn.hero.core.ModBlocks;
import mod.syconn.hero.core.ModKeys;
import mod.syconn.hero.feature.heros.interfaces.IHeroHolder;
import mod.syconn.hero.feature.ironman.client.renderers.IronmanArmorRenderer;
import mod.syconn.hero.item.IronmanArmorItem;
import mod.syconn.hero.utils.Constants;
import mod.syconn.hero.utils.interfaces.IModifiedItemRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.DyeColor;

@Environment(EnvType.CLIENT)
public class HeroClient {

    public static void init() {
        ModKeys.KEYS.forEach(KeyMappingRegistry::register);

        IModifiedItemRenderer.register(IronmanArmorItem.class, new IronmanArmorRenderer());
        ColorHandlerRegistry.registerItemColors((s, layer) -> layer == 0 ? DyeColor.GRAY.getFireworkColor() : -1, ModBlocks.SUIT_DISPLAY.get());
        ColorHandlerRegistry.registerBlockColors((state, level, pos, layer) -> layer == 0 ? SuitDisplayBlockEntity.getColor(level, state, pos) : -1, ModBlocks.SUIT_DISPLAY.get());
        RenderTypeRegistry.register(RenderType.cutout(), ModBlocks.SUIT_DISPLAY.get());
        EntityModelLayerRegistry.register(DisplayDoorModel.LAYER_LOCATION, DisplayDoorModel::createBodyLayer);
        BlockEntityRendererRegistry.register(ModBlockEntities.SUIT_DISPLAY.get(), SuitDisplayRenderer::new);

        ClientLifecycleEvent.CLIENT_SETUP.register(HeroClient::setupEvent);
        ClientPlayerEvent.CLIENT_PLAYER_JOIN.register(HeroClient::onClientJoin);
        ClientGuiEvent.RENDER_HUD.register(IronmanOverlay::renderOverlay);
    }

    public static void setupEvent(Minecraft minecraft) {}

    public static void onClientJoin(LocalPlayer player) {
        Constants.TRACKER.clientPlayerJoined(player);
    }

    public static void onClientTick(LocalPlayer player) {
        if (player instanceof IHeroHolder holder && (!Minecraft.getInstance().isPaused() || !Minecraft.getInstance().isSingleplayer())) holder.hero$getManager().clientTick(player);
        if (ModKeys.EDIT_SETTINGS.consumeClick()) ConfigApi.INSTANCE.openScreen("hero");
    }

    public static float getTickDelta() {
        return Minecraft.getInstance().getDeltaFrameTime();
    }
}
