package mod.syconn.hero.blockentities;

import mod.syconn.hero.block.SuitDisplayBlock;
import mod.syconn.hero.core.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class SuitDisplayBlockEntity extends SyncedBlockEntity {

    private int color = DyeColor.GRAY.getFireworkColor();
    private float openProgress = 0f;

    public SuitDisplayBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(ModBlockEntities.SUIT_DISPLAY.get(), pWorldPosition, pBlockState);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, SuitDisplayBlockEntity be) {
        if (level.isClientSide) {
            float target = state.getValue(BlockStateProperties.OPEN) ? 1f : 0f;
            be.openProgress = Mth.approach(be.openProgress, target, 0.15f);
        }
    }

    public void setColor(int color) {
        this.color = color;
        markDirty();
    }

    public int getColor() {
        return color;
    }

    public float getOpenProgress() {
        return openProgress;
    }

    @Override
    public void load(CompoundTag tag) {
        this.color = tag.getInt("color");
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        tag.putInt("color", this.color);
    }

    public static int getColor(BlockAndTintGetter level, BlockState state, BlockPos pos) {
        if (level != null && level.getBlockEntity(SuitDisplayBlock.getBlockEntityPos(level, state, pos)) instanceof SuitDisplayBlockEntity be) return be.getColor();
        return -1;
    }
}
