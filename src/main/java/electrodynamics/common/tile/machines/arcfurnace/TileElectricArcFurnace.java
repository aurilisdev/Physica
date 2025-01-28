package electrodynamics.common.tile.machines.arcfurnace;

import java.util.List;

import electrodynamics.Electrodynamics;
import electrodynamics.client.particle.lavawithphysics.ParticleOptionLavaWithPhysics;
import electrodynamics.common.block.subtype.SubtypeMachine;
import electrodynamics.common.inventory.container.tile.ContainerElectricArcFurnace;
import electrodynamics.common.inventory.container.tile.ContainerElectricArcFurnaceDouble;
import electrodynamics.common.inventory.container.tile.ContainerElectricArcFurnaceTriple;
import electrodynamics.common.item.ItemUpgrade;
import electrodynamics.common.item.subtype.SubtypeItemUpgrade;
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
import electrodynamics.registers.ElectrodynamicsDataComponentTypes;
import electrodynamics.registers.ElectrodynamicsSounds;
import electrodynamics.registers.ElectrodynamicsTiles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.BlastingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.block.state.BlockState;

public class TileElectricArcFurnace extends GenericTile implements ITickableSound {

	protected BlastingRecipe[] cachedRecipe = null;

	private List<RecipeHolder<BlastingRecipe>> cachedRecipes = null;

	private boolean isSoundPlaying = false;

	private final int extra;

	public TileElectricArcFurnace(BlockPos worldPosition, BlockState blockState) {
		this(SubtypeMachine.electricarcfurnace, 0, worldPosition, blockState);
	}

	public TileElectricArcFurnace(SubtypeMachine machine, int extra, BlockPos worldPosition, BlockState blockState) {
		super(extra == 1 ? ElectrodynamicsTiles.TILE_ELECTRICARCFURNACEDOUBLE.get() : extra == 2 ? ElectrodynamicsTiles.TILE_ELECTRICARCFURNACETRIPLE.get() : ElectrodynamicsTiles.TILE_ELECTRICARCFURNACE.get(), worldPosition, blockState);

		this.extra = extra;

		int processorCount = extra + 1;
		int inputsPerProc = 1;
		int outputPerProc = 1;

		addComponent(new ComponentPacketHandler(this));
		addComponent(new ComponentTickable(this).tickClient(this::tickClient));
		addComponent(new ComponentElectrodynamic(this, false, true).setInputDirections(BlockEntityUtils.MachineDirection.BACK).voltage(ElectrodynamicsCapabilities.DEFAULT_VOLTAGE * Math.pow(2, extra)).maxJoules(Constants.ELECTRICARCFURNACE_USAGE_PER_TICK * 20 * (extra + 1)));
		addComponent(new ComponentInventory(this, InventoryBuilder.newInv().processors(processorCount, inputsPerProc, outputPerProc, 0).upgrades(3)).validUpgrades(ContainerElectricArcFurnace.VALID_UPGRADES).valid(machineValidator()).implementMachineInputsAndOutputs());
		addComponent(new ComponentContainerProvider(machine, this).createMenu((id, player) -> (extra == 0 ? new ContainerElectricArcFurnace(id, player, getComponent(IComponentType.Inventory), getCoordsArray()) : extra == 1 ? new ContainerElectricArcFurnaceDouble(id, player, getComponent(IComponentType.Inventory), getCoordsArray()) : extra == 2 ? new ContainerElectricArcFurnaceTriple(id, player, getComponent(IComponentType.Inventory), getCoordsArray()) : null)));

		for (int i = 0; i <= extra; i++) {
			addProcessor(new ComponentProcessor(this, i, extra + 1).canProcess(this::canProcess).process(this::process).requiredTicks(Constants.ELECTRICARCFURNACE_REQUIRED_TICKS).usage(Constants.ELECTRICARCFURNACE_USAGE_PER_TICK));
		}
		cachedRecipe = new BlastingRecipe[extra + 1];
	}

	protected void process(ComponentProcessor component) {
		ComponentInventory inv = getComponent(IComponentType.Inventory);
		ItemStack input = inv.getInputsForProcessor(component.getProcessorNumber()).get(0);
		ItemStack output = inv.getOutputsForProcessor(component.getProcessorNumber()).get(0);
		ItemStack result = cachedRecipe[component.getProcessorNumber()].getResultItem(level.registryAccess());
		int index = inv.getOutputSlots().get(component.getProcessorNumber());
		if (!output.isEmpty()) {
			output.setCount(output.getCount() + result.getCount());
			inv.setItem(index, output);
		} else {
			inv.setItem(index, result.copy());
		}
		input.shrink(1);
		inv.setItem(inv.getInputSlotsForProcessor(component.getProcessorNumber()).get(0), input.copy());
		for (ItemStack stack : inv.getUpgradeContents()) {
			if (!stack.isEmpty() && ((ItemUpgrade) stack.getItem()).subtype == SubtypeItemUpgrade.experience) {
				stack.set(ElectrodynamicsDataComponentTypes.XP, stack.getOrDefault(ElectrodynamicsDataComponentTypes.XP, 0.0) + cachedRecipe[component.getProcessorNumber()].getExperience());
				break;
			}
		}
	}

	protected boolean canProcess(ComponentProcessor component) {
		boolean canProcess = checkConditions(component);

		if (BlockEntityUtils.isLit(this) ^ canProcess || isProcessorActive()) {
			BlockEntityUtils.updateLit(this, canProcess || isProcessorActive());
		}

		return canProcess;
	}

	private boolean checkConditions(ComponentProcessor component) {
		component.setShouldKeepProgress(true);
		ComponentInventory inv = getComponent(IComponentType.Inventory);
		ItemStack input = inv.getInputsForProcessor(component.getProcessorNumber()).get(0);
		if (input.isEmpty()) {
			return false;
		}

		if (cachedRecipes == null) {
			cachedRecipes = level.getRecipeManager().getAllRecipesFor(RecipeType.BLASTING);
		}

		if (cachedRecipe == null) {
			component.setShouldKeepProgress(false);
			return false;
		}

		if (cachedRecipe[component.getProcessorNumber()] == null) {
			cachedRecipe[component.getProcessorNumber()] = getMatchedRecipe(input);
			if (cachedRecipe[component.getProcessorNumber()] == null) {
				component.setShouldKeepProgress(false);
				return false;
			}
			component.operatingTicks.set(0.0);
		}

		if (!cachedRecipe[component.getProcessorNumber()].matches(new SingleRecipeInput(input), level)) {
			cachedRecipe[component.getProcessorNumber()] = null;
			component.setShouldKeepProgress(false);
			return false;
		}

		ComponentElectrodynamic electro = getComponent(IComponentType.Electrodynamic);
		if (electro.getJoulesStored() < component.getUsage() * component.operatingSpeed.get()) {
			return false;
		}
		electro.maxJoules(component.getUsage() * component.operatingSpeed.get() * 10 * component.totalProcessors);

		ItemStack output = inv.getOutputContents().get(component.getProcessorNumber());
		ItemStack result = cachedRecipe[component.getProcessorNumber()].getResultItem(level.registryAccess());
		return (output.isEmpty() || output.getItem() == result.getItem()) && output.getCount() + result.getCount() <= output.getMaxStackSize();

	}

	protected void tickClient(ComponentTickable tickable) {
		if (!isProcessorActive()) {
			return;
		}

		double threshhold = 0.5;

		if(extra == 1) {
			threshhold = 0.75;
		} else if (extra == 2){
			threshhold = 0.9;
		}

		if (level.random.nextDouble() < threshhold) {

			Direction direction = getFacing();

			double axisShift = 0.5;

			if (extra == 1) {
				axisShift = Electrodynamics.RANDOM.nextDouble(0.5) + 0.25;
			} else if (extra == 2) {
				axisShift = Electrodynamics.RANDOM.nextDouble(0.6) + 0.22;
			}

			double yShift = 0.6;

			double xShift = direction.getAxis() == Direction.Axis.X ? direction.getStepX() * (direction.getStepX() == -0.5 ? 0 : 0.5) : axisShift;
			double zShift = direction.getAxis() == Direction.Axis.Z ? direction.getStepZ() * (direction.getStepZ() == -0.5 ? 0 : 0.5) : axisShift;

			double xVel = (Math.random() * 2.0 - 1.0) * 0.4F;
			double yVel = Math.random() * 0.4F;
			double zVel = (Math.random() * 2.0 - 1.0) * 0.4F;
			double rand = (Math.random() + Math.random() + 1.0) * 0.15F;
			double vectorMag = Math.sqrt(xVel * xVel + yVel * yVel + zVel * zVel);
			xVel = xVel / vectorMag * rand * 0.4F;
			yVel = Math.max(0.05, yVel / vectorMag * rand * 0.4F + 0.1F);
			zVel = zVel / vectorMag * rand * 0.4F;

			level.addParticle(new ParticleOptionLavaWithPhysics().setParameters(0.05F, 1, 1), worldPosition.getX() + xShift, worldPosition.getY() + yShift, worldPosition.getZ() + zShift, xVel, yVel, zVel);
			level.addParticle(ParticleTypes.SMOKE, worldPosition.getX() + xShift, worldPosition.getY() + yShift, worldPosition.getZ(), 0, 0, 0);






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

	private BlastingRecipe getMatchedRecipe(ItemStack stack) {
		for (RecipeHolder<BlastingRecipe> recipe : cachedRecipes) {
			if (recipe.value().matches(new SingleRecipeInput(stack), level)) {
				return recipe.value();
			}
		}
		return null;
	}

	@Override
	public int getComparatorSignal() {
		return (int) (((double) getNumActiveProcessors() / (double) Math.max(1, getNumProcessors())) * 15.0);
	}

}
