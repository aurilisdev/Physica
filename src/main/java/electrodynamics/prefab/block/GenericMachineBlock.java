package electrodynamics.prefab.block;

import java.util.HashMap;

import javax.annotation.Nullable;

import com.mojang.serialization.MapCodec;

import electrodynamics.common.block.states.ElectrodynamicsBlockStates;
import electrodynamics.common.block.voxelshapes.VoxelShapeProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType.BlockEntitySupplier;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class GenericMachineBlock extends GenericEntityBlockWaterloggable {

    private final BlockEntitySupplier<BlockEntity> blockEntitySupplier;
    private final VoxelShapeProvider shapeProvider;

    public static HashMap<BlockPos, LivingEntity> IPLAYERSTORABLE_MAP = new HashMap<>();

    public GenericMachineBlock(BlockEntitySupplier<BlockEntity> blockEntitySupplier, VoxelShapeProvider provider) {
        super(Blocks.IRON_BLOCK.properties().strength(3.5F).sound(SoundType.METAL).noOcclusion().requiresCorrectToolForDrops());
        registerDefaultState(stateDefinition.any().setValue(ElectrodynamicsBlockStates.FACING, Direction.NORTH));
        this.blockEntitySupplier = blockEntitySupplier;
        this.shapeProvider = provider;
    }

    public GenericMachineBlock(BlockEntitySupplier<BlockEntity> blockEntitySupplier, boolean temp) {
        this(blockEntitySupplier, VoxelShapeProvider.DEFAULT);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        Direction dir = null;
        if (state.hasProperty(ElectrodynamicsBlockStates.FACING)) {

            dir = state.getValue(ElectrodynamicsBlockStates.FACING);

        }
        return shapeProvider.getShape(dir);
    }

    @Override
    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, @Nullable LivingEntity pPlacer, ItemStack pStack) {
        if (isIPlayerStorable()) {
            IPLAYERSTORABLE_MAP.put(pPos, pPlacer);
        }
        super.setPlacedBy(pLevel, pPos, pState, pPlacer, pStack);
    }

    @Override
    public final BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return blockEntitySupplier.create(pos, state);
    }

    @Override
    public float getShadeBrightness(BlockState state, BlockGetter worldIn, BlockPos pos) {
        return 1;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return super.getStateForPlacement(context).setValue(ElectrodynamicsBlockStates.FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(ElectrodynamicsBlockStates.FACING);
    }

    public boolean isIPlayerStorable() {
        return false;
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        throw new UnsupportedOperationException("Need to implement CODEC");
    }
}
