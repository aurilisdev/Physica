package electrodynamics.common.tile.pipelines.gas.gastransformer.compressor;

import electrodynamics.common.inventory.container.tile.ContainerCompressor;
import electrodynamics.common.inventory.container.tile.ContainerDecompressor;
import electrodynamics.common.settings.Constants;
import electrodynamics.common.tile.pipelines.gas.gastransformer.IAddonTankManager;
import electrodynamics.common.tile.pipelines.gas.gastransformer.TileGasTransformerAddonTank;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.tile.components.type.ComponentContainerProvider;
import electrodynamics.prefab.tile.components.type.ComponentGasHandlerMulti;
import electrodynamics.prefab.tile.components.type.ComponentProcessor;
import electrodynamics.prefab.tile.components.type.ComponentTickable;
import electrodynamics.prefab.utilities.BlockEntityUtils;
import electrodynamics.registers.ElectrodynamicsBlocks;
import electrodynamics.registers.ElectrodynamicsSounds;
import electrodynamics.registers.ElectrodynamicsTiles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class GenericTileBasicCompressor extends GenericTileCompressor implements IAddonTankManager {

    public GenericTileBasicCompressor(BlockEntityType<?> type, BlockPos worldPos, BlockState blockState) {
        super(type, worldPos, blockState);
    }

    @Override
    public void tickClient(ComponentTickable tickable) {

        super.tickClient(tickable);

        if (level.getRandom().nextDouble() < 0.15) {


            //TODO add particles?


        }

    }

    @Override
    public void updateTankCount() {
        BlockPos abovePos = getBlockPos().above();
        BlockState aboveState = getLevel().getBlockState(abovePos);
        BlockEntity aboveTile;
        int tankCount = 0;
        for (int i = 0; i < Constants.GAS_TRANSFORMER_ADDON_TANK_LIMIT; i++) {
            if (!aboveState.is(ElectrodynamicsBlocks.BLOCK_COMPRESSOR_ADDONTANK)) {
                break;
            }
            aboveTile = getLevel().getBlockEntity(abovePos);
            if ((aboveTile == null) || !(aboveTile instanceof TileGasTransformerAddonTank tank)) {
                break;
            }
            abovePos = abovePos.above();
            aboveState = getLevel().getBlockState(abovePos);
            tank.setOwnerPos(getBlockPos());
            tankCount++;
        }
        ComponentGasHandlerMulti handler = getComponent(IComponentType.GasHandler);
        handler.getInputTanks()[0].setCapacity(Constants.GAS_TRANSFORMER_BASE_INPUT_CAPCITY + Constants.GAS_TRANSFORMER_ADDON_TANK_CAPCITY * tankCount);
        handler.getOutputTanks()[0].setCapacity(Constants.GAS_TRANSFORMER_BASE_OUTPUT_CAPCITY + Constants.GAS_TRANSFORMER_ADDON_TANK_CAPCITY * tankCount);
    }

    @Override
    public void outputToPipe(ComponentProcessor processor, ComponentGasHandlerMulti multi, Direction facing) {
        processor.outputToGasPipe();
    }

    @Override
    public void updateLit(boolean isHeating, Direction facing) {
        if (BlockEntityUtils.isLit(this) ^ isHeating) {
            BlockEntityUtils.updateLit(this, isHeating);
        }
    }

    public static class TileCompressor extends GenericTileBasicCompressor {
        public TileCompressor(BlockPos worldPos, BlockState blockState) {
            super(ElectrodynamicsTiles.TILE_COMPRESSOR.get(), worldPos, blockState);
        }

        @Override
        public double getPressureMultiplier() {
            return 2;
        }

        @Override
        public SoundEvent getSound() {
            return ElectrodynamicsSounds.SOUND_COMPRESSORRUNNING.get();
        }

        @Override
        public ComponentContainerProvider getContainerProvider() {
            return new ComponentContainerProvider("container.compressor", this).createMenu((id, inv) -> new ContainerCompressor(id, inv, getComponent(IComponentType.Inventory), getCoordsArray()));
        }

        @Override
        public double getUsagePerTick() {
            return Constants.COMPRESSOR_USAGE_PER_TICK;
        }

        @Override
        public int getConversionRate() {
            return Constants.COMPRESSOR_CONVERSION_RATE;
        }
    }

    public static class TileDecompressor extends GenericTileBasicCompressor {
        public TileDecompressor(BlockPos worldPos, BlockState blockState) {
            super(ElectrodynamicsTiles.TILE_DECOMPRESSOR.get(), worldPos, blockState);
        }

        @Override
        public double getPressureMultiplier() {
            return 0.5;
        }

        @Override
        public SoundEvent getSound() {
            return ElectrodynamicsSounds.SOUND_DECOMPRESSORRUNNING.get();
        }

        @Override
        public ComponentContainerProvider getContainerProvider() {
            return new ComponentContainerProvider("container.decompressor", this).createMenu((id, inv) -> new ContainerDecompressor(id, inv, getComponent(IComponentType.Inventory), getCoordsArray()));
        }

        @Override
        public double getUsagePerTick() {
            return Constants.DECOMPRESSOR_USAGE_PER_TICK;
        }

        @Override
        public int getConversionRate() {
            return Constants.DECOMPRESSOR_CONVERSION_RATE;
        }
    }

}
