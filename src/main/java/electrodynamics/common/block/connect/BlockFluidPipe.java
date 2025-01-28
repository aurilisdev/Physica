package electrodynamics.common.block.connect;

import java.util.HashSet;

import com.mojang.serialization.MapCodec;

import electrodynamics.api.network.cable.type.IFluidPipe;
import electrodynamics.common.block.connect.util.AbstractRefreshingConnectBlock;
import electrodynamics.common.block.connect.util.EnumConnectType;
import electrodynamics.common.network.utils.FluidUtilities;
import electrodynamics.common.tile.pipelines.fluid.GenericTileFluidPipe;
import electrodynamics.common.tile.pipelines.fluid.TileFluidPipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class BlockFluidPipe extends AbstractRefreshingConnectBlock<GenericTileFluidPipe> {

    public static final HashSet<Block> PIPESET = new HashSet<>();

    public final IFluidPipe pipe;

    public BlockFluidPipe(IFluidPipe pipe) {
        super(Blocks.IRON_BLOCK.properties().sound(SoundType.METAL).strength(0.15f).dynamicShape().noOcclusion(), 3);
        this.pipe = pipe;
        PIPESET.add(this);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TileFluidPipe(pos, state);
    }

    @Override
    public EnumConnectType getConnection(BlockState otherState, BlockEntity otherTile, GenericTileFluidPipe thisConductor, Direction dir) {
        EnumConnectType connection = EnumConnectType.NONE;
        if (otherTile instanceof GenericTileFluidPipe) {
            connection = EnumConnectType.WIRE;
        } else if (FluidUtilities.isFluidReceiver(otherTile, dir.getOpposite())) {
            connection = EnumConnectType.INVENTORY;
        }
        return connection;
    }

    @Override
    public GenericTileFluidPipe getCableIfValid(BlockEntity tile) {
        if (tile instanceof GenericTileFluidPipe pipe) {
            return pipe;
        }
        return null;
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        throw new UnsupportedOperationException("Need to implement CODEC");
    }

}
