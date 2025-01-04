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
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import electrodynamics.Electrodynamics;
import electrodynamics.common.packet.types.client.PacketSetClientCombustionFuel;
import electrodynamics.prefab.utilities.object.CombustionFuelSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.network.PacketDistributor;

public class CombustionFuelRegister extends SimplePreparableReloadListener<HashSet<JsonObject>> {

    public static CombustionFuelRegister INSTANCE = null;

    public static final String FOLDER = "machines/combustion_fuel";

    protected static final String JSON_EXTENSION = ".json";
    protected static final int JSON_EXTENSION_LENGTH = JSON_EXTENSION.length();

    private static final Gson GSON = new Gson();

    private final HashSet<CombustionFuelSource> fuels = new HashSet<>();

    private final Logger logger = Electrodynamics.LOGGER;

    @Override
    protected HashSet<JsonObject> prepare(ResourceManager manager, ProfilerFiller profiler) {
        HashSet<JsonObject> fuels = new HashSet<>();
        List<Entry<ResourceLocation, Resource>> resources = new ArrayList<>(manager.listResources(FOLDER, CombustionFuelRegister::isJson).entrySet());
        Collections.reverse(resources);

        for (Entry<ResourceLocation, Resource> entry : resources) {
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
    protected void apply(HashSet<JsonObject> jsons, ResourceManager manager, ProfilerFiller profiler) {
        fuels.clear();
        for (JsonObject json : jsons) {
            fuels.add(CombustionFuelSource.fromJson(json));
        }
    }

    public HashSet<CombustionFuelSource> getFuels() {
        return fuels;
    }

    public void setClientValues(HashSet<CombustionFuelSource> values) {
        fuels.clear();
        fuels.addAll(values);
    }

    public TagKey<Fluid>[] getFluidTags() {
        List<TagKey<Fluid>> values = new ArrayList<>();
        for (CombustionFuelSource source : fuels) {
            values.add(source.getTag());
        }
        TagKey<Fluid>[] arr = new TagKey[values.size()];
        return values.toArray(arr);
    }

    public CombustionFuelSource getFuelFromFluid(FluidStack stack) {
        for (CombustionFuelSource fuel : fuels) {
            if (fuel.isFuelSource(stack)) {
                return fuel;
            }
        }
        return CombustionFuelSource.EMPTY;
    }

    public CombustionFuelRegister subscribeAsSyncable() {
        NeoForge.EVENT_BUS.addListener(getDatapackSyncListener());
        return this;
    }

    private Consumer<OnDatapackSyncEvent> getDatapackSyncListener() {
        return event -> {
            ServerPlayer player = event.getPlayer();
            PacketSetClientCombustionFuel packet = new PacketSetClientCombustionFuel(fuels);
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

}
