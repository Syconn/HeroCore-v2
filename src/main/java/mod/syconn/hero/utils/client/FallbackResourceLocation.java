package mod.syconn.hero.utils.client;

import net.minecraft.resources.ResourceLocation;

public class FallbackResourceLocation extends ResourceLocation {

    private final ResourceLocation source;

    public FallbackResourceLocation(String namespace, String path, ResourceLocation source) {
        super(namespace, path);
        this.source = source;
    }

    public ResourceLocation getSource()
    {
        return source;
    }
}
