package mod.syconn.hero.utils.client;

import mod.syconn.hero.utils.generic.ColorUtil;
import net.minecraft.resources.ResourceLocation;

public class TintedResourceLocation extends ResourceLocation {

    private final int tint;
    private final ColorUtil.TintMode tintMode;

    public TintedResourceLocation(String namespace, String path, int tint) {
        this(namespace, path, tint, ColorUtil.TintMode.Multiply);
    }

    public TintedResourceLocation(String namespace, String path, int tint, ColorUtil.TintMode mode) {
        super(namespace, path);
        this.tint = tint;
        this.tintMode = mode;
    }

    public TintedResourceLocation(ResourceLocation other, int tint, ColorUtil.TintMode mode) {
        super(other.getNamespace(), other.getPath());
        this.tint = tint;
        this.tintMode = mode;
    }

    public int getTint()
    {
        return tint;
    }

    public ColorUtil.TintMode getTintMode() {
        return tintMode;
    }
}
