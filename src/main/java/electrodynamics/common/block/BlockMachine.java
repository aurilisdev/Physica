package electrodynamics.common.block;

import com.mojang.serialization.MapCodec;

import electrodynamics.api.multiblock.Subnode;
import electrodynamics.api.multiblock.parent.IMultiblockParentBlock;
import electrodynamics.api.multiblock.parent.IMultiblockParentTile;
import electrodynamics.common.block.subtype.SubtypeMachine;
import electrodynamics.common.tile.machines.quarry.TileQuarry;
import electrodynamics.prefab.block.GenericMachineBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockMachine extends GenericMachineBlock implements IMultiblockParentBlock {

    public static final BooleanProperty ON = BlockStateProperties.LIT;
    public static final IntegerProperty BATTERY_CHARGE = BlockStateProperties.AGE_7;

    public static final VoxelShape[] STANDARD_CUBE = new VoxelShape[] { Shapes.block(), Shapes.block(), Shapes.block(), Shapes.block(), Shapes.block(), Shapes.block() };

    public static final Subnode[] advancedsolarpanelsubnodes = new Subnode[9];
    public static final Subnode[] windmillsubnodes = { new Subnode(new BlockPos(0, 1, 0),
            new VoxelShape[] { Shapes.block(), Shapes.block(), Shapes.or(Block.box(5, 0, 5, 11, 16, 11), Block.box(5, 10, 3, 11, 16, 13)), Shapes.or(Block.box(5, 0, 5, 11, 16, 11), Block.box(5, 10, 3, 11, 16, 13)), Shapes.or(Block.box(5, 0, 5, 11, 16, 11), Block.box(3, 10, 5, 13, 16, 11)), Shapes.or(Block.box(5, 0, 5, 11, 16, 11), Block.box(3, 10, 5, 13, 16, 11)) }) };
    static {

        int counter = 0;

        int radius = 1;

        for (int i = -radius; i <= radius; i++) {

            for (int j = -radius; j <= radius; j++) {
                if (i == 0 && j == 0) {
                    advancedsolarpanelsubnodes[counter] = new Subnode(new BlockPos(i, 1, j), Shapes.or(Block.box(6, 0, 6, 10, 16, 10), Block.box(5, 13, 5, 11, 16, 11), Block.box(0, 14, 0, 16, 16, 16)));
                } else {
                    advancedsolarpanelsubnodes[counter] = new Subnode(new BlockPos(i, 1, j), Block.box(0, 14, 0, 16, 16, 16));
                }

                counter++;
            }
        }

    }

    public final SubtypeMachine machine;

    public BlockMachine(SubtypeMachine machine) {
        super(machine::createTileEntity);
        this.machine = machine;
        if (machine.litBrightness > 0) {
            registerDefaultState(stateDefinition.any().setValue(ON, false));
        }

    }

    @Override
    public boolean propagatesSkylightDown(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        if (machine.propogateLightDown) {
            return true;
        }
        return super.propagatesSkylightDown(pState, pLevel, pPos);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {

        if (machine == SubtypeMachine.advancedsolarpanel) {
            return isValidMultiblockPlacement(state, worldIn, pos, advancedsolarpanelsubnodes);
        }
        if (machine == SubtypeMachine.windmill) {
            return isValidMultiblockPlacement(state, worldIn, pos, windmillsubnodes);
        }
        return super.canSurvive(state, worldIn, pos);

    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return machine.getRenderType();
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter world, BlockPos pos) {

        if (machine.litBrightness > 0 && state.hasProperty(ON) && state.getValue(ON)) {
            return machine.litBrightness;
        }

        return super.getLightEmission(state, world, pos);
    }

    @Override
    public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(worldIn, pos, state, placer, stack);
        BlockEntity tile = worldIn.getBlockEntity(pos);
        if (hasMultiBlock() && tile instanceof IMultiblockParentTile multi) {
            multi.onNodePlaced(worldIn, pos, state, placer, stack);
        }
    }

    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        BlockEntity tile = worldIn.getBlockEntity(pos);
        if (!(state.getBlock() == newState.getBlock() && state.getValue(FACING) != newState.getValue(FACING))) {

            if (tile instanceof IMultiblockParentTile multi) {
                multi.onNodeReplaced(worldIn, pos, true);
            }
            if (tile instanceof TileQuarry quarry && quarry.hasCorners()) {
                quarry.handleFramesDecay();
            }
        }
        if (newState.isAir()) {
            if (tile instanceof IMultiblockParentTile multi) {
                multi.onNodeReplaced(worldIn, pos, false);
            }
        }

        super.onRemove(state, worldIn, pos, newState, isMoving);

    }

    @Override
    public boolean hasMultiBlock() {
        return machine == SubtypeMachine.advancedsolarpanel || machine == SubtypeMachine.windmill;
    }

    @Override
    public boolean isIPlayerStorable() {
        return machine.isPlayerStorable();
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, LevelReader level, BlockPos pos, Player player) {
        ItemStack stack = super.getCloneItemStack(state, target, level, pos, player);
        BlockEntity tile = level.getBlockEntity(pos);
        if (tile != null) {
            tile.saveToItem(stack, level.registryAccess());
        }
        return stack;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return super.getStateForPlacement(context).setValue(ON, false);
    }

    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(ON);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        throw new UnsupportedOperationException("Need to implement CODEC");
    }
}
