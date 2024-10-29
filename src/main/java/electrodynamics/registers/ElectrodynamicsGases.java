package electrodynamics.registers;

import java.util.concurrent.ConcurrentHashMap;

import electrodynamics.api.References;
import electrodynamics.api.gas.Gas;
import electrodynamics.common.tags.ElectrodynamicsTags;
import electrodynamics.prefab.utilities.ElectroTextUtils;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ElectrodynamicsGases {

    public static final ConcurrentHashMap<Holder<Fluid>, Gas> MAPPED_GASSES = new ConcurrentHashMap<>();

    public static final ResourceLocation GAS_REGISTRY_LOC = ResourceLocation.fromNamespaceAndPath(References.ID, "gases");
    public static final ResourceKey<Registry<Gas>> GAS_REGISTRY_KEY = ResourceKey.createRegistryKey(GAS_REGISTRY_LOC);
    public static final DeferredRegister<Gas> GASES = DeferredRegister.create(GAS_REGISTRY_KEY, References.ID);
    public static final Registry<Gas> GAS_REGISTRY = ElectrodynamicsGases.GASES.makeRegistry(builder -> builder.sync(true));

    public static final Holder<Gas> EMPTY = GASES.register("empty", () -> new Gas(new Holder.Direct<>(Items.AIR), null, ElectroTextUtils.gas("empty")));
    public static final Holder<Gas> HYDROGEN = GASES.register("hydrogen", () -> new Gas(ElectrodynamicsItems.ITEM_PORTABLECYLINDER, ElectrodynamicsTags.Gases.HYDROGEN, ElectroTextUtils.gas("hydrogen"), 33, ElectrodynamicsFluids.FLUID_HYDROGEN));
    public static final Holder<Gas> OXYGEN = GASES.register("oxygen", () -> new Gas(ElectrodynamicsItems.ITEM_PORTABLECYLINDER, ElectrodynamicsTags.Gases.OXYGEN, ElectroTextUtils.gas("oxygen"), 90, ElectrodynamicsFluids.FLUID_OXYGEN));
    public static final Holder<Gas> STEAM = GASES.register("steam", () -> new Gas(ElectrodynamicsItems.ITEM_PORTABLECYLINDER, ElectrodynamicsTags.Gases.STEAM, ElectroTextUtils.gas("steam"), 373, new Holder.Direct<>(Fluids.WATER)));
    public static final Holder<Gas> NITROGEN = GASES.register("nitrogen", () -> new Gas(ElectrodynamicsItems.ITEM_PORTABLECYLINDER, ElectrodynamicsTags.Gases.NITROGEN, ElectroTextUtils.gas("nitrogen")));
    public static final Holder<Gas> CARBON_DIOXIDE = GASES.register("carbondioxide", () -> new Gas(ElectrodynamicsItems.ITEM_PORTABLECYLINDER, ElectrodynamicsTags.Gases.CARBON_DIOXIDE, ElectroTextUtils.gas("carbondioxide")));
    public static final Holder<Gas> ARGON = GASES.register("argon", () -> new Gas(ElectrodynamicsItems.ITEM_PORTABLECYLINDER, ElectrodynamicsTags.Gases.ARGON, ElectroTextUtils.gas("argon")));
    public static final Holder<Gas> SULFUR_DIOXIDE = GASES.register("sulfurdioxide", () -> new Gas(ElectrodynamicsItems.ITEM_PORTABLECYLINDER, ElectrodynamicsTags.Gases.SULFUR_DIOXIDE, ElectroTextUtils.gas("sulfurdioxide")));

}
