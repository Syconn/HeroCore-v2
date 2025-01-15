package mod.syconn.hero.network;

import dev.architectury.networking.NetworkChannel;
import mod.syconn.hero.Constants;
import mod.syconn.hero.network.messages.MessageAlterHover;
import mod.syconn.hero.network.messages.MessageFlightMode;
import mod.syconn.hero.network.messages.MessageSuitPropel;

public class Network {

    public static final NetworkChannel CHANNEL = NetworkChannel.create(Constants.withId("network"));

    public static void init() {
        CHANNEL.register(MessageSuitPropel.class, MessageSuitPropel::encode, MessageSuitPropel::new, MessageSuitPropel::apply);
        CHANNEL.register(MessageFlightMode.class, MessageFlightMode::encode, MessageFlightMode::new, MessageFlightMode::apply);
        CHANNEL.register(MessageAlterHover.class, MessageAlterHover::encode, MessageAlterHover::new, MessageAlterHover::apply);
    }
}
