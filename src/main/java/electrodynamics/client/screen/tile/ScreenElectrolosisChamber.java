package electrodynamics.client.screen.tile;

import java.util.ArrayList;
import java.util.List;

import electrodynamics.api.electricity.formatting.ChatFormatter;
import electrodynamics.api.electricity.formatting.DisplayUnit;
import electrodynamics.common.inventory.container.tile.ContainerElectrolosisChamber;
import electrodynamics.common.settings.Constants;
import electrodynamics.common.tile.machines.TileElectrolosisChamber;
import electrodynamics.prefab.screen.component.types.ScreenComponentMultiLabel;
import electrodynamics.prefab.screen.component.types.ScreenComponentProgress;
import electrodynamics.prefab.screen.component.types.gauges.ScreenComponentFluidGauge;
import electrodynamics.prefab.screen.component.types.guitab.ScreenComponentElectricInfo;
import electrodynamics.prefab.screen.component.utils.AbstractScreenComponentInfo;
import electrodynamics.prefab.screen.types.GenericMaterialScreen;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.tile.components.type.ComponentElectrodynamic;
import electrodynamics.prefab.tile.components.type.ComponentFluidHandlerMulti;
import electrodynamics.prefab.utilities.ElectroTextUtils;
import electrodynamics.prefab.utilities.math.Color;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Inventory;

public class ScreenElectrolosisChamber extends GenericMaterialScreen<ContainerElectrolosisChamber> {
    public ScreenElectrolosisChamber(ContainerElectrolosisChamber container, Inventory inv, Component titleIn) {
        super(container, inv, titleIn);

        addComponent(new ScreenComponentElectricInfo(this::getElectricInfo, -AbstractScreenComponentInfo.SIZE + 1, 2));

        addComponent(new ScreenComponentProgress(ScreenComponentProgress.ProgressBars.PROGRESS_ARROW_RIGHT_BIG, () -> {
            TileElectrolosisChamber chamber = container.getSafeHost();
            if(chamber == null || !chamber.isActive.get()) {
                return 0;
            }
            if(chamber.neededTicks.get() <= 0) {
                return 1;
            }

            return Math.min(1, chamber.operatingTicks.get() / chamber.neededTicks.get());
        }, 56, 31));

        addComponent(new ScreenComponentMultiLabel(0, 0, graphics -> {
            TileElectrolosisChamber chamber = container.getSafeHost();
            if(chamber == null) {
                return;
            }
            Component text = ElectroTextUtils.ratio(ChatFormatter.getChatDisplayShort((double) chamber.processAmount.get() / 1000.0, DisplayUnit.BUCKETS), ChatFormatter.getChatDisplayShort(chamber.neededTicks.get(), DisplayUnit.TIME_TICKS));
            int width = getFontRenderer().width(text);
            float scale = 1;
            if(width >  70) {
                scale = 70.0F / width;
                width = 70;
            }
            int diff = 70 - width;
            int half = diff / 2;
            int x = (int) Math.ceil((52 + half) / scale) + 1;
            int y = (int) Math.ceil(52.0F / scale);
            graphics.pose().pushPose();
            graphics.pose().scale(scale, scale, scale);
            graphics.drawString(getFontRenderer(), text, x, y, Color.TEXT_GRAY.color(), false);
            graphics.pose().popPose();
        }));

        addComponent(new ScreenComponentFluidGauge(() -> {
            TileElectrolosisChamber boiler = container.getSafeHost();
            if (boiler != null) {
                return boiler.<ComponentFluidHandlerMulti>getComponent(IComponentType.FluidHandler).getInputTanks()[0];
            }
            return null;
        }, 38, 18));
        addComponent(new ScreenComponentFluidGauge(() -> {
            TileElectrolosisChamber boiler = container.getSafeHost();
            if (boiler != null) {
                return boiler.<ComponentFluidHandlerMulti>getComponent(IComponentType.FluidHandler).getOutputTanks()[0];
            }
            return null;
        }, 124, 18));
    }

    private List<FormattedCharSequence> getElectricInfo() {
        List<FormattedCharSequence> list = new ArrayList<>();
        TileElectrolosisChamber chamber = menu.getSafeHost();
        if(chamber == null) {
            return list;
        }
        ComponentElectrodynamic el = chamber.getComponent(IComponentType.Electrodynamic);
        list.add(ElectroTextUtils.gui("machine.usage", ChatFormatter.getChatDisplayShort(Constants.ELECTROLOSIS_CHAMBER_TARGET_JOULES * 20, DisplayUnit.WATT).withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.DARK_GRAY).getVisualOrderText());
        list.add(ElectroTextUtils.gui("machine.voltage", ChatFormatter.getChatDisplayShort(el.getVoltage(), DisplayUnit.VOLTAGE).withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.DARK_GRAY).getVisualOrderText());
        list.add(ElectroTextUtils.tooltip("electrolosischamber.satisfaction", ChatFormatter.getChatDisplayShort(el.getJoulesStored() / Constants.ELECTROLOSIS_CHAMBER_TARGET_JOULES * 100.0, DisplayUnit.PERCENTAGE).withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.DARK_GRAY).getVisualOrderText());
        return list;
    }
}
