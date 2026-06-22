package mod.syconn.hero.mixin;

import mod.syconn.hero.feature.heros.HeroManager;
import mod.syconn.hero.feature.heros.interfaces.IHeroHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity implements IHeroHolder {

    @Unique
    private final HeroManager hero$manager = new HeroManager();

    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public HeroManager hero$getManager() {
        return this.hero$manager;
    }
}
