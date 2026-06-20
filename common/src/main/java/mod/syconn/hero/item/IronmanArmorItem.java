package mod.syconn.hero.item;

import mod.syconn.hero.core.ModTags;
import mod.syconn.hero.feature.ironman.client.renderers.IronmanArmorRenderer;
import mod.syconn.hero.utils.interfaces.ICustomArmor;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public class IronmanArmorItem extends ArmorItem implements ICustomArmor {

    public IronmanArmorItem(ArmorMaterial material, ArmorItem.Type armorType, Properties properties) {
        super(material, armorType, properties);
    }

    @Override
    public String getName() {
        return this.material.getName();
    }

    @Override
    public Optional<ResourceLocation> getRenderLocation(LivingEntity entity, EquipmentSlot slot) {
        return IronmanArmorRenderer.getRenderLocation(entity, this, slot);
    }

    public static boolean wearingFullSameSuit(Player player) {
        NonNullList<ItemStack> inv = player.getInventory().armor;
        TagKey<Item> mark = getMark(inv.get(0));
        for (ItemStack stack : inv) {
            if (!stack.is(ModTags.IRONMAN_ARMOR) || mark != getMark(stack)) return false;
            mark = getMark(stack);
        }

        return true;
    }

    public static TagKey<Item> getMark(ItemStack stack) {
        if (stack.is(ModTags.MARK_2)) return ModTags.MARK_2;
        if (stack.is(ModTags.MARK_5)) return ModTags.MARK_5;
        if (stack.is(ModTags.MARK_42)) return ModTags.MARK_42;
        if (stack.is(ModTags.MARK_43)) return ModTags.MARK_43;
        if (stack.is(ModTags.WAR_MACHINE)) return ModTags.WAR_MACHINE;
        return null;
    }
}
