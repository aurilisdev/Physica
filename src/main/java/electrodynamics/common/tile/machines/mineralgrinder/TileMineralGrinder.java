package electrodynamics.common.tile.machines.mineralgrinder;

import electrodynamics.api.particle.ParticleAPI;
import electrodynamics.common.block.subtype.SubtypeMachine;
import electrodynamics.common.inventory.container.tile.ContainerO2OProcessor;
import electrodynamics.common.inventory.container.tile.ContainerO2OProcessorDouble;
import electrodynamics.common.inventory.container.tile.ContainerO2OProcessorTriple;
import electrodynamics.common.recipe.ElectrodynamicsRecipeInit;
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
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class TileMineralGrinder extends GenericTile implements ITickableSound {
	public long clientRunningTicks = 0;

	private boolean isSoundPlaying = false;

	public TileMineralGrinder(BlockPos pos, BlockState state) {
		this(SubtypeMachine.mineralgrinder, 0, pos, state);
	}

	public TileMineralGrinder(SubtypeMachine machine, int extra, BlockPos pos, BlockState state) {
		super(extra == 1 ? ElectrodynamicsTiles.TILE_MINERALGRINDERDOUBLE.get() : extra == 2 ? ElectrodynamicsTiles.TILE_MINERALGRINDERTRIPLE.get() : ElectrodynamicsTiles.TILE_MINERALGRINDER.get(), pos, state);

		int processorCount = extra + 1;
		int inputsPerProc = 1;
		int outputPerProc = 1;
		int biprodsPerProc = 1;

		addComponent(new ComponentPacketHandler(this));
		addComponent(new ComponentTickable(this).tickClient(this::tickClient));
		addComponent(new ComponentElectrodynamic(this, false, true).setInputDirections(BlockEntityUtils.MachineDirection.BACK).voltage(ElectrodynamicsCapabilities.DEFAULT_VOLTAGE * Math.pow(2, extra)));
		addComponent(new ComponentInventory(this, InventoryBuilder.newInv().processors(processorCount, inputsPerProc, outputPerProc, biprodsPerProc).upgrades(3)).validUpgrades(ContainerO2OProcessor.VALID_UPGRADES).valid(machineValidator()).implementMachineInputsAndOutputs());
		addComponent(new ComponentContainerProvider(machine, this).createMenu((id, player) -> (extra == 0 ? new ContainerO2OProcessor(id, player, getComponent(IComponentType.Inventory), getCoordsArray()) : extra == 1 ? new ContainerO2OProcessorDouble(id, player, getComponent(IComponentType.Inventory), getCoordsArray()) : extra == 2 ? new ContainerO2OProcessorTriple(id, player, getComponent(IComponentType.Inventory), getCoordsArray()) : null)));

		for (int i = 0; i <= extra; i++) {
			addProcessor(new ComponentProcessor(this, i, extra + 1).canProcess(component -> component.canProcessItem2ItemRecipe(component, ElectrodynamicsRecipeInit.MINERAL_GRINDER_TYPE.get())).process(component -> component.processItem2ItemRecipe(component)));
		}
	}

	protected void tickClient(ComponentTickable tickable) {
		if (!isProcessorActive()) {
			return;
		}

		if (level.random.nextDouble() < 0.15) {
			level.addParticle(ParticleTypes.SMOKE, worldPosition.getX() + level.random.nextDouble(), worldPosition.getY() + level.random.nextDouble() * 0.2 + 0.8, worldPosition.getZ() + level.random.nextDouble(), 0.0D, 0.0D, 0.0D);
		}
		int amount = getType() == ElectrodynamicsTiles.TILE_MINERALGRINDERDOUBLE.get() ? 2 : getType() == ElectrodynamicsTiles.TILE_MINERALGRINDERTRIPLE.get() ? 3 : 1;
		for (int i = 0; i < amount; i++) {
			ComponentInventory inv = getComponent(IComponentType.Inventory);
			ItemStack stack = inv.getInputsForProcessor(getProcessor(i).getProcessorNumber()).get(0);
			if (stack.getItem() instanceof BlockItem it) {
				Block block = it.getBlock();
				double d4 = level.random.nextDouble() * 12.0 / 16.0 + 0.5 - 6.0 / 16.0;
				double d6 = level.random.nextDouble() * 12.0 / 16.0 + 0.5 - 6.0 / 16.0;
				ParticleAPI.addGrindedParticle(level, worldPosition.getX() + d4, worldPosition.getY() + 0.8, worldPosition.getZ() + d6, 0.0D, 5D, 0.0D, block.defaultBlockState(), worldPosition);
			}
		}
		clientRunningTicks++;

		if (!isSoundPlaying) {
			isSoundPlaying = true;
			SoundBarrierMethods.playTileSound(ElectrodynamicsSounds.SOUND_MINERALGRINDER.get(), this, true);
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
