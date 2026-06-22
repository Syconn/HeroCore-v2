package mod.syconn.hero.item;

import mod.syconn.hero.core.ModTags;
import mod.syconn.hero.feature.ironman.client.renderers.IronmanArmorRenderer;
import mod.syconn.hero.feature.ironman.server.data.SuitTag;
import mod.syconn.hero.utils.generic.FontUtil;
import mod.syconn.hero.utils.interfaces.ICustomArmor;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class IronmanArmorItem extends ArmorItem implements ICustomArmor {

    public IronmanArmorItem(ArmorItem.Type armorType, Properties properties) {
        super(ArmorMaterials.IRON, armorType, properties);
    }

    @Override
    public @NotNull Component getName(ItemStack stack) {
        var suitName = SuitTag.getOrCreate(stack).getMaterial().getName().replace("_", " ").toUpperCase();
        return Component.literal(FontUtil.capitalizeWords(suitName + " " + type.getName().toUpperCase()));
    }

    @Override
    public String getMaterialName(ItemStack stack) {
        return SuitTag.getOrCreate(stack).getMaterial().getName();
    }

    @Override
    public Optional<ResourceLocation> getRenderLocation(LivingEntity entity, EquipmentSlot slot) {
        return IronmanArmorRenderer.getRenderLocation(entity, this, slot);
    }

    @Override
    public boolean isValidRepairItem(ItemStack stack, ItemStack repairCandidate) {
        return SuitTag.getOrCreate(stack).getMaterial().getRepairIngredient().test(repairCandidate) || super.isValidRepairItem(stack, repairCandidate);
    }

    @Override
    public SoundEvent getEquipSound(ItemStack stack) {
        return SuitTag.getOrCreate(stack).getMaterial().getEquipSound();
    }

    @Override
    public int getDefense(ItemStack stack) {
        return SuitTag.getOrCreate(stack).getMaterial().getDefenseForType(this.type);
    }

    @Override
    public float getToughness(ItemStack stack) {
        return SuitTag.getOrCreate(stack).getMaterial().getToughness();
    }

    public static boolean wearingFullSameSuit(Player player) { // TODO NEEDS FIXING
        NonNullList<ItemStack> inv = player.getInventory().armor;
        TagKey<Item> mark = getMark(inv.get(0));
        for (ItemStack stack : inv) {
            if (!stack.is(ModTags.IRONMAN_ARMOR) || mark != getMark(stack)) return false;
            mark = getMark(stack);
        }

        return true;
    }

    public static TagKey<Item> getMark(ItemStack stack) { // TODO NEEDS FIXING
        if (stack.is(ModTags.MARK_2)) return ModTags.MARK_2;
        if (stack.is(ModTags.MARK_5)) return ModTags.MARK_5;
        if (stack.is(ModTags.MARK_42)) return ModTags.MARK_42;
        if (stack.is(ModTags.MARK_43)) return ModTags.MARK_43;
        if (stack.is(ModTags.WAR_MACHINE)) return ModTags.WAR_MACHINE;
        return null;
    }
}
