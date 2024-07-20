package electrodynamics.common.block;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javax.annotation.Nullable;

import com.google.common.collect.Maps;
import com.mojang.serialization.MapCodec;

import electrodynamics.common.block.connect.util.EnumConnectType;
import electrodynamics.common.tile.machines.quarry.TileLogisticalManager;
import electrodynamics.prefab.block.GenericEntityBlockWaterloggable;
import electrodynamics.prefab.tile.types.IConnectTile;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * INVENTORY corresponds to a chest WIRE corresponds to a Quarry
 * 
 * @author skip999
 *
 */
public class BlockLogisticalManager extends GenericEntityBlockWaterloggable {

    protected final VoxelShape[] boundingBoxes = new VoxelShape[7];

    protected HashMap<EnumConnectType[], VoxelShape> shapestates = new HashMap<>();


    public BlockLogisticalManager() {
        super(Blocks.IRON_BLOCK.properties().strength(3.5F).sound(SoundType.METAL).noOcclusion().requiresCorrectToolForDrops());
        generateBoundingBoxes(3);
    }

    public void generateBoundingBoxes(double radius) {
        double w = radius;
        double sm = 8 - w;
        double lg = 8 + w;
        // down
        boundingBoxes[0] = Block.box(sm, 0, sm, lg, lg, lg);
        // up
        boundingBoxes[1] = Block.box(sm, sm, sm, lg, 16, lg);
        // north
        boundingBoxes[2] = Block.box(sm, sm, 0, lg, lg, lg);
        // south
        boundingBoxes[3] = Block.box(sm, sm, sm, lg, lg, 16);
        // west
        boundingBoxes[4] = Block.box(0, sm, sm, lg, lg, lg);
        // east
        boundingBoxes[5] = Block.box(sm, sm, sm, 16, lg, lg);
        // center
        boundingBoxes[6] = Block.box(sm, sm, sm, lg, lg, lg);
    }


    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        VoxelShape shape = boundingBoxes[6];
        EnumConnectType[] checked = new EnumConnectType[6];

        BlockEntity entity = level.getBlockEntity(pos);

        if(!(entity instanceof IConnectTile)) {
            return Shapes.empty();
        }

        EnumConnectType[] connections = ((IConnectTile) entity).readConnections();

        for (int i = 0; i < 6; i++) {
            EnumConnectType connection = connections[i];

            if (connection != EnumConnectType.NONE) {
                checked[i] = connection;
            }

        }
        if (shapestates.containsKey(checked)) {
            return shapestates.get(checked);
        }
        for (int i = 0; i < 6; i++) {

            EnumConnectType connection = checked[i];

            if(connection == null) {
                continue;
            }

            shape = Shapes.join(shape, boundingBoxes[i], BooleanOp.OR);
        }
        shapestates.put(checked, shape);
        if (shape == null) {
            return Shapes.empty();
        }
        return shape;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TileLogisticalManager(pos, state);
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
        return true;
    }


    @Override
    public void setPlacedBy(Level worldIn, BlockPos pos, BlockState stateIn, @Nullable LivingEntity placer, ItemStack stack) {
        if (worldIn.isClientSide) {
            return;
        }
        BlockState currentState = stateIn;
        TileLogisticalManager tile = (TileLogisticalManager) worldIn.getBlockEntity(pos);
        if (tile == null) {
            return;
        }
        for (Direction dir : Direction.values()) {
            if (TileLogisticalManager.isQuarry(pos.relative(dir), worldIn)) {
                tile.writeConnection(dir, EnumConnectType.WIRE);
            } else if (TileLogisticalManager.isValidInventory(pos.relative(dir), worldIn, dir.getOpposite())) {
                tile.writeConnection(dir, EnumConnectType.INVENTORY);
            }
        }
        worldIn.setBlockAndUpdate(pos, currentState);
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor world, BlockPos currentPos, BlockPos facingPos) {
        stateIn = super.updateShape(stateIn, facing, facingState, world, currentPos, facingPos);
        TileLogisticalManager tile = (TileLogisticalManager) world.getBlockEntity(currentPos);
        EnumConnectType connection = EnumConnectType.NONE;
        if (tile == null) {
            return stateIn;
        }
        if (TileLogisticalManager.isQuarry(facingPos, world)) {
            connection = EnumConnectType.WIRE;
        } else if (TileLogisticalManager.isValidInventory(facingPos, world, facing.getOpposite())) {
            connection = EnumConnectType.INVENTORY;
        }
        tile.writeConnection(facing, connection);
        return stateIn;
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        throw new UnsupportedOperationException("Need to implement CODEC");
    }

}
