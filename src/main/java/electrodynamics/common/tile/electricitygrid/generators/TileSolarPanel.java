package electrodynamics.common.tile.electricitygrid.generators;

import electrodynamics.common.block.subtype.SubtypeMachine;
import electrodynamics.common.inventory.container.tile.ContainerSolarPanel;
import electrodynamics.common.item.subtype.SubtypeItemUpgrade;
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
import electrodynamics.prefab.utilities.object.TransferPack;
import electrodynamics.registers.ElectrodynamicsTiles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class TileSolarPanel extends GenericGeneratorTile {

	protected CachedTileOutput output;

	protected Property<Boolean> generating = property(new Property<>(PropertyTypes.BOOLEAN, "generating", false));
	protected Property<Double> multiplier = property(new Property<>(PropertyTypes.DOUBLE, "multiplier", 1.0));
	protected Property<Boolean> hasRedstoneSignal = property(new Property<>(PropertyTypes.BOOLEAN, "redstonesignal", false));

	@Override
	public double getMultiplier() {
		return multiplier.get();
	}

	@Override
	public void setMultiplier(double val) {
		multiplier.set(val);
	}

	public TileSolarPanel(BlockPos worldPosition, BlockState blockState) {
		this(ElectrodynamicsTiles.TILE_SOLARPANEL.get(), worldPosition, blockState, 2.25, SubtypeItemUpgrade.improvedsolarcell);
	}

	public TileSolarPanel(BlockEntityType<?> type, BlockPos worldPosition, BlockState blockState, double multiplier, SubtypeItemUpgrade... itemUpgrades) {
		super(type, worldPosition, blockState, multiplier, itemUpgrades);
		addComponent(new ComponentTickable(this).tickServer(this::tickServer));
		addComponent(new ComponentPacketHandler(this));
		addComponent(new ComponentElectrodynamic(this, true, false).setOutputDirections(BlockEntityUtils.MachineDirection.BOTTOM));
		addComponent(new ComponentInventory(this, InventoryBuilder.newInv().upgrades(1)).validUpgrades(ContainerSolarPanel.VALID_UPGRADES).valid(machineValidator()));
		addComponent(new ComponentContainerProvider(SubtypeMachine.solarpanel, this).createMenu((id, player) -> new ContainerSolarPanel(id, player, getComponent(IComponentType.Inventory), getCoordsArray())));
	}

	protected void tickServer(ComponentTickable tickable) {
		if (hasRedstoneSignal.get()) {
			generating.set(false);
			return;
		}
		if (output == null) {
			output = new CachedTileOutput(level, worldPosition.relative(Direction.DOWN));
		}
		if (tickable.getTicks() % 40 == 0) {
			output.update(worldPosition.relative(Direction.DOWN));
			generating.set(level.canSeeSky(worldPosition.offset(0, 1, 0)));
		}
		if (level.isDay() && generating.get() && output.valid()) {
			ElectricityUtils.receivePower(output.getSafe(), Direction.UP, getProduced(), false);
		}
	}

	@Override
	public TransferPack getProduced() {
		double mod = 1.0f - Mth.clamp(1.0F - (Mth.cos(level.getTimeOfDay(1f) * ((float) Math.PI * 2f)) * 2.0f + 0.2f), 0.0f, 1.0f);
		double temp = level.getBiomeManager().getBiome(getBlockPos()).value().getBaseTemperature();
		double lerped = Mth.lerp((temp + 1) / 3.0, 1.5, 3) / 3.0;
		return TransferPack.ampsVoltage(getMultiplier() * Constants.SOLARPANEL_AMPERAGE * lerped * mod * (level.isRaining() || level.isThundering() ? 0.8f : 1), this.<ComponentElectrodynamic>getComponent(IComponentType.Electrodynamic).getVoltage());
	}

	@Override
	public int getComparatorSignal() {
		return generating.get() ? 15 : 0;
	}

	@Override
	public void onNeightborChanged(BlockPos neighbor, boolean blockStateTrigger) {
		if (level.isClientSide) {
			return;
		}
		hasRedstoneSignal.set(level.hasNeighborSignal(getBlockPos()));
	}

}
