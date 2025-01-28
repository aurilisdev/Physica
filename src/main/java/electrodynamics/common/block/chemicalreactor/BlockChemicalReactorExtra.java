package electrodynamics.common.block.chemicalreactor;

import java.util.Collections;
import java.util.List;

import electrodynamics.common.block.voxelshapes.VoxelShapeProvider;
import electrodynamics.common.tile.machines.chemicalreactor.TileChemicalReactorDummy;
import electrodynamics.prefab.block.GenericMachineBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

/**
 * We have to do this because of how Mojank handles rendering
 */
public class BlockChemicalReactorExtra extends GenericMachineBlock {

    public final Location loc;
    public BlockChemicalReactorExtra(Location loc) {
        super(TileChemicalReactorDummy::new, VoxelShapeProvider.DEFAULT);
        this.loc = loc;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return loc == Location.MIDDLE ? RenderShape.MODEL : super.getRenderShape(state);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        return Collections.emptyList();
    }

    public static enum Location {
        MIDDLE(1), TOP(2);

        public final Vec3i offsetDownToParent;
        public final Vec3i offsetUpFromParent;

        private Location(int y){
            offsetDownToParent = new Vec3i(0, -y, 0);
            offsetUpFromParent = new Vec3i(0, y, 0);
        }
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, LevelReader level, BlockPos pos, Player player) {
        BlockPos offset = pos.offset(loc.offsetDownToParent);
        return level.getBlockState(offset).getCloneItemStack(new BlockHitResult(offset.getCenter(), Direction.DOWN, offset, false), level, offset, player);
    }
}
