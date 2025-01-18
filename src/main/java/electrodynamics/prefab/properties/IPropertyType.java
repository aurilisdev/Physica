package electrodynamics.prefab.properties;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.StreamCodec;

/**
 * Interface to allow for custom property types in dependent mods
 *
 * @author skip999
 */

public interface IPropertyType<TYPE, BUFFERTYPE> {

    default boolean hasChanged(TYPE currentValue, TYPE newValue) {
        return currentValue.equals(newValue);
    }

    public StreamCodec<BUFFERTYPE, TYPE> getPacketCodec();

    public void writeToTag(TagWriter<TYPE> writer);

    public TYPE readFromTag(TagReader<TYPE> reader);

    public static final record TagWriter<TYPE>(Property<TYPE> prop, CompoundTag tag, HolderLookup.Provider registries) {

    }

    public static final record TagReader<TYPE>(Property<TYPE> prop, CompoundTag tag, HolderLookup.Provider registries) {

    }

}
