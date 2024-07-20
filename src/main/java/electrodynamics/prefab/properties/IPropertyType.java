package electrodynamics.prefab.properties;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

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

    public ResourceLocation getId();

    public static final record TagWriter<TYPE>(Property<TYPE> prop, CompoundTag tag, Level world) {

    }

    public static final record TagReader<TYPE>(Property<TYPE> prop, CompoundTag tag, Level world) {

    }

}
