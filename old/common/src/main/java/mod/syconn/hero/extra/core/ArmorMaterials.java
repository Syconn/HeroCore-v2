package mod.syconn.hero.extra.core;

import mod.syconn.hero.registrar.TagRegistrar;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;

import java.util.HashMap;

public class ArmorMaterials {

    public static final ArmorMaterial MARK_42 = new ArmorMaterial(15, new HashMap<>() {{
        put(ArmorType.BOOTS, 2);
        put(ArmorType.LEGGINGS, 5);
        put(ArmorType.CHESTPLATE, 6);
        put(ArmorType.HELMET, 2);
        put(ArmorType.BODY, 5);
    }}, 9, SoundEvents.ARMOR_EQUIP_IRON, 0.0F, 0.0F, TagRegistrar.TITANIUM_PLATE, TagRegistrar.MARK_42);
}
