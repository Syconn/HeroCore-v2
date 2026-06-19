package mod.syconn.hero.utils.interfaces;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;

import java.util.Optional;

public interface ICustomArmor {

    String getName();
    Optional<ResourceLocation> getRenderLocation(LivingEntity entity, EquipmentSlot equipmentSlot);
}
