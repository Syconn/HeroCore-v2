package mod.syconn.hero.common.item;

import mod.syconn.hero.common.components.EnergyComponent;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class IronmanArmorItem extends ArmorItem {

    public static final Map<Type, Integer> DEFENSE = Util.make(new EnumMap<>(Type.class), map -> {
        map.put(Type.BOOTS, 2);
        map.put(Type.LEGGINGS, 5);
        map.put(Type.CHESTPLATE, 6);
        map.put(Type.HELMET, 2);
        map.put(Type.BODY, 4);
    });

    public IronmanArmorItem(Holder<ArmorMaterial> material, ArmorItem.Type armorType, Properties properties) {
        super(material, armorType, properties.stacksTo(1));
    }

    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        EnergyComponent energy = EnergyComponent.from(stack);
        tooltipComponents.add(Component.literal("Energy: " + energy.energy() + "/" + energy.max()).withStyle(ChatFormatting.AQUA));
    }
}
