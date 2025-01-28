package electrodynamics.datagen.utils.model;

import com.google.gson.JsonObject;

import electrodynamics.client.modelbakers.bakerytypes.SlaveNodeModelLoader;
import net.neoforged.neoforge.client.model.generators.CustomLoaderBuilder;
import net.neoforged.neoforge.client.model.generators.ModelBuilder;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class SlaveNodeModelBuilder <T extends ModelBuilder<T>> extends CustomLoaderBuilder<T> {

    public static <T extends ModelBuilder<T>> SlaveNodeModelBuilder<T> begin(T parent, ExistingFileHelper existingFileHelper) {
        return new SlaveNodeModelBuilder<>(parent, existingFileHelper);
    }

    private ModelFile model;

    protected SlaveNodeModelBuilder(T parent, ExistingFileHelper existingFileHelper) {
        super(SlaveNodeModelLoader.ID, parent, existingFileHelper, false);
    }

    public SlaveNodeModelBuilder<T> model(ModelFile model) {
        this.model = model;

        return this;
    }

    @Override
    public JsonObject toJson(JsonObject json) {
        json = super.toJson(json);

        JsonObject modelElement = new JsonObject();
        modelElement.addProperty("parent", model.getLocation().toString());
        json.add("model", modelElement);

        return json;
    }
}
