package electrodynamics.common.fluid.types.liquid;

import electrodynamics.api.References;
import electrodynamics.common.fluid.FluidNonPlaceable;
import electrodynamics.common.fluid.types.SimpleWaterBasedFluidType;
import electrodynamics.registers.ElectrodynamicsItems;
import net.neoforged.neoforge.fluids.FluidType;

public class FluidPolyethylene extends FluidNonPlaceable {

	private final FluidType type;

	public FluidPolyethylene() {
		super(() -> ElectrodynamicsItems.ITEM_CANISTERREINFORCED);
		type = new SimpleWaterBasedFluidType(References.ID, "fluidpolyethylene", -376664948);
	}

	@Override
	public FluidType getFluidType() {
		return type;
	}
}
