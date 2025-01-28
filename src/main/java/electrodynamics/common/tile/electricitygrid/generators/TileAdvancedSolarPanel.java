package electrodynamics.common.tile.electricitygrid.generators;

import electrodynamics.api.multiblock.subnodebased.parent.IMultiblockParentBlock;
import electrodynamics.api.multiblock.subnodebased.parent.IMultiblockParentTile;
import electrodynamics.common.block.subtype.SubtypeMachine;
import electrodynamics.common.inventory.container.tile.ContainerSolarPanel;
import electrodynamics.common.item.subtype.SubtypeItemUpgrade;
import electrodynamics.common.settings.Constants;
import electrodynamics.common.tile.TileMultiSubnode;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.tile.components.type.ComponentContainerProvider;
import electrodynamics.prefab.tile.components.type.ComponentElectrodynamic;
import electrodynamics.prefab.utilities.object.TargetValue;
import electrodynamics.prefab.utilities.object.TransferPack;
import electrodynamics.registers.ElectrodynamicsCapabilities;
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

public class TileAdvancedSolarPanel extends TileSolarPanel implements IMultiblockParentTile {

	public final TargetValue currentRotation = new TargetValue();

	public TileAdvancedSolarPanel(BlockPos worldPosition, BlockState blockState) {
		super(ElectrodynamicsTiles.TILE_ADVANCEDSOLARPANEL.get(), worldPosition, blockState, 2.25, SubtypeItemUpgrade.improvedsolarcell);
		this.<ComponentElectrodynamic>getComponent(IComponentType.Electrodynamic).voltage(ElectrodynamicsCapabilities.DEFAULT_VOLTAGE * 2);
		forceComponent(new ComponentContainerProvider(SubtypeMachine.advancedsolarpanel, this).createMenu((id, player) -> new ContainerSolarPanel(id, player, getComponent(IComponentType.Inventory), getCoordsArray())));
	}

	@Override
	public TransferPack getProduced() {
		double mod = 1.0f - Mth.clamp(1.0F - (Mth.cos(level.getTimeOfDay(1f) * ((float) Math.PI * 2f)) * 2.0f + 0.2f), 0.0f, 1.0f);
		double temp = level.getBiomeManager().getBiome(getBlockPos()).value().getBaseTemperature();
		double lerped = Mth.lerp((temp + 1) / 3.0, 1.5, 3) / 3.0;
		return TransferPack.ampsVoltage(getMultiplier() * Constants.ADVANCEDSOLARPANEL_AMPERAGE * lerped * mod * (level.isRaining() || level.isThundering() ? 0.8f : 1), this.<ComponentElectrodynamic>getComponent(IComponentType.Electrodynamic).getVoltage());
	}

	@Override
	public IMultiblockParentBlock.SubnodeWrapper getSubNodes() {
		return SubtypeMachine.Subnodes.ADVANCEDSOLARPANEL;
	}

	@Override
	public void onSubnodeDestroyed(TileMultiSubnode subnode) {
		level.destroyBlock(worldPosition, true);
	}

	@Override
	public InteractionResult onSubnodeUseWithoutItem(Player player, BlockHitResult hit, TileMultiSubnode subnode) {
		return useWithoutItem(player, hit);
	}

	@Override
	public ItemInteractionResult onSubnodeUseWithItem(ItemStack used, Player player, InteractionHand hand, BlockHitResult hit, TileMultiSubnode subnode) {
		return useWithItem(used, player, hand, hit);
	}

	@Override
	public int getSubdnodeComparatorSignal(TileMultiSubnode subnode) {
		return getComparatorSignal();
	}

	@Override
	public Direction getFacingDirection() {
		return getFacing();
	}

}
