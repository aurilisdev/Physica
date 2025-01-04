package electrodynamics.client.guidebook.utils.components;

import java.util.ArrayList;
import java.util.List;

import electrodynamics.client.guidebook.utils.pagedata.OnClick;
import electrodynamics.client.guidebook.utils.pagedata.OnKeyPress;
import electrodynamics.client.guidebook.utils.pagedata.OnTooltip;
import electrodynamics.client.guidebook.utils.pagedata.graphics.AbstractGraphicWrapper;
import electrodynamics.prefab.utilities.ElectroTextUtils;
import electrodynamics.prefab.utilities.math.Color;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;

public class Page {

	private final int pageNumber;
	public List<TextWrapper> text = new ArrayList<>();
	public List<GraphicWrapper> graphics = new ArrayList<>();
	public Chapter associatedChapter;

	public List<TextWrapper> tooltipText = new ArrayList<>();
	public List<GraphicWrapper> tooltipGraphics = new ArrayList<>();

	public List<TextWrapper> clickText = new ArrayList<>();
	public List<GraphicWrapper> clickGraphics = new ArrayList<>();

	public List<TextWrapper> keyPressText = new ArrayList<>();
	public List<GraphicWrapper> keyPressGraphics = new ArrayList<>();

	public Page(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public int getPage() {
		return pageNumber;
	}

	public void renderAdditionalText(GuiGraphics graphics, int refX, int refY, int xPageShift, Font font, int textWidth, int textStartX) {

		Module currMod = associatedChapter.module;
		Component moduleTitle = currMod.getTitle().withStyle(ChatFormatting.BOLD);
		int xShift = (textWidth - font.width(moduleTitle)) / 2;
		graphics.drawString(font, moduleTitle, refX + textStartX + xShift + xPageShift, refY + 16, Color.TEXT_GRAY.color(), false);

		Component chapTitle = associatedChapter.getTitle().withStyle(ChatFormatting.UNDERLINE);
		xShift = (textWidth - font.width(chapTitle)) / 2;
		graphics.drawString(font, chapTitle, refX + textStartX + xShift + xPageShift, refY + 26, Color.TEXT_GRAY.color(), false);

		Component pageNumber = Component.literal(getPage() + 1 + "");
		xShift = (textWidth - font.width(pageNumber)) / 2;
		graphics.drawString(font, pageNumber, refX + textStartX + xShift + xPageShift, refY + 200, Color.TEXT_GRAY.color(), false);

	}

	public record TextWrapper(int x, int y, FormattedText characters, Color color, boolean centered, OnTooltip onTooltip, OnClick onClick, OnKeyPress onKeyPress) {

	}

	public record GraphicWrapper(int x, int y, AbstractGraphicWrapper<?> graphic, OnTooltip onTooltip, OnClick onClick, OnKeyPress onKeyPress) {

	}

	public static class ChapterPage extends Page {

		public Module associatedModule;

		public ChapterPage(int pageNumber, Module module) {
			super(pageNumber);
			associatedModule = module;
		}

		@Override
		public void renderAdditionalText(GuiGraphics graphics, int refX, int refY, int xPageShift, Font font, int textWidth, int textStartX) {

			Module currMod = associatedModule;
			Component moduleTitle = currMod.getTitle().withStyle(ChatFormatting.BOLD);
			int xShift = (textWidth - font.width(moduleTitle)) / 2;
			graphics.drawString(font, moduleTitle, refX + xShift + textStartX + xPageShift, refY + 16, Color.TEXT_GRAY.color(), false);

			Component chapTitle = ElectroTextUtils.guidebook("chapters").withStyle(ChatFormatting.UNDERLINE);
			xShift = (textWidth - font.width(chapTitle)) / 2;
			graphics.drawString(font, chapTitle, refX + textStartX + xShift + xPageShift, refY + 31, Color.TEXT_GRAY.color(), false);
		}

	}

	public static class ModulePage extends Page {

		public ModulePage(int pageNumber) {
			super(pageNumber);
		}

		@Override
		public void renderAdditionalText(GuiGraphics graphics, int refX, int refY, int xPageShift, Font font, int textWidth, int textStartX) {
			Component modTitle = ElectroTextUtils.guidebook("availablemodules").withStyle(ChatFormatting.BOLD);
			int xShift = (textWidth - font.width(modTitle)) / 2;
			graphics.drawString(font, modTitle, refX + textStartX + xShift + xPageShift, refY + 16, Color.TEXT_GRAY.color(), false);
		}

	}

	public static class CoverPage extends Page {

		public CoverPage(int pageNumber) {
			super(pageNumber);
		}

		@Override
		public void renderAdditionalText(GuiGraphics graphics, int refX, int refY, int xPageShift, Font font, int textWidth, int textStartX) {
			// Not used as of now
		}

	}

}
