package electrodynamics.common.recipe.recipeutils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import electrodynamics.api.gas.Gas;
import electrodynamics.api.gas.GasStack;
import electrodynamics.common.recipe.ElectrodynamicsRecipeInit;
import electrodynamics.registers.ElectrodynamicsGases;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.crafting.ICustomIngredient;
import net.neoforged.neoforge.common.crafting.IngredientType;
import net.neoforged.neoforge.common.util.NeoForgeExtraCodecs;

/**
 * An extension of the Ingredient class for Gases
 * 
 * @author skip999
 *
 */
public class GasIngredient implements Predicate<GasStack>, ICustomIngredient {

    public static final MapCodec<GasIngredient> CODEC_DIRECT_GAS = RecordCodecBuilder.mapCodec(instance ->
    //
    instance.group(

            //
            ElectrodynamicsGases.GAS_REGISTRY.byNameCodec().fieldOf("gas").forGetter(instance0 -> instance0.gas),
            //
            Codec.INT.fieldOf("amount").forGetter(instance0 -> instance0.amount),
            //
            Codec.INT.fieldOf("temp").forGetter(instance0 -> instance0.temperature),
            //
            Codec.INT.fieldOf("pressure").forGetter(instance0 -> instance0.pressure)

    //
    ).apply(instance, GasIngredient::new)
    //

    );

    public static final MapCodec<GasIngredient> CODEC_TAGGED_GAS = RecordCodecBuilder.mapCodec(instance ->
    //
    instance.group(

            //
            TagKey.codec(ElectrodynamicsGases.GAS_REGISTRY_KEY).fieldOf("tag").forGetter(instance0 -> instance0.tag),
            //
            Codec.INT.fieldOf("amount").forGetter(instance0 -> instance0.amount),
            //
            Codec.INT.fieldOf("temp").forGetter(instance0 -> instance0.temperature),
            //
            Codec.INT.fieldOf("pressure").forGetter(instance0 -> instance0.pressure)

    //
    ).apply(instance, GasIngredient::new)
    //

    );

    public static final MapCodec<GasIngredient> CODEC = NeoForgeExtraCodecs.xor(CODEC_TAGGED_GAS, CODEC_DIRECT_GAS).xmap(either -> either.map(tag -> tag, gas -> gas), value -> {
        //

        if (value.tag != null) {
            return Either.left(value);
        } else if (value.gas != null) {
            return Either.right(value);
        } else {
            throw new UnsupportedOperationException("The Gas Ingredient neither has a tag nor a direct gas value defined!");
        }

    });

    public static final Codec<List<GasIngredient>> LIST_CODEC = CODEC.codec().listOf();

    public static final StreamCodec<RegistryFriendlyByteBuf, GasIngredient> STREAM_CODEC = new StreamCodec<>() {

        @Override
        public void encode(RegistryFriendlyByteBuf buf, GasIngredient ing) {
            List<GasStack> gasStacks = ing.getMatchingGases();
            buf.writeInt(gasStacks.size());
            for (GasStack stack : gasStacks) {
                GasStack.STREAM_CODEC.encode(buf, stack);
            }
        }

        @Override
        public GasIngredient decode(RegistryFriendlyByteBuf buf) {
            List<GasStack> stacks = new ArrayList<>();
            int count = buf.readInt();
            for (int i = 0; i < count; i++) {
                stacks.add(GasStack.STREAM_CODEC.decode(buf));
            }
            return new GasIngredient(stacks);
        }
    };

    public static final StreamCodec<RegistryFriendlyByteBuf, List<GasIngredient>> LIST_STREAM_CODEC = new StreamCodec<>() {

        @Override
        public void encode(RegistryFriendlyByteBuf buf, List<GasIngredient> ings) {
            buf.writeInt(ings.size());
            for (GasIngredient ing : ings) {
                STREAM_CODEC.encode(buf, ing);
            }
        }

        @Override
        public List<GasIngredient> decode(RegistryFriendlyByteBuf buf) {
            int length = buf.readInt();
            List<GasIngredient> ings = new ArrayList<>();
            for (int i = 0; i < length; i++) {
                ings.add(STREAM_CODEC.decode(buf));
            }
            return ings;
        }
    };

    @Nonnull
    private List<GasStack> gasStacks;

    @Nullable
    private TagKey<Gas> tag;
    @Nullable
    private Gas gas;
    private int amount;
    private int temperature;
    private int pressure;

    public GasIngredient(Gas gas, int amount, int temperature, int pressure) {
        this(new GasStack(gas, amount, temperature, pressure));
    }

    public GasIngredient(GasStack gasStack) {
        this.gas = gasStack.getGas();
        this.amount = gasStack.getAmount();
        this.temperature = gasStack.getTemperature();
        this.pressure = gasStack.getPressure();
    }

    public GasIngredient(List<GasStack> stacks) {
        gasStacks = stacks;
        GasStack gas = getGasStack();
        this.gas = gas.getGas();
        this.amount = gas.getAmount();
        this.temperature = gas.getTemperature();
        this.pressure = gas.getPressure();
    }

    public GasIngredient(TagKey<Gas> tag, int amount, int temperature, int pressure) {
        this.tag = tag;
        this.amount = amount;
        this.temperature = temperature;
        this.pressure = pressure;
    }

    @Override
    public boolean test(ItemStack stack) {
        return false;
    }

    @Override
    public Stream<ItemStack> getItems() {
        return null;
    }

    @Override
    public boolean isSimple() {
        return false;
    }

    @Override
    public IngredientType<?> getType() {
        return ElectrodynamicsRecipeInit.GAS_INGREDIENT_TYPE.get();
    }

    public boolean testGas(@Nullable GasStack gas, boolean checkTemperature, boolean checkPressure) {
        if (gas == null || gas.isEmpty()) {
            return false;
        }
        for (GasStack g : getMatchingGases()) {
            if (gas.getAmount() >= g.getAmount()) {
                if (g.isSameGas(gas)) {
                    if (!checkTemperature && !checkPressure) {
                        return true;
                    }
                    boolean sameTemp = g.isSameTemperature(gas);
                    boolean samePres = g.isSamePressure(gas);
                    if (!checkTemperature) {
                        return samePres;
                    }
                    if (checkPressure) {
                        return sameTemp && samePres;
                    }
                    return sameTemp;
                }
            }
        }
        return false;

    }

    public GasStack getGasStack() {
        return getMatchingGases().size() < 1 ? GasStack.EMPTY : getMatchingGases().get(0);
    }

    public List<GasStack> getMatchingGases() {

        if (gasStacks == null) {
            gasStacks = new ArrayList<>();
            if (tag != null) {
                ElectrodynamicsGases.GAS_REGISTRY.getTag(tag).get().forEach(h -> {
                    gasStacks.add(new GasStack(h.value(), amount, temperature, pressure));
                });
            } else if (gas != null) {
                gasStacks.add(new GasStack(gas, amount, temperature, pressure));
            } else {
                throw new UnsupportedOperationException("Gas Ingredient has neither a gas nor a gas tag defined");
            }
        }

        return gasStacks;

    }

    @Override
    public String toString() {
        return getGasStack().toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof GasIngredient ing) {
            return ing.getMatchingGases().equals(getMatchingGases()) && ing.amount == amount && ing.pressure == pressure && ing.temperature == temperature;
        }
        return false;
    }

    @Override
    public boolean test(GasStack gasStack) {
        return testGas(gasStack, true, true);
    }
}
