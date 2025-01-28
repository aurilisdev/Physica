package electrodynamics.common.tile.pipelines.gas;

import org.jetbrains.annotations.Nullable;

import electrodynamics.api.capability.types.gas.IGasHandler;
import electrodynamics.api.gas.GasAction;
import electrodynamics.api.gas.GasStack;
import electrodynamics.common.tile.pipelines.GenericTileValve;
import electrodynamics.prefab.utilities.BlockEntityUtils;
import electrodynamics.prefab.utilities.CapabilityUtils;
import electrodynamics.registers.ElectrodynamicsCapabilities;
import electrodynamics.registers.ElectrodynamicsTiles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class TileGasValve extends GenericTileValve {

    private boolean isLocked = false;

    public TileGasValve(BlockPos pos, BlockState state) {
        super(ElectrodynamicsTiles.TILE_GASVALVE.get(), pos, state);
    }

    @Override
    public @Nullable IGasHandler getGasHandlerCapability(@Nullable Direction side) {
        if (side == null || isLocked) {
            return null;
        }

        Direction facing = getFacing();

        if (BlockEntityUtils.getRelativeSide(facing, INPUT_DIR.mappedDir) == side || BlockEntityUtils.getRelativeSide(facing, OUTPUT_DIR.mappedDir) == side) {

            BlockEntity relative = level.getBlockEntity(worldPosition.relative(side.getOpposite()));

            if (relative == null) {
                return CapabilityUtils.EMPTY_GAS;
            }

            isLocked = true;

            IGasHandler gas = relative.getLevel().getCapability(ElectrodynamicsCapabilities.CAPABILITY_GASHANDLER_BLOCK, relative.getBlockPos(), relative.getBlockState(), relative, side);

            isLocked = false;

            return gas == null ? CapabilityUtils.EMPTY_GAS : new CapDispatcher(gas);
        }

        return null;
    }

    private class CapDispatcher implements IGasHandler {

        private final IGasHandler parent;

        private CapDispatcher(IGasHandler parent) {
            this.parent = parent;
        }

        @Override
        public int getTanks() {
            if (isClosed || isLocked) {
                return 0;
            }
            isLocked = true;
            int tanks = parent.getTanks();
            isLocked = false;
            return tanks;
        }

        @Override
        public GasStack getGasInTank(int tank) {
            if (isClosed || isLocked) {
                return GasStack.EMPTY;
            }
            isLocked = true;
            GasStack stack = parent.getGasInTank(tank);
            isLocked = false;
            return stack;
        }

        @Override
        public int getTankCapacity(int tank) {
            if (isClosed || isLocked) {
                return 0;
            }
            isLocked = true;
            int cap = parent.getTankCapacity(tank);
            isLocked = false;
            return cap;
        }

        @Override
        public int getTankMaxTemperature(int tank) {
            if (isClosed || isLocked) {
                return 0;
            }
            isLocked = true;
            int temp = parent.getTankMaxTemperature(tank);
            isLocked = false;
            return temp;
        }

        @Override
        public int getTankMaxPressure(int tank) {
            if (isClosed || isLocked) {
                return 0;
            }
            isLocked = true;
            int pres = parent.getTankMaxPressure(tank);
            isLocked = false;
            return pres;
        }

        @Override
        public boolean isGasValid(int tank, GasStack gas) {
            if (isClosed || isLocked) {
                return false;
            }
            isLocked = true;
            boolean valid = parent.isGasValid(tank, gas);
            isLocked = false;
            return valid;
        }

        @Override
        public int fill(GasStack gas, GasAction action) {
            if (isClosed || isLocked) {
                return 0;
            }
            isLocked = true;
            int fill = parent.fill(gas, action);
            isLocked = false;
            return fill;
        }

        @Override
        public GasStack drain(GasStack gas, GasAction action) {
            if (isClosed || isLocked) {
                return GasStack.EMPTY;
            }
            isLocked = true;
            GasStack drain = parent.drain(gas, action);
            isLocked = false;
            return drain;
        }

        @Override
        public GasStack drain(int maxFill, GasAction action) {
            if (isClosed || isLocked) {
                return GasStack.EMPTY;
            }
            isLocked = true;
            GasStack drain = parent.drain(maxFill, action);
            isLocked = false;
            return drain;
        }

        @Override
        public int heat(int tank, int deltaTemperature, GasAction action) {
            if (isClosed || isLocked) {
                return -1;
            }
            isLocked = true;
            int heat = parent.heat(tank, deltaTemperature, action);
            isLocked = false;
            return heat;
        }

        @Override
        public int bringPressureTo(int tank, int atm, GasAction action) {
            if (isClosed || isLocked) {
                return -1;
            }
            isLocked = true;
            int pres = parent.bringPressureTo(tank, atm, action);
            isLocked = false;
            return pres;
        }

    }

}
