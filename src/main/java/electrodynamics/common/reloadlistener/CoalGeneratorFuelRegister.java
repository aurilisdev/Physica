package electrodynamics.common.reloadlistener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.Consumer;

import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import electrodynamics.Electrodynamics;
import electrodynamics.common.packet.types.client.PacketSetClientCoalGenFuels;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.network.PacketDistributor;

public class CoalGeneratorFuelRegister extends SimplePreparableReloadListener<JsonObject> {

    public static CoalGeneratorFuelRegister INSTANCE = null;

    public static final String KEY = "values";
    public static final String FOLDER = "machines";
    public static final String FILE_NAME = "coal_generator_fuels";

    protected static final String JSON_EXTENSION = ".json";
    protected static final int JSON_EXTENSION_LENGTH = JSON_EXTENSION.length();

    private static final Gson GSON = new Gson();

    private final HashSet<Item> fuels = new HashSet<>();

    private final HashSet<TagKey<Item>> tags = new HashSet<>();

    private final Logger logger = Electrodynamics.LOGGER;

    @Override
    protected JsonObject prepare(ResourceManager manager, ProfilerFiller profiler) {
        JsonObject combinedFuelsJson = new JsonObject();

        List<Entry<ResourceLocation, Resource>> resources = new ArrayList<>(manager.listResources(FOLDER, CoalGeneratorFuelRegister::isJson).entrySet());
        Collections.reverse(resources);
        JsonArray combinedArray = new JsonArray();
        for (Entry<ResourceLocation, Resource> entry : resources) {
            ResourceLocation loc = entry.getKey();
            final String namespace = loc.getNamespace();
            final String filePath = loc.getPath();
            final String dataPath = filePath.substring(FOLDER.length() + 1, filePath.length() - JSON_EXTENSION_LENGTH);

            final ResourceLocation jsonFile = ResourceLocation.fromNamespaceAndPath(namespace, dataPath);

            Resource resource = entry.getValue();
            try (final InputStream inputStream = resource.open(); final Reader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));) {
                final JsonObject json = (JsonObject) GsonHelper.fromJson(GSON, reader, JsonElement.class);
                combinedArray.addAll(json.get(KEY).getAsJsonArray());
            } catch (RuntimeException | IOException exception) {
                logger.error("Data loader for {} could not read data {} from file {} in data pack {}", FOLDER, jsonFile, loc, resource.sourcePackId(), exception);
            }

        }
        combinedFuelsJson.add(KEY, combinedArray);

        return combinedFuelsJson;
    }

    @Override
    protected void apply(JsonObject json, ResourceManager manager, ProfilerFiller profiler) {
        fuels.clear();
        tags.clear();
        ArrayList<String> list = GSON.fromJson(json.get(KEY).getAsJsonArray(), ArrayList.class);
        list.forEach(key -> {
            if (key.charAt(0) == '#') {
                tags.add(ItemTags.create(ResourceLocation.parse(key.substring(1))));
            } else {
                fuels.add(BuiltInRegistries.ITEM.get(ResourceLocation.parse(key)));
            }
        });

    }

    public void generateTagValues() {
        tags.forEach(tag -> {
            for (ItemStack item : Ingredient.of(tag).getItems()) {
                fuels.add(item.getItem());
            }
        });
        tags.clear();
    }

    public void setClientValues(HashSet<Item> fuels) {
        this.fuels.clear();
        this.fuels.addAll(fuels);
    }

    public CoalGeneratorFuelRegister subscribeAsSyncable() {
        NeoForge.EVENT_BUS.addListener(getDatapackSyncListener());
        return this;
    }

    public HashSet<Item> getFuels() {
        return fuels;
    }

    public boolean isFuel(Item item) {
        return fuels.contains(item);
    }

    private Consumer<OnDatapackSyncEvent> getDatapackSyncListener() {
        return event -> {
            generateTagValues();
            ServerPlayer player = event.getPlayer();
            PacketSetClientCoalGenFuels packet = new PacketSetClientCoalGenFuels(fuels);
            if (player == null) {
                PacketDistributor.sendToAllPlayers(packet);
            } else {
                PacketDistributor.sendToPlayer(player, packet);
            }
        };
    }

    private static boolean isJson(final ResourceLocation filename) {
        return filename.getPath().contains(FILE_NAME + JSON_EXTENSION);
    }

}
