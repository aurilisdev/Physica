package electrodynamics.api.multiblock.assemblybased;

import org.jetbrains.annotations.Nullable;

import electrodynamics.Electrodynamics;
import electrodynamics.api.capability.types.electrodynamic.ICapabilityElectrodynamic;
import electrodynamics.api.capability.types.gas.IGasHandler;
import electrodynamics.api.gas.GasTank;
import electrodynamics.client.modelbakers.modelproperties.ModelPropertySlaveNode;
import electrodynamics.prefab.properties.Property;
import electrodynamics.prefab.properties.PropertyTypes;
import electrodynamics.prefab.tile.components.type.ComponentElectrodynamic;
import electrodynamics.prefab.tile.components.type.ComponentInventory;
import electrodynamics.prefab.tile.components.type.ComponentPacketHandler;
import electrodynamics.prefab.tile.components.type.ComponentTickable;
import electrodynamics.prefab.utilities.BlockEntityUtils;
import electrodynamics.prefab.utilities.Scheduler;
import electrodynamics.registers.ElectrodynamicsTiles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.IItemHandler;

/**
 * @author skip999
 */
public class TileMultiblockSlave extends TileReplaceable {

	public final Property<BlockPos> controller = property(new Property<>(PropertyTypes.BLOCK_POS, "controllerpos", BlockEntityUtils.OUT_OF_REACH)).onTileLoaded(prop -> {
		if(!getLevel().isClientSide()) {
			return;
		}
		if(!(getLevel().getBlockEntity(prop.get()) instanceof TileMultiblockController)) {
			Scheduler.schedule(1, () -> level.setBlockAndUpdate(getBlockPos(), getDisguise()));
		}
	});
	public final Property<Integer> index = property(new Property<>(PropertyTypes.INTEGER, "nodeindex", -1));
	public final Property<ResourceLocation> renderModel = property(new Property<>(PropertyTypes.RESOURCE_LOCATION, "model", MultiblockSlaveNode.NOMODEL));
	
	private boolean destroyed = false;
	
	public TileMultiblockSlave(BlockPos worldPos, BlockState blockState) {
		super(ElectrodynamicsTiles.TILE_MULTIBLOCK_SLAVE.get(), worldPos, blockState);
		addComponent(new ComponentPacketHandler(this));
		addComponent(new ComponentTickable(this).tickServer(this::tickServer));
	}

	private void tickServer(ComponentTickable componentTickable) {
		if(disguisedBlock.get() == 27068) {
			Electrodynamics.LOGGER.info(getDisguise());
		}
	}

	@Override
	public int getComparatorSignal() {
		if(level.getBlockEntity(controller.get()) instanceof TileMultiblockController controller) {
			return controller.getSlaveComparatorSignal(this);
		}
		return super.getComparatorSignal();
	}
	
	@Override
	public int getDirectSignal(Direction dir) {
		if(level.getBlockEntity(controller.get()) instanceof TileMultiblockController controller) {
			return controller.getSlaveDirectSignal(this, dir);
		}
		return super.getDirectSignal(dir);
	}
	
	@Override
	public int getSignal(Direction dir) {
		if(level.getBlockEntity(controller.get()) instanceof TileMultiblockController controller) {
			return controller.getSlaveSignal(this, dir);
		}
		return super.getSignal(dir);
	}
	
	@Override
	public boolean isPoweredByRedstone() {
		if(level.getBlockEntity(controller.get()) instanceof TileMultiblockController controller) {
			return controller.isSlavePoweredByRedstone(this);
		}
		return super.isPoweredByRedstone();
	}
	
	@Override
	public void onBlockDestroyed() {
		super.onBlockDestroyed();
		
		if(destroyed) {
			return;
		}
		
		destroyed = true;
		
		if(!level.isClientSide) {
			level.setBlockAndUpdate(getBlockPos(), getDisguise());
		}
		
		if(level.getBlockEntity(controller.get()) instanceof TileMultiblockController controller) {
			controller.onSlaveDestroyed(this);
		}
		
	}
	
	@Override
	public void onBlockStateUpdate(BlockState oldState, BlockState newState) {
		super.onBlockStateUpdate(oldState, newState);
		if(level.getBlockEntity(controller.get()) instanceof TileMultiblockController controller) {
			controller.onSlaveBlockStateUpdate(this, oldState, newState);
		}
	}
	
	@Override
	public void onEnergyChange(ComponentElectrodynamic cap) {
		super.onEnergyChange(cap);
		if(level.getBlockEntity(controller.get()) instanceof TileMultiblockController controller) {
			controller.onSlaveEnergyChange(this, cap);
		}
	}
	
	@Override
	public void onEntityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
		super.onEntityInside(state, level, pos, entity);
		if(level.getBlockEntity(controller.get()) instanceof TileMultiblockController controller) {
			controller.onSlaveEntityInside(this, state, level, pos, entity);
		}
	}
	
	@Override
	public void onFluidTankChange(FluidTank tank) {
		super.onFluidTankChange(tank);
		if(level.getBlockEntity(controller.get()) instanceof TileMultiblockController controller) {
			controller.onSlaveFluidTankChange(this, tank);
		}
	}
	
	@Override
	public void onGasTankChange(GasTank tank) {
		super.onGasTankChange(tank);
		if(level.getBlockEntity(controller.get()) instanceof TileMultiblockController controller) {
			controller.onSlaveGasTankChange(this, tank);
		}
	}
	
	@Override
	public void onInventoryChange(ComponentInventory inv, int index) {
		super.onInventoryChange(inv, index);
		if(level.getBlockEntity(controller.get()) instanceof TileMultiblockController controller) {
			controller.onSlaveInventoryChange(this, inv, index);
		}
	}

	@Override
	public InteractionResult useWithoutItem(Player player, BlockHitResult hit) {
		if(level.getBlockEntity(controller.get()) instanceof TileMultiblockController controller) {
			return controller.slaveUseWithoutItem(this, player, hit);
		}
		return super.useWithoutItem(player, hit);
	}

	@Override
	public ItemInteractionResult useWithItem(ItemStack used, Player player, InteractionHand hand, BlockHitResult hit) {
		if(level.getBlockEntity(controller.get()) instanceof TileMultiblockController controller) {
			return controller.slaveUseWithItem(this, used, player, hand, hit);
		}
		return super.useWithItem(used, player, hand, hit);
	}
	
	@Override
	public void onNeightborChanged(BlockPos neighbor, boolean blockStateTrigger) {
		super.onNeightborChanged(neighbor, blockStateTrigger);
		if(level.getBlockEntity(controller.get()) instanceof TileMultiblockController controller) {
			controller.onSlaveNeightborChanged(this, neighbor, blockStateTrigger);
		}
	}
	
	@Override
	public void onPlace(BlockState state, boolean isMoving) {
		super.onPlace(state, isMoving);
		if(level.getBlockEntity(controller.get()) instanceof TileMultiblockController controller) {
			controller.onSlavePlace(this, state, isMoving);
		}
	}

	public VoxelShape getShape() {
		if(level.getBlockEntity(controller.get()) instanceof TileMultiblockController controller) {
			return controller.getSlaveShape(this);
		}
		return Shapes.block();
	}

	@Override
	public @Nullable ICapabilityElectrodynamic getElectrodynamicCapability(@Nullable Direction side) {
		if(level.getBlockEntity(controller.get()) instanceof TileMultiblockController controller) {
			return controller.getSlaveCapabilityElectrodynamic(this, side);
		}
		return super.getElectrodynamicCapability(side);
	}

	@Override
	public @Nullable IItemHandler getItemHandlerCapability(@Nullable Direction side) {
		if(level.getBlockEntity(controller.get()) instanceof TileMultiblockController controller) {
			return controller.getSlaveItemHandlerCapability(this, side);
		}
		return super.getItemHandlerCapability(side);
	}

	@Override
	public @Nullable IFluidHandler getFluidHandlerCapability(@Nullable Direction side) {
		if(level.getBlockEntity(controller.get()) instanceof TileMultiblockController controller) {
			return controller.getSlaveFluidHandlerCapability(this, side);
		}
		return super.getFluidHandlerCapability(side);
	}

	@Override
	public @Nullable IGasHandler getGasHandlerCapability(@Nullable Direction side) {
		if(level.getBlockEntity(controller.get()) instanceof TileMultiblockController controller) {
			return controller.getSlaveGasHandlerCapability(this, side);
		}
		return super.getGasHandlerCapability(side);
	}

	@Override
	public ModelData getModelData() {
		return ModelData.builder().with(ModelPropertySlaveNode.INSTANCE, new ModelPropertySlaveNode.SlaveNodeWrapper(renderModel.get(), getFacing())).build();
	}
}
