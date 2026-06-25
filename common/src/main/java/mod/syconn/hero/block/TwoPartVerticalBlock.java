package mod.syconn.hero.block;

import mod.syconn.hero.utils.block.ModBlockStateProperties;
import mod.syconn.hero.utils.block.TwoPart;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class TwoPartVerticalBlock extends VerticalRotatableBlock {

    public static final EnumProperty<TwoPart> PART = ModBlockStateProperties.TWO_PART;

    public TwoPartVerticalBlock(Properties properties) {
        super(properties);
    }

    protected static Direction getOtherPart(BlockState state) {
        return state.getValue(PART) == TwoPart.DOWN ? state.getValue(VERTICAL) : state.getValue(VERTICAL).getOpposite();
    }

    @Override @SuppressWarnings("deprecation")
    public @NotNull BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (direction == getOtherPart(state)) return neighborState.is(this) && neighborState.getValue(PART) != state.getValue(PART) ? super.updateShape(state, direction, neighborState, level, pos, neighborPos) : Blocks.AIR.defaultBlockState();
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        var pose = context.getClickedPos().relative(Direction.UP);
        var level = context.getLevel();
        return level.getBlockState(pose).canBeReplaced(context) && level.getWorldBorder().isWithinBounds(pose) ? this.defaultBlockState().setValue(VERTICAL, Direction.UP).setValue(FACING, context.getHorizontalDirection()) : null;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(VERTICAL, PART, FACING);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        if (!level.isClientSide) {
            BlockPos blockPos = pos.relative(state.getValue(VERTICAL));
            level.setBlock(blockPos, state.setValue(PART, TwoPart.UP), 3);
            level.blockUpdated(pos, Blocks.AIR);
            state.updateNeighbourShapes(level, pos, 3);
        }
    }
}
