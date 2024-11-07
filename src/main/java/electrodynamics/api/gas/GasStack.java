package electrodynamics.api.gas;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import electrodynamics.registers.ElectrodynamicsGases;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

import java.util.Objects;

/**
 * An implementation of a FluidStack-like object for gases
 *
 * @author skip999
 */
public class GasStack {

    public static final Codec<GasStack> CODEC = RecordCodecBuilder.create(instance ->
                    //
                    instance.group(
                            //
                            ElectrodynamicsGases.GAS_REGISTRY.byNameCodec().fieldOf("gas").forGetter(instance0 -> instance0.gas.value()),
                            //
                            Codec.DOUBLE.fieldOf("amount").forGetter(instance0 -> instance0.amount),
                            //

                            Codec.DOUBLE.fieldOf("temp").forGetter(instance0 -> instance0.temperature),
                            //
                            Codec.INT.fieldOf("pressure").forGetter(instance0 -> instance0.pressure)
                            //
                    ).apply(instance, GasStack::new)
//        
    );

    public static final StreamCodec<FriendlyByteBuf, GasStack> STREAM_CODEC = StreamCodec.composite(

            ByteBufCodecs.fromCodec(ElectrodynamicsGases.GAS_REGISTRY.byNameCodec()), GasStack::getGas,
            ByteBufCodecs.DOUBLE, GasStack::getAmount,
            ByteBufCodecs.DOUBLE, GasStack::getTemperature,
            ByteBufCodecs.INT, GasStack::getPressure,
            GasStack::new
    );

    public static final GasStack EMPTY = new GasStack();

    public static final double ABSOLUTE_ZERO = 1; // zero technically, but that makes volumes a pain in the ass
    public static final int VACUUM = 1; // zero technically, but that makes volumes a pain in the ass

    private Holder<Gas> gas = ElectrodynamicsGases.EMPTY;
    private double amount = 0; // mB
    private double temperature = Gas.ROOM_TEMPERATURE;
    private int pressure = Gas.PRESSURE_AT_SEA_LEVEL; // ATM

    private boolean isEmpty = false;

    private GasStack() {
        isEmpty = true;
    }

    public GasStack(Gas gas) {
        this.gas = Holder.direct(gas);
        if (gas == ElectrodynamicsGases.EMPTY) {
            isEmpty = true;
        }
    }

    public GasStack(Gas gas, double amount, double temperature, int pressure) {
        this(gas);
        this.amount = amount;
        this.temperature = temperature;
        this.pressure = pressure;
    }

    public Gas getGas() {
        return gas.value();
    }

    public double getAmount() {
        return isEmpty() ? 0 : amount;
    }

    public double getTemperature() {
        return isEmpty() ? Gas.ROOM_TEMPERATURE : temperature;
    }

    public int getPressure() {
        return isEmpty() ? Gas.PRESSURE_AT_SEA_LEVEL : pressure;
    }

    public GasStack copy() {
        return new GasStack(gas.value(), amount, temperature, pressure);
    }

    public void setAmount(double amount) {
        if (isEmpty()) {
            throw new UnsupportedOperationException("An empty Gas Stack cannot be modified");
        }
        this.amount = amount;
    }

    public void shrink(double amount) {
        if (isEmpty()) {
            throw new UnsupportedOperationException("An empty Gas Stack cannot be modified");
        }
        this.amount -= Math.min(Math.abs(amount), this.amount);
        if (this.amount == 0) {
            gas = ElectrodynamicsGases.EMPTY;
            this.amount = 0;
            isEmpty = true;
        }
    }

    public void grow(double amount) {
        if (isEmpty()) {
            throw new UnsupportedOperationException("An empty Gas Stack cannot be modified");
        }
        this.amount += Math.abs(amount);
    }

    /**
     * A negative temperature is analogous to cooling
     * <p>
     * Temperatures cannot drop below 1 degree Kelvin
     *
     * @param deltaTemp The change in temperature
     */
    public void heat(double deltaTemp) {
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

    public double getVolumeChangeFromHeating(double deltaTemp) {
        if (isEmpty()) {
            throw new UnsupportedOperationException("An empty Gas Stack cannot be modified");
        }
        if (isAbsoluteZero() && deltaTemp < 0 || temperature + deltaTemp < ABSOLUTE_ZERO) {
            throw new UnsupportedOperationException("The temperature cannot drop below absolute zero");
        }

        double change = (deltaTemp + temperature) / temperature;

        return amount * change;

    }

    public double getVolumeChangeFromPressurizing(int atm) {
        if (isEmpty()) {
            throw new UnsupportedOperationException("An empty Gas Stack cannot be modified");
        }

        if (isVacuum() || atm < VACUUM) {
            throw new UnsupportedOperationException("You cannot have a pressure less than " + VACUUM);
        }

        double change = (double) atm / (double) pressure;

        return amount / change;
    }

    public boolean isEmpty() {
        return this.getGas().isEmpty() || isEmpty;
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
        return temperature <= gas.value().getCondensationTemp();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof GasStack other) {
            return other.getGas().equals(getGas()) && other.amount == amount && other.temperature == temperature && other.pressure == pressure;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(gas, amount, temperature, pressure, isEmpty);
    }

    @Override
    public String toString() {
        return gas.toString() + ", amount: " + amount + " mB, temp: " + temperature + " K, pressure: " + pressure + " ATM";
    }

    public CompoundTag writeToNbt() {
        CompoundTag tag = new CompoundTag();
        tag.putString("name", ElectrodynamicsGases.GAS_REGISTRY.getKey(getGas()).toString());
        tag.putDouble("amount", amount);
        tag.putDouble("temperature", temperature);
        tag.putInt("pressure", pressure);
        return tag;
    }

    public static GasStack readFromNbt(CompoundTag tag) {
        Gas gas = ElectrodynamicsGases.GAS_REGISTRY.get(ResourceLocation.parse(tag.getString("name")));
        double amount = tag.getDouble("amount");
        double temperature = tag.getDouble("temperature");
        int pressure = tag.getInt("pressure");
        return new GasStack(gas, amount, temperature, pressure);
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

        double medianTemperature = (stack1.temperature + stack2.temperature) / 2.0;

        double deltaT1 = medianTemperature - stack1.temperature;
        double deltaT2 = medianTemperature - stack2.temperature;

        stack1.bringPressureTo(newPressure);
        stack2.bringPressureTo(newPressure);

        stack1.heat(deltaT1);
        stack2.heat(deltaT2);

        GasStack stack = new GasStack(stack1.getGas());

        stack.setAmount(stack1.getAmount() + stack2.getAmount());
        stack.temperature = medianTemperature;
        stack.pressure = stack1.getPressure();

        return stack;

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
    public static double getMaximumAcceptance(GasStack stack1, GasStack stack2, double maximumAccept) {

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

        return remaining / deltaT2Factor * deltaP2Factor;

    }

}
