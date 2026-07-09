package mod.syconn.hero.utils;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;

public class Constants {

    public static final String MOD = "hero";
    public static final RandomSource RANDOM = RandomSource.createNewThreadLocalInstance();
    public static final UpdateTracker TRACKER = new UpdateTracker();
    
    public static ResourceLocation withId(String id) {
        return new ResourceLocation(MOD, id);
    }

    public static ModelResourceLocation withId(String loc, String path) {
        return new ModelResourceLocation(MOD, loc, path);
    }
}
