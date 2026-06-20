package mod.syconn.hero.fabric;

import mod.syconn.hero.HeroCore;
import net.fabricmc.api.ModInitializer;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

public final class HeroFabric implements ModInitializer {
    
    public void onInitialize() {
        HeroCore.init();
    }
}
