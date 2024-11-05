package electrodynamics.common.tile.electricitygrid;

import electrodynamics.api.capability.types.electrodynamic.ICapabilityElectrodynamic.LoadProfile;
import electrodynamics.common.block.subtype.SubtypeMachine;
import electrodynamics.common.inventory.container.tile.ContainerPotentiometer;
import electrodynamics.prefab.properties.Property;
import electrodynamics.prefab.properties.PropertyTypes;
import electrodynamics.prefab.tile.GenericTile;
import electrodynamics.prefab.tile.components.type.ComponentContainerProvider;
import electrodynamics.prefab.tile.components.type.ComponentElectrodynamic;
import electrodynamics.prefab.utilities.BlockEntityUtils;
import electrodynamics.prefab.utilities.object.TransferPack;
import electrodynamics.registers.ElectrodynamicsTiles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public class TilePotentiometer extends GenericTile {

	public final Property<Double> powerConsumption = property(new Property<>(PropertyTypes.DOUBLE, "consumption", -1.0));

	public TilePotentiometer(BlockPos pos, BlockState state) {
		super(ElectrodynamicsTiles.TILE_POTENTIOMETER.get(), pos, state);
		addComponent(new ComponentContainerProvider(SubtypeMachine.potentiometer, this).createMenu((id, player) -> new ContainerPotentiometer(id, player, getCoordsArray())));
		addComponent(new ComponentElectrodynamic(this, false, true).receivePower(this::receivePower).getConnectedLoad(this::getConnectedLoad)
				//
				.setInputDirections(BlockEntityUtils.MachineDirection.FRONT, BlockEntityUtils.MachineDirection.BACK, BlockEntityUtils.MachineDirection.LEFT, BlockEntityUtils.MachineDirection.RIGHT, BlockEntityUtils.MachineDirection.BOTTOM).voltage(-1.0D));
	}

	private TransferPack receivePower(TransferPack pack, boolean debug) {
		if (powerConsumption.get() < 0) {
			return pack;
		}
		double accepted = Math.min(pack.getJoules(), powerConsumption.get());
		return TransferPack.joulesVoltage(accepted, pack.getVoltage());
	}

	private TransferPack getConnectedLoad(LoadProfile loadProfile, Direction dir) {
		if (dir == Direction.DOWN) {
			return TransferPack.EMPTY;
		}
		return TransferPack.joulesVoltage(powerConsumption.get() < 0 ? Double.MAX_VALUE : powerConsumption.get(), -1);
	}

}
