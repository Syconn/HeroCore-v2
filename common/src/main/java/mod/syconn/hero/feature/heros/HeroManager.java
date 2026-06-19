package mod.syconn.hero.feature.heros;

import mod.syconn.hero.feature.heros.interfaces.IHeroType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.Map;

@Environment(EnvType.CLIENT)
public record HeroManager(Map<Class<? extends IHeroType>, IHeroType> types) {

    public void tick(Player player) {
        if (player instanceof AbstractClientPlayer) clientTick(player);
        else serverTick(player);
    }

    private void clientTick(Player player) {
        for (var type : types.values()) type.clientTick(player);
    }

    private void serverTick(Player player) {
        for (var type : types.values()) type.serverTick(player);
    }

    @SuppressWarnings("unchecked")
    public <T extends IHeroType> T getType(Class<T> clazz) {
        return (T) types.get(clazz);
    }
}
