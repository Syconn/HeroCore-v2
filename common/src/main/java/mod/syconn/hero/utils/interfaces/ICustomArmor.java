package mod.syconn.hero.utils.interfaces;

import mod.syconn.hero.features.ironman.client.renderers.item.ModifiedIronmanArmorRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public interface ICustomArmor extends IItemExtensions {

    String getMaterialName(ItemStack stack);
    SoundEvent getEquipSound(ItemStack stack);
    int getDefense(ItemStack stack);
    float getToughness(ItemStack stack);

    default Optional<ResourceLocation> getRenderLocation(LivingEntity entity, EquipmentSlot slot) {
        return this.getRenderLocation(entity.getItemBySlot(slot), slot);
    }

    default Optional<ResourceLocation> getRenderLocation(ItemStack stack, EquipmentSlot slot) {
        return ModifiedIronmanArmorRenderer.getRenderLocation(this, stack, slot);
    }
}
