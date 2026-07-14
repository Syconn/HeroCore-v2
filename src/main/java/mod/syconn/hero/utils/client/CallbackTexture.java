package mod.syconn.hero.utils.client;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.platform.TextureUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import mod.syconn.hero.utils.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;

import java.io.IOException;
import java.util.function.Consumer;

public abstract class CallbackTexture extends SimpleTexture {

    protected final Consumer<Boolean> completionCallback;
    protected boolean isLoaded;
    protected NativeImage image;

    public CallbackTexture(ResourceLocation location, Consumer<Boolean> completionCallback) {
        super(location);
        this.completionCallback = completionCallback;
    }

    protected void complete(NativeImage image) {
        var minecraft = Minecraft.getInstance();
        minecraft.execute(() -> {
            this.isLoaded = true;
            if (!RenderSystem.isOnRenderThread()) RenderSystem.recordRenderCall(() -> this.complete(image));
            else {
                if (image != null) {
                    this.upload(image);
                    completionCallback.accept(true);
                } else completionCallback.accept(false);

                this.image = image;
            }
        });
    }

    public NativeImage getImage()
    {
        return image;
    }

    private void upload(NativeImage image) {
        TextureUtil.prepareImage(this.getId(), 0, image.getWidth(), image.getHeight());
        image.upload(0, 0, 0, 0, 0, image.getWidth(), image.getHeight(), false, false, false, false);
    }

    @Override
    public void load(ResourceManager manager) throws IOException {
        Minecraft.getInstance().execute(() -> {
            if (!this.isLoaded) {
                try {
                    super.load(manager);
                } catch (IOException var3) {
                    Constants.LOG.error("e: ", var3);
                }

                this.isLoaded = true;
            }
        });

        complete(generateImage(manager));
    }

    protected abstract NativeImage generateImage(ResourceManager manager) throws IOException;
}
