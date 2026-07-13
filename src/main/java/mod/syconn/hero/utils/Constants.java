package mod.syconn.hero.utils;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;

public class Constants {

    public static final String MOD = "hero";
    public static final RandomSource RANDOM = RandomSource.createNewThreadLocalInstance();
    public static final UpdateTracker TRACKER = new UpdateTracker();

    public static ResourceLocation withDefault(String id) {
        //? if 1.20.1 {
        return new ResourceLocation(id);
         //? } else
        //return ResourceLocation.withDefaultNamespace(id);
    }

    public static ResourceLocation withId(String id) {
        //? if 1.20.1 {
        return new ResourceLocation(MOD, id);
         //? } else
        //return ResourceLocation.fromNamespaceAndPath(MOD, id);
    }

    public static ModelResourceLocation withId(String loc, String path) {
        //? if 1.20.1 {
        return new ModelResourceLocation(MOD, loc, path);
        //? } else
        //return new ModelResourceLocation(Constants.withId(loc), path);
    }

    //? if >=1.21.1 {
    /*public static final ResourceLocation ANIM_LAYER = withId("animations");
    *///? }
}
