package electrodynamics.common.block.connect;

import java.util.HashSet;

import com.mojang.serialization.MapCodec;

import electrodynamics.api.gas.Gas;
import electrodynamics.api.network.cable.IRefreshableCable;
import electrodynamics.api.network.cable.type.IGasPipe;
import electrodynamics.common.block.connect.util.AbstractRefreshingConnectBlock;
import electrodynamics.common.block.connect.util.EnumConnectType;
import electrodynamics.common.block.subtype.SubtypeGasPipe;
import electrodynamics.common.block.subtype.SubtypeGasPipe.InsulationMaterial;
import electrodynamics.common.network.type.GasNetwork;
import electrodynamics.common.network.utils.GasUtilities;
import electrodynamics.common.tile.pipelines.gas.TileGasPipe;
import electrodynamics.prefab.tile.types.GenericConnectTile;
import electrodynamics.prefab.utilities.Scheduler;
import electrodynamics.registers.ElectrodynamicsBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public class BlockGasPipe extends AbstractRefreshingConnectBlock {

    public static final HashSet<Block> PIPESET = new HashSet<>();

    public final SubtypeGasPipe pipe;

    public BlockGasPipe(SubtypeGasPipe pipe) {
        super(pipe.material.sound(pipe.soundType).strength(0.15f).dynamicShape().noOcclusion(), pipe.radius);

        this.pipe = pipe;

        PIPESET.add(this);

    }

    @Override
    public boolean isFlammable(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
        return pipe.insulationMaterial.canCombust;
    }

    @Override
    public int getFlammability(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
        if (!pipe.insulationMaterial.canCombust) {
            return 0;
        }

        return state.hasProperty(BlockStateProperties.WATERLOGGED) && state.getValue(BlockStateProperties.WATERLOGGED) ? 0 : 150;
    }

    @Override
    public int getFireSpreadSpeed(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
        if (!pipe.insulationMaterial.canCombust) {
            return 0;
        }

        return state.hasProperty(BlockStateProperties.WATERLOGGED) && state.getValue(BlockStateProperties.WATERLOGGED) ? 0 : 400;
    }

    @Override
    public void onCaughtFire(BlockState state, Level world, BlockPos pos, Direction face, LivingEntity igniter) {
        super.onCaughtFire(state, world, pos, face, igniter);
        Scheduler.schedule(5, () -> world.setBlockAndUpdate(pos, ElectrodynamicsBlocks.getBlock(SubtypeGasPipe.getPipeForType(pipe.pipeMaterial, InsulationMaterial.NONE)).defaultBlockState()));
    }

    @Override
    public void entityInside(BlockState state, Level worldIn, BlockPos pos, Entity entityIn) {
        TileGasPipe tile = (TileGasPipe) worldIn.getBlockEntity(pos);
        if (worldIn.isClientSide() || tile == null || tile.getNetwork() == null && tile.getNetwork().transmittedThisTick <= 0) {
            return;
        }
        GasNetwork network = tile.getNetwork();

        double multipler = pipe.insulationMaterial == InsulationMaterial.NONE ? 1.0 : 1.2;

        if (network.temperatureOfTransmitted >= Gas.MINIMUM_HEAT_BURN_TEMP * multipler) {

            entityIn.hurt(entityIn.damageSources().inFire(), 1.0F);

        } else if (network.temperatureOfTransmitted > 0 && network.temperatureOfTransmitted <= Gas.MINIMUM_FREEZE_TEMP / multipler) {

            entityIn.hurt(entityIn.damageSources().freeze(), 1.0F);

        }

    }

    @Override
    public BlockState refreshConnections(BlockState otherState, BlockEntity otherTile, BlockState state, BlockEntity thisTile, Direction dir) {
        if(!(thisTile instanceof GenericConnectTile)) {
            return state;
        }
        GenericConnectTile thisConnect = (GenericConnectTile) thisTile;
        EnumConnectType connection = EnumConnectType.NONE;
        if (otherTile instanceof IGasPipe) {
            connection = EnumConnectType.WIRE;
        } else if (GasUtilities.isGasReciever(otherTile, dir.getOpposite())) {
            connection = EnumConnectType.INVENTORY;
        }
        thisConnect.writeConnection(dir, connection);
        return state;
    }

    @Override
    public IRefreshableCable getCableIfValid(BlockEntity tile) {
        if (tile instanceof IGasPipe pipe) {
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
