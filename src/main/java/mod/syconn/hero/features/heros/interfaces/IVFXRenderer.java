package mod.syconn.hero.features.heros.interfaces;

import mod.syconn.hero.network.Network;
import mod.syconn.hero.network.messages.clientside.PlayParticleEffect;
import mod.syconn.hero.utils.client.ParticleEvent;
import net.minecraft.server.level.ServerPlayer;

public interface IVFXRenderer {

    default void addParticleEvent(ServerPlayer player, ParticleEvent event) {
        for (var viewer : player.serverLevel().players()) Network.CHANNEL.sendToPlayer(viewer, new PlayParticleEffect(event));
    }
}
