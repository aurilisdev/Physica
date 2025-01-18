package electrodynamics.client.guidebook.chapters;

import java.util.ArrayList;
import java.util.List;

import electrodynamics.Electrodynamics;
import electrodynamics.api.electricity.formatting.ChatFormatter;
import electrodynamics.api.electricity.formatting.DisplayUnit;
import electrodynamics.api.gas.Gas;
import electrodynamics.api.gas.GasStack;
import electrodynamics.client.guidebook.ScreenGuidebook;
import electrodynamics.client.guidebook.utils.components.Chapter;
import electrodynamics.client.guidebook.utils.components.Module;
import electrodynamics.client.guidebook.utils.pagedata.OnKeyPress;
import electrodynamics.client.guidebook.utils.pagedata.OnTooltip;
import electrodynamics.client.guidebook.utils.pagedata.graphics.AbstractGraphicWrapper.GraphicTextDescriptor;
import electrodynamics.client.guidebook.utils.pagedata.graphics.GasWrapperObject;
import electrodynamics.client.guidebook.utils.pagedata.graphics.ImageWrapperObject;
import electrodynamics.client.guidebook.utils.pagedata.graphics.ItemWrapperObject;
import electrodynamics.client.guidebook.utils.pagedata.text.TextWrapperObject;
import electrodynamics.common.block.subtype.SubtypeGasPipe;
import electrodynamics.common.block.subtype.SubtypeMachine;
import electrodynamics.common.settings.Constants;
import electrodynamics.common.tags.ElectrodynamicsTags;
import electrodynamics.common.tile.pipelines.gas.tank.GenericTileGasTank;
import electrodynamics.compatibility.jei.JeiBuffer;
import electrodynamics.prefab.utilities.ElectroTextUtils;
import electrodynamics.registers.ElectrodynamicsGases;
import electrodynamics.registers.ElectrodynamicsItems;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ChapterGases extends Chapter {

	private static final ImageWrapperObject LOGO = new ImageWrapperObject(0, 0, 0, 0, 32, 32, 32, 32, Electrodynamics.rl("textures/item/gaspipe/gaspipeuninsulatedsteel.png"));

	public ChapterGases(Module module) {
		super(module);
	}

	@Override
	public ImageWrapperObject getLogo() {
		return LOGO;
	}

	@Override
	public MutableComponent getTitle() {
		return ElectroTextUtils.guidebook("chapter.gases");
	}

	@Override
	public void addData() {
		// introduction
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.gases.l1")).setIndentions(1).setSeparateStart());

		blankLine();
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.gases.topic.header", 1, ElectroTextUtils.guidebook("chapter.gases.topic.gaslist"))).setSeparateStart());
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.gases.topic.header", 2, ElectroTextUtils.guidebook("chapter.gases.topic.gaspressure"))).setSeparateStart());
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.gases.topic.header", 3, ElectroTextUtils.guidebook("chapter.gases.topic.gastemperature"))).setSeparateStart());
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.gases.topic.header", 4, ElectroTextUtils.guidebook("chapter.gases.topic.gasio"))).setSeparateStart());
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.gases.topic.header", 5, ElectroTextUtils.guidebook("chapter.gases.topic.gaspipes"))).setSeparateStart());
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.gases.topic.header", 6, ElectroTextUtils.guidebook("chapter.gases.topic.gasmanipulation"))).setSeparateStart());
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.gases.topic.header", 7, ElectroTextUtils.guidebook("chapter.gases.topic.gastools"))).setSeparateStart());
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.gases.topic.header", 8, ElectroTextUtils.guidebook("chapter.gases.topic.gasgui"))).setSeparateStart());

		/* Gas List */

		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.gases.topic.gaslist").withStyle(ChatFormatting.BOLD, ChatFormatting.UNDERLINE)).setNewPage());

		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.gases.l2.1")).setIndentions(1).setSeparateStart());

		for (DeferredHolder<Gas, ? extends Gas> gas : ElectrodynamicsGases.GASES.getEntries()) {
			if (gas.get().isEmpty()) {
				continue;
			}
			pageData.add(new GasWrapperObject(0, 0, 32, 32, 36, gas.get(), new GraphicTextDescriptor(36, 11, gas.get().getDescription()))

					.onTooltip(new OnTooltip() {

						@Override
						public void onTooltip(GuiGraphics graphics, int xAxis, int yAxis, ScreenGuidebook screen) {
							if (JeiBuffer.isJeiInstalled()) {
								List<FormattedCharSequence> tooltips = new ArrayList<>();
								tooltips.add(ElectroTextUtils.tooltip("guidebookjeirecipe").withStyle(ChatFormatting.GRAY).getVisualOrderText());
								tooltips.add(ElectroTextUtils.tooltip("guidebookjeiuse").withStyle(ChatFormatting.GRAY).getVisualOrderText());
								graphics.renderTooltip(screen.getFontRenderer(), tooltips, xAxis, yAxis);
							}

						}
					}).onKeyPress(new OnKeyPress() {

						@Override
						public void onKeyPress(int keyCode, int scanCode, int modifiers, int x, int y, int xAxis, int yAxis, ScreenGuidebook screen) {

						}

						@Override
						public Object getJeiLookup() {
							return new GasStack(gas.get(), 1, Gas.ROOM_TEMPERATURE, Gas.PRESSURE_AT_SEA_LEVEL);
						}

					}));
		}

		blankLine();
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.gases.l2.2")).setSeparateStart());

		/* Pressure */

		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.gases.topic.gaspressure").withStyle(ChatFormatting.BOLD, ChatFormatting.UNDERLINE)).setNewPage());

		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.gases.l3", ElectroTextUtils.guidebook("chapter.gases.pressure").withStyle(ChatFormatting.BOLD))).setIndentions(1).setSeparateStart());
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.gases.l4.1")).setSeparateStart().setIndentions(1));
		pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 50, 150, 50, 55, Electrodynamics.rl("textures/screen/guidebook/itemmaxgasvals.png")));
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.gases.l4.2")).setSeparateStart());
		pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 50, 150, 50, 55, Electrodynamics.rl("textures/screen/guidebook/machinepressuretooltip.png")));
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.gases.l4.3")).setSeparateStart());
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.gases.l5")).setIndentions(1).setSeparateStart());

		/* Temperature */

		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.gases.topic.gastemperature").withStyle(ChatFormatting.BOLD, ChatFormatting.UNDERLINE)).setNewPage());

		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.gases.l6", ElectroTextUtils.guidebook("chapter.gases.temperature").withStyle(ChatFormatting.BOLD))).setIndentions(1).setSeparateStart());
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.gases.l7.1")).setSeparateStart().setIndentions(1));
		pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 50, 150, 50, 55, Electrodynamics.rl("textures/screen/guidebook/itemmaxgasvals.png")));
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.gases.l7.2")).setSeparateStart());
		pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 50, 150, 50, 55, Electrodynamics.rl("textures/screen/guidebook/machinetemperaturetooltip.png")));
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.gases.l7.3")).setSeparateStart());
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.gases.l8")).setIndentions(1).setSeparateStart());
		// condensed gases
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.gases.l9")).setIndentions(1).setSeparateStart());
		for (DeferredHolder<Gas, ? extends Gas> gas : ElectrodynamicsGases.GASES.getEntries()) {
			if (gas.get().isEmpty() || gas.get().noCondensedFluid()) {
				continue;
			}
			blankLine();
			pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.gases.gas", gas.get().getDescription()).withStyle(ChatFormatting.ITALIC)).setSeparateStart());
			pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.gases.condensedfluid", gas.get().getCondensedFluid().getFluidType().getDescription())).setSeparateStart());
			pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.gases.condtemp", ChatFormatter.getChatDisplayShort(gas.get().getCondensationTemp(), DisplayUnit.TEMPERATURE_KELVIN))).setSeparateStart());
		}
		blankLine();
		// condensed gas machine handling
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.gases.l10.1")).setSeparateStart());
		pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 60, 150, 60, 65, Electrodynamics.rl("textures/screen/guidebook/gasmachinecondensedgui.png")));
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.gases.l10.2")).setSeparateStart());

		/* Gas IO */

		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.gases.topic.gasio").withStyle(ChatFormatting.BOLD, ChatFormatting.UNDERLINE)).setNewPage());

		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.gases.l11")).setIndentions(1).setSeparateStart());
		blankLine();
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.gases.input")).setIndentions(1).setSeparateStart());
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.gases.output")).setIndentions(1).setSeparateStart());
		blankLine();
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.gases.l12")).setSeparateStart());
		pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 79, 150, 79, 81, Electrodynamics.rl("textures/screen/guidebook/gasio.png")).setNewPage().onTooltip(new OnTooltip() {

			@Override
			public void onTooltip(GuiGraphics graphics, int xAxis, int yAxis, ScreenGuidebook screen) {
				List<FormattedCharSequence> tooltips = new ArrayList<>();
				tooltips.add(ElectroTextUtils.guidebook("chapter.electricity.left", ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.gasvent).getDescription().copy().withStyle(ChatFormatting.DARK_GRAY)).withStyle(ChatFormatting.GRAY).getVisualOrderText());
				tooltips.add(ElectroTextUtils.guidebook("chapter.electricity.middle", ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.electrolyticseparator).getDescription().copy().withStyle(ChatFormatting.DARK_GRAY)).withStyle(ChatFormatting.GRAY).getVisualOrderText());
				tooltips.add(ElectroTextUtils.guidebook("chapter.electricity.right", ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.gaspipefilter).getDescription().copy().withStyle(ChatFormatting.DARK_GRAY)).withStyle(ChatFormatting.GRAY).getVisualOrderText());

				graphics.renderTooltip(screen.getFontRenderer(), tooltips, xAxis, yAxis);
			}

		}));

		/* Gas Pipes */

		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.gases.topic.gaspipes").withStyle(ChatFormatting.BOLD, ChatFormatting.UNDERLINE)).setNewPage());

		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.gases.l13.1")).setIndentions(1).setSeparateStart());
		blankLine();
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.gases.pipecopper").withStyle(ChatFormatting.ITALIC)).setSeparateStart());
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.gases.pipecapacity", ChatFormatter.formatFluidMilibuckets(SubtypeGasPipe.UNINSULATEDCOPPER.getMaxTransfer()))).setSeparateStart());
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.gases.pipepressure", ChatFormatter.getChatDisplayShort(SubtypeGasPipe.UNINSULATEDCOPPER.getPipeMaterial().getMaxPressuire(), DisplayUnit.PRESSURE_ATM))).setSeparateStart());
		blankLine();
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.gases.pipesteel").withStyle(ChatFormatting.ITALIC)).setSeparateStart());
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.gases.pipecapacity", ChatFormatter.formatFluidMilibuckets(SubtypeGasPipe.UNINSULATEDSTEEL.getMaxTransfer()))).setSeparateStart());
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.gases.pipepressure", ChatFormatter.getChatDisplayShort(SubtypeGasPipe.UNINSULATEDSTEEL.getPipeMaterial().getMaxPressuire(), DisplayUnit.PRESSURE_ATM))).setSeparateStart());
		blankLine();
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.gases.pipeplastic").withStyle(ChatFormatting.ITALIC)).setSeparateStart());
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.gases.pipecapacity", ChatFormatter.formatFluidMilibuckets(SubtypeGasPipe.UNINSULATEDPLASTIC.getMaxTransfer()))).setSeparateStart());
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.gases.pipepressure", ChatFormatter.getChatDisplayShort(SubtypeGasPipe.UNINSULATEDPLASTIC.getPipeMaterial().getMaxPressuire(), DisplayUnit.PRESSURE_ATM))).setSeparateStart());
		blankLine();
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.gases.l13.2")).setSeparateStart());
		blankLine();
		for (Holder<Gas> gas : ElectrodynamicsGases.GAS_REGISTRY.getTag(ElectrodynamicsTags.Gases.IS_CORROSIVE).get()) {
			pageData.add(new TextWrapperObject(gas.value().getDescription()).setSeparateStart());
		}
		blankLine();
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.gases.l13.3")).setSeparateStart());

		/* Gas Manipulator */

		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.gases.topic.gasmanipulation").withStyle(ChatFormatting.BOLD, ChatFormatting.UNDERLINE)).setNewPage());

		// compressors
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.gases.l14")).setIndentions(1).setSeparateStart());
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.gases.l15", ElectrodynamicsItems.ITEM_COMPRESSOR.get().getDescription().copy().withStyle(ChatFormatting.BOLD), ElectrodynamicsItems.ITEM_DECOMPRESSOR.get().getDescription().copy().withStyle(ChatFormatting.BOLD), Constants.COMPRESSOR_CONVERSION_RATE, Constants.ADVACNED_COMPRESSOR_CONVERSION_RATE)).setIndentions(1).setSeparateStart());
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.gases.l16.1", ElectrodynamicsItems.ITEM_COMPRESSOR_ADDONTANK.get().getDescription(), ElectrodynamicsItems.ITEM_COMPRESSOR_ADDONTANK.get().getDescription().copy().withStyle(ChatFormatting.BOLD))).setIndentions(1).setSeparateStart());
		pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 79, 150, 79, 81, Electrodynamics.rl("textures/screen/guidebook/pressurizedtankuse.png")));
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.gases.l16.2", Constants.GAS_TRANSFORMER_ADDON_TANK_LIMIT)).setSeparateStart());

		// thermal manip
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.gases.l17.1", ElectrodynamicsItems.ITEM_THERMOELECTRIC_MANIPULATOR.get().getDescription().copy().withStyle(ChatFormatting.BOLD), Constants.THERMOELECTRIC_MANIPULATOR_CONVERSION_RATE, Constants.THERMOELECTRIC_MANIPULATOR_HEAT_TRANSFER, Constants.ADVANCED_THERMOELECTRIC_MANIPULATOR_CONVERSION_RATE, Constants.ADVANCED_THERMOELECTRIC_MANIPULATOR_HEAT_TRANSFER)).setIndentions(1).setSeparateStart());
		pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 79, 150, 79, 81, Electrodynamics.rl("textures/screen/guidebook/thermomanipgui.png")));
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.gases.l17.2", ElectrodynamicsItems.ITEM_THERMOELECTRIC_MANIPULATOR.get().getDescription(), ElectrodynamicsItems.ITEM_COMPRESSOR.get().getDescription(), ElectrodynamicsItems.ITEM_DECOMPRESSOR.get().getDescription(), ElectrodynamicsItems.ITEM_COMPRESSOR_ADDONTANK.get().getDescription())).setSeparateStart());

		/* Gas Tools */

		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.gases.topic.gastools").withStyle(ChatFormatting.BOLD, ChatFormatting.UNDERLINE)).setNewPage());

		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.gases.l18.1")).setIndentions(1).setSeparateStart());

		// Portable Gas Cylinder

		pageData.add(new TextWrapperObject(ElectrodynamicsItems.ITEM_PORTABLECYLINDER.get().getDescription().copy().withStyle(ChatFormatting.BOLD)).setCentered().setNewPage());
		pageData.add(new ItemWrapperObject(7 + ScreenGuidebook.TEXT_WIDTH / 2 - 16, 5, 32, 30, 30, 2.0F, ElectrodynamicsItems.ITEM_PORTABLECYLINDER.get()).onTooltip(new OnTooltip() {

			@Override
			public void onTooltip(GuiGraphics graphics, int xAxis, int yAxis, ScreenGuidebook screen) {
				if (JeiBuffer.isJeiInstalled()) {
					List<FormattedCharSequence> tooltips = new ArrayList<>();
					tooltips.add(ElectroTextUtils.tooltip("guidebookjeirecipe").withStyle(ChatFormatting.GRAY).getVisualOrderText());
					graphics.renderTooltip(screen.getFontRenderer(), tooltips, xAxis, yAxis);
				}

			}
		}).onKeyPress(new OnKeyPress() {

			@Override
			public void onKeyPress(int keyCode, int scanCode, int modifiers, int x, int y, int xAxis, int yAxis, ScreenGuidebook screen) {

			}

			@Override
			public Object getJeiLookup() {
				return new ItemStack(ElectrodynamicsItems.ITEM_PORTABLECYLINDER.get());
			}

		}));

		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.gases.l18.portablecylinder.1")).setSeparateStart().setIndentions(1));

		// Gas Valve

		pageData.add(new TextWrapperObject(ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.gasvalve).getDescription().copy().withStyle(ChatFormatting.BOLD)).setCentered().setNewPage());
		pageData.add(new ItemWrapperObject(7 + ScreenGuidebook.TEXT_WIDTH / 2 - 16, 5, 32, 30, 30, 2.0F, ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.gasvalve)).onTooltip(new OnTooltip() {

			@Override
			public void onTooltip(GuiGraphics graphics, int xAxis, int yAxis, ScreenGuidebook screen) {
				if (JeiBuffer.isJeiInstalled()) {
					List<FormattedCharSequence> tooltips = new ArrayList<>();
					tooltips.add(ElectroTextUtils.tooltip("guidebookjeirecipe").withStyle(ChatFormatting.GRAY).getVisualOrderText());
					graphics.renderTooltip(screen.getFontRenderer(), tooltips, xAxis, yAxis);
				}

			}
		}).onKeyPress(new OnKeyPress() {

			@Override
			public void onKeyPress(int keyCode, int scanCode, int modifiers, int x, int y, int xAxis, int yAxis, ScreenGuidebook screen) {

			}

			@Override
			public Object getJeiLookup() {
				return new ItemStack(ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.gasvalve));
			}

		}));

		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.gases.l18.gasvalve.1")).setSeparateStart().setIndentions(1));
		pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 75, 150, 75, Electrodynamics.rl("textures/screen/guidebook/gasvalveoff.png")));
		pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 75, 150, 75, Electrodynamics.rl("textures/screen/guidebook/gasvalveon.png")));
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.gases.l18.gasvalve.2")).setSeparateStart());

		// Gas Pipe Pump

		pageData.add(new TextWrapperObject(ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.gaspipepump).getDescription().copy().withStyle(ChatFormatting.BOLD)).setCentered().setNewPage());
		pageData.add(new ItemWrapperObject(7 + ScreenGuidebook.TEXT_WIDTH / 2 - 16, 5, 32, 30, 30, 2.0F, ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.gaspipepump)).onTooltip(new OnTooltip() {

			@Override
			public void onTooltip(GuiGraphics graphics, int xAxis, int yAxis, ScreenGuidebook screen) {
				if (JeiBuffer.isJeiInstalled()) {
					List<FormattedCharSequence> tooltips = new ArrayList<>();
					tooltips.add(ElectroTextUtils.tooltip("guidebookjeirecipe").withStyle(ChatFormatting.GRAY).getVisualOrderText());
					graphics.renderTooltip(screen.getFontRenderer(), tooltips, xAxis, yAxis);
				}

			}
		}).onKeyPress(new OnKeyPress() {

			@Override
			public void onKeyPress(int keyCode, int scanCode, int modifiers, int x, int y, int xAxis, int yAxis, ScreenGuidebook screen) {

			}

			@Override
			public Object getJeiLookup() {
				return new ItemStack(ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.gaspipepump));
			}

		}));

		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.gases.l18.gaspipepump.1")).setSeparateStart().setIndentions(1));
		pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 79, 150, 79, 85, Electrodynamics.rl("textures/screen/guidebook/gaspipepump.png")));
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.gases.l18.gaspipepump.2")).setSeparateStart());
		pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 79, 150, 79, 85, Electrodynamics.rl("textures/screen/guidebook/gaspipepumpgui.png")));
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.gases.l18.gaspipepump.3")).setSeparateStart());

		// Gas Filter Pipe

		pageData.add(new TextWrapperObject(ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.gaspipefilter).getDescription().copy().withStyle(ChatFormatting.BOLD)).setCentered().setNewPage());
		pageData.add(new ItemWrapperObject(7 + ScreenGuidebook.TEXT_WIDTH / 2 - 16, 5, 32, 30, 30, 2.0F, ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.gaspipefilter)).onTooltip(new OnTooltip() {

			@Override
			public void onTooltip(GuiGraphics graphics, int xAxis, int yAxis, ScreenGuidebook screen) {
				if (JeiBuffer.isJeiInstalled()) {
					List<FormattedCharSequence> tooltips = new ArrayList<>();
					tooltips.add(ElectroTextUtils.tooltip("guidebookjeirecipe").withStyle(ChatFormatting.GRAY).getVisualOrderText());
					graphics.renderTooltip(screen.getFontRenderer(), tooltips, xAxis, yAxis);
				}

			}
		}).onKeyPress(new OnKeyPress() {

			@Override
			public void onKeyPress(int keyCode, int scanCode, int modifiers, int x, int y, int xAxis, int yAxis, ScreenGuidebook screen) {

			}

			@Override
			public Object getJeiLookup() {
				return new ItemStack(ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.gaspipefilter));
			}

		}));

		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.gases.l18.gasfilterpipe.1")).setSeparateStart().setIndentions(1));
		pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 79, 150, 79, Electrodynamics.rl("textures/screen/guidebook/gasfilterpipe.png")));
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.gases.l18.gasfilterpipe.2")).setSeparateStart());
		pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 79, 150, 79, Electrodynamics.rl("textures/screen/guidebook/gasfilterpipegui1.png")));
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.gases.l18.gasfilterpipe.3", ElectroTextUtils.guidebook("chapter.gases.blacklist").withStyle(ChatFormatting.BOLD), ElectroTextUtils.guidebook("chapter.gases.whitelist").withStyle(ChatFormatting.BOLD))).setSeparateStart());
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.gases.l18.gasfilterpipe.4")).setSeparateStart().setIndentions(1));
		pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 75, 150, 75, Electrodynamics.rl("textures/screen/guidebook/gasfilterpipegui2.png")));
		pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 75, 150, 75, Electrodynamics.rl("textures/screen/guidebook/gasfilterpipegui3.png")));
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.gases.l18.gasfilterpipe.5")).setSeparateStart());

		// Gas Vent

		pageData.add(new TextWrapperObject(ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.gasvent).getDescription().copy().withStyle(ChatFormatting.BOLD)).setCentered().setNewPage());
		pageData.add(new ItemWrapperObject(7 + ScreenGuidebook.TEXT_WIDTH / 2 - 16, 5, 32, 30, 30, 2.0F, ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.gasvent)).onTooltip(new OnTooltip() {

			@Override
			public void onTooltip(GuiGraphics graphics, int xAxis, int yAxis, ScreenGuidebook screen) {
				if (JeiBuffer.isJeiInstalled()) {
					List<FormattedCharSequence> tooltips = new ArrayList<>();
					tooltips.add(ElectroTextUtils.tooltip("guidebookjeirecipe").withStyle(ChatFormatting.GRAY).getVisualOrderText());
					graphics.renderTooltip(screen.getFontRenderer(), tooltips, xAxis, yAxis);
				}

			}
		}).onKeyPress(new OnKeyPress() {

			@Override
			public void onKeyPress(int keyCode, int scanCode, int modifiers, int x, int y, int xAxis, int yAxis, ScreenGuidebook screen) {

			}

			@Override
			public Object getJeiLookup() {
				return new ItemStack(ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.gasvent));
			}

		}));

		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.gases.l18.gasvent.1")).setIndentions(1).setSeparateStart());

		// Gas Cylinders

		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.gases.gascylinders").withStyle(ChatFormatting.BOLD)).setCentered().setNewPage());
		pageData.add(new ItemWrapperObject(7 + ScreenGuidebook.TEXT_WIDTH / 2 - 16, 5, 32, 30, 30, 2.0F, ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.gastankhsla)).onTooltip(new OnTooltip() {

			@Override
			public void onTooltip(GuiGraphics graphics, int xAxis, int yAxis, ScreenGuidebook screen) {
				if (JeiBuffer.isJeiInstalled()) {
					List<FormattedCharSequence> tooltips = new ArrayList<>();
					tooltips.add(ElectroTextUtils.tooltip("guidebookjeirecipe").withStyle(ChatFormatting.GRAY).getVisualOrderText());
					graphics.renderTooltip(screen.getFontRenderer(), tooltips, xAxis, yAxis);
				}

			}
		}).onKeyPress(new OnKeyPress() {

			@Override
			public void onKeyPress(int keyCode, int scanCode, int modifiers, int x, int y, int xAxis, int yAxis, ScreenGuidebook screen) {

			}

			@Override
			public Object getJeiLookup() {
				return new ItemStack(ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.gastankhsla));
			}

		}));

		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.gases.l18.gascylinder.1", ChatFormatter.getChatDisplayShort(GenericTileGasTank.HEAT_LOSS * 20, DisplayUnit.TEMPERATURE_KELVIN), ElectrodynamicsItems.ITEM_FIBERGLASSSHEET.get().getDescription().copy().withStyle(ChatFormatting.BOLD), ChatFormatter.getChatDisplayShort(GenericTileGasTank.INSULATION_EFFECTIVENESS * 100 - 100, DisplayUnit.PERCENTAGE), ChatFormatter.getChatDisplayShort(Math.pow(GenericTileGasTank.INSULATION_EFFECTIVENESS, 6) * 100 - 100, DisplayUnit.PERCENTAGE))).setSeparateStart().setIndentions(1));

		pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 75, 150, 75, Electrodynamics.rl("textures/screen/guidebook/fiberglasssheetuse.png")));

		/* Gas GUIs */

		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.gases.topic.gasgui").withStyle(ChatFormatting.BOLD, ChatFormatting.UNDERLINE)).setNewPage());

		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.gases.l19", ElectrodynamicsItems.ITEM_PORTABLECYLINDER.get().getDescription())).setIndentions(1).setSeparateStart());
	}

}
