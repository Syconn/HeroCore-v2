package mod.syconn.hero.fabric.datagen;

import mod.syconn.hero.core.ModKeys;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;

import static mod.syconn.hero.core.ModItems.*;

public class LangGen extends FabricLanguageProvider {

    public LangGen(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    public void generateTranslations(TranslationBuilder translationBuilder) {
        translationBuilder.add("itemGroup.hero.hero_items", "Super Hero Gear");

        translationBuilder.add(MARK_2_HELMET.get(), "Mark 2 Helmet");
        translationBuilder.add(MARK_2_CHESTPLATE.get(), "Mark 2 Chestplate");
        translationBuilder.add(MARK_2_LEGGINGS.get(), "Mark 2 Leggings");
        translationBuilder.add(MARK_2_BOOTS.get(), "Mark 2 Boots");

        translationBuilder.add(MARK_5_HELMET.get(), "Mark 5 Helmet");
        translationBuilder.add(MARK_5_CHESTPLATE.get(), "Mark 5 Chestplate");
        translationBuilder.add(MARK_5_LEGGINGS.get(), "Mark 5 Leggings");
        translationBuilder.add(MARK_5_BOOTS.get(), "Mark 5 Boots");

        translationBuilder.add(MARK_42_HELMET.get(), "Mark 42 Helmet");
        translationBuilder.add(MARK_42_CHESTPLATE.get(), "Mark 42 Chestplate");
        translationBuilder.add(MARK_42_LEGGINGS.get(), "Mark 42 Leggings");
        translationBuilder.add(MARK_42_BOOTS.get(), "Mark 42 Boots");

        translationBuilder.add(MARK_43_HELMET.get(), "Mark 43 Helmet");
        translationBuilder.add(MARK_43_CHESTPLATE.get(), "Mark 43 Chestplate");
        translationBuilder.add(MARK_43_LEGGINGS.get(), "Mark 43 Leggings");
        translationBuilder.add(MARK_43_BOOTS.get(), "Mark 43 Boots");

        translationBuilder.add(WAR_MACHINE_HELMET.get(), "War Machine Helmet");
        translationBuilder.add(WAR_MACHINE_CHESTPLATE.get(), "War Machine Chestplate");
        translationBuilder.add(WAR_MACHINE_LEGGINGS.get(), "War Machine Leggings");
        translationBuilder.add(WAR_MACHINE_BOOTS.get(), "War Machine Boots");

        translationBuilder.add(ModKeys.modCategory(), "HeroCore Controls");
        translationBuilder.add("hero.ability.screen", "Ability Menu");
    }
}
