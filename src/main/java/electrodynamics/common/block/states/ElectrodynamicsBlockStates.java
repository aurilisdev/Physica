package electrodynamics.common.block.states;

import java.util.Locale;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public class ElectrodynamicsBlockStates {

    public static void init() {
    }

    public static final BooleanProperty QUARRY_FRAME_DECAY = BooleanProperty.create("quarryframedecay");
    public static final EnumProperty<AddonTankNeighborType> ADDONTANK_NEIGHBOR_STATUS = EnumProperty.create("addontankneighborstatus", AddonTankNeighborType.class);
    public static final BooleanProperty COMPRESSORSIDE_HAS_TOPTANK = BooleanProperty.create("compressorsidehastoptank");
    public static final EnumProperty<ManipulatorHeatingStatus> MANIPULATOR_HEATING_STATUS = EnumProperty.create("manipulatorheatingstatus", ManipulatorHeatingStatus.class);
    public static final BooleanProperty HAS_SCAFFOLDING = BooleanProperty.create("hasscaffolding");
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final BooleanProperty LIT = BlockStateProperties.LIT;


    public static enum AddonTankNeighborType implements StringRepresentable {
        NONE,
        BOTTOMTANK,
        TOPTANK,
        BOTTOMANDTOPTANK;

        @Override
        public String getSerializedName() {
            return name().toLowerCase(Locale.ROOT);
        }

    }

    public static enum ManipulatorHeatingStatus implements StringRepresentable {
        OFF,
        COOL,
        HEAT;

        @Override
        public String getSerializedName() {
            return name().toLowerCase(Locale.ROOT);
        }

    }

}
