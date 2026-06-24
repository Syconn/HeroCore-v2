package mod.syconn.hero.block;

import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import org.jetbrains.annotations.NotNull;

public abstract class VerticalRotatableBlock extends HorizontalDirectionalBlock {

    public static final DirectionProperty VERTICAL = BlockStateProperties.VERTICAL_DIRECTION;

    protected VerticalRotatableBlock(BlockBehaviour.Properties properties) {
        super(properties);

    }

    @Override
    public @NotNull BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(VERTICAL, rotation.rotate(state.getValue(VERTICAL)));
    }

    @Override
    public @NotNull BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(VERTICAL)));
    }
}
