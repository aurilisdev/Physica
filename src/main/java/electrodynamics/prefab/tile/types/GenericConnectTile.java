package electrodynamics.prefab.tile.types;

import org.jetbrains.annotations.NotNull;

import electrodynamics.client.modelbakers.modelproperties.ModelPropertyConnections;
import electrodynamics.common.block.connect.util.EnumConnectType;
import electrodynamics.prefab.properties.Property;
import electrodynamics.prefab.properties.PropertyTypes;
import electrodynamics.prefab.tile.GenericTile;
import electrodynamics.prefab.tile.components.type.ComponentPacketHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;

public abstract class GenericConnectTile extends GenericTile implements IConnectTile {

    // DUNSWE

    public static final int DOWN_MASK = 0b00000000000000000000000000001111;
    public static final int UP_MASK = 0b00000000000000000000000011110000;
    public static final int NORTH_MASK = 0b00000000000000000000111100000000;
    public static final int SOUTH_MASK = 0b00000000000000001111000000000000;
    public static final int WEST_MASK = 0b00000000000011110000000000000000;
    public static final int EAST_MASK = 0b00000000111100000000000000000000;

    protected EnumConnectType[] connectionsArr = {
            EnumConnectType.NONE,
            EnumConnectType.NONE,
            EnumConnectType.NONE,
            EnumConnectType.NONE,
            EnumConnectType.NONE,
            EnumConnectType.NONE
    };


    public final Property<Integer> connections = property(new Property<>(PropertyTypes.INTEGER, "connections", 0).setShouldUpdateOnChange().onChange((property, old) -> {
        requestModelDataUpdate();
        if (level != null && level.isClientSide()) {
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 8); //
        }
        connectionsArr = readConnectionsInternal(property.get());

    }).onTileLoaded(property -> {
        requestModelDataUpdate();
        connectionsArr = readConnectionsInternal(property.get());
    }).setNoUpdateServer());

    public final Property<BlockState> camoflaugedBlock = property(new Property<>(PropertyTypes.BLOCK_STATE, "camoflaugedblock", Blocks.AIR.defaultBlockState())).setShouldUpdateOnChange().onChange((property, block) -> {
        if (level == null) {
            return;
        }
        level.getChunkSource().getLightEngine().checkBlock(worldPosition);
    }).onTileLoaded(property -> {
        level.getChunkSource().getLightEngine().checkBlock(worldPosition);
    });

    public final Property<BlockState> scaffoldBlock = property(new Property<>(PropertyTypes.BLOCK_STATE, "scaffoldblock", Blocks.AIR.defaultBlockState())).setShouldUpdateOnChange().onChange((property, block) -> {
        if (level == null) {
            return;
        }
        level.getChunkSource().getLightEngine().checkBlock(worldPosition);
    }).onTileLoaded(property -> {
        level.getChunkSource().getLightEngine().checkBlock(worldPosition);
    });

    public GenericConnectTile(BlockEntityType<?> tile, BlockPos pos, BlockState state) {
        super(tile, pos, state);
        addComponent(new ComponentPacketHandler(this));
    }

    public void setCamoBlock(BlockState block) {
        camoflaugedBlock.set(block);
        setChanged();
    }

    public BlockState getCamoBlock() {
        return camoflaugedBlock.get();
    }

    public boolean isCamoAir() {
        return getCamoBlock().isAir();
    }

    public void setScaffoldBlock(BlockState scaffold) {
        scaffoldBlock.set(scaffold);
        setChanged();
    }

    public BlockState getScaffoldBlock() {
        return scaffoldBlock.get();
    }

    public boolean isScaffoldAir() {
        return getScaffoldBlock().isAir();
    }

    public EnumConnectType readConnection(Direction dir, int connections) {

        if (connections == 0) {
            return EnumConnectType.NONE;
        }

        int extracted = 0;
        switch (dir) {
            case DOWN:
                extracted = connections & DOWN_MASK;
                break;
            case UP:
                extracted = connections & UP_MASK;
                break;
            case NORTH:
                extracted = connections & NORTH_MASK;
                break;
            case SOUTH:
                extracted = connections & SOUTH_MASK;
                break;
            case WEST:
                extracted = connections & WEST_MASK;
                break;
            case EAST:
                extracted = connections & EAST_MASK;
                break;
            default:
                break;
        }
        //return EnumConnectType.NONE;

        return EnumConnectType.values()[(extracted >> (dir.ordinal() * 4))];


    }

    public boolean writeConnections(Direction[] dirs, EnumConnectType[] connections) {

        int connectionData = this.connections.get();
        int masked;

        for (Direction dir : dirs) {
            switch (dir) {
                case DOWN:
                    masked = connectionData & ~DOWN_MASK;
                    break;
                case UP:
                    masked = connectionData & ~UP_MASK;
                    break;
                case NORTH:
                    masked = connectionData & ~NORTH_MASK;
                    break;
                case SOUTH:
                    masked = connectionData & ~SOUTH_MASK;
                    break;
                case WEST:
                    masked = connectionData & ~WEST_MASK;
                    break;
                case EAST:
                    masked = connectionData & ~EAST_MASK;
                    break;
                default:
                    masked = 0;
                    break;
            }
            connectionData = masked | (connections[dir.ordinal()].ordinal() << (dir.ordinal() * 4));
        }

        this.connections.set(connectionData);

        return this.connections.isDirty();

    }

    public boolean writeConnection(Direction dir, EnumConnectType connection) {
        int connectionData = this.connections.get();
        int masked;


        switch (dir) {
            case DOWN:
                masked = connectionData & ~DOWN_MASK;
                break;
            case UP:
                masked = connectionData & ~UP_MASK;
                break;
            case NORTH:
                masked = connectionData & ~NORTH_MASK;
                break;
            case SOUTH:
                masked = connectionData & ~SOUTH_MASK;
                break;
            case WEST:
                masked = connectionData & ~WEST_MASK;
                break;
            case EAST:
                masked = connectionData & ~EAST_MASK;
                break;
            default:
                masked = 0;
                break;
        }
        connectionData = masked | (connection.ordinal() << (dir.ordinal() * 4));


        this.connections.set(connectionData);

        return this.connections.isDirty();
    }

    @Override
    public EnumConnectType[] readConnections() {
        return connectionsArr;
    }

    private EnumConnectType[] readConnectionsInternal(int connections) {
        EnumConnectType[] arr = new EnumConnectType[6];
        for (Direction dir : Direction.values()) {
            arr[dir.ordinal()] = readConnection(dir, connections);
        }
        return arr;
    }

    @Override
    public @NotNull ModelData getModelData() {
        return ModelData.builder().with(ModelPropertyConnections.INSTANCE, () -> readConnections()).build();
    }


}
