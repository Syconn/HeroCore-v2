package mod.syconn.hero.server;

import dev.architectury.event.events.common.TickEvent;
import mod.syconn.hero.feature.heros.interfaces.IHeroHolder;
import net.minecraft.world.entity.player.Player;

public class HeroServer {

    public static void init() {
        TickEvent.PLAYER_PRE.register(HeroServer::playerTick);
    }

    public static void playerTick(Player player) {
        System.out.println(player + " " + (player instanceof IHeroHolder));

        if (player instanceof IHeroHolder holder) holder.hero$getManager().serverTick(player);
    }
}
