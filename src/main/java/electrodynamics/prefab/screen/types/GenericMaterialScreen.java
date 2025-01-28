package electrodynamics.prefab.screen.types;

import java.util.HashSet;
import java.util.Set;

import electrodynamics.prefab.inventory.container.GenericContainer;
import electrodynamics.prefab.screen.GenericScreen;
import electrodynamics.prefab.screen.component.types.gauges.ScreenComponentFluidGauge;
import electrodynamics.prefab.screen.component.types.gauges.ScreenComponentGasGauge;
import electrodynamics.prefab.screen.component.utils.AbstractScreenComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

/**
 * 
 * This is a simple addon class that allows for a clean integration for fluid and gas lookups with JEI
 * 
 * Note the tile does not need to be a GenericMaterialTile to use this class
 * 
 * @author skip999
 *
 * @param <T>
 */
public class GenericMaterialScreen<T extends GenericContainer> extends GenericScreen<T> {

	private Set<ScreenComponentFluidGauge> fluidGauges = new HashSet<>();
	private Set<ScreenComponentGasGauge> gasGauges = new HashSet<>();

	public GenericMaterialScreen(T container, Inventory inv, Component titleIn) {
		super(container, inv, titleIn);
	}

	@Override
	public AbstractScreenComponent addComponent(AbstractScreenComponent component) {
		super.addComponent(component);

		if (component instanceof ScreenComponentFluidGauge gauge) {
			fluidGauges.add(gauge);
		} else if (component instanceof ScreenComponentGasGauge gauge) {
			gasGauges.add(gauge);
		}
		return component;
	}

	public Set<ScreenComponentFluidGauge> getFluidGauges() {
		return fluidGauges;
	}

	public Set<ScreenComponentGasGauge> getGasGauges() {
		return gasGauges;
	}

}
