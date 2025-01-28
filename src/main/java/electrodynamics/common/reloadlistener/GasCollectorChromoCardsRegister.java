package electrodynamics.common.reloadlistener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javax.annotation.Nullable;

import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import electrodynamics.Electrodynamics;
import electrodynamics.api.gas.Gas;
import electrodynamics.api.gas.GasStack;
import electrodynamics.common.packet.types.client.PacketSetClientGasCollectorCards;
import electrodynamics.registers.ElectrodynamicsGases;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
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
import net.minecraft.world.level.biome.Biome;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.network.PacketDistributor;

public class GasCollectorChromoCardsRegister extends SimplePreparableReloadListener<HashSet<JsonObject>> {

    public static GasCollectorChromoCardsRegister INSTANCE = null;

    public static final String FOLDER = "machines/gas_collector_chromotography_cards";

    public static final String ITEM_KEY = "item";
    public static final String GAS_KEY = "gas";
    public static final String AMOUNT_KEY = "amount";
    public static final String PRESSURE_KEY = "pressure";
    public static final String TEMPERATURE_KEY = "temperature";
    public static final String BIOME_KEY = "biome";

    protected static final String JSON_EXTENSION = ".json";
    protected static final int JSON_EXTENSION_LENGTH = JSON_EXTENSION.length();

    private static final Gson GSON = new Gson();

    private final HashMap<Item, AtmosphericResult> results = new HashMap<>();

    private final HashMap<TagKey<Item>, AtmosphericResult> tags = new HashMap<>();

    private final Logger logger = Electrodynamics.LOGGER;

    @Override
    protected HashSet<JsonObject> prepare(ResourceManager manager, ProfilerFiller profiler) {
        HashSet<JsonObject> fuels = new HashSet<>();
        List<Map.Entry<ResourceLocation, Resource>> resources = new ArrayList<>(manager.listResources(FOLDER, GasCollectorChromoCardsRegister::isJson).entrySet());
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
                fuels.add(json);
            } catch (RuntimeException | IOException exception) {
                logger.error("Data loader for {} could not read data {} from file {} in data pack {}", FOLDER, jsonFile, loc, resource.sourcePackId(), exception);
            }

        }

        return fuels;
    }

    @Override
    protected void apply(HashSet<JsonObject> json, ResourceManager manager, ProfilerFiller profiler) {
        results.clear();
        tags.clear();

        json.forEach(set -> {

            String item = set.get(ITEM_KEY).getAsString();
            Gas gas = ElectrodynamicsGases.GAS_REGISTRY.get(ResourceKey.create(ElectrodynamicsGases.GAS_REGISTRY_KEY, ResourceLocation.parse(set.get(GAS_KEY).getAsString())));
            int amount = set.get(AMOUNT_KEY).getAsInt();
            int temperature = set.get(TEMPERATURE_KEY).getAsInt();
            int pressure = set.get(PRESSURE_KEY).getAsInt();

            ResourceKey<Biome> biome = null;
            TagKey<Biome> biomeTag = null;

            if(set.has(BIOME_KEY)){
                String biomeString = set.get(BIOME_KEY).getAsString();

                if (biomeString.contains("#")) {

                    biomeString = biomeString.substring(1);

                    biomeTag = TagKey.create(Registries.BIOME, ResourceLocation.parse(biomeString));

                } else {

                    biome = ResourceKey.create(Registries.BIOME, ResourceLocation.parse(biomeString));

                }

            }

            AtmosphericResult result = new AtmosphericResult(new GasStack(gas, amount, temperature, pressure), biome, biomeTag);

            if (item.contains("#")) {

                item = item.substring(1);

                tags.put(ItemTags.create(ResourceLocation.parse(item)), result);

            } else {

                results.put(BuiltInRegistries.ITEM.get(ResourceLocation.parse(item)), result);

            }


        });

    }

    public void generateTagValues() {
        for (Map.Entry<TagKey<Item>, AtmosphericResult> entry : tags.entrySet()) {
            for (ItemStack item : Ingredient.of(entry.getKey()).getItems()) {
                results.put(item.getItem(), entry.getValue());
            }
        }
        tags.clear();
    }

    public void setClientValues(HashMap<Item, AtmosphericResult> values) {
        this.results.clear();
        this.results.putAll(values);
    }

    public GasCollectorChromoCardsRegister subscribeAsSyncable() {
        NeoForge.EVENT_BUS.addListener(getDatapackSyncListener());
        return this;
    }

    public HashMap<Item, AtmosphericResult> getEntries() {
        return results;
    }

    public boolean hasResult(Item item) {
        return results.containsKey(item);
    }

    public AtmosphericResult getResult(Item item) {
        return results.get(item);
    }

    private Consumer<OnDatapackSyncEvent> getDatapackSyncListener() {
        return event -> {
            generateTagValues();
            ServerPlayer player = event.getPlayer();
            PacketSetClientGasCollectorCards packet = new PacketSetClientGasCollectorCards(results);
            if (player == null) {
                PacketDistributor.sendToAllPlayers(packet);
            } else {
                PacketDistributor.sendToPlayer(player, packet);
            }
        };
    }

    private static boolean isJson(final ResourceLocation filename) {
        return filename.toString().contains(FOLDER) && filename.toString().endsWith(JSON_EXTENSION);
    }

    public static final record AtmosphericResult(GasStack stack, @Nullable ResourceKey<Biome> biome, @Nullable TagKey<Biome> biomeTag) {

    }
}
