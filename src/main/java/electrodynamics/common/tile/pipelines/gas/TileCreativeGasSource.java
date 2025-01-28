package electrodynamics.common.tile.pipelines.gas;

import electrodynamics.api.capability.types.gas.IGasHandler;
import electrodynamics.api.capability.types.gas.IGasHandlerItem;
import electrodynamics.api.gas.GasAction;
import electrodynamics.api.gas.GasStack;
import electrodynamics.api.gas.GasTank;
import electrodynamics.common.block.subtype.SubtypeMachine;
import electrodynamics.common.inventory.container.tile.ContainerCreativeGasSource;
import electrodynamics.prefab.tile.GenericTile;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.tile.components.type.ComponentContainerProvider;
import electrodynamics.prefab.tile.components.type.ComponentGasHandlerSimple;
import electrodynamics.prefab.tile.components.type.ComponentInventory;
import electrodynamics.prefab.tile.components.type.ComponentPacketHandler;
import electrodynamics.prefab.tile.components.type.ComponentTickable;
import electrodynamics.prefab.utilities.BlockEntityUtils;
import electrodynamics.registers.ElectrodynamicsCapabilities;
import electrodynamics.registers.ElectrodynamicsTiles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class TileCreativeGasSource extends GenericTile {
    public TileCreativeGasSource(BlockPos worldPos, BlockState blockState) {
        super(ElectrodynamicsTiles.TILE_CREATIVEGASSOURCE.get(), worldPos, blockState);
        addComponent(new ComponentTickable(this).tickServer(this::tickServer));
        addComponent(new ComponentPacketHandler(this));
        addComponent(new ComponentGasHandlerSimple(this, "", 128000, 1000000, 1000000).setOutputDirections(BlockEntityUtils.MachineDirection.values()));
        addComponent(new ComponentInventory(this, ComponentInventory.InventoryBuilder.newInv().gasInputs(1).gasOutputs(1)).valid((slot, stack, i) -> stack.getCapability(ElectrodynamicsCapabilities.CAPABILITY_GASHANDLER_ITEM) != null));
        addComponent(new ComponentContainerProvider(SubtypeMachine.creativegassource, this).createMenu((id, player) -> new ContainerCreativeGasSource(id, player, getComponent(IComponentType.Inventory), getCoordsArray())));
    }

    private void tickServer(ComponentTickable tick) {

        ComponentGasHandlerSimple simple = getComponent(IComponentType.GasHandler);
        ComponentInventory inv = getComponent(IComponentType.Inventory);
        ItemStack input = inv.getItem(0);
        ItemStack output = inv.getItem(1);

        simple.setGas(new GasStack(simple.getGas().getGas(), simple.getCapacity(), simple.getGas().getTemperature(), simple.getGas().getPressure()));

        // set tank fluid from slot 1
        if (!input.isEmpty()) {

            IGasHandlerItem handler = input.getCapability(ElectrodynamicsCapabilities.CAPABILITY_GASHANDLER_ITEM);

            if (handler != null) {

                GasStack contained = handler.drain(Integer.MAX_VALUE, GasAction.SIMULATE);

                simple.setGas(new GasStack(contained.getGas(), simple.getCapacity(), contained.getTemperature(), contained.getPressure()));

            }

        }

        // fill item in slot 2
        if (!output.isEmpty()) {

            IGasHandlerItem handler = output.getCapability(ElectrodynamicsCapabilities.CAPABILITY_GASHANDLER_ITEM);

            if (handler != null) {

                handler.fill(simple.getGas().copy(), GasAction.EXECUTE);

                inv.setItem(1, handler.getContainer());

            }

        }

        Direction facing = getFacing();

        for (Direction relative : simple.outputDirections) {

            Direction direction = BlockEntityUtils.getRelativeSide(facing, relative.getOpposite());

            BlockPos face = getBlockPos().relative(direction.getOpposite());

            BlockEntity faceTile = getLevel().getBlockEntity(face);

            if (faceTile == null) {
                continue;
            }

            IGasHandler handler = faceTile.getLevel().getCapability(ElectrodynamicsCapabilities.CAPABILITY_GASHANDLER_BLOCK, faceTile.getBlockPos(), faceTile.getBlockState(), faceTile, direction);

            if (handler == null) {
                continue;
            }

            for (GasTank gasTank : simple.asArray()) {

                GasStack tankGas = gasTank.getGas();

                handler.fill(tankGas, GasAction.EXECUTE);

            }
        }
    }
}
