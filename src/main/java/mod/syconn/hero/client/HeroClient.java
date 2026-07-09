package mod.syconn.hero.client;

import dev.architectury.event.events.client.ClientLifecycleEvent;
import dev.architectury.event.events.client.ClientPlayerEvent;
import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.registry.client.keymappings.KeyMappingRegistry;
import dev.architectury.registry.client.level.entity.EntityModelLayerRegistry;
import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import dev.architectury.registry.client.rendering.ColorHandlerRegistry;
import dev.architectury.registry.client.rendering.RenderTypeRegistry;
import mod.syconn.hero.client.model.DisplayDoorModel;
import mod.syconn.hero.core.ModBlockEntities;
import mod.syconn.hero.core.ModBlocks;
import mod.syconn.hero.core.ModKeys;
import mod.syconn.hero.core.ModMenus;
import mod.syconn.hero.features.heros.interfaces.IHeroHolder;
import mod.syconn.hero.features.ironman.blockentity.SuitDisplayBlockEntity;
import mod.syconn.hero.features.ironman.client.renderers.block.SuitDisplayRenderer;
import mod.syconn.hero.features.ironman.client.renderers.item.ModifiedIronmanArmorRenderer;
import mod.syconn.hero.features.ironman.item.IronmanArmorItem;
import mod.syconn.hero.utils.Constants;
import mod.syconn.hero.utils.HeroConfig;
import mod.syconn.hero.utils.interfaces.IModifiedItemRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.DyeColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class HeroClient {

    public static void init() {
        ModKeys.KEYS.forEach(KeyMappingRegistry::register);

        IModifiedItemRenderer.register(IronmanArmorItem.class, new ModifiedIronmanArmorRenderer());

        EntityModelLayerRegistry.register(DisplayDoorModel.LAYER_LOCATION, DisplayDoorModel::createBodyLayer);

        ClientLifecycleEvent.CLIENT_SETUP.register(HeroClient::setupEvent);
        ClientPlayerEvent.CLIENT_PLAYER_JOIN.register(HeroClient::onClientJoin);
        ClientTickEvent.CLIENT_PRE.register(HeroClient::onClientTick);
    }

    public static void setupEvent(Minecraft minecraft) {
        RenderTypeRegistry.register(RenderType.cutout(), ModBlocks.SUIT_DISPLAY.get());

        ColorHandlerRegistry.registerItemColors((s, layer) -> layer == 0 ? DyeColor.GRAY.getFireworkColor() : -1, ModBlocks.SUIT_DISPLAY.get());
        ColorHandlerRegistry.registerBlockColors((state, level, pos, layer) -> layer == 0 ? SuitDisplayBlockEntity.getColor(level, state, pos) : -1, ModBlocks.SUIT_DISPLAY.get());

        BlockEntityRendererRegistry.register(ModBlockEntities.SUIT_DISPLAY.get(), SuitDisplayRenderer::new);

        ModMenus.registerScreens();
    }

    public static void onClientJoin(LocalPlayer player) {
        Constants.TRACKER.clientPlayerJoined(player);
    }

    public static void onClientTick(Minecraft mc) {
        var player = mc.player;
        if (player instanceof IHeroHolder holder && (!mc.isPaused() || !mc.isSingleplayer())) holder.hero$getManager().clientTick(player);
        if (ModKeys.EDIT_SETTINGS.consumeClick()) mc.setScreen(HeroConfig.getScreen(mc.screen, Constants.MOD));
    }

    public static float getTickDelta() {
        return Minecraft.getInstance().getDeltaFrameTime();
    }
}
