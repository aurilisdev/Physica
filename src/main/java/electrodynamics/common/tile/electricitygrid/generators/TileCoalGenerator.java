package electrodynamics.common.tile.electricitygrid.generators;

import java.util.ArrayList;
import java.util.List;

import electrodynamics.common.block.states.ElectrodynamicsBlockStates;
import electrodynamics.common.block.subtype.SubtypeMachine;
import electrodynamics.common.inventory.container.tile.ContainerCoalGenerator;
import electrodynamics.common.reloadlistener.CoalGeneratorFuelRegister;
import electrodynamics.common.settings.Constants;
import electrodynamics.prefab.properties.Property;
import electrodynamics.prefab.properties.PropertyTypes;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.tile.components.type.ComponentContainerProvider;
import electrodynamics.prefab.tile.components.type.ComponentElectrodynamic;
import electrodynamics.prefab.tile.components.type.ComponentInventory;
import electrodynamics.prefab.tile.components.type.ComponentInventory.InventoryBuilder;
import electrodynamics.prefab.tile.components.type.ComponentPacketHandler;
import electrodynamics.prefab.tile.components.type.ComponentTickable;
import electrodynamics.prefab.utilities.BlockEntityUtils;
import electrodynamics.prefab.utilities.ElectricityUtils;
import electrodynamics.prefab.utilities.object.CachedTileOutput;
import electrodynamics.prefab.utilities.object.TargetValue.PropertyTargetValue;
import electrodynamics.prefab.utilities.object.TransferPack;
import electrodynamics.registers.ElectrodynamicsCapabilities;
import electrodynamics.registers.ElectrodynamicsTiles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class TileCoalGenerator extends GenericGeneratorTile {
	protected CachedTileOutput output;
	protected TransferPack currentOutput = TransferPack.EMPTY;
	public PropertyTargetValue heat = new PropertyTargetValue(property(new Property<>(PropertyTypes.DOUBLE, "heat", 27.0)));
	public Property<Integer> burnTime = property(new Property<>(PropertyTypes.INTEGER, "burnTime", 0));
	public Property<Integer> maxBurnTime = property(new Property<>(PropertyTypes.INTEGER, "maxBurnTime", 1));
	// for future planned upgrades
	private Property<Double> multiplier = property(new Property<>(PropertyTypes.DOUBLE, "multiplier", 1.0));
	private Property<Boolean> hasRedstoneSignal = property(new Property<>(PropertyTypes.BOOLEAN, "redstonesignal", false));

	public TileCoalGenerator(BlockPos worldPosition, BlockState blockState) {
		super(ElectrodynamicsTiles.TILE_COALGENERATOR.get(), worldPosition, blockState, 1.0);
		addComponent(new ComponentPacketHandler(this));
		addComponent(new ComponentTickable(this).tickClient(this::tickClient).tickServer(this::tickServer));
		addComponent(new ComponentElectrodynamic(this, true, false).setOutputDirections(BlockEntityUtils.MachineDirection.BACK));
		addComponent(new ComponentInventory(this, InventoryBuilder.newInv().inputs(1)).setDirectionsBySlot(0, BlockEntityUtils.MachineDirection.TOP, BlockEntityUtils.MachineDirection.BOTTOM, BlockEntityUtils.MachineDirection.FRONT, BlockEntityUtils.MachineDirection.LEFT, BlockEntityUtils.MachineDirection.RIGHT)
				//
				.valid((index, stack, i) -> getValidItems().contains(stack.getItem())));
		addComponent(new ComponentContainerProvider(SubtypeMachine.coalgenerator, this).createMenu((id, player) -> new ContainerCoalGenerator(id, player, getComponent(IComponentType.Inventory), getCoordsArray())));
	}

	protected void tickServer(ComponentTickable tickable) {
		if (burnTime.get() > 0) {
			burnTime.set(burnTime.get() - 1);
		}
		if (hasRedstoneSignal.get()) {
			return;
		}
		Direction facing = getFacing();
		if (output == null) {
			output = new CachedTileOutput(level, worldPosition.relative(facing.getOpposite()));
		}
		if (tickable.getTicks() % 20 == 0) {
			output.update(worldPosition.relative(facing.getOpposite()));
		}
		ComponentInventory inv = getComponent(IComponentType.Inventory);
		ItemStack fuel = inv.getItem(0);
		if (burnTime.get() <= 0 && !fuel.isEmpty()) {
			burnTime.set(fuel.getBurnTime(null));
			fuel.shrink(1);
			maxBurnTime.set(Math.max(burnTime.get(), 1));
		}
		boolean greaterBurnTime = burnTime.get() > 0;
		if (BlockEntityUtils.isLit(this) ^ greaterBurnTime) {
			BlockEntityUtils.updateLit(this, greaterBurnTime);
		}
		if (heat.getValue() > 27 && output.valid()) {
			ElectricityUtils.receivePower(output.getSafe(), facing, currentOutput, false);
		}
		heat.rangeParameterize(27, 3000, burnTime.get() > 0 ? 3000 : 27, heat.getValue(), 600).flush();
		currentOutput = getProduced();
	}

	protected void tickClient(ComponentTickable tickable) {
		if (getBlockState().getValue(ElectrodynamicsBlockStates.LIT)) {
			Direction dir = getFacing();
			if (level.random.nextInt(10) == 0) {
				level.playLocalSound(worldPosition.getX() + 0.5D, worldPosition.getY() + 0.5D, worldPosition.getZ() + 0.5D, SoundEvents.CAMPFIRE_CRACKLE, SoundSource.BLOCKS, 0.5F + level.random.nextFloat(), level.random.nextFloat() * 0.7F + 0.6F, false);
			}

			if (level.random.nextInt(10) == 0) {
				for (int i = 0; i < level.random.nextInt(1) + 1; ++i) {
					level.addParticle(ParticleTypes.LAVA, worldPosition.getX() + 0.5D, worldPosition.getY() + 0.5D, worldPosition.getZ() + 0.5D, dir.getStepX(), 0.0, dir.getStepZ());
				}
			}
		}
	}

	@Override
	public double getMultiplier() {
		return multiplier.get();
	}

	@Override
	public void setMultiplier(double val) {
		multiplier.set(val);
	}

	@Override
	public TransferPack getProduced() {
		return TransferPack.ampsVoltage(multiplier.get() * Constants.COALGENERATOR_AMPERAGE * ((heat.getValue() - 27.0) / (3000.0 - 27.0)), ElectrodynamicsCapabilities.DEFAULT_VOLTAGE);
	}

	public static List<Item> getValidItems() {
		return new ArrayList<>(CoalGeneratorFuelRegister.INSTANCE.getFuels());
	}

	@Override
	public int getComparatorSignal() {
		return (int) ((heat.getValue() / 3000.0) * 15.0);
	}

	@Override
	public void onNeightborChanged(BlockPos neighbor, boolean blockStateTrigger) {
		if (level.isClientSide) {
			return;
		}
		hasRedstoneSignal.set(level.hasNeighborSignal(getBlockPos()));
	}

}
