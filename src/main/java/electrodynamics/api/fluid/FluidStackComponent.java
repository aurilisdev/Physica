package electrodynamics.api.fluid;

import java.util.Objects;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.fluids.FluidStack;

public class FluidStackComponent {

    public static final Codec<FluidStackComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(

            FluidStack.OPTIONAL_CODEC.fieldOf("fluid").forGetter(instance0 -> instance0.fluid)

    ).apply(instance, FluidStackComponent::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, FluidStackComponent> STREAM_CODEC = StreamCodec.composite(
            FluidStack.OPTIONAL_STREAM_CODEC, instance -> instance.fluid,
            FluidStackComponent::new

    );

    public static final FluidStackComponent EMPTY = new FluidStackComponent(FluidStack.EMPTY);

    public FluidStack fluid = FluidStack.EMPTY;

    public FluidStackComponent(FluidStack fluid) {
        this.fluid = fluid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FluidStackComponent that = (FluidStackComponent) o;
        return Objects.equals(fluid, that.fluid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fluid);
    }
}
