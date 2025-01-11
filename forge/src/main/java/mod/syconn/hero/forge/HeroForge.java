package mod.syconn.hero.forge;

import dev.architectury.platform.forge.EventBuses;
import mod.syconn.hero.Constants;
import mod.syconn.hero.HeroCore;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Constants.MOD_ID)
public final class HeroForge {
    
    public HeroForge(FMLJavaModLoadingContext context) {
        EventBuses.registerModEventBus(Constants.MOD_ID, context.getModEventBus());
        HeroCore.init();
    }
}
