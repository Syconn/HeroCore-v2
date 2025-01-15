package mod.syconn.hero;

import net.minecraft.resources.ResourceLocation;

public class Constants {

    public static final String MOD_ID = "hero";
    
    public static ResourceLocation withId(String id) {
        return new ResourceLocation(MOD_ID, id);
    }
}
