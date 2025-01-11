package mod.syconn.hero.extra.data.menu;

import mod.syconn.hero.extra.core.IMenuData;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public record PositionMenuData(BlockPos pos) implements IMenuData<PositionMenuData> {

    public static final StreamCodec<RegistryFriendlyByteBuf, PositionMenuData> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, PositionMenuData::pos, PositionMenuData::new
    );

    public StreamCodec<RegistryFriendlyByteBuf, PositionMenuData> codec() {
        return STREAM_CODEC;
    }
}
