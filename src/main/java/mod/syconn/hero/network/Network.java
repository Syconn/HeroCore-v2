package mod.syconn.hero.network;

//? if 1.20.1 {
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
//? } else {

/*import dev.architectury.networking.NetworkManager;
import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import mod.syconn.hero.network.messages.clientside.PlayParticleEffect;
import mod.syconn.hero.network.messages.clientside.SyncClientPacket;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
*///? }

public class Network {

    //? if 1.20.1 {
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
    //? } else {
    /*public static void init() {
        // S2C Packets
        registerS2C(PlayParticleEffect.TYPE, PlayParticleEffect.STREAM_CODEC, PlayParticleEffect::handle);
        registerS2C(SyncClientPacket.TYPE, SyncClientPacket.STREAM_CODEC, SyncClientPacket::handle);

        // C2S Packets
    }

    private static <T extends CustomPacketPayload> void registerC2S(CustomPacketPayload.Type<T> id, StreamCodec<? super RegistryFriendlyByteBuf, T> codec, NetworkManager.NetworkReceiver<T> receiver) {
        NetworkManager.registerReceiver(NetworkManager.c2s(), id, codec, receiver);
    }

    private static <T extends CustomPacketPayload> void registerS2C(CustomPacketPayload.Type<T> id, StreamCodec<? super RegistryFriendlyByteBuf, T> codec, NetworkManager.NetworkReceiver<T> receiver) {
        if (Platform.getEnvironment() == Env.CLIENT) NetworkManager.registerReceiver(NetworkManager.s2c(), id, codec, receiver);
        else NetworkManager.registerS2CPayloadType(id, codec);
    }
    *///? }
}
