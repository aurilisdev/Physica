package electrodynamics.client.screen.item;

import java.util.ArrayList;
import java.util.List;

import electrodynamics.api.electricity.formatting.ChatFormatter;
import electrodynamics.api.electricity.formatting.DisplayUnit;
import electrodynamics.common.inventory.container.item.ContainerSeismicScanner;
import electrodynamics.common.item.gear.tools.electric.ItemSeismicScanner;
import electrodynamics.common.packet.types.server.PacketSeismicScanner;
import electrodynamics.prefab.inventory.container.slot.itemhandler.SlotItemHandlerGeneric;
import electrodynamics.prefab.screen.GenericScreen;
import electrodynamics.prefab.screen.component.button.ScreenComponentButton;
import electrodynamics.prefab.screen.component.types.ScreenComponentMultiLabel;
import electrodynamics.prefab.screen.component.types.ScreenComponentSimpleLabel;
import electrodynamics.prefab.screen.component.types.ScreenComponentSlot;
import electrodynamics.prefab.screen.component.types.guitab.ScreenComponentElectricInfo;
import electrodynamics.prefab.screen.component.types.guitab.ScreenComponentGuiTab;
import electrodynamics.prefab.screen.component.utils.AbstractScreenComponent;
import electrodynamics.prefab.screen.component.utils.AbstractScreenComponentInfo;
import electrodynamics.prefab.utilities.ElectroTextUtils;
import electrodynamics.prefab.utilities.RenderingUtils;
import electrodynamics.prefab.utilities.math.Color;
import electrodynamics.prefab.utilities.object.Location;
import electrodynamics.registers.ElectrodynamicsDataComponentTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.network.PacketDistributor;

public class ScreenSeismicScanner extends GenericScreen<ContainerSeismicScanner> {

    private List<AbstractScreenComponent> componentsToHide = new ArrayList<>();
    private List<AbstractScreenComponent> componentsToShow = new ArrayList<>();

    private final ScreenComponentButton<?> button;

    public ScreenSeismicScanner(ContainerSeismicScanner container, Inventory inv, Component titleIn) {
        super(container, inv, titleIn);

        inventoryLabelY += 60;
        imageHeight += 60;

        addComponent(button = (ScreenComponentButton<?>) new ScreenComponentButton<>(ScreenComponentGuiTab.GuiInfoTabTextures.REGULAR, -AbstractScreenComponentInfo.SIZE + 1, AbstractScreenComponentInfo.SIZE + 2).onTooltip((graphics, but, xAxis, yAxis) -> {
            ScreenComponentButton<?> button = (ScreenComponentButton<?>) but;
            List<Component> tooltips = new ArrayList<>();
            tooltips.add(ElectroTextUtils.tooltip("scannerpattern").withStyle(ChatFormatting.DARK_GRAY));
            if (!button.isPressed) {
                tooltips.add(ElectroTextUtils.tooltip("inventoryio.presstoshow").withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY));
            } else {
                tooltips.add(ElectroTextUtils.tooltip("inventoryio.presstohide").withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY));
            }

            graphics.renderComponentTooltip(getFontRenderer(), tooltips, xAxis, yAxis);
        }).setOnPress(but -> {

            ScreenComponentButton<?> button = but;

            componentsToHide.forEach(component -> {
                component.setActive(button.isPressed);
                component.setVisible(button.isPressed);
            });

            for (int i = 1; i < ItemSeismicScanner.SLOT_COUNT; i++) {
                ((SlotItemHandlerGeneric) getMenu().slots.get(i)).setActive(button.isPressed);
            }

            button.isPressed = !button.isPressed;

            componentsToShow.forEach(component -> {
                component.setActive(button.isPressed);
                component.setVisible(button.isPressed);
            });

            ((SlotItemHandlerGeneric) getMenu().slots.get(0)).setActive(button.isPressed);


        }).setIcon(ScreenComponentSlot.IconType.SONAR_PROFILE));


        addComponent(new ScreenComponentElectricInfo(this::getElectricInformation, -AbstractScreenComponentInfo.SIZE + 1, 2));
        componentsToShow.add(addComponent(new ScreenComponentSimpleLabel(15, 23, 10, Color.TEXT_GRAY, ElectroTextUtils.gui("seismicscanner.material"))));
        componentsToShow.add(addComponent(new ScreenComponentSimpleLabel(15, 63, 10, Color.TEXT_GRAY, ElectroTextUtils.gui("seismicscanner.pattern"))));
        componentsToShow.add(addComponent(new ScreenComponentSimpleLabel(20, 89, 10, Color.TEXT_GRAY, () -> getPatternName(menu.getOwnerItem()))));
        componentsToShow.add(addComponent(new ScreenComponentSimpleLabel(20, 99, 10, Color.TEXT_GRAY, () -> ElectroTextUtils.gui("seismicscanner.patternintegrity", getPatternDurability(menu.getOwnerItem())))));

        componentsToHide.add(addComponent(new ScreenComponentSimpleLabel(20, 80, 10, Color.TEXT_GRAY, ElectroTextUtils.gui("seismicscanner.dataheader"))));
        componentsToHide.add(addComponent(new ScreenComponentMultiLabel(0, 0, graphics -> {
            ItemStack ownerItem = menu.getOwnerItem();

            Location playerLoc = ownerItem.getOrDefault(ElectrodynamicsDataComponentTypes.LOCATION_1, new Location(0, 0, 0));
            Location blockLoc = ownerItem.getOrDefault(ElectrodynamicsDataComponentTypes.LOCATION_2, new Location(0, 0, 0));

            if (blockLoc.equals(playerLoc)) {
                graphics.drawString(font, ElectroTextUtils.gui("seismicscanner.xcoordna"), 30, 90, Color.TEXT_GRAY.color(), false);
                graphics.drawString(font, ElectroTextUtils.gui("seismicscanner.ycoordna"), 30, 100, Color.TEXT_GRAY.color(), false);
                graphics.drawString(font, ElectroTextUtils.gui("seismicscanner.zcoordna"), 30, 110, Color.TEXT_GRAY.color(), false);
            } else {
                graphics.drawString(font, ElectroTextUtils.gui("seismicscanner.xcoord", blockLoc.intX()), 30, 90, Color.TEXT_GRAY.color(), false);
                graphics.drawString(font, ElectroTextUtils.gui("seismicscanner.ycoord", blockLoc.intY()), 30, 100, Color.TEXT_GRAY.color(), false);
                graphics.drawString(font, ElectroTextUtils.gui("seismicscanner.zcoord", blockLoc.intZ()), 30, 110, Color.TEXT_GRAY.color(), false);
            }
        })));

        componentsToHide.add(addComponent(new ScreenComponentButton<>(15, 24, 120, 20).setLabel(ElectroTextUtils.gui("seismicscanner.performscan")).setOnPress(
                button -> {
                    ItemStack owner = menu.getOwnerItem();
                    if (owner.isEmpty()) {
                        return;
                    }
                    InteractionHand hand = menu.getHand();
                    if (hand == null) {
                        return;
                    }
                    PacketDistributor.sendToServer(new PacketSeismicScanner(inv.player.getUUID(), PacketSeismicScanner.Type.manualping, 0, hand.ordinal()));
                }
        )));
        componentsToHide.add(addComponent(new ScreenComponentButton<>(15, 46, 120, 20)
                //
                .setOnPress(button -> {
                    ItemStack owner = menu.getOwnerItem();
                    if (owner.isEmpty()) {
                        return;
                    }
                    InteractionHand hand = menu.getHand();
                    if (hand == null) {
                        return;
                    }
                    ItemSeismicScanner.ScannerMode mode = ItemSeismicScanner.ScannerMode.values()[owner.getOrDefault(ElectrodynamicsDataComponentTypes.ENUM, 0)];
                    if (mode == ItemSeismicScanner.ScannerMode.PASSIVE) {
                        mode = ItemSeismicScanner.ScannerMode.ACTIVE;
                    } else {
                        mode = ItemSeismicScanner.ScannerMode.PASSIVE;
                    }
                    PacketDistributor.sendToServer(new PacketSeismicScanner(inv.player.getUUID(), PacketSeismicScanner.Type.switchsonarmode, mode.ordinal(), hand.ordinal()));
                }).setLabel(() -> {
                    //
                    ItemStack owner = menu.getOwnerItem();
                    if (owner.isEmpty()) {
                        return Component.empty();
                    }
                    ItemSeismicScanner.ScannerMode mode = ItemSeismicScanner.ScannerMode.values()[owner.getOrDefault(ElectrodynamicsDataComponentTypes.ENUM, 0)];
                    return mode == ItemSeismicScanner.ScannerMode.PASSIVE ? ElectroTextUtils.gui("seismicscanner.scanpassive") : ElectroTextUtils.gui("seismicscanner.scanactive");
                })));

        componentsToShow.forEach(component -> {
            component.setActive(false);
            component.setVisible(false);
        });

    }

    private Component getPatternDurability(ItemStack owner) {
        if (owner.getOrDefault(ElectrodynamicsDataComponentTypes.BLOCK, Blocks.AIR) == Blocks.AIR) {
            return ElectroTextUtils.gui("seismicscanner.nopattern");
        } else {
            return ChatFormatter.getChatDisplayShort(owner.getOrDefault(ElectrodynamicsDataComponentTypes.PATTERN_INTEGRITY, 0.0) / ItemSeismicScanner.FULL_PATTERN * 100, DisplayUnit.PERCENTAGE);
        }
    }

    private Component getPatternName(ItemStack owner) {
        Block block = owner.getOrDefault(ElectrodynamicsDataComponentTypes.BLOCK, Blocks.AIR);
        if (block == Blocks.AIR) {
            return ElectroTextUtils.gui("seismicscanner.nopatternstored");
        } else {
            return block.getName();
        }
    }

    @Override
    public void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.renderBackground(graphics, mouseX, mouseY, partialTick);
        if (!button.isPressed) {
            return;
        }
        int x = (int) getGuiWidth() + 21;
        int y = (int) getGuiHeight() + 73;

        Block pattern = menu.getOwnerItem().getOrDefault(ElectrodynamicsDataComponentTypes.BLOCK, Blocks.AIR);

        if (pattern == Blocks.AIR) {
            ScreenComponentSlot.IconType icon = ScreenComponentSlot.IconType.CUBE_OUTLINE;
            graphics.blit(icon.getLocation(), x, y, icon.textureU(), icon.textureV(), icon.textureWidth(), icon.textureHeight(), icon.imageWidth(), icon.imageHeight());
        } else {
            RenderingUtils.renderItemScaled(graphics, new ItemStack(pattern).getItem(), x, y, 1.0F);
        }


    }

    @Override
    protected void initializeComponents() {
        super.initializeComponents();
        ((SlotItemHandlerGeneric) getMenu().slots.get(0)).setActive(false);
    }

    private List<? extends FormattedCharSequence> getElectricInformation() {
        ArrayList<FormattedCharSequence> list = new ArrayList<>();
        ItemStack ownerItem = menu.getOwnerItem();
        if (ownerItem.getItem() instanceof ItemSeismicScanner scanner) {
            list.add(ElectroTextUtils.gui("machine.usage", ChatFormatter.getChatDisplayShort(ItemSeismicScanner.JOULES_PER_SCAN / 20.0, DisplayUnit.WATT).withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.DARK_GRAY).getVisualOrderText());
            list.add(ElectroTextUtils.gui("machine.voltage", ChatFormatter.getChatDisplayShort(120, DisplayUnit.VOLTAGE).withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.DARK_GRAY).getVisualOrderText());
            list.add(ElectroTextUtils.gui("machine.stored", ChatFormatter.getChatDisplayShort(scanner.getJoulesStored(ownerItem), DisplayUnit.JOULES).withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.DARK_GRAY).getVisualOrderText());
        }
        return list;
    }

}
