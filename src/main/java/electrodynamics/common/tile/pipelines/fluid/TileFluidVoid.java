package electrodynamics.common.tile.pipelines.fluid;

import electrodynamics.common.block.subtype.SubtypeMachine;
import electrodynamics.common.inventory.container.tile.ContainerFluidVoid;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.tile.components.type.ComponentContainerProvider;
import electrodynamics.prefab.tile.components.type.ComponentFluidHandlerSimple;
import electrodynamics.prefab.tile.components.type.ComponentInventory;
import electrodynamics.prefab.tile.components.type.ComponentInventory.InventoryBuilder;
import electrodynamics.prefab.tile.components.type.ComponentPacketHandler;
import electrodynamics.prefab.tile.components.type.ComponentTickable;
import electrodynamics.prefab.tile.types.GenericMaterialTile;
import electrodynamics.prefab.utilities.BlockEntityUtils;
import electrodynamics.registers.ElectrodynamicsTiles;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;

public class TileFluidVoid extends GenericMaterialTile {
	
	public static final int CAPACITY = 128000;
	
	public static final int INPUT_SLOT = 0;

	public TileFluidVoid(BlockPos worldPos, BlockState blockState) {
		super(ElectrodynamicsTiles.TILE_FLUIDVOID.get(), worldPos, blockState);
		addComponent(new ComponentTickable(this).tickServer(this::tickServer));
		addComponent(new ComponentPacketHandler(this));
		addComponent(new ComponentFluidHandlerSimple(CAPACITY, this, "").setInputDirections(BlockEntityUtils.MachineDirection.values()));
		addComponent(new ComponentInventory(this, InventoryBuilder.newInv().bucketInputs(1)).valid((slot, stack, i) -> stack.getCapability(Capabilities.FluidHandler.ITEM) != null));
		addComponent(new ComponentContainerProvider(SubtypeMachine.fluidvoid, this).createMenu((id, player) -> new ContainerFluidVoid(id, player, getComponent(IComponentType.Inventory), getCoordsArray())));
	}

	private void tickServer(ComponentTickable tick) {
		
		ComponentInventory inv = getComponent(IComponentType.Inventory);
		
		ComponentFluidHandlerSimple simple = getComponent(IComponentType.FluidHandler);
		
		simple.drain(simple.getFluidAmount(), FluidAction.EXECUTE);
		
		ItemStack input = inv.getItem(INPUT_SLOT);
		
		if(input.isEmpty()) {
			return;
		}
		
		IFluidHandlerItem handler = input.getCapability(Capabilities.FluidHandler.ITEM);
	
		if(handler == null) {
		    return;
		}
		
		handler.drain(Integer.MAX_VALUE, FluidAction.EXECUTE);
		
		inv.setItem(INPUT_SLOT, handler.getContainer());
		
	}

}
