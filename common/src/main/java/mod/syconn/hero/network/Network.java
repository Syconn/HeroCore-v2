package mod.syconn.hero.network;

import dev.architectury.networking.NetworkChannel;
import mod.syconn.hero.network.messages.PlayAnimationPacket;
import mod.syconn.hero.network.messages.clientside.PlayParticleEffect;
import mod.syconn.hero.network.messages.clientside.SyncClientPacket;
import mod.syconn.hero.network.messages.clientside.SyncResourceDataPacket;
import mod.syconn.hero.network.messages.serverside.FlightTravelPacket;
import mod.syconn.hero.network.messages.serverside.HoverPacket;
import mod.syconn.hero.network.messages.serverside.PlaySoundPacket;
import mod.syconn.hero.network.messages.serverside.SaveAbilityDataPacket;
import mod.syconn.hero.utils.Constants;

public class Network {

    public static final NetworkChannel CHANNEL = NetworkChannel.create(Constants.withId("network"));

    public static void init() {
        CHANNEL.register(SaveAbilityDataPacket.class, SaveAbilityDataPacket::encode, SaveAbilityDataPacket::new, SaveAbilityDataPacket::apply);
        CHANNEL.register(FlightTravelPacket.class, FlightTravelPacket::encode, FlightTravelPacket::new, FlightTravelPacket::apply);
        CHANNEL.register(HoverPacket.class, HoverPacket::encode, HoverPacket::new, HoverPacket::apply);
        CHANNEL.register(PlayAnimationPacket.class, PlayAnimationPacket::encode, PlayAnimationPacket::new, PlayAnimationPacket::apply);
        CHANNEL.register(SyncClientPacket.class, SyncClientPacket::encode, SyncClientPacket::new, SyncClientPacket::apply);
        CHANNEL.register(PlaySoundPacket.class, PlaySoundPacket::encode, PlaySoundPacket::new, PlaySoundPacket::apply);
        CHANNEL.register(SyncResourceDataPacket.class, SyncResourceDataPacket::encode, SyncResourceDataPacket::new, SyncResourceDataPacket::apply);
        CHANNEL.register(PlayParticleEffect.class, PlayParticleEffect::encode, PlayParticleEffect::new, PlayParticleEffect::apply);
    }
}
