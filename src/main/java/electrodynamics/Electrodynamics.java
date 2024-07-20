package electrodynamics;

import java.util.Random;
import java.util.function.Consumer;

import electrodynamics.prefab.properties.PropertyTypes;
import net.neoforged.fml.common.EventBusSubscriber;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import electrodynamics.api.References;
import electrodynamics.client.ClientRegister;
import electrodynamics.common.block.states.ElectrodynamicsBlockStates;
import electrodynamics.common.block.voxelshapes.ElectrodynamicsVoxelShapeRegistry;
import electrodynamics.common.entity.ElectrodynamicsAttributeModifiers;
import electrodynamics.common.event.ServerEventHandler;
import electrodynamics.common.eventbus.RegisterPropertiesEvent;
import electrodynamics.common.packet.types.client.PacketResetGuidebookPages;
import electrodynamics.common.reloadlistener.CoalGeneratorFuelRegister;
import electrodynamics.common.reloadlistener.CombustionFuelRegister;
import electrodynamics.common.reloadlistener.ThermoelectricGeneratorHeatRegister;
import electrodynamics.common.settings.Constants;
import electrodynamics.common.settings.OreConfig;
import electrodynamics.common.tags.ElectrodynamicsTags;
import electrodynamics.prefab.configuration.ConfigurationHandler;
import electrodynamics.prefab.properties.PropertyManager;
import electrodynamics.registers.ElectrodynamicsCapabilities;
import electrodynamics.registers.UnifiedElectrodynamicsRegister;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoader;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
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
        UnifiedElectrodynamicsRegister.register(bus);

        ElectrodynamicsAttributeModifiers.init();

    }

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        ServerEventHandler.init();
        CombustionFuelRegister.INSTANCE = new CombustionFuelRegister().subscribeAsSyncable();
        CoalGeneratorFuelRegister.INSTANCE = new CoalGeneratorFuelRegister().subscribeAsSyncable();
        ThermoelectricGeneratorHeatRegister.INSTANCE = new ThermoelectricGeneratorHeatRegister().subscribeAsSyncable();
        NeoForge.EVENT_BUS.addListener(getGuidebookListener());
        ElectrodynamicsTags.init();
        // CraftingHelper.register(ConfigCondition.Serializer.INSTANCE); // Probably wrong location after update from 1.18.2 to
        // 1.19.2

        // RegisterFluidToGasMapEvent map = new RegisterFluidToGasMapEvent();
        // MinecraftForge.EVENT_BUS.post(map);
        // ElectrodynamicsGases.MAPPED_GASSES.putAll(map.fluidToGasMap);

        event.enqueueWork(() -> {
            RegisterPropertiesEvent properties = new RegisterPropertiesEvent();

            ModLoader.postEvent(properties);

            PropertyManager.registerProperties(properties.getRegisteredProperties());
        });
        ElectrodynamicsVoxelShapeRegistry.init();

    }

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        ElectrodynamicsCapabilities.register(event);

    }

    @SubscribeEvent
    public static void registerProperties(RegisterPropertiesEvent event) {
        event.registerProperty(PropertyTypes.BYTE);
        event.registerProperty(PropertyTypes.BOOLEAN);
        event.registerProperty(PropertyTypes.INTEGER);
        event.registerProperty(PropertyTypes.LONG);
        event.registerProperty(PropertyTypes.FLOAT);
        event.registerProperty(PropertyTypes.DOUBLE);
        event.registerProperty(PropertyTypes.UUID);
        event.registerProperty(PropertyTypes.COMPOUND_TAG);
        event.registerProperty(PropertyTypes.BLOCK_POS);
        event.registerProperty(PropertyTypes.INVENTORY_ITEMS);
        event.registerProperty(PropertyTypes.FLUID_STACK);
        event.registerProperty(PropertyTypes.BLOCK_POS_LIST);
        event.registerProperty(PropertyTypes.LOCATION);
        event.registerProperty(PropertyTypes.GAS_STACK);
        event.registerProperty(PropertyTypes.ITEM_STACK);
        event.registerProperty(PropertyTypes.BLOCK_STATE);
        event.registerProperty(PropertyTypes.TRANSFER_PACK);
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
            if(player == null){
                PacketDistributor.sendToAllPlayers(PacketResetGuidebookPages.PACKET);
            } else {
                PacketDistributor.sendToPlayer(player, PacketResetGuidebookPages.PACKET);
            }
        };

    }

}
