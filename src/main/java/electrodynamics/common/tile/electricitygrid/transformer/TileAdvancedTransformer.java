package electrodynamics.common.tile.electricitygrid.transformer;

import electrodynamics.common.block.subtype.SubtypeMachine;
import electrodynamics.common.inventory.container.tile.ContainerAdvancedDowngradeTransformer;
import electrodynamics.common.inventory.container.tile.ContainerAdvancedUpgradeTransformer;
import electrodynamics.prefab.properties.Property;
import electrodynamics.prefab.properties.PropertyTypes;
import electrodynamics.prefab.tile.components.type.ComponentContainerProvider;
import electrodynamics.registers.ElectrodynamicsTiles;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class TileAdvancedTransformer extends TileGenericTransformer {

	public final Property<Double> coilRatio;

	public final double defaultCoilRatio;

	public TileAdvancedTransformer(BlockEntityType<?> type, BlockPos worldPosition, BlockState blockState, double defaultCoilRatio) {
		super(type, worldPosition, blockState);
		coilRatio = property(new Property<>(PropertyTypes.DOUBLE, "coilratio", defaultCoilRatio));
		this.defaultCoilRatio = defaultCoilRatio;
	}

	// This version is enclosed and won't shock but more expensive
	@Override
	public void onEntityInside(BlockState state, Level level, BlockPos pos, Entity entity) {

	}

	@Override
	public double getCoilRatio() {
		return coilRatio.get() <= 0 ? defaultCoilRatio : coilRatio.get();
	}

	public static final class TileAdvancedDowngradeTransformer extends TileAdvancedTransformer {

		public TileAdvancedDowngradeTransformer(BlockPos worldPosition, BlockState blockState) {
			super(ElectrodynamicsTiles.TILE_ADVANCEDDOWNGRADETRANSFORMER.get(), worldPosition, blockState, 0.5);
			addComponent(new ComponentContainerProvider(SubtypeMachine.advanceddowngradetransformer, this).createMenu((id, playerinv) -> new ContainerAdvancedDowngradeTransformer(id, playerinv, getCoordsArray())));
		}

	}

	public static final class TileAdvancedUpgradeTransformer extends TileAdvancedTransformer {

		public TileAdvancedUpgradeTransformer(BlockPos worldPosition, BlockState blockState) {
			super(ElectrodynamicsTiles.TILE_ADVANCEDUPGRADETRANSFORMER.get(), worldPosition, blockState, 2.0);
			addComponent(new ComponentContainerProvider(SubtypeMachine.advancedupgradetransformer, this).createMenu((id, playerinv) -> new ContainerAdvancedUpgradeTransformer(id, playerinv, getCoordsArray())));
		}

	}

}
