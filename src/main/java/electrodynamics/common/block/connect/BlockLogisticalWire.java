package electrodynamics.common.block.connect;

import java.util.HashSet;

import com.mojang.serialization.MapCodec;

import electrodynamics.api.References;
import electrodynamics.common.block.states.ElectrodynamicsBlockStates;
import electrodynamics.common.block.subtype.SubtypeWire;
import electrodynamics.common.tile.electricitygrid.TileLogisticalWire;
import electrodynamics.prefab.utilities.math.Color;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

public class BlockLogisticalWire extends BlockWire {

    public static final Color REDSTONE_ON = new Color(211, 5, 5, 255);
    public static final Color REDSTONE_OFF = new Color(124, 25, 25, 255);

    public static final HashSet<Block> WIRES = new HashSet<>();

    public BlockLogisticalWire(SubtypeWire wire) {
        super(wire);
        WIRES.add(this);
        stateDefinition.any().setValue(ElectrodynamicsBlockStates.LIT, false);
    }

    @Override
    public void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(ElectrodynamicsBlockStates.LIT);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState superState = super.getStateForPlacement(context);
        return superState.setValue(ElectrodynamicsBlockStates.LIT, false);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TileLogisticalWire(pos, state);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        throw new UnsupportedOperationException("Need to implement CODEC");
    }

    @EventBusSubscriber(value = Dist.CLIENT, modid = References.ID, bus = EventBusSubscriber.Bus.MOD)
    private static class ColorHandler {

        @SubscribeEvent
        public static void registerColoredBlocks(RegisterColorHandlersEvent.Block event) {
            WIRES.forEach(block -> event.register((state, level, pos, tintIndex) -> {
                if (tintIndex == 0) {
                    return ((BlockLogisticalWire) block).wire.getWireColor().getColor().color();
                }
                if (tintIndex != 1) {
                    return 0xFFFFFFFF;
                }
                if (state.getValue(ElectrodynamicsBlockStates.LIT)) {
                    return REDSTONE_ON.color();
                }
                return REDSTONE_OFF.color();
            }, block));
        }
    }

}
