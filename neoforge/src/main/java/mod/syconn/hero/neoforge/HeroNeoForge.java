package mod.syconn.hero.neoforge;

import mod.syconn.hero.Constants;
import mod.syconn.hero.HeroCore;
import net.neoforged.fml.common.Mod;

@Mod(Constants.MOD_ID)
public final class HeroNeoForge {
    public HeroNeoForge() {
        HeroCore.init();
    }
}
