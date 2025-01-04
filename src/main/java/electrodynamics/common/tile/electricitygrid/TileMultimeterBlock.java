package electrodynamics.common.tile.electricitygrid;

import electrodynamics.api.capability.types.electrodynamic.ICapabilityElectrodynamic.LoadProfile;
import electrodynamics.api.network.cable.type.IConductor;
import electrodynamics.common.network.type.ElectricNetwork;
import electrodynamics.prefab.properties.Property;
import electrodynamics.prefab.properties.PropertyTypes;
import electrodynamics.prefab.tile.GenericTile;
import electrodynamics.prefab.tile.components.type.ComponentElectrodynamic;
import electrodynamics.prefab.tile.components.type.ComponentPacketHandler;
import electrodynamics.prefab.tile.components.type.ComponentTickable;
import electrodynamics.prefab.utilities.BlockEntityUtils;
import electrodynamics.prefab.utilities.object.CachedTileOutput;
import electrodynamics.prefab.utilities.object.TransferPack;
import electrodynamics.registers.ElectrodynamicsTiles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public class TileMultimeterBlock extends GenericTile {

	//TODO Flip it so it places facing towards the player

	public Property<Double> voltage = property(new Property<>(PropertyTypes.DOUBLE, "voltageNew", 0.0).setNoSave());
	public Property<Double> minVoltage = property(new Property<>(PropertyTypes.DOUBLE, "minvoltage", 0.0).setNoSave());
	public Property<Double> joules = property(new Property<>(PropertyTypes.DOUBLE, "joulesNew", 0.0).setNoSave());
	public Property<Double> resistance = property(new Property<>(PropertyTypes.DOUBLE, "resistanceNew", 0.0).setNoSave());
	public Property<Double> loss = property(new Property<>(PropertyTypes.DOUBLE, "lossNew", 0.0).setNoSave());

	public CachedTileOutput input;

	public TileMultimeterBlock(BlockPos worldPosition, BlockState blockState) {
		super(ElectrodynamicsTiles.TILE_MULTIMETERBLOCK.get(), worldPosition, blockState);
		addComponent(new ComponentTickable(this).tickServer(this::tickServer));
		addComponent(new ComponentPacketHandler(this));
		addComponent(new ComponentElectrodynamic(this, false, false).receivePower(this::receivePower).getConnectedLoad(this::getConnectedLoad).setInputDirections(BlockEntityUtils.MachineDirection.FRONT).voltage(-1));
	}

	public void tickServer(ComponentTickable tickable) {

		if (tickable.getTicks() % (minVoltage.get() == 0 ? 20 : 2) == 0) {
			Direction facing = getFacing();
			if (input == null) {
				input = new CachedTileOutput(level, worldPosition.relative(facing));
			}
			if (input.getSafe() instanceof IConductor) {
				IConductor cond = input.getSafe();
				if (cond.getAbstractNetwork() instanceof ElectricNetwork net) {
					joules.set(net.getActiveTransmitted());
					voltage.set(net.getActiveVoltage());
					minVoltage.set(net.getMinimumVoltage());
					resistance.set(net.getResistance());
					loss.set(net.getLastEnergyLoss());
				}
			} else {
				joules.set(0.0);
				voltage.set(0.0);
				minVoltage.set(0.0);
				resistance.set(0.0);
				loss.set(0.0);
			}
		}
	}

	protected TransferPack receivePower(TransferPack transfer, boolean debug) {
		return TransferPack.EMPTY;
	}

	protected TransferPack getConnectedLoad(LoadProfile loadProfile, Direction dir) {
		return TransferPack.EMPTY;
	}
}
