package electrodynamics.client.guidebook;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import com.mojang.blaze3d.platform.InputConstants;

import electrodynamics.Electrodynamics;
import electrodynamics.client.guidebook.utils.components.Chapter;
import electrodynamics.client.guidebook.utils.components.Module;
import electrodynamics.client.guidebook.utils.components.Page;
import electrodynamics.client.guidebook.utils.components.Page.ChapterPage;
import electrodynamics.client.guidebook.utils.components.Page.CoverPage;
import electrodynamics.client.guidebook.utils.components.Page.GraphicWrapper;
import electrodynamics.client.guidebook.utils.components.Page.ModulePage;
import electrodynamics.client.guidebook.utils.components.Page.TextWrapper;
import electrodynamics.client.guidebook.utils.pagedata.AbstractWrapperObject;
import electrodynamics.client.guidebook.utils.pagedata.OnClick;
import electrodynamics.client.guidebook.utils.pagedata.OnKeyPress;
import electrodynamics.client.guidebook.utils.pagedata.OnTooltip;
import electrodynamics.client.guidebook.utils.pagedata.graphics.AbstractGraphicWrapper;
import electrodynamics.client.guidebook.utils.pagedata.graphics.AbstractGraphicWrapper.GraphicTextDescriptor;
import electrodynamics.client.guidebook.utils.pagedata.graphics.ImageWrapperObject;
import electrodynamics.client.guidebook.utils.pagedata.text.TextWrapperObject;
import electrodynamics.common.inventory.container.item.ContainerGuidebook;
import electrodynamics.prefab.screen.GenericScreen;
import electrodynamics.prefab.screen.component.button.ScreenComponentButton;
import electrodynamics.prefab.screen.component.button.type.ButtonGuidebook;
import electrodynamics.prefab.screen.component.button.type.ButtonGuidebook.GuidebookButtonType;
import electrodynamics.prefab.screen.component.button.type.ButtonModuleSelector;
import electrodynamics.prefab.screen.component.button.type.ButtonSearchedText;
import electrodynamics.prefab.screen.component.button.type.ButtonSpecificPage;
import electrodynamics.prefab.screen.component.editbox.type.EditBoxSpecificPage;
import electrodynamics.prefab.screen.component.types.ScreenComponentGuidebookArrow;
import electrodynamics.prefab.screen.component.types.ScreenComponentGuidebookArrow.ArrowTextures;
import electrodynamics.prefab.utilities.ElectroTextUtils;
import electrodynamics.prefab.utilities.math.Color;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;

/**
 * A basic implementation of a Guidebook that allows for variable length text and images along with some basic formatting options.
 * 
 * @author skip999
 *
 */
public class ScreenGuidebook extends GenericScreen<ContainerGuidebook> {

	public static final List<Module> GUIDEBOOK_MODULES = new ArrayList<>();

	// 0 is defined as the starting page
	public static final int GUIDEBOOK_STARTING_PAGE = 0;

	public static final int MODULES_PER_PAGE = 4;
	public static final int CHAPTERS_PER_PAGE = 4;

	public static final int LEFT_TEXTURE_WIDTH = 171;
	public static final int LEFT_TEXTURE_HEIGHT = 224;

	public static final int RIGHT_TEXTURE_WIDTH = 171;
	public static final int RIGHT_TEXTURE_HEIGHT = 224;

	public static final int OLD_TEXTURE_WIDTH = 171;

	public static final int LEFT_X_SHIFT = -OLD_TEXTURE_WIDTH / 2 + 2;
	public static final int RIGHT_X_SHIFT = OLD_TEXTURE_WIDTH / 2 + 3;

	public static final int TEXT_START_Y = 40;
	public static final int TEXT_END_Y = 180;

	public static final int LINE_HEIGHT = 10;

	public static final int TEXT_START_X = 12;
	public static final int TEXT_END_X = 166;

	public static final int TEXT_WIDTH = TEXT_END_X - TEXT_START_X;
	public static final int LINES_PER_PAGE = (TEXT_END_Y - TEXT_START_Y) / LINE_HEIGHT + 1;// have to add one to get the last line

	public static final int Y_PIXELS_PER_PAGE = LINES_PER_PAGE * LINE_HEIGHT;

	private static final int MODULE_SEPERATION = 40;
	private static final int CHAPTER_SEPERATION = 35;

	public static int currPageNumber = 0;

	private int nextPageNumber = 0;

	private static final List<Page> PAGES = new ArrayList<>();

	public static final HashSet<Object> JEI_INGREDIENTS = new HashSet<>();

	private static final ResourceLocation PAGE_TEXTURE_LEFT = Electrodynamics.rl("textures/screen/guidebook/resources/guidebookpageleft.png");
	private static final ResourceLocation PAGE_TEXTURE_RIGHT = Electrodynamics.rl("textures/screen/guidebook/resources/guidebookpageright.png");

	private static ButtonGuidebook forward;
	private static ButtonGuidebook back;

	private static ButtonGuidebook home;
	private static ButtonGuidebook chapters;
	private static ButtonGuidebook search;

	private static EditBoxSpecificPage searchBox;

	private static ScreenComponentGuidebookArrow down;
	private static ScreenComponentGuidebookArrow up;

	private static ButtonModuleSelector caseSensitive;

	private static final List<ButtonModuleSelector> MODULE_PARAMETERS = new ArrayList<>();

	private static final List<SearchHit> SEARCHES = new ArrayList<>();

	private static final List<ButtonSearchedText> SEARCH_BUTTONS = new ArrayList<>();

	private int lineY = TEXT_START_Y;

	private int lineX = TEXT_START_X;

	private int graphicPixelHeightLeft = LINES_PER_PAGE * LINE_HEIGHT;
	private int graphicPixelWidthLeft = TEXT_WIDTH;

	private int previousHeight = 0;

	private Color color = Color.BLACK;
	private boolean centered = false;
	private MutableComponent mergedText = Component.empty();

	private OnTooltip textOnTooltip = null;

	private OnClick textOnClick = null;

	private OnKeyPress textOnKeyPress = null;

	private boolean previousWasText = false;

	private static boolean hasInitHappened = false;

	private static final List<ScreenComponentButton<?>> BUTTONS = new ArrayList<>();

	public ScreenGuidebook(ContainerGuidebook screenContainer, Inventory inv, Component titleIn) {
		super(screenContainer, inv, titleIn);
		imageHeight += 58;
		inventoryLabelY += 58;
	}

	@Override
	protected void init() {

		if (!hasInitHappened) {

			BUTTONS.clear();
			MODULE_PARAMETERS.clear();
			SEARCH_BUTTONS.clear();
			SEARCHES.clear();
			PAGES.clear();

			initPageButtons();

			sortModules();

			PAGES.add(getCoverPage());
			nextPageNumber++;

			genModulePages();

			genPages();

			genSearchPages();

			hasInitHappened = true;
		}

		addButtons();

		super.init();

	}

	private void addButtons() {

		addComponent(forward);
		addComponent(back);

		addComponent(home);
		addComponent(chapters);
		addComponent(search);

		for (ScreenComponentButton<?> button : BUTTONS) {
			addComponent(button);
		}
		for (ButtonModuleSelector button : MODULE_PARAMETERS) {
			addComponent(button);
		}
		for (ButtonSearchedText button : SEARCH_BUTTONS) {
			addComponent(button);
		}

		addComponent(caseSensitive);

		addEditBox(searchBox);

		addComponent(up);
		addComponent(down);
	}

	private void sortModules() {
		List<Module> temp = new ArrayList<>(GUIDEBOOK_MODULES);
		GUIDEBOOK_MODULES.clear();
		GUIDEBOOK_MODULES.add(temp.get(0));
		temp.remove(0);
		List<MutableComponent> cats = new ArrayList<>();
		for (Module mod : temp) {
			cats.add(mod.getTitle());
		}
		cats.sort((component1, component2) -> component1.toString().compareToIgnoreCase(component2.toString()));
		for (MutableComponent cat : cats) {
			for (int i = 0; i < temp.size(); i++) {
				Module mod = temp.get(i);
				if (mod.isCat(cat)) {
					GUIDEBOOK_MODULES.add(mod);
					temp.remove(i);
					break;
				}
			}
		}
		for (Module module : GUIDEBOOK_MODULES) {
			module.chapters.clear();
			module.addChapters();
		}
	}

	private void initPageButtons() {
		forward = new ButtonGuidebook(GuidebookButtonType.RIGHT, 142 - 45, 200).setOnPress(button -> pageForward());
		back = new ButtonGuidebook(GuidebookButtonType.LEFT, 10 + 44, 200).setOnPress(button -> pageBackward());

		home = new ButtonGuidebook(GuidebookButtonType.HOME, 115 - 186, 202).setOnPress(button -> goToModulePage());
		chapters = new ButtonGuidebook(GuidebookButtonType.CHAPTERS, 50 - 100, 202).setOnPress(button -> goToChapterPage());
		search = new ButtonGuidebook(GuidebookButtonType.SEARCH, 235, 202).setOnPress(button -> goToSearchPage());
	}

	private void genModulePages() {

		int numPages = (int) Math.ceil((double) GUIDEBOOK_MODULES.size() / (double) MODULES_PER_PAGE);
		int index = 0;
		for (int i = 0; i < numPages; i++) {
			final ModulePage page = new ModulePage(nextPageNumber);

			for (int j = 0; j < MODULES_PER_PAGE; j++) {

				if (index >= GUIDEBOOK_MODULES.size()) {
					break;
				}
				Module module = GUIDEBOOK_MODULES.get(index);
				GraphicWrapper wrapper = new GraphicWrapper(TEXT_START_X, j * MODULE_SEPERATION + TEXT_START_Y - 2, module.getLogo(), module.onTooltip(), module.onClick(), module.onKeyPress());
				page.graphics.add(wrapper);
				if (module.onTooltip() != null) {
					page.tooltipGraphics.add(wrapper);
				}
				if (module.onClick() != null) {
					page.clickGraphics.add(wrapper);
				}
				if (module.onKeyPress() != null) {
					page.keyPressGraphics.add(wrapper);
				}
				int xShift = nextPageNumber % 2 == 0 ? LEFT_X_SHIFT : RIGHT_X_SHIFT - 8;
				BUTTONS.add(new ButtonSpecificPage(45 + xShift, 43 + j * MODULE_SEPERATION, 120, 20, nextPageNumber).setLabel(module.getTitle()).setOnPress(button -> setPageNumber(module.getPage())));
				index++;
			}

			PAGES.add(page);

			nextPageNumber++;
		}

	}

	private void genPages() {

		for (Module module : GUIDEBOOK_MODULES) {

			module.setStartPage(nextPageNumber);

			int numPages = (int) Math.ceil((double) module.chapters.size() / (double) CHAPTERS_PER_PAGE);
			int index = 0;

			for (int i = 0; i < numPages; i++) {
				final ChapterPage chapterPage = new ChapterPage(nextPageNumber, module);
				for (int j = 0; j < CHAPTERS_PER_PAGE; j++) {
					if (index >= module.chapters.size()) {
						break;
					}
					final Chapter chapter = module.chapters.get(index);

					GraphicWrapper wrapper = new GraphicWrapper(TEXT_START_X, j * CHAPTER_SEPERATION + TEXT_START_Y + 10, chapter.getLogo(), chapter.onTooltip(), chapter.onClick(), chapter.onKeyPress());

					chapterPage.graphics.add(wrapper);

					if (chapter.onTooltip() != null) {
						chapterPage.tooltipGraphics.add(wrapper);
					}

					if (chapter.onClick() != null) {
						chapterPage.clickGraphics.add(wrapper);
					}

					if (chapter.onKeyPress() != null) {
						chapterPage.keyPressGraphics.add(wrapper);
					}

					int xShift = nextPageNumber % 2 == 0 ? LEFT_X_SHIFT : RIGHT_X_SHIFT - 8;

					BUTTONS.add(new ButtonSpecificPage(45 + xShift, 56 + j * CHAPTER_SEPERATION, 120, 20, nextPageNumber).setLabel(chapter.getTitle()).setOnPress(button -> setPageNumber(chapter.getStartPage())));

					index++;
				}
				PAGES.add(chapterPage);
				nextPageNumber++;

			}

			for (Chapter chapter : module.chapters) {

				chapter.setStartPage(nextPageNumber);

				Page currentPage = new Page(nextPageNumber);
				currentPage.associatedChapter = chapter;
				nextPageNumber++;

				int counter = 0;
				for (AbstractWrapperObject<?> data : chapter.pageData) {

					if (data instanceof TextWrapperObject textWrapper) {

						previousWasText = true;
						lineX = TEXT_START_X;
						graphicPixelWidthLeft = TEXT_WIDTH;
						previousHeight = 0;

						if (textWrapper == TextWrapperObject.BLANK_LINE) {

							currentPage = writeCurrentTextToPage(currentPage, chapter);

							graphicPixelHeightLeft -= LINE_HEIGHT;

							if (graphicPixelHeightLeft <= 0) {
								currentPage = resetToNewPage(currentPage, chapter);
							} else {
								lineY += LINE_HEIGHT;
							}

						} else {

							if (textWrapper.newPage || textWrapper.isSeparateStart) {

								currentPage = writeCurrentTextToPage(currentPage, chapter);

							}

							centered = textWrapper.center;
							color = textWrapper.color;

							textOnTooltip = textWrapper.onTooltip;
							textOnClick = textWrapper.onClick;
							textOnKeyPress = textWrapper.onKeyPress;

							MutableComponent indentions = Component.empty();

							for (int i = 0; i < textWrapper.numberOfIndentions; i++) {
								indentions = indentions.append("    ");
							}

							mergedText = mergedText.append(indentions.append(textWrapper.text));

							if (textWrapper.newPage) {

								currentPage = resetToNewPage(currentPage, chapter);

							}
						}

					} else if (data instanceof AbstractGraphicWrapper<?> graphicWrapper) {

						if (previousWasText) {
							currentPage = writeCurrentTextToPage(currentPage, chapter);
							centered = false;
							previousWasText = false;
						}

						if (graphicWrapper.newPage) {
							currentPage = resetToNewPage(currentPage, chapter);
							previousHeight = 0;
						}

						int trueHeight = graphicWrapper.trueHeight - graphicWrapper.descriptorTopOffset + graphicWrapper.descriptorBottomOffset;

						if (trueHeight > Y_PIXELS_PER_PAGE) {
							throw new UnsupportedOperationException("The image cannot be more than " + (Y_PIXELS_PER_PAGE) + " pixels tall!");
						}

						if (graphicWrapper.allowNextToOthers && graphicWrapper.width <= graphicPixelWidthLeft) {
							graphicPixelWidthLeft -= graphicWrapper.width;

							GraphicWrapper wrapper = new GraphicWrapper(lineX, lineY, graphicWrapper, graphicWrapper.onTooltip, graphicWrapper.onClick, graphicWrapper.onKeyPress);

							currentPage.graphics.add(wrapper);

							if (graphicWrapper.onTooltip != null) {
								currentPage.tooltipGraphics.add(wrapper);
							}

							if (graphicWrapper.onClick != null) {
								currentPage.clickGraphics.add(wrapper);
							}

							if (graphicWrapper.onKeyPress != null) {
								currentPage.keyPressGraphics.add(wrapper);
							}

							lineX += graphicWrapper.width;

							if (trueHeight > previousHeight) {
								lineY += previousHeight;
								previousHeight = trueHeight;
								lineY -= trueHeight;
							}

						} else {
							lineX = TEXT_START_X;
							graphicPixelWidthLeft = TEXT_WIDTH;
							previousHeight = 0;

							if (trueHeight > graphicPixelHeightLeft) {

								currentPage = resetToNewPage(currentPage, chapter);
								previousHeight = trueHeight;

							}

							GraphicWrapper wrapper = new GraphicWrapper(lineX, lineY, graphicWrapper, graphicWrapper.onTooltip, graphicWrapper.onClick, graphicWrapper.onKeyPress);

							currentPage.graphics.add(wrapper);

							if (graphicWrapper.onTooltip != null) {
								currentPage.tooltipGraphics.add(wrapper);
							}

							if (graphicWrapper.onClick != null) {
								currentPage.clickGraphics.add(wrapper);
							}

							if (graphicWrapper.onKeyPress != null) {
								currentPage.keyPressGraphics.add(wrapper);
							}

							lineY += trueHeight;
							graphicPixelHeightLeft -= trueHeight;
						}

						if (graphicPixelHeightLeft == 0 && counter < module.chapters.size() - 1) {
							currentPage = resetToNewPage(currentPage, chapter);
							previousHeight = 0;
						}

					}

					counter++;

				}

				currentPage = writeCurrentTextToPage(currentPage, chapter);
				PAGES.add(currentPage);
				graphicPixelHeightLeft = Y_PIXELS_PER_PAGE;
				lineY = TEXT_START_Y;
				lineX = TEXT_START_X;
				graphicPixelWidthLeft = TEXT_WIDTH;

			}

		}

		if (PAGES.size() % 2 == 1) {
			PAGES.add(new CoverPage(nextPageNumber));
			nextPageNumber++;
		}

	}

	private Page writeCurrentTextToPage(Page currentPage, Chapter chapter) {

		if (mergedText.equals(Component.empty())) {
			return currentPage;
		}

		int remainder = lineY % LINE_HEIGHT;
		if (remainder > 0) {
			int whole = lineY - remainder;
			lineY = whole + LINE_HEIGHT;
			graphicPixelHeightLeft = TEXT_END_Y + LINE_HEIGHT - lineY;
		}

		List<FormattedText> text = new ArrayList<>(font.getSplitter().splitLines(mergedText, TEXT_WIDTH, Style.EMPTY));
		mergedText = Component.empty();

		while (text.size() > 0) {

			if (graphicPixelHeightLeft <= 0) {
				currentPage = resetToNewPage(currentPage, chapter);
			}
			TextWrapper wrapper = new TextWrapper(lineX, lineY, text.get(0), color, centered, textOnTooltip, textOnClick, textOnKeyPress);
			currentPage.text.add(wrapper);
			if (textOnTooltip != null) {
				currentPage.tooltipText.add(wrapper);
			}
			if (textOnClick != null) {
				currentPage.clickText.add(wrapper);
			}
			if (textOnKeyPress != null) {
				currentPage.keyPressText.add(wrapper);
			}

			text.remove(0);

			lineY += LINE_HEIGHT;

			graphicPixelHeightLeft -= LINE_HEIGHT;
		}

		textOnTooltip = null;
		textOnClick = null;
		textOnKeyPress = null;

		return currentPage;

	}

	private Page resetToNewPage(Page page, Chapter chapter) {
		PAGES.add(page);
		page = new Page(nextPageNumber);
		page.associatedChapter = chapter;
		nextPageNumber++;

		graphicPixelHeightLeft = Y_PIXELS_PER_PAGE;
		lineY = TEXT_START_Y;
		lineX = TEXT_START_X;
		graphicPixelWidthLeft = TEXT_WIDTH;

		return page;
	}

	private void genSearchPages() {

		PAGES.add(getSearchPageLeft());
		nextPageNumber++;

		PAGES.add(getSeatchPageRight());
		nextPageNumber++;

	}

	@Override
	protected void renderBg(GuiGraphics graphics, float partialTick, int x, int y) {

		int guiWidth = (width - imageWidth) / 2;
		int guiHeight = (height - imageHeight) / 2;

		graphics.blit(PAGE_TEXTURE_LEFT, guiWidth + LEFT_X_SHIFT, guiHeight, 0, 0, LEFT_TEXTURE_WIDTH, LEFT_TEXTURE_HEIGHT);

		graphics.blit(PAGE_TEXTURE_RIGHT, guiWidth + RIGHT_X_SHIFT, guiHeight, 0, 0, RIGHT_TEXTURE_WIDTH, RIGHT_TEXTURE_HEIGHT);

		updatePageArrowVis();

		renderPageBackground(graphics, LEFT_X_SHIFT, guiWidth, guiHeight, getCurrentPage());
		renderPageBackground(graphics, RIGHT_X_SHIFT - 8, guiWidth, guiHeight, getNextPage());

	}

	private void renderPageBackground(GuiGraphics graphics, int xShift, int guiWidth, int guiHeight, Page page) {

		for (GraphicWrapper graphic : page.graphics) {
			graphic.graphic().render(graphics, graphic.x(), graphic.y(), xShift, guiWidth, guiHeight, page);
		}

	}

	@Override
	protected void renderLabels(GuiGraphics graphics, int x, int y) {

		int refX = getXRef();
		int refY = getYRef();

		renderPageLabels(graphics, LEFT_X_SHIFT, refX, refY, getCurrentPage());
		renderPageLabels(graphics, RIGHT_X_SHIFT - 8, refX, refY, getNextPage());

		int guiWidth = (width - imageWidth) / 2;
		int guiHeight = (height - imageHeight) / 2;

		int xAxis = x - guiWidth;
		int yAxis = y - guiHeight;

		renderPageTooltips(graphics, LEFT_X_SHIFT, x, y, refX, refY, xAxis, yAxis, guiWidth, guiHeight, getCurrentPage());
		renderPageTooltips(graphics, RIGHT_X_SHIFT - 8, x, y, refX, refY, xAxis, yAxis, guiWidth, guiHeight, getNextPage());

	}

	private void renderPageLabels(GuiGraphics graphics, int xPageShift, int refX, int refY, Page page) {

		for (TextWrapper text : page.text) {

			if (text.centered()) {
				int xShift = (TEXT_WIDTH - font.width(text.characters())) / 2;
				graphics.drawString(getFontRenderer(), Language.getInstance().getVisualOrder(text.characters()), text.x() + refX + xShift + xPageShift, refY + text.y(), text.color().color(), false);
			} else {
				graphics.drawString(getFontRenderer(), Language.getInstance().getVisualOrder(text.characters()), text.x() + refX + xPageShift, text.y() + refY, text.color().color(), false);
			}

		}

		for (GraphicWrapper wrapper : page.graphics) {

			AbstractGraphicWrapper<?> graphic = wrapper.graphic();

			for (GraphicTextDescriptor descriptor : graphic.descriptors) {
				graphics.drawString(getFontRenderer(), descriptor.text, refX + wrapper.x() + descriptor.xOffsetFromImage + xPageShift, refY + wrapper.y() + descriptor.yOffsetFromImage, descriptor.color, false);
			}

		}

		page.renderAdditionalText(graphics, refX, refY, xPageShift, font, TEXT_WIDTH, TEXT_START_X);

	}

	private void renderPageTooltips(GuiGraphics graphics, int xPageShift, int mouseX, int mouseY, int refX, int refY, int xAxis, int yAxis, int guiWidth, int guiHeight, Page page) {

		int textWidth = 0;
		int xShift = 0;

		for (TextWrapper text : page.tooltipText) {

			textWidth = font.width(text.characters());

			if (text.centered()) {
				xShift = (TEXT_WIDTH - textWidth) / 2;

			} else {
				xShift = 0;
			}

			if (isPointInRegionText(refX + xShift + xPageShift + text.x(), refY + text.y(), xAxis, yAxis, textWidth, LINE_HEIGHT)) {
				text.onTooltip().onTooltip(graphics, xAxis, yAxis, this);
			}

		}

		xShift = 0;

		for (GraphicWrapper wrapper : page.tooltipGraphics) {

			AbstractGraphicWrapper<?> graphic = wrapper.graphic();

			if (isPointInRegionGraphic(mouseX, mouseY, guiWidth + wrapper.x() + graphic.lookupXOffset + xPageShift, guiHeight + wrapper.y() + graphic.lookupYOffset - graphic.descriptorTopOffset, graphic.width, graphic.height)) {
				wrapper.onTooltip().onTooltip(graphics, xAxis, yAxis, this);
			}

			for (GraphicTextDescriptor descriptor : graphic.descriptors) {

				if (descriptor.onTooltip != null && isPointInRegionText(refX + wrapper.x() + descriptor.xOffsetFromImage + xPageShift, refY + wrapper.y() + descriptor.yOffsetFromImage, xAxis, yAxis, font.width(descriptor.text), LINE_HEIGHT)) {
					descriptor.onTooltip.onTooltip(graphics, xAxis, yAxis, this);
				}

			}

		}

	}

	public Page getCurrentPage() {

		if (currPageNumber >= PAGES.size()) {
			currPageNumber = PAGES.size() - 2;
		}
		return PAGES.get(currPageNumber);
	}

	public Page getNextPage() {
		if (currPageNumber >= PAGES.size()) {
			currPageNumber = PAGES.size() - 2;
		}
		return PAGES.get(currPageNumber + 1);
	}

	protected void pageForward() {
		if (currPageNumber < PAGES.size() - 2) {
			movePage(2);
		}
		updatePageArrowVis();
	}

	protected void pageBackward() {
		if (currPageNumber > GUIDEBOOK_STARTING_PAGE + 1) {
			movePage(-2);
		}
		updatePageArrowVis();
	}

	protected void goToChapterPage() {
		Page page = getCurrentPage();
		if (page instanceof ModulePage module) {
			setPageNumber(module.getPage());
		} else if (page instanceof ChapterPage chapter) {
			setPageNumber(chapter.associatedModule.getPage());
		} else {
			setPageNumber(page.associatedChapter.module.getPage());
		}
	}

	protected void goToSearchPage() {
		setPageNumber(PAGES.size() - 2);
	}

	protected void goToModulePage() {
		setPageNumber(1);
	}

	private void movePage(int number) {
		currPageNumber += number;
		currPageNumber = Mth.clamp(currPageNumber, 0, PAGES.size() - 4);
	}

	private static void setPageNumber(int number) {
		if (number % 2 == 1) {
			number = number - 1;
		}
		currPageNumber = number;
	}

	private void updatePageArrowVis() {
		forward.setVisible(currPageNumber < PAGES.size() - 4);
		back.setVisible(currPageNumber > GUIDEBOOK_STARTING_PAGE && currPageNumber < PAGES.size() - 2);
		home.setVisible(currPageNumber != 0);
		chapters.setVisible(currPageNumber != 0 && currPageNumber < PAGES.size() - 4);
		search.setVisible(currPageNumber < PAGES.size() - 3);
	}

	public static void addGuidebookModule(Module module) {
		if (module instanceof ModuleElectrodynamics) {
			GUIDEBOOK_MODULES.add(0, module);
		} else {
			GUIDEBOOK_MODULES.add(module);
		}

	}

	private CoverPage getCoverPage() {

		CoverPage page = new CoverPage(nextPageNumber);

		List<FormattedText> split = font.getSplitter().splitLines(ElectroTextUtils.guidebook("title").withStyle(ChatFormatting.BOLD), TEXT_WIDTH, Style.EMPTY);

		int y = 16;

		for (FormattedText text : split) {

			page.text.add(new TextWrapper(TEXT_START_X, y, text, TextWrapperObject.DEFAULT_COLOR, true, null, null, null));
			y += LINE_HEIGHT;

		}

		y += 5;

		int xOffset = (TEXT_WIDTH - 74) / 2;

		page.graphics.add(new GraphicWrapper(TEXT_START_X, y, new ImageWrapperObject(xOffset, 0, 0, 0, 74, 100, 74, 100, Electrodynamics.rl("textures/screen/guidebook/resources/guidebookcover.png")), null, null, null));

		y += 105;

		split = font.getSplitter().splitLines(ElectroTextUtils.guidebook("titlequote"), TEXT_WIDTH, Style.EMPTY);

		for (FormattedText text : split) {

			page.text.add(new TextWrapper(TEXT_START_X, y, text, TextWrapperObject.DEFAULT_COLOR, false, null, null, null));
			y += LINE_HEIGHT;

		}

		return page;

	}

	private CoverPage getSearchPageLeft() {
		CoverPage page = new CoverPage(nextPageNumber);

		List<FormattedText> split = font.getSplitter().splitLines(ElectroTextUtils.guidebook("searchparameters").withStyle(ChatFormatting.BOLD), TEXT_WIDTH, Style.EMPTY);

		int y = 16;

		for (FormattedText text : split) {

			page.text.add(new TextWrapper(TEXT_START_X, y, text, TextWrapperObject.DEFAULT_COLOR, true, null, null, null));
			y += LINE_HEIGHT + 2;

		}

		y += 5;

		for (Module module : GUIDEBOOK_MODULES) {

			page.text.add(new TextWrapper(TEXT_START_X + 15, y, module.getTitle(), TextWrapperObject.DEFAULT_COLOR, false, null, null, null));

			ButtonModuleSelector selector = new ButtonModuleSelector(70, -1 + y, nextPageNumber, true);

			MODULE_PARAMETERS.add(selector);

			y += LINE_HEIGHT;

		}

		page.text.add(new TextWrapper(TEXT_START_X + 15, 165, ElectroTextUtils.guidebook("casesensitive"), TextWrapperObject.DEFAULT_COLOR, false, null, null, null));
		caseSensitive = new ButtonModuleSelector(70, 165, nextPageNumber, false);

		BUTTONS.add(new ButtonSpecificPage(-71, 180, 75, 20, nextPageNumber).setLabel(ElectroTextUtils.guidebook("selectall")).setOnPress(button -> {
			for (ButtonModuleSelector selector : MODULE_PARAMETERS) {
				selector.setSelected(true);
			}
		}));

		BUTTONS.add(new ButtonSpecificPage(8, 180, 75, 20, nextPageNumber).setLabel(ElectroTextUtils.guidebook("selectnone")).setOnPress(button -> {
			for (ButtonModuleSelector selector : MODULE_PARAMETERS) {
				selector.setSelected(false);
			}
		}));

		return page;
	}

	private CoverPage getSeatchPageRight() {
		CoverPage page = new CoverPage(nextPageNumber);

		searchBox = new EditBoxSpecificPage(92, 10, TEXT_WIDTH, 12, nextPageNumber, getFontRenderer());
		searchBox.setResponder(this::onTextSearched);
		searchBox.setTextColor(Color.WHITE);
		searchBox.setMaxLength(100);

		for (int i = 0; i < SEARCH_BUTTON_COUNT; i++) {
			ButtonSearchedText search = (ButtonSearchedText) new ButtonSearchedText(92, 35 + 35 * i, TEXT_WIDTH, 20, nextPageNumber).setOnPress(button -> setPageNumber(((ButtonSearchedText) button).specifiedPage));
			SEARCH_BUTTONS.add(search);
			search.setShouldShow(false);
		}

		down = new ScreenComponentGuidebookArrow(ArrowTextures.ARROW_DOWN, 174, 200, nextPageNumber);

		up = new ScreenComponentGuidebookArrow(ArrowTextures.ARROW_UP, 154, 200, nextPageNumber);

		return page;
	}

	private final int minScroll = 0;
	private int maxScroll = 0;
	private int scrollIndex = 0;

	private static final int SEARCH_BUTTON_COUNT = 5;

	private void onTextSearched(String text) {

		SEARCHES.clear();

		if (text.isEmpty() || text.isBlank()) {
			maxScroll = 0;
			scrollIndex = 0;
			resetSearchButtons();
			return;
		}

		List<SearchHit> found = new ArrayList<>();

		List<Module> selectedModules = new ArrayList<>();

		for (int i = 0; i < GUIDEBOOK_MODULES.size(); i++) {
			if (MODULE_PARAMETERS.get(i).isSelected()) {
				selectedModules.add(GUIDEBOOK_MODULES.get(i));
			}
		}

		for (Page page : PAGES) {

			if (!(page instanceof ChapterPage || page instanceof ModulePage || page instanceof CoverPage)) {

				for (Module module : selectedModules) {
					if (page.associatedChapter.module.isCat(module.getTitle())) {
						for (TextWrapper wrapper : page.text) {

							if (caseSensitive.isSelected() && wrapper.characters().getString().contains(text)) {

								found.add(new SearchHit(wrapper.characters(), page.getPage(), page.associatedChapter));

							} else if (!caseSensitive.isSelected() && wrapper.characters().getString().toLowerCase(Locale.ROOT).contains(text.toLowerCase())) {

								found.add(new SearchHit(wrapper.characters(), page.getPage(), page.associatedChapter));

							}

						}

						for (GraphicWrapper graphic : page.graphics) {
							for (GraphicTextDescriptor descriptor : graphic.graphic().descriptors) {
								if (caseSensitive.isSelected() && descriptor.text.getString().contains(text)) {

									found.add(new SearchHit(descriptor.text, page.getPage(), page.associatedChapter));

								} else if (!caseSensitive.isSelected() && descriptor.text.getString().toLowerCase(Locale.ROOT).contains(text.toLowerCase())) {

									found.add(new SearchHit(descriptor.text, page.getPage(), page.associatedChapter));

								}
							}
						}

					}
				}
			}

		}
		SEARCHES.addAll(found);

		maxScroll = Math.max(0, SEARCHES.size() - SEARCH_BUTTON_COUNT);

		if (scrollIndex > maxScroll) {
			scrollIndex = maxScroll;
		}

		updateSearchButtons();

	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double deltaX, double deltaY) {

		if (SEARCHES.size() == 0) {
			resetSearchButtons();
			return super.mouseScrolled(mouseX, mouseY, deltaX, deltaY);
		}

		if (currPageNumber >= PAGES.size() - 2) {

			int scrollDelta = deltaY > 0 ? -1 : 1;

			scrollIndex = Mth.clamp(scrollIndex + scrollDelta, minScroll, maxScroll);

			updateSearchButtons();

		}
		return super.mouseScrolled(mouseX, mouseY, deltaX, deltaY);
	}

	private void updateSearchButtons() {
		up.setShouldRender(scrollIndex > minScroll);
		down.setShouldRender(scrollIndex < maxScroll);

		resetSearchButtons();

		if (SEARCHES.size() == 0) {
			return;
		}

		if (scrollIndex > maxScroll) {
			scrollIndex = maxScroll;
		}

		int listSize = Math.min(SEARCH_BUTTON_COUNT, SEARCHES.size());

		for (int i = 0; i < listSize; i++) {

			ButtonSearchedText searched = SEARCH_BUTTONS.get(i);
			SearchHit hit = SEARCHES.get(i + scrollIndex);

			searched.setShouldShow(true);
			searched.setChapter(hit.chapter.getTitle());
			searched.setLine(hit.text);
			searched.setPage(hit.page);

		}

	}

	private void resetSearchButtons() {
		for (ButtonSearchedText button : SEARCH_BUTTONS) {
			button.setLine(Component.empty());
			button.setChapter(Component.empty());
			button.setShouldShow(false);
			button.setPage(0);
		}
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {

		double mouseX = minecraft.mouseHandler.xpos() * minecraft.getWindow().getGuiScaledWidth() / minecraft.getWindow().getScreenWidth();
		double mouseY = minecraft.mouseHandler.ypos() * minecraft.getWindow().getGuiScaledHeight() / minecraft.getWindow().getScreenHeight();

		int refX = getXRef();
		int refY = getYRef();

		int guiWidth = (width - imageWidth) / 2;
		int guiHeight = (height - imageHeight) / 2;

		int xAxis = (int) (mouseX - guiWidth);
		int yAxis = (int) (mouseY - guiHeight);

		handlePageKeyPress(LEFT_X_SHIFT, (int) mouseX, (int) mouseY, refX, refY, xAxis, yAxis, guiWidth, guiHeight, keyCode, scanCode, modifiers, getCurrentPage());
		handlePageKeyPress(RIGHT_X_SHIFT - 8, (int) mouseX, (int) mouseY, refX, refY, xAxis, yAxis, guiWidth, guiHeight, keyCode, scanCode, modifiers, getNextPage());

		InputConstants.Key mouseKey = InputConstants.getKey(keyCode, scanCode);
		if (this.minecraft.options.keyInventory.isActiveAndMatches(mouseKey) && searchBox.isVisible() && searchBox.isFocused()) {
			return false;
		}
		return super.keyPressed(keyCode, scanCode, modifiers);
	}

	private void handlePageKeyPress(int xPageShift, int mouseX, int mouseY, int refX, int refY, int xAxis, int yAxis, int guiWidth, int guiHeight, int keyCode, int scanCode, int modifiers, Page page) {

		int textWidth = 0;
		int xShift = 0;

		int x = 0;
		int y = 0;

		for (TextWrapper text : page.keyPressText) {

			textWidth = font.width(text.characters());

			if (text.centered()) {
				xShift = (TEXT_WIDTH - textWidth) / 2;

			}

			x = refX + xShift + xPageShift + text.x();
			y = refY + text.y();

			if (isPointInRegionText(x, y, xAxis, yAxis, textWidth, LINE_HEIGHT)) {
				text.onKeyPress().onKeyPress(keyCode, scanCode, modifiers, x, y, xAxis, yAxis, this);
			}

		}

		for (GraphicWrapper wrapper : page.keyPressGraphics) {

			AbstractGraphicWrapper<?> graphic = wrapper.graphic();

			x = guiWidth + wrapper.x() + graphic.lookupXOffset + xPageShift;
			y = guiHeight + wrapper.y() + graphic.lookupYOffset - graphic.descriptorTopOffset;

			if (isPointInRegionGraphic(mouseX, mouseY, x, y, graphic.width, graphic.height)) {
				wrapper.onKeyPress().onKeyPress(keyCode, scanCode, modifiers, x, y, xAxis, yAxis, this);
			}

			for (GraphicTextDescriptor descriptor : graphic.descriptors) {

				x = refX + wrapper.x() + descriptor.xOffsetFromImage + xPageShift;
				y = refY + wrapper.y() + descriptor.yOffsetFromImage;

				if (descriptor.onKeyPress != null && isPointInRegionText(x, y, xAxis, yAxis, font.width(descriptor.text), LINE_HEIGHT)) {
					descriptor.onKeyPress.onKeyPress(keyCode, scanCode, modifiers, x, y, xAxis, yAxis, this);
				}

			}

		}
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		int refX = getXRef();
		int refY = getYRef();

		int guiWidth = (width - imageWidth) / 2;
		int guiHeight = (height - imageHeight) / 2;

		int xAxis = (int) (mouseX - guiWidth);
		int yAxis = (int) (mouseY - guiHeight);

		handlePageClick(LEFT_X_SHIFT, (int) mouseX, (int) mouseY, refX, refY, xAxis, yAxis, guiWidth, guiHeight, getCurrentPage());
		handlePageClick(RIGHT_X_SHIFT - 8, (int) mouseX, (int) mouseY, refX, refY, xAxis, yAxis, guiWidth, guiHeight, getNextPage());

		return super.mouseClicked(mouseX, mouseY, button);
	}

	private void handlePageClick(int xPageShift, int mouseX, int mouseY, int refX, int refY, int xAxis, int yAxis, int guiWidth, int guiHeight, Page page) {

		int textWidth = 0;
		int xShift = 0;

		int x = 0;
		int y = 0;

		for (TextWrapper text : page.clickText) {

			textWidth = font.width(text.characters());

			if (text.centered()) {
				xShift = (TEXT_WIDTH - textWidth) / 2;

			}

			x = refX + xShift + xPageShift + text.x();
			y = refY + text.y();

			if (isPointInRegionText(x, y, xAxis, yAxis, textWidth, LINE_HEIGHT)) {
				text.onClick().onClick(x, y, xAxis, yAxis, this);
			}

		}

		for (GraphicWrapper wrapper : page.clickGraphics) {

			AbstractGraphicWrapper<?> graphic = wrapper.graphic();

			x = guiWidth + wrapper.x() + graphic.lookupXOffset + xPageShift;
			y = guiHeight + wrapper.y() + graphic.lookupYOffset - graphic.descriptorTopOffset;

			if (isPointInRegionGraphic(mouseX, mouseY, x, y, graphic.width, graphic.height)) {
				wrapper.onClick().onClick(x, y, xAxis, yAxis, this);
			}

			for (GraphicTextDescriptor descriptor : graphic.descriptors) {

				x = refX + wrapper.x() + descriptor.xOffsetFromImage + xPageShift;
				y = refY + wrapper.y() + descriptor.yOffsetFromImage;

				if (descriptor.onClick != null && isPointInRegionText(x, y, xAxis, yAxis, font.width(descriptor.text), LINE_HEIGHT)) {
					descriptor.onClick.onClick(x, y, xAxis, yAxis, this);
				}

			}

		}

	}

	public boolean isPointInRegionText(int x, int y, double xAxis, double yAxis, int width, int height) {
		return xAxis >= x && xAxis <= x + width - 1 && yAxis >= y && yAxis <= y + height - 1;
	}

	public boolean isPointInRegionGraphic(int mouseX, int mouseY, int x, int y, int width, int height) {
		return mouseX >= x && mouseY >= y && mouseX < (x + width) && mouseY < (y + height);
	}

	public static void setInitNotHappened() {
		currPageNumber = 0;
		hasInitHappened = false;
	}

	public int getXRef() {
		return titleLabelX - 8;
	}

	public int getYRef() {
		return titleLabelY - 6;
	}

	private record SearchHit(FormattedText text, int page, Chapter chapter) {

	}

}
