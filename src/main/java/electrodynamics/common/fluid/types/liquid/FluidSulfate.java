package electrodynamics.common.fluid.types.liquid;

import electrodynamics.api.References;
import electrodynamics.common.fluid.FluidNonPlaceable;
import electrodynamics.common.fluid.types.SimpleWaterBasedFluidType;
import electrodynamics.common.fluid.types.liquid.subtype.SubtypeSulfateFluid;
import electrodynamics.registers.ElectrodynamicsItems;
import net.neoforged.neoforge.fluids.FluidType;

public class FluidSulfate extends FluidNonPlaceable {

	public SubtypeSulfateFluid mineral;

	public FluidSulfate(SubtypeSulfateFluid mineral) {
		super(() -> ElectrodynamicsItems.ITEM_CANISTERREINFORCED);
		this.mineral = mineral;
		type = new SimpleWaterBasedFluidType(References.ID, "fluidsulfate" + mineral.name());
	}

	private final FluidType type;

	@Override
	public FluidType getFluidType() {
		return type;
	}
}
