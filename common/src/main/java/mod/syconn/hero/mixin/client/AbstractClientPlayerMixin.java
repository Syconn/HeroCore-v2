package mod.syconn.hero.mixin.client;

import com.mojang.authlib.GameProfile;
import mod.syconn.hero.feature.heros.HeroManager;
import mod.syconn.hero.feature.heros.interfaces.IHeroHolder;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(AbstractClientPlayer.class)
public abstract class AbstractClientPlayerMixin extends Player implements IHeroHolder {

    @Unique
    private final HeroManager hero$manager = new HeroManager(IHeroHolder.CLASS_MAP);

    public AbstractClientPlayerMixin(Level level, BlockPos pos, float yRot, GameProfile gameProfile) {
        super(level, pos, yRot, gameProfile);
    }

    @Override
    public HeroManager hero$getManager() {
        return this.hero$manager;
    }
}
