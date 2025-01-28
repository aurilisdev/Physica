package electrodynamics;

import java.util.Random;
import java.util.function.Consumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import electrodynamics.api.References;
import electrodynamics.api.electricity.RegisterWiresEvent;
import electrodynamics.client.ClientRegister;
import electrodynamics.common.block.connect.BlockWire;
import electrodynamics.common.block.states.ElectrodynamicsBlockStates;
import electrodynamics.common.block.voxelshapes.ElectrodynamicsVoxelShapes;
import electrodynamics.common.entity.ElectrodynamicsAttributeModifiers;
import electrodynamics.common.event.ServerEventHandler;
import electrodynamics.common.packet.types.client.PacketResetGuidebookPages;
import electrodynamics.common.reloadlistener.CoalGeneratorFuelRegister;
import electrodynamics.common.reloadlistener.CombustionFuelRegister;
import electrodynamics.common.reloadlistener.GasCollectorChromoCardsRegister;
import electrodynamics.common.reloadlistener.ThermoelectricGeneratorHeatRegister;
import electrodynamics.common.settings.Constants;
import electrodynamics.common.settings.OreConfig;
import electrodynamics.common.tags.ElectrodynamicsTags;
import electrodynamics.prefab.configuration.ConfigurationHandler;
import electrodynamics.registers.ElectrodynamicsBlocks;
import electrodynamics.registers.UnifiedElectrodynamicsRegister;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoader;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@Mod(References.ID)
@EventBusSubscriber(modid = References.ID, bus = EventBusSubscriber.Bus.MOD)
public class Electrodynamics {

    public static Logger LOGGER = LogManager.getLogger(electrodynamics.api.References.ID);

    public static final Random RANDOM = new Random();

    public Electrodynamics(IEventBus bus) {
        ConfigurationHandler.registerConfig(Constants.class);
        ConfigurationHandler.registerConfig(OreConfig.class);
        // MUST GO BEFORE BLOCKS!!!!
        ElectrodynamicsBlockStates.init();
        ElectrodynamicsVoxelShapes.init();
        UnifiedElectrodynamicsRegister.register(bus);

        ElectrodynamicsAttributeModifiers.init();

    }

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        ServerEventHandler.init();
        CombustionFuelRegister.INSTANCE = new CombustionFuelRegister().subscribeAsSyncable();
        CoalGeneratorFuelRegister.INSTANCE = new CoalGeneratorFuelRegister().subscribeAsSyncable();
        GasCollectorChromoCardsRegister.INSTANCE = new GasCollectorChromoCardsRegister().subscribeAsSyncable();
        ThermoelectricGeneratorHeatRegister.INSTANCE = new ThermoelectricGeneratorHeatRegister().subscribeAsSyncable();

        NeoForge.EVENT_BUS.addListener(getGuidebookListener());
        ElectrodynamicsTags.init();
        // CraftingHelper.register(ConfigCondition.Serializer.INSTANCE); // Probably wrong location after update from 1.18.2 to
        // 1.19.2

        // RegisterFluidToGasMapEvent map = new RegisterFluidToGasMapEvent();
        // MinecraftForge.EVENT_BUS.post(map);
        // ElectrodynamicsGases.MAPPED_GASSES.putAll(map.fluidToGasMap);

        event.enqueueWork(() -> {

            RegisterWiresEvent wiresEvent = new RegisterWiresEvent();

            ModLoader.postEvent(wiresEvent);

            wiresEvent.process();
        });

    }

    // I wonder how long this bug has been there
    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ClientRegister.setup();
        });
    }

    // Don't really have a better place to put this for now
    private static Consumer<OnDatapackSyncEvent> getGuidebookListener() {

        return event -> {
            ServerPlayer player = event.getPlayer();
            if (player == null) {
                PacketDistributor.sendToAllPlayers(PacketResetGuidebookPages.PACKET);
            } else {
                PacketDistributor.sendToPlayer(player, PacketResetGuidebookPages.PACKET);
            }
        };

    }

    @SubscribeEvent
    public static void registerWires(RegisterWiresEvent event) {
        for (BlockWire wire : ElectrodynamicsBlocks.BLOCKS_WIRE.getAllValues()) {
            event.registerWire(wire);
        }
    }

    public static final ResourceLocation rl(String path) {
        return ResourceLocation.fromNamespaceAndPath(References.ID, path);
    }

    public static final ResourceLocation vanillarl(String path) {
        return ResourceLocation.withDefaultNamespace(path);
    }

    public static final ResourceLocation forgerl(String path) {
        return ResourceLocation.fromNamespaceAndPath("neoforge", path);
    }

    public static final ResourceLocation commonrl(String path) {
        return ResourceLocation.fromNamespaceAndPath("c", path);
    }

}
