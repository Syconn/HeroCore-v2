package mod.syconn.hero.item;

import mod.syconn.hero.feature.ironman.client.renderers.IronmanArmorRenderer;
import mod.syconn.hero.feature.ironman.server.data.SuitTag;
import mod.syconn.hero.utils.generic.FontUtil;
import mod.syconn.hero.utils.interfaces.ICustomArmor;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class IronmanArmorItem extends ArmorItem implements ICustomArmor { // TODO SERVERS BREAK

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

    public static boolean wearingFullSameSuit(Player player) {
        var inv = player.getInventory().armor;
        if (inv.get(0).getItem() instanceof IronmanArmorItem) {
            var mark = SuitTag.getOrCreate(inv.get(0)).model;
            for (ItemStack stack : inv) if (!(stack.getItem() instanceof IronmanArmorItem) || !SuitTag.getOrCreate(stack).model.equals(mark)) return false;
            return true;
        }
        return false;
    }
}
