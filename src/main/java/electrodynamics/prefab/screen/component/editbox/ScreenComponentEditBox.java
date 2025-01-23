package electrodynamics.prefab.screen.component.editbox;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import electrodynamics.Electrodynamics;
import electrodynamics.api.screen.ITexture;
import electrodynamics.prefab.screen.component.ScreenComponentGeneric;
import electrodynamics.prefab.utilities.math.Color;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.util.StringUtil;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
/**
 * A modified variant of the EditBox class integrated into the Electrodynamics render system and fixing certain issues with the vanilla-provided class
 * 
 * That's a spicy copy-pasta
 * 
 * @author skip999
 */
public class ScreenComponentEditBox extends ScreenComponentGeneric {

	public static final ResourceLocation TEXTURE = Electrodynamics.rl("textures/screen/component/textinputbar.png");

	public static final char[] POSITIVE_DECIMAL = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.' };
	public static final char[] DECIMAL = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.', '-' };

	public static final char[] POSITIVE_INTEGER = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
	public static final char[] INTEGER = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '-' };

	public static final int BACKWARDS = -1;
	public static final int FORWARDS = 1;
	public static final Color DEFAULT_TEXT_COLOR = new Color(224, 224, 224, 0);
	private final Font font;
	/** Has the current text being edited on the textbox. */
	private String value = "";
	private int maxLength = 32;
	private int frame;
	/** if true the textbox can lose focus by clicking elsewhere on the screen */
	private boolean canLoseFocus = true;
	/** If this value is true along with isFocused, keyTyped will process the keys. */
	private boolean isEditable = true;
	private boolean shiftPressed;
	/** The current character index that should be used as start of the rendered text. */
	private int displayPos;
	private int cursorPos;
	/** other selection position, maybe the same as the cursor */
	private int highlightPos;
	private Color textColor = DEFAULT_TEXT_COLOR;
	private Color textColorUneditable = new Color(112, 112, 112, 0);
	@Nullable
	private String suggestion;
	@Nullable
	private Consumer<String> responder;
	/** Called to check if the text is valid */
	private Predicate<String> filter = Objects::nonNull;
	private BiFunction<String, Integer, FormattedCharSequence> formatter = (p_94147_, p_94148_) -> FormattedCharSequence.forward(p_94147_, Style.EMPTY);

	public ScreenComponentEditBox(int x, int y, int width, int height, Font font) {
		super(x, y, width, height);
		texture = TextInputTextures.TEXT_INPUT_BASE;
		this.font = font;
	}

	public ScreenComponentEditBox setResponder(Consumer<String> responder) {
		this.responder = responder;
		return this;
	}

	public ScreenComponentEditBox setFormatter(BiFunction<String, Integer, FormattedCharSequence> textFormatter) {
		this.formatter = textFormatter;
		return this;
	}

	/**
	 * Sets the text of the textbox, and moves the cursor to the end.
	 */
	public ScreenComponentEditBox setValue(String text) {
		if (this.filter.test(text)) {
			if (text.length() > this.maxLength) {
				this.value = text.substring(0, this.maxLength);
			} else {
				this.value = text;
			}

			this.moveCursorToEnd();
			this.setHighlightPos(this.cursorPos);
			this.onValueChange(text);
		}
		return this;
	}

	/**
	 * Returns the contents of the textbox
	 */
	public String getValue() {
		return this.value;
	}

	/**
	 * Returns the text between the cursor and selectionEnd.
	 */
	public String getHighlighted() {
		int min = Math.min(this.cursorPos, this.highlightPos);
		int max = Math.max(this.cursorPos, this.highlightPos);
		return this.value.substring(min, max);
	}

	public ScreenComponentEditBox setFilter(Predicate<String> validator) {
		this.filter = validator;
		return this;
	}

	public ScreenComponentEditBox setFilter(char[] validChars) {
		return setFilter(getValidator(validChars));
	}

	/**
	 * Adds the given text after the cursor, or replaces the currently selected text if there is a selection.
	 */
	public void insertText(String textToWrite) {
		int min = Math.min(this.cursorPos, this.highlightPos);
		int max = Math.max(this.cursorPos, this.highlightPos);
		int length = this.maxLength - this.value.length() - (min - max);
		String filtered = StringUtil.filterText(textToWrite);
		int filteredLength = filtered.length();
		if (length < filteredLength) {
			filtered = filtered.substring(0, length);
			filteredLength = length;
		}

		String updated = (new StringBuilder(this.value)).replace(min, max, filtered).toString();
		if (this.filter.test(updated)) {
			this.value = updated;
			this.setCursorPosition(min + filteredLength);
			this.setHighlightPos(this.cursorPos);
			this.onValueChange(this.value);
		}
	}

	private void onValueChange(String newText) {
		if (this.responder != null) {
			this.responder.accept(newText);
		}

	}

	private void deleteText(int count) {
		if (Screen.hasControlDown()) {
			this.deleteWords(count);
		} else {
			this.deleteChars(count);
		}

	}

	/**
	 * Deletes the given number of words from the current cursor's position, unless there is currently a selection, in which case the selection is deleted instead.
	 */
	public void deleteWords(int num) {
		if (!this.value.isEmpty()) {
			if (this.highlightPos != this.cursorPos) {
				this.insertText("");
			} else {
				this.deleteChars(this.getWordPosition(num) - this.cursorPos);
			}
		}
	}

	/**
	 * Deletes the given number of characters from the current cursor's position, unless there is currently a selection, in which case the selection is deleted instead.
	 */
	public void deleteChars(int num) {
		if (!this.value.isEmpty()) {
			if (this.highlightPos != this.cursorPos) {
				this.insertText("");
			} else {
				int cursorPos = this.getCursorPos(num);
				int min = Math.min(cursorPos, this.cursorPos);
				int max = Math.max(cursorPos, this.cursorPos);
				if (min != max) {
					String updated = (new StringBuilder(this.value)).delete(min, max).toString();
					if (this.filter.test(updated)) {
						this.value = updated;
						this.moveCursorTo(min);
					}
				}
			}
		}
	}

	/**
	 * Gets the starting index of the word at the specified number of words away from the cursor position.
	 */
	public int getWordPosition(int numWords) {
		return this.getWordPosition(numWords, this.getCursorPosition());
	}

	/**
	 * Gets the starting index of the word at a distance of the specified number of words away from the given position.
	 */
	private int getWordPosition(int numOfWords, int position) {
		return this.getWordPosition(numOfWords, position, true);
	}

	/**
	 * Like getNthWordFromPos (which wraps this), but adds option for skipping consecutive spaces
	 */
	private int getWordPosition(int numOfWords, int position, boolean skipSpaces) {
		int originalPos = position;
		boolean noWords = numOfWords < 0;
		int absNoWords = Math.abs(numOfWords);

		for (int i = 0; i < absNoWords; ++i) {
			if (!noWords) {
				int lengthOfText = this.value.length();
				originalPos = this.value.indexOf(32, originalPos);
				if (originalPos == -1) {
					originalPos = lengthOfText;
				} else {
					while (skipSpaces && originalPos < lengthOfText && this.value.charAt(originalPos) == ' ') {
						++originalPos;
					}
				}
			} else {
				while (skipSpaces && originalPos > 0 && this.value.charAt(originalPos - 1) == ' ') {
					--originalPos;
				}

				while (originalPos > 0 && this.value.charAt(originalPos - 1) != ' ') {
					--originalPos;
				}
			}
		}

		return originalPos;
	}

	/**
	 * Moves the text cursor by a specified number of characters and clears the selection
	 */
	public void moveCursor(int delta) {
		this.moveCursorTo(this.getCursorPos(delta));
	}

	private int getCursorPos(int delta) {
		return Util.offsetByCodepoints(this.value, this.cursorPos, delta);
	}

	/**
	 * Sets the current position of the cursor.
	 */
	public void moveCursorTo(int pos) {
		this.setCursorPosition(pos);
		if (!this.shiftPressed) {
			this.setHighlightPos(this.cursorPos);
		}

		this.onValueChange(this.value);
	}

	public void setCursorPosition(int pos) {
		this.cursorPos = Mth.clamp(pos, 0, this.value.length());
	}

	/**
	 * Moves the cursor to the very start of this text box.
	 */
	public void moveCursorToStart() {
		this.moveCursorTo(0);
	}

	/**
	 * Moves the cursor to the very end of this text box.
	 */
	public void moveCursorToEnd() {
		this.moveCursorTo(this.value.length());
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		if (!this.canConsumeInput()) {
			return false;
		}
		this.shiftPressed = Screen.hasShiftDown();
		if (Screen.isSelectAll(keyCode)) {
			this.moveCursorToEnd();
			this.setHighlightPos(0);
			return true;
		}
		if (Screen.isCopy(keyCode)) {
			Minecraft.getInstance().keyboardHandler.setClipboard(this.getHighlighted());
			return true;
		}
		if (Screen.isPaste(keyCode)) {
			if (this.isEditable) {
				this.insertText(Minecraft.getInstance().keyboardHandler.getClipboard());
			}

			return true;
		}
		if (Screen.isCut(keyCode)) {
			Minecraft.getInstance().keyboardHandler.setClipboard(this.getHighlighted());
			if (this.isEditable) {
				this.insertText("");
			}

			return true;
		}
		switch (keyCode) {
		case 259:
			if (this.isEditable) {
				this.shiftPressed = false;
				this.deleteText(-1);
				this.shiftPressed = Screen.hasShiftDown();
			}

			return true;
		case 260:
		case 264:
		case 265:
		case 266:
		case 267:
		default:
			return false;
		case 261:
			if (this.isEditable) {
				this.shiftPressed = false;
				this.deleteText(1);
				this.shiftPressed = Screen.hasShiftDown();
			}

			return true;
		case 262:
			if (Screen.hasControlDown()) {
				this.moveCursorTo(this.getWordPosition(1));
			} else {
				this.moveCursor(1);
			}

			return true;
		case 263:
			if (Screen.hasControlDown()) {
				this.moveCursorTo(this.getWordPosition(-1));
			} else {
				this.moveCursor(-1);
			}

			return true;
		case 268:
			this.moveCursorToStart();
			return true;
		case 269:
			this.moveCursorToEnd();
			return true;
		}
	}

	public boolean canConsumeInput() {
		return this.isVisible() && this.isFocused() && this.isEditable();
	}

	@Override
	public boolean charTyped(char codePoint, int modifiers) {
		if (!this.canConsumeInput()) {
			return false;
		}
		if (!StringUtil.isAllowedChatCharacter(codePoint)) {
			return false;
		}
		if (this.isEditable) {
			this.insertText(Character.toString(codePoint));
		}

		return true;
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (!this.isVisible()) {
			return false;
		}
		boolean mouseOver = isMouseOver(mouseX, mouseY);
		if (this.canLoseFocus) {
			this.setFocus(mouseOver);
		}

		if (this.isFocused() && mouseOver && button == 0) {
			int exitBoxXPos = Mth.floor(mouseX) - this.xLocation - ((int) gui.getGuiWidth()) - 4;

			String text = this.font.plainSubstrByWidth(this.value.substring(this.displayPos), this.getInnerWidth());
			this.moveCursorTo(this.font.plainSubstrByWidth(text, exitBoxXPos).length() + this.displayPos);
			return true;
		}
		return false;
	}

	/**
	 * Sets focus to this gui element
	 */
	public ScreenComponentEditBox setFocus(boolean isFocused) {
		this.setFocused(isFocused);
		return this;
	}

	/**
	 * Increments the cursor counter
	 */
	public void tick() {
		++this.frame;
	}

	@Override
	public void renderBackground(GuiGraphics graphics, int xAxis, int yAxis, int guiWidth, int guiHeight) {

		drawExpandedBox(graphics, texture.getLocation(), this.xLocation + guiWidth, this.yLocation + guiHeight, width, height);

		Color textColor = this.isEditable ? this.textColor : this.textColorUneditable;
		int highlightedSize = this.cursorPos - this.displayPos;
		int highlightedLength = this.highlightPos - this.displayPos;

		String displayedText = this.font.plainSubstrByWidth(this.value.substring(this.displayPos), this.getInnerWidth());

		boolean isHighlightedValid = highlightedSize >= 0 && highlightedSize <= displayedText.length();
		boolean blinkCursor = this.isFocused() && this.frame / 6 % 2 == 0 && isHighlightedValid;

		int textStartX = this.xLocation + guiWidth + 4;
		int textStartY = this.yLocation + guiHeight + (this.height - 8) / 2;

		int textStartPre = textStartX;

		if (highlightedLength > displayedText.length()) {
			highlightedLength = displayedText.length();
		}

		if (!displayedText.isEmpty()) {
			String highlightedText = isHighlightedValid ? displayedText.substring(0, highlightedSize) : displayedText;
			textStartPre = graphics.drawString(font, this.formatter.apply(highlightedText, this.displayPos), textStartX, textStartY, textColor.color());
		}

		boolean isCursorPastLength = this.cursorPos < this.value.length() || this.value.length() >= this.getMaxLength();

		int textStartPreCopy = textStartPre;

		if (!isHighlightedValid) {
			textStartPreCopy = highlightedSize > 0 ? textStartX + this.width : textStartX;
		} else if (isCursorPastLength) {
			textStartPreCopy = textStartPre - 1;
			--textStartPre;
		}

		if (!displayedText.isEmpty() && isHighlightedValid && highlightedSize < displayedText.length()) {
			graphics.drawString(font, this.formatter.apply(displayedText.substring(highlightedSize), this.cursorPos), textStartPre, textStartY, textColor.color());
		}

		if (!isCursorPastLength && this.suggestion != null) {
			graphics.drawString(font, this.suggestion, textStartPreCopy - 1, textStartY, -8355712);
		}

		if (blinkCursor) {
			if (isCursorPastLength) {
				graphics.fill(RenderType.guiOverlay(), textStartPreCopy, textStartY - 1, textStartPreCopy + 1, textStartY + 1 + 9, -3092272);
			} else {
				graphics.drawString(font, "_", textStartPreCopy, textStartY, textColor.color());
			}
		}

		if (highlightedLength != highlightedSize) {
			int l1 = textStartX + this.font.width(displayedText.substring(0, highlightedLength));
			this.renderHighlight(graphics, textStartPreCopy, textStartY - 1, l1 - 1, textStartY + 1 + 9, guiWidth, guiHeight);
		}

	}

	/**
	 * Draws the blue selection box.
	 */
	private void renderHighlight(GuiGraphics graphics, int pMinX, int pMinY, int pMaxX, int pMaxY, int guiWidth, int guiHeight) {
		if (pMinX < pMaxX) {
			int i = pMinX;
			pMinX = pMaxX;
			pMaxX = i;
		}

		if (pMinY < pMaxY) {
			int j = pMinY;
			pMinY = pMaxY;
			pMaxY = j;
		}

		if (pMaxX > this.xLocation + this.width + guiWidth) {
			pMaxX = this.yLocation + this.width;
		}

		if (pMinX > this.xLocation + this.width + guiWidth) {
			pMinX = this.xLocation + this.width;
		}

		graphics.fill(RenderType.guiTextHighlight(), pMinX, pMinY, pMaxX, pMaxY, -16776961);
	}

	/**
	 * Sets the maximum length for the text in this text box. If the current text is longer than this length, the current text will be trimmed.
	 */
	public ScreenComponentEditBox setMaxLength(int length) {
		this.maxLength = length;
		if (this.value.length() > length) {
			this.value = this.value.substring(0, length);
			this.onValueChange(this.value);
		}
		return this;

	}

	/**
	 * Returns the maximum number of character that can be contained in this textbox.
	 */
	public int getMaxLength() {
		return this.maxLength;
	}

	/**
	 * Returns the current position of the cursor.
	 */
	public int getCursorPosition() {
		return this.cursorPos;
	}

	/**
	 * Sets the color to use when drawing this text box's text. A different color is used if this text box is disabled.
	 */
	public ScreenComponentEditBox setTextColor(Color pColor) {
		this.textColor = pColor;
		return this;
	}

	/**
	 * Sets the color to use for text in this text box when this text box is disabled.
	 */
	public ScreenComponentEditBox setTextColorUneditable(Color pColor) {
		this.textColorUneditable = pColor;
		return this;
	}

	protected void onFocusedChanged(boolean pFocused) {
		if (pFocused) {
			this.frame = 0;
		}
	}

	private boolean isEditable() {
		return this.isEditable;
	}

	/**
	 * Sets whether this text box is enabled. Disabled text boxes cannot be typed in.
	 */
	public void setEditable(boolean pEnabled) {
		this.isEditable = pEnabled;
	}

	/**
	 * Returns the width of the textbox depending on if background drawing is enabled.
	 */
	public int getInnerWidth() {
		return this.width - 8;
	}

	/**
	 * Sets the position of the selection anchor (the selection anchor and the cursor position mark the edges of the selection). If the anchor is set beyond the bounds of the current text, it will be put back inside.
	 */
	public void setHighlightPos(int position) {
		int length = this.value.length();
		this.highlightPos = Mth.clamp(position, 0, length);
		if (this.font != null) {
			if (this.displayPos > length) {
				this.displayPos = length;
			}

			int innerWidth = this.getInnerWidth();

			String text = this.font.plainSubstrByWidth(this.value.substring(this.displayPos), innerWidth);

			int textStartX = text.length() + this.displayPos;
			if (this.highlightPos == this.displayPos) {
				this.displayPos -= this.font.plainSubstrByWidth(this.value, innerWidth, true).length();
			}

			if (this.highlightPos > textStartX) {
				this.displayPos += this.highlightPos - textStartX;
			} else if (this.highlightPos <= this.displayPos) {
				this.displayPos -= this.displayPos - this.highlightPos;
			}

			this.displayPos = Mth.clamp(this.displayPos, 0, length);
		}

	}

	/**
	 * Sets whether this text box loses focus when something other than it is clicked.
	 */
	public void setCanLoseFocus(boolean canLoseFocus) {
		this.canLoseFocus = canLoseFocus;
	}

	public void setSuggestion(@Nullable String suggestion) {
		this.suggestion = suggestion;
	}

	public int getScreenX(int charNum) {
		return (int) (charNum > this.value.length() ? this.xLocation + gui.getGuiWidth() : this.xLocation + gui.getGuiWidth() + this.font.width(this.value.substring(0, charNum)));
	}

	public void setX(int xPos) {
		this.xLocation = xPos;
	}

	public static void drawExpandedBox(GuiGraphics graphics, ResourceLocation texture, int x, int y, int boxWidth, int boxHeight) {
		if (boxWidth < 18) {
			if (boxHeight < 18) {
				graphics.blit(texture, x, y, boxWidth, boxHeight, 0, 0, boxWidth, boxHeight, boxWidth, boxHeight);
			} else {
				graphics.blit(texture, x, y, boxWidth, 7, 0, 0, boxWidth, 7, boxWidth, 18);

				int sectionHeight = boxHeight - 14;
				int heightIterations = sectionHeight / 4;
				int remainderHeight = sectionHeight % 4;

				int heightOffset = 7;
				for (int i = 0; i < heightIterations; i++) {
					graphics.blit(texture, x, y + heightOffset, boxWidth, 4, 0, 7, boxWidth, 4, boxWidth, 18);
					heightOffset += 4;
				}
				graphics.blit(texture, x, y + heightOffset, boxWidth, remainderHeight, 0, 7, boxWidth, remainderHeight, boxWidth, 18);

				graphics.blit(texture, x, y + boxHeight - 7, boxWidth, 7, 0, 11, boxWidth, 7, boxWidth, 18);
			}
		} else if (boxHeight < 18) {
			graphics.blit(texture, x, y, 7, boxHeight, 0, 0, 7, boxHeight, 18, boxHeight);

			int sectionWidth = boxWidth - 14;
			int widthIterations = sectionWidth / 4;
			int remainderWidth = sectionWidth % 4;

			int widthOffset = 7;
			for (int i = 0; i < widthIterations; i++) {
				graphics.blit(texture, x + widthOffset, y, 4, boxHeight, 7, 0, 4, boxHeight, 18, boxHeight);
				widthOffset += 4;
			}
			graphics.blit(texture, x + widthOffset, y, remainderWidth, boxHeight, 7, 0, remainderWidth, boxHeight, 18, boxHeight);

			graphics.blit(texture, x + boxWidth - 7, y, 7, boxHeight, 11, 0, 7, boxHeight, 18, boxHeight);
		} else {
			// the button is >= 18x18 at this point

			// draw squares
			int squareWidth = boxWidth - 10;
			int squareWidthIterations = squareWidth / 8;
			int remainderSquareWidth = squareWidth % 8;

			int squareHeight = boxHeight - 10;
			int squareHeightIterations = squareHeight / 8;
			int remainderSquareHeight = squareHeight % 8;

			int heightOffset = 5;
			int widthOffset = 5;

			for (int i = 0; i <= squareHeightIterations; i++) {
				int height = i == squareHeightIterations ? remainderSquareHeight : 8;
				for (int j = 0; j < squareWidthIterations; j++) {
					draw(graphics, texture, x, y, widthOffset, heightOffset, 5, 5, 8, height);
					widthOffset += 8;
				}
				draw(graphics, texture, x, y, widthOffset, heightOffset, 5, 5, remainderSquareWidth, height);
				widthOffset = 5;
				heightOffset += 8;
			}

			// draw tl corner
			draw(graphics, texture, x, y, 0, 0, 0, 0, 8, 8);

			// draw top strip

			int stripWidth = boxWidth - 14;
			int stripWidthIterations = stripWidth / 4;
			int remainderStripWidth = stripWidth % 4;

			int stripHeight = boxHeight - 14;
			int stripHeightIterations = stripHeight / 4;
			int remainderStripHeight = stripHeight % 4;

			widthOffset = 7;
			for (int i = 0; i < stripWidthIterations; i++) {
				draw(graphics, texture, x, y, widthOffset, 0, 7, 0, 4, 5);
				widthOffset += 4;
			}
			draw(graphics, texture, x, y, widthOffset, 0, 7, 0, remainderStripWidth, 5);

			// draw tr corner
			draw(graphics, texture, x, y, boxWidth - 8, 0, 10, 0, 8, 8);

			// draw left strip
			heightOffset = 7;
			for (int i = 0; i < stripHeightIterations; i++) {
				draw(graphics, texture, x, y, 0, heightOffset, 0, 7, 5, 4);
				heightOffset += 4;
			}
			draw(graphics, texture, x, y, 0, heightOffset, 0, 5, 5, remainderStripHeight);

			// draw right strip
			heightOffset = 7;
			widthOffset = boxWidth - 5;
			for (int i = 0; i < stripHeightIterations; i++) {
				draw(graphics, texture, x, y, widthOffset, heightOffset, 13, 7, 5, 4);
				heightOffset += 4;
			}
			draw(graphics, texture, x, y, widthOffset, heightOffset, 13, 7, 5, remainderStripHeight);

			// draw bl corner
			draw(graphics, texture, x, y, 0, boxHeight - 8, 0, 10, 8, 8);

			// draw bottom strip
			heightOffset = boxHeight - 5;
			widthOffset = 7;
			for (int i = 0; i < stripWidthIterations; i++) {
				draw(graphics, texture, x, y, widthOffset, heightOffset, 7, 13, 4, 5);
				widthOffset += 4;
			}
			draw(graphics, texture, x, y, widthOffset, heightOffset, 7, 13, remainderStripWidth, 5);

			// draw br corner
			draw(graphics, texture, x, y, boxWidth - 8, boxHeight - 8, 10, 10, 8, 8);

		}

	}

	private static void draw(GuiGraphics graphics, ResourceLocation texture, int x, int y, int widthOffset, int heightOffset, int textXOffset, int textYOffset, int width, int height) {
		graphics.blit(texture, x + widthOffset, y + heightOffset, width, height, textXOffset, textYOffset, width, height, 18, 18);
	}

	public static Predicate<String> getValidator(char[] validChars) {
		return string -> {

			if (string.isEmpty()) {
				return true;
			}

			boolean flag = false;

			for (char character : string.toCharArray()) {
				for (char valid : validChars) {
					if (valid == character) {
						flag = true;
						break;
					}
				}
				if (!flag) {
					return false;
				}
				flag = false;
			}
			return true;
		};
	}

	public enum TextInputTextures implements ITexture {
		TEXT_INPUT_BASE(0, 0, 0, 0, 16, 16, TEXTURE);

		private final int textureWidth;
		private final int textureHeight;
		private final int textureU;
		private final int textureV;
		private final int imageWidth;
		private final int imageHeight;
		private final ResourceLocation loc;

		TextInputTextures(int textureWidth, int textureHeight, int textureU, int textureV, int imageWidth, int imageHeight, ResourceLocation loc) {
			this.textureWidth = textureWidth;
			this.textureHeight = textureHeight;
			this.textureU = textureU;
			this.textureV = textureV;
			this.imageWidth = imageWidth;
			this.imageHeight = imageHeight;
			this.loc = loc;
		}

		@Override
		public ResourceLocation getLocation() {
			return loc;
		}

		@Override
		public int imageHeight() {
			return imageHeight;
		}

		@Override
		public int imageWidth() {
			return imageWidth;
		}

		@Override
		public int textureHeight() {
			return textureHeight;
		}

		@Override
		public int textureU() {
			return textureU;
		}

		@Override
		public int textureV() {
			return textureV;
		}

		@Override
		public int textureWidth() {
			return textureWidth;
		}

	}
}