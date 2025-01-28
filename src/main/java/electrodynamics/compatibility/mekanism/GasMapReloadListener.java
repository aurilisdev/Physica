package electrodynamics.compatibility.mekanism;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import electrodynamics.Electrodynamics;
import electrodynamics.api.gas.Gas;
import electrodynamics.registers.ElectrodynamicsGases;
import mekanism.api.MekanismAPI;
import mekanism.api.chemical.Chemical;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;

public class GasMapReloadListener extends SimplePreparableReloadListener<JsonObject> {

    public static final GasMapReloadListener INSTANCE = new GasMapReloadListener();

    public static final String FOLDER = "machines";
    public static final String FILE_NAME = "rotary_unifier_mappings";
    protected static final String JSON_EXTENSION = ".json";
    protected static final int JSON_EXTENSION_LENGTH = JSON_EXTENSION.length();

    private static final Gson GSON = new Gson();

    public final HashMap<Gas, Chemical> gasToChemicalMap = new HashMap<>();
    public final HashMap<Chemical, Gas> chemicalToGasMap = new HashMap<>();

    private final Logger logger = Electrodynamics.LOGGER;

    @Override
    protected JsonObject prepare(ResourceManager manager, ProfilerFiller profiler) {
        JsonObject combined = new JsonObject();

        List<Map.Entry<ResourceLocation, Resource>> resources = new ArrayList<>(manager.listResources(FOLDER, GasMapReloadListener::isJson).entrySet());
        Collections.reverse(resources);

        for (Map.Entry<ResourceLocation, Resource> entry : resources) {
            ResourceLocation loc = entry.getKey();
            final String namespace = loc.getNamespace();
            final String filePath = loc.getPath();
            final String dataPath = filePath.substring(FOLDER.length() + 1, filePath.length() - JSON_EXTENSION_LENGTH);

            final ResourceLocation jsonFile = ResourceLocation.fromNamespaceAndPath(namespace, dataPath);

            Resource resource = entry.getValue();
            try (final InputStream inputStream = resource.open(); final Reader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));) {
                final JsonObject json = (JsonObject) GsonHelper.fromJson(GSON, reader, JsonElement.class);

                json.entrySet().forEach(set -> {

                    if (combined.has(set.getKey())) {
                        combined.remove(set.getKey());
                    }

                    combined.add(set.getKey(), set.getValue());
                });

            } catch (RuntimeException | IOException exception) {
                logger.error("Data loader for {} could not read data {} from file {} in data pack {}", FOLDER, jsonFile, loc, resource.sourcePackId(), exception);
            }

        }
        return combined;
    }

    @Override
    protected void apply(JsonObject json, ResourceManager manager, ProfilerFiller profiler) {
        gasToChemicalMap.clear();
        chemicalToGasMap.clear();

        json.entrySet().forEach(set -> {

            ResourceLocation gasKey = ResourceLocation.parse(set.getKey());
            ResourceLocation chemicalKey = ResourceLocation.parse(set.getValue().getAsString());

            Gas gas = ElectrodynamicsGases.GAS_REGISTRY.get(gasKey);
            Chemical chemical = MekanismAPI.CHEMICAL_REGISTRY.get(chemicalKey);

            gasToChemicalMap.put(gas, chemical);
            chemicalToGasMap.put(chemical, gas);

        });

    }

    private static boolean isJson(final ResourceLocation filename) {
        return filename.getPath().contains(FILE_NAME + JSON_EXTENSION);
    }

}
