package electrodynamics.prefab.screen.component.types.gauges;

import java.util.function.Supplier;

import electrodynamics.api.capability.types.gas.IGasHandlerItem;
import electrodynamics.api.gas.GasAction;
import electrodynamics.api.gas.GasStack;
import electrodynamics.api.gas.PropertyGasTank;
import electrodynamics.api.gas.utils.IGasTank;
import electrodynamics.common.packet.types.server.PacketUpdateCarriedItemServer;
import electrodynamics.prefab.inventory.container.GenericContainerBlockEntity;
import electrodynamics.prefab.screen.GenericScreen;
import electrodynamics.prefab.tile.GenericTile;
import electrodynamics.registers.ElectrodynamicsCapabilities;
import electrodynamics.registers.ElectrodynamicsSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;

public class ScreenComponentGasGaugeInput extends ScreenComponentGasGauge {

    public ScreenComponentGasGaugeInput(Supplier<IGasTank> gasStack, int x, int y) {
        super(gasStack, x, y);
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

        PropertyGasTank tank = (PropertyGasTank) gasTank.get();

        if (tank == null) {
            return;
        }

        GenericScreen<?> screen = (GenericScreen<?>) gui;

        GenericTile owner = (GenericTile) ((GenericContainerBlockEntity<?>) screen.getMenu()).getHostFromIntArray();

        if (owner == null) {
            return;
        }

        ItemStack stack = screen.getMenu().getCarried();

        GasStack tankGas = tank.getGas().copy();

        IGasHandlerItem handler = stack.getCapability(ElectrodynamicsCapabilities.CAPABILITY_GASHANDLER_ITEM);

        if (handler == null) {
            return;
        }

        double taken = handler.fillTank(0, tankGas, GasAction.EXECUTE);

        if (taken <= 0) {
            return;
        }

        tank.drain(taken, GasAction.EXECUTE);

        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(ElectrodynamicsSounds.SOUND_PRESSURERELEASE.get(), 1.0F));

        tank.updateServer();

        stack = handler.getContainer();

        PacketDistributor.sendToServer(new PacketUpdateCarriedItemServer(stack.copy(), ((GenericContainerBlockEntity<?>) screen.getMenu()).getHostFromIntArray().getBlockPos(), Minecraft.getInstance().player.getUUID()));

    }

}
