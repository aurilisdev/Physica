package electrodynamics.api.capability.types.itemhandler;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Consumer;

import net.minecraft.core.BlockPos;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.items.ItemStackHandler;

public class CapabilityItemStackHandler extends ItemStackHandler implements INBTSerializable<CompoundTag> {

    private final ItemStack owner;

    private BiPredicate<Integer, ItemStack> validator = (slot, stack) -> true;

    private ContainerLevelAccess access = ContainerLevelAccess.NULL;

    private Consumer<OnChangeWrapper> onChange = (onChange) -> {
    };

    public CapabilityItemStackHandler(int size, ItemStack owner) {
        super(size);
        this.owner = owner;
    }

    public CapabilityItemStackHandler setOnChange(Consumer<OnChangeWrapper> onChange) {
        this.onChange = onChange;
        return this;
    }

    public CapabilityItemStackHandler setValidator(BiPredicate<Integer, ItemStack> predicate) {
        this.validator = predicate;
        return this;
    }

    @Override
    protected void onContentsChanged(int slot) {
        onChange.accept(new OnChangeWrapper(owner, this, slot, access));
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        return validator.test(slot, stack);
    }

    public List<ItemStack> getItems() {
        return stacks;
    }

    public void setLevelAccess(Level level, BlockPos pos) {
        access = ContainerLevelAccess.create(level, pos);
    }



    public static record OnChangeWrapper(ItemStack owner, CapabilityItemStackHandler capability, int slot, ContainerLevelAccess levelAccess) {

    }

}
