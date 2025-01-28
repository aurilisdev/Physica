package electrodynamics.common.tile.electricitygrid.generators;

import electrodynamics.api.electricity.generator.IElectricGenerator;
import electrodynamics.common.block.subtype.SubtypeMachine;
import electrodynamics.common.inventory.container.tile.ContainerCombustionChamber;
import electrodynamics.common.network.utils.FluidUtilities;
import electrodynamics.common.reloadlistener.CombustionFuelRegister;
import electrodynamics.common.settings.Constants;
import electrodynamics.prefab.properties.Property;
import electrodynamics.prefab.properties.PropertyTypes;
import electrodynamics.prefab.sound.SoundBarrierMethods;
import electrodynamics.prefab.sound.utils.ITickableSound;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.tile.components.type.ComponentContainerProvider;
import electrodynamics.prefab.tile.components.type.ComponentElectrodynamic;
import electrodynamics.prefab.tile.components.type.ComponentFluidHandlerMulti;
import electrodynamics.prefab.tile.components.type.ComponentInventory;
import electrodynamics.prefab.tile.components.type.ComponentInventory.InventoryBuilder;
import electrodynamics.prefab.tile.components.type.ComponentPacketHandler;
import electrodynamics.prefab.tile.components.type.ComponentTickable;
import electrodynamics.prefab.tile.types.GenericMaterialTile;
import electrodynamics.prefab.utilities.BlockEntityUtils;
import electrodynamics.prefab.utilities.ElectricityUtils;
import electrodynamics.prefab.utilities.object.CachedTileOutput;
import electrodynamics.prefab.utilities.object.CombustionFuelSource;
import electrodynamics.prefab.utilities.object.TransferPack;
import electrodynamics.registers.ElectrodynamicsSounds;
import electrodynamics.registers.ElectrodynamicsTiles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

public class TileCombustionChamber extends GenericMaterialTile implements IElectricGenerator, ITickableSound {

	public static final int TICKS_PER_MILLIBUCKET = 200;
	public static final int TANK_CAPACITY = 1000;
	public Property<Boolean> running = property(new Property<>(PropertyTypes.BOOLEAN, "running", false));
	public Property<Integer> burnTime = property(new Property<>(PropertyTypes.INTEGER, "burnTime", 0));
	private double fuelMultiplier = 1;
	private CachedTileOutput output;
	// for future upgrades
	private Property<Double> multiplier = property(new Property<>(PropertyTypes.DOUBLE, "multiplier", 1.0));
	private Property<Boolean> hasRedstoneSignal = property(new Property<>(PropertyTypes.BOOLEAN, "redstonesignal", false));

	private boolean isSoundPlaying = false;

	public TileCombustionChamber(BlockPos worldPosition, BlockState blockState) {
		super(ElectrodynamicsTiles.TILE_COMBUSTIONCHAMBER.get(), worldPosition, blockState);
		addComponent(new ComponentTickable(this).tickServer(this::tickServer).tickClient(this::tickClient));
		addComponent(new ComponentPacketHandler(this));
		addComponent(new ComponentElectrodynamic(this, true, false).setOutputDirections(BlockEntityUtils.MachineDirection.RIGHT));
		addComponent(new ComponentInventory(this, InventoryBuilder.newInv().bucketInputs(1)).valid((slot, stack, i) -> stack.getCapability(Capabilities.FluidHandler.ITEM) != null));
		addComponent(new ComponentFluidHandlerMulti(this).setInputTanks(1, TANK_CAPACITY).setInputDirections(BlockEntityUtils.MachineDirection.LEFT).setInputFluidTags(CombustionFuelRegister.INSTANCE.getFluidTags()));
		addComponent(new ComponentContainerProvider(SubtypeMachine.combustionchamber, this).createMenu((id, player) -> new ContainerCombustionChamber(id, player, getComponent(IComponentType.Inventory), getCoordsArray())));
	}

	protected void tickServer(ComponentTickable tickable) {
		if (hasRedstoneSignal.get()) {
			running.set(false);
			return;
		}
		Direction facing = getFacing();
		if (output == null) {
			output = new CachedTileOutput(level, worldPosition.relative(facing.getClockWise()));
		}
		if (tickable.getTicks() % 40 == 0) {
			output.update(worldPosition.relative(facing.getClockWise()));
		}
		ComponentFluidHandlerMulti handler = getComponent(IComponentType.FluidHandler);
		FluidUtilities.drainItem(this, handler.getInputTanks());
		FluidTank tank = handler.getInputTanks()[0];
		if (burnTime.get() <= 0) {
			running.set(false);
			if (tank.getFluidAmount() > 0) {
				CombustionFuelSource source = CombustionFuelRegister.INSTANCE.getFuelFromFluid(tank.getFluid());
				if (!source.isEmpty()) {
					tank.drain(new FluidStack(tank.getFluid().getFluid(), source.getFuelUsage()), FluidAction.EXECUTE);
					fuelMultiplier = source.getPowerMultiplier();
					running.set(true);
					burnTime.set(TICKS_PER_MILLIBUCKET);
				}

			}
		} else {
			running.set(true);
		}
		if (burnTime.get() > 0) {
			burnTime.set(burnTime.get() - 1);
		}
		if (running.get() && burnTime.get() > 0 && output.valid()) {
			ElectricityUtils.receivePower(output.getSafe(), facing.getClockWise().getOpposite(), getProduced(), false);
		}
	}

	protected void tickClient(ComponentTickable tickable) {
		if (!running.get()) {
			return;
		}

		if (level.random.nextDouble() < 0.15) {
			level.addParticle(ParticleTypes.SMOKE, worldPosition.getX() + level.random.nextDouble(), worldPosition.getY() + level.random.nextDouble(), worldPosition.getZ() + level.random.nextDouble(), 0.0D, 0.0D, 0.0D);
		}

		if (!isSoundPlaying) {
			isSoundPlaying = true;
			SoundBarrierMethods.playTileSound(ElectrodynamicsSounds.SOUND_COMBUSTIONCHAMBER.get(), this, true);
		}
	}

	@Override
	public void setNotPlaying() {
		isSoundPlaying = false;
	}

	@Override
	public boolean shouldPlaySound() {
		return running.get();
	}

	@Override
	public void setMultiplier(double val) {
		multiplier.set(val);
	}

	@Override
	public double getMultiplier() {
		return multiplier.get();
	}

	@Override
	public TransferPack getProduced() {
		ComponentElectrodynamic electro = getComponent(IComponentType.Electrodynamic);
		return TransferPack.joulesVoltage(Constants.COMBUSTIONCHAMBER_JOULES_PER_TICK * fuelMultiplier * multiplier.get(), electro.getVoltage());
	}

	@Override
	public int getComparatorSignal() {
		return running.get() ? 15 : 0;
	}

	@Override
	public void onNeightborChanged(BlockPos neighbor, boolean blockStateTrigger) {
		if (level.isClientSide) {
			return;
		}
		hasRedstoneSignal.set(level.hasNeighborSignal(getBlockPos()));
	}

}
