package mod.syconn.hero.utils.generic;

import com.mojang.blaze3d.platform.NativeImage;
import dev.architectury.utils.GameInstance;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ResourceUtil {

    private static final Map<String, ResourceLocation> DYNAMIC_TEXTURES = new HashMap<>();

    public static Optional<NativeImage> loadResource(ResourceLocation location) {
        try {
            var inputStream = GameInstance.getClient().getResourceManager().open(location);
            var nativeImage = NativeImage.read(inputStream);
            inputStream.close();
            return Optional.of(nativeImage);
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    public static ResourceLocation registerOrGet(String id, DynamicTexture texture) {
        if (DYNAMIC_TEXTURES.containsKey(id.toLowerCase())) return updateTexture(DYNAMIC_TEXTURES.get(id.toLowerCase()), texture);
        var resourceLocation = GameInstance.getClient().getTextureManager().register(id.toLowerCase(), texture);
        DYNAMIC_TEXTURES.put(id.toLowerCase(), resourceLocation);
        return resourceLocation;
    }

    private static ResourceLocation updateTexture(ResourceLocation loaded, DynamicTexture target) {
        var resource = GameInstance.getClient().getTextureManager().getTexture(loaded);
        if (resource instanceof DynamicTexture texture && target.getPixels() != null) {
            for (int x = 0; x < texture.getPixels().getWidth(); x++) {
                for (int y = 0; y < texture.getPixels().getHeight(); y++) {
                    texture.getPixels().setPixelRGBA(x, y, target.getPixels().getPixelRGBA(x, y));
                }
            }

            texture.upload();
        }
        return loaded;
    }
}