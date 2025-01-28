package electrodynamics.prefab.screen.component.types;

import java.util.function.Supplier;

import electrodynamics.common.packet.types.server.PacketUpdateCarriedItemServer;
import electrodynamics.prefab.inventory.container.types.GenericContainerBlockEntity;
import electrodynamics.prefab.properties.Property;
import electrodynamics.prefab.screen.GenericScreen;
import electrodynamics.prefab.screen.component.ScreenComponentGeneric;
import electrodynamics.prefab.screen.component.types.ScreenComponentSlot.IconType;
import electrodynamics.prefab.tile.GenericTile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import net.neoforged.neoforge.network.PacketDistributor;

public class ScreenComponentCondensedFluid extends ScreenComponentGeneric {

    private final Supplier<Property<FluidStack>> fluidPropertySupplier;

    public ScreenComponentCondensedFluid(Supplier<Property<FluidStack>> fluidStackSupplier, int x, int y) {
        super(IconType.FLUID_DARK, x, y);
        this.fluidPropertySupplier = fluidStackSupplier;
    }

    @Override
    public void renderBackground(GuiGraphics graphics, int xAxis, int yAxis, int guiWidth, int guiHeight) {

        super.renderBackground(graphics, xAxis, yAxis, guiWidth, guiHeight);

        Property<FluidStack> fluidProperty = fluidPropertySupplier.get();

        if (fluidProperty == null || fluidProperty.get().isEmpty()) {
            return;
        }

        IconType fluidFull = IconType.FLUID_BLUE;

        graphics.blit(fluidFull.getLocation(), guiWidth + xLocation + 1, guiHeight + yLocation + 1, fluidFull.textureU(), fluidFull.textureV(), fluidFull.textureWidth(), fluidFull.textureHeight(), fluidFull.imageWidth(), fluidFull.imageHeight());
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (isActiveAndVisible() && isValidClick(button) && isInClickRegion(mouseX, mouseY)) {

            onMouseClick(mouseX, mouseY);

            return true;
        }
        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (isValidClick(button)) {
            onMouseRelease(mouseX, mouseY);
            return true;
        }
        return false;
    }

    @Override
    public void onMouseClick(double mouseX, double mouseY) {

        Property<FluidStack> fluidProperty = fluidPropertySupplier.get();

        if (fluidProperty == null || fluidProperty.get().isEmpty()) {
            return;
        }

        FluidStack fluidStack = fluidProperty.get();

        GenericScreen<?> screen = (GenericScreen<?>) gui;

        GenericTile owner = (GenericTile) ((GenericContainerBlockEntity<?>) screen.getMenu()).getSafeHost();

        if (owner == null) {
            return;
        }

        ItemStack stack = screen.getMenu().getCarried();

        IFluidHandlerItem handler = stack.getCapability(Capabilities.FluidHandler.ITEM);

        if (handler == null) {
            return;
        }

        int taken = handler.fill(fluidStack, FluidAction.EXECUTE);

        if (taken <= 0) {
            return;
        }

        fluidStack.shrink(taken);

        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.BUCKET_FILL, 1.0F));

        stack = handler.getContainer();

        PacketDistributor.sendToServer(new PacketUpdateCarriedItemServer(stack.copy(), owner.getBlockPos(), Minecraft.getInstance().player.getUUID()));

    }

}
