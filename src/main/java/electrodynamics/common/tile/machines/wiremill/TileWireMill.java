package electrodynamics.common.tile.machines.wiremill;

import electrodynamics.common.block.subtype.SubtypeMachine;
import electrodynamics.common.inventory.container.tile.ContainerO2OProcessor;
import electrodynamics.common.inventory.container.tile.ContainerO2OProcessorDouble;
import electrodynamics.common.inventory.container.tile.ContainerO2OProcessorTriple;
import electrodynamics.common.recipe.ElectrodynamicsRecipeInit;
import electrodynamics.common.settings.Constants;
import electrodynamics.prefab.sound.SoundBarrierMethods;
import electrodynamics.prefab.sound.utils.ITickableSound;
import electrodynamics.prefab.tile.GenericTile;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.tile.components.type.ComponentContainerProvider;
import electrodynamics.prefab.tile.components.type.ComponentElectrodynamic;
import electrodynamics.prefab.tile.components.type.ComponentInventory;
import electrodynamics.prefab.tile.components.type.ComponentInventory.InventoryBuilder;
import electrodynamics.prefab.tile.components.type.ComponentPacketHandler;
import electrodynamics.prefab.tile.components.type.ComponentProcessor;
import electrodynamics.prefab.tile.components.type.ComponentTickable;
import electrodynamics.prefab.utilities.BlockEntityUtils;
import electrodynamics.registers.ElectrodynamicsCapabilities;
import electrodynamics.registers.ElectrodynamicsSounds;
import electrodynamics.registers.ElectrodynamicsTiles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.block.state.BlockState;

public class TileWireMill extends GenericTile implements ITickableSound {

	private boolean isSoundPlaying = false;

	public TileWireMill(BlockPos worldPosition, BlockState blockState) {
		this(SubtypeMachine.wiremill, 0, worldPosition, blockState);
	}

	public TileWireMill(SubtypeMachine machine, int extra, BlockPos worldPosition, BlockState blockState) {
		super(extra == 1 ? ElectrodynamicsTiles.TILE_WIREMILLDOUBLE.get() : extra == 2 ? ElectrodynamicsTiles.TILE_WIREMILLTRIPLE.get() : ElectrodynamicsTiles.TILE_WIREMILL.get(), worldPosition, blockState);

		int processorCount = extra + 1;
		int inputsPerProc = 1;
		int outputPerProc = 1;
		int biprodsPerProc = 1;

		addComponent(new ComponentPacketHandler(this));
		addComponent(new ComponentTickable(this).tickClient(this::tickClient));
		addComponent(new ComponentElectrodynamic(this, false, true).setInputDirections(BlockEntityUtils.MachineDirection.BACK).voltage(ElectrodynamicsCapabilities.DEFAULT_VOLTAGE * Math.pow(2, extra)).joules(Constants.WIREMILL_USAGE_PER_TICK * 20 * (extra + 1)));
		addComponent(new ComponentInventory(this, InventoryBuilder.newInv().processors(processorCount, inputsPerProc, outputPerProc, biprodsPerProc).upgrades(3)).validUpgrades(ContainerO2OProcessor.VALID_UPGRADES).valid(machineValidator()).implementMachineInputsAndOutputs());
		addComponent(new ComponentContainerProvider(machine, this).createMenu((id, player) -> (extra == 0 ? new ContainerO2OProcessor(id, player, getComponent(IComponentType.Inventory), getCoordsArray()) : extra == 1 ? new ContainerO2OProcessorDouble(id, player, getComponent(IComponentType.Inventory), getCoordsArray()) : extra == 2 ? new ContainerO2OProcessorTriple(id, player, getComponent(IComponentType.Inventory), getCoordsArray()) : null)));

		for (int i = 0; i <= extra; i++) {
			addProcessor(new ComponentProcessor(this, i, extra + 1).canProcess(component -> component.canProcessItem2ItemRecipe(component, ElectrodynamicsRecipeInit.WIRE_MILL_TYPE.get())).process(component -> component.processItem2ItemRecipe(component)).requiredTicks(Constants.WIREMILL_REQUIRED_TICKS).usage(Constants.WIREMILL_USAGE_PER_TICK));
		}
	}

	protected void tickClient(ComponentTickable tickable) {
		if (!isProcessorActive()) {
			return;
		}

		if (level.random.nextDouble() < 0.15) {
			level.addParticle(ParticleTypes.SMOKE, worldPosition.getX() + level.random.nextDouble(), worldPosition.getY() + level.random.nextDouble() * 0.5 + 0.5, worldPosition.getZ() + level.random.nextDouble(), 0.0D, 0.0D, 0.0D);
		}

		if (!isSoundPlaying) {
			isSoundPlaying = true;
			SoundBarrierMethods.playTileSound(ElectrodynamicsSounds.SOUND_HUM.get(), this, true);
		}
	}

	@Override
	public void setNotPlaying() {
		isSoundPlaying = false;
	}

	@Override
	public boolean shouldPlaySound() {
		return isProcessorActive();
	}

	@Override
	public int getComparatorSignal() {
		return (int) (((double) getNumActiveProcessors() / (double) Math.max(1, getNumProcessors())) * 15.0);
	}

}
