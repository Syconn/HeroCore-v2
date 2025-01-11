package mod.syconn.hero.common.entity;

import mod.syconn.hero.core.ModDamageTypes;
import mod.syconn.hero.core.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class ThrownMjolnir extends AbstractArrow {

    private ItemStack mjonirItem = new ItemStack(ModItems.MJOLNIR.get());
    private boolean dealtDamage;
    public int clientSideReturnTridentTickCount;

    public ThrownMjolnir(EntityType<? extends ThrownMjolnir> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public ThrownMjolnir(Level pLevel, LivingEntity pShooter, ItemStack pStack) {
        super(EntityType.TRIDENT, pShooter, pLevel);
        mjonirItem = pStack.copy();
    }

    public void tick() {
        if (this.isOnFire()) setRemainingFireTicks(0);

        if (this.inGroundTime > 4) {
            this.dealtDamage = true;
        }

        Entity entity = this.getOwner();
        int speed = 3;
        if ((this.dealtDamage || this.isNoPhysics()) && entity != null) {
            if (!this.isAcceptableReturnOwner()) {
                if (this.level() instanceof ServerLevel sl && this.pickup == Pickup.ALLOWED) this.spawnAtLocation(this.getPickupItem(), 0.1F);
                this.discard();
            } else {
                this.setNoPhysics(true);
                Vec3 vec3 = entity.getEyePosition().subtract(this.position());
                this.setPosRaw(this.getX(), this.getY() + vec3.y * 0.015 * (double)speed, this.getZ());
                if (this.level().isClientSide) this.yOld = this.getY();

                double d0 = 0.05 * (double)speed;
                this.setDeltaMovement(this.getDeltaMovement().scale(0.95).add(vec3.normalize().scale(d0)));
                if (this.clientSideReturnTridentTickCount == 0) this.playSound(SoundEvents.TRIDENT_RETURN, 10.0F, 1.0F);
                this.clientSideReturnTridentTickCount++;
            }
        }

        super.tick();
    }

    private boolean isAcceptableReturnOwner() {
        Entity entity = this.getOwner();
        return entity != null && entity.isAlive() && (!(entity instanceof ServerPlayer) || !entity.isSpectator());
    }

    protected EntityHitResult findHitEntity(Vec3 pStartVec, Vec3 pEndVec) {
        return this.dealtDamage ? null : super.findHitEntity(pStartVec, pEndVec);
    }

    protected void onHitEntity(EntityHitResult pResult) {
        Entity entity = pResult.getEntity();
        float f = 8.0F;
        Entity entity2 = this.getOwner();
        DamageSource damagesource = ModDamageTypes.mjolnir(this, entity2 == null ? this : entity2);

        this.dealtDamage = true;
        if (entity.hurt(damagesource, f)) {
            if (entity.getType() == EntityType.ENDERMAN) return;

            if (entity instanceof LivingEntity livingentity) {
                if (entity2 instanceof LivingEntity) {
                    EnchantmentHelper.doPostHurtEffects(livingentity, entity2);
                    EnchantmentHelper.doPostDamageEffects((LivingEntity)entity2, livingentity);
                }

                this.doPostHurtEffects(livingentity);
            }
        }

        this.setDeltaMovement(this.getDeltaMovement().multiply(-0.01, -0.1, -0.01));
        this.playSound(SoundEvents.TRIDENT_HIT, 1.0F, 1.0F);
        this.strikeLightning(pResult.getEntity().getOnPos());
    }

    protected void onHitBlock(BlockHitResult pResult) {
        super.onHitBlock(pResult);
        this.strikeLightning(pResult.getBlockPos());
    }

    private void strikeLightning(BlockPos point) {
        LightningBolt lightningbolt = EntityType.LIGHTNING_BOLT.create(level());
        if (lightningbolt != null) {
            lightningbolt.moveTo(Vec3.atBottomCenterOf(point));
            lightningbolt.setVisualOnly(false);
            level().addFreshEntity(lightningbolt);
        }
    }

    protected ItemStack getPickupItem() {
        return this.mjonirItem.copy();
    }

    public ItemStack getMjonirItem() {
        return mjonirItem;
    }

    protected boolean tryPickup(Player pPlayer) {
        return super.tryPickup(pPlayer) || this.isNoPhysics() && this.ownedBy(pPlayer) && pPlayer.getInventory().add(this.getPickupItem());
    }

    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(ModItems.MJOLNIR.get());
    }

    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.TRIDENT_HIT_GROUND;
    }

    public void playerTouch(Player pEntity) {
        if (this.ownedBy(pEntity) || this.getOwner() == null) {
            super.playerTouch(pEntity);
        }
    }

    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.dealtDamage = pCompound.getBoolean("DealtDamage");
    }

    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putBoolean("DealtDamage", this.dealtDamage);
    }

    public void tickDespawn() {
        if (this.pickup != Pickup.ALLOWED) {
            super.tickDespawn();
        }
    }

    protected float getWaterInertia() {
        return 0.99F;
    }

    public boolean shouldRender(double pX, double pY, double pZ) {
        return true;
    }
}