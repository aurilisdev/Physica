package electrodynamics.common.tile.machines.mineralcrusher;

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
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class TileMineralCrusher extends GenericTile implements ITickableSound {
	public long clientRunningTicks = 0;

	private boolean isSoundPlaying = false;

	public TileMineralCrusher(BlockPos pos, BlockState state) {
		this(SubtypeMachine.mineralcrusher, 0, pos, state);
	}

	public TileMineralCrusher(SubtypeMachine machine, int extra, BlockPos pos, BlockState state) {
		super(extra == 1 ? ElectrodynamicsTiles.TILE_MINERALCRUSHERDOUBLE.get() : extra == 2 ? ElectrodynamicsTiles.TILE_MINERALCRUSHERTRIPLE.get() : ElectrodynamicsTiles.TILE_MINERALCRUSHER.get(), pos, state);

		int processorCount = extra + 1;
		int inputsPerProc = 1;
		int outputPerProc = 1;
		int biprodsPerProc = 1;

		addComponent(new ComponentPacketHandler(this));
		addComponent(new ComponentTickable(this).tickClient(this::tickClient));
		addComponent(new ComponentElectrodynamic(this, false, true).setInputDirections(BlockEntityUtils.MachineDirection.BACK).voltage(ElectrodynamicsCapabilities.DEFAULT_VOLTAGE * 2 * Math.pow(2, extra)));
		addComponent(new ComponentInventory(this, InventoryBuilder.newInv().processors(processorCount, inputsPerProc, outputPerProc, biprodsPerProc).upgrades(3)).validUpgrades(ContainerO2OProcessor.VALID_UPGRADES).valid(machineValidator()).implementMachineInputsAndOutputs());
		addComponent(new ComponentContainerProvider(machine, this).createMenu((id, player) -> (extra == 0 ? new ContainerO2OProcessor(id, player, getComponent(IComponentType.Inventory), getCoordsArray()) : extra == 1 ? new ContainerO2OProcessorDouble(id, player, getComponent(IComponentType.Inventory), getCoordsArray()) : extra == 2 ? new ContainerO2OProcessorTriple(id, player, getComponent(IComponentType.Inventory), getCoordsArray()) : null)));

		for (int i = 0; i <= extra; i++) {
			addProcessor(new ComponentProcessor(this, i, extra + 1).canProcess(component -> component.canProcessItem2ItemRecipe(component, ElectrodynamicsRecipeInit.MINERAL_CRUSHER_TYPE.get())).process(component -> component.processItem2ItemRecipe(component)));
		}
	}

	protected void tickClient(ComponentTickable tickable) {
		if (!isProcessorActive()) {
			return;
		}

		Direction direction = getFacing();
		if (level.random.nextDouble() < 0.15) {
			double d4 = level.random.nextDouble();
			double d5 = direction.getAxis() == Direction.Axis.X ? direction.getStepX() * (direction.getStepX() == -1 ? 0 : 1) : d4;
			double d6 = level.random.nextDouble();
			double d7 = direction.getAxis() == Direction.Axis.Z ? direction.getStepZ() * (direction.getStepZ() == -1 ? 0 : 1) : d4;
			level.addParticle(ParticleTypes.SMOKE, worldPosition.getX() + d5, worldPosition.getY() + d6, worldPosition.getZ() + d7, 0.0D, 0.0D, 0.0D);
		}
		double progress = Math.sin(0.05 * Math.PI * (clientRunningTicks % 20));
		if (progress < 0.3) {
			for (int i = 0; i < 5; i++) {
				double d4 = level.random.nextDouble() * 4.0 / 16.0 + 0.5 - 2.0 / 16.0;
				double d6 = level.random.nextDouble() * 4.0 / 16.0 + 0.5 - 2.0 / 16.0;
				level.addParticle(ParticleTypes.SMOKE, worldPosition.getX() + d4 + direction.getStepX() * 0.2, worldPosition.getY() + 0.4, worldPosition.getZ() + d6 + direction.getStepZ() * 0.2, 0.0D, 0.0D, 0.0D);
			}
			int amount = getType() == ElectrodynamicsTiles.TILE_MINERALCRUSHERDOUBLE.get() ? 2 : getType() == ElectrodynamicsTiles.TILE_MINERALCRUSHERTRIPLE.get() ? 3 : 0;
			for (int in = 0; in < amount; in++) {
				ComponentInventory inv = getComponent(IComponentType.Inventory);
				ItemStack stack = inv.getInputsForProcessor(getProcessor(in).getProcessorNumber()).get(0);
				if (stack.getItem() instanceof BlockItem it) {
					Block block = it.getBlock();
					for (int i = 0; i < 5; i++) {
						double d4 = level.random.nextDouble() * 4.0 / 16.0 + 0.5 - 2.0 / 16.0;
						double d6 = level.random.nextDouble() * 4.0 / 16.0 + 0.5 - 2.0 / 16.0;
						ParticleAPI.addGrindedParticle(level, worldPosition.getX() + d4 + direction.getStepX() * 0.2, worldPosition.getY() + 0.4, worldPosition.getZ() + d6 + direction.getStepZ() * 0.2, 0.0D, 0.0D, 0.0D, block.defaultBlockState(), worldPosition);
					}
				}
			}
		}
		clientRunningTicks++;

		if (!isSoundPlaying) {
			isSoundPlaying = true;
			SoundBarrierMethods.playTileSound(ElectrodynamicsSounds.SOUND_MINERALCRUSHER.get(), this, true);
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
