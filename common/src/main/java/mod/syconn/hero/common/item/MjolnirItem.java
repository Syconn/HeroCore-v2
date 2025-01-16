package mod.syconn.hero.common.item;

import mod.syconn.hero.common.entity.ThrownMjolnir;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class MjolnirItem extends Item {

    public MjolnirItem(Properties pProperties) {
        super(pProperties.attributes(createAttributes()).component(DataComponents.TOOL, createToolProperties()));
    }

    public static ItemAttributeModifiers createAttributes() {
        return ItemAttributeModifiers.builder()
                .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_ID, 8.0, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_ID, -2.9F, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .build();
    }

    public static Tool createToolProperties() {
        return new Tool(List.of(), 1.0F, 2);
    }

    public boolean canAttackBlock(BlockState state, Level level, BlockPos pos, Player player) {
        return true;
    }

    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BLOCK;
    }

    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pEntityLiving, int pTimeLeft) {
        if (pEntityLiving instanceof Player player) {
            int i = this.getUseDuration(pStack) - pTimeLeft;
            if (i >= 10) {
                if (!pLevel.isClientSide && player.isCrouching()) {
                    ThrownMjolnir thrownMjolnir = new ThrownMjolnir(pLevel, player, pStack);
                    thrownMjolnir.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 2.5F, 1.0F);
                    if (player.isCreative()) thrownMjolnir.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                    pLevel.addFreshEntity(thrownMjolnir);
                    pLevel.playSound(null, thrownMjolnir, SoundEvents.TRIDENT_THROW.value(), SoundSource.PLAYERS, 1.0F, 1.0F);
                    if (!player.isCreative()) player.getInventory().removeItem(pStack);
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
            }
        }
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(player.getItemInHand(hand));
    }

    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        return true;
    }

    public int getEnchantmentValue() {
        return 1;
    }
}
