package electrodynamics.common.tile.machines.quarry;

import org.jetbrains.annotations.NotNull;

import electrodynamics.client.modelbakers.modelproperties.ModelPropertyConnections;
import electrodynamics.common.block.connect.util.EnumConnectType;
import electrodynamics.common.item.ItemDrillHead;
import electrodynamics.prefab.properties.Property;
import electrodynamics.prefab.properties.PropertyTypes;
import electrodynamics.prefab.tile.GenericTile;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.tile.components.type.ComponentInventory;
import electrodynamics.prefab.tile.components.type.ComponentTickable;
import electrodynamics.prefab.tile.types.IConnectTile;
import electrodynamics.prefab.utilities.Scheduler;
import electrodynamics.registers.ElectrodynamicsTiles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.items.IItemHandler;

public class TileLogisticalManager extends GenericTile implements IConnectTile {

    private TileQuarry[] quarries = new TileQuarry[6];
    private BlockEntity[] inventories = new BlockEntity[6];

    // DUNSWE

    public static final int DOWN_MASK = 0b00000000000000000000000000001111;
    public static final int UP_MASK = 0b00000000000000000000000011110000;
    public static final int NORTH_MASK = 0b00000000000000000000111100000000;
    public static final int SOUTH_MASK = 0b00000000000000001111000000000000;
    public static final int WEST_MASK = 0b00000000000011110000000000000000;
    public static final int EAST_MASK = 0b00000000111100000000000000000000;

    public final Property<Integer> connections = property(new Property<>(PropertyTypes.INTEGER, "connections", 0).setShouldUpdateOnChange().onChange((property, old) -> {
        requestModelDataUpdate();
        if(level != null && level.isClientSide()){
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 8); //
        }
    }).onTileLoaded(property -> requestModelDataUpdate()));

    public TileLogisticalManager(BlockPos pos, BlockState state) {
        super(ElectrodynamicsTiles.TILE_LOGISTICALMANAGER.get(), pos, state);
        addComponent(new ComponentTickable(this).tickServer(this::tickServer));
    }

    private void tickServer(ComponentTickable tick) {
        for (int i = 0; i < 6; i++) {
            BlockEntity inventory = inventories[i];

            if (inventory == null) {
                continue;
            }

            IItemHandler invHandler = inventory.getLevel().getCapability(Capabilities.ItemHandler.BLOCK, inventory.getBlockPos(), inventory.getBlockState(), inventory, Direction.values()[i].getOpposite());

            if (invHandler == null) {
                continue;
            }

            for (TileQuarry quarry : quarries) {
                if (quarry != null) {
                    manipulateItems(quarry.getComponent(IComponentType.Inventory), invHandler);
                }
            }

        }

    }

    @Override
    public void onNeightborChanged(BlockPos neighbor, boolean blockStateTrigger) {
        if (level.isClientSide) {
            return;
        }
        refreshConnections();
    }

    @Override
    public void onPlace(BlockState oldState, boolean isMoving) {
        super.onPlace(oldState, isMoving);
        if (level.isClientSide) {
            return;
        }
        refreshConnections();
    }

    public void refreshConnections() {
        quarries = new TileQuarry[6];
        inventories = new BlockEntity[6];
        for (Direction dir : Direction.values()) {
            BlockEntity entity = level.getBlockEntity(getBlockPos().relative(dir));

            if (entity == null) {
                continue;
            }

            if (entity instanceof TileQuarry quarry) {
                quarries[dir.ordinal()] = quarry;
            } else if (entity.getLevel().getCapability(Capabilities.ItemHandler.BLOCK, entity.getBlockPos(), entity.getBlockState(), entity, dir.getOpposite()) != null) {
                inventories[dir.ordinal()] = entity;
            }

        }
    }

    @Override
    public void onLoad() {
        super.onLoad();
        Scheduler.schedule(1, this::refreshConnections);
    }

    private void manipulateItems(ComponentInventory quarryInventory, IItemHandler handler) {

        if (quarryInventory.getItem(TileQuarry.DRILL_HEAD_INDEX).isEmpty()) {
            restockDrillHead(quarryInventory, handler);
        }

        addItemsToInventory(quarryInventory, handler);

    }

    private void restockDrillHead(ComponentInventory quarryInventory, IItemHandler handler) {

        ItemStack stack;

        for (int i = 0; i < handler.getSlots(); i++) {

            stack = handler.getStackInSlot(i);

            if (!stack.isEmpty() && stack.getItem() instanceof ItemDrillHead) {
                quarryInventory.setItem(TileQuarry.DRILL_HEAD_INDEX, stack.copy());
                handler.extractItem(i, stack.getMaxStackSize(), false);
                break;
            }

        }

    }

    private void addItemsToInventory(ComponentInventory quarryInventory, IItemHandler handler) {
        for (int i = 0; i < quarryInventory.outputs(); i++) {
            int index = i + quarryInventory.getOutputStartIndex();
            ItemStack mined = quarryInventory.getItem(index);
            if (!mined.isEmpty()) {
                for (int j = 0; j < handler.getSlots(); j++) {
                    mined = handler.insertItem(j, mined, false);
                    quarryInventory.setItem(index, mined);
                    quarryInventory.setChanged(index);
                    if (mined.isEmpty()) {
                        break;
                    }
                }
            }
        }

    }

    public static boolean isQuarry(BlockPos pos, LevelReader world) {
        BlockEntity entity = world.getBlockEntity(pos);
        return entity instanceof TileQuarry;
    }

    public static boolean isValidInventory(BlockPos pos, LevelReader world, Direction dir) {
        BlockEntity entity = world.getBlockEntity(pos);
        if (entity == null) {
            return false;
        }

        if (entity.getLevel().getCapability(Capabilities.ItemHandler.BLOCK, entity.getBlockPos(), entity.getBlockState(), entity, dir) != null) {
            return true;
        }

        return entity instanceof Container;
    }

    public EnumConnectType readConnection(Direction dir) {

        int connectionData = connections.get();

        if (connectionData == 0) {
            return EnumConnectType.NONE;
        }

        int extracted = 0;
        switch (dir) {
            case DOWN:
                extracted = connectionData & DOWN_MASK;
                break;
            case UP:
                extracted = connectionData & UP_MASK;
                break;
            case NORTH:
                extracted = connectionData & NORTH_MASK;
                break;
            case SOUTH:
                extracted = connectionData & SOUTH_MASK;
                break;
            case WEST:
                extracted = connectionData & WEST_MASK;
                break;
            case EAST:
                extracted = connectionData & EAST_MASK;
                break;
            default:
                break;
        }

        // return EnumConnectType.NONE;

        return EnumConnectType.values()[(extracted >> (dir.ordinal() * 4))];

    }

    public void writeConnection(Direction dir, EnumConnectType connection) {

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

        connections.set(masked | (connection.ordinal() << (dir.ordinal() * 4)));
    }

    @Override
    public EnumConnectType[] readConnections() {
        EnumConnectType[] connections = new EnumConnectType[6];
        for (Direction dir : Direction.values()) {
            connections[dir.ordinal()] = readConnection(dir);
        }
        return connections;
    }

    @Override
    public @NotNull ModelData getModelData() {
        return ModelData.builder().with(ModelPropertyConnections.INSTANCE, () -> readConnections()).build();
    }

}
