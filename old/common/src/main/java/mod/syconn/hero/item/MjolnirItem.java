package mod.syconn.hero.item;

import mod.syconn.hero.entity.ThrownMjolnir;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class MjolnirItem extends Item implements IUseAnim {

    public MjolnirItem(Properties pProperties) {
        super(ToolMaterial.IRON.applyToolProperties(pProperties, BlockTags.MINEABLE_WITH_PICKAXE, 5, -2.4F));
    }

    public int getUseDuration(ItemStack pStack, LivingEntity pEntity) {
        return 72000;
    }

    public boolean releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pEntityLiving, int pTimeLeft) {
        if (pEntityLiving instanceof Player player) {
            int i = this.getUseDuration(pStack, pEntityLiving) - pTimeLeft;
            if (i >= 10) {
                if (!pLevel.isClientSide && player.isCrouching()) {
                    ThrownMjolnir thrownMjolnir = new ThrownMjolnir(pLevel, player, pStack);
                    thrownMjolnir.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 2.5F, 1.0F);
                    if (player.hasInfiniteMaterials()) thrownMjolnir.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                    pLevel.addFreshEntity(thrownMjolnir);
                    pLevel.playSound(null, thrownMjolnir, SoundEvents.TRIDENT_THROW.value(), SoundSource.PLAYERS, 1.0F, 1.0F);
                    if (!player.hasInfiniteMaterials()) player.getInventory().removeItem(pStack);
                }
                else if (!player.isCrouching()) {
                    float f7 = player.getYRot();
                    float f1 = player.getXRot();
                    float f2 = -Mth.sin(f7 * (float) (Math.PI / 180.0)) * Mth.cos(f1 * (float) (Math.PI / 180.0));
                    float f3 = -Mth.sin(f1 * (float) (Math.PI / 180.0));
                    float f4 = Mth.cos(f7 * (float) (Math.PI / 180.0)) * Mth.cos(f1 * (float) (Math.PI / 180.0));
                    float f5 = Mth.sqrt(f2 * f2 + f3 * f3 + f4 * f4);
                    f2 *= 3 / f5;
                    f3 *= 3 / f5;
                    f4 *= 3 / f5;
                    player.push(f2, f3, f4);
                    player.startAutoSpinAttack(20, 8.0F, pStack);
                    if (player.onGround()) player.move(MoverType.SELF, new Vec3(0.0, 1.1999999F, 0.0));
                    pLevel.playSound(null, player, SoundEvents.TRIDENT_THROW.value(), SoundSource.PLAYERS, 1.0F, 1.0F);
                }
                player.awardStat(Stats.ITEM_USED.get(this));
                return true;
            }
        }
        return false;
    }

    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        player.startUsingItem(hand);
        return InteractionResult.CONSUME;
    }

    public UseAnim getAnimation() {
        return UseAnim.THROW_HAMMER;
    }

    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        return true;
    }
}
