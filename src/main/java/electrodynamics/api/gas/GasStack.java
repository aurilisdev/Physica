package electrodynamics.api.gas;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.Nullable;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import electrodynamics.registers.ElectrodynamicsGases;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;

/**
 * An implementation of a FluidStack-like object for gases
 *
 * @author skip999
 */
public class GasStack {

    private static final Codec<GasStack> INTERNAL_CODEC = RecordCodecBuilder.create(instance ->
                    //
                    instance.group(
                            //
                            ElectrodynamicsGases.GAS_REGISTRY.holderByNameCodec().fieldOf("gas").forGetter(GasStack::getGasHolder),
                            //
                            Codec.INT.fieldOf("amount").forGetter(instance0 -> instance0.amount),
                            //

                            Codec.INT.fieldOf("temp").forGetter(instance0 -> instance0.temperature),
                            //
                            Codec.INT.fieldOf("pressure").forGetter(instance0 -> instance0.pressure)
                            //
                    ).apply(instance, GasStack::new)
//        
    );

    public static final Codec<GasStack> CODEC = ExtraCodecs.optionalEmptyMap(INTERNAL_CODEC)
            .xmap(optional -> optional.orElse(GasStack.EMPTY), stack -> stack.isEmpty() ? Optional.empty() : Optional.of(stack));

    public static final StreamCodec<RegistryFriendlyByteBuf, GasStack> STREAM_CODEC = new StreamCodec<>() {

        private static final StreamCodec<RegistryFriendlyByteBuf, Holder<Gas>> GAS_STREAM_CODEC = ByteBufCodecs.holderRegistry(ElectrodynamicsGases.GAS_REGISTRY_KEY);

        @Override
        public GasStack decode(RegistryFriendlyByteBuf buffer) {
            int amt = buffer.readInt();

            if (amt <= 0) {
                return GasStack.EMPTY;
            }

            return new GasStack(GAS_STREAM_CODEC.decode(buffer), amt, buffer.readInt(), buffer.readInt());
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buffer, GasStack value) {
            if (value.isEmpty()) {
                buffer.writeInt(0);
            } else {
                buffer.writeInt(value.getAmount());
                GAS_STREAM_CODEC.encode(buffer, value.getGasHolder());
                buffer.writeInt(value.getTemperature());
                buffer.writeInt(value.getPressure());
            }
        }
    };

    public static final GasStack EMPTY = new GasStack(null);

    public static final int ABSOLUTE_ZERO = 1; // zero technically, but that makes volumes a pain in the ass
    public static final int VACUUM = 1; // zero technically, but that makes volumes a pain in the ass

    private final Gas gas;
    private int amount = 0; // mB
    private int temperature = Gas.ROOM_TEMPERATURE;
    private int pressure = Gas.PRESSURE_AT_SEA_LEVEL; // ATM

    private GasStack(@Nullable Void unused) {
        this.gas = null;
    }

    public GasStack(@NonNull Gas gas, int amount, int temperature, int pressure) {
        this.gas = gas;
        this.amount = amount;
        this.temperature = temperature;
        this.pressure = pressure;
    }

    public GasStack(@NonNull Holder<Gas> gas, int amount, int temperature, int pressure) {
        this(gas.value(), amount, temperature, pressure);
    }

    public Gas getGas() {
        return isEmpty() ? ElectrodynamicsGases.EMPTY.get() : gas;
    }

    public Holder<Gas> getGasHolder() {
        return getGas().getBuiltInRegistry();
    }

    public int getAmount() {
        return isEmpty() ? 0 : amount;
    }

    public int getTemperature() {
        return isEmpty() ? Gas.ROOM_TEMPERATURE : temperature;
    }

    public int getPressure() {
        return isEmpty() ? Gas.PRESSURE_AT_SEA_LEVEL : pressure;
    }

    public GasStack copy() {
        if (isEmpty()) {
            return EMPTY;
        }
        return new GasStack(gas, amount, temperature, pressure);
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void shrink(int amount) {
        this.amount -= Math.min(Math.abs(amount), this.amount);
    }

    public void grow(int amount) {
        this.amount += Math.abs(amount);
    }

    /**
     * A negative temperature is analogous to cooling
     * <p>
     * Temperatures cannot drop below 1 degree Kelvin
     *
     * @param deltaTemp The change in temperature
     */
    public void heat(int deltaTemp) {
        amount = getVolumeChangeFromHeating(deltaTemp);
        temperature += deltaTemp;
    }

    /**
     * Sets the pressure of this GasStack to the desired pressure and updates the volume accordingly
     *
     * @param atm
     */
    public void bringPressureTo(int atm) {
        amount = getVolumeChangeFromPressurizing(atm);
        pressure = atm;
    }

    public int getVolumeChangeFromHeating(int deltaTemp) {
        if (isAbsoluteZero() && deltaTemp < 0 || temperature + deltaTemp < ABSOLUTE_ZERO) {
            throw new UnsupportedOperationException("The temperature cannot drop below absolute zero");
        }

        double change = (deltaTemp + (double) temperature) / temperature;

        return (int) Math.ceil(amount * change);

    }

    public int getVolumeChangeFromPressurizing(int atm) {
        if (isVacuum() || atm < VACUUM) {
            throw new UnsupportedOperationException("You cannot have a pressure less than " + VACUUM);
        }

        double change = (double) atm / (double) pressure;

        return (int) Math.ceil(amount / change);
    }

    public boolean isEmpty() {
        return this == EMPTY || this.gas == ElectrodynamicsGases.EMPTY.value() || this.amount <= 0;
    }

    public boolean is(TagKey<Gas> tag) {
        return this.getGas().getBuiltInRegistry().is(tag);
    }

    public boolean is(Gas fluid) {
        return this.getGas() == fluid;
    }

    public boolean is(Predicate<Holder<Gas>> holderPredicate) {
        return holderPredicate.test(this.getGasHolder());
    }

    public boolean is(Holder<Gas> holder) {
        return is(holder.value());
    }

    public boolean is(HolderSet<Gas> holderSet) {
        return holderSet.contains(this.getGasHolder());
    }

    public boolean isSameGas(GasStack other) {
        return this.gas.equals(other.gas);
    }

    public boolean isSameAmount(GasStack other) {
        return amount == other.amount;
    }

    public boolean isSameTemperature(GasStack other) {
        return temperature == other.temperature;
    }

    public boolean isSamePressure(GasStack other) {
        return pressure == other.pressure;
    }

    public boolean isAbsoluteZero() {
        return temperature == ABSOLUTE_ZERO;
    }

    public boolean isVacuum() {
        return pressure < VACUUM;
    }

    public boolean isCondensed() {
        return temperature <= gas.getCondensationTemp();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof GasStack other) {
            boolean empty = isEmpty();
            boolean otherEmpty = other.isEmpty();

            if ((empty && !otherEmpty) || (!empty && otherEmpty)) {
                return false;
            }
            return (empty && otherEmpty) || other.getGas().equals(getGas()) && other.amount == amount && other.temperature == temperature && other.pressure == pressure;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(gas, amount, temperature, pressure);
    }

    @Override
    public String toString() {
        return gas.toString() + ", amount: " + amount + " mB, temp: " + temperature + " K, pressure: " + pressure + " ATM";
    }

    public CompoundTag writeToNbt() {
        CompoundTag tag = new CompoundTag();

        if (isEmpty()) {
            tag.putInt("amount", 0);
        } else {
            tag.putString("name", ElectrodynamicsGases.GAS_REGISTRY.getKey(getGas()).toString());
            tag.putInt("amount", amount);
            tag.putInt("temperature", temperature);
            tag.putInt("pressure", pressure);
        }
        return tag;
    }

    public static GasStack readFromNbt(CompoundTag tag) {
        int amount = tag.getInt("amount");

        if (amount <= 0) {
            return GasStack.EMPTY;
        } else {
            Gas gas = ElectrodynamicsGases.GAS_REGISTRY.get(ResourceLocation.parse(tag.getString("name")));
            int temperature = tag.getInt("temperature");
            int pressure = tag.getInt("pressure");
            return new GasStack(gas, amount, temperature, pressure);
        }
    }

    /**
     * Equalizes the temperature of two gas stacks to their respective median values and adjusts the volume of the resulting
     * stack accordingly
     * <p>
     * The gas with the greater volume becomes the ruling pressure
     * <p>
     * It is assumed you have checked none of the gas stacks are unmodifiable
     *
     * @param stack1 : The first stack
     * @param stack2 : The second stack
     * @return A gas stack that has the average temperature and pressure of the two stacks with the corresponding volume
     */
    public static GasStack equalizePresrsureAndTemperature(GasStack stack1, GasStack stack2) {

        int newPressure = stack1.getAmount() > stack2.getAmount() ? stack1.getPressure() : stack2.getPressure();

        int medianTemperature = (int) ((stack1.temperature + stack2.temperature) / 2.0);

        int deltaT1 = medianTemperature - stack1.temperature;
        int deltaT2 = medianTemperature - stack2.temperature;

        stack1.bringPressureTo(newPressure);
        stack2.bringPressureTo(newPressure);

        stack1.heat(deltaT1);
        stack2.heat(deltaT2);

        return new GasStack(stack1.getGas(), stack1.getAmount() + stack2.getAmount(), medianTemperature, stack1.getPressure());

    }

    /**
     * Determines how much gas from stack 2 could be accepted into a container once stack1 and stack2 have equalized
     * temperatures and pressures
     * <p>
     * The gas stack with the greater volume becomes the ruling pressure
     * <p>
     * It is assumed you have checked none of the gas stacks are unmodifiable
     *
     * @param stack1        : The existing GasStack in the container
     * @param stack2        : The gas attempting to be inserted into the container
     * @param maximumAccept : The capacity of the container
     * @return How much of stack2 could be accepted before the temperatures and pressures equalize
     */
    public static int getMaximumAcceptance(GasStack stack1, GasStack stack2, int maximumAccept) {

        int rulingPressure = stack1.getAmount() > stack2.getAmount() ? stack1.getPressure() : stack2.getPressure();
        double medianTemperature = (stack1.temperature + stack2.temperature) / 2.0;

        double deltaT1 = medianTemperature - stack1.temperature;
        double deltaT2 = medianTemperature - stack2.temperature;

        double deltaP1Factor = (double) rulingPressure / (double) stack1.getPressure();
        double deltaT1Factor = (deltaT1 + stack1.getTemperature()) / stack1.getTemperature();

        double newStack1Volume = stack1.getAmount() * deltaT1Factor / deltaP1Factor;

        double remaining = maximumAccept - newStack1Volume;

        if (remaining <= 0) {
            return 0;
        }

        double deltaP2Factor = (double) rulingPressure / (double) stack2.getPressure();
        double deltaT2Factor = (deltaT2 + stack2.getTemperature()) / stack2.getTemperature();

        double newStack2Volume = stack2.getAmount() * deltaT2Factor / deltaP2Factor;

        if (newStack2Volume <= remaining) {
            return stack2.getAmount();
        }

        return (int) Math.ceil(remaining / deltaT2Factor * deltaP2Factor);

    }

}
