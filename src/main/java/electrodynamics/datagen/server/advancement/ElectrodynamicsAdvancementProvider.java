package electrodynamics.datagen.server.advancement;

import static electrodynamics.datagen.utils.AdvancementBuilder.create;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import electrodynamics.Electrodynamics;
import electrodynamics.api.References;
import electrodynamics.common.block.subtype.SubtypeMachine;
import electrodynamics.common.block.subtype.SubtypeOre;
import electrodynamics.common.block.subtype.SubtypeWire;
import electrodynamics.common.condition.ConfigCondition;
import electrodynamics.common.item.subtype.SubtypeRawOre;
import electrodynamics.datagen.utils.AdvancementBuilder;
import electrodynamics.datagen.utils.AdvancementBuilder.AdvancementBackgrounds;
import electrodynamics.prefab.utilities.ElectroTextUtils;
import electrodynamics.registers.ElectrodynamicsItems;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementRewards.Builder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.PlayerTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;

public class ElectrodynamicsAdvancementProvider implements DataProvider {

    public final String modID;

    private final PackOutput.PathProvider pathProvider;
    private final CompletableFuture<HolderLookup.Provider> registries;

    private final List<AdvancementBuilder> builders = new ArrayList<>();

    public ElectrodynamicsAdvancementProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, String modID) {
        this.pathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, "advancements");
        this.registries = registries;
        this.modID = modID;
    }

    public ElectrodynamicsAdvancementProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        this(output, registries, References.ID);
    }

    @Override
    public CompletableFuture<?> run(CachedOutput output) {
        return this.registries.thenCompose(provider -> {
            generate(provider);
            //

            List<CompletableFuture<?>> list = new ArrayList<>();

            Set<ResourceLocation> advancementIds = new HashSet<>();

            for (AdvancementBuilder builder : builders) {

                AdvancementHolder holder = builder.build();

                if (!advancementIds.add(holder.id())) {
                    throw new IllegalStateException("Duplicate advancement " + holder.id());
                }

                Path path = this.pathProvider.json(holder.id());

                list.add(DataProvider.saveStable(output, builder.serializeToJson(provider), path));

            }

            return CompletableFuture.allOf(list.toArray(CompletableFuture[]::new));

        });
    }

    @Override
    public String getName() {
        return modID + " Advancement Provider";
    }

    public AdvancementBuilder advancement(String name) {
        AdvancementBuilder builder = create(ResourceLocation.fromNamespaceAndPath(modID, name));
        builders.add(builder);
        return builder;
    }

    public void generate(HolderLookup.Provider registries) {

        advancement("dispenseguidebook")
                //
                .addCriterion("SpawnIn", PlayerTrigger.TriggerInstance.tick())
                //
                .rewards(Builder.loot(ResourceKey.create(Registries.LOOT_TABLE, Electrodynamics.vanillarl("advancement_reward/electroguidebook"))))
                //
                .condition(new ConfigCondition());

        AdvancementHolder root = advancement("root")
                //
                .display(ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.downgradetransformer), ElectroTextUtils.advancement("root.title").withStyle(ChatFormatting.AQUA), ElectroTextUtils.advancement("root.desc"), AdvancementBackgrounds.STONE, AdvancementType.TASK, true, true, false)
                //
                .addCriterion("HasCraftingTable", InventoryChangeTrigger.TriggerInstance.hasItems(Items.CRAFTING_TABLE))
                //
                .rewards(Builder.experience(10))
                //
                .build();

        // ORES

        AdvancementHolder ores = advancement("ores")
                //
                .display(ElectrodynamicsItems.ITEMS_ORE.getValue(SubtypeOre.tin), ElectroTextUtils.advancement("ores.title").withStyle(ChatFormatting.GRAY), ElectroTextUtils.advancement("ores.desc"), AdvancementBackgrounds.NONE, AdvancementType.TASK, false, false, false)
                //
                .addCriterion("HasWoodenPickaxe", InventoryChangeTrigger.TriggerInstance.hasItems(Items.WOODEN_PICKAXE))
                //
                .rewards(Builder.experience(10))
                //
                .parent(root)
                //
                .build();

        advancement("raworevanadium")
                //
                .display(ElectrodynamicsItems.ITEMS_RAWORE.getValue(SubtypeRawOre.vanadinite), ElectroTextUtils.advancement("rawvanadium.title").withStyle(ChatFormatting.GRAY), ElectroTextUtils.advancement("rawvanadium.desc"), AdvancementBackgrounds.NONE, AdvancementType.TASK, true, true, false)
                //
                .addCriterion("HasRawOre", InventoryChangeTrigger.TriggerInstance.hasItems(ElectrodynamicsItems.ITEMS_RAWORE.getValue(SubtypeRawOre.vanadinite)))
                //
                .rewards(Builder.experience(10))
                //
                .parent(ores);

        advancement("raworeuranium")
                //
                .display(ElectrodynamicsItems.ITEMS_RAWORE.getValue(SubtypeRawOre.uranium), ElectroTextUtils.advancement("rawuranium.title").withStyle(ChatFormatting.GRAY), ElectroTextUtils.advancement("rawuranium.desc"), AdvancementBackgrounds.NONE, AdvancementType.TASK, true, true, false)
                //
                .addCriterion("HasRawOre", InventoryChangeTrigger.TriggerInstance.hasItems(ElectrodynamicsItems.ITEMS_RAWORE.getValue(SubtypeRawOre.uranium)))
                //
                .rewards(Builder.experience(10))
                //
                .parent(ores);

        advancement("raworechromium")
                //
                .display(ElectrodynamicsItems.ITEMS_RAWORE.getValue(SubtypeRawOre.chromium), ElectroTextUtils.advancement("rawchromium.title").withStyle(ChatFormatting.GRAY), ElectroTextUtils.advancement("rawchromium.desc"), AdvancementBackgrounds.NONE, AdvancementType.TASK, true, true, false)
                //
                .addCriterion("HasRawOre", InventoryChangeTrigger.TriggerInstance.hasItems(ElectrodynamicsItems.ITEMS_RAWORE.getValue(SubtypeRawOre.chromium)))
                //
                .rewards(Builder.experience(10))
                //
                .parent(ores);

        advancement("rawfluorite")
                //
                .display(ElectrodynamicsItems.ITEMS_RAWORE.getValue(SubtypeRawOre.fluorite), ElectroTextUtils.advancement("rawfluorite.title").withStyle(ChatFormatting.GRAY), ElectroTextUtils.advancement("rawfluorite.desc"), AdvancementBackgrounds.NONE, AdvancementType.TASK, true, true, false)
                //
                .addCriterion("HasRawOre", InventoryChangeTrigger.TriggerInstance.hasItems(ElectrodynamicsItems.ITEMS_RAWORE.getValue(SubtypeRawOre.fluorite)))
                //
                .rewards(Builder.experience(10))
                //
                .parent(ores);

        advancement("raworelead")
                //
                .display(ElectrodynamicsItems.ITEMS_RAWORE.getValue(SubtypeRawOre.lead), ElectroTextUtils.advancement("rawlead.title").withStyle(ChatFormatting.GRAY), ElectroTextUtils.advancement("rawlead.desc"), AdvancementBackgrounds.NONE, AdvancementType.TASK, true, true, false)
                //
                .addCriterion("HasRawOre", InventoryChangeTrigger.TriggerInstance.hasItems(ElectrodynamicsItems.ITEMS_RAWORE.getValue(SubtypeRawOre.lead)))
                //
                .rewards(Builder.experience(10))
                //
                .parent(ores);

        advancement("raworelithium")
                //
                .display(ElectrodynamicsItems.ITEMS_RAWORE.getValue(SubtypeRawOre.lepidolite), ElectroTextUtils.advancement("rawlithium.title").withStyle(ChatFormatting.GRAY), ElectroTextUtils.advancement("rawlithium.desc"), AdvancementBackgrounds.NONE, AdvancementType.TASK, true, true, false)
                //
                .addCriterion("HasRawOre", InventoryChangeTrigger.TriggerInstance.hasItems(ElectrodynamicsItems.ITEMS_RAWORE.getValue(SubtypeRawOre.lepidolite)))
                //
                .rewards(Builder.experience(10))
                //
                .parent(ores);

        advancement("raworesilver")
                //
                .display(ElectrodynamicsItems.ITEMS_RAWORE.getValue(SubtypeRawOre.silver), ElectroTextUtils.advancement("rawsilver.title").withStyle(ChatFormatting.GRAY), ElectroTextUtils.advancement("rawsilver.desc"), AdvancementBackgrounds.NONE, AdvancementType.TASK, true, true, false)
                //
                .addCriterion("HasRawOre", InventoryChangeTrigger.TriggerInstance.hasItems(ElectrodynamicsItems.ITEMS_RAWORE.getValue(SubtypeRawOre.silver)))
                //
                .rewards(Builder.experience(10))
                //
                .parent(ores);

        advancement("raworethorium")
                //
                .display(ElectrodynamicsItems.ITEMS_RAWORE.getValue(SubtypeRawOre.thorium), ElectroTextUtils.advancement("rawthorium.title").withStyle(ChatFormatting.GRAY), ElectroTextUtils.advancement("rawthorium.desc"), AdvancementBackgrounds.NONE, AdvancementType.TASK, true, true, false)
                //
                .addCriterion("HasRawOre", InventoryChangeTrigger.TriggerInstance.hasItems(ElectrodynamicsItems.ITEMS_RAWORE.getValue(SubtypeRawOre.thorium)))
                //
                .rewards(Builder.experience(10))
                //
                .parent(ores);

        advancement("raworetin")
                //
                .display(ElectrodynamicsItems.ITEMS_RAWORE.getValue(SubtypeRawOre.tin), ElectroTextUtils.advancement("rawtin.title").withStyle(ChatFormatting.GRAY), ElectroTextUtils.advancement("rawtin.desc"), AdvancementBackgrounds.NONE, AdvancementType.TASK, true, true, false)
                //
                .addCriterion("HasRawOre", InventoryChangeTrigger.TriggerInstance.hasItems(ElectrodynamicsItems.ITEMS_RAWORE.getValue(SubtypeRawOre.tin)))
                //
                .rewards(Builder.experience(10))
                //
                .parent(ores);

        advancement("raworetitanium")
                //
                .display(ElectrodynamicsItems.ITEMS_RAWORE.getValue(SubtypeRawOre.titanium), ElectroTextUtils.advancement("rawtitanium.title").withStyle(ChatFormatting.GRAY), ElectroTextUtils.advancement("rawtitanium.desc"), AdvancementBackgrounds.NONE, AdvancementType.TASK, true, true, false)
                //
                .addCriterion("HasRawOre", InventoryChangeTrigger.TriggerInstance.hasItems(ElectrodynamicsItems.ITEMS_RAWORE.getValue(SubtypeRawOre.titanium)))
                //
                .rewards(Builder.experience(10))
                //
                .parent(ores);

        // WIRING

        AdvancementHolder basicWiring = advancement("basicwiring")
                //
                .display(ElectrodynamicsItems.ITEMS_WIRE.getValue(SubtypeWire.copper), ElectroTextUtils.advancement("basicwiring.title").withStyle(ChatFormatting.GRAY), ElectroTextUtils.advancement("basicwiring.desc"), AdvancementBackgrounds.NONE, AdvancementType.TASK, true, true, false)
                //
                .addCriterion("HasCopperWire", InventoryChangeTrigger.TriggerInstance.hasItems(ElectrodynamicsItems.ITEMS_WIRE.getValue(SubtypeWire.copper)))
                //
                .rewards(Builder.experience(20))
                //
                .parent(root)
                //
                .build();

        AdvancementHolder betterWiring = advancement("betterwiring")
                //
                .display(ElectrodynamicsItems.ITEMS_WIRE.getValue(SubtypeWire.silver), ElectroTextUtils.advancement("betterwiring.title").withStyle(ChatFormatting.GRAY), ElectroTextUtils.advancement("betterwiring.desc"), AdvancementBackgrounds.NONE, AdvancementType.TASK, true, true, false)
                //
                .addCriterion("HasSilverWire", InventoryChangeTrigger.TriggerInstance.hasItems(ElectrodynamicsItems.ITEMS_WIRE.getValue(SubtypeWire.silver)))
                //
                .rewards(Builder.experience(20))
                //
                .parent(basicWiring)
                //
                .build();

        AdvancementHolder superiorWiring = advancement("superiorwiring")
                //
                .display(ElectrodynamicsItems.ITEMS_WIRE.getValue(SubtypeWire.gold), ElectroTextUtils.advancement("superiorwiring.title").withStyle(ChatFormatting.GRAY), ElectroTextUtils.advancement("superiorwiring.desc"), AdvancementBackgrounds.NONE, AdvancementType.TASK, true, true, false)
                //
                .addCriterion("HasGoldWire", InventoryChangeTrigger.TriggerInstance.hasItems(ElectrodynamicsItems.ITEMS_WIRE.getValue(SubtypeWire.gold)))
                //
                .rewards(Builder.experience(20))
                //
                .parent(betterWiring)
                //
                .build();

        advancement("superconductivewiring")
                //
                .display(ElectrodynamicsItems.ITEMS_WIRE.getValue(SubtypeWire.superconductive), ElectroTextUtils.advancement("superconductivewiring.title").withStyle(ChatFormatting.GRAY), ElectroTextUtils.advancement("superconductivewiring.desc"), AdvancementBackgrounds.NONE, AdvancementType.TASK, true, true, false)
                //
                .addCriterion("HasSuperconductiveWire", InventoryChangeTrigger.TriggerInstance.hasItems(ElectrodynamicsItems.ITEMS_WIRE.getValue(SubtypeWire.superconductive)))
                //
                .rewards(Builder.experience(50))
                //
                .parent(superiorWiring);

        // INSULATION

        AdvancementHolder insulation = advancement("insulation")
                //
                .display(ElectrodynamicsItems.ITEMS_WIRE.getValue(SubtypeWire.insulatedcopperblack), ElectroTextUtils.advancement("insulatedwiring.title").withStyle(ChatFormatting.GRAY), ElectroTextUtils.advancement("insulation.desc"), AdvancementBackgrounds.NONE, AdvancementType.TASK, true, true, false)
                //
                .addCriterion("HasInsulation", InventoryChangeTrigger.TriggerInstance.hasItems(ElectrodynamicsItems.ITEM_INSULATION.get()))
                //
                .rewards(Builder.experience(20))
                //
                .parent(basicWiring)
                //
                .build();

        AdvancementHolder insulatedWiring = advancement("insulatedwiring")
                //
                .display(ElectrodynamicsItems.ITEM_INSULATION.get(), ElectroTextUtils.advancement("insulatedwiring.title").withStyle(ChatFormatting.GRAY), ElectroTextUtils.advancement("insulatedwiring.desc"), AdvancementBackgrounds.NONE, AdvancementType.TASK, true, true, false)
                //
                .addCriterion("HasInsulatedCopperWire", InventoryChangeTrigger.TriggerInstance.hasItems(ElectrodynamicsItems.ITEMS_WIRE.getValue(SubtypeWire.insulatedcopperblack)))
                //
                .rewards(Builder.experience(20))
                //
                .parent(insulation)
                //
                .build();

        advancement("highlyinsulatedwiring")
                //
                .display(ElectrodynamicsItems.ITEMS_WIRE.getValue(SubtypeWire.highlyinsulatedcopperblack), ElectroTextUtils.advancement("highlyinsulatedwiring.title").withStyle(ChatFormatting.GRAY), ElectroTextUtils.advancement("highlyinsulatedwiring.desc"), AdvancementBackgrounds.NONE, AdvancementType.TASK, true, true, false)
                //
                .addCriterion("HasHighlyInsulatedCopperWire", InventoryChangeTrigger.TriggerInstance.hasItems(ElectrodynamicsItems.ITEMS_WIRE.getValue(SubtypeWire.highlyinsulatedcopperblack)))
                //
                .rewards(Builder.experience(20))
                //
                .parent(insulatedWiring);

        advancement("ceramicinsulation")
                //
                .display(ElectrodynamicsItems.ITEM_CERAMICINSULATION.get(), ElectroTextUtils.advancement("ceramicinsulation.title").withStyle(ChatFormatting.GRAY), ElectroTextUtils.advancement("ceramicinsulation.desc"), AdvancementBackgrounds.NONE, AdvancementType.TASK, true, true, false)
                //
                .addCriterion("HasCeramicInsulation", InventoryChangeTrigger.TriggerInstance.hasItems(ElectrodynamicsItems.ITEM_CERAMICINSULATION.get()))
                //
                .rewards(Builder.experience(30))
                //
                .parent(insulation);

        advancement("ceramicinsulatedwiring")
                //
                .display(ElectrodynamicsItems.ITEMS_WIRE.getValue(SubtypeWire.ceramicinsulatedcopperbrown), ElectroTextUtils.advancement("ceramicinsulatedwiring.title").withStyle(ChatFormatting.GRAY), ElectroTextUtils.advancement("ceramicinsulatedwiring.desc"), AdvancementBackgrounds.NONE, AdvancementType.TASK, true, true, false)
                //
                .addCriterion("HasCeramicInsulatedCopperWire", InventoryChangeTrigger.TriggerInstance.hasItems(ElectrodynamicsItems.ITEMS_WIRE.getValue(SubtypeWire.ceramicinsulatedcopperbrown)))
                //
                .rewards(Builder.experience(15))
                //
                .parent(insulatedWiring);

        // WIRING DEVICES

        advancement("downgradetransformer")
                //
                .display(ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.downgradetransformer), ElectroTextUtils.advancement("downgradetransformer.title").withStyle(ChatFormatting.GOLD), ElectroTextUtils.advancement("downgradetransformer.desc"), AdvancementBackgrounds.NONE, AdvancementType.TASK, true, true, false)
                //
                .addCriterion("HasTransformer", InventoryChangeTrigger.TriggerInstance.hasItems(ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.downgradetransformer)))
                //
                .rewards(Builder.experience(25))
                //
                .parent(basicWiring);

        advancement("upgradetransformer")
                //
                .display(ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.upgradetransformer), ElectroTextUtils.advancement("upgradetransformer.title").withStyle(ChatFormatting.LIGHT_PURPLE), ElectroTextUtils.advancement("upgradetransformer.desc"), AdvancementBackgrounds.NONE, AdvancementType.TASK, true, true, false)
                //
                .addCriterion("HasTransformer", InventoryChangeTrigger.TriggerInstance.hasItems(ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.upgradetransformer)))
                //
                .rewards(Builder.experience(25))
                //
                .parent(basicWiring);

        advancement("circuitbreaker")
                //
                .display(ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.circuitbreaker), ElectroTextUtils.advancement("circuitbreaker.title").withStyle(ChatFormatting.GRAY), ElectroTextUtils.advancement("circuitbreaker.desc"), AdvancementBackgrounds.NONE, AdvancementType.TASK, true, true, false)
                //
                .addCriterion("HasCircuitBreaker", InventoryChangeTrigger.TriggerInstance.hasItems(ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.circuitbreaker)))
                //
                .rewards(Builder.experience(30))
                //
                .parent(basicWiring);

        // GENERATORS

        AdvancementHolder coalGenerator = advancement("coalgenerator")
                //
                .display(ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.coalgenerator), ElectroTextUtils.advancement("coalgenerator.title").withStyle(ChatFormatting.GOLD), ElectroTextUtils.advancement("coalgenerator.desc"), AdvancementBackgrounds.NONE, AdvancementType.TASK, true, true, false)
                //
                .addCriterion("HasCoalGenerator", InventoryChangeTrigger.TriggerInstance.hasItems(ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.coalgenerator)))
                //
                .rewards(Builder.experience(25))
                //
                .parent(root)
                //
                .build();

        advancement("thermoelectricgenerator")
                //
                .display(ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.thermoelectricgenerator), ElectroTextUtils.advancement("thermoelectricgenerator.title").withStyle(ChatFormatting.RED), ElectroTextUtils.advancement("thermoelectricgenerator.desc"), AdvancementBackgrounds.NONE, AdvancementType.TASK, true, true, false)
                //
                .addCriterion("HasThermoGenerator", InventoryChangeTrigger.TriggerInstance.hasItems(ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.thermoelectricgenerator)))
                //
                .rewards(Builder.experience(20))
                //
                .parent(coalGenerator);

        AdvancementHolder solarPanel = advancement("solarpanel")
                //
                .display(ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.solarpanel), ElectroTextUtils.advancement("solarpanel.title").withStyle(ChatFormatting.BLUE), ElectroTextUtils.advancement("solarpanel.desc"), AdvancementBackgrounds.NONE, AdvancementType.TASK, true, true, false)
                //
                .addCriterion("HasPanel", InventoryChangeTrigger.TriggerInstance.hasItems(ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.solarpanel)))
                //
                .rewards(Builder.experience(25))
                //
                .parent(coalGenerator)
                //
                .build();

        advancement("advancedsolarpanel")
                //
                .display(ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.advancedsolarpanel), ElectroTextUtils.advancement("advancedsolarpanel.title").withStyle(ChatFormatting.DARK_BLUE), ElectroTextUtils.advancement("advancedsolarpanel.desc"), AdvancementBackgrounds.NONE, AdvancementType.TASK, true, true, false)
                //
                .addCriterion("HasPanel", InventoryChangeTrigger.TriggerInstance.hasItems(ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.advancedsolarpanel)))
                //
                .rewards(Builder.experience(75))
                //
                .parent(solarPanel);

        // BATTERY BOXES

        AdvancementHolder batteryBox = advancement("batterybox")
                //
                .display(ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.batterybox), ElectroTextUtils.advancement("batterybox.title").withStyle(ChatFormatting.GOLD), ElectroTextUtils.advancement("batterybox.desc"), AdvancementBackgrounds.NONE, AdvancementType.TASK, true, true, false)
                //
                .addCriterion("HasBatteryBox", InventoryChangeTrigger.TriggerInstance.hasItems(ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.batterybox)))
                //
                .rewards(Builder.experience(30))
                //
                .parent(coalGenerator)
                //
                .build();

        AdvancementHolder lithiumBatteryBox = advancement("lithiumbatterybox")
                //
                .display(ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.lithiumbatterybox), ElectroTextUtils.advancement("lithiumbatterybox.title").withStyle(ChatFormatting.BLUE), ElectroTextUtils.advancement("lithiumbatterybox.desc"), AdvancementBackgrounds.NONE, AdvancementType.TASK, true, true, false)
                //
                .addCriterion("HasBatteryBox", InventoryChangeTrigger.TriggerInstance.hasItems(ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.lithiumbatterybox)))
                //
                .rewards(Builder.experience(70))
                //
                .parent(batteryBox)
                //
                .build();

        advancement("carbynebatterybox")
                //
                .display(ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.carbynebatterybox), ElectroTextUtils.advancement("carbynebatterybox.title").withStyle(ChatFormatting.RED), ElectroTextUtils.advancement("carbynebatterybox.desc"), AdvancementBackgrounds.NONE, AdvancementType.TASK, true, true, false)
                //
                .addCriterion("HasBatteryBox", InventoryChangeTrigger.TriggerInstance.hasItems(ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.carbynebatterybox)))
                //
                .rewards(Builder.experience(150))
                //
                .parent(lithiumBatteryBox);

        // WIRE MILLS

        AdvancementHolder wiremill = advancement("wiremill")
                //
                .display(ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.wiremill), ElectroTextUtils.advancement("wiremill.title").withStyle(ChatFormatting.GOLD), ElectroTextUtils.advancement("wiremill.desc"), AdvancementBackgrounds.NONE, AdvancementType.TASK, true, true, false)
                //
                .addCriterion("HasMill", InventoryChangeTrigger.TriggerInstance.hasItems(ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.wiremill)))
                //
                .rewards(Builder.experience(20))
                //
                .parent(coalGenerator)
                //
                .build();

        AdvancementHolder doubleWiremill = advancement("doublewiremill")
                //
                .display(ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.wiremilldouble), ElectroTextUtils.advancement("doublewiremill.title").withStyle(ChatFormatting.BLUE), ElectroTextUtils.advancement("doublewiremill.desc"), AdvancementBackgrounds.NONE, AdvancementType.TASK, true, true, false)
                //
                .addCriterion("HasMill", InventoryChangeTrigger.TriggerInstance.hasItems(ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.wiremilldouble)))
                //
                .rewards(Builder.experience(50))
                //
                .parent(wiremill)
                //
                .build();

        advancement("triplewiremill")
                //
                .display(ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.wiremilltriple), ElectroTextUtils.advancement("triplewiremill.title").withStyle(ChatFormatting.RED), ElectroTextUtils.advancement("triplewiremill.desc"), AdvancementBackgrounds.NONE, AdvancementType.TASK, true, true, false)
                //
                .addCriterion("HasMill", InventoryChangeTrigger.TriggerInstance.hasItems(ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.wiremilltriple)))
                //
                .rewards(Builder.experience(100))
                //
                .parent(doubleWiremill);

        // ELECTRIC FURNACE

        AdvancementHolder electricFurnace = advancement("electricfurnace")
                //
                .display(ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.electricfurnace), ElectroTextUtils.advancement("electricfurnace.title").withStyle(ChatFormatting.GOLD), ElectroTextUtils.advancement("electricfurnace.desc"), AdvancementBackgrounds.NONE, AdvancementType.TASK, true, true, false)
                //
                .addCriterion("HasFurnace", InventoryChangeTrigger.TriggerInstance.hasItems(ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.electricfurnace)))
                //
                .rewards(Builder.experience(30))
                //
                .parent(coalGenerator)
                //
                .build();

        AdvancementHolder doubleElectricFurnace = advancement("doubleelectricfurnace")
                //
                .display(ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.electricfurnacedouble), ElectroTextUtils.advancement("doubleelectricfurnace.title").withStyle(ChatFormatting.BLUE), ElectroTextUtils.advancement("doubleelectricfurnace.desc"), AdvancementBackgrounds.NONE, AdvancementType.TASK, true, true, false)
                //
                .addCriterion("HasFurnace", InventoryChangeTrigger.TriggerInstance.hasItems(ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.electricfurnacedouble)))
                //
                .rewards(Builder.experience(50))
                //
                .parent(electricFurnace)
                //
                .build();

        advancement("tripleelectricfurnace")
                //
                .display(ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.electricfurnacetriple), ElectroTextUtils.advancement("tripleelectricfurnace.title").withStyle(ChatFormatting.RED), ElectroTextUtils.advancement("tripleelectricfurnace.desc"), AdvancementBackgrounds.NONE, AdvancementType.TASK, true, true, false)
                //
                .addCriterion("HasFurnace", InventoryChangeTrigger.TriggerInstance.hasItems(ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.electricfurnacetriple)))
                //
                .rewards(Builder.experience(100))
                //
                .parent(doubleElectricFurnace);

        // ELECTRIC ARC FURNACE

        AdvancementHolder electricArcFurnace = advancement("electricarcfurnace")
                //
                .display(ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.electricarcfurnace), ElectroTextUtils.advancement("electricarcfurnace.title").withStyle(ChatFormatting.GOLD), ElectroTextUtils.advancement("electricarcfurnace.desc"), AdvancementBackgrounds.NONE, AdvancementType.TASK, true, true, false)
                //
                .addCriterion("HasFurnace", InventoryChangeTrigger.TriggerInstance.hasItems(ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.electricarcfurnace)))
                //
                .rewards(Builder.experience(30))
                //
                .parent(coalGenerator)
                //
                .build();

        AdvancementHolder doubleElectricArcFurnace = advancement("doubleelectricarcfurnace")
                //
                .display(ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.electricarcfurnacedouble), ElectroTextUtils.advancement("doubleelectricarcfurnace.title").withStyle(ChatFormatting.BLUE), ElectroTextUtils.advancement("doubleelectricarcfurnace.desc"), AdvancementBackgrounds.NONE, AdvancementType.TASK, true, true, false)
                //
                .addCriterion("HasFurnace", InventoryChangeTrigger.TriggerInstance.hasItems(ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.electricarcfurnacedouble)))
                //
                .rewards(Builder.experience(50))
                //
                .parent(electricArcFurnace)
                //
                .build();

        advancement("tripleelectricarcfurnace")
                //
                .display(ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.electricarcfurnacetriple), ElectroTextUtils.advancement("tripleelectricarcfurnace.title").withStyle(ChatFormatting.RED), ElectroTextUtils.advancement("tripleelectricarcfurnace.desc"), AdvancementBackgrounds.NONE, AdvancementType.TASK, true, true, false)
                //
                .addCriterion("HasFurnace", InventoryChangeTrigger.TriggerInstance.hasItems(ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.electricarcfurnacetriple)))
                //
                .rewards(Builder.experience(150))
                //
                .parent(doubleElectricArcFurnace);

        // MINERAL GRINDER

        AdvancementHolder mineralGrinder = advancement("mineralgrinder")
                //
                .display(ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.mineralgrinder), ElectroTextUtils.advancement("mineralgrinder.title").withStyle(ChatFormatting.GOLD), ElectroTextUtils.advancement("mineralgrinder.desc"), AdvancementBackgrounds.NONE, AdvancementType.TASK, true, true, false)
                //
                .addCriterion("HasGrinder", InventoryChangeTrigger.TriggerInstance.hasItems(ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.mineralgrinder)))
                //
                .rewards(Builder.experience(20))
                //
                .parent(coalGenerator)
                //
                .build();

        AdvancementHolder doubleMineralGrinder = advancement("doublemineralgrinder")
                //
                .display(ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.mineralgrinderdouble), ElectroTextUtils.advancement("doublemineralgrinder.title").withStyle(ChatFormatting.BLUE), ElectroTextUtils.advancement("doublemineralgrinder.desc"), AdvancementBackgrounds.NONE, AdvancementType.TASK, true, true, false)
                //
                .addCriterion("HasGrinder", InventoryChangeTrigger.TriggerInstance.hasItems(ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.mineralgrinderdouble)))
                //
                .rewards(Builder.experience(50))
                //
                .parent(mineralGrinder)
                //
                .build();

        advancement("triplemineralgrinder")
                //
                .display(ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.mineralgrindertriple), ElectroTextUtils.advancement("triplemineralgrinder.title").withStyle(ChatFormatting.RED), ElectroTextUtils.advancement("triplemineralgrinder.desc"), AdvancementBackgrounds.NONE, AdvancementType.TASK, true, true, false)
                //
                .addCriterion("HasGrinder", InventoryChangeTrigger.TriggerInstance.hasItems(ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.mineralgrindertriple)))
                //
                .rewards(Builder.experience(100))
                //
                .parent(doubleMineralGrinder);

        // MINERAL CRUSHER

        AdvancementHolder mineralCrusher = advancement("mineralcrusher")
                //
                .display(ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.mineralcrusher), ElectroTextUtils.advancement("mineralcrusher.title").withStyle(ChatFormatting.GOLD), ElectroTextUtils.advancement("mineralcrusher.desc"), AdvancementBackgrounds.NONE, AdvancementType.TASK, true, true, false)
                //
                .addCriterion("HasCrusher", InventoryChangeTrigger.TriggerInstance.hasItems(ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.mineralcrusher)))
                //
                .rewards(Builder.experience(30))
                //
                .parent(coalGenerator)
                //
                .build();

        AdvancementHolder doubleMineralCrusher = advancement("doublemineralcrusher")
                //
                .display(ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.mineralcrusherdouble), ElectroTextUtils.advancement("doublemineralcrusher.title").withStyle(ChatFormatting.BLUE), ElectroTextUtils.advancement("doublemineralcrusher.desc"), AdvancementBackgrounds.NONE, AdvancementType.TASK, true, true, false)
                //
                .addCriterion("HasCrusher", InventoryChangeTrigger.TriggerInstance.hasItems(ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.mineralcrusherdouble)))
                //
                .rewards(Builder.experience(70))
                //
                .parent(mineralCrusher)
                //
                .build();

        advancement("triplemineralcrusher")
                //
                .display(ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.mineralcrushertriple), ElectroTextUtils.advancement("triplemineralcrusher.title").withStyle(ChatFormatting.RED), ElectroTextUtils.advancement("triplemineralcrusher.desc"), AdvancementBackgrounds.NONE, AdvancementType.TASK, true, true, false)
                //
                .addCriterion("HasCrusher", InventoryChangeTrigger.TriggerInstance.hasItems(ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.mineralcrushertriple)))
                //
                .rewards(Builder.experience(150))
                //
                .parent(doubleMineralCrusher);

        // MISC

        advancement("multimeter")
                //
                .display(ElectrodynamicsItems.ITEM_MULTIMETER.get(), ElectroTextUtils.advancement("multimeter.title").withStyle(ChatFormatting.GRAY), ElectroTextUtils.advancement("multimeter.desc"), AdvancementBackgrounds.NONE, AdvancementType.TASK, true, true, false)
                //
                .addCriterion("HasMeter", InventoryChangeTrigger.TriggerInstance.hasItems(ElectrodynamicsItems.ITEM_MULTIMETER.get()))
                //
                .rewards(Builder.experience(10))
                //
                .parent(basicWiring);
    }

}
