package mod.syconn.hero.network;

import dev.architectury.networking.NetworkChannel;
import mod.syconn.hero.Constants;
import mod.syconn.hero.network.messages.MessageLaunch;

public class Network {

    public static final NetworkChannel CHANNEL = NetworkChannel.create(Constants.withId("network"));

    public static void init() {
        CHANNEL.register(MessageLaunch.class, MessageLaunch::encode, MessageLaunch::new, MessageLaunch::apply);
    }
}
