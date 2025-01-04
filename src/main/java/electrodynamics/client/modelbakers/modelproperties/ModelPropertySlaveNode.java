package electrodynamics.client.modelbakers.modelproperties;

import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.data.ModelProperty;

public class ModelPropertySlaveNode extends ModelProperty<ModelPropertySlaveNode.SlaveNodeWrapper> {

    public static final ModelPropertySlaveNode INSTANCE = new ModelPropertySlaveNode();
    public static final record SlaveNodeWrapper(ResourceLocation id, Direction facing) {

    }
}
