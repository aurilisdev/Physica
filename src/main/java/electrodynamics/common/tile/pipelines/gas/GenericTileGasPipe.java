package electrodynamics.common.tile.pipelines.gas;

import java.util.Set;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Lists;

import electrodynamics.api.capability.types.gas.IGasHandler;
import electrodynamics.api.gas.GasAction;
import electrodynamics.api.gas.GasStack;
import electrodynamics.api.network.cable.type.IGasPipe;
import electrodynamics.common.network.type.GasNetwork;
import electrodynamics.prefab.tile.types.GenericRefreshingConnectTile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class GenericTileGasPipe extends GenericRefreshingConnectTile<IGasPipe, GenericTileGasPipe, GasNetwork> {
    private final IGasHandler[] capability = new IGasHandler[6];

    public GenericTileGasPipe(BlockEntityType<?> tileEntityTypeIn, BlockPos worldPos, BlockState blockState) {
        super(tileEntityTypeIn, worldPos, blockState);
        for (Direction dir : Direction.values()) {
            capability[dir.ordinal()] = new IGasHandler() {

                @Override
                public boolean isGasValid(int tank, GasStack gas) {
                    return gas != null;
                }

                @Override
                public int heat(int tank, int deltaTemperature, GasAction action) {
                    return -1;
                }

                @Override
                public int getTanks() {
                    return 1;
                }

                @Override
                public int getTankMaxTemperature(int tank) {
                    return -1;
                }

                @Override
                public int getTankMaxPressure(int tank) {
                    return getNetwork() == null ? 0 : getNetwork().maxPressure;
                }

                @Override
                public int getTankCapacity(int tank) {
                    return 0;
                }

                @Override
                public GasStack getGasInTank(int tank) {
                    return GasStack.EMPTY;
                }

                @Override
                public int fill(GasStack gas, GasAction action) {
                    if (action == GasAction.SIMULATE || getNetwork() == null || gas.isEmpty()) {
                        return 0;
                    }
                    return getNetwork().emit(gas, Lists.newArrayList(level.getBlockEntity(worldPos.relative(dir))), action == GasAction.SIMULATE).getAmount();
                }

                @Override
                public GasStack drain(int maxFill, GasAction action) {
                    return GasStack.EMPTY;
                }

                @Override
                public GasStack drain(GasStack gas, GasAction action) {
                    return GasStack.EMPTY;
                }

                @Override
                public int bringPressureTo(int tank, int atm, GasAction action) {
                    return -1;
                }
            };
        }
    }

    @Override
    public @Nullable IGasHandler getGasHandlerCapability(@Nullable Direction side) {
        if (side == null) {
            return null;
        }
        return capability[side.ordinal()];

    }

    @Override
    public double getMaxTransfer() {
        return getCableType().getMaxTransfer();
    }

    @Override
    public GasNetwork createInstance(Set<GasNetwork> gasNetworks) {
        return new GasNetwork(gasNetworks);
    }

    @Override
    public GasNetwork createInstanceConductor(Set<GenericTileGasPipe> genericTileGasPipes) {
        return new GasNetwork(genericTileGasPipes);
    }

    @Override
    public void destroyViolently() {
        if (level.isClientSide) {
            return;
        }
        level.playSound(null, getBlockPos(), SoundEvents.GENERIC_EXPLODE.value(), SoundSource.BLOCKS, 1.0F, 1.0F);
        level.destroyBlock(getBlockPos(), false);
    }
}
