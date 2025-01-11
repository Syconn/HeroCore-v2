package mod.syconn.hero.fabric.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;

import static mod.syconn.hero.core.ModItems.*;

public class LangGen extends FabricLanguageProvider {

    public LangGen(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    public void generateTranslations(TranslationBuilder translationBuilder) {
        translationBuilder.add("itemGroup.hero.hero_items", "Super Hero Gear");
        translationBuilder.add(MJOLNIR.get(), "Mjolnir");
        translationBuilder.add(SHURIKEN.get(), "Shuriken");
        translationBuilder.add(GAUNTLET.get(), "Iron Man Gauntlet");
        translationBuilder.add(MARK_42_HELMET.get(), "Mark 42 Helmet");
        translationBuilder.add(MARK_42_CHESTPLATE.get(), "Mark 42 Chestplate");
        translationBuilder.add(MARK_42_LEGGINGS.get(), "Mark 42 Leggings");
        translationBuilder.add(MARK_42_BOOTS.get(), "Mark 42 Boots");
        translationBuilder.add(INGOT_STEEL.get(), "Steel Ingot");
        translationBuilder.add(INGOT_TIN.get(), "Tin Ingot");
        translationBuilder.add(TITANIUM_PLATE.get(), "Titanium Plate");
        translationBuilder.add(ARC_REACTOR.get(), "Arc Reactor");
        translationBuilder.add("key.categories.hero", "HeroCore Controls");
        translationBuilder.add("key.hero.use.ability1", "Use Ability 1");
        translationBuilder.add("key.hero.use.ability2", "Use Ability 2");
        translationBuilder.add("key.hero.use.ability3", "Use Ability 3");
        translationBuilder.add("key.hero.menu.abilities", "Open Abilities Menu");
        translationBuilder.add("hero.ability.screen", "Ability Menu");
    }
}
