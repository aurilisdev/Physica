package electrodynamics.common.tile.electricitygrid.generators;

import electrodynamics.api.multiblock.subnodebased.parent.IMultiblockParentBlock;
import electrodynamics.api.multiblock.subnodebased.parent.IMultiblockParentTile;
import electrodynamics.common.block.subtype.SubtypeMachine;
import electrodynamics.common.inventory.container.tile.ContainerWindmill;
import electrodynamics.common.item.subtype.SubtypeItemUpgrade;
import electrodynamics.common.settings.Constants;
import electrodynamics.common.tile.TileMultiSubnode;
import electrodynamics.prefab.properties.Property;
import electrodynamics.prefab.properties.PropertyTypes;
import electrodynamics.prefab.sound.SoundBarrierMethods;
import electrodynamics.prefab.sound.utils.ITickableSound;
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
import electrodynamics.registers.ElectrodynamicsSounds;
import electrodynamics.registers.ElectrodynamicsTiles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class TileWindmill extends GenericGeneratorTile implements IMultiblockParentTile, ITickableSound {

	protected CachedTileOutput output;
	private Property<Boolean> isGenerating = property(new Property<>(PropertyTypes.BOOLEAN, "isGenerating", false));
	public Property<Boolean> directionFlag = property(new Property<>(PropertyTypes.BOOLEAN, "directionFlag", false));
	public Property<Double> generating = property(new Property<>(PropertyTypes.DOUBLE, "generating", 0.0));
	private Property<Double> multiplier = property(new Property<>(PropertyTypes.DOUBLE, "multiplier", 1.0));
	private Property<Boolean> hasRedstoneSignal = property(new Property<>(PropertyTypes.BOOLEAN, "redstonesignal", false));
	public double savedTickRotation;
	public double rotationSpeed;

	private boolean isSoundPlaying = false;

	public TileWindmill(BlockPos worldPosition, BlockState blockState) {
		super(ElectrodynamicsTiles.TILE_WINDMILL.get(), worldPosition, blockState, 2.25, SubtypeItemUpgrade.stator);
		addComponent(new ComponentPacketHandler(this));
		addComponent(new ComponentTickable(this).tickServer(this::tickServer).tickCommon(this::tickCommon).tickClient(this::tickClient));
		addComponent(new ComponentElectrodynamic(this, true, false).setOutputDirections(BlockEntityUtils.MachineDirection.BOTTOM));
		addComponent(new ComponentInventory(this, InventoryBuilder.newInv().upgrades(1)).validUpgrades(ContainerWindmill.VALID_UPGRADES).valid(machineValidator()));
		addComponent(new ComponentContainerProvider(SubtypeMachine.windmill, this).createMenu((id, player) -> new ContainerWindmill(id, player, getComponent(IComponentType.Inventory), getCoordsArray())));

	}

	protected void tickServer(ComponentTickable tickable) {
		if (hasRedstoneSignal.get()) {
			generating.set(false);
			return;
		}
		Direction facing = getFacing();
		if (tickable.getTicks() % 40 == 0) {
			isGenerating.set(level.isEmptyBlock(worldPosition.relative(facing).relative(Direction.UP)));
			float height = Math.max(0, level.getHeight());
			double f = Math.log10((Math.max(0, getBlockPos().getY()) + height / 10.0) * 10.0 / height);
			generating.set(Constants.WINDMILL_MAX_AMPERAGE * Mth.clamp(f, 0, 1));
		}
		if (output == null) {
			output = new CachedTileOutput(level, worldPosition.relative(Direction.DOWN));
		}
		output.update(worldPosition.relative(Direction.DOWN));
		if (isGenerating.get() && output.valid()) {
			ElectricityUtils.receivePower(output.getSafe(), Direction.UP, getProduced(), false);
		}
	}

	protected void tickCommon(ComponentTickable tickable) {
		savedTickRotation += (directionFlag.get() ? 1 : -1) * rotationSpeed;
		rotationSpeed = Mth.clamp(rotationSpeed + 0.05 * (isGenerating.get() ? 1 : -1), 0.0, 1.0);
	}

	protected void tickClient(ComponentTickable tickable) {
		if (shouldPlaySound() && !isSoundPlaying) {
			isSoundPlaying = true;
			SoundBarrierMethods.playTileSound(ElectrodynamicsSounds.SOUND_HUM.get(), this, true);
		}
	}

	@Override
	public void onNeightborChanged(BlockPos neighbor, boolean blockStateTrigger) {
		if (level.isClientSide) {
			return;
		}
		hasRedstoneSignal.set(level.hasNeighborSignal(getBlockPos()));
	}

	@Override
	public void setNotPlaying() {
		isSoundPlaying = false;
	}

	@Override
	public boolean shouldPlaySound() {
		return isGenerating.get();
	}

	@Override
	public IMultiblockParentBlock.SubnodeWrapper getSubNodes() {
		return SubtypeMachine.Subnodes.WINDMILL;
	}

	@Override
	public void setMultiplier(double val) {
		multiplier.set(val);
	}

	@Override
	public double getMultiplier() {
		return multiplier.get();
	}

	@Override
	public TransferPack getProduced() {
		return TransferPack.ampsVoltage(generating.get() * multiplier.get(), this.<ComponentElectrodynamic>getComponent(IComponentType.Electrodynamic).getVoltage());
	}

	@Override
	public int getComparatorSignal() {
		return isGenerating.get() ? 15 : 0;
	}

	@Override
	public void onSubnodeDestroyed(TileMultiSubnode subnode) {
		level.destroyBlock(worldPosition, true);
	}

	@Override
	public int getSubdnodeComparatorSignal(TileMultiSubnode subnode) {
		return getComparatorSignal();
	}

	@Override
	public ItemInteractionResult onSubnodeUseWithItem(ItemStack used, Player player, InteractionHand hand, BlockHitResult hit, TileMultiSubnode subnode) {
		return useWithItem(used, player, hand, hit);
	}

	@Override
	public InteractionResult onSubnodeUseWithoutItem(Player player, BlockHitResult hit, TileMultiSubnode subnode) {
		return useWithoutItem(player, hit);
	}

	@Override
	public Direction getFacingDirection() {
		return getFacing();
	}

}
