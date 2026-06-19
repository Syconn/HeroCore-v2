package mod.syconn.hero.feature.heros.interfaces;

import net.minecraft.world.entity.player.Player;

public interface IServerSynced {

    default void syncPlayer(Player player) {};
    default void serverTick(Player player) {};
}
