package electrodynamics.common.recipe.recipeutils;

import java.util.ArrayList;
import java.util.List;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import electrodynamics.Electrodynamics;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.fluids.FluidStack;

public class ProbableFluid {

    public static final Codec<ProbableFluid> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            //
            BuiltInRegistries.FLUID.byNameCodec().fieldOf("fluid").forGetter(instance0 -> instance0.fluid.getFluid()),
            //
            Codec.INT.fieldOf("amount").forGetter(instance0 -> instance0.fluid.getAmount()),
            //
            Codec.DOUBLE.fieldOf("chance").forGetter(instance0 -> instance0.chance)

    )
            //
            .apply(instance, (fluid, amt, chance) -> new ProbableFluid(new FluidStack(fluid, amt), chance))

    //
    );

    public static final Codec<List<ProbableFluid>> LIST_CODEC = CODEC.listOf();

    public static final StreamCodec<RegistryFriendlyByteBuf, ProbableFluid> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public ProbableFluid decode(RegistryFriendlyByteBuf buf) {
            return new ProbableFluid(FluidStack.STREAM_CODEC.decode(buf), buf.readDouble());
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, ProbableFluid fluid) {
            FluidStack.STREAM_CODEC.encode(buf, fluid.fluid);
            buf.writeDouble(fluid.chance);
        }
    };

    public static final StreamCodec<RegistryFriendlyByteBuf, List<ProbableFluid>> LIST_STREAM_CODEC = new StreamCodec<>() {

        @Override
        public void encode(RegistryFriendlyByteBuf buf, List<ProbableFluid> probable) {
            buf.writeInt(probable.size());
            for (ProbableFluid fluid : probable) {
                STREAM_CODEC.encode(buf, fluid);
            }
        }

        @Override
        public List<ProbableFluid> decode(RegistryFriendlyByteBuf buf) {
            int count = buf.readInt();
            List<ProbableFluid> fluids = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                fluids.add(STREAM_CODEC.decode(buf));
            }
            return fluids;
        }
    };

    public static final List<ProbableFluid> NONE = new ArrayList<>();

    private FluidStack fluid;
    // 0: 0% chance
    // 1: 100% chance
    private double chance;

    public ProbableFluid(FluidStack stack, double chance) {
        fluid = stack;
        setChance(chance);
    }

    public FluidStack getFullStack() {
        return fluid;
    }

    private void setChance(double chance) {
        this.chance = chance > 1 ? 1 : chance < 0 ? 0 : chance;
    }

    public double getChance() {
        return chance;
    }

    public FluidStack roll() {
        double random = Electrodynamics.RANDOM.nextDouble();
        if (random > 1 - chance) {
            double amount = chance >= 1 ? fluid.getAmount() : fluid.getAmount() * random;
            int fluidAmount = (int) Math.ceil(amount);
            return new FluidStack(fluid.getFluidHolder(), fluidAmount);
        }
        return FluidStack.EMPTY;
    }

}
