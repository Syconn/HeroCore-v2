package mod.syconn.hero.utils.interfaces;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public interface ISpecialRenderer {

    List<ModelResourceLocation> SPECIAL_RENDERS = new ArrayList<>();
    List<String> SPECIAL_RENDER_FOLDER = new ArrayList<>();

    static void registerPath(String path) {
        SPECIAL_RENDER_FOLDER.add(path);
    }

    static void registerSpecial(ModelResourceLocation modelResourceLocation) {
        SPECIAL_RENDERS.add(modelResourceLocation);
    }

    static void registerSpecial(ResourceLocation resourceLocation) {
        SPECIAL_RENDERS.add(new ModelResourceLocation(resourceLocation.getNamespace(), resourceLocation.getPath(), "inventory"));
    }

    static ModelResourceLocation itemModelPath(ResourceLocation location) {
        return new ModelResourceLocation(location.getNamespace(), trimSuffix(trimPrefix(location.getPath())), "inventory");
    }

    private static String trimPrefix(String str) {
        if (str.startsWith("models/item/")) return str.substring("models/item/".length());
        return str;
    }

    private static String trimSuffix(String str) {
        if (str.endsWith(".json")) return str.substring(0, str.length() - ".json".length());
        return str;
    }
}
