package mod.syconn.hero.features.ironman.item;

import mod.syconn.hero.features.ironman.server.data.SuitTag;
import mod.syconn.hero.utils.generic.FontUtil;
import mod.syconn.hero.utils.interfaces.ICustomArmor;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class IronmanArmorItem extends ArmorItem implements ICustomArmor {

    public IronmanArmorItem(Type armorType, Properties properties) {
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

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (entity instanceof Player && stack.getItem() instanceof ICustomArmor) {
            SuitTag.updateIf(stack, t -> !t.open, SuitTag::openCloseSuit);
            SuitTag.update(stack, SuitTag::tick);
        }
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

    public static boolean wearingFullSameSuit(Map<EquipmentSlot, ItemStack> gear) {
        if (gear.get(EquipmentSlot.HEAD).getItem() instanceof IronmanArmorItem) {
            var mark = SuitTag.getOrCreate(gear.get(EquipmentSlot.HEAD)).model;
            for (ItemStack stack : gear.values()) if (!(stack.getItem() instanceof IronmanArmorItem) || !SuitTag.getOrCreate(stack).model.equals(mark)) return false;
            return true;
        }
        return false;
    }
}
