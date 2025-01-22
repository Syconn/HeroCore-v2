package mod.syconn.hero.network;

import dev.architectury.networking.NetworkChannel;
import mod.syconn.hero.Constants;
import mod.syconn.hero.network.messages.MessageAlterHover;
import mod.syconn.hero.network.messages.MessageSuitPropel;
import mod.syconn.hero.network.messages.MessageUpdateSuitSettings;

public class Network {

    public static final NetworkChannel CHANNEL = NetworkChannel.create(Constants.withId("network"));

    public static void init() {
        CHANNEL.register(MessageSuitPropel.class, MessageSuitPropel::encode, MessageSuitPropel::new, MessageSuitPropel::apply);
        CHANNEL.register(MessageUpdateSuitSettings.class, MessageUpdateSuitSettings::encode, MessageUpdateSuitSettings::new, MessageUpdateSuitSettings::apply);
        CHANNEL.register(MessageAlterHover.class, MessageAlterHover::encode, MessageAlterHover::new, MessageAlterHover::apply);
    }
}
