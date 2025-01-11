package mod.syconn.hero.datagen;

import mod.syconn.hero.Constants;
import mod.syconn.hero.registrar.TagRegistrar;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

@OnlyIn(Dist.CLIENT)

public class EquipmentGen implements DataProvider {

    private final PackOutput.PathProvider pathProvider;

    public EquipmentGen(PackOutput output) {
        this.pathProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "equipment");
    }

    private static void bootstrap(BiConsumer<ResourceKey<EquipmentAsset>, EquipmentClientInfo> output) {
        output.accept(TagRegistrar.MARK_42, onlyHumanoid("mark_42"));
    }

    private static EquipmentClientInfo onlyHumanoid(String name) {
        return EquipmentClientInfo.builder().addHumanoidLayers(Constants.withId(name)).build();
    }

    private static EquipmentClientInfo humanoidAndHorse(String name) {
        return EquipmentClientInfo.builder()
                .addHumanoidLayers(Constants.withId(name))
                .addLayers(
                        EquipmentClientInfo.LayerType.HORSE_BODY, EquipmentClientInfo.Layer.leatherDyeable(Constants.withId(name), false)
                )
                .build();
    }

    public CompletableFuture<?> run(CachedOutput output) {
        Map<ResourceKey<EquipmentAsset>, EquipmentClientInfo> map = new HashMap<>();
        bootstrap((resource, equipmentClientInfo) -> {
            if (map.putIfAbsent(resource, equipmentClientInfo) != null) throw new IllegalStateException("Tried to register equipment asset twice for id: " + resource);
        });
        return DataProvider.saveAll(output, EquipmentClientInfo.CODEC, this.pathProvider::json, map);
    }

    public String getName() {
        return "Equipment Asset Definitions";
    }
}
