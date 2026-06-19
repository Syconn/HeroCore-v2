package mod.syconn.hero.mixin;

import mod.syconn.hero.feature.heros.HeroManager;
import mod.syconn.hero.feature.heros.interfaces.IHeroHolder;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Player.class)
public abstract class PlayerMixin implements IHeroHolder {

    @Unique
    private final HeroManager hero$manager = new HeroManager(IHeroHolder.CLASS_MAP);

    @Override
    public HeroManager hero$getManager() {
        return this.hero$manager;
    }
}
