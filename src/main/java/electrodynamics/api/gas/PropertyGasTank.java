package electrodynamics.api.gas;

import java.util.function.BiConsumer;
import java.util.function.Predicate;

import electrodynamics.prefab.properties.Property;
import electrodynamics.prefab.properties.PropertyTypes;
import electrodynamics.prefab.tile.GenericTile;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.Level.ExplosionInteraction;
import net.minecraft.world.level.block.Blocks;

/**
 * An extension of the GasTank class incorporating the Electrodynamics property system
 * 
 * @author skip999
 *
 */
public class PropertyGasTank extends GasTank {

	protected GenericTile holder;

	protected Property<GasStack> gasProperty;
	protected Property<Integer> capacityProperty;
	protected Property<Integer> maxTemperatureProperty;
	protected Property<Integer> maxPressureProperty;

	protected BiConsumer<GasTank, GenericTile> onGasCondensed = (gas, tile) -> {
	};

	public PropertyGasTank(GenericTile holder, String key, int capacity, int maxTemperature, int maxPressure) {
		super(capacity, maxTemperature, maxPressure);

		this.holder = holder;

		gasProperty = holder.property(new Property<>(PropertyTypes.GAS_STACK, "propertygastankstack" + key, GasStack.EMPTY));
		capacityProperty = holder.property(new Property<>(PropertyTypes.INTEGER, "propertygastankcapacity" + key, capacity));
		maxTemperatureProperty = holder.property(new Property<>(PropertyTypes.INTEGER, "propertygastankmaxtemperature" + key, maxTemperature));
		maxPressureProperty = holder.property(new Property<>(PropertyTypes.INTEGER, "propertygastankmaxpressure" + key, maxPressure));
	}

	public PropertyGasTank(GenericTile holder, String key, int capacity, int maxTemperature, int maxPressure, Predicate<GasStack> isGasValid) {
		super(capacity, maxTemperature, maxPressure, isGasValid);

		this.holder = holder;

		gasProperty = holder.property(new Property<>(PropertyTypes.GAS_STACK, "propertygastankstack" + key, GasStack.EMPTY));
		capacityProperty = holder.property(new Property<>(PropertyTypes.INTEGER, "propertygastankcapacity" + key, capacity));
		maxTemperatureProperty = holder.property(new Property<>(PropertyTypes.INTEGER, "propertygastankmaxtemperature" + key, maxTemperature));
		maxPressureProperty = holder.property(new Property<>(PropertyTypes.INTEGER, "propertygastankmaxpressure" + key, maxPressure));

	}

	protected PropertyGasTank(PropertyGasTank other) {
		super(other.getCapacity(), other.getMaxTemperature(), other.getMaxPressure(), other.isGasValid);

		this.holder = other.holder;

		gasProperty = other.gasProperty;
		capacityProperty = other.capacityProperty;
		maxTemperatureProperty = other.maxTemperatureProperty;
		maxPressureProperty = other.maxPressureProperty;

	}

	@Override
	public PropertyGasTank setValidator(Predicate<GasStack> predicate) {
		return (PropertyGasTank) super.setValidator(predicate);
	}

	public PropertyGasTank setOnGasCondensed(BiConsumer<GasTank, GenericTile> onGasCondensed) {
		this.onGasCondensed = onGasCondensed;
		return this;
	}

	@Override
	public void setGas(GasStack gas) {
		gasProperty.set(gas);
	}

	@Override
	public void setCapacity(int capacity) {
		capacityProperty.set(capacity);
	}

	@Override
	public void setMaxTemperature(int temperature) {
		maxTemperatureProperty.set(temperature);
	}

	@Override
	public void setMaxPressure(int atm) {
		maxPressureProperty.set(atm);
	}

	@Override
	public GasStack getGas() {
		return gasProperty.get();
	}

	@Override
	public int getGasAmount() {
		return getGas().getAmount();
	}

	@Override
	public int getCapacity() {
		return capacityProperty.get();
	}

	@Override
	public int getMaxTemperature() {
		return maxTemperatureProperty.get();
	}

	@Override
	public int getMaxPressure() {
		return maxPressureProperty.get();
	}

	@Override
	public void onChange() {
		if (holder != null) {
			gasProperty.forceDirty();
			holder.onGasTankChange(this);
		}
	}

	@Override
	public void onOverheat() {
		if (holder != null) {
			Level world = holder.getLevel();
			BlockPos pos = holder.getBlockPos();
			world.setBlockAndUpdate(pos, Blocks.LAVA.defaultBlockState());
		}
	}

	@Override
	public void onOverpressure() {
		if (holder != null) {
			Level world = holder.getLevel();
			BlockPos pos = holder.getBlockPos();
			world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
			world.explode(null, pos.getX(), pos.getY(), pos.getZ(), 2.0F, ExplosionInteraction.BLOCK);
		}
	}

	@Override
	public void onGasCondensed() {
		if (holder != null) {
			onGasCondensed.accept(this, holder);
		}
	}

	public PropertyGasTank[] asArray() {
		return new PropertyGasTank[] { this };
	}

}
