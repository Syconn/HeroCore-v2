package mod.syconn.hero.datagen;

import mod.syconn.hero.Constants;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

import static mod.syconn.hero.registrar.ItemRegistrar.*;

public class LangGen extends LanguageProvider {

    public LangGen(PackOutput output) {
        super(output, Constants.MOD_ID, "en_us");
    }

    protected void addTranslations() {
        add("itemGroup.hero.hero_items", "Super Hero Gear");
        add(MJOLNIR.get(), "Mjolnir");
        add(SHURIKEN.get(), "Shuriken");
        add(GAUNTLET.get(), "Iron Man Gauntlet");
        add(MARK_42_HELMET.get(), "Mark 42 Helmet");
        add(MARK_42_CHESTPLATE.get(), "Mark 42 Chestplate");
        add(MARK_42_LEGGINGS.get(), "Mark 42 Leggings");
        add(MARK_42_BOOTS.get(), "Mark 42 Boots");
        add(INGOT_STEEL.get(), "Steel Ingot");
        add(INGOT_TIN.get(), "Tin Ingot");
        add(TITANIUM_PLATE.get(), "Titanium Plate");
        add(ARC_REACTOR.get(), "Arc Reactor");
        add("key.categories.hero", "HeroCore Controls");
        add("key.hero.use.ability1", "Use Ability 1");
        add("key.hero.use.ability2", "Use Ability 2");
        add("key.hero.use.ability3", "Use Ability 3");
        add("key.hero.menu.abilities", "Open Abilities Menu");
        add("hero.ability.screen", "Ability Menu");
    }
}
