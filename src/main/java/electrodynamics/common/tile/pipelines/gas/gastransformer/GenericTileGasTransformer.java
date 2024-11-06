package electrodynamics.common.tile.pipelines.gas.gastransformer;

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

	public static final double BASE_INPUT_CAPACITY = 5000;
	public static final int INPUT_PRESSURE = 1048576;// 2^20
	public static final double INPUT_TEMPERATURE = 1000000;

	public static final double BASE_OUTPUT_CAPACITY = 5000;
	public static final int OUTPUT_PRESSURE = 1048576;// 2^20
	public static final double OUTPUT_TEMPERATURE = 1000000;

	public static final double MAX_JOULES = 50000;
	public static final double USAGE_PER_TICK = 100;

	public static final double BASE_CONVERSION_RATE = 20;

	public GenericTileGasTransformer(BlockEntityType<?> tileEntityTypeIn, BlockPos worldPos, BlockState blockState) {
		super(tileEntityTypeIn, worldPos, blockState);
		addComponent(new ComponentPacketHandler(this));
		addComponent(new ComponentTickable(this).tickClient(this::tickClient));
		addComponent(getInventory());
		addComponent(new ComponentProcessor(this).canProcess(this::canProcess).process(this::process).usage(USAGE_PER_TICK));
		addComponent(new ComponentGasHandlerMulti(this).setTanks(1, arr(BASE_INPUT_CAPACITY), arr(INPUT_TEMPERATURE), arr(INPUT_PRESSURE), 1, arr(BASE_OUTPUT_CAPACITY), arr(OUTPUT_TEMPERATURE), arr(OUTPUT_PRESSURE)).setInputDirections(BlockEntityUtils.MachineDirection.RIGHT)
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

}
