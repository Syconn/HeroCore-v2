package mod.syconn.hero.common.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.mojang.blaze3d.platform.InputConstants;
import mod.syconn.hero.common.entity.ThrownMjolnir;
import mod.syconn.hero.core.ModKeyBindings;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MjolnirItem extends Item {

    private final Multimap<Attribute, AttributeModifier> defaultModifiers;

    public MjolnirItem(Properties pProperties) {
        super(pProperties);
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", 5, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", -2.4F, AttributeModifier.Operation.ADDITION));
        this.defaultModifiers = builder.build();
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
                float f = player.getYRot();
                float g = player.getXRot();
                float h = -Mth.sin(f * (float) (Math.PI / 180.0)) * Mth.cos(g * (float) (Math.PI / 180.0));
                float k = -Mth.sin(g * (float) (Math.PI / 180.0));
                float l = Mth.cos(f * (float) (Math.PI / 180.0)) * Mth.cos(g * (float) (Math.PI / 180.0));
                float m = Mth.sqrt(h * h + k * k + l * l);
                float n = 3.0F * ((3.0F) / 4.0F);
                h *= n / m;
                k *= n / m;
                l *= n / m;
                player.push(h, k, l);
                player.startAutoSpinAttack(20);
                if (player.onGround()) player.move(MoverType.SELF, new Vec3(0.0, 1.1999999F, 0.0));
                pLevel.playSound(null, player, SoundEvents.TRIDENT_RIPTIDE_1, SoundSource.PLAYERS, 1.0F, 1.0F);
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

    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
        return slot == EquipmentSlot.MAINHAND ? this.defaultModifiers : super.getDefaultAttributeModifiers(slot);
    }

    public int getEnchantmentValue() {
        return 1;
    }

    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
        if (level != null && level.isClientSide && InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), InputConstants.KEY_LCONTROL)) {
            tooltipComponents.add(Component.literal(""));
            tooltipComponents.add(Component.literal("To throw Mjolnir press " + ModKeyBindings.key(ModKeyBindings.ABILITY1)).withStyle(ChatFormatting.GOLD));
            tooltipComponents.add(Component.literal("To use ThunderCry press " + ModKeyBindings.key(ModKeyBindings.ABILITY2)).withStyle(ChatFormatting.GOLD));
        } else {
            tooltipComponents.add(Component.literal(""));
            tooltipComponents.add(Component.literal("Press LCTRL for more info").withStyle(ChatFormatting.GOLD));
        }
    }

    public static void throwHammer(Player player, ItemStack stack) {
        ThrownMjolnir thrownMjolnir = new ThrownMjolnir(player.level(), player, stack);
        thrownMjolnir.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 2.5F, 1.0F); // 2.5F
        if (player.isCreative()) thrownMjolnir.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
        player.level().addFreshEntity(thrownMjolnir);
        player.level().playSound(null, thrownMjolnir, SoundEvents.TRIDENT_THROW, SoundSource.PLAYERS, 1.0F, 1.0F);
        if (!player.isCreative()) player.getInventory().removeItem(stack);
        player.getCooldowns().addCooldown(stack.getItem(), 30);
    }
}
