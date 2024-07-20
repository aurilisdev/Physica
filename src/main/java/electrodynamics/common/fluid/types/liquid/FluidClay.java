package electrodynamics.common.fluid.types.liquid;

import electrodynamics.api.References;
import electrodynamics.common.fluid.FluidNonPlaceable;
import electrodynamics.common.fluid.types.SimpleWaterBasedFluidType;
import electrodynamics.registers.ElectrodynamicsItems;
import net.neoforged.neoforge.fluids.FluidType;

public class FluidClay extends FluidNonPlaceable {
	private final FluidType type;

	public FluidClay() {
		super(() -> ElectrodynamicsItems.ITEM_CANISTERREINFORCED);
		type = new SimpleWaterBasedFluidType(References.ID, "fluidclay");
	}

	@Override
	public FluidType getFluidType() {
		return type;
	}
}
