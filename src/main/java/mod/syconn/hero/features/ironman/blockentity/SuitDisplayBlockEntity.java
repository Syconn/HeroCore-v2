package mod.syconn.hero.features.ironman.blockentity;

import mod.syconn.hero.blockentities.SyncedBlockEntity;
import mod.syconn.hero.core.ModBlockEntities;
import mod.syconn.hero.core.ModSounds;
import mod.syconn.hero.features.ironman.block.SuitDisplayBlock;
import mod.syconn.hero.features.ironman.client.renderers.entity.IronmanArmorRenderer;
import mod.syconn.hero.features.ironman.item.IronmanArmorItem;
import mod.syconn.hero.features.ironman.server.data.SuitTag;
import mod.syconn.hero.server.container.EquipmentContainer;
import mod.syconn.hero.utils.generic.AnimationUtil;
import mod.syconn.hero.utils.interfaces.ICustomArmor;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class SuitDisplayBlockEntity extends SyncedBlockEntity {

    private static final byte OPEN_TICKS = 12;
    private static final byte SPIN_TICKS = 16;

    private final EquipmentContainer container = new EquipmentContainer();
    private final List<UUID> modifiedPlayer = new ArrayList<>();
    private float suitSpin = 0f;
    private boolean nearbyPlayer = false;
    private boolean lastNearbyPlayer = false;
    private float openProgress = 0f;

    public SuitDisplayBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(ModBlockEntities.SUIT_DISPLAY.get(), pWorldPosition, pBlockState);
        this.container.addListener(c -> this.markDirty());
    }

    public static void tick(Level level, BlockPos pos, BlockState state, SuitDisplayBlockEntity be) {
        if (level.isClientSide) {
            if (be.openProgress > 0) be.openProgress--;
            if (be.openProgress < 0) be.openProgress++;
            if (be.suitSpin > 0) be.suitSpin--;
            if (be.suitSpin < 0) be.suitSpin++;

            be.lastNearbyPlayer = be.nearbyPlayer;
            be.startSuitSpin(!level.getEntitiesOfClass(Player.class, new AABB(be.worldPosition).inflate(1f)).isEmpty());
            var gear = IronmanArmorRenderer.getClientRenderStack(be.getGear());
            if (gear != null && gear.getItem() instanceof ICustomArmor) SuitTag.update(gear, SuitTag::tick);
        } else {
            if (state.getValue(BlockStateProperties.OPEN)) {
                var players = level.getEntitiesOfClass(Player.class, new AABB(be.worldPosition).deflate(0.75f));
                players.forEach(be::handleArmorSwap);
                var nearby = players.stream().map(Player::getUUID).collect(Collectors.toSet());
                be.modifiedPlayer.removeIf(uuid -> !nearby.contains(uuid));
            }

            var stack = be.container.getItem(EquipmentSlot.HEAD.getIndex());
            if (stack.getItem() instanceof IronmanArmorItem) {
                SuitTag.updateIf(stack, t -> t.lifted, SuitTag::openCloseHelmet);
                SuitTag.update(stack, SuitTag::tick);
            }
        }
    }

    private void handleArmorSwap(Player player) { // TODO NOT UPDATING BLOCK COLOR
        if (this.modifiedPlayer.contains(player.getUUID())) return;

        float pitch = 0.95f + player.getRandom().nextFloat() * 0.1f;
        if (IronmanArmorItem.wearingFullSameSuit(player) && this.container.isGearEmpty()) player.level().playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.SUIT_UNEQUIP.get(), SoundSource.PLAYERS, 0.8f, pitch);
        else if (IronmanArmorItem.naked(player) && this.container.isGearFull()) player.level().playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.SUIT_EQUIP.get(), SoundSource.PLAYERS, 0.8f, pitch);
        for (var slot : EquipmentSlot.values()) {
            if (!slot.isArmor()) continue;
            if (!this.container.getItem(slot.getIndex()).isEmpty()) {
                if (player.getItemBySlot(slot).getItem() instanceof ICustomArmor || player.getItemBySlot(slot).isEmpty()) this.swap(player, slot);
                else if (!(player.getItemBySlot(slot).getItem() instanceof ICustomArmor)) {
                    player.addItem(player.getItemBySlot(slot).copy());
                    player.setItemSlot(slot, ItemStack.EMPTY);
                    this.swap(player, slot);
                }
            } else if (player.getItemBySlot(slot).getItem() instanceof ICustomArmor) this.swap(player, slot);
        }
        this.modifiedPlayer.add(player.getUUID());
        player.level().sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
    }

    private void swap(Player player, EquipmentSlot slot) {
        var item = container.getItem(slot.getIndex()).copy();
        container.setItem(slot.getIndex(), player.getItemBySlot(slot).copy());
        player.setItemSlot(slot, item);
        this.markDirty();
    }

    private void startSuitSpin(boolean nearbyPlayer) {
        if (!getBlockState().getValue(BlockStateProperties.OPEN)) nearbyPlayer = false;
        if (this.suitSpin != 0 || nearbyPlayer == lastNearbyPlayer) return;
        this.suitSpin = nearbyPlayer ? -SPIN_TICKS : SPIN_TICKS;
        this.nearbyPlayer = nearbyPlayer;
        if (level != null && level.isClientSide() && nearbyPlayer) {
            var pitch = 0.95f + level.getRandom().nextFloat() * 0.1f;
            level.playLocalSound(worldPosition.getX(), this.worldPosition.getY(), this.worldPosition.getZ(), ModSounds.DISPLAY_SPIN.get(), SoundSource.BLOCKS, 0.8f, pitch, true);
        }
    }

    public float getSuitSpin(float partialTicks) {
        if (this.suitSpin > 0) return AnimationUtil.outCubic(1f - (this.suitSpin - partialTicks) / SPIN_TICKS);
        if (this.suitSpin < 0) return AnimationUtil.inCubic(-(this.suitSpin + partialTicks) / SPIN_TICKS);
        return this.nearbyPlayer ? 0f : 1f;
    }

    public int getColor() {
        var stack = this.container.getItem(EquipmentSlot.CHEST.getIndex());
        return stack.getItem() instanceof ICustomArmor ? SuitTag.getOrCreate(stack).color : DyeColor.GRAY.getFireworkColor();
    }

    public void open(Level level) {
        if (getBlockState().getValue(BlockStateProperties.POWERED) && getBlockState().getValue(BlockStateProperties.OPEN)) return;
        if (level.isClientSide()) this.openProgress = this.getBlockState().getValue(BlockStateProperties.OPEN) ? -OPEN_TICKS : OPEN_TICKS;
        else {
            var pitch = 0.95f + level.getRandom().nextFloat() * 0.1f;
            level.playSound(null, worldPosition.getX(), this.worldPosition.getY(), this.worldPosition.getZ(), this.getBlockState().getValue(BlockStateProperties.OPEN) ? ModSounds.DISPLAY_CLOSE.get() : ModSounds.DISPLAY_OPEN.get(), SoundSource.BLOCKS, 0.8f, pitch);
        }
        level.setBlock(worldPosition, this.getBlockState().setValue(BlockStateProperties.OPEN, !this.getBlockState().getValue(BlockStateProperties.OPEN)), 3);
        this.markDirty();
    }

    public float getOpenProgress(float partialTicks) {
        var open = this.getBlockState().getValue(BlockStateProperties.OPEN);
        if (this.openProgress == 0) return open ? 1 : 0;
        if (this.openProgress > 0) return AnimationUtil.outCubic(1 - (this.openProgress - partialTicks) / OPEN_TICKS);
        return AnimationUtil.inCubic(-(this.openProgress + partialTicks) / OPEN_TICKS);
    }

    public Map<EquipmentSlot, ItemStack> getGear() {
        return this.container.getGear();
    }

    public EquipmentContainer getContainer() {
        return container;
    }

    @Override
    public void load(CompoundTag tag) {
        container.read(tag.getCompound("container"));
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        tag.put("container", container.save());
    }

    public static int getColor(BlockAndTintGetter level, BlockState state, BlockPos pos) {
        if (level != null && state.getBlock() instanceof SuitDisplayBlock && level.getBlockEntity(SuitDisplayBlock.getBlockEntityPos(level, state, pos)) instanceof SuitDisplayBlockEntity be) return be.getColor();
        return -1;
    }
}
