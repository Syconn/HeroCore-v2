package mod.syconn.hero.network;

import dev.architectury.networking.NetworkManager;
import mod.syconn.hero.network.messages.MessageAlterHover;
import mod.syconn.hero.network.messages.MessageFlightMode;
import mod.syconn.hero.network.messages.MessageSuitPropel;

public class Network {

    public static void clientReceivers() {}

    public static void serverReceivers() {
        NetworkManager.registerReceiver(NetworkManager.Side.C2S, MessageAlterHover.TYPE, MessageAlterHover.STREAM_CODEC, MessageAlterHover::handle);
        NetworkManager.registerReceiver(NetworkManager.Side.C2S, MessageSuitPropel.TYPE, MessageSuitPropel.STREAM_CODEC, MessageSuitPropel::handle);
        NetworkManager.registerReceiver(NetworkManager.Side.C2S, MessageFlightMode.TYPE, MessageFlightMode.STREAM_CODEC, MessageFlightMode::handle);
    }
}
