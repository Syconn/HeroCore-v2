package mod.syconn.hero.network;

import dev.architectury.networking.NetworkChannel;
import mod.syconn.hero.Constants;
import mod.syconn.hero.network.messages.*;

public class Network {

    public static final NetworkChannel CHANNEL = NetworkChannel.create(Constants.withId("network"));

    public static void initC2S() {
        CHANNEL.register(MessageSuitPropel.class, MessageSuitPropel::encode, MessageSuitPropel::new, MessageSuitPropel::apply);
        CHANNEL.register(MessageUpdateSuitSettings.class, MessageUpdateSuitSettings::encode, MessageUpdateSuitSettings::new, MessageUpdateSuitSettings::apply);
        CHANNEL.register(MessageAlterHover.class, MessageAlterHover::encode, MessageAlterHover::new, MessageAlterHover::apply);
        CHANNEL.register(MessageSyncPersistentData.class, MessageSyncPersistentData::encode, MessageSyncPersistentData::new, MessageSyncPersistentData::apply);
        CHANNEL.register(MessageThrowMjolnir.class, MessageThrowMjolnir::encode, MessageThrowMjolnir::new, MessageThrowMjolnir::apply);
        CHANNEL.register(MessageMjolnirStrikeEnemy.class, MessageMjolnirStrikeEnemy::encode, MessageMjolnirStrikeEnemy::new, MessageMjolnirStrikeEnemy::apply);
    }

    public static void initS2C() {
        CHANNEL.register(MessageSyncPersistentData.class, MessageSyncPersistentData::encode, MessageSyncPersistentData::new, MessageSyncPersistentData::apply);
    }
}
