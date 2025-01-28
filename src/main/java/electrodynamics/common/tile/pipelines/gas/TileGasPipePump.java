package electrodynamics.common.tile.pipelines.gas;

import org.jetbrains.annotations.Nullable;

import electrodynamics.api.capability.types.gas.IGasHandler;
import electrodynamics.common.inventory.container.tile.ContainerGasPipePump;
import electrodynamics.common.network.type.GasNetwork;
import electrodynamics.common.settings.Constants;
import electrodynamics.prefab.properties.Property;
import electrodynamics.prefab.properties.PropertyTypes;
import electrodynamics.prefab.tile.GenericTile;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.tile.components.type.ComponentContainerProvider;
import electrodynamics.prefab.tile.components.type.ComponentElectrodynamic;
import electrodynamics.prefab.tile.components.type.ComponentPacketHandler;
import electrodynamics.prefab.tile.components.type.ComponentTickable;
import electrodynamics.prefab.utilities.BlockEntityUtils;
import electrodynamics.prefab.utilities.CapabilityUtils;
import electrodynamics.registers.ElectrodynamicsCapabilities;
import electrodynamics.registers.ElectrodynamicsTiles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class TileGasPipePump extends GenericTile {

	public static final BlockEntityUtils.MachineDirection INPUT_DIR = BlockEntityUtils.MachineDirection.FRONT;
	public static final BlockEntityUtils.MachineDirection OUTPUT_DIR = BlockEntityUtils.MachineDirection.BACK;
	
	private boolean isLocked = false;

	public final Property<Integer> priority = property(new Property<>(PropertyTypes.INTEGER, "pumppriority", 0).onChange((prop, oldval) -> {

		if (level == null || level.isClientSide) {
			return;
		}

		BlockEntity entity = level.getBlockEntity(worldPosition.relative(getFacing()));

		if (entity instanceof TileGasPipe pipe) {
			GasNetwork network = pipe.getNetwork();

			if (network != null) {
				network.updateGasPipePumpStats(this, prop.get(), oldval);
			}
		}

	}));

	public TileGasPipePump(BlockPos pos, BlockState state) {
		super(ElectrodynamicsTiles.TILE_GASPIPEPUMP.get(), pos, state);
		addComponent(new ComponentTickable(this).tickServer(this::tickServer));
		addComponent(new ComponentPacketHandler(this));
		addComponent(new ComponentElectrodynamic(this, false, true).voltage(ElectrodynamicsCapabilities.DEFAULT_VOLTAGE).maxJoules(Constants.PIPE_PUMP_USAGE_PER_TICK * 10).setInputDirections(BlockEntityUtils.MachineDirection.LEFT));
		addComponent(new ComponentContainerProvider("container.gaspipepump", this).createMenu((id, inv) -> new ContainerGasPipePump(id, inv, getCoordsArray())));
	}

	public void tickServer(ComponentTickable tick) {

		ComponentElectrodynamic electro = getComponent(IComponentType.Electrodynamic);

		electro.joules(Math.max(electro.getJoulesStored() - Constants.PIPE_PUMP_USAGE_PER_TICK, 0));

	}
	
	@Override
	public @Nullable IGasHandler getGasHandlerCapability(@Nullable Direction side) {
	    if(side == null || isLocked) {
	        return null;
	    }
	    Direction facing = getFacing();

        if (side == BlockEntityUtils.getRelativeSide(facing, OUTPUT_DIR.mappedDir)) {
            return CapabilityUtils.EMPTY_GAS;
        }

        if (side == BlockEntityUtils.getRelativeSide(facing, INPUT_DIR.mappedDir)) {

            BlockEntity output = level.getBlockEntity(worldPosition.relative(side.getOpposite()));
            if (output == null) {
                return CapabilityUtils.EMPTY_GAS;
            }
            
            isLocked = true;
            
            IGasHandler gas = output.getLevel().getCapability(ElectrodynamicsCapabilities.CAPABILITY_GASHANDLER_BLOCK, output.getBlockPos(), output.getBlockState(), output, side);
            
            isLocked = false;
            
            return gas == null ? CapabilityUtils.EMPTY_GAS : gas;

        }
        
        return null;
	}

	public boolean isPowered() {
		return this.<ComponentElectrodynamic>getComponent(IComponentType.Electrodynamic).getJoulesStored() >= Constants.PIPE_PUMP_USAGE_PER_TICK;
	}

}
