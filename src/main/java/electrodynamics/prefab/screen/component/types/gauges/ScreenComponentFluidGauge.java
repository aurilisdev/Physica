package electrodynamics.prefab.screen.component.types.gauges;

import java.util.ArrayList;
import java.util.List;

import electrodynamics.api.electricity.formatting.ChatFormatter;
import electrodynamics.api.fluid.PropertyFluidTank;
import electrodynamics.api.screen.component.FluidTankSupplier;
import electrodynamics.common.packet.types.server.PacketUpdateCarriedItemServer;
import electrodynamics.prefab.inventory.container.types.GenericContainerBlockEntity;
import electrodynamics.prefab.screen.GenericScreen;
import electrodynamics.prefab.tile.GenericTile;
import electrodynamics.prefab.utilities.ElectroTextUtils;
import electrodynamics.prefab.utilities.RenderingUtils;
import electrodynamics.prefab.utilities.math.Color;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.IFluidTank;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import net.neoforged.neoforge.network.PacketDistributor;

@OnlyIn(Dist.CLIENT)
public class ScreenComponentFluidGauge extends AbstractScreenComponentGauge {
	public FluidTankSupplier fluidInfoHandler;

	public ScreenComponentFluidGauge(FluidTankSupplier fluidInfoHandler, int x, int y) {
		super(x, y);
		this.fluidInfoHandler = fluidInfoHandler;
	}

	@Override
	protected int getScaledLevel() {
		IFluidTank tank = fluidInfoHandler.getTank();
		if (tank != null) {
			if (tank.getFluidAmount() > 0 && tank.getCapacity() > 0) {
				return tank.getFluidAmount() * (GaugeTextures.BACKGROUND_DEFAULT.textureHeight() - 2) / tank.getCapacity();
			}
		}

		return 0;
	}

	@Override
	protected void applyColor() {
		IFluidTank tank = fluidInfoHandler.getTank();
		if (tank != null) {
			FluidStack fluidStack = tank.getFluid();
			if (!fluidStack.isEmpty()) {
				IClientFluidTypeExtensions extensions = IClientFluidTypeExtensions.of(fluidStack.getFluid());
				RenderingUtils.setShaderColor(new Color(extensions.getTintColor(fluidStack)));
			}
		}
	}

	@Override
	protected ResourceLocation getTexture() {
		IFluidTank tank = fluidInfoHandler.getTank();
		if (tank != null) {
			FluidStack fluidStack = tank.getFluid();
			IClientFluidTypeExtensions extensions = IClientFluidTypeExtensions.of(fluidStack.getFluid());
			return extensions.getStillTexture();
		}
		return texture.getLocation();
	}

	@Override
	protected List<? extends FormattedCharSequence> getTooltips() {
		List<FormattedCharSequence> tooltips = new ArrayList<>();
		IFluidTank tank = fluidInfoHandler.getTank();
		if (tank != null) {
			FluidStack fluidStack = tank.getFluid();
			if (fluidStack.getAmount() > 0) {
				tooltips.add(Component.translatable(fluidStack.getTranslationKey()).getVisualOrderText());
				tooltips.add(ElectroTextUtils.ratio(ChatFormatter.formatFluidMilibuckets(tank.getFluidAmount()), ChatFormatter.formatFluidMilibuckets(tank.getCapacity())).withStyle(ChatFormatting.GRAY).getVisualOrderText());
			} else {
				tooltips.add(ElectroTextUtils.ratio(Component.literal("0"), ChatFormatter.formatFluidMilibuckets(tank.getCapacity())).withStyle(ChatFormatting.GRAY).getVisualOrderText());
			}
		}
		return tooltips;
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

	    	
		PropertyFluidTank tank = fluidInfoHandler.getTank() instanceof PropertyFluidTank x ? x : null;

		if (tank == null) {
			return;
		}

		GenericScreen<?> screen = (GenericScreen<?>) gui;

		GenericTile owner = (GenericTile) ((GenericContainerBlockEntity<?>) screen.getMenu()).getSafeHost();

		if (owner == null) {
			return;
		}

		ItemStack stack = screen.getMenu().getCarried();

		IFluidHandlerItem handler = stack.getCapability(Capabilities.FluidHandler.ITEM);

		if(handler == null) {
			return;
		}

		FluidStack drainedSourceFluid = tank.getFluid().copy();

		int taken = handler.fill(drainedSourceFluid, IFluidHandler.FluidAction.EXECUTE);

		//drain this fluid gauge if the amount taken was greater than zero
		if (taken > 0) {

			tank.drain(taken, IFluidHandler.FluidAction.EXECUTE);

			Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.BUCKET_FILL, 1.0F));

			stack = handler.getContainer();

			PacketDistributor.sendToServer(new PacketUpdateCarriedItemServer(stack.copy(), ((GenericContainerBlockEntity<?>) screen.getMenu()).getSafeHost().getBlockPos(), Minecraft.getInstance().player.getUUID()));

			return;

		}
		//we didn't drain the gauge, now we try to fill it

		for(int i = 0; i < handler.getTanks(); i++){
			drainedSourceFluid = handler.getFluidInTank(i);
			taken = tank.fill(drainedSourceFluid, IFluidHandler.FluidAction.EXECUTE);
			if(taken <= 0) {
				continue;
			}
			handler.drain(taken, IFluidHandler.FluidAction.EXECUTE);

			Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.BUCKET_EMPTY, 1.0F));

			stack = handler.getContainer();

			PacketDistributor.sendToServer(new PacketUpdateCarriedItemServer(stack.copy(), ((GenericContainerBlockEntity<?>) screen.getMenu()).getSafeHost().getBlockPos(), Minecraft.getInstance().player.getUUID()));

			return;
		}





	}
}