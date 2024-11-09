package electrodynamics.prefab.tile.types;

import electrodynamics.api.capability.types.gas.IGasHandlerItem;
import electrodynamics.api.gas.GasAction;
import electrodynamics.api.gas.GasStack;
import electrodynamics.api.gas.GasTank;
import electrodynamics.prefab.tile.GenericTile;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.tile.components.utils.IComponentFluidHandler;
import electrodynamics.prefab.tile.components.utils.IComponentGasHandler;
import electrodynamics.registers.ElectrodynamicsCapabilities;
import electrodynamics.registers.ElectrodynamicsSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

//You come up with a better name :D
public class GenericMaterialTile extends GenericTile {

    public GenericMaterialTile(BlockEntityType<?> tileEntityTypeIn, BlockPos worldPos, BlockState blockState) {
        super(tileEntityTypeIn, worldPos, blockState);
    }

    @Override
    public ItemInteractionResult useWithItem(ItemStack used, Player player, InteractionHand hand, BlockHitResult hit) {

        Level world = getLevel();

        IFluidHandlerItem handlerFluidItem = used.getCapability(Capabilities.FluidHandler.ITEM);

        if (handlerFluidItem != null && hasComponent(IComponentType.FluidHandler)) {

            IComponentFluidHandler fluidHandler = getComponent(IComponentType.FluidHandler);

            // first try to drain the item
            for (FluidTank tank : fluidHandler.getInputTanks()) {

                int space = tank.getSpace();

                FluidStack containedFluid = handlerFluidItem.drain(space, FluidAction.SIMULATE);

                if (containedFluid.isEmpty()) {
                    continue;
                }

                if (!world.isClientSide) {

                    tank.fill(containedFluid, FluidAction.EXECUTE);

                    if (!player.isCreative()) {

                        handlerFluidItem.drain(space, FluidAction.EXECUTE);

                    }

                    world.playSound(null, player.blockPosition(), SoundEvents.BUCKET_EMPTY, SoundSource.PLAYERS, 1, 1);

                    player.setItemInHand(hand, handlerFluidItem.getContainer());

                }

                return ItemInteractionResult.CONSUME;

            }
            // now try to fill it
            for (FluidTank tank : fluidHandler.getOutputTanks()) {

                FluidStack tankFluid = tank.getFluid();

                int taken = handlerFluidItem.fill(tankFluid, FluidAction.EXECUTE);

                if (taken <= 0) {
                    continue;
                }

                if (!world.isClientSide) {

                    tank.drain(taken, FluidAction.EXECUTE);

                    world.playSound(null, player.blockPosition(), SoundEvents.BUCKET_FILL, SoundSource.PLAYERS, 1, 1);

                    player.setItemInHand(hand, handlerFluidItem.getContainer());

                }

                return ItemInteractionResult.CONSUME;

            }
        }

        IGasHandlerItem handlerGasItem = used.getCapability(ElectrodynamicsCapabilities.CAPABILITY_GASHANDLER_ITEM);

        if (handlerGasItem != null && hasComponent(IComponentType.GasHandler)) {

            IComponentGasHandler gasHandler = getComponent(IComponentType.GasHandler);

            // first try to drain the item
            for (GasTank tank : gasHandler.getInputTanks()) {

                int space = tank.getSpace();

                GasStack containedGas = handlerGasItem.drain(space, GasAction.SIMULATE);

                if (containedGas.isEmpty()) {
                    continue;
                }

                if (!world.isClientSide) {

                    tank.fill(containedGas, GasAction.EXECUTE);

                    if (!player.isCreative()) {

                        handlerGasItem.drain(space, GasAction.EXECUTE);

                    }

                    world.playSound(null, player.blockPosition(), ElectrodynamicsSounds.SOUND_PRESSURERELEASE.get(), SoundSource.PLAYERS, 1, 1);

                    player.setItemInHand(hand, handlerGasItem.getContainer());

                }

                return ItemInteractionResult.CONSUME;

            }
            // now try to fill it
            for (GasTank tank : gasHandler.getOutputTanks()) {

                GasStack tankGas = tank.getGas();

                int taken = handlerGasItem.fill(tankGas, GasAction.EXECUTE);

                if (taken <= 0) {
                    continue;
                }

                if (!world.isClientSide) {

                    tank.drain(taken, GasAction.EXECUTE);

                    world.playSound(null, player.blockPosition(), ElectrodynamicsSounds.SOUND_PRESSURERELEASE.get(), SoundSource.PLAYERS, 1, 1);

                    player.setItemInHand(hand, handlerGasItem.getContainer());

                }

                return ItemInteractionResult.CONSUME;

            }
        }
        return super.useWithItem(used, player, hand, hit);
    }

}
