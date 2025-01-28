package electrodynamics.client.screen.tile;

import electrodynamics.common.inventory.container.tile.ContainerChemicalReactor;
import electrodynamics.common.tile.machines.chemicalreactor.TileChemicalReactor;
import electrodynamics.prefab.screen.component.types.ScreenComponentCondensedFluid;
import electrodynamics.prefab.screen.component.types.ScreenComponentProgress;
import electrodynamics.prefab.screen.component.types.guitab.ScreenComponentElectricInfo;
import electrodynamics.prefab.screen.component.types.guitab.ScreenComponentGasPressure;
import electrodynamics.prefab.screen.component.types.guitab.ScreenComponentGasTemperature;
import electrodynamics.prefab.screen.component.types.wrapper.WrapperCyclableFluidGauge;
import electrodynamics.prefab.screen.component.types.wrapper.WrapperCyclableGasGauge;
import electrodynamics.prefab.screen.component.types.wrapper.WrapperInventoryIO;
import electrodynamics.prefab.screen.component.utils.AbstractScreenComponentInfo;
import electrodynamics.prefab.screen.types.GenericMaterialScreen;
import electrodynamics.prefab.tile.GenericTile;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.tile.components.type.ComponentProcessor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ScreenChemicalReactor extends GenericMaterialScreen<ContainerChemicalReactor> {
    public ScreenChemicalReactor(ContainerChemicalReactor container, Inventory inv, Component titleIn) {
        super(container, inv, titleIn);

        imageHeight += 35;
        inventoryLabelY += 35;

        addComponent(new ScreenComponentProgress(ScreenComponentProgress.ProgressBars.PROGRESS_ARROW_RIGHT, () -> {
            GenericTile furnace = container.getSafeHost();
            if (furnace != null) {
                ComponentProcessor processor = furnace.getComponent(IComponentType.Processor);
                if (processor.isActive()) {
                    return processor.operatingTicks.get() / processor.requiredTicks.get();
                }
            }
            return 0;
        }, 66, 52));

        WrapperCyclableFluidGauge fluidInput = new WrapperCyclableFluidGauge(6, 26, container, this, true);
        WrapperCyclableGasGauge gasInput = new WrapperCyclableGasGauge(26, 26, container, this, true);

        WrapperCyclableFluidGauge fluidOutput = new WrapperCyclableFluidGauge(112, 26, container, this, false);
        WrapperCyclableGasGauge gasOutput = new WrapperCyclableGasGauge(132, 26, container, this, false);

        new WrapperInventoryIO(this, -AbstractScreenComponentInfo.SIZE + 1, AbstractScreenComponentInfo.SIZE * 3 + 2, 75, 117, 8, 107)
                //
                .hideAdditional(show -> {
                    //
                    fluidInput.getComponents().forEach(component -> {
                        component.setActive(show);
                        component.setVisible(show);
                    });
                    //
                    gasInput.getComponents().forEach(component -> {
                        component.setActive(show);
                        component.setVisible(show);
                    });
                    //
                    fluidOutput.getComponents().forEach(component -> {
                        component.setActive(show);
                        component.setVisible(show);
                    });
                    //
                    gasOutput.getComponents().forEach(component -> {
                        component.setActive(show);
                        component.setVisible(show);
                    });
                    //
                });

        addComponent(new ScreenComponentGasTemperature(-AbstractScreenComponentInfo.SIZE + 1, 2 + AbstractScreenComponentInfo.SIZE * 2));
        addComponent(new ScreenComponentGasPressure(-AbstractScreenComponentInfo.SIZE + 1, 2 + AbstractScreenComponentInfo.SIZE));
        addComponent(new ScreenComponentElectricInfo(-AbstractScreenComponentInfo.SIZE + 1, 2));
        addComponent(new ScreenComponentCondensedFluid(() -> {
            TileChemicalReactor electric = container.getSafeHost();
            if (electric == null) {
                return null;
            }

            return electric.condensedFluidFromGas;

        }, 153, 84));
    }
}
