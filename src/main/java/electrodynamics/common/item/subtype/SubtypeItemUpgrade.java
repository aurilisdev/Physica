package electrodynamics.common.item.subtype;

import java.util.List;

import javax.annotation.Nullable;

import org.apache.logging.log4j.util.TriConsumer;

import electrodynamics.api.ISubtype;
import electrodynamics.prefab.tile.GenericTile;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.tile.components.type.ComponentInventory;
import electrodynamics.prefab.tile.components.type.ComponentProcessor;
import electrodynamics.prefab.utilities.ItemUtils;
import electrodynamics.prefab.utilities.NBTUtils;
import electrodynamics.registers.ElectrodynamicsDataComponentTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;

public enum SubtypeItemUpgrade implements ISubtype {

    basiccapacity(2),
    // box.currentCapacityMultiplier.set(Math.min(box.currentCapacityMultiplier.get()
    // * 1.5, Math.pow(1.5, 3)));
    // box.currentVoltageMultiplier.set(Math.min(box.currentVoltageMultiplier.get()
    // * 2, 2));

    basicspeed(3),
    // processor.operatingSpeed.set(Math.min(processor.operatingSpeed.get() * 1.5,
    // Math.pow(1.5, 3)));

    advancedcapacity(4),
    // box.currentCapacityMultiplier.set(Math.min(box.currentCapacityMultiplier.get()
    // * 2.25, Math.pow(2.25, 3)));
    // box.currentVoltageMultiplier.set(Math.min(box.currentVoltageMultiplier.get()
    // * 4, 4));

    advancedspeed(3),
    // processor.operatingSpeed.set(Math.min(processor.operatingSpeed.get() * 2.25,
    // Math.pow(2.25, 3)));

    // the only way to optimize this one further is to increase the tick delay.
    // Currently, it's set to every 4 ticks
    iteminput((holder, processor, upgrade) -> {
        ComponentInventory inv = holder.getComponent(IComponentType.Inventory);

        if (!inv.hasInputRoom()) {
            return;
        }

        int tickNumber = upgrade.getOrDefault(ElectrodynamicsDataComponentTypes.TIMER, 0);

        if (tickNumber < 4) {
            upgrade.set(ElectrodynamicsDataComponentTypes.TIMER, tickNumber + 1);
            return;
        }

        upgrade.set(ElectrodynamicsDataComponentTypes.TIMER, 0);
        List<Direction> dirs = NBTUtils.readDirectionList(upgrade);

        if (dirs.size() == 0) {
            return;
        }

        if (upgrade.getOrDefault(ElectrodynamicsDataComponentTypes.SMART, false)) {

            int index = 0;
            Direction dir = Direction.DOWN;
            for (int slot : inv.getInputSlotsForProcessor(processor.getProcessorNumber())) {
                if (index < dirs.size()) {
                    dir = dirs.get(index);
                }
                inputSmartMode(getBlockEntity(holder, dir), inv, slot, processor.getProcessorNumber(), dir);
                index++;
            }
        } else {
            for (Direction dir : dirs) {
                inputDefaultMode(getBlockEntity(holder, dir), inv, dir, processor.getProcessorNumber());
            }
        }

    }, 1),
    // I can't really optimize this one any more than it is
    itemoutput((holder, processor, upgrade) -> {
        ComponentInventory inv = holder.getComponent(IComponentType.Inventory);
        if (!inv.hasItemsInOutput()) {
            return;
        }

        int tickNumber = upgrade.getOrDefault(ElectrodynamicsDataComponentTypes.TIMER, 0);

        if (tickNumber < 4) {
            upgrade.set(ElectrodynamicsDataComponentTypes.TIMER, tickNumber + 1);
            return;
        }

        upgrade.set(ElectrodynamicsDataComponentTypes.TIMER, 0);

        List<Direction> dirs = NBTUtils.readDirectionList(upgrade);

        if (dirs.size() <= 0) {
            return;
        }

        if (upgrade.getOrDefault(ElectrodynamicsDataComponentTypes.SMART, false)) {

            int size = 0;
            Direction dir = Direction.DOWN;

            for (int i = 0; i < inv.outputs(); i++) {

                if (size < dirs.size()) {
                    dir = dirs.get(size);
                }

                outputSmartMode(getBlockEntity(holder, dir), inv, i + inv.getOutputStartIndex(), dir);

                size++;
            }

            for (int i = 0; i < inv.biproducts(); i++) {

                if (size < dirs.size()) {
                    dir = dirs.get(size);
                }

                outputSmartMode(getBlockEntity(holder, dir), inv, i + inv.getItemBiproductStartIndex(), dir);

                size++;
            }

        } else {
            for (Direction dir : dirs) {
                outputDefaultMode(getBlockEntity(holder, dir), inv, dir);
            }
        }

    }, 1),
    improvedsolarcell(1),
    // generator.setMultiplier(2.25);
    stator(1),
    // generator.setMultiplier(2.25);
    range(12),
    experience(1),
    itemvoid(1),
    silktouch(1),
    fortune(3),
    unbreaking(3);

    public final TriConsumer<GenericTile, ComponentProcessor, ItemStack> applyUpgrade;
    public final int maxSize;
    // does it have an appliable effect?
    public final boolean isEmpty;

    SubtypeItemUpgrade(TriConsumer<GenericTile, ComponentProcessor, ItemStack> applyUpgrade, int maxSize) {
        this.applyUpgrade = applyUpgrade;
        this.maxSize = maxSize;
        isEmpty = false;
    }

    SubtypeItemUpgrade(int maxStackSize) {
        applyUpgrade = (holder, processor, upgrade) -> {
        };
        maxSize = maxStackSize;
        isEmpty = true;
    }

    @Override
    public String tag() {
        return "upgrade" + name();
    }

    @Override
    public String forgeTag() {
        return "upgrade/" + name();
    }

    @Override
    public boolean isItem() {
        return true;
    }

    private static void inputSmartMode(BlockEntity entity, ComponentInventory inv, int slot, int procNumber, Direction dir) {

        if (entity == null) {
            return;
        }

        IItemHandler item = entity.getLevel().getCapability(Capabilities.ItemHandler.BLOCK, entity.getBlockPos(), entity.getBlockState(), entity, dir.getOpposite());

        if (item == null) {
            return;
        }

        removeItemFromHandler(item, inv, slot);

    }

    private static void inputDefaultMode(BlockEntity entity, ComponentInventory inv, Direction dir, int procNumber) {

        if (entity == null) {
            return;
        }

        IItemHandler item = entity.getLevel().getCapability(Capabilities.ItemHandler.BLOCK, entity.getBlockPos(), entity.getBlockState(), entity, dir.getOpposite());

        if (item == null) {
            return;
        }

        for (int slot : inv.getInputSlotsForProcessor(procNumber)) {
            removeItemFromHandler(item, inv, slot);
        }

    }

    public static void removeItemFromHandler(IItemHandler handler, ComponentInventory inv, int slot) {
        for (int i = 0; i < handler.getSlots(); i++) {
            ItemStack stack = handler.getStackInSlot(i);
            if (!stack.isEmpty()) {
                ItemStack slotItem = inv.getItem(slot);
                boolean canPlace = inv.canPlaceItem(i, stack);
                if (slotItem.isEmpty() && canPlace) {
                    int taken = stack.getCount() < inv.getMaxStackSize() ? stack.getCount() : inv.getMaxStackSize();
                    ItemStack removed = handler.extractItem(i, taken, false);
                    inv.setItem(slot, removed.copy());
                    inv.setChanged(slot);
                } else if (ItemUtils.testItems(stack.getItem(), slotItem.getItem()) && canPlace) {
                    int cap = slotItem.getMaxStackSize() < inv.getMaxStackSize() ? slotItem.getMaxStackSize() : inv.getMaxStackSize();
                    int canTake = cap - slotItem.getCount();
                    inv.getItem(slot).grow(handler.extractItem(i, canTake, false).getCount());
                    inv.setChanged(slot);
                }

            }

        }
    }

    private static void outputSmartMode(BlockEntity entity, ComponentInventory inv, int index, Direction dir) {
        if (entity == null) {
            return;
        }
        IItemHandler item = entity.getLevel().getCapability(Capabilities.ItemHandler.BLOCK, entity.getBlockPos(), entity.getBlockState(), entity, dir.getOpposite());

        if (item == null) {
            return;
        }
        addItemToHandler(item, inv, index);
    }

    private static void outputDefaultMode(BlockEntity entity, ComponentInventory inv, Direction dir) {
        if (entity == null) {
            return;
        }
        IItemHandler item = entity.getLevel().getCapability(Capabilities.ItemHandler.BLOCK, entity.getBlockPos(), entity.getBlockState(), entity, dir.getOpposite());

        if (item == null) {
            return;
        }
        for (int i = 0; i < inv.outputs(); i++) {
            addItemToHandler(item, inv, i + inv.getOutputStartIndex());
        }
        for (int i = 0; i < inv.biproducts(); i++) {
            addItemToHandler(item, inv, i + inv.getItemBiproductStartIndex());
        }
    }

    // returns if the itemstack changed or not
    private static void addItemToHandler(IItemHandler handler, ComponentInventory inv, int index) {
        for (int i = 0; i < handler.getSlots(); i++) {
            ItemStack used = handler.insertItem(i, inv.getItem(index), false);
            inv.setItem(index, used);
            inv.setChanged(index);
            if (used.isEmpty()) {
                break;
            }
        }

    }

    @Nullable
    private static BlockEntity getBlockEntity(GenericTile holder, Direction dir) {
        BlockPos pos = holder.getBlockPos().relative(dir);
        BlockState state = holder.getLevel().getBlockState(pos);
        if (state.hasBlockEntity()) {
            return holder.getLevel().getBlockEntity(holder.getBlockPos().relative(dir));
        }
        return null;
    }
}
