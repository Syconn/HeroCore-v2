package mod.syconn.hero.network;

import dev.architectury.networking.NetworkChannel;
import mod.syconn.hero.network.messages.serverside.FlightTravelPacket;
import mod.syconn.hero.network.messages.serverside.HoverPacket;
import mod.syconn.hero.network.messages.serverside.SaveAbilityDataPacket;
import mod.syconn.hero.utils.Constants;

public class Network {

    public static final NetworkChannel CHANNEL = NetworkChannel.create(Constants.withId("network"));

    public static void init() {
        CHANNEL.register(SaveAbilityDataPacket.class, SaveAbilityDataPacket::encode, SaveAbilityDataPacket::new, SaveAbilityDataPacket::apply);
        CHANNEL.register(FlightTravelPacket.class, FlightTravelPacket::encode, FlightTravelPacket::new, FlightTravelPacket::apply);
        CHANNEL.register(HoverPacket.class, HoverPacket::encode, HoverPacket::new, HoverPacket::apply);
    }
}
