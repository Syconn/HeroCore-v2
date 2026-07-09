//? if forge {
package mod.syconn.hero.platform.forge.client;

import dev.architectury.utils.GameInstance;
import mod.syconn.hero.utils.Constants;
import mod.syconn.hero.utils.interfaces.ISpecialRenderer;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Mod.EventBusSubscriber(modid = Constants.MOD, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ForgeClientEntry {

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
        public static void clientTickEvent(TickEvent.ClientTickEvent event) {
//            if (event.side.isClient()) StarWarsClient.onClientTick(Minecraft.getInstance().player);
        }
    }
}
//? }