package mod.syconn.hero.utils;

import kotlin.jvm.functions.Function0;
import me.fzzyhmstrs.fzzy_config.api.ConfigApi;
import me.fzzyhmstrs.fzzy_config.api.RegisterType;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;

public class Constants {

    public static final String MOD = "hero";
    public static final UpdateTracker TRACKER = new UpdateTracker();
    public static final HeroConfig CONFIG = ConfigApi.registerAndLoadConfig((Function0<? extends HeroConfig>) HeroConfig::new, RegisterType.BOTH);
    
    public static ResourceLocation withId(String id) {
        return new ResourceLocation(MOD, id);
    }

    public static ModelResourceLocation withId(String loc, String path) {
        return new ModelResourceLocation(MOD, loc, path);
    }
}
