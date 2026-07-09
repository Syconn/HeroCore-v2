package mod.syconn.hero.utils.interfaces;

import com.google.common.collect.Multimap;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public interface IItemExtensions {

    default Multimap<Attribute, AttributeModifier> getAttributeModifications(ItemStack stack, EquipmentSlot slot) {
        return ((Item) this).getDefaultAttributeModifiers(slot);
    }
}
