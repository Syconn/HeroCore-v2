package mod.syconn.hero.mixin;

import com.mojang.authlib.GameProfile;
import mod.syconn.hero.feature.heros.HeroManager;
import mod.syconn.hero.feature.heros.interfaces.IHeroHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerMixin {

//    @Unique
//    private final HeroManager hero$manager = new HeroManager(IHeroHolder.CLASS_MAP);
//
//    static {
//        System.out.println("ServerPlayerMixin CLASS LOADED");
//    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void postInit(Level level, BlockPos pos, float yRot, GameProfile gameProfile, CallbackInfo ci) {
        System.out.println("PlayerMixin loaded");
    }

//    @Override
//    public HeroManager hero$getManager() {
//        return this.hero$manager;
//    }
}
