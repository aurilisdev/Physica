package electrodynamics.common.item.gear.tools.electric;

import java.util.ArrayList;
import java.util.List;

import electrodynamics.api.References;
import electrodynamics.api.capability.types.itemhandler.CapabilityItemStackHandler;
import electrodynamics.api.creativetab.CreativeTabSupplier;
import electrodynamics.api.electricity.formatting.ChatFormatter;
import electrodynamics.api.electricity.formatting.DisplayUnit;
import electrodynamics.api.item.IItemElectric;
import electrodynamics.common.inventory.container.item.ContainerElectricDrill;
import electrodynamics.common.item.ItemDrillHead;
import electrodynamics.common.item.gear.tools.electric.utils.ElectricItemTier;
import electrodynamics.common.item.subtype.SubtypeDrillHead;
import electrodynamics.common.tags.ElectrodynamicsTags;
import electrodynamics.prefab.inventory.container.types.GenericContainerItem;
import electrodynamics.prefab.item.ElectricItemProperties;
import electrodynamics.prefab.utilities.ElectroTextUtils;
import electrodynamics.prefab.utilities.math.Color;
import electrodynamics.registers.ElectrodynamicsDataComponentTypes;
import electrodynamics.registers.ElectrodynamicsItems;
import electrodynamics.registers.ElectrodynamicsSounds;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

public class ItemElectricDrill extends DiggerItem implements IItemElectric, CreativeTabSupplier {

    private final Holder<CreativeModeTab> creativeTab;

    private static final List<ItemElectricDrill> DRILLS = new ArrayList<>();

    private static final Component CONTAINER_TITLE = Component.translatable("container.electricdrill");

    public static final int SLOT_COUNT = 3;

    public static final double POWER_USAGE = 1666666.66667 / (120.0 * 20.0);

    private static final String SUBTYPE = "subtype";
    private final ElectricItemProperties properties;

    public ItemElectricDrill(ElectricItemProperties properties, Holder<CreativeModeTab> creativeTab) {
        super(ElectricItemTier.ELECTRIC_DRILL, ElectrodynamicsTags.Blocks.ELECTRIC_DRILL_BLOCKS, properties.durability(0).attributes(createAttributes(ElectricItemTier.ELECTRIC_DRILL, 4, -2.4F)));
        this.properties = properties;
        this.creativeTab = creativeTab;
        DRILLS.add(this);
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        return true;
    }

    @Override
    public void addCreativeModeItems(CreativeModeTab group, List<ItemStack> items) {

        ItemStack empty = new ItemStack(this);
        IItemElectric.setEnergyStored(empty, 0);
        items.add(empty);

        ItemStack charged = new ItemStack(this);
        IItemElectric.setEnergyStored(charged, getMaximumCapacity(charged));
        items.add(charged);

    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        if (getJoulesStored(stack) < properties.extract.getJoules()) {
            return 0;
        }

        float normalized = (float) Math.max(1, getHead(stack).speedBoost * getSpeedBoost(stack));

        return super.getDestroySpeed(stack, state) * normalized;

    }

    @Override
    public boolean mineBlock(ItemStack stack, Level worldIn, BlockState state, BlockPos pos, LivingEntity entityLiving) {

        IItemElectric.setEnergyStored(stack, getJoulesStored(stack) - stack.getOrDefault(ElectrodynamicsDataComponentTypes.POWER_USAGE, POWER_USAGE));

        return super.mineBlock(stack, worldIn, state, pos, entityLiving);
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return (int) Math.round(13.0f * getJoulesStored(stack) / getMaximumCapacity(stack));
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return getJoulesStored(stack) < getMaximumCapacity(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(ElectroTextUtils.tooltip("item.electric.info", ElectroTextUtils.ratio(ChatFormatter.getChatDisplayShort(getJoulesStored(stack), DisplayUnit.JOULES), ChatFormatter.getChatDisplayShort(getMaximumCapacity(stack), DisplayUnit.JOULES)).withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.DARK_GRAY));
        tooltip.add(ElectroTextUtils.tooltip("item.electric.voltage", ChatFormatter.getChatDisplayShort(properties.receive.getVoltage(), DisplayUnit.VOLTAGE).withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.DARK_GRAY));

        IItemElectric.addBatteryTooltip(stack, context, tooltip);
        tooltip.add(ElectroTextUtils.tooltip("electricdrill.miningspeed", ChatFormatter.getChatDisplayShort(getHead(stack).speedBoost * 100, DisplayUnit.PERCENTAGE).withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.DARK_GRAY));
        tooltip.add(ElectroTextUtils.tooltip("electricdrill.usage", ChatFormatter.getChatDisplayShort(stack.getOrDefault(ElectrodynamicsDataComponentTypes.POWER_USAGE, POWER_USAGE), DisplayUnit.JOULES).withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.DARK_GRAY));

        tooltip.add(ElectroTextUtils.tooltip("electricdrill.overclock", ChatFormatter.getChatDisplayShort(getSpeedBoost(stack) * 100, DisplayUnit.PERCENTAGE).withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.DARK_GRAY));
    }

    @Override
    public ElectricItemProperties getElectricProperties() {
        return properties;
    }

    @Override
    public Item getDefaultStorageBattery() {
        return ElectrodynamicsItems.ITEM_BATTERY.get();
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {

        if (!level.isClientSide) {

            player.openMenu(getMenuProvider(level, player, player.getItemInHand(hand), hand));

        }

        return super.use(level, player, hand);
    }

    public MenuProvider getMenuProvider(Level world, Player player, ItemStack stack, InteractionHand hand) {
        return new SimpleMenuProvider((id, inv, play) -> {
            CapabilityItemStackHandler handler = (CapabilityItemStackHandler) stack.getCapability(Capabilities.ItemHandler.ITEM);
            if (handler == null) {
                handler = new CapabilityItemStackHandler(SLOT_COUNT, stack);
            }
            return new ContainerElectricDrill(id, player.getInventory(), handler, GenericContainerItem.makeData(hand));
        }, CONTAINER_TITLE);
    }

    @Override
    public boolean overrideOtherStackedOnMe(ItemStack stack, ItemStack other, Slot slot, ClickAction action, Player player, SlotAccess access) {

        if (!other.isEmpty() && other.getItem() instanceof ItemDrillHead head) {

            ItemStack oldHead = new ItemStack(ElectrodynamicsItems.ITEMS_DRILLHEAD.getValue(getHead(stack)));

            saveHead(stack, head.head);

            access.set(oldHead);

            player.level().playLocalSound(player.getX(), player.getY(), player.getZ(), ElectrodynamicsSounds.SOUND_BATTERY_SWAP.get(), SoundSource.PLAYERS, 0.25F, 1.0F, false);

            return true;

        }

        if (!IItemElectric.overrideOtherStackedOnMe(stack, other, slot, action, player, access)) {
            return super.overrideOtherStackedOnMe(stack, other, slot, action, player, access);
        }

        return true;

    }

    public static double getSpeedBoost(ItemStack stack) {
        return stack.getOrDefault(ElectrodynamicsDataComponentTypes.SPEED, 0.0);
    }

    public static SubtypeDrillHead getHead(ItemStack stack) {
        return SubtypeDrillHead.values()[stack.getOrDefault(ElectrodynamicsDataComponentTypes.ENUM, 0)];
    }

    public static void saveHead(ItemStack stack, SubtypeDrillHead head) {
        stack.set(ElectrodynamicsDataComponentTypes.ENUM, head.ordinal());
    }

	@EventBusSubscriber(value = Dist.CLIENT, modid = References.ID, bus = EventBusSubscriber.Bus.MOD)
    private static class ColorHandler {

        @SubscribeEvent
        public static void registerColoredBlocks(RegisterColorHandlersEvent.Item event) {
            DRILLS.forEach(item -> event.register((stack, index) -> {
                if (index == 1) {
                    return getHead(stack).color.color();
                }
                return Color.WHITE.color();
            }, item));
        }

    }

    @Override
    public boolean isAllowedInCreativeTab(CreativeModeTab tab) {
        return creativeTab.value() == tab;
    }

    @Override
    public boolean hasCreativeTab() {
        return creativeTab != null;
    }

}
