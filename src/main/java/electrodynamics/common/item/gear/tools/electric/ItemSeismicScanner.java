package electrodynamics.common.item.gear.tools.electric;

import java.util.List;

import electrodynamics.api.capability.types.itemhandler.CapabilityItemStackHandler;
import electrodynamics.api.electricity.formatting.ChatFormatter;
import electrodynamics.api.electricity.formatting.DisplayUnit;
import electrodynamics.common.inventory.container.item.ContainerSeismicScanner;
import electrodynamics.common.packet.types.client.PacketAddClientRenderInfo;
import electrodynamics.prefab.inventory.container.types.GenericContainerItem;
import electrodynamics.prefab.item.ElectricItemProperties;
import electrodynamics.prefab.item.ItemElectric;
import electrodynamics.prefab.utilities.ElectroTextUtils;
import electrodynamics.prefab.utilities.WorldUtils;
import electrodynamics.prefab.utilities.object.Location;
import electrodynamics.registers.ElectrodynamicsDataComponentTypes;
import electrodynamics.registers.ElectrodynamicsItems;
import electrodynamics.registers.ElectrodynamicsSounds;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.network.PacketDistributor;

public class ItemSeismicScanner extends ItemElectric {

    private static final Component CONTAINER_TITLE = Component.translatable("container.seismicscanner");

    public static final int SLOT_COUNT = 4;
    public static final int RADUIS_BLOCKS = 20;
    public static final int COOLDOWN = 200;
    public static final int JOULES_PER_SCAN = 1000;

    public static final int PATTERN_PER_SCAN = 5;
    public static final int PATTERN_USES = 20;

    public static final double FULL_PATTERN = PATTERN_PER_SCAN * PATTERN_USES;

    public static final String PLAY_LOC = "player";
    public static final String BLOCK_LOC = "block";

    public ItemSeismicScanner(ElectricItemProperties properties, Holder<CreativeModeTab> creativeTab) {
        super(properties, creativeTab, item -> ElectrodynamicsItems.ITEM_BATTERY.get());
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltips, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltips, flag);
        tooltips.add(ElectroTextUtils.tooltip("seismicscanner.range", stack.getOrDefault(ElectrodynamicsDataComponentTypes.RANGE.get(), 1) * RADUIS_BLOCKS).withStyle(ChatFormatting.YELLOW));
        int cooldown = stack.getOrDefault(ElectrodynamicsDataComponentTypes.TIMER, 0);
        if (cooldown > 0) {
            tooltips.add(ElectroTextUtils.tooltip("seismicscanner.cooldown", ChatFormatter.getChatDisplay(cooldown, DisplayUnit.TIME_TICKS)).withStyle(ChatFormatting.BOLD, ChatFormatting.RED));
        }

    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {

        if (context.getLevel().isClientSide() || !context.getPlayer().isShiftKeyDown()) {
            return super.onItemUseFirst(stack, context);
        }

        BlockState state = context.getLevel().getBlockState(context.getClickedPos());

        if (state.isAir()) {
            stack.remove(ElectrodynamicsDataComponentTypes.BLOCK);
            stack.remove(ElectrodynamicsDataComponentTypes.PATTERN_INTEGRITY);
        } else {
            stack.set(ElectrodynamicsDataComponentTypes.BLOCK, state.getBlock());
            stack.set(ElectrodynamicsDataComponentTypes.PATTERN_INTEGRITY, FULL_PATTERN);
        }


        return InteractionResult.SUCCESS;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(final Level world, Player player, InteractionHand hand) {
        if (!world.isClientSide) {
            ItemStack scanner = player.getItemInHand(hand);


            if (!player.isShiftKeyDown()) {
                player.openMenu(getMenuProvider(world, player, scanner, hand));
            }
        }
        return super.use(world, player, hand);
    }

    public MenuProvider getMenuProvider(Level world, Player player, ItemStack stack, InteractionHand hand) {
        return new SimpleMenuProvider((id, inv, play) -> {
            CapabilityItemStackHandler handler = (CapabilityItemStackHandler) stack.getCapability(Capabilities.ItemHandler.ITEM);
            if (handler == null) {
                handler = new CapabilityItemStackHandler(SLOT_COUNT, stack);
            }
            return new ContainerSeismicScanner(id, player.getInventory(), handler, GenericContainerItem.makeData(hand));
        }, CONTAINER_TITLE);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int itemSlot, boolean isSelected) {
        if (!world.isClientSide) {

            int time = stack.getOrDefault(ElectrodynamicsDataComponentTypes.TIMER, 0);

            if (time <= 0) {
                if (stack.getOrDefault(ElectrodynamicsDataComponentTypes.USED, false)) {

                    performScan(world, stack, entity);
                    stack.remove(ElectrodynamicsDataComponentTypes.USED);
                } else if (ScannerMode.values()[stack.getOrDefault(ElectrodynamicsDataComponentTypes.ENUM, 0)] == ScannerMode.ACTIVE) {
                    performScan(world, stack, entity);
                }
            } else {
                stack.set(ElectrodynamicsDataComponentTypes.TIMER, time - 1);
            }
        }
        super.inventoryTick(stack, world, entity, itemSlot, isSelected);
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return slotChanged;
    }

    public static void performScan(Level world, ItemStack stack, Entity entity) {

        ItemSeismicScanner seismic = (ItemSeismicScanner) stack.getItem();

        boolean isTimerUp = stack.getOrDefault(ElectrodynamicsDataComponentTypes.TIMER, 0) <= 0;
        boolean isPowered = seismic.getJoulesStored(stack) >= JOULES_PER_SCAN;

        if (!isTimerUp || !isPowered) {
            return;
        }

        Block pattern = stack.getOrDefault(ElectrodynamicsDataComponentTypes.BLOCK, Blocks.AIR);
        double patternIntegrity = stack.getOrDefault(ElectrodynamicsDataComponentTypes.PATTERN_INTEGRITY, 0.0);

        Block toScanFor = Blocks.AIR;

        if (pattern == Blocks.AIR || patternIntegrity < PATTERN_PER_SCAN) {
            IItemHandler handler = stack.getCapability(Capabilities.ItemHandler.ITEM);
            ItemStack ore = handler == null ? ItemStack.EMPTY : handler.getStackInSlot(0);

            if (ore.getItem() instanceof BlockItem blockItem) {
                toScanFor = blockItem.getBlock();
            }


        } else if (pattern != Blocks.AIR && patternIntegrity >= PATTERN_PER_SCAN) {

            toScanFor = pattern;
            patternIntegrity -= PATTERN_PER_SCAN;
            if (patternIntegrity <= 0) {
                stack.remove(ElectrodynamicsDataComponentTypes.BLOCK);
                stack.remove(ElectrodynamicsDataComponentTypes.PATTERN_INTEGRITY);
            } else {
                stack.set(ElectrodynamicsDataComponentTypes.PATTERN_INTEGRITY, patternIntegrity);
            }

        }

        if (toScanFor == Blocks.AIR) {
            stack.remove(ElectrodynamicsDataComponentTypes.BLOCK);
            stack.remove(ElectrodynamicsDataComponentTypes.PATTERN_INTEGRITY);
            return;
        }

        seismic.extractPower(stack, JOULES_PER_SCAN, false);

        stack.set(ElectrodynamicsDataComponentTypes.TIMER, COOLDOWN);

        world.playSound(null, entity.blockPosition(), ElectrodynamicsSounds.SOUND_SEISMICSCANNER.get(), SoundSource.PLAYERS, 1, 1);

        Location playerPos = new Location(entity.getOnPos());
        Location blockPos = new Location(WorldUtils.getClosestBlockToCenter(world, playerPos.toBlockPos(), RADUIS_BLOCKS * stack.getOrDefault(ElectrodynamicsDataComponentTypes.RANGE, 1), toScanFor));
        stack.set(ElectrodynamicsDataComponentTypes.LOCATION_1, playerPos);
        stack.set(ElectrodynamicsDataComponentTypes.LOCATION_2, blockPos);
        PacketDistributor.sendToPlayer((ServerPlayer) entity, new PacketAddClientRenderInfo(entity.getUUID(), blockPos.toBlockPos()));

    }

    public static enum ScannerMode {
        PASSIVE, ACTIVE;
    }

}
