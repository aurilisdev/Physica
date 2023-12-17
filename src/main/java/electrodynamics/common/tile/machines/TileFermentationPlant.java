package electrodynamics.common.tile.machines;

import electrodynamics.api.capability.ElectrodynamicsCapabilities;
import electrodynamics.common.block.subtype.SubtypeMachine;
import electrodynamics.common.inventory.container.tile.ContainerFermentationPlant;
import electrodynamics.common.recipe.ElectrodynamicsRecipeInit;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.tile.components.type.ComponentContainerProvider;
import electrodynamics.prefab.tile.components.type.ComponentElectrodynamic;
import electrodynamics.prefab.tile.components.type.ComponentFluidHandlerMulti;
import electrodynamics.prefab.tile.components.type.ComponentInventory;
import electrodynamics.prefab.tile.components.type.ComponentPacketHandler;
import electrodynamics.prefab.tile.components.type.ComponentProcessor;
import electrodynamics.prefab.tile.components.type.ComponentTickable;
import electrodynamics.prefab.tile.components.type.ComponentInventory.InventoryBuilder;
import electrodynamics.prefab.tile.types.GenericFluidTile;
import electrodynamics.registers.ElectrodynamicsBlockTypes;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;

public class TileFermentationPlant extends GenericFluidTile {

	public static final int MAX_TANK_CAPACITY = 5000;

	public TileFermentationPlant() {
		super(ElectrodynamicsBlockTypes.TILE_FERMENTATIONPLANT.get());
		addComponent(new ComponentTickable(this).tickClient(this::tickClient));
		addComponent(new ComponentPacketHandler(this));
		addComponent(new ComponentElectrodynamic(this, false, true).setInputDirections(Direction.DOWN).voltage(ElectrodynamicsCapabilities.DEFAULT_VOLTAGE));
		addComponent(new ComponentFluidHandlerMulti(this).setTanks(1, 1, new int[] { MAX_TANK_CAPACITY }, new int[] { MAX_TANK_CAPACITY }).setInputDirections(Direction.WEST).setOutputDirections(Direction.EAST).setRecipeType(ElectrodynamicsRecipeInit.FERMENTATION_PLANT_TYPE));
		addComponent(new ComponentInventory(this, InventoryBuilder.newInv().processors(1, 1, 0, 0).bucketInputs(1).bucketOutputs(1).upgrades(3)).setDirectionsBySlot(0, Direction.NORTH, Direction.UP, Direction.SOUTH, Direction.DOWN).validUpgrades(ContainerFermentationPlant.VALID_UPGRADES).valid(machineValidator()));
		addComponent(new ComponentProcessor(this).canProcess(processor -> processor.outputToFluidPipe().consumeBucket().dispenseBucket().canProcessFluidItem2FluidRecipe(processor, ElectrodynamicsRecipeInit.FERMENTATION_PLANT_TYPE)).process(component -> component.processFluidItem2FluidRecipe(component)));
		addComponent(new ComponentContainerProvider(SubtypeMachine.fermentationplant, this).createMenu((id, player) -> new ContainerFermentationPlant(id, player, getComponent(IComponentType.Inventory), getCoordsArray())));

	}

	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return super.getRenderBoundingBox().inflate(1);
	}

	protected void tickClient(ComponentTickable tickable) {
		if (this.<ComponentProcessor>getComponent(IComponentType.Processor).isActive()) {
			if (level.random.nextDouble() < 0.15) {
				level.addParticle(ParticleTypes.SMOKE, worldPosition.getX() + level.random.nextDouble(), worldPosition.getY() + level.random.nextDouble() * 0.4 + 0.5, worldPosition.getZ() + level.random.nextDouble(), 0.0D, 0.0D, 0.0D);
			}
			Direction dir = getFacing();
			double x = worldPosition.getX() + 0.55 - dir.getStepX() * 0.2;
			double z = worldPosition.getZ() + 0.55 - dir.getStepZ() * 0.2;
			level.addParticle(ParticleTypes.SOUL_FIRE_FLAME, x, worldPosition.getY() + 0.4, z, 0.0D, 0.0D, 0.0D);
		}
	}

	@Override
	public int getComparatorSignal() {
		return this.<ComponentProcessor>getComponent(IComponentType.Processor).isActive() ? 15 : 0;
	}
	
}