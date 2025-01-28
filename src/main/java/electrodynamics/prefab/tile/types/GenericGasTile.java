package electrodynamics.prefab.tile.types;

import java.util.function.BiConsumer;

import electrodynamics.api.gas.GasStack;
import electrodynamics.api.gas.GasTank;
import electrodynamics.prefab.properties.Property;
import electrodynamics.prefab.properties.PropertyTypes;
import electrodynamics.prefab.tile.GenericTile;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;

public class GenericGasTile extends GenericMaterialTile {

    public static final int MAX_CONDENSED_AMOUNT = 10000;

    public final Property<FluidStack> condensedFluidFromGas = property(new Property<>(PropertyTypes.FLUID_STACK, "condensedfluidfromgas", FluidStack.EMPTY));

    public GenericGasTile(BlockEntityType<?> tileEntityTypeIn, BlockPos worldPos, BlockState blockState) {
        super(tileEntityTypeIn, worldPos, blockState);
    }

    public BiConsumer<GasTank, GenericTile> getCondensedHandler() {

        return (tank, tile) -> {

            GasStack tankGas = tank.getGas().copy();

            tank.setGas(GasStack.EMPTY);

            if (tankGas.isEmpty()) {
                return;
            }

            Fluid condensedFluid = tankGas.getGas().getCondensedFluid();

            if (condensedFluid.isSame(Fluids.EMPTY)) {
                return;
            }

            tankGas.bringPressureTo(GasStack.VACUUM);

            FluidStack currentCondensate = condensedFluidFromGas.get();

            if (currentCondensate.getFluid().isSame(condensedFluid)) {

                int room = Math.max(0, MAX_CONDENSED_AMOUNT - currentCondensate.getAmount());

                int taken = Math.min(room, tankGas.getAmount());

                currentCondensate.setAmount(currentCondensate.getAmount() + taken);

                condensedFluidFromGas.set(currentCondensate);

            } else {

                FluidStack newFluid = new FluidStack(condensedFluid, Math.min(tankGas.getAmount(), MAX_CONDENSED_AMOUNT));

                condensedFluidFromGas.set(newFluid);

            }

        };

    }

}
