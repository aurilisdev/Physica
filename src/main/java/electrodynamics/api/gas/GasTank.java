package electrodynamics.api.gas;

import java.util.function.Predicate;

import javax.annotation.Nonnull;

import electrodynamics.api.capability.types.gas.IGasHandler;
import electrodynamics.api.gas.utils.IGasTank;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;

public class GasTank implements IGasTank, IGasHandler {

	protected Predicate<GasStack> isGasValid;
	private int capacity;
	private int maxTemperature;
	private int maxPressure;
	@Nonnull
	private GasStack gas = GasStack.EMPTY;

	public GasTank(int capacity, int maxTemperature, int maxPressure) {
		this(capacity, maxTemperature, maxPressure, gas -> true);
	}

	public GasTank(int capacity, int maxTemperature, int maxPressure, Predicate<GasStack> isGasValid) {
		this.capacity = capacity;
		this.maxTemperature = maxTemperature;
		this.maxPressure = maxPressure;
		this.isGasValid = isGasValid;
	}

	public GasTank setValidator(Predicate<GasStack> predicate) {
		isGasValid = predicate;
		return this;
	}

	public void setGas(GasStack gas) {
		this.gas = gas;
	}

	@Override
	public GasStack getGas() {
		return gas;
	}

	@Override
	public int getGasAmount() {
		return getGas().getAmount();
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	@Override
	public int getCapacity() {
		return capacity;
	}

	public void setMaxTemperature(int temperature) {
		maxTemperature = temperature;
	}

	@Override
	public int getMaxTemperature() {
		return maxTemperature;
	}

	public void setMaxPressure(int pressure) {
		maxPressure = pressure;
	}

	@Override
	public int getMaxPressure() {
		return maxPressure;
	}

	@Override
	public boolean isGasValid(GasStack other) {
		return isGasValid.test(other);
	}

	@Override
	public int fill(GasStack resource, GasAction action) {

		if (resource.isEmpty()) {
			return 0;
		}

		if (!isGasValid(resource)) {
			return 0;
		}

		if (isEmpty()) {

			int accepted = resource.getAmount() > getCapacity() ? getCapacity() : resource.getAmount();

			if (action == GasAction.EXECUTE) {

				GasStack taken = resource.copy();

				taken.setAmount(accepted);

				setGas(taken);

				onChange();

				if (getGas().getTemperature() > getMaxTemperature()) {

					onOverheat();

				}

				if (getGas().getPressure() > getMaxPressure()) {

					onOverpressure();

				}

				if (getGas().isCondensed()) {

					onGasCondensed();

				}

			}

			return accepted;

		}
		if (!getGas().isSameGas(resource)) {
			return 0;
		}

		int canTake = GasStack.getMaximumAcceptance(getGas(), resource, getCapacity());

		if (canTake == 0) {
			return 0;
		}

		if (action == GasAction.EXECUTE) {

			GasStack accepted = resource.copy();

			accepted.setAmount(canTake);

			GasStack equalized = GasStack.equalizePresrsureAndTemperature(getGas().copy(), accepted);

			setGas(equalized);

			onChange();

			if (getGas().getTemperature() > getMaxTemperature()) {

				onOverheat();

			}

			if (getGas().getPressure() > getMaxPressure()) {

				onOverpressure();

			}

			if (getGas().isCondensed()) {

				onGasCondensed();

			}

		}

		return canTake;

	}

	@Override
	public GasStack drain(int amount, GasAction action) {

		if (isEmpty() || amount == 0) {
			return GasStack.EMPTY;
		}

		int taken = Math.min(getGas().getAmount(), amount);

		GasStack takenStack = new GasStack(getGas().getGas(), taken, getGas().getTemperature(), getGas().getPressure());

		if (action == GasAction.EXECUTE) {

			GasStack newStack = getGas().copy();

			newStack.shrink(taken);

			if (newStack.getAmount() == 0) {

				setGas(GasStack.EMPTY);

			} else {

				setGas(newStack);

			}

			onChange();
		}

		return takenStack;
	}

	@Override
	public GasStack drain(GasStack resource, GasAction action) {

		if (resource.isEmpty() || !getGas().isSameGas(resource) || !getGas().isSamePressure(resource) || !getGas().isSameTemperature(resource)) {
			return GasStack.EMPTY;
		}

		return drain(resource.getAmount(), action);
	}

	@Override
	public int heat(int tank, int deltaTemperature, GasAction action) {

		if (getGas().isAbsoluteZero() && deltaTemperature < 0) {
			return -1;
		}

		GasStack updated = getGas().copy();

		updated.heat(deltaTemperature);

		if (updated.getAmount() > getCapacity()) {
			return -1;
		}

		if (action == GasAction.EXECUTE) {

			setGas(updated);

			onChange();

			if (getGas().getTemperature() > getMaxTemperature()) {

				onOverheat();

			} else if (getGas().isCondensed()) {
				onGasCondensed();
			}

		}

		return getCapacity() - updated.getAmount();
	}

	@Override
	public int bringPressureTo(int tank, int atm, GasAction action) {

		if (getGas().isVacuum() && atm < GasStack.VACUUM) {
			return -1;
		}

		GasStack updated = getGas().copy();

		updated.bringPressureTo(atm);

		if (updated.getAmount() > getCapacity()) {

			return -1;

		}

		if (action == GasAction.EXECUTE) {

			setGas(updated);

			onChange();

			if (getGas().getPressure() > getMaxPressure()) {

				onOverpressure();

			}

		}

		return getCapacity() - updated.getAmount();

	}

	public int getSpace() {
		return Math.max(getCapacity() - getGasAmount(), 0);
	}

	public void onChange() {

	}

	public void onOverheat() {

	}

	public void onOverpressure() {

	}

	public void onGasCondensed() {

	}

	public boolean isEmpty() {
		return getGas().isEmpty();
	}

	public CompoundTag writeToNbt() {
		CompoundTag tag = new CompoundTag();
		tag.put("gasstack", getGas().writeToNbt());
		tag.putInt("capacity", getCapacity());
		tag.putInt("maxtemp", getMaxTemperature());
		tag.putInt("maxpres", getMaxPressure());
		return tag;
	}

	public static GasTank readFromNbt(CompoundTag tag) {

		GasStack stack = GasStack.readFromNbt(tag.getCompound("gasstack"));

		GasTank tank = new GasTank(tag.getInt("capacity"), tag.getInt("maxtemp"), tag.getInt("maxpres"));

		tank.setGas(stack);

		return tank;

	}

	public void writeToBuffer(RegistryFriendlyByteBuf buffer) {

		GasStack.STREAM_CODEC.encode(buffer, getGas());

		buffer.writeDouble(getCapacity());

		buffer.writeDouble(getMaxTemperature());

		buffer.writeInt(getMaxPressure());

	}

	public static GasTank readFromBuffer(RegistryFriendlyByteBuf buffer) {

		GasStack stack = GasStack.STREAM_CODEC.decode(buffer);

		GasTank tank = new GasTank(buffer.readInt(), buffer.readInt(), buffer.readInt());

		tank.setGas(stack);

		return tank;

	}

	@Override
	public String toString() {
		return "Gas: " + getGas().toString() + ", Capacity: " + getCapacity() + " mB, Max Temp: " + getMaxTemperature() + " K, Max Pressure: " + getMaxPressure() + " ATM";
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof GasTank other) {

			return getGas().equals(obj) && getCapacity() == other.getCapacity() && getMaxTemperature() == other.getMaxTemperature() && getMaxPressure() == other.getMaxPressure();

		}
		return false;
	}

	@Override
	public int getTanks() {
		return 1;
	}

	@Override
	public GasStack getGasInTank(int tank) {
		return getGas();
	}

	@Override
	public int getTankCapacity(int tank) {
		return getCapacity();
	}

	@Override
	public int getTankMaxTemperature(int tank) {
		return getMaxTemperature();
	}

	@Override
	public int getTankMaxPressure(int tank) {
		return getMaxPressure();
	}

	@Override
	public boolean isGasValid(int tank, GasStack gas) {
		return isGasValid(gas);
	}
	
	public int getRoom() {
		return getCapacity() - getGasAmount();
	}

}
