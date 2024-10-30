package electrodynamics.client.guidebook.chapters;

import java.util.ArrayList;
import java.util.List;

import electrodynamics.client.guidebook.ScreenGuidebook;
import electrodynamics.client.guidebook.utils.components.Chapter;
import electrodynamics.client.guidebook.utils.components.Module;
import electrodynamics.client.guidebook.utils.pagedata.OnKeyPress;
import electrodynamics.client.guidebook.utils.pagedata.OnTooltip;
import electrodynamics.client.guidebook.utils.pagedata.graphics.ItemWrapperObject;
import electrodynamics.client.guidebook.utils.pagedata.text.TextWrapperObject;
import electrodynamics.common.item.subtype.SubtypeItemUpgrade;
import electrodynamics.compatibility.jei.JeiBuffer;
import electrodynamics.prefab.utilities.ElectroTextUtils;
import electrodynamics.registers.ElectrodynamicsItems;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.ItemStack;

public class ChapterUpgrades extends Chapter {

	private static final ItemWrapperObject LOGO = new ItemWrapperObject(7, 10, 32, 32, 32, 2.0F, ElectrodynamicsItems.ITEMS_UPGRADE.getValue(SubtypeItemUpgrade.advancedspeed));

	public ChapterUpgrades(Module module) {
		super(module);
	}

	@Override
	public ItemWrapperObject getLogo() {
		return LOGO;
	}

	@Override
	public MutableComponent getTitle() {
		return ElectroTextUtils.guidebook("chapter.upgrades");
	}

	@Override
	public void addData() {
		// Injector Upgrade tutorial
		pageData.add(new TextWrapperObject(ElectrodynamicsItems.ITEMS_UPGRADE.getValue(SubtypeItemUpgrade.iteminput).getDescription().copy().withStyle(ChatFormatting.BOLD)).setCentered().setSeparateStart());
		pageData.add(new ItemWrapperObject(7 + ScreenGuidebook.TEXT_WIDTH / 2 - 16, 5, 32, 30, 30, 2.0F, ElectrodynamicsItems.ITEMS_UPGRADE.getValue(SubtypeItemUpgrade.iteminput)).onTooltip(new OnTooltip() {

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
				return new ItemStack(ElectrodynamicsItems.ITEMS_UPGRADE.getValue(SubtypeItemUpgrade.iteminput));
			}

		}));
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.upgrades.l1")).setIndentions(1).setSeparateStart());
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.upgrades.default").withStyle(ChatFormatting.BOLD)));
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.upgrades.l2")));
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.upgrades.smart").withStyle(ChatFormatting.BOLD)));
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.upgrades.l3")));

		// Ejector Upgrade tutorial
		pageData.add(new TextWrapperObject(ElectrodynamicsItems.ITEMS_UPGRADE.getValue(SubtypeItemUpgrade.itemoutput).getDescription().copy().withStyle(ChatFormatting.BOLD)).setCentered().setNewPage());
		pageData.add(new ItemWrapperObject(7 + ScreenGuidebook.TEXT_WIDTH / 2 - 16, 5, 32, 30, 30, 2.0F, ElectrodynamicsItems.ITEMS_UPGRADE.getValue(SubtypeItemUpgrade.itemoutput)).onTooltip(new OnTooltip() {

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
				return new ItemStack(ElectrodynamicsItems.ITEMS_UPGRADE.getValue(SubtypeItemUpgrade.itemoutput));
			}

		}));
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.upgrades.l4")).setIndentions(1).setSeparateStart());
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.upgrades.default").withStyle(ChatFormatting.BOLD)));
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.upgrades.l5")));
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.upgrades.smart").withStyle(ChatFormatting.BOLD)));
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.upgrades.l6")));
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.upgrades.l7")).setIndentions(1).setSeparateStart());

	}

}
