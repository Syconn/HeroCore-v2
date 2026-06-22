package mod.syconn.hero.server;

import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.event.events.common.TickEvent;
import mod.syconn.hero.feature.addons.IronmanContent;
import mod.syconn.hero.feature.heros.interfaces.IHeroHolder;
import mod.syconn.hero.utils.server.SyncedResourceManager;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class HeroServer {

    public static void init() {
        PlayerEvent.PLAYER_JOIN.register(HeroServer::playerJoinedServer);
        TickEvent.PLAYER_POST.register(HeroServer::playerTick);
    }

    public static void playerJoinedServer(ServerPlayer player) {
        SyncedResourceManager.handleJoin(player);
    }

    public static void playerTick(Player player) {
        if (player instanceof IHeroHolder holder && !player.level().isClientSide()) holder.hero$getManager().serverTick(player);
    }
}
