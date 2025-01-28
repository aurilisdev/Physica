package electrodynamics.prefab.screen;

import java.util.ArrayList;
import java.util.List;

import electrodynamics.Electrodynamics;
import electrodynamics.api.screen.IScreenWrapper;
import electrodynamics.api.screen.component.ISlotTexture;
import electrodynamics.prefab.inventory.container.GenericContainer;
import electrodynamics.prefab.screen.component.editbox.ScreenComponentEditBox;
import electrodynamics.prefab.screen.component.types.ScreenComponentSimpleLabel;
import electrodynamics.prefab.screen.component.types.ScreenComponentSlot;
import electrodynamics.prefab.screen.component.types.ScreenComponentSlot.IconType;
import electrodynamics.prefab.screen.component.types.ScreenComponentSlot.SlotType;
import electrodynamics.prefab.screen.component.utils.AbstractScreenComponent;
import electrodynamics.prefab.screen.component.utils.SlotTextureProvider;
import electrodynamics.prefab.utilities.math.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GenericScreen<T extends GenericContainer> extends AbstractContainerScreen<T> implements IScreenWrapper {

	protected ResourceLocation defaultResource = Electrodynamics.rl("textures/screen/component/base.png");
	private List<AbstractScreenComponent> components = new ArrayList<>();
	public List<ScreenComponentSlot> slots = new ArrayList<>();
	private List<ScreenComponentEditBox> editBoxes = new ArrayList<>();
	protected int playerInvOffset = 0;

	// Ability to manipulate labels
	public ScreenComponentSimpleLabel guiTitle;
	public ScreenComponentSimpleLabel playerInvLabel;

	public GenericScreen(T container, Inventory inv, Component title) {
		super(container, inv, title);
		initializeComponents();
	}

	protected void initializeComponents() {
		for (Slot slot : menu.slots) {
			ScreenComponentSlot component = createScreenSlot(slot);
			addComponent(component);
			slots.add(component);
		}
		addComponent(guiTitle = new ScreenComponentSimpleLabel(this.titleLabelX, this.titleLabelY, 10, Color.TEXT_GRAY, this.title));
		addComponent(playerInvLabel = new ScreenComponentSimpleLabel(this.inventoryLabelX, this.inventoryLabelY, 10, Color.TEXT_GRAY, this.playerInventoryTitle));
	}

	protected ScreenComponentSlot createScreenSlot(Slot slot) {
		if (slot instanceof SlotTextureProvider provider) {
			ISlotTexture texture = provider.getSlotType();
			return new ScreenComponentSlot(slot, texture, provider.getIconType(), slot.x + texture.xOffset(), slot.y + texture.yOffset());
		}
		return new ScreenComponentSlot(slot, SlotType.NORMAL, IconType.NONE, slot.x + SlotType.NORMAL.xOffset(), slot.y + SlotType.NORMAL.yOffset());
	}

	@Override
	protected void containerTick() {
		super.containerTick();
		for (ScreenComponentEditBox box : editBoxes) {
			box.tick();
		}
	}

	@Override
	protected void init() {
		super.init();
		guiTitle.xLocation = titleLabelX;
		guiTitle.yLocation = titleLabelY;
		playerInvLabel.xLocation = inventoryLabelX;
		playerInvLabel.yLocation = inventoryLabelY;
		for (AbstractScreenComponent component : components) {
			addRenderableWidget(component);
		}
	}

	@Override
	public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(graphics, mouseX, mouseY, partialTicks);
		super.render(graphics, mouseX, mouseY, partialTicks);
		renderTooltip(graphics, mouseX, mouseY);
	}

	@Override
	protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
		// super.renderLabels(graphics, mouseX, mouseY);
		int guiWidth = (int) getGuiWidth();
		int guiHeight = (int) getGuiHeight();
		int xAxis = mouseX - guiWidth;
		int yAxis = mouseY - guiHeight;
		for (AbstractScreenComponent component : components) {
			component.renderForeground(graphics, xAxis, yAxis, guiWidth, guiHeight);
		}
	}

	@Override
	protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
		// RenderingUtils.bindTexture(defaultResource);
		int guiWidth = (int) getGuiWidth();
		int guiHeight = (int) getGuiHeight();

		int y = guiHeight;

		graphics.blit(defaultResource, guiWidth, y, 0, 0, 176, 4, 176, 18);

		y += 4;

		int wholeHeight = (imageHeight - 8) / 10;
		int remainder = (imageHeight - 8) % 10;

		for(int i = 0; i < wholeHeight; i++){
			graphics.blit(defaultResource, guiWidth, y, 0, 4, 176, 10, 176, 18);
			y += 10;
		}

		graphics.blit(defaultResource, guiWidth, y, 0, 4, 176, remainder, 176, 18);
		y += remainder;

		graphics.blit(defaultResource, guiWidth, y, 0, 14, 176, 4, 176, 18);
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {

		for (AbstractScreenComponent component : components) {
			component.preOnMouseClick(mouseX, mouseY, button);
		}

		return super.mouseClicked(mouseX, mouseY, button);
	}

	@Override
	public Font getFontRenderer() {
		return Minecraft.getInstance().font;
	}

	@Override
	public void resize(Minecraft pMinecraft, int pWidth, int pHeight) {
		List<String> strings = new ArrayList<>();
		List<ScreenComponentEditBox> boxes = new ArrayList<>(editBoxes);
		for (ScreenComponentEditBox box : boxes) {
			strings.add(box.getValue());
		}
		super.resize(pMinecraft, pWidth, pHeight);
		for (int i = 0; i < boxes.size(); i++) {
			boxes.get(i).setValue(strings.get(i));
		}
	}

	@Override
	public double getGuiWidth() {
		return (width - imageWidth) / 2.0;
	}

	@Override
	public double getGuiHeight() {
		return (height - imageHeight) / 2.0;
	}

	public double getXAxis(double mouseX) {
		return mouseX - getGuiWidth();
	}

	public double getYAxis(double mouseY) {
		return mouseY - getGuiHeight();
	}

	public AbstractScreenComponent addComponent(AbstractScreenComponent component) {
		components.add(component);
		component.setScreen(this);
		return component;
	}

	public List<AbstractScreenComponent> getComponents() {
		return components;
	}

	public void addEditBox(ScreenComponentEditBox box) {
		editBoxes.add(box);
		addComponent(box);
	}

}