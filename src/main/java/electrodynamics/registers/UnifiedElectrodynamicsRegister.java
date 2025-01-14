package electrodynamics.registers;

import electrodynamics.api.electricity.formatting.ChatFormatter;
import electrodynamics.api.electricity.formatting.DisplayUnit;
import electrodynamics.common.block.subtype.SubtypeMachine;
import electrodynamics.common.blockitem.types.BlockItemDescriptable;
import electrodynamics.common.recipe.ElectrodynamicsRecipeInit;
import electrodynamics.common.settings.Constants;
import electrodynamics.prefab.utilities.ElectroTextUtils;
import net.minecraft.ChatFormatting;
import net.neoforged.bus.api.IEventBus;

public class UnifiedElectrodynamicsRegister {

	public static void register(IEventBus bus) {
		ElectrodynamicsBlocks.BLOCKS.register(bus);
		ElectrodynamicsTiles.BLOCK_ENTITY_TYPES.register(bus);
		ElectrodynamicsItems.ITEMS.register(bus);
		ElectrodynamicsFluids.FLUIDS.register(bus);
		ElectrodynamicsFluidTypes.FLUID_TYPES.register(bus);
		ElectrodynamicsEntities.ENTITIES.register(bus);
		ElectrodynamicsMenuTypes.MENU_TYPES.register(bus);
		ElectrodynamicsSounds.SOUNDS.register(bus);
		ElectrodynamicsGases.GASES.register(bus);
		ElectrodynamicsParticles.PARTICLES.register(bus);
		ElectrodynamicsCreativeTabs.CREATIVE_TABS.register(bus);
		ElectrodynamicsRuleTestTypes.RULE_TEST_TYPES.register(bus);
		ElectrodynamicsRecipeInit.INGREDIENT_TYPES.register(bus);
		ElectrodynamicsRecipeInit.RECIPE_TYPES.register(bus);
        ElectrodynamicsRecipeInit.RECIPE_SERIALIZER.register(bus);
		ElectrodynamicsDataComponentTypes.DATA_COMPONENT_TYPES.register(bus);
		ElectrodynamicsArmorMaterials.ARMOR_MATERIALS.register(bus);
	}

	static {
		// machines
		// cleaner and simpler is it not?
		BlockItemDescriptable.addDescription(ElectrodynamicsBlocks.BLOCKS_MACHINE.getHolder(SubtypeMachine.electricfurnace), ElectroTextUtils.voltageTooltip(120));
		BlockItemDescriptable.addDescription(ElectrodynamicsBlocks.BLOCKS_MACHINE.getHolder(SubtypeMachine.electricfurnacedouble), ElectroTextUtils.voltageTooltip(240));
		BlockItemDescriptable.addDescription(ElectrodynamicsBlocks.BLOCKS_MACHINE.getHolder(SubtypeMachine.electricfurnacetriple), ElectroTextUtils.voltageTooltip(480));
		BlockItemDescriptable.addDescription(ElectrodynamicsBlocks.BLOCKS_MACHINE.getHolder(SubtypeMachine.wiremill), ElectroTextUtils.voltageTooltip(120));
		BlockItemDescriptable.addDescription(ElectrodynamicsBlocks.BLOCKS_MACHINE.getHolder(SubtypeMachine.wiremilldouble), ElectroTextUtils.voltageTooltip(240));
		BlockItemDescriptable.addDescription(ElectrodynamicsBlocks.BLOCKS_MACHINE.getHolder(SubtypeMachine.wiremilltriple), ElectroTextUtils.voltageTooltip(480));
		BlockItemDescriptable.addDescription(ElectrodynamicsBlocks.BLOCKS_MACHINE.getHolder(SubtypeMachine.mineralcrusher), ElectroTextUtils.voltageTooltip(240));
		BlockItemDescriptable.addDescription(ElectrodynamicsBlocks.BLOCKS_MACHINE.getHolder(SubtypeMachine.mineralcrusherdouble), ElectroTextUtils.voltageTooltip(480));
		BlockItemDescriptable.addDescription(ElectrodynamicsBlocks.BLOCKS_MACHINE.getHolder(SubtypeMachine.mineralcrushertriple), ElectroTextUtils.voltageTooltip(960));
		BlockItemDescriptable.addDescription(ElectrodynamicsBlocks.BLOCKS_MACHINE.getHolder(SubtypeMachine.mineralgrinder), ElectroTextUtils.voltageTooltip(120));
		BlockItemDescriptable.addDescription(ElectrodynamicsBlocks.BLOCKS_MACHINE.getHolder(SubtypeMachine.mineralgrinderdouble), ElectroTextUtils.voltageTooltip(240));
		BlockItemDescriptable.addDescription(ElectrodynamicsBlocks.BLOCKS_MACHINE.getHolder(SubtypeMachine.mineralgrindertriple), ElectroTextUtils.voltageTooltip(480));
		BlockItemDescriptable.addDescription(ElectrodynamicsBlocks.BLOCKS_MACHINE.getHolder(SubtypeMachine.oxidationfurnace), ElectroTextUtils.voltageTooltip(240));
		BlockItemDescriptable.addDescription(ElectrodynamicsBlocks.BLOCKS_MACHINE.getHolder(SubtypeMachine.mineralwasher), ElectroTextUtils.voltageTooltip(480));
		BlockItemDescriptable.addDescription(ElectrodynamicsBlocks.BLOCKS_MACHINE.getHolder(SubtypeMachine.chemicalmixer), ElectroTextUtils.voltageTooltip(240));
		BlockItemDescriptable.addDescription(ElectrodynamicsBlocks.BLOCKS_MACHINE.getHolder(SubtypeMachine.chemicalcrystallizer), ElectroTextUtils.voltageTooltip(240));
		BlockItemDescriptable.addDescription(ElectrodynamicsBlocks.BLOCKS_MACHINE.getHolder(SubtypeMachine.energizedalloyer), ElectroTextUtils.voltageTooltip(480));
		BlockItemDescriptable.addDescription(ElectrodynamicsBlocks.BLOCKS_MACHINE.getHolder(SubtypeMachine.reinforcedalloyer), ElectroTextUtils.voltageTooltip(960));
		BlockItemDescriptable.addDescription(ElectrodynamicsBlocks.BLOCKS_MACHINE.getHolder(SubtypeMachine.lathe), ElectroTextUtils.voltageTooltip(240));
		BlockItemDescriptable.addDescription(ElectrodynamicsBlocks.BLOCKS_MACHINE.getHolder(SubtypeMachine.chargerlv), ElectroTextUtils.voltageTooltip(120));
		BlockItemDescriptable.addDescription(ElectrodynamicsBlocks.BLOCKS_MACHINE.getHolder(SubtypeMachine.chargermv), ElectroTextUtils.voltageTooltip(240));
		BlockItemDescriptable.addDescription(ElectrodynamicsBlocks.BLOCKS_MACHINE.getHolder(SubtypeMachine.chargerhv), ElectroTextUtils.voltageTooltip(480));
		BlockItemDescriptable.addDescription(ElectrodynamicsBlocks.BLOCKS_MACHINE.getHolder(SubtypeMachine.fermentationplant), ElectroTextUtils.voltageTooltip(120));
		BlockItemDescriptable.addDescription(ElectrodynamicsBlocks.BLOCKS_MACHINE.getHolder(SubtypeMachine.electricpump), ElectroTextUtils.voltageTooltip(120));
		BlockItemDescriptable.addDescription(ElectrodynamicsBlocks.BLOCKS_MACHINE.getHolder(SubtypeMachine.electrolyticseparator), ElectroTextUtils.voltageTooltip(240));
		BlockItemDescriptable.addDescription(ElectrodynamicsBlocks.BLOCKS_MACHINE.getHolder(SubtypeMachine.electricarcfurnace), ElectroTextUtils.voltageTooltip(120));
		BlockItemDescriptable.addDescription(ElectrodynamicsBlocks.BLOCKS_MACHINE.getHolder(SubtypeMachine.electricarcfurnacedouble), ElectroTextUtils.voltageTooltip(240));
		BlockItemDescriptable.addDescription(ElectrodynamicsBlocks.BLOCKS_MACHINE.getHolder(SubtypeMachine.electricarcfurnacetriple), ElectroTextUtils.voltageTooltip(480));
		BlockItemDescriptable.addDescription(ElectrodynamicsBlocks.BLOCKS_MACHINE.getHolder(SubtypeMachine.electrolosischamber), ElectroTextUtils.voltageTooltip(1920));

		BlockItemDescriptable.addDescription(ElectrodynamicsBlocks.BLOCK_COMPRESSOR, ElectroTextUtils.voltageTooltip(120));
		BlockItemDescriptable.addDescription(ElectrodynamicsBlocks.BLOCK_DECOMPRESSOR, ElectroTextUtils.voltageTooltip(120));
		BlockItemDescriptable.addDescription(ElectrodynamicsBlocks.BLOCK_ADVANCEDCOMPRESSOR, ElectroTextUtils.voltageTooltip(120));
		BlockItemDescriptable.addDescription(ElectrodynamicsBlocks.BLOCK_ADVANCEDDECOMPRESSOR, ElectroTextUtils.voltageTooltip(120));
		BlockItemDescriptable.addDescription(ElectrodynamicsBlocks.BLOCK_COMPRESSOR_ADDONTANK, ElectroTextUtils.tooltip("addontankcap", ChatFormatter.formatFluidMilibuckets(Constants.GAS_TRANSFORMER_ADDON_TANK_CAPCITY).withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.DARK_GRAY));
		BlockItemDescriptable.addDescription(ElectrodynamicsBlocks.BLOCK_THERMOELECTRICMANIPULATOR, ElectroTextUtils.voltageTooltip(120));
		BlockItemDescriptable.addDescription(ElectrodynamicsBlocks.BLOCK_ADVANCED_THERMOELECTRICMANIPULATOR, ElectroTextUtils.voltageTooltip(120));
		BlockItemDescriptable.addDescription(ElectrodynamicsBlocks.BLOCKS_MACHINE.getHolder(SubtypeMachine.gaspipepump), ElectroTextUtils.voltageTooltip(120));
		BlockItemDescriptable.addDescription(ElectrodynamicsBlocks.BLOCKS_MACHINE.getHolder(SubtypeMachine.fluidpipepump), ElectroTextUtils.voltageTooltip(120));

		BlockItemDescriptable.addDescription(ElectrodynamicsBlocks.BLOCKS_MACHINE.getHolder(SubtypeMachine.gastanksteel), ElectroTextUtils.tooltip("gastank.capacity", ChatFormatter.getChatDisplayShort(8, DisplayUnit.BUCKETS).withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.DARK_GRAY));
		BlockItemDescriptable.addDescription(ElectrodynamicsBlocks.BLOCKS_MACHINE.getHolder(SubtypeMachine.gastankreinforced), ElectroTextUtils.tooltip("gastank.capacity", ChatFormatter.getChatDisplayShort(32, DisplayUnit.BUCKETS).withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.DARK_GRAY));
		BlockItemDescriptable.addDescription(ElectrodynamicsBlocks.BLOCKS_MACHINE.getHolder(SubtypeMachine.gastankhsla), ElectroTextUtils.tooltip("gastank.capacity", ChatFormatter.getChatDisplayShort(128, DisplayUnit.BUCKETS).withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.DARK_GRAY));
		BlockItemDescriptable.addDescription(ElectrodynamicsBlocks.BLOCK_ROTARYUNIFIER, ElectroTextUtils.tooltip("rotaryunifier.use1").withStyle(ChatFormatting.DARK_GRAY));
		BlockItemDescriptable.addDescription(ElectrodynamicsBlocks.BLOCK_ROTARYUNIFIER, ElectroTextUtils.tooltip("rotaryunifier.use2").withStyle(ChatFormatting.DARK_GRAY));

		// generators
		BlockItemDescriptable.addDescription(ElectrodynamicsBlocks.BLOCKS_MACHINE.getHolder(SubtypeMachine.solarpanel), ElectroTextUtils.voltageTooltip(120));
		BlockItemDescriptable.addDescription(ElectrodynamicsBlocks.BLOCKS_MACHINE.getHolder(SubtypeMachine.advancedsolarpanel), ElectroTextUtils.voltageTooltip(240));
		BlockItemDescriptable.addDescription(ElectrodynamicsBlocks.BLOCKS_MACHINE.getHolder(SubtypeMachine.thermoelectricgenerator), ElectroTextUtils.voltageTooltip(120));
		BlockItemDescriptable.addDescription(ElectrodynamicsBlocks.BLOCKS_MACHINE.getHolder(SubtypeMachine.combustionchamber), ElectroTextUtils.voltageTooltip(120));
		BlockItemDescriptable.addDescription(ElectrodynamicsBlocks.BLOCKS_MACHINE.getHolder(SubtypeMachine.hydroelectricgenerator), ElectroTextUtils.voltageTooltip(120));
		BlockItemDescriptable.addDescription(ElectrodynamicsBlocks.BLOCKS_MACHINE.getHolder(SubtypeMachine.windmill), ElectroTextUtils.voltageTooltip(120));
		BlockItemDescriptable.addDescription(ElectrodynamicsBlocks.BLOCKS_MACHINE.getHolder(SubtypeMachine.coalgenerator), ElectroTextUtils.voltageTooltip(120));

		// misc
		BlockItemDescriptable.addDescription(ElectrodynamicsBlocks.BLOCKS_MACHINE.getHolder(SubtypeMachine.downgradetransformer), ElectroTextUtils.tooltip("transformer.energyloss", ChatFormatter.getChatDisplayShort(Constants.TRANSFORMER_EFFICIENCY * 100, DisplayUnit.PERCENTAGE).withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.DARK_GRAY));
		BlockItemDescriptable.addDescription(ElectrodynamicsBlocks.BLOCKS_MACHINE.getHolder(SubtypeMachine.upgradetransformer), ElectroTextUtils.tooltip("transformer.energyloss", ChatFormatter.getChatDisplayShort(Constants.TRANSFORMER_EFFICIENCY * 100, DisplayUnit.PERCENTAGE).withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.DARK_GRAY));
		BlockItemDescriptable.addDescription(ElectrodynamicsBlocks.BLOCKS_MACHINE.getHolder(SubtypeMachine.advanceddowngradetransformer), ElectroTextUtils.tooltip("transformer.energyloss", ChatFormatter.getChatDisplayShort(Constants.TRANSFORMER_EFFICIENCY * 100, DisplayUnit.PERCENTAGE).withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.DARK_GRAY));
		BlockItemDescriptable.addDescription(ElectrodynamicsBlocks.BLOCKS_MACHINE.getHolder(SubtypeMachine.advancedupgradetransformer), ElectroTextUtils.tooltip("transformer.energyloss", ChatFormatter.getChatDisplayShort(Constants.TRANSFORMER_EFFICIENCY * 100, DisplayUnit.PERCENTAGE).withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.DARK_GRAY));
		BlockItemDescriptable.addDescription(ElectrodynamicsBlocks.BLOCKS_MACHINE.getHolder(SubtypeMachine.circuitbreaker), ElectroTextUtils.tooltip("transformer.energyloss", ChatFormatter.getChatDisplayShort(Constants.CIRCUITBREAKER_EFFICIENCY * 100, DisplayUnit.PERCENTAGE).withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.DARK_GRAY));
		BlockItemDescriptable.addDescription(ElectrodynamicsBlocks.BLOCKS_MACHINE.getHolder(SubtypeMachine.relay), ElectroTextUtils.tooltip("transformer.energyloss", ChatFormatter.getChatDisplayShort(Constants.RELAY_EFFICIENCY * 100, DisplayUnit.PERCENTAGE).withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.DARK_GRAY));
		BlockItemDescriptable.addDescription(ElectrodynamicsBlocks.BLOCKS_MACHINE.getHolder(SubtypeMachine.currentregulator), ElectroTextUtils.tooltip("transformer.energyloss", ChatFormatter.getChatDisplayShort(Constants.CURRENTREGULATOR_EFFICIENCY * 100, DisplayUnit.PERCENTAGE).withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.DARK_GRAY));
		BlockItemDescriptable.addDescription(ElectrodynamicsBlocks.BLOCKS_MACHINE.getHolder(SubtypeMachine.batterybox), ElectroTextUtils.voltageTooltip(120));
		BlockItemDescriptable.addDescription(ElectrodynamicsBlocks.BLOCKS_MACHINE.getHolder(SubtypeMachine.lithiumbatterybox), ElectroTextUtils.voltageTooltip(240));
		BlockItemDescriptable.addDescription(ElectrodynamicsBlocks.BLOCKS_MACHINE.getHolder(SubtypeMachine.carbynebatterybox), ElectroTextUtils.voltageTooltip(480));
		BlockItemDescriptable.addDescription(ElectrodynamicsBlocks.BLOCKS_MACHINE.getHolder(SubtypeMachine.creativepowersource), ElectroTextUtils.tooltip("creativepowersource.joke").withStyle(ChatFormatting.DARK_GRAY));
		BlockItemDescriptable.addDescription(ElectrodynamicsBlocks.BLOCKS_MACHINE.getHolder(SubtypeMachine.creativefluidsource), ElectroTextUtils.tooltip("creativefluidsource.joke").withStyle(ChatFormatting.DARK_GRAY));
		BlockItemDescriptable.addDescription(ElectrodynamicsBlocks.BLOCKS_MACHINE.getHolder(SubtypeMachine.creativegassource), ElectroTextUtils.tooltip("creativegassource.joke").withStyle(ChatFormatting.DARK_GRAY));
		BlockItemDescriptable.addDescription(ElectrodynamicsBlocks.BLOCKS_MACHINE.getHolder(SubtypeMachine.fluidvoid), ElectroTextUtils.tooltip("fluidvoid").withStyle(ChatFormatting.DARK_GRAY));
		BlockItemDescriptable.addDescription(ElectrodynamicsBlocks.BLOCKS_MACHINE.getHolder(SubtypeMachine.tanksteel), ElectroTextUtils.tooltip("fluidtank.capacity", ChatFormatter.getChatDisplayShort(8, DisplayUnit.BUCKETS).withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.DARK_GRAY));
		BlockItemDescriptable.addDescription(ElectrodynamicsBlocks.BLOCKS_MACHINE.getHolder(SubtypeMachine.tankreinforced), ElectroTextUtils.tooltip("fluidtank.capacity", ChatFormatter.getChatDisplayShort(32, DisplayUnit.BUCKETS).withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.DARK_GRAY));
		BlockItemDescriptable.addDescription(ElectrodynamicsBlocks.BLOCKS_MACHINE.getHolder(SubtypeMachine.tankhsla), ElectroTextUtils.tooltip("fluidtank.capacity", ChatFormatter.getChatDisplayShort(128, DisplayUnit.BUCKETS).withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.DARK_GRAY));
		BlockItemDescriptable.addDescription(ElectrodynamicsBlocks.BLOCK_SEISMICMARKER, ElectroTextUtils.tooltip("seismicmarker.redstone").withStyle(ChatFormatting.DARK_GRAY));
		BlockItemDescriptable.addDescription(ElectrodynamicsBlocks.BLOCKS_MACHINE.getHolder(SubtypeMachine.seismicrelay), ElectroTextUtils.tooltip("seismicrelay.use").withStyle(ChatFormatting.DARK_GRAY));
		BlockItemDescriptable.addDescription(ElectrodynamicsBlocks.BLOCKS_MACHINE.getHolder(SubtypeMachine.coolantresavoir), ElectroTextUtils.tooltip("coolantresavoir.place").withStyle(ChatFormatting.DARK_GRAY));
		BlockItemDescriptable.addDescription(ElectrodynamicsBlocks.BLOCKS_MACHINE.getHolder(SubtypeMachine.motorcomplex), ElectroTextUtils.tooltip("motorcomplex.use").withStyle(ChatFormatting.DARK_GRAY));
		BlockItemDescriptable.addDescription(ElectrodynamicsBlocks.BLOCK_FRAME, ElectroTextUtils.tooltip("blockframe.joke").withStyle(ChatFormatting.DARK_GRAY));
		BlockItemDescriptable.addDescription(ElectrodynamicsBlocks.BLOCK_FRAME_CORNER, ElectroTextUtils.tooltip("blockframe.joke").withStyle(ChatFormatting.DARK_GRAY));
		BlockItemDescriptable.addDescription(ElectrodynamicsBlocks.BLOCKS_MACHINE.getHolder(SubtypeMachine.quarry), ElectroTextUtils.tooltip("quarry.power").withStyle(ChatFormatting.DARK_GRAY));
		BlockItemDescriptable.addDescription(ElectrodynamicsBlocks.BLOCK_LOGISTICALMANAGER, ElectroTextUtils.tooltip("logisticalmanager.use").withStyle(ChatFormatting.DARK_GRAY));

		BlockItemDescriptable.addDescription(ElectrodynamicsBlocks.BLOCKS_MACHINE.getHolder(SubtypeMachine.gasvent), ElectroTextUtils.tooltip("gasvent").withStyle(ChatFormatting.DARK_GRAY));
		BlockItemDescriptable.addDescription(ElectrodynamicsBlocks.BLOCKS_MACHINE.getHolder(SubtypeMachine.gascollector), ElectroTextUtils.voltageTooltip(240));
		//BlockItemDescriptable.addDescription(ElectrodynamicsBlocks.BLOCKS_MACHINE.getHolder(SubtypeMachine.gasvalve), ElectroTextUtils.tooltip("gasvalve").withStyle(ChatFormatting.DARK_GRAY));
		//BlockItemDescriptable.addDescription(ElectrodynamicsBlocks.BLOCKS_MACHINE.getHolder(SubtypeMachine.fluidvalve), ElectroTextUtils.tooltip("fluidvalve"));
		BlockItemDescriptable.addDescription(ElectrodynamicsBlocks.BLOCKS_MACHINE.getHolder(SubtypeMachine.potentiometer), ElectroTextUtils.tooltip("potentiometer.use").withStyle(ChatFormatting.DARK_GRAY));
		BlockItemDescriptable.addDescription(ElectrodynamicsBlocks.BLOCK_CHEMICALREACTOR, ElectroTextUtils.voltageTooltip(480));
	}

}
