//? if forge {
/*package mod.syconn.hero.platform.forge.client;

import dev.architectury.utils.GameInstance;
import mod.syconn.hero.features.ironman.client.screen.overlays.IronmanOverlay;
import mod.syconn.hero.utils.Constants;
import mod.syconn.hero.utils.interfaces.ISpecialRenderer;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Mod.EventBusSubscriber(modid = Constants.MOD, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ForgeClientEntry {

//    @SubscribeEvent
//    public static void addRenderLayers(EntityRenderersEvent.AddLayers event) {
//        for (String skin : event.getSkins()) {
//            var renderer = event.getSkin(skin);
//            if (renderer instanceof PlayerRenderer playerRenderer) playerRenderer.addLayer(new SWGearLayer<>(playerRenderer, event.getContext().getItemRenderer()));
//        }
//    }

    @SubscribeEvent
    public static void registerAdditionalModels(final ModelEvent.RegisterAdditional event) {
        var files = new ArrayList<ResourceLocation>();
        ISpecialRenderer.SPECIAL_RENDER_FOLDER.forEach(path -> scanFilesInDirectory(path, ISpecialRenderer::itemModelPath, files));
        files.addAll(ISpecialRenderer.SPECIAL_RENDERS);
        files.forEach(event::register);
    }

    private static void scanFilesInDirectory(String name, Function<ResourceLocation, ResourceLocation> pathModifier, List<ResourceLocation> output) {
        output.addAll(FileToIdConverter.json(name).listMatchingResources(GameInstance.getClient().getResourceManager()).keySet().stream().map(pathModifier).toList());
    }

    @Mod.EventBusSubscriber(modid = Constants.MOD, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
    static class StarWarsForgeModClient {

        @SubscribeEvent
        public static void renderOverlay(RenderGuiEvent.Pre event) {
            IronmanOverlay.renderOverlay(event.getGuiGraphics(), event.getPartialTick());
        }
    }
}
*///? }