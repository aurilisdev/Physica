package electrodynamics.datagen.utils.model;

import com.google.gson.JsonObject;

import electrodynamics.client.modelbakers.bakerytypes.CableModelLoader;
import electrodynamics.common.block.connect.util.EnumConnectType;
import net.neoforged.neoforge.client.model.generators.CustomLoaderBuilder;
import net.neoforged.neoforge.client.model.generators.ModelBuilder;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class WireModelBuilder<T extends ModelBuilder<T>> extends CustomLoaderBuilder<T> {

	public static <T extends ModelBuilder<T>> WireModelBuilder<T> begin(T parent, ExistingFileHelper existingFileHelper) {
		return new WireModelBuilder<>(parent, existingFileHelper);
	}

	private ModelFile none;
	private ModelFile wire;
	private ModelFile inventory;

	protected WireModelBuilder(T parent, ExistingFileHelper existingFileHelper) {
		super(CableModelLoader.ID, parent, existingFileHelper, false);

	}

	public WireModelBuilder<T> models(ModelFile none, ModelFile wire, ModelFile inventory) {
		this.none = none;
		this.wire = wire;
		this.inventory = inventory;

		return this;
	}

	@Override
	public JsonObject toJson(JsonObject json) {
		json = super.toJson(json);

		JsonObject noneElement = new JsonObject();
		noneElement.addProperty("parent", none.getLocation().toString());
		json.add(EnumConnectType.NONE.toString(), noneElement);

		JsonObject wireElement = new JsonObject();
		wireElement.addProperty("parent", wire.getLocation().toString());
		json.add(EnumConnectType.WIRE.toString(), wireElement);

		JsonObject inventoryElement = new JsonObject();
		inventoryElement.addProperty("parent", inventory.getLocation().toString());
		json.add(EnumConnectType.INVENTORY.toString(), inventoryElement);

		return json;
	}

}
