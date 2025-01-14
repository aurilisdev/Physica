package electrodynamics.common.item.gear.tools;

import java.util.ArrayList;
import java.util.List;

import electrodynamics.api.capability.types.fluid.RestrictedFluidHandlerItemStack;
import electrodynamics.api.electricity.formatting.ChatFormatter;
import electrodynamics.api.inventory.InventoryTickConsumer;
import electrodynamics.common.item.ItemElectrodynamics;
import electrodynamics.prefab.utilities.ElectroTextUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.HitResult.Type;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;

public class ItemCanister extends ItemElectrodynamics {

    public static final int MAX_FLUID_CAPACITY = 5000;

    public static final List<InventoryTickConsumer> INVENTORY_TICK_CONSUMERS = new ArrayList<>();

    public ItemCanister(Item.Properties properties, Holder<CreativeModeTab> creativeTab) {
        super(properties, creativeTab);
    }

    @Override
    public void addCreativeModeItems(CreativeModeTab group, List<ItemStack> items) {

        items.add(new ItemStack(this));

        if (Capabilities.FluidHandler.ITEM == null) {
            return;
        }

        BuiltInRegistries.FLUID.stream().forEach(fluid -> {
            if (fluid.isSame(Fluids.EMPTY)) {
                return;
            }
            ItemStack temp = new ItemStack(this);

            IFluidHandlerItem cap = temp.getCapability(Capabilities.FluidHandler.ITEM);

            if (cap == null) {
                return;
            }

            RestrictedFluidHandlerItemStack restricted = (RestrictedFluidHandlerItemStack) cap;

            restricted.setFluid(new FluidStack(fluid, MAX_FLUID_CAPACITY));

            items.add(temp);
        });

    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slot, isSelected);
        INVENTORY_TICK_CONSUMERS.forEach(consumer -> consumer.apply(stack, level, entity, slot, isSelected));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flagIn) {
        if (Capabilities.FluidHandler.ITEM == null) {

            super.appendHoverText(stack, context, tooltip, flagIn);

            return;
        }
        IFluidHandlerItem handler = stack.getCapability(Capabilities.FluidHandler.ITEM);

        if (handler == null) {
            super.appendHoverText(stack, context, tooltip, flagIn);

            return;
        }

        RestrictedFluidHandlerItemStack restricted = (RestrictedFluidHandlerItemStack) handler;

        if(!restricted.getFluid().isEmpty()) {
            tooltip.add(restricted.getFluid().getFluidType().getDescription().copy().withStyle(ChatFormatting.GRAY));
        }
        tooltip.add(ElectroTextUtils.ratio(ChatFormatter.formatFluidMilibuckets(restricted.getFluidInTank(0).getAmount()), ChatFormatter.formatFluidMilibuckets(MAX_FLUID_CAPACITY)).withStyle(ChatFormatting.DARK_GRAY));

        super.appendHoverText(stack, context, tooltip, flagIn);
    }

    @Override
    public int getBarWidth(ItemStack stack) {

        IFluidHandlerItem handler = stack.getCapability(Capabilities.FluidHandler.ITEM);

        if (handler == null) {
            return 13;
        }

        return (int) (13.0 * handler.getFluidInTank(0).getAmount() / handler.getTankCapacity(0));
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {

        IFluidHandlerItem handler = stack.getCapability(Capabilities.FluidHandler.ITEM);

        if (handler == null) {
            return false;
        }

        return !handler.getFluidInTank(0).isEmpty();

    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        useCanister(worldIn, playerIn, handIn);
        return InteractionResultHolder.pass(playerIn.getItemInHand(handIn));
    }

    public void useCanister(Level world, Player player, InteractionHand hand) {

        ItemStack stack = player.getItemInHand(hand);

        HitResult trace = getPlayerPOVHitResult(world, player, net.minecraft.world.level.ClipContext.Fluid.ANY);

        if (world.isClientSide || trace.getType() == Type.MISS || trace.getType() == Type.ENTITY) {
            return;
        }

        BlockHitResult blockTrace = (BlockHitResult) trace;

        BlockPos pos = blockTrace.getBlockPos();

        BlockState state = world.getBlockState(pos);

        if (!state.getFluidState().isSource() || state.getFluidState().isEmpty()) {
            return;
        }

        FluidStack sourceFluid = new FluidStack(state.getFluidState().getType(), 1000);
        
        IFluidHandlerItem handler = stack.getCapability(Capabilities.FluidHandler.ITEM);
        
        if(handler == null) {
            return;
        }

        int accepted = handler.fill(sourceFluid, FluidAction.SIMULATE);

        if (accepted < 1000) {
            return;
        }

        handler.fill(sourceFluid, FluidAction.EXECUTE);

        world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());

        world.playSound(null, player.blockPosition(), SoundEvents.BUCKET_FILL, SoundSource.PLAYERS, 1, 1);
    }

}
