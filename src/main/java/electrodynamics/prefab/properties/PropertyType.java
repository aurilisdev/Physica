package electrodynamics.prefab.properties;

import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;

public class PropertyType<TYPE, BUFFERTYPE> implements IPropertyType<TYPE, BUFFERTYPE> {

    private final ResourceLocation id;

    private final BiPredicate<TYPE, TYPE> comparison;

    private final Consumer<TagWriter<TYPE>> writeToNbt;

    private final Function<TagReader<TYPE>, TYPE> readFromNbt;
    private final StreamCodec<BUFFERTYPE, TYPE> packetCodec;


    public PropertyType(ResourceLocation id, @Nullable BiPredicate<TYPE, TYPE> comparison, StreamCodec<BUFFERTYPE, TYPE> packetCodec, Consumer<TagWriter<TYPE>> tagWriter, Function<TagReader<TYPE>, TYPE> tagReader) {

        this.id = id;
        if (comparison == null) {
            this.comparison = (thisObject, otherObject) -> thisObject.equals(otherObject);
        } else {
            this.comparison = comparison;
        }
        this.packetCodec = packetCodec;
        this.writeToNbt = tagWriter;
        this.readFromNbt = tagReader;
    }

    @Override
    public StreamCodec<BUFFERTYPE, TYPE> getPacketCodec() {
        return packetCodec;
    }

    @Override
    public void writeToTag(TagWriter<TYPE> writer) {
        writeToNbt.accept(writer);
    }

    @Override
    public TYPE readFromTag(TagReader<TYPE> reader) {
        return readFromNbt.apply(reader);
    }

    @Override
    public boolean hasChanged(TYPE currentValue, TYPE newValue) {
        return comparison.test(currentValue, newValue);
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }
}
