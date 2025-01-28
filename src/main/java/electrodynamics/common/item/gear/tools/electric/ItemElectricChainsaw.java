package electrodynamics.common.item.gear.tools.electric;

import java.util.List;

import electrodynamics.api.creativetab.CreativeTabSupplier;
import electrodynamics.api.electricity.formatting.ChatFormatter;
import electrodynamics.api.electricity.formatting.DisplayUnit;
import electrodynamics.api.item.IItemElectric;
import electrodynamics.common.item.gear.tools.electric.utils.ElectricItemTier;
import electrodynamics.prefab.item.ElectricItemProperties;
import electrodynamics.prefab.utilities.ElectroTextUtils;
import electrodynamics.registers.ElectrodynamicsItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;

public class ItemElectricChainsaw extends DiggerItem implements IItemElectric, CreativeTabSupplier {

    private final ElectricItemProperties properties;
    private final Holder<CreativeModeTab> creativeTab;

    public ItemElectricChainsaw(ElectricItemProperties properties, Holder<CreativeModeTab> creativeTab) {
        super(ElectricItemTier.ELECTRIC_CHAINSAW, BlockTags.MINEABLE_WITH_AXE, properties.durability(0).attributes(createAttributes(ElectricItemTier.ELECTRIC_CHAINSAW, 4, -2.4F)));
        this.properties = properties;
        this.creativeTab = creativeTab;
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
        return getJoulesStored(stack) > properties.extract.getJoules() ? super.getDestroySpeed(stack, state) : 0;
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level worldIn, BlockState state, BlockPos pos, LivingEntity entityLiving) {
        extractPower(stack, properties.extract.getJoules(), false);
        return super.mineBlock(stack, worldIn, state, pos, entityLiving);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity pTarget, LivingEntity pAttacker) {
        return super.hurtEnemy(stack, pTarget, pAttacker) && getJoulesStored(stack) > properties.extract.getJoules();
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
        super.appendHoverText(stack, context, tooltip, flagIn);
        tooltip.add(ElectroTextUtils.tooltip("item.electric.info", ElectroTextUtils.ratio(ChatFormatter.getChatDisplayShort(getJoulesStored(stack), DisplayUnit.JOULES), ChatFormatter.getChatDisplayShort(getMaximumCapacity(stack), DisplayUnit.JOULES)).withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.DARK_GRAY));
        tooltip.add(ElectroTextUtils.tooltip("item.electric.voltage", ChatFormatter.getChatDisplayShort(properties.receive.getVoltage(), DisplayUnit.VOLTAGE).withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.DARK_GRAY));
        IItemElectric.addBatteryTooltip(stack, context, tooltip);
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
    public boolean overrideOtherStackedOnMe(ItemStack stack, ItemStack other, Slot slot, ClickAction action, Player player, SlotAccess access) {

        if (!IItemElectric.overrideOtherStackedOnMe(stack, other, slot, action, player, access)) {
            return super.overrideOtherStackedOnMe(stack, other, slot, action, player, access);
        }

        return true;

    }

    @Override
    public boolean isAllowedInCreativeTab(CreativeModeTab tab) {
        return creativeTab.value() == tab;
    }

    @Override
    public boolean hasCreativeTab() {
        return creativeTab != null;
    }

    @Override
    public boolean canPerformAction(ItemStack stack, ItemAbility itemAbility) {
        return getJoulesStored(stack) > properties.extract.getJoules() ? ItemAbilities.DEFAULT_AXE_ACTIONS.contains(itemAbility) || ItemAbilities.DEFAULT_SHEARS_ACTIONS.contains(itemAbility) : false;
    }


}
