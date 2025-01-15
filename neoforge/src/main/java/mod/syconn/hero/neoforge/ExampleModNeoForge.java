package mod.syconn.hero.neoforge;

import net.neoforged.fml.common.Mod;

import mod.syconn.hero.ExampleMod;

@Mod(ExampleMod.MOD_ID)
public final class ExampleModNeoForge {
    public ExampleModNeoForge() {
        // Run our common setup.
        ExampleMod.init();
    }
}
