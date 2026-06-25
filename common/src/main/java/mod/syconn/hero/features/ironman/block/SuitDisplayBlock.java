package mod.syconn.hero.features.ironman.block;

import mod.syconn.hero.block.TwoPartVerticalBlock;
import mod.syconn.hero.core.ModBlockEntities;
import mod.syconn.hero.features.ironman.blockentity.SuitDisplayBlockEntity;
import mod.syconn.hero.features.ironman.server.menu.SuitDisplayMenu;
import mod.syconn.hero.utils.block.TwoPart;
import mod.syconn.hero.utils.generic.ModelUtil;
import mod.syconn.hero.utils.interfaces.IEntityBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SuitDisplayBlock extends TwoPartVerticalBlock implements IEntityBlock { // TODO ITEM DROPS AND TOOL REQ, Texture Lighting Elements better, Redstone TOGGLES DOOR, Walk in suit animation, Auto Place, Charging, Deploy MODE?

    public static final BooleanProperty OPEN = BlockStateProperties.OPEN; // TODO ADD LIKE GROUND LANDING PARTICLES

    public SuitDisplayBlock() {
        super(Properties.copy(Blocks.BEACON));
        this.registerDefaultState(this.stateDefinition.any().setValue(PART, TwoPart.DOWN).setValue(FACING, Direction.NORTH).setValue(OPEN, false));
    }

    @Override @SuppressWarnings("deprecation")
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return ModelUtil.rotateShape(Direction.NORTH, state.getValue(FACING), getShape(state.getValue(PART).down(), state.getValue(OPEN)));
    }

    @Override @SuppressWarnings("deprecation")
    public @NotNull InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.getBlockEntity(getBlockEntityPos(level, pos)) instanceof SuitDisplayBlockEntity be) {
            var open = state.getValue(OPEN);
            if (open && !player.isShiftKeyDown()) SuitDisplayMenu.openMenu(player, getBlockEntityPos(level, pos));
            else be.open(level);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.CONSUME;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        if (state.getValue(PART).down()) return new SuitDisplayBlockEntity(pos, state);
        return null;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(OPEN);
    }

    @Override @SuppressWarnings("deprecation")
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
        super.neighborChanged(state, level, pos, neighborBlock, neighborPos, movedByPiston);
        var otherState = level.getBlockState(neighborPos);
        if (otherState.getBlock() == this && state.getValue(OPEN) != otherState.getValue(OPEN)) level.setBlock(pos, state.setValue(OPEN, otherState.getValue(OPEN)), 3);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, ModBlockEntities.SUIT_DISPLAY.get(), SuitDisplayBlockEntity::tick);
    }

    public static BlockPos getBlockEntityPos(@NotNull BlockAndTintGetter level, BlockPos pos) {
        return level.getBlockEntity(pos) instanceof SuitDisplayBlockEntity ? pos : pos.relative(getOtherPart(level.getBlockState(pos)));
    }

    public VoxelShape getShape(boolean bottom, boolean open) {
        VoxelShape shape = Shapes.empty();
        if (!open) return Shapes.box(0, 0, 0, 1, 1, 0.75);
        if (bottom) {
            shape = Shapes.join(shape, Shapes.box(0, 0, 0, 1, 0.09375, 1), BooleanOp.OR);
            shape = Shapes.join(shape, Shapes.box(0, 0.09375, 0, 0.0625, 1, 0.75), BooleanOp.OR);
            shape = Shapes.join(shape, Shapes.box(0.9375, 0.09375, 0, 1, 1, 0.75), BooleanOp.OR);
            shape = Shapes.join(shape, Shapes.box(0.0625, 0.09375, 0, 0.9375, 1, 0.0625), BooleanOp.OR);
        } else {
            shape = Shapes.join(shape, Shapes.box(0, 0, 0, 1, 1, 0.0625), BooleanOp.OR);
            shape = Shapes.join(shape, Shapes.box(0, 0, 0.0625, 0.0625, 1, 0.75), BooleanOp.OR);
            shape = Shapes.join(shape, Shapes.box(0.9375, 0, 0.0625, 1, 1, 0.75), BooleanOp.OR);
            shape = Shapes.join(shape, Shapes.box(0.0625, 0.9375, 0.0625, 0.9375, 1, 1), BooleanOp.OR);
        }
        return shape;
    }
}
