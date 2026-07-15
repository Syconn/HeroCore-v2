package mod.syconn.hero.utils.client;

import com.mojang.blaze3d.platform.NativeImage;
import mod.syconn.hero.utils.generic.ColorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;

import java.io.IOException;
import java.util.function.Consumer;

public class TintedTexture extends CallbackTexture {

    private final TintedResourceLocation texture;

    public TintedTexture(TintedResourceLocation texture, ResourceLocation fallbackSkin, Consumer<Boolean> completionCallback) {
        super(fallbackSkin, completionCallback);
        this.texture = texture;
    }

    @Override
    protected NativeImage generateImage(ResourceManager manager) throws IOException {
        var textureManager = Minecraft.getInstance().getTextureManager();
        var tex = textureManager.getTexture(texture);

        NativeImage nativeImage;
        if (tex instanceof DynamicTexture dynamicTexture) nativeImage = dynamicTexture.getPixels();
        else if (tex instanceof CallbackTexture t) nativeImage = t.getImage();
        else {
            var texData = TextureImage.load(manager, texture);
            nativeImage = texData.getImage();
        }

        if (nativeImage == null) throw new IOException("NativeImage was null");

        var width = nativeImage.getWidth();
        var height = nativeImage.getHeight();
        for (var x = 0; x < width; x++) for (var y = 0; y < height; y++) nativeImage.setPixelRGBA(x, y, ColorUtil.tint(nativeImage.getPixelRGBA(x, y), texture.getTint(), texture.getTintMode()));

        return nativeImage;
    }
}

