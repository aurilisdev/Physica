package electrodynamics.client.modelbakers.modelproperties;

import java.util.function.Supplier;

import electrodynamics.common.block.connect.util.EnumConnectType;
import net.neoforged.neoforge.client.model.data.ModelProperty;

public class ModelPropertyConnections extends ModelProperty<Supplier<EnumConnectType[]>> {

    public static final ModelPropertyConnections INSTANCE = new ModelPropertyConnections();

}
