package electrodynamics.common.network.utils;

import electrodynamics.api.capability.types.gas.IGasHandler;
import electrodynamics.api.capability.types.gas.IGasHandlerItem;
import electrodynamics.api.gas.GasAction;
import electrodynamics.api.gas.GasStack;
import electrodynamics.api.gas.GasTank;
import electrodynamics.prefab.tile.GenericTile;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.tile.components.type.ComponentInventory;
import electrodynamics.prefab.utilities.BlockEntityUtils;
import electrodynamics.registers.ElectrodynamicsCapabilities;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

public class GasUtilities {

    public static boolean isGasReciever(BlockEntity acceptor, Direction dir) {
        return acceptor != null && acceptor.getLevel().getCapability(ElectrodynamicsCapabilities.CAPABILITY_GASHANDLER_BLOCK, acceptor.getBlockPos(), acceptor.getBlockState(), acceptor, dir) != null;
    }

    public static int recieveGas(BlockEntity reciever, Direction dir, GasStack gas, GasAction action) {
        if (gas.isEmpty() || gas.getAmount() <= 0) {
            return 0;
        }
        GasStack copy = gas.copy();

        IGasHandler handler = reciever.getLevel().getCapability(ElectrodynamicsCapabilities.CAPABILITY_GASHANDLER_BLOCK, reciever.getBlockPos(), reciever.getBlockState(), reciever, dir);

        if (handler == null) {
            return 0;
        }

        int taken = 0;
        int recieved = 0;

        for (int i = 0; i < handler.getTanks(); i++) {
            if (handler.isGasValid(i, copy)) {
                recieved = handler.fill(copy, action);
                copy.shrink(recieved);
                taken += recieved;
            }
        }
        return taken;
    }

    public static void outputToPipe(GenericTile tile, GasTank[] tanks, Direction... outputDirections) {

        Direction facing = tile.getFacing();

        for (Direction relative : outputDirections) {

            Direction direction = BlockEntityUtils.getRelativeSide(facing, relative);

            BlockEntity faceTile = tile.getLevel().getBlockEntity(tile.getBlockPos().relative(direction));

            if (faceTile == null) {
                continue;
            }

            IGasHandler handler = faceTile.getLevel().getCapability(ElectrodynamicsCapabilities.CAPABILITY_GASHANDLER_BLOCK, faceTile.getBlockPos(), faceTile.getBlockState(), faceTile, direction.getOpposite());

            if (handler == null) {
                continue;
            }

            for (GasTank gasTank : tanks) {

                if(gasTank.isEmpty()) {

                }

                for (int i = 0; i < handler.getTanks(); i++) {

                    GasStack tankGas = gasTank.getGas().copy();

                    int amtAccepted = handler.fill(tankGas, GasAction.EXECUTE);

                    GasStack taken = new GasStack(tankGas.getGas(), amtAccepted, tankGas.getTemperature(), tankGas.getPressure());

                    gasTank.drain(taken, GasAction.EXECUTE);
                }

            }
        }
    }

    public static void drainItem(GenericTile tile, GasTank[] tanks) {

        ComponentInventory inv = tile.getComponent(IComponentType.Inventory);

        int cylinderIndex = inv.getInputGasStartIndex();

        int size = inv.getInputGasContents().size();

        if (tanks.length < size) {

            return;

        }

        int index;

        for (int i = 0; i < size; i++) {

            index = cylinderIndex + i;

            GasTank tank = tanks[i];

            ItemStack stack = inv.getItem(index);

            int room = tank.getRoom();

            if (stack.isEmpty() || room <= 0) {
                continue;
            }

            IGasHandlerItem handler = stack.getCapability(ElectrodynamicsCapabilities.CAPABILITY_GASHANDLER_ITEM);

            if (handler == null) {
                continue;
            }

            for (int j = 0; j < handler.getTanks(); j++) {

                if (room <= 0) {
                    break;
                }

                GasStack taken = handler.drain(room, GasAction.SIMULATE);

                if (taken.isEmpty() || !tank.isGasValid(taken)) {
                    continue;
                }

                int takenAmt = tank.fill(taken, GasAction.EXECUTE);

                handler.drain(takenAmt, GasAction.EXECUTE);

                room = tank.getRoom();

            }

            inv.setItem(index, handler.getContainer());

        }

    }

    public static void fillItem(GenericTile tile, GasTank[] tanks) {

        ComponentInventory inv = tile.getComponent(IComponentType.Inventory);

        int cylinderIndex = inv.getOutputGasStartIndex();

        int size = inv.getOutputGasContents().size();

        if (tanks.length < size) {

            return;

        }

        int index;

        for (int i = 0; i < size; i++) {

            index = cylinderIndex + i;

            GasTank tank = tanks[i];

            ItemStack stack = inv.getItem(index);

            if (tank.isEmpty() || stack.isEmpty()) {
                continue;
            }

            IGasHandlerItem handler = stack.getCapability(ElectrodynamicsCapabilities.CAPABILITY_GASHANDLER_ITEM);

            if (handler == null) {
                continue;
            }

            for (int j = 0; j < handler.getTanks(); j++) {

                if (tank.isEmpty()) {
                    break;
                }

                GasStack gas = tank.getGas();

                if (gas.getTemperature() > handler.getTankMaxTemperature(0) || gas.getPressure() > handler.getTankMaxPressure(0)) {

                    tile.getLevel().playSound(null, tile.getBlockPos(), SoundEvents.GENERIC_EXPLODE.value(), SoundSource.BLOCKS, 1.0F, 1.0F);

                }

                int taken = handler.fill(gas, GasAction.EXECUTE);

                tank.drain(taken, GasAction.EXECUTE);

            }

            inv.setItem(index, handler.getContainer());

        }
    }

}
