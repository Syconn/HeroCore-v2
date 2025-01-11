package mod.syconn.hero.item;

import mod.syconn.hero.extra.data.components.EnergyComponent;
import mod.syconn.hero.registrar.ComponentRegistrar;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;

import java.util.List;

public class IronmanArmorItem extends ArmorItem {

    public IronmanArmorItem(ArmorMaterial material, ArmorType armorType, Properties properties) {
        super(material, armorType, properties);
    }

    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        EnergyComponent component = stack.getOrDefault(ComponentRegistrar.ENERGY_COMPONENT.get(), new EnergyComponent(100, 100));
        tooltipComponents.add(Component.literal("Energy: " + (int) component.energy() + "/" + (int) component.maxEnergy()).withStyle(ChatFormatting.AQUA));
    }
}
