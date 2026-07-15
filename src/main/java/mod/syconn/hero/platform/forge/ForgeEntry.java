//? if forge {
package mod.syconn.hero.platform.forge;

import dev.architectury.platform.forge.EventBuses;
import mod.syconn.hero.HeroCore;
import mod.syconn.hero.utils.Constants;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Constants.MOD)
public class ForgeEntry {

    public ForgeEntry(FMLJavaModLoadingContext context) {
        EventBuses.registerModEventBus(Constants.MOD, context.getModEventBus());
        HeroCore.init();
    }
}
//?}