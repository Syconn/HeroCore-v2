package mod.syconn.hero.utils.server;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import mod.syconn.hero.utils.generic.NBTUtil;
import mod.syconn.hero.utils.interfaces.ISerializable;
import mod.syconn.hero.utils.interfaces.ISpecialRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class JsonResourceReloader<D extends ISerializable<CompoundTag>> extends SimpleJsonResourceReloadListener implements SyncedResourceManager.ISyncedData {

    private final Map<ResourceLocation, D> resources = new HashMap<>();
    private final Function<JsonObject, D> jsonReader;
    private final Function<CompoundTag, D> tagReader;
    private final ResourceLocation id;

    public JsonResourceReloader(ResourceLocation id, String directory, Function<JsonObject, D> jsonReader, Function<CompoundTag, D> tagReader) {
        super(new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create(), directory);
        this.jsonReader = jsonReader;
        this.tagReader = tagReader;
        this.id = id;
    }

    public JsonResourceReloader(ResourceLocation id, String directory, Function<JsonObject, D> jsonReader, Function<CompoundTag, D> tagReader, String specialRenderPath) {
        this(id, directory, jsonReader, tagReader);
        ISpecialRenderer.registerPath(specialRenderPath);
    }

    protected void apply(Map<ResourceLocation, JsonElement> pJsonMap, ResourceManager resourceManager, ProfilerFiller profiler) {
        pJsonMap.forEach(((resourceLocation, jsonElement) -> resources.put(resourceLocation.withPath(id.getPath() + "/" + resourceLocation.getPath()), jsonReader.apply(jsonElement.getAsJsonObject()))));
    }

    public void reload(final Map<ResourceLocation, D> resources) {
        this.resources.clear();
        this.resources.putAll(resources);
    }

    public Set<Map.Entry<ResourceLocation, D>> sets() {
        return resources.entrySet();
    }

    public D get(ResourceLocation id) {
        return resources.get(id);
    }

    public ResourceLocation getId() {
        return this.id;
    }

    public CompoundTag writeData() {
        return NBTUtil.putMap(this.resources, NBTUtil::putResourceLocation, ISerializable::writeTag);
    }

    public void readData(CompoundTag tag) {
        this.reload(NBTUtil.getMap(tag, NBTUtil::getResourceLocation, this.tagReader));
    }
}