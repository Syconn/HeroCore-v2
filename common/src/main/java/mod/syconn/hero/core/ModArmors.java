package mod.syconn.hero.core;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import mod.syconn.hero.Constants;
import mod.syconn.hero.common.item.IronmanArmorItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

import static mod.syconn.hero.Constants.MOD_ID;

public class ModArmors {

    public static final DeferredRegister<ArmorMaterial> ARMOR = DeferredRegister.create(MOD_ID, Registries.ARMOR_MATERIAL);

    public static final RegistrySupplier<ArmorMaterial> MARK_42 = ARMOR.register("mark_42", () -> new ArmorMaterial(IronmanArmorItem.DEFENSE, 20,
            SoundEvents.ARMOR_EQUIP_IRON, () -> Ingredient.of(ModTags.TITANIUM_PLATE), List.of(new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "mark_42"))), 0,0));
}
