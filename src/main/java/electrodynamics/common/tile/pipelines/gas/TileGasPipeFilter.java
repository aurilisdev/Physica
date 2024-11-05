package electrodynamics.common.tile.pipelines.gas;

import java.util.ArrayList;
import java.util.List;

import electrodynamics.prefab.properties.PropertyTypes;
import org.jetbrains.annotations.Nullable;

import electrodynamics.api.capability.types.gas.IGasHandler;
import electrodynamics.api.gas.Gas;
import electrodynamics.api.gas.GasAction;
import electrodynamics.api.gas.GasStack;
import electrodynamics.common.inventory.container.tile.ContainerGasPipeFilter;
import electrodynamics.prefab.properties.Property;
import electrodynamics.prefab.tile.GenericTile;
import electrodynamics.prefab.tile.components.type.ComponentContainerProvider;
import electrodynamics.prefab.tile.components.type.ComponentPacketHandler;
import electrodynamics.prefab.utilities.BlockEntityUtils;
import electrodynamics.prefab.utilities.CapabilityUtils;
import electrodynamics.registers.ElectrodynamicsTiles;
import electrodynamics.registers.ElectrodynamicsCapabilities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class TileGasPipeFilter extends GenericTile {

    public static final BlockEntityUtils.MachineDirection INPUT_DIR = BlockEntityUtils.MachineDirection.FRONT;
    public static final BlockEntityUtils.MachineDirection OUTPUT_DIR = BlockEntityUtils.MachineDirection.BACK;

    private boolean isLocked = false;

    @SuppressWarnings("rawtypes")
    public final Property[] filteredGases = {
            //
            property(new Property<>(PropertyTypes.GAS_STACK, "gasone", GasStack.EMPTY)),
            //
            property(new Property<>(PropertyTypes.GAS_STACK, "gastwo", GasStack.EMPTY)),
            //
            property(new Property<>(PropertyTypes.GAS_STACK, "gasthree", GasStack.EMPTY)),
            //
            property(new Property<>(PropertyTypes.GAS_STACK, "gasfour", GasStack.EMPTY)) };

    public final Property<Boolean> isWhitelist = property(new Property<>(PropertyTypes.BOOLEAN, "iswhitelist", false));

    public TileGasPipeFilter(BlockPos worldPos, BlockState blockState) {
        super(ElectrodynamicsTiles.TILE_GASPIPEFILTER.get(), worldPos, blockState);
        addComponent(new ComponentPacketHandler(this));
        addComponent(new ComponentContainerProvider("container.gaspipefilter", this).createMenu((id, inv) -> new ContainerGasPipeFilter(id, inv, getCoordsArray())));
    }

    @Override
    public @Nullable IGasHandler getGasHandlerCapability(@Nullable Direction side) {
        if (side == null || isLocked) {
            return null;
        }

        Direction facing = getFacing();

        if (side == BlockEntityUtils.getRelativeSide(facing, OUTPUT_DIR.mappedDir)) {
            return CapabilityUtils.EMPTY_GAS;
        }

        if (side == BlockEntityUtils.getRelativeSide(facing, INPUT_DIR.mappedDir)) {

            BlockEntity output = level.getBlockEntity(getBlockPos().relative(side.getOpposite()));

            if (output == null) {
                return CapabilityUtils.EMPTY_GAS;
            }

            isLocked = true;

            IGasHandler gas = output.getLevel().getCapability(ElectrodynamicsCapabilities.CAPABILITY_GASHANDLER_BLOCK, output.getBlockPos(), output.getBlockState(), output, side);

            isLocked = false;

            return gas == null ? CapabilityUtils.EMPTY_GAS : new FilteredGasCap(gas, getFilteredGases(), isWhitelist.get());

        }

        return null;
    }

    private List<Gas> getFilteredGases() {
        List<Gas> gases = new ArrayList<>();

        for (Property<GasStack> prop : filteredGases) {
            if (!prop.get().isEmpty()) {
                gases.add(prop.get().getGas());
            }
        }

        return gases;
    }

    private class FilteredGasCap implements IGasHandler {

        private final IGasHandler outputCap;
        private final List<Gas> validGases;
        private final boolean whitelist;

        private FilteredGasCap(IGasHandler outputCap, List<Gas> validGases, boolean whitelist) {
            this.outputCap = outputCap;
            this.validGases = validGases;
            this.whitelist = whitelist;
        }

        @Override
        public int getTanks() {
            if (isLocked) {
                return 0;
            }
            isLocked = true;
            int count = outputCap.getTanks();
            isLocked = false;
            return count;
        }

        @Override
        public GasStack getGasInTank(int tank) {
            if (isLocked) {
                return GasStack.EMPTY;
            }
            isLocked = true;
            GasStack stack = outputCap.getGasInTank(tank);
            isLocked = false;
            return stack;
        }

        @Override
        public double getTankCapacity(int tank) {
            if (isLocked) {
                return 0;
            }
            isLocked = true;
            double cap = outputCap.getTankCapacity(tank);
            isLocked = false;
            return cap;
        }

        @Override
        public double getTankMaxTemperature(int tank) {
            if (isLocked) {
                return 0;
            }
            isLocked = true;
            double temp = outputCap.getTankMaxTemperature(tank);

            isLocked = false;
            return temp;
        }

        @Override
        public int getTankMaxPressure(int tank) {
            if (isLocked) {
                return 0;
            }
            isLocked = true;
            int pres = outputCap.getTankMaxPressure(tank);
            isLocked = false;
            return pres;
        }

        @Override
        public boolean isGasValid(int tank, GasStack gas) {

            if (isLocked) {
                return false;
            }

            if (whitelist) {

                if (validGases.isEmpty()) {
                    return false;
                }

                if (validGases.contains(gas.getGas())) {
                    isLocked = true;
                    boolean valid = outputCap.isGasValid(tank, gas);

                    isLocked = false;
                    return valid;
                }

                return false;

            }

            if (validGases.isEmpty() || !validGases.contains(gas.getGas())) {

                isLocked = true;
                boolean valid = outputCap.isGasValid(tank, gas);

                isLocked = false;

                return valid;
            }

            return false;
        }

        @Override
        public double fillTank(int tank, GasStack gas, GasAction action) {
            if (isLocked) {
                return 0;
            }
            if (isGasValid(tank, gas)) {
                isLocked = true;
                double fill = outputCap.fillTank(tank, gas, action);

                isLocked = false;
                return fill;
            }
            return 0;
        }

        @Override
        public GasStack drainTank(int tank, GasStack gas, GasAction action) {
            if (isLocked) {
                return GasStack.EMPTY;
            }
            isLocked = true;
            GasStack drain = outputCap.drainTank(tank, tank, action);

            isLocked = false;
            return drain;
        }

        @Override
        public GasStack drainTank(int tank, double maxFill, GasAction action) {
            if (isLocked) {
                return GasStack.EMPTY;
            }
            isLocked = true;
            GasStack drain = outputCap.drainTank(tank, maxFill, action);
            isLocked = false;
            return drain;
        }

        @Override
        public double heat(int tank, double deltaTemperature, GasAction action) {
            if (isLocked) {
                return -1;
            }
            isLocked = true;
            double heat = outputCap.heat(tank, deltaTemperature, action);

            isLocked = false;
            return heat;
        }

        @Override
        public double bringPressureTo(int tank, int atm, GasAction action) {
            if (isLocked) {
                return -1;
            }
            isLocked = true;
            double pres = outputCap.bringPressureTo(tank, atm, action);

            isLocked = false;
            return pres;
        }

    }

}
