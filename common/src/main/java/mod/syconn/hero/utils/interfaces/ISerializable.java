package mod.syconn.hero.utils.interfaces;

import com.google.gson.JsonObject;
import net.minecraft.nbt.Tag;

public interface ISerializable<T extends Tag> {

    T writeTag();
    JsonObject toJson();
}
