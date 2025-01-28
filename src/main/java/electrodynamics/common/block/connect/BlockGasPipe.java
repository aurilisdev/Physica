package electrodynamics.common.block.connect;

import java.util.HashSet;

import com.mojang.serialization.MapCodec;

import electrodynamics.api.gas.Gas;
import electrodynamics.api.network.cable.type.IGasPipe;
import electrodynamics.common.block.connect.util.AbstractRefreshingConnectBlock;
import electrodynamics.common.block.connect.util.EnumConnectType;
import electrodynamics.common.network.type.GasNetwork;
import electrodynamics.common.network.utils.GasUtilities;
import electrodynamics.common.tile.pipelines.gas.GenericTileGasPipe;
import electrodynamics.common.tile.pipelines.gas.TileGasPipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class BlockGasPipe extends AbstractRefreshingConnectBlock<GenericTileGasPipe> {

    public static final HashSet<Block> PIPESET = new HashSet<>();

    public final IGasPipe pipe;

    public BlockGasPipe(IGasPipe pipe) {
        super(pipe.getProperties().sound(pipe.getSoundType()).strength(0.15f).dynamicShape().noOcclusion(), pipe.getRadius());

        this.pipe = pipe;

        PIPESET.add(this);

    }

    /*
    @Override
    public boolean isFlammable(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
        return pipe.insulationMaterial.canCombust;
    }

    @Override
    public int getFlammability(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
        if (!pipe.insulationMaterial.canCombust) {
            return 0;
        }

        return state.hasProperty(ElectrodynamicsBlockStates.WATERLOGGED) && state.getValue(ElectrodynamicsBlockStates.WATERLOGGED) ? 0 : 150;
    }

    @Override
    public int getFireSpreadSpeed(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
        if (!pipe.insulationMaterial.canCombust) {
            return 0;
        }

        return state.hasProperty(ElectrodynamicsBlockStates.WATERLOGGED) && state.getValue(ElectrodynamicsBlockStates.WATERLOGGED) ? 0 : 400;
    }

    @Override
    public void onCaughtFire(BlockState state, Level world, BlockPos pos, Direction face, LivingEntity igniter) {
        super.onCaughtFire(state, world, pos, face, igniter);
        Scheduler.schedule(5, () -> world.setBlockAndUpdate(pos, ElectrodynamicsBlocks.BLOCKS_GASPIPE.getValue(SubtypeGasPipe.getPipeForType(pipe.pipeMaterial, InsulationMaterial.NONE)).defaultBlockState()));
    }

     */

    @Override
    public void entityInside(BlockState state, Level worldIn, BlockPos pos, Entity entityIn) {
        TileGasPipe tile = (TileGasPipe) worldIn.getBlockEntity(pos);
        if (worldIn.isClientSide() || tile == null || tile.getNetwork() == null && tile.getNetwork().transmittedThisTick <= 0) {
            return;
        }
        GasNetwork network = tile.getNetwork();

        double multipler = 1.0;//pipe.insulationMaterial == InsulationMaterial.NONE ? 1.0 : 1.2;

        if (network.temperatureOfTransmitted >= Gas.MINIMUM_HEAT_BURN_TEMP * multipler) {

            entityIn.hurt(entityIn.damageSources().inFire(), 1.0F);

        } else if (network.temperatureOfTransmitted > 0 && network.temperatureOfTransmitted <= Gas.MINIMUM_FREEZE_TEMP / multipler) {

            entityIn.hurt(entityIn.damageSources().freeze(), 1.0F);

        }

    }

    @Override
    public EnumConnectType getConnection(BlockState otherState, BlockEntity otherTile, GenericTileGasPipe thisConductor, Direction dir) {
        EnumConnectType connection = EnumConnectType.NONE;
        if (otherTile instanceof GenericTileGasPipe) {
            connection = EnumConnectType.WIRE;
        } else if (GasUtilities.isGasReciever(otherTile, dir.getOpposite())) {
            connection = EnumConnectType.INVENTORY;
        }
        return connection;
    }

    @Override
    public GenericTileGasPipe getCableIfValid(BlockEntity tile) {
        if (tile instanceof GenericTileGasPipe pipe) {
            return pipe;
        }
        return null;
    }


    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TileGasPipe(pos, state);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        throw new UnsupportedOperationException("Need to implement CODEC");
    }

}
