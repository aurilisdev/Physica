package electrodynamics.common.tile.pipelines.fluid;

import electrodynamics.common.block.subtype.SubtypeMachine;
import electrodynamics.common.inventory.container.tile.ContainerCreativeFluidSource;
import electrodynamics.prefab.tile.GenericTile;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.tile.components.type.ComponentContainerProvider;
import electrodynamics.prefab.tile.components.type.ComponentFluidHandlerSimple;
import electrodynamics.prefab.tile.components.type.ComponentInventory;
import electrodynamics.prefab.tile.components.type.ComponentInventory.InventoryBuilder;
import electrodynamics.prefab.tile.components.type.ComponentPacketHandler;
import electrodynamics.prefab.tile.components.type.ComponentTickable;
import electrodynamics.prefab.utilities.BlockEntityUtils;
import electrodynamics.registers.ElectrodynamicsTiles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

public class TileCreativeFluidSource extends GenericTile {

    public TileCreativeFluidSource(BlockPos worldPos, BlockState blockState) {
        super(ElectrodynamicsTiles.TILE_CREATIVEFLUIDSOURCE.get(), worldPos, blockState);
        addComponent(new ComponentTickable(this).tickServer(this::tickServer));
        addComponent(new ComponentPacketHandler(this));
        addComponent(new ComponentFluidHandlerSimple(128000, this, "").setOutputDirections(BlockEntityUtils.MachineDirection.values()));
        addComponent(new ComponentInventory(this, InventoryBuilder.newInv().bucketInputs(1).bucketOutputs(1)).valid((slot, stack, i) -> stack.getCapability(Capabilities.FluidHandler.ITEM) != null));
        addComponent(new ComponentContainerProvider(SubtypeMachine.creativefluidsource, this).createMenu((id, player) -> new ContainerCreativeFluidSource(id, player, getComponent(IComponentType.Inventory), getCoordsArray())));
    }

    private void tickServer(ComponentTickable tick) {

        ComponentFluidHandlerSimple simple = (ComponentFluidHandlerSimple) getComponent(IComponentType.FluidHandler);
        ComponentInventory inv = getComponent(IComponentType.Inventory);
        ItemStack input = inv.getItem(0);
        ItemStack output = inv.getItem(1);

        simple.setFluid(new FluidStack(simple.getFluid().getFluidHolder(), simple.getCapacity()));

        // set tank fluid from slot 1
        if (!input.isEmpty()) {

            IFluidHandlerItem handler = input.getCapability(Capabilities.FluidHandler.ITEM);

            if (handler != null) {

                simple.setFluid(new FluidStack(handler.drain(Integer.MAX_VALUE, FluidAction.SIMULATE).getFluidHolder(), simple.getCapacity()));

            }

        }

        // fill item in slot 2
        if (!output.isEmpty()) {

            IFluidHandlerItem handler = output.getCapability(Capabilities.FluidHandler.ITEM);

            if (handler != null) {

                handler.fill(simple.getFluid().copy(), FluidAction.EXECUTE);

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

            IFluidHandler handler = faceTile.getLevel().getCapability(Capabilities.FluidHandler.BLOCK, faceTile.getBlockPos(), faceTile.getBlockState(), faceTile, direction);

            if (handler == null) {
                continue;
            }

            for (FluidTank fluidTank : simple.asArray()) {

                FluidStack tankFluid = fluidTank.getFluid();

                handler.fill(tankFluid, FluidAction.EXECUTE);

            }
        }
    }

}
