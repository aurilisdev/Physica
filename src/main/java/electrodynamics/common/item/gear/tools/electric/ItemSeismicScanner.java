package electrodynamics.common.item.gear.tools.electric;

import java.util.List;
import java.util.function.Supplier;

import electrodynamics.api.capability.types.itemhandler.CapabilityItemStackHandler;
import electrodynamics.common.inventory.container.item.ContainerSeismicScanner;
import electrodynamics.common.packet.types.client.PacketAddClientRenderInfo;
import electrodynamics.prefab.item.ElectricItemProperties;
import electrodynamics.prefab.item.ItemElectric;
import electrodynamics.prefab.utilities.ElectroTextUtils;
import electrodynamics.prefab.utilities.WorldUtils;
import electrodynamics.prefab.utilities.object.Location;
import electrodynamics.registers.ElectrodynamicsDataComponentTypes;
import electrodynamics.registers.ElectrodynamicsItems;
import electrodynamics.registers.ElectrodynamicsSounds;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.network.PacketDistributor;

public class ItemSeismicScanner extends ItemElectric {

    private static final Component CONTAINER_TITLE = Component.translatable("container.seismicscanner");

    public static final int SLOT_COUNT = 1;
    public static final int RADUIS_BLOCKS = 16;
    public static final int COOLDOWN_SECONDS = 10;
    public static final int JOULES_PER_SCAN = 1000;

    public static final String PLAY_LOC = "player";
    public static final String BLOCK_LOC = "block";

    public ItemSeismicScanner(ElectricItemProperties properties, Supplier<CreativeModeTab> creativeTab) {
        super(properties, creativeTab, item -> ElectrodynamicsItems.ITEM_BATTERY.get());
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltips, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltips, flag);
        tooltips.add(ElectroTextUtils.tooltip("seismicscanner.use"));
        tooltips.add(ElectroTextUtils.tooltip("seismicscanner.opengui").withStyle(ChatFormatting.GRAY));
        boolean onCooldown = stack.getOrDefault(ElectrodynamicsDataComponentTypes.TIMER, 0) > 0;
        if (onCooldown) {
            tooltips.add(ElectroTextUtils.tooltip("seismicscanner.oncooldown").withStyle(ChatFormatting.BOLD, ChatFormatting.RED));
        } else {
            tooltips.add(ElectroTextUtils.tooltip("seismicscanner.showuse").withStyle(ChatFormatting.GRAY));
        }

    }

    @Override
    public InteractionResultHolder<ItemStack> use(final Level world, Player player, InteractionHand hand) {
        if (!world.isClientSide) {
            ItemStack scanner = player.getItemInHand(hand);
            ItemSeismicScanner seismic = (ItemSeismicScanner) scanner.getItem();
            boolean isTimerUp = scanner.getOrDefault(ElectrodynamicsDataComponentTypes.TIMER, 0) <= 0;
            boolean isPowered = seismic.getJoulesStored(scanner) >= JOULES_PER_SCAN;
            IItemHandler handler = scanner.getCapability(Capabilities.ItemHandler.ITEM);

            ItemStack ore = handler == null ? ItemStack.EMPTY : handler.getStackInSlot(0);
            if (player.isShiftKeyDown() && isTimerUp && isPowered && !ore.isEmpty()) {
                extractPower(scanner, getElectricProperties().extract.getJoules(), false);
                scanner.set(ElectrodynamicsDataComponentTypes.TIMER, COOLDOWN_SECONDS * 20);
                world.playSound(null, player.blockPosition(), ElectrodynamicsSounds.SOUND_SEISMICSCANNER.get(), SoundSource.PLAYERS, 1, 1);
                if (ore.getItem() instanceof BlockItem oreBlockItem) {
                    Location playerPos = new Location(player.getOnPos());
                    Location blockPos = new Location(WorldUtils.getClosestBlockToCenter(world, playerPos.toBlockPos(), RADUIS_BLOCKS, oreBlockItem.getBlock()));
                    scanner.set(ElectrodynamicsDataComponentTypes.LOCATION_1, playerPos);
                    scanner.set(ElectrodynamicsDataComponentTypes.LOCATION_2, blockPos);
                    PacketDistributor.sendToPlayer((ServerPlayer) player, new PacketAddClientRenderInfo(player.getUUID(), blockPos.toBlockPos()));
                }
            } else {
                player.openMenu(getMenuProvider(world, player, scanner));
            }
        }
        return super.use(world, player, hand);
    }

    public MenuProvider getMenuProvider(Level world, Player player, ItemStack stack) {
        return new SimpleMenuProvider((id, inv, play) -> {
            CapabilityItemStackHandler handler = (CapabilityItemStackHandler) stack.getCapability(Capabilities.ItemHandler.ITEM);
            if (handler == null) {
                handler = new CapabilityItemStackHandler(SLOT_COUNT, stack);
            }
            return new ContainerSeismicScanner(id, player.getInventory(), handler);
        }, CONTAINER_TITLE);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int itemSlot, boolean isSelected) {
        if (!world.isClientSide) {
            int time = stack.getOrDefault(ElectrodynamicsDataComponentTypes.TIMER, 0);
            if (time > 0) {
                stack.set(ElectrodynamicsDataComponentTypes.TIMER, time - 1);
            }
        }
        super.inventoryTick(stack, world, entity, itemSlot, isSelected);
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return slotChanged;
    }

}
