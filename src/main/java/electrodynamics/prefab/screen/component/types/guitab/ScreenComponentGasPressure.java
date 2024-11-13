package electrodynamics.prefab.screen.component.types.guitab;

import java.util.ArrayList;
import java.util.List;

import electrodynamics.api.electricity.formatting.ChatFormatter;
import electrodynamics.api.electricity.formatting.DisplayUnit;
import electrodynamics.api.gas.PropertyGasTank;
import electrodynamics.api.screen.component.TextPropertySupplier;
import electrodynamics.prefab.inventory.container.types.GenericContainerBlockEntity;
import electrodynamics.prefab.screen.GenericScreen;
import electrodynamics.prefab.screen.component.types.ScreenComponentSlot.IconType;
import electrodynamics.prefab.screen.component.utils.AbstractScreenComponentInfo;
import electrodynamics.prefab.tile.GenericTile;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.tile.components.utils.IComponentGasHandler;
import electrodynamics.prefab.utilities.ElectroTextUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.util.FormattedCharSequence;

public class ScreenComponentGasPressure extends ScreenComponentGuiTab {

	public ScreenComponentGasPressure(TextPropertySupplier infoHandler, int x, int y) {
		super(GuiInfoTabTextures.REGULAR, IconType.PRESSURE_GAUGE, infoHandler, x, y);
	}

	public ScreenComponentGasPressure(int x, int y) {
		super(GuiInfoTabTextures.REGULAR, IconType.PRESSURE_GAUGE, AbstractScreenComponentInfo.EMPTY, x, y);
	}

	@Override
	protected List<? extends FormattedCharSequence> getInfo(List<? extends FormattedCharSequence> list) {
		if (infoHandler == EMPTY) {
			return getMaxPressureInfo();
		}
		return super.getInfo(list);
	}

	private List<? extends FormattedCharSequence> getMaxPressureInfo() {

		List<FormattedCharSequence> tooltips = new ArrayList<>();

		GenericTile generic = (GenericTile) ((GenericContainerBlockEntity<?>) ((GenericScreen<?>) gui).getMenu()).getSafeHost();

		if (generic == null) {
			return tooltips;
		}

		IComponentGasHandler handler = generic.getComponent(IComponentType.GasHandler);

		int index = 1;

		for (PropertyGasTank tank : handler.getInputTanks()) {

			tooltips.add(ElectroTextUtils.tooltip("tankmaxin", index, ChatFormatter.getChatDisplayShort(tank.getMaxPressure(), DisplayUnit.PRESSURE_ATM)).withStyle(ChatFormatting.GRAY).getVisualOrderText());

			index++;
		}

		index = 1;

		for (PropertyGasTank tank : handler.getOutputTanks()) {

			tooltips.add(ElectroTextUtils.tooltip("tankmaxout", index, ChatFormatter.getChatDisplayShort(tank.getMaxPressure(), DisplayUnit.PRESSURE_ATM)).withStyle(ChatFormatting.DARK_GRAY).getVisualOrderText());

			index++;

		}

		return tooltips;

	}

}
