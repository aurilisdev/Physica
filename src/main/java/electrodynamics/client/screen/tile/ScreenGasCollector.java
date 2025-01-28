package electrodynamics.client.screen.tile;

import java.util.ArrayList;
import java.util.List;

import electrodynamics.api.electricity.formatting.ChatFormatter;
import electrodynamics.api.electricity.formatting.DisplayUnit;
import electrodynamics.api.gas.GasStack;
import electrodynamics.common.inventory.container.tile.ContainerGasCollector;
import electrodynamics.common.reloadlistener.GasCollectorChromoCardsRegister;
import electrodynamics.common.settings.Constants;
import electrodynamics.common.tile.pipelines.gas.TileGasCollector;
import electrodynamics.prefab.screen.component.types.ScreenComponentCondensedFluid;
import electrodynamics.prefab.screen.component.types.ScreenComponentProgress;
import electrodynamics.prefab.screen.component.types.gauges.ScreenComponentGasGauge;
import electrodynamics.prefab.screen.component.types.guitab.ScreenComponentElectricInfo;
import electrodynamics.prefab.screen.component.types.guitab.ScreenComponentGasPressure;
import electrodynamics.prefab.screen.component.types.guitab.ScreenComponentGasTemperature;
import electrodynamics.prefab.screen.component.utils.AbstractScreenComponentInfo;
import electrodynamics.prefab.screen.types.GenericMaterialScreen;
import electrodynamics.prefab.tile.GenericTile;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.tile.components.type.ComponentGasHandlerSimple;
import electrodynamics.prefab.tile.components.type.ComponentInventory;
import electrodynamics.prefab.tile.components.type.ComponentProcessor;
import electrodynamics.prefab.utilities.ElectroTextUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Inventory;

public class ScreenGasCollector extends GenericMaterialScreen<ContainerGasCollector> {
    public ScreenGasCollector(ContainerGasCollector container, Inventory inv, Component titleIn) {
        super(container, inv, titleIn);
        addComponent(new ScreenComponentGasGauge(() -> {
            TileGasCollector boiler = container.getSafeHost();
            if (boiler != null) {
                return boiler.<ComponentGasHandlerSimple>getComponent(IComponentType.GasHandler);
            }
            return null;
        }, 90, 18));
        addComponent(new ScreenComponentProgress(ScreenComponentProgress.ProgressBars.FAN, () -> {
            GenericTile furnace = container.getSafeHost();
            if (furnace != null) {
                ComponentProcessor processor = furnace.getComponent(IComponentType.Processor);
                if (processor.isActive()) {
                    return 1.0;
                }
            }
            return 0;
        }, 57, 34).onTooltip((graphics, component, xAxis, yAxis) -> {
            TileGasCollector boiler = container.getSafeHost();
            if (boiler == null) {
                return;
            }
            ComponentProcessor processor = boiler.getComponent(IComponentType.Processor);
            if(!processor.isActive()) {
                return;
            }
            ComponentInventory inventory = boiler.getComponent(IComponentType.Inventory);
            GasCollectorChromoCardsRegister.AtmosphericResult result = GasCollectorChromoCardsRegister.INSTANCE.getResult(inventory.getItem(TileGasCollector.CARD_SLOT).getItem());
            GasStack stack = result.stack();
            List<FormattedCharSequence> text = new ArrayList<>();
            text.add(stack.getGas().getDescription().copy().withStyle(ChatFormatting.GRAY).getVisualOrderText());
            text.add(ElectroTextUtils.ratio(ChatFormatter.getChatDisplayShort(stack.getAmount() / 1000.0, DisplayUnit.BUCKETS), DisplayUnit.TIME_TICKS.getSymbol()).withStyle(ChatFormatting.DARK_GRAY).getVisualOrderText());
            text.add(ChatFormatter.getChatDisplayShort(stack.getTemperature(), DisplayUnit.TEMPERATURE_KELVIN).withStyle(ChatFormatting.DARK_GRAY).getVisualOrderText());
            text.add(ChatFormatter.getChatDisplayShort(stack.getPressure(), DisplayUnit.PRESSURE_ATM).withStyle(ChatFormatting.DARK_GRAY).getVisualOrderText());
            graphics.renderTooltip(getFontRenderer(), text, xAxis, yAxis);
        }));
        addComponent(new ScreenComponentGasTemperature(-AbstractScreenComponentInfo.SIZE + 1, 2 + AbstractScreenComponentInfo.SIZE * 2));
        addComponent(new ScreenComponentGasPressure(-AbstractScreenComponentInfo.SIZE + 1, 2 + AbstractScreenComponentInfo.SIZE));
        addComponent(new ScreenComponentElectricInfo(-AbstractScreenComponentInfo.SIZE + 1, 2).wattage(Constants.GAS_COLLECTOR_USAGE_PER_TICK * 20));
        addComponent(new ScreenComponentCondensedFluid(() -> {
            TileGasCollector electric = container.getSafeHost();
            if (electric == null) {
                return null;
            }

            return electric.condensedFluidFromGas;

        }, 122, 20));
    }
}
