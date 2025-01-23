package electrodynamics.client.guidebook.chapters;

import java.util.ArrayList;
import java.util.List;

import electrodynamics.Electrodynamics;
import electrodynamics.client.guidebook.ScreenGuidebook;
import electrodynamics.client.guidebook.utils.components.Chapter;
import electrodynamics.client.guidebook.utils.components.Module;
import electrodynamics.client.guidebook.utils.pagedata.OnKeyPress;
import electrodynamics.client.guidebook.utils.pagedata.OnTooltip;
import electrodynamics.client.guidebook.utils.pagedata.graphics.ImageWrapperObject;
import electrodynamics.client.guidebook.utils.pagedata.graphics.ItemWrapperObject;
import electrodynamics.client.guidebook.utils.pagedata.text.TextWrapperObject;
import electrodynamics.common.block.subtype.SubtypeMachine;
import electrodynamics.common.network.type.ElectricNetwork;
import electrodynamics.compatibility.jei.JeiBuffer;
import electrodynamics.prefab.utilities.ElectroTextUtils;
import electrodynamics.registers.ElectrodynamicsItems;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.ItemStack;

public class ChapterElectricity extends Chapter {

	private static final ImageWrapperObject LOGO = new ImageWrapperObject(2, 2, 0, 0, 28, 28, 28, 28, Electrodynamics.rl("textures/item/wire/wiregold.png"));

	public ChapterElectricity(Module module) {
		super(module);
	}

	@Override
	public ImageWrapperObject getLogo() {
		return LOGO;
	}

	@Override
	public MutableComponent getTitle() {
		return ElectroTextUtils.guidebook("chapter.electricity");
	}

	@Override
	public void addData() {
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electricity.l1")).setIndentions(1).setSeparateStart());
		blankLine();
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electricity.topic.header", 1, ElectroTextUtils.guidebook("chapter.electricity.topic.electricbasics"))).setSeparateStart());
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electricity.topic.header", 2, ElectroTextUtils.guidebook("chapter.electricity.topic.machinefundamentals"))).setSeparateStart());
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electricity.topic.header", 3, ElectroTextUtils.guidebook("chapter.electricity.topic.wirebasics"))).setSeparateStart());
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electricity.topic.header", 4, ElectroTextUtils.guidebook("chapter.electricity.topic.wireinsulation"))).setSeparateStart());
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electricity.topic.header", 5, ElectroTextUtils.guidebook("chapter.electricity.topic.wiremodification"))).setSeparateStart());
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electricity.topic.header", 6, ElectroTextUtils.guidebook("chapter.electricity.topic.transformers"))).setSeparateStart());
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electricity.topic.header", 7, ElectroTextUtils.guidebook("chapter.electricity.topic.gridconcepts"))).setSeparateStart());
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electricity.topic.header", 8, ElectroTextUtils.guidebook("chapter.electricity.topic.gridtools"))).setSeparateStart());
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electricity.topic.header", 9, ElectroTextUtils.guidebook("chapter.electricity.topic.onfe"))).setSeparateStart());
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electricity.topic.header", 10, ElectroTextUtils.guidebook("chapter.electricity.topic.summary"))).setSeparateStart());

		/* Electricity Basics */

		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electricity.topic.electricbasics").withStyle(ChatFormatting.BOLD, ChatFormatting.UNDERLINE)).setNewPage());

		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electricity.l2.1")).setIndentions(1).setSeparateStart());
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electricity.powerformula").withStyle(ChatFormatting.BOLD)).setIndentions(1).setSeparateStart());
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electricity.l2.2")).setSeparateStart());

		/* Machine Fundamentals */

		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electricity.topic.machinefundamentals").withStyle(ChatFormatting.BOLD, ChatFormatting.UNDERLINE)).setNewPage());

		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electricity.l3.1")).setSeparateStart().setIndentions(1));
		pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 79, 150, 79, 81, Electrodynamics.rl("textures/screen/guidebook/voltagetooltipinventory.png")));
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electricity.l3.2")).setSeparateStart());

		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electricity.l4")).setSeparateStart().setIndentions(1));

		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electricity.voltageval", 120, ElectroTextUtils.guidebook("chapter.electricity.yellow"))).setSeparateStart().setIndentions(1));
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electricity.voltageval", 240, ElectroTextUtils.guidebook("chapter.electricity.blue"))).setSeparateStart().setIndentions(1));
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electricity.voltageval", 480, ElectroTextUtils.guidebook("chapter.electricity.red"))).setSeparateStart().setIndentions(1));
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electricity.voltageval", 960, ElectroTextUtils.guidebook("chapter.electricity.purple"))).setSeparateStart().setIndentions(1));
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electricity.voltageval", 1920, ElectroTextUtils.guidebook("chapter.electricity.white"))).setSeparateStart().setIndentions(1));

		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electricity.l5")).setSeparateStart());

		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electricity.voltageexample", 120)).setCentered().setNewPage());
		pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 79, 150, 79, 81, Electrodynamics.rl("textures/screen/guidebook/120vmachines.png")).onTooltip(new OnTooltip() {

			@Override
			public void onTooltip(GuiGraphics graphics, int xAxis, int yAxis, ScreenGuidebook screen) {
				List<FormattedCharSequence> tooltips = new ArrayList<>();
				tooltips.add(ElectroTextUtils.guidebook("chapter.electricity.left", ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.mineralgrinder).getDescription().copy().withStyle(ChatFormatting.DARK_GRAY)).withStyle(ChatFormatting.GRAY).getVisualOrderText());
				tooltips.add(ElectroTextUtils.guidebook("chapter.electricity.middle", ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.batterybox).getDescription().copy().withStyle(ChatFormatting.DARK_GRAY)).withStyle(ChatFormatting.GRAY).getVisualOrderText());
				tooltips.add(ElectroTextUtils.guidebook("chapter.electricity.right", ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.electricfurnace).getDescription().copy().withStyle(ChatFormatting.DARK_GRAY)).withStyle(ChatFormatting.GRAY).getVisualOrderText());

				graphics.renderTooltip(screen.getFontRenderer(), tooltips, xAxis, yAxis);
			}

		}));
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electricity.voltageexamplenote")));

		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electricity.voltageexample", 240)).setCentered().setNewPage());
		pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 79, 150, 79, 81, Electrodynamics.rl("textures/screen/guidebook/240vmachines.png")).onTooltip(new OnTooltip() {

			@Override
			public void onTooltip(GuiGraphics graphics, int xAxis, int yAxis, ScreenGuidebook screen) {

				List<FormattedCharSequence> tooltips = new ArrayList<>();
				tooltips.add(ElectroTextUtils.guidebook("chapter.electricity.left", ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.chemicalmixer).getDescription().copy().withStyle(ChatFormatting.DARK_GRAY)).withStyle(ChatFormatting.GRAY).getVisualOrderText());
				tooltips.add(ElectroTextUtils.guidebook("chapter.electricity.middle", ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.lathe).getDescription().copy().withStyle(ChatFormatting.DARK_GRAY)).withStyle(ChatFormatting.GRAY).getVisualOrderText());
				tooltips.add(ElectroTextUtils.guidebook("chapter.electricity.right", ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.oxidationfurnace).getDescription().copy().withStyle(ChatFormatting.DARK_GRAY)).withStyle(ChatFormatting.GRAY).getVisualOrderText());

				graphics.renderTooltip(screen.getFontRenderer(), tooltips, xAxis, yAxis);
			}

		}));

		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electricity.voltageexample", 480)).setCentered().setNewPage());
		pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 79, 150, 79, 81, Electrodynamics.rl("textures/screen/guidebook/480vmachines.png")).onTooltip(new OnTooltip() {

			@Override
			public void onTooltip(GuiGraphics graphics, int xAxis, int yAxis, ScreenGuidebook screen) {
				List<FormattedCharSequence> tooltips = new ArrayList<>();
				tooltips.add(ElectroTextUtils.guidebook("chapter.electricity.left", ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.mineralwasher).getDescription().copy().withStyle(ChatFormatting.DARK_GRAY)).withStyle(ChatFormatting.GRAY).getVisualOrderText());
				tooltips.add(ElectroTextUtils.guidebook("chapter.electricity.middle", ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.energizedalloyer).getDescription().copy().withStyle(ChatFormatting.DARK_GRAY)).withStyle(ChatFormatting.GRAY).getVisualOrderText());
				tooltips.add(ElectroTextUtils.guidebook("chapter.electricity.right", ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.carbynebatterybox).getDescription().copy().withStyle(ChatFormatting.DARK_GRAY)).withStyle(ChatFormatting.GRAY).getVisualOrderText());

				graphics.renderTooltip(screen.getFontRenderer(), tooltips, xAxis, yAxis);
			}

		}));

		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electricity.voltageexample", 960)).setCentered().setNewPage());
		pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 79, 150, 79, 81, Electrodynamics.rl("textures/screen/guidebook/960vmachines.png")).onTooltip(new OnTooltip() {

			@Override
			public void onTooltip(GuiGraphics graphics, int xAxis, int yAxis, ScreenGuidebook screen) {
				List<FormattedCharSequence> tooltips = new ArrayList<>();
				tooltips.add(ElectroTextUtils.guidebook("chapter.electricity.left", ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.reinforcedalloyer).getDescription().copy().withStyle(ChatFormatting.DARK_GRAY)).withStyle(ChatFormatting.GRAY).getVisualOrderText());
				tooltips.add(ElectroTextUtils.guidebook("chapter.electricity.middle", ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.mineralcrushertriple).getDescription().copy().withStyle(ChatFormatting.DARK_GRAY)).withStyle(ChatFormatting.GRAY).getVisualOrderText());

				graphics.renderTooltip(screen.getFontRenderer(), tooltips, xAxis, yAxis);
			}

		}));

		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electricity.l6")).setNewPage().setIndentions(1));

		pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 50, 150, 50, 55, Electrodynamics.rl("textures/screen/guidebook/voltagetooltip1.png")).setNewPage());
		pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 55, 150, 55, 60, Electrodynamics.rl("textures/screen/guidebook/voltagetooltip2.png")));
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electricity.guivoltagenote")));

		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electricity.l7")).setNewPage().setIndentions(1));

		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electricity.l8")).setSeparateStart().setIndentions(1));
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electricity.energyinput")).setSeparateStart().setIndentions(1));
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electricity.energyoutput")).setSeparateStart().setIndentions(1));
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electricity.l9")).setSeparateStart());
		pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 79, 150, 79, Electrodynamics.rl("textures/screen/guidebook/energyio.png")).onTooltip(new OnTooltip() {

			@Override
			public void onTooltip(GuiGraphics graphics, int xAxis, int yAxis, ScreenGuidebook screen) {
				List<FormattedCharSequence> tooltips = new ArrayList<>();
				tooltips.add(ElectroTextUtils.guidebook("chapter.electricity.left", ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.batterybox).getDescription().copy().withStyle(ChatFormatting.DARK_GRAY)).withStyle(ChatFormatting.GRAY).getVisualOrderText());
				tooltips.add(ElectroTextUtils.guidebook("chapter.electricity.middle", ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.relay).getDescription().copy().withStyle(ChatFormatting.DARK_GRAY)).withStyle(ChatFormatting.GRAY).getVisualOrderText());
				tooltips.add(ElectroTextUtils.guidebook("chapter.electricity.right", ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.downgradetransformer).getDescription().copy().withStyle(ChatFormatting.DARK_GRAY)).withStyle(ChatFormatting.GRAY).getVisualOrderText());

				graphics.renderTooltip(screen.getFontRenderer(), tooltips, xAxis, yAxis);
			}

		}));

		/* Wire Basics */

		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electricity.topic.wirebasics").withStyle(ChatFormatting.BOLD, ChatFormatting.UNDERLINE)).setNewPage());

		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electricity.l10", ElectroTextUtils.guidebook("chapter.electricity.wires").withStyle(ChatFormatting.BOLD))).setSeparateStart().setIndentions(1));
		pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 72, 150, 72, 75, Electrodynamics.rl("textures/screen/guidebook/wiretooltip1.png")));

		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electricity.l11.1", ElectroTextUtils.guidebook("chapter.electricity.resistance").withStyle(ChatFormatting.BOLD), ElectroTextUtils.guidebook("chapter.electricity.powerfromcurrent").withStyle(ChatFormatting.BOLD))).setSeparateStart().setIndentions(1));
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electricity.l11.2", ElectroTextUtils.guidebook("chapter.electricity.ampacity").withStyle(ChatFormatting.BOLD))).setSeparateStart().setIndentions(1));
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electricity.l11.3", ElectricNetwork.MAXIMUM_OVERLOAD_PERIOD_TICKS)).setSeparateStart().setIndentions(1));
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electricity.l11.4", ElectroTextUtils.guidebook("chapter.electricity.insulation").withStyle(ChatFormatting.BOLD))).setSeparateStart().setIndentions(1));

		/* Wire Insulation */

		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electricity.topic.wireinsulation").withStyle(ChatFormatting.BOLD, ChatFormatting.UNDERLINE)).setNewPage());

		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electricity.l12.1")).setSeparateStart().setIndentions(1));
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electricity.l12.2", ElectrodynamicsItems.ITEM_RUBBERBOOTS.get().getDescription().copy().withStyle(ChatFormatting.BOLD))).setSeparateStart().setIndentions(1));

		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electricity.l13.1")).setSeparateStart().setIndentions(1));
		pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 79, 150, 79, Electrodynamics.rl("textures/screen/guidebook/woolwire.png")));
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electricity.l13.2")).setSeparateStart());
		pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 79, 150, 79, Electrodynamics.rl("textures/screen/guidebook/logisticalwire.png")));
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electricity.l13.3")).setSeparateStart());

		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electricity.l13.4")).setSeparateStart().setIndentions(1));
		pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 79, 150, 79, Electrodynamics.rl("textures/screen/guidebook/ceramicwire.png")));
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electricity.l13.5")).setSeparateStart());

		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electricity.l14.1")).setSeparateStart().setIndentions(1));
		pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 79, 150, 79, Electrodynamics.rl("textures/screen/guidebook/coloredwires.png")));
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electricity.l14.2")).setSeparateStart());

		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electricity.l15")).setSeparateStart().setIndentions(1));

		/* Wire Modification */

		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electricity.topic.wiremodification").withStyle(ChatFormatting.BOLD, ChatFormatting.UNDERLINE)).setNewPage());

		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electricity.l16")).setSeparateStart().setIndentions(1));

		/* Transformers */

		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electricity.topic.transformers").withStyle(ChatFormatting.BOLD, ChatFormatting.UNDERLINE)).setNewPage());

		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electricity.l17.1")).setSeparateStart().setIndentions(1));
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electricity.turnsratioformula").withStyle(ChatFormatting.BOLD)).setSeparateStart().setIndentions(1));
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electricity.l17.2")).setSeparateStart());
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electricity.l17.3", ElectroTextUtils.guidebook("chapter.electricity.upgrade").withStyle(ChatFormatting.BOLD), ElectroTextUtils.guidebook("chapter.electricity.downgrade").withStyle(ChatFormatting.BOLD))).setSeparateStart().setIndentions(1));

		/* Grid Concepts */

		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electricity.topic.gridconcepts").withStyle(ChatFormatting.BOLD, ChatFormatting.UNDERLINE)).setNewPage());

		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electricity.l18.1", ElectroTextUtils.guidebook("chapter.electricity.grid").withStyle(ChatFormatting.BOLD))).setSeparateStart().setIndentions(1));

		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electricity.l18.2")).setSeparateStart().setIndentions(1));
		pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 79, 150, 79, Electrodynamics.rl("textures/screen/guidebook/electrodynamicsmodel.png")));
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electricity.l18.3")).setSeparateStart());

		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electricity.l18.4", ElectroTextUtils.guidebook("chapter.electricity.neutralcurrent").withStyle(ChatFormatting.BOLD))).setSeparateStart().setIndentions(1));
		pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 79, 150, 79, Electrodynamics.rl("textures/screen/guidebook/neutralwire.png")));
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electricity.neutralwirenote").withStyle(ChatFormatting.ITALIC)));
		blankLine();
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electricity.l18.5", ElectroTextUtils.guidebook("chapter.electricity.neutralloss").withStyle(ChatFormatting.BOLD))).setSeparateStart());

		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electricity.l18.6")).setSeparateStart().setIndentions(1));

		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electricity.l18.7", ElectroTextUtils.guidebook("chapter.electricity.inrushcurrent").withStyle(ChatFormatting.BOLD))).setSeparateStart().setIndentions(1));
		pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 79, 150, 79, Electrodynamics.rl("textures/screen/guidebook/inrushcurrent.png")));
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electricity.l18.8")).setSeparateStart());

		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electricity.l18.9", ElectricNetwork.MAXIMUM_OVERLOAD_PERIOD_TICKS)).setSeparateStart().setIndentions(1));
		// return current
		// power distribution model
		// inrush current
		// best practices

		/* Grid Tools */

		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electricity.topic.gridtools").withStyle(ChatFormatting.BOLD, ChatFormatting.UNDERLINE)).setNewPage());

		// hand multimeter

		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electricity.l19.1")).setSeparateStart().setIndentions(1));

		pageData.add(new TextWrapperObject(ElectrodynamicsItems.ITEM_MULTIMETER.get().getDescription().copy().withStyle(ChatFormatting.BOLD)).setCentered().setNewPage());
		pageData.add(new ItemWrapperObject(7 + ScreenGuidebook.TEXT_WIDTH / 2 - 16, 5, 32, 30, 30, 2.0F, ElectrodynamicsItems.ITEM_MULTIMETER.get()).onTooltip(new OnTooltip() {

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
				return new ItemStack(ElectrodynamicsItems.ITEM_MULTIMETER.get());
			}

		}));

		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electricity.l19.handheldmultimeter.1")).setIndentions(1).setSeparateStart());
		pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 40, 150, 40, 45, Electrodynamics.rl("textures/screen/guidebook/multimeterdisplay1.png")));
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electricity.l19.handheldmultimeter.2")).setSeparateStart());

		// block multimeter

		pageData.add(new TextWrapperObject(ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.multimeterblock).getDescription().copy().withStyle(ChatFormatting.BOLD)).setCentered().setNewPage());
		pageData.add(new ItemWrapperObject(7 + ScreenGuidebook.TEXT_WIDTH / 2 - 16, 5, 32, 30, 30, 2.0F, ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.multimeterblock)).onTooltip(new OnTooltip() {

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
				return new ItemStack(ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.multimeterblock));
			}

		}));

		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electricity.l19.multimeterblock.1")).setIndentions(1).setSeparateStart());

		// relay

		pageData.add(new TextWrapperObject(ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.relay).getDescription().copy().withStyle(ChatFormatting.BOLD)).setCentered().setNewPage());
		pageData.add(new ItemWrapperObject(7 + ScreenGuidebook.TEXT_WIDTH / 2 - 16, 5, 32, 30, 30, 2.0F, ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.relay)).onTooltip(new OnTooltip() {

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
				return new ItemStack(ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.relay));
			}

		}));

		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electricity.l19.relay.1")).setSeparateStart().setIndentions(1));
		pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 75, 150, 75, Electrodynamics.rl("textures/screen/guidebook/relayoff.png")));
		pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 75, 150, 75, Electrodynamics.rl("textures/screen/guidebook/relayon.png")));
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electricity.l19.relay.2")).setSeparateStart());

		// circuit breaker

		pageData.add(new TextWrapperObject(ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.circuitbreaker).getDescription().copy().withStyle(ChatFormatting.BOLD)).setCentered().setNewPage());
		pageData.add(new ItemWrapperObject(7 + ScreenGuidebook.TEXT_WIDTH / 2 - 16, 5, 32, 30, 30, 2.0F, ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.circuitbreaker)).onTooltip(new OnTooltip() {

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
				return new ItemStack(ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.circuitbreaker));
			}

		}));

		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electricity.l19.circuitbreaker.1")).setSeparateStart().setIndentions(1));
		pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 75, 150, 75, Electrodynamics.rl("textures/screen/guidebook/breakeroff.png")));
		pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 75, 150, 75, Electrodynamics.rl("textures/screen/guidebook/breakeron.png")));
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electricity.l19.circuitbreaker.2")));

		// current regulator

		pageData.add(new TextWrapperObject(ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.currentregulator).getDescription().copy().withStyle(ChatFormatting.BOLD)).setCentered().setNewPage());
		pageData.add(new ItemWrapperObject(7 + ScreenGuidebook.TEXT_WIDTH / 2 - 16, 5, 32, 30, 30, 2.0F, ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.currentregulator)).onTooltip(new OnTooltip() {

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
				return new ItemStack(ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.currentregulator));
			}

		}));

		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electricity.l19.currentregulator.1")).setIndentions(1).setSeparateStart());

		// circuit monitor

		pageData.add(new TextWrapperObject(ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.circuitmonitor).getDescription().copy().withStyle(ChatFormatting.BOLD)).setCentered().setNewPage());
		pageData.add(new ItemWrapperObject(7 + ScreenGuidebook.TEXT_WIDTH / 2 - 16, 5, 32, 30, 30, 2.0F, ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.circuitmonitor)).onTooltip(new OnTooltip() {

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
				return new ItemStack(ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.circuitmonitor));
			}

		}));

		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electricity.l19.circuitmonitor.1")).setSeparateStart().setIndentions(1));
		pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 75, 150, 75, Electrodynamics.rl("textures/screen/guidebook/circuitmonitor.png")));
		pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 150, 150, 150, Electrodynamics.rl("textures/screen/guidebook/circuitmonitorgui.png")));
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electricity.l19.circuitmonitor.2", ElectroTextUtils.gui("networkwattage").withStyle(ChatFormatting.BOLD), ElectroTextUtils.gui("networkvoltage").withStyle(ChatFormatting.BOLD), ElectroTextUtils.gui("networkampacity").withStyle(ChatFormatting.BOLD), ElectroTextUtils.gui("networkminimumvoltage").withStyle(ChatFormatting.BOLD), ElectroTextUtils.gui("networkresistance").withStyle(ChatFormatting.BOLD), ElectroTextUtils.gui("networkload").withStyle(ChatFormatting.BOLD), ElectroTextUtils.gui("networkwattage"))).setSeparateStart().setIndentions(1));
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electricity.l19.circuitmonitor.3")).setSeparateStart().setIndentions(1));

		// Potentiometer

		pageData.add(new TextWrapperObject(ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.potentiometer).getDescription().copy().withStyle(ChatFormatting.BOLD)).setCentered().setNewPage());
		pageData.add(new ItemWrapperObject(7 + ScreenGuidebook.TEXT_WIDTH / 2 - 16, 5, 32, 30, 30, 2.0F, ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.potentiometer)).onTooltip(new OnTooltip() {

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
				return new ItemStack(ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.potentiometer));
			}

		}));

		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electricity.l19.potentiometer.1")).setIndentions(1).setSeparateStart());

		/* On FE */

		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electricity.topic.onfe").withStyle(ChatFormatting.BOLD, ChatFormatting.UNDERLINE)).setNewPage());

		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electricity.l20")).setSeparateStart().setIndentions(1));

		/* Summary */

		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electricity.topic.summary").withStyle(ChatFormatting.BOLD, ChatFormatting.UNDERLINE)).setNewPage());

		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electricity.l21")).setSeparateStart().setIndentions(1));

		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electricity.symbols").withStyle(ChatFormatting.BOLD)).setNewPage());
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electricity.symbvoltage")).setSeparateStart().setIndentions(1));
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electricity.symbcurrent")).setSeparateStart().setIndentions(1));
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electricity.symbresistance")).setSeparateStart().setIndentions(1));
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electricity.symbpower")).setSeparateStart().setIndentions(1));
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electricity.symbenergy")).setSeparateStart().setIndentions(1));
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electricity.symbturnsratio")).setSeparateStart().setIndentions(1));
		blankLine();

		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electricity.equations").withStyle(ChatFormatting.BOLD)).setSeparateStart());
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electricity.powerfromenergy")).setSeparateStart().setIndentions(1));
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electricity.powerfromvoltage")).setSeparateStart().setIndentions(1));
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electricity.energytickstoseconds")).setSeparateStart().setIndentions(1));
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electricity.ohmslaw")).setSeparateStart().setIndentions(1));
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electricity.powerfromcurrent")).setSeparateStart().setIndentions(1));
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electricity.turnsratioformula")).setSeparateStart().setIndentions(1));

	}

}
