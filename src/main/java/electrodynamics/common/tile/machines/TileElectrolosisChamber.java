package electrodynamics.common.tile.machines;

import electrodynamics.api.IWrenchItem;
import electrodynamics.api.References;
import electrodynamics.api.capability.types.electrodynamic.ICapabilityElectrodynamic;
import electrodynamics.api.multiblock.assemblybased.Multiblock;
import electrodynamics.api.multiblock.assemblybased.TileMultiblockController;
import electrodynamics.api.multiblock.assemblybased.TileMultiblockSlave;
import electrodynamics.common.block.subtype.SubtypeMachine;
import electrodynamics.common.inventory.container.tile.ContainerElectrolosisChamber;
import electrodynamics.common.recipe.ElectrodynamicsRecipeInit;
import electrodynamics.prefab.tile.components.CapabilityInputType;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.tile.components.type.*;
import electrodynamics.prefab.tile.components.utils.IComponentFluidHandler;
import electrodynamics.prefab.utilities.BlockEntityUtils;
import electrodynamics.registers.ElectrodynamicsCapabilities;
import electrodynamics.registers.ElectrodynamicsTiles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.Nullable;

public class TileElectrolosisChamber extends TileMultiblockController {

    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(References.ID, "electrolosischamber");
    public static final ResourceKey<Multiblock> RESOURCE_KEY = Multiblock.makeKey(ID);

    public static final int MAX_INPUT_TANK_CAPACITY = 5000;
    public static final int MAX_OUTPUT_TANK_CAPACITY = 5000;

    public TileElectrolosisChamber(BlockPos worldPos, BlockState blockState) {
        super(ElectrodynamicsTiles.TILE_ELECTROLOSISCHAMBER.get(), worldPos, blockState);

        addComponent(new ComponentElectrodynamic(this, false, true).setInputDirections(BlockEntityUtils.MachineDirection.BACK).voltage(ElectrodynamicsCapabilities.DEFAULT_VOLTAGE * 2));
        addComponent(new ComponentFluidHandlerMulti(this).setInputDirections(BlockEntityUtils.MachineDirection.RIGHT).setInputTanks(1, arr(MAX_INPUT_TANK_CAPACITY)).setOutputDirections(BlockEntityUtils.MachineDirection.LEFT).setOutputTanks(1, MAX_OUTPUT_TANK_CAPACITY).setRecipeType(ElectrodynamicsRecipeInit.ELECTROLOSIS_CHAMBER_TYPE.get()));
        addComponent(new ComponentContainerProvider(SubtypeMachine.electrolosischamber, this).createMenu((id, player) -> new ContainerElectrolosisChamber(id, player, new SimpleContainer(0), getCoordsArray())));


    }

    @Override
    public @Nullable IFluidHandler getFluidHandlerCapability(@Nullable Direction side) {
        return null;
    }

    @Nullable
    @Override
    public IFluidHandler getSlaveFluidHandlerCapability(TileMultiblockSlave slave, @Nullable Direction side) {
        if(slave.index.get() != 35 && slave.index.get() != 39) {
            return null;
        }
        return this.<IComponentFluidHandler>getComponent(IComponentType.FluidHandler).getCapability(side, CapabilityInputType.NONE);
    }

    @Override
    public @Nullable ICapabilityElectrodynamic getElectrodynamicCapability(@Nullable Direction side) {
        return null;
    }

    @Nullable
    @Override
    public ICapabilityElectrodynamic getSlaveCapabilityElectrodynamic(TileMultiblockSlave slave, @Nullable Direction side) {
        if(slave.index.get() != 7) {
            return null;
        }
        return this.<ComponentElectrodynamic>getComponent(IComponentType.Electrodynamic).getCapability(side, CapabilityInputType.NONE);
    }

    @Override
    public ItemInteractionResult useWithItem(ItemStack used, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide() && hit.getBlockPos().equals(getBlockPos()) && used.getItem() instanceof IWrenchItem) {
            checkFormed();
            if (isFormed.get()) {
                formMultiblock();
            } else {
                destroyMultiblock();
            }
            return ItemInteractionResult.CONSUME;


        }
        return super.useWithItem(used, player, hand, hit);
    }

    @Override
    public InteractionResult useWithoutItem(Player player, BlockHitResult hit) {
        if (!isFormed.get()) {
            return InteractionResult.FAIL;
        }
        return super.useWithoutItem(player, hit);
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
