package mod.syconn.hero.blockentities;

import mod.syconn.hero.block.SuitDisplayBlock;
import mod.syconn.hero.client.HeroClient;
import mod.syconn.hero.core.ModBlockEntities;
import mod.syconn.hero.features.addons.IronmanContent;
import mod.syconn.hero.features.ironman.server.data.SuitTag;
import mod.syconn.hero.utils.block.ModBlockStateProperties;
import mod.syconn.hero.utils.generic.AnimationUtil;
import mod.syconn.hero.utils.generic.NBTUtil;
import mod.syconn.hero.utils.interfaces.ICustomArmor;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;

import java.util.HashMap;
import java.util.Map;

public class SuitDisplayBlockEntity extends SyncedBlockEntity {

    private static final byte OPEN_TICKS = 12;
    private static final byte SPIN_TICKS = 16;

    private Map<EquipmentSlot, ItemStack> gear = new HashMap<>();
    private float suitSpin = 0f;
    private boolean nearbyPlayer = false;
    private boolean lastNearbyPlayer = false;
    private float openProgress = 0f;

    public SuitDisplayBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(ModBlockEntities.SUIT_DISPLAY.get(), pWorldPosition, pBlockState);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, SuitDisplayBlockEntity be) {
        if (level.isClientSide) {
            if (be.openProgress > 0) be.openProgress--;
            if (be.openProgress < 0) be.openProgress++;
            if (be.suitSpin > 0) be.suitSpin--;
            if (be.suitSpin < 0) be.suitSpin++;

            be.lastNearbyPlayer = be.nearbyPlayer;
            be.nearbyPlayer = !level.getEntitiesOfClass(Player.class, new AABB(be.worldPosition).inflate(0.5f)).isEmpty();
            be.startSuitSpin();
        } else {
//            var playersWithin = level.getEntitiesOfClass(Player.class, new AABB(be.worldPosition));

        }
    }

    private void startSuitSpin() {
        if (this.suitSpin != 0 || nearbyPlayer == lastNearbyPlayer) return;
        this.suitSpin = nearbyPlayer ? -SPIN_TICKS : SPIN_TICKS;
    }

    public float getSuitSpin(float partialTicks) {
        if (this.suitSpin == 0) return nearbyPlayer ? 0 : 1;
        if (this.suitSpin > 0) return AnimationUtil.outCubic(1 - (this.suitSpin - partialTicks) / SPIN_TICKS);
        return AnimationUtil.inCubic(-(this.suitSpin + partialTicks) / SPIN_TICKS);
    }

    public int getColor() {
        var stack = gear.get(EquipmentSlot.CHEST);
        return stack != null && stack.getItem() instanceof ICustomArmor ? SuitTag.getOrCreate(stack).color : DyeColor.GRAY.getFireworkColor();
    }

    public void open(Level level) {
        if (level.isClientSide()) this.openProgress = this.getBlockState().getValue(BlockStateProperties.OPEN) ? -OPEN_TICKS : OPEN_TICKS;
        level.setBlock(worldPosition, this.getBlockState().setValue(BlockStateProperties.OPEN, !this.getBlockState().getValue(BlockStateProperties.OPEN)), 3);
        this.markDirty();
    }

    public float getOpenProgress(float partialTicks) {
        var open = this.getBlockState().getValue(BlockStateProperties.OPEN);
        if (this.openProgress == 0) return open ? 1 : 0;
        if (this.openProgress > 0) return AnimationUtil.outCubic(1 - (this.openProgress - partialTicks) / OPEN_TICKS);
        return AnimationUtil.inCubic(-(this.openProgress + partialTicks) / OPEN_TICKS);
    }

    public void setFromModel(ItemStack stack) {
        this.setGear(IronmanContent.createSuitMap(SuitTag.getOrCreate(stack).model));
        this.markDirty();
    }

    private void setGear(Map<EquipmentSlot, ItemStack> gear) {
        this.gear = gear;
    }

    public Map<EquipmentSlot, ItemStack> getGear() {
        return gear;
    }

    @Override
    public void load(CompoundTag tag) {
        this.gear = NBTUtil.getMap(tag.getCompound("gear"), t -> NBTUtil.getEnum(EquipmentSlot.class, t), ItemStack::of);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        tag.put("gear", NBTUtil.putMap(this.gear, NBTUtil::putEnum, s -> NBTUtil.convert(s::save)));
    }

    public static int getColor(BlockAndTintGetter level, BlockState state, BlockPos pos) {
        if (level != null && level.getBlockEntity(SuitDisplayBlock.getBlockEntityPos(level, state, pos)) instanceof SuitDisplayBlockEntity be) return be.getColor();
        return -1;
    }
}
