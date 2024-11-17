package electrodynamics.common.tile.machines;

import electrodynamics.api.References;
import electrodynamics.api.multiblock.assemblybased.Multiblock;
import electrodynamics.api.multiblock.assemblybased.TileMultiblockController;
import electrodynamics.registers.ElectrodynamicsItems;
import electrodynamics.registers.ElectrodynamicsTiles;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class TileElectrolosisChamber extends TileMultiblockController {

    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(References.ID, "electrolosischamber");
    public static final ResourceKey<Multiblock> RESOURCE_KEY = Multiblock.makeKey(ID);


    public TileElectrolosisChamber(BlockPos worldPos, BlockState blockState) {
        super(ElectrodynamicsTiles.TILE_ELECTROLOSISCHAMBER.get(), worldPos, blockState);
    }

    @Override
    public ItemInteractionResult useWithItem(ItemStack used, Player player, InteractionHand hand, BlockHitResult hit) {
        if(!level.isClientSide() && hit.getBlockPos().equals(getBlockPos()) && used.is(ElectrodynamicsItems.ITEM_WRENCH)) {
            checkFormed();
            if(isFormed.get()) {
                formMultiblock();
            } else {
                destroyMultiblock();
            }


        }
        return super.useWithItem(used, player, hand, hit);
    }

    @Override
    public ResourceLocation getMultiblockId() {
        return ID;
    }

    @Override
    public ResourceKey<Multiblock> getResourceKey() {
        return RESOURCE_KEY;
    }
}
