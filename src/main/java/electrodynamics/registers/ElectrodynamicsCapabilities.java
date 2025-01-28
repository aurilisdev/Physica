package electrodynamics.registers;

import org.jetbrains.annotations.Nullable;

import electrodynamics.Electrodynamics;
import electrodynamics.api.References;
import electrodynamics.api.capability.types.electrodynamic.ICapabilityElectrodynamic;
import electrodynamics.api.capability.types.fluid.RestrictedFluidHandlerItemStack;
import electrodynamics.api.capability.types.gas.IGasHandler;
import electrodynamics.api.capability.types.gas.IGasHandlerItem;
import electrodynamics.api.capability.types.itemhandler.CapabilityItemStackHandler;
import electrodynamics.api.capability.types.locationstorage.ILocationStorage;
import electrodynamics.api.gas.GasHandlerItemStack;
import electrodynamics.common.item.ItemUpgrade;
import electrodynamics.common.item.gear.armor.types.ItemCombatArmor;
import electrodynamics.common.item.gear.armor.types.ItemHydraulicBoots;
import electrodynamics.common.item.gear.armor.types.ItemJetpack;
import electrodynamics.common.item.gear.tools.ItemCanister;
import electrodynamics.common.item.gear.tools.ItemPortableCylinder;
import electrodynamics.common.item.gear.tools.electric.ItemElectricDrill;
import electrodynamics.common.item.gear.tools.electric.ItemSeismicScanner;
import electrodynamics.common.item.gear.tools.electric.utils.ItemRailgun;
import electrodynamics.compatibility.mekanism.MekanismHandler;
import electrodynamics.prefab.tile.GenericTile;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.ItemCapability;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

@EventBusSubscriber(modid = References.ID, bus = EventBusSubscriber.Bus.MOD)
public class ElectrodynamicsCapabilities {

    public static final double DEFAULT_VOLTAGE = 120.0;
    public static final String LOCATION_KEY = "location";

    public static final BlockCapability<ICapabilityElectrodynamic, @Nullable Direction> CAPABILITY_ELECTRODYNAMIC_BLOCK = BlockCapability.createSided(Electrodynamics.rl("electrodynamicblock"), ICapabilityElectrodynamic.class);

    public static final ItemCapability<ILocationStorage, Void> CAPABILITY_LOCATIONSTORAGE_ITEM = ItemCapability.createVoid(Electrodynamics.rl("locationstorageitem"), ILocationStorage.class);

    public static final ItemCapability<IGasHandlerItem, Void> CAPABILITY_GASHANDLER_ITEM = ItemCapability.createVoid(Electrodynamics.rl("gashandleritem"), IGasHandlerItem.class);
    public static final BlockCapability<IGasHandler, @Nullable Direction> CAPABILITY_GASHANDLER_BLOCK = BlockCapability.createSided(Electrodynamics.rl("gashandlerblock"), IGasHandler.class);

    @SubscribeEvent
    public static void register(RegisterCapabilitiesEvent event) {

        /* ITEMS */

        // Electric Drill

        event.registerItem(Capabilities.ItemHandler.ITEM, (itemStack, context) -> new CapabilityItemStackHandler(ItemElectricDrill.SLOT_COUNT, itemStack)
                //
                .setOnChange((onChangeWrapper) -> {
                    //
                    int fortune = 0;
                    boolean silkTouch = false;
                    double speedBoost = 1;

                    for (ItemStack content : onChangeWrapper.capability().getItems()) {
                        if (!content.isEmpty() && content.getItem() instanceof ItemUpgrade upgrade && upgrade.subtype.isEmpty) {
                            for (int i = 0; i < content.getCount(); i++) {

                                switch (upgrade.subtype) {

                                    case basicspeed:
                                        speedBoost = Math.min(speedBoost * 1.5, Math.pow(1.5, 3));
                                        break;
                                    case advancedspeed:
                                        speedBoost = Math.min(speedBoost * 2.25, Math.pow(2.25, 3));
                                        break;
                                    case fortune:

                                        if (!silkTouch) {
                                            fortune = Math.min(fortune + 1, 9);
                                        }
                                        break;
                                    case silktouch:
                                        if (fortune == 0) {
                                            silkTouch = true;
                                        }
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }
                    }

                    final int finalFortune = fortune;
                    final boolean finalSilkTouch = silkTouch;

                    onChangeWrapper.levelAccess().execute((level, pos) -> {

                        ItemStack stack = onChangeWrapper.owner();

                        Holder<Enchantment> fortuneEnchantment = level.registryAccess().registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(Enchantments.FORTUNE);

                        EnchantmentHelper.updateEnchantments(stack, enchantments -> enchantments.set(fortuneEnchantment, 0));
                        EnchantmentHelper.updateEnchantments(stack, enchantments -> enchantments.set(fortuneEnchantment, finalFortune));

                        Holder<Enchantment> silkTouchEnchantment = level.registryAccess().registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(Enchantments.FORTUNE);

                        EnchantmentHelper.updateEnchantments(stack, enchantments -> enchantments.set(silkTouchEnchantment, 0));
                        EnchantmentHelper.updateEnchantments(stack, enchantments -> enchantments.set(silkTouchEnchantment, finalSilkTouch ? 1 : 0));

                    });

                    onChangeWrapper.owner().set(ElectrodynamicsDataComponentTypes.SPEED, speedBoost);

                    double multiplier = 1;

                    if (silkTouch) {
                        multiplier += 3;
                    }

                    if (fortune > 0) {
                        multiplier += fortune;
                    }

                    onChangeWrapper.owner().set(ElectrodynamicsDataComponentTypes.POWER_USAGE, ItemElectricDrill.POWER_USAGE * multiplier);

                }), ElectrodynamicsItems.ITEM_ELECTRICDRILL.get());

        // Seismic Scanner

        event.registerItem(Capabilities.ItemHandler.ITEM, (itemStack, context) -> new CapabilityItemStackHandler(ItemSeismicScanner.SLOT_COUNT, itemStack).setOnChange(wrapper -> {
            int range = 1;

            for (ItemStack content : wrapper.capability().getItems()) {
                if (!content.isEmpty() && content.getItem() instanceof ItemUpgrade upgrade && upgrade.subtype.isEmpty) {
                    for (int i = 0; i < content.getCount(); i++) {

                        switch (upgrade.subtype) {

                            case range:
                                range = Math.min(4, range + 1);
                                break;
                            default:
                                break;
                        }
                    }
                }
            }

            wrapper.owner().set(ElectrodynamicsDataComponentTypes.RANGE, range);
        }), ElectrodynamicsItems.ITEM_SEISMICSCANNER.get());

        // Reinforced Cannister

        event.registerItem(Capabilities.FluidHandler.ITEM, (itemStack, context) -> new RestrictedFluidHandlerItemStack.SwapEmpty(itemStack, itemStack, ItemCanister.MAX_FLUID_CAPACITY), ElectrodynamicsItems.ITEM_CANISTERREINFORCED.get());

        // Portable Cylinder

        event.registerItem(CAPABILITY_GASHANDLER_ITEM, (itemStack, context) -> new GasHandlerItemStack(itemStack, ItemPortableCylinder.MAX_GAS_CAPCITY, ItemPortableCylinder.MAX_TEMPERATURE, ItemPortableCylinder.MAX_PRESSURE), ElectrodynamicsItems.ITEM_PORTABLECYLINDER.get());

        // Jetpack

        event.registerItem(CAPABILITY_GASHANDLER_ITEM, (itemStack, context) -> new GasHandlerItemStack(itemStack, ItemJetpack.MAX_CAPACITY, ItemJetpack.MAX_TEMPERATURE, ItemJetpack.MAX_PRESSURE).setPredicate(ItemJetpack.getGasValidator()), ElectrodynamicsItems.ITEM_JETPACK.get());

        // Hydraulic Boots

        event.registerItem(Capabilities.FluidHandler.ITEM, (itemStack, context) -> new RestrictedFluidHandlerItemStack(itemStack, ItemHydraulicBoots.MAX_CAPACITY).setValidator(ItemHydraulicBoots.getPredicate()), ElectrodynamicsItems.ITEM_HYDRAULICBOOTS.get());

        // Combat Helmet

        event.registerItem(CAPABILITY_GASHANDLER_ITEM, (itemStack, context) -> new GasHandlerItemStack(itemStack, ItemCombatArmor.HELMET_CAPACITY, ItemCombatArmor.HELMET_MAX_TEMP, ItemCombatArmor.HELMET_MAX_PRESSURE).setPredicate(ItemCombatArmor.getHelmetGasValidator()), ElectrodynamicsItems.ITEM_COMBATHELMET.get());

        // Combat Chestplate

        event.registerItem(CAPABILITY_GASHANDLER_ITEM, (itemStack, context) -> new GasHandlerItemStack(itemStack, ItemJetpack.MAX_CAPACITY, ItemJetpack.MAX_TEMPERATURE, ItemJetpack.MAX_PRESSURE).setPredicate(ItemJetpack.getGasValidator()), ElectrodynamicsItems.ITEM_COMBATCHESTPLATE.get());

        // Combat Leggings

        event.registerItem(CAPABILITY_GASHANDLER_ITEM, (itemStack, context) -> new GasHandlerItemStack(itemStack, ItemCombatArmor.LEGGINGS_CAPACITY, ItemCombatArmor.LEGGINGS_MAX_TEMP, ItemCombatArmor.LEGGINGS_MAX_PRESSURE).setPredicate(ItemCombatArmor.getLeggingsGasValidator()), ElectrodynamicsItems.ITEM_COMBATLEGGINGS.get());

        // Combat Boots

        event.registerItem(Capabilities.FluidHandler.ITEM, (itemStack, context) -> new RestrictedFluidHandlerItemStack(itemStack, ItemHydraulicBoots.MAX_CAPACITY).setValidator(ItemHydraulicBoots.getPredicate()), ElectrodynamicsItems.ITEM_COMBATBOOTS.get());

        // Railguns

        event.registerItem(Capabilities.FluidHandler.ITEM, (itemStack, context) -> new RestrictedFluidHandlerItemStack(itemStack, ItemRailgun.CAPACITY).setValidator(ItemRailgun.getPredicate()), ElectrodynamicsItems.ITEM_KINETICRAILGUN.get());
        event.registerItem(Capabilities.FluidHandler.ITEM, (itemStack, context) -> new RestrictedFluidHandlerItemStack(itemStack, ItemRailgun.CAPACITY).setValidator(ItemRailgun.getPredicate()), ElectrodynamicsItems.ITEM_PLASMARAILGUN.get());

        /* TILES */

        ElectrodynamicsTiles.BLOCK_ENTITY_TYPES.getEntries().forEach(entry -> {
            event.registerBlockEntity(CAPABILITY_ELECTRODYNAMIC_BLOCK, (BlockEntityType<? extends GenericTile>) entry.get(), (tile, context) -> tile.getElectrodynamicCapability(context));
            event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, (BlockEntityType<? extends GenericTile>) entry.get(), (tile, context) -> tile.getFluidHandlerCapability(context));
            event.registerBlockEntity(CAPABILITY_GASHANDLER_BLOCK, (BlockEntityType<? extends GenericTile>) entry.get(), (tile, context) -> tile.getGasHandlerCapability(context));
            event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, (BlockEntityType<? extends GenericTile>) entry.get(), (tile, context) -> tile.getItemHandlerCapability(context));
        });

        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, ElectrodynamicsTiles.TILE_BATTERYBOX.get(), (tile, context) -> tile.getFECapability(context));

        if(ModList.get().isLoaded(References.MEKANISM_ID)) {
            MekanismHandler.registerCapabilities(event);
        }

    }

}
