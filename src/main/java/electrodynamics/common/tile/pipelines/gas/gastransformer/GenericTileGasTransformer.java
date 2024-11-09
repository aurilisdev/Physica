package electrodynamics.common.tile.pipelines.gas.gastransformer;

import electrodynamics.common.settings.Constants;
import electrodynamics.prefab.sound.utils.ITickableSound;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.tile.components.type.ComponentContainerProvider;
import electrodynamics.prefab.tile.components.type.ComponentGasHandlerMulti;
import electrodynamics.prefab.tile.components.type.ComponentInventory;
import electrodynamics.prefab.tile.components.type.ComponentPacketHandler;
import electrodynamics.prefab.tile.components.type.ComponentProcessor;
import electrodynamics.prefab.tile.components.type.ComponentTickable;
import electrodynamics.prefab.tile.types.GenericGasTile;
import electrodynamics.prefab.utilities.BlockEntityUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class GenericTileGasTransformer extends GenericGasTile implements ITickableSound {

	protected boolean isSoundPlaying = false;

	public GenericTileGasTransformer(BlockEntityType<?> tileEntityTypeIn, BlockPos worldPos, BlockState blockState) {
		super(tileEntityTypeIn, worldPos, blockState);
		addComponent(new ComponentPacketHandler(this));
		addComponent(new ComponentTickable(this).tickClient(this::tickClient));
		addComponent(getInventory());
		addComponent(new ComponentProcessor(this).canProcess(this::canProcess).process(this::process).usage(getUsagePerTick()));
		addComponent(new ComponentGasHandlerMulti(this).setTanks(1, arr(Constants.GAS_TRANSFORMER_BASE_INPUT_CAPCITY), arr(Constants.GAS_TRANSFORMER_INPUT_TEMP_CAP), arr(Constants.GAS_TRANSFORMER_INPUT_PRESSURE_CAP), 1, arr(Constants.GAS_TRANSFORMER_BASE_OUTPUT_CAPCITY), arr(Constants.GAS_TRANSFORMER_OUTPUT_TEMP_CAP), arr(Constants.GAS_TRANSFORMER_OUTPUT_PRESSURE_CAP)).setInputDirections(BlockEntityUtils.MachineDirection.RIGHT)
				.setOutputDirections(BlockEntityUtils.MachineDirection.LEFT).setCondensedHandler(getCondensedHandler()));
		addComponent(getContainerProvider());
	}

	public abstract boolean canProcess(ComponentProcessor processor);

	public abstract void process(ComponentProcessor processor);

	public abstract void tickClient(ComponentTickable tickable);

	@Override
	public void setNotPlaying() {
		isSoundPlaying = false;
	}

	@Override
	public boolean shouldPlaySound() {
		return this.<ComponentProcessor>getComponent(IComponentType.Processor).isActive();
	}

	public abstract ComponentContainerProvider getContainerProvider();

	public abstract ComponentInventory getInventory();

	public abstract double getUsagePerTick();

	public abstract int getConversionRate();

}
