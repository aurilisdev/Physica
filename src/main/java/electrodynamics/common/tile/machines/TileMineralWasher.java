package electrodynamics.common.tile.machines;

import electrodynamics.common.block.subtype.SubtypeMachine;
import electrodynamics.common.inventory.container.tile.ContainerMineralWasher;
import electrodynamics.common.recipe.ElectrodynamicsRecipeInit;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.tile.components.type.ComponentContainerProvider;
import electrodynamics.prefab.tile.components.type.ComponentElectrodynamic;
import electrodynamics.prefab.tile.components.type.ComponentFluidHandlerMulti;
import electrodynamics.prefab.tile.components.type.ComponentInventory;
import electrodynamics.prefab.tile.components.type.ComponentInventory.InventoryBuilder;
import electrodynamics.prefab.tile.components.type.ComponentPacketHandler;
import electrodynamics.prefab.tile.components.type.ComponentProcessor;
import electrodynamics.prefab.tile.components.type.ComponentTickable;
import electrodynamics.prefab.tile.types.GenericMaterialTile;
import electrodynamics.prefab.utilities.BlockEntityUtils;
import electrodynamics.registers.ElectrodynamicsCapabilities;
import electrodynamics.registers.ElectrodynamicsTiles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.block.state.BlockState;

public class TileMineralWasher extends GenericMaterialTile {
    public static final int MAX_TANK_CAPACITY = 5000;

    public TileMineralWasher(BlockPos worldPosition, BlockState blockState) {
        super(ElectrodynamicsTiles.TILE_MINERALWASHER.get(), worldPosition, blockState);
        addComponent(new ComponentTickable(this).tickClient(this::tickClient));
        addComponent(new ComponentPacketHandler(this));
        addComponent(new ComponentElectrodynamic(this, false, true).setInputDirections(BlockEntityUtils.MachineDirection.BACK).voltage(ElectrodynamicsCapabilities.DEFAULT_VOLTAGE * 4));
        addComponent(new ComponentFluidHandlerMulti(this).setTanks(1, 1, new int[]{MAX_TANK_CAPACITY}, new int[]{MAX_TANK_CAPACITY}).setInputDirections(BlockEntityUtils.MachineDirection.RIGHT).setOutputDirections(BlockEntityUtils.MachineDirection.LEFT).setRecipeType(ElectrodynamicsRecipeInit.MINERAL_WASHER_TYPE.get()));
        addComponent(new ComponentInventory(this, InventoryBuilder.newInv().processors(1, 1, 0, 0).bucketInputs(1).bucketOutputs(1).upgrades(3))
                //
                .setDirectionsBySlot(0, BlockEntityUtils.MachineDirection.BOTTOM, BlockEntityUtils.MachineDirection.TOP, BlockEntityUtils.MachineDirection.FRONT).validUpgrades(ContainerMineralWasher.VALID_UPGRADES).valid(machineValidator()));
        addComponent(new ComponentProcessor(this).canProcess(component -> component.outputToFluidPipe().consumeBucket().dispenseBucket().canProcessFluidItem2FluidRecipe(component, ElectrodynamicsRecipeInit.MINERAL_WASHER_TYPE.get())).process(component -> component.processFluidItem2FluidRecipe(component)));
        addComponent(new ComponentContainerProvider(SubtypeMachine.mineralwasher, this).createMenu((id, player) -> new ContainerMineralWasher(id, player, getComponent(IComponentType.Inventory), getCoordsArray())));
    }

    protected void tickClient(ComponentTickable tickable) {
        if (this.<ComponentProcessor>getComponent(IComponentType.Processor).isActive() && level.getRandom().nextDouble() < 0.15) {
            level.addParticle(ParticleTypes.SMOKE, worldPosition.getX() + level.random.nextDouble(), worldPosition.getY() + level.random.nextDouble() * 0.4 + 0.5, worldPosition.getZ() + level.random.nextDouble(), 0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    public int getComparatorSignal() {
        return this.<ComponentProcessor>getComponent(IComponentType.Processor).isActive() ? 15 : 0;
    }

}
