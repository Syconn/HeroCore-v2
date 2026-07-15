package mod.syconn.hero.utils.interfaces;

import mod.syconn.hero.features.ironman.client.renderers.item.ModifiedIronmanArmorRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Optional;

public interface ICustomArmor extends IItemExtensions {

    String getMaterialName(ItemStack stack);
    SoundEvent getEquipSound(ItemStack stack);
    int getDefense(ItemStack stack);
    float getToughness(ItemStack stack);

    default List<Optional<ResourceLocation>> getRenderLocation(LivingEntity entity, EquipmentSlot slot) {
        return this.getRenderLocation(entity.getItemBySlot(slot), slot, entity.getItemBySlot(EquipmentSlot.HEAD));
    }

    default List<Optional<ResourceLocation>> getRenderLocation(ItemStack stack, EquipmentSlot slot, ItemStack dataStack) {
        return ModifiedIronmanArmorRenderer.getRenderLocations(this, stack, slot, dataStack);
    }
}
