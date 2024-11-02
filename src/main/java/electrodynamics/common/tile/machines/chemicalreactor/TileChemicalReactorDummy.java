package electrodynamics.common.tile.machines.chemicalreactor;

import electrodynamics.api.capability.types.electrodynamic.ICapabilityElectrodynamic;
import electrodynamics.common.block.chemicalreactor.BlockChemicalReactorExtra;
import electrodynamics.prefab.tile.GenericTile;
import electrodynamics.prefab.tile.components.CapabilityInputType;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.tile.components.type.ComponentElectrodynamic;
import electrodynamics.prefab.tile.components.type.ComponentFluidHandlerMulti;
import electrodynamics.prefab.tile.components.type.ComponentInventory;
import electrodynamics.registers.ElectrodynamicsTiles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

public class TileChemicalReactorDummy extends GenericTile {

    private boolean destroyed = false;
    public TileChemicalReactorDummy(BlockPos worldPos, BlockState blockState) {
        super(ElectrodynamicsTiles.TILE_CHEMICALREACTOR_DUMMY.get(), worldPos, blockState);
    }

    @Override
    public @Nullable ICapabilityElectrodynamic getElectrodynamicCapability(@Nullable Direction side) {

        if(getLevel().getBlockEntity(getBlockPos().offset(getLocation().offsetDownToParent)) instanceof TileChemicalReactor reactor){
            return reactor.<ComponentElectrodynamic>getComponent(IComponentType.Electrodynamic).getCapability(side, CapabilityInputType.NONE);
        }

        return null;
    }

    @Override
    public @Nullable IFluidHandler getFluidHandlerCapability(@Nullable Direction side) {

        if(getLevel().getBlockEntity(getBlockPos().offset(getLocation().offsetDownToParent)) instanceof TileChemicalReactor reactor){
            return reactor.<ComponentFluidHandlerMulti>getComponent(IComponentType.FluidHandler).getCapability(side, CapabilityInputType.NONE);
        }

        return null;
    }

    @Override
    public @Nullable IItemHandler getItemHandlerCapability(@Nullable Direction side) {
        if(getLevel().getBlockEntity(getBlockPos().offset(getLocation().offsetDownToParent)) instanceof TileChemicalReactor reactor) {
            return reactor.<ComponentInventory>getComponent(IComponentType.Inventory).getCapability(side, CapabilityInputType.NONE);
        }

        return null;
    }

    @Override
    public void onBlockDestroyed() {
        if(!destroyed && getLevel().getBlockEntity(getBlockPos().offset(getLocation().offsetDownToParent)) instanceof TileChemicalReactor reactor) {
            getLevel().destroyBlock(getBlockPos().offset(getLocation().offsetDownToParent), true);
            destroyed = true;
        }
        super.onBlockDestroyed();
    }

    @Override
    public ItemInteractionResult useWithItem(ItemStack used, Player player, InteractionHand hand, BlockHitResult hit) {
        if(getLevel().getBlockEntity(getBlockPos().offset(getLocation().offsetDownToParent)) instanceof TileChemicalReactor reactor) {
            return reactor.useWithItem(used, player, hand, hit);
        }
        return super.useWithItem(used, player, hand, hit);
    }

    @Override
    public InteractionResult useWithoutItem(Player player, BlockHitResult hit) {
        if(getLevel().getBlockEntity(getBlockPos().offset(getLocation().offsetDownToParent)) instanceof TileChemicalReactor reactor) {
            return reactor.useWithoutItem(player, hit);
        }
        return super.useWithoutItem(player, hit);
    }

    @Override
    public int getComparatorSignal() {
        if(getLevel().getBlockEntity(getBlockPos().offset(getLocation().offsetDownToParent)) instanceof TileChemicalReactor reactor) {
            return reactor.getComparatorSignal();
        }
        return super.getComparatorSignal();
    }

    public BlockChemicalReactorExtra.Location getLocation(){
        return ((BlockChemicalReactorExtra) getBlockState().getBlock()).loc;
    }
}
