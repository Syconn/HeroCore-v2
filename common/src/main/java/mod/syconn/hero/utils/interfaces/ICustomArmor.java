package mod.syconn.hero.utils.interfaces;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public interface ICustomArmor extends IItemExtensions {

    String getMaterialName(ItemStack stack);
    Optional<ResourceLocation> getRenderLocation(LivingEntity entity, EquipmentSlot equipmentSlot);
    SoundEvent getEquipSound(ItemStack stack);
    int getDefense(ItemStack stack);
    float getToughness(ItemStack stack);
}
