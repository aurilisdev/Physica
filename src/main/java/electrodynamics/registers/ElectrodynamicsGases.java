package electrodynamics.registers;

import java.util.concurrent.ConcurrentHashMap;

import electrodynamics.Electrodynamics;
import electrodynamics.api.References;
import electrodynamics.api.gas.Gas;
import electrodynamics.prefab.utilities.ElectroTextUtils;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ElectrodynamicsGases {

    public static final ConcurrentHashMap<Holder<Fluid>, Gas> MAPPED_GASSES = new ConcurrentHashMap<>();

    public static final ResourceLocation GAS_REGISTRY_LOC = Electrodynamics.rl("gases");
    public static final ResourceKey<Registry<Gas>> GAS_REGISTRY_KEY = ResourceKey.createRegistryKey(GAS_REGISTRY_LOC);
    public static final DeferredRegister<Gas> GASES = DeferredRegister.create(GAS_REGISTRY_KEY, References.ID);
    public static final Registry<Gas> GAS_REGISTRY = ElectrodynamicsGases.GASES.makeRegistry(builder -> builder.sync(true));

    public static final DeferredHolder<Gas, Gas> EMPTY = GASES.register("empty", () -> new Gas(new Holder.Direct<>(Items.AIR), ElectroTextUtils.gas("empty")));
    public static final DeferredHolder<Gas, Gas> HYDROGEN = GASES.register("hydrogen", () -> new Gas(ElectrodynamicsItems.ITEM_PORTABLECYLINDER, ElectroTextUtils.gas("hydrogen"), 33, ElectrodynamicsFluids.FLUID_HYDROGEN));
    public static final DeferredHolder<Gas, Gas> OXYGEN = GASES.register("oxygen", () -> new Gas(ElectrodynamicsItems.ITEM_PORTABLECYLINDER, ElectroTextUtils.gas("oxygen"), 90, ElectrodynamicsFluids.FLUID_OXYGEN));
    public static final DeferredHolder<Gas, Gas> STEAM = GASES.register("steam", () -> new Gas(ElectrodynamicsItems.ITEM_PORTABLECYLINDER, ElectroTextUtils.gas("steam"), 373, BuiltInRegistries.FLUID.wrapAsHolder(Fluids.WATER)));
    public static final DeferredHolder<Gas, Gas> NITROGEN = GASES.register("nitrogen", () -> new Gas(ElectrodynamicsItems.ITEM_PORTABLECYLINDER, ElectroTextUtils.gas("nitrogen")));
    public static final DeferredHolder<Gas, Gas> CARBON_DIOXIDE = GASES.register("carbondioxide", () -> new Gas(ElectrodynamicsItems.ITEM_PORTABLECYLINDER, ElectroTextUtils.gas("carbondioxide")));
    public static final DeferredHolder<Gas, Gas> ARGON = GASES.register("argon", () -> new Gas(ElectrodynamicsItems.ITEM_PORTABLECYLINDER, ElectroTextUtils.gas("argon")));
    public static final DeferredHolder<Gas, Gas> SULFUR_DIOXIDE = GASES.register("sulfurdioxide", () -> new Gas(ElectrodynamicsItems.ITEM_PORTABLECYLINDER, ElectroTextUtils.gas("sulfurdioxide")));
    public static final DeferredHolder<Gas, Gas> AMMONIA = GASES.register("ammonia", () -> new Gas(ElectrodynamicsItems.ITEM_PORTABLECYLINDER, ElectroTextUtils.gas("ammonia"), 239, ElectrodynamicsFluids.FLUID_AMMONIA));

}
