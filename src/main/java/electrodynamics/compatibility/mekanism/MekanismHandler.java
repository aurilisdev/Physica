package electrodynamics.compatibility.mekanism;

import java.util.Objects;
import java.util.function.Predicate;

import electrodynamics.api.gas.Gas;
import electrodynamics.api.gas.GasAction;
import electrodynamics.api.gas.GasStack;
import electrodynamics.api.gas.PropertyGasTank;
import electrodynamics.common.network.utils.GasUtilities;
import electrodynamics.common.settings.Constants;
import electrodynamics.common.tile.compatibility.TileRotaryUnifier;
import electrodynamics.prefab.properties.Property;
import electrodynamics.prefab.properties.PropertyType;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.tile.components.type.ComponentElectrodynamic;
import electrodynamics.prefab.tile.components.type.ComponentProcessor;
import electrodynamics.prefab.utilities.BlockEntityUtils;
import electrodynamics.registers.ElectrodynamicsTiles;
import mekanism.api.Action;
import mekanism.api.chemical.Chemical;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.chemical.IChemicalHandler;
import mekanism.common.capabilities.Capabilities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.event.AddReloadListenerEvent;

public class MekanismHandler {

    public static final PropertyType<ChemicalStack, RegistryFriendlyByteBuf> CHEMICAL_STACK = new PropertyType<>(
            //
            Objects::equals,
            //
            ChemicalStack.OPTIONAL_STREAM_CODEC,
            //
            writer -> {
                Tag fluidTag = new CompoundTag();
                fluidTag = ChemicalStack.OPTIONAL_CODEC.encode(writer.prop().get(), NbtOps.INSTANCE, fluidTag).getOrThrow();
                writer.tag().put(writer.prop().getName(), fluidTag);
                int i = 0;
            },
            //
            reader -> ChemicalStack.OPTIONAL_CODEC.decode(NbtOps.INSTANCE, reader.tag().getCompound(reader.prop().getName())).getOrThrow().getFirst()
            //
    );

    public static void addDataListener(AddReloadListenerEvent event) {
        event.addListener(GasMapReloadListener.INSTANCE);
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.CHEMICAL.block(), ElectrodynamicsTiles.TILE_ROTARYUNIFIER.get(), (tile, context) -> {
            if (context == null || context != tile.chemicalIO) {
                return null;
            }
            Property<ChemicalStack> prop = getProp(tile);
            return new IChemicalHandler() {
                @Override
                public int getChemicalTanks() {
                    return 1;
                }

                @Override
                public ChemicalStack getChemicalInTank(int tank) {
                    return prop.get();
                }

                @Override
                public void setChemicalInTank(int tank, ChemicalStack stack) {
                    prop.set(stack);
                }

                @Override
                public long getChemicalTankCapacity(int tank) {
                    return TileRotaryUnifier.MAX_CHEM_AMOUNT;
                }

                @Override
                public boolean isValid(int tank, ChemicalStack stack) {
                    return GasMapReloadListener.INSTANCE.chemicalToGasMap.containsKey(stack.getChemical()) && (prop.get().is(stack.getChemical()) || prop.get().isEmpty());
                }

                @Override
                public ChemicalStack insertChemical(int tank, ChemicalStack stack, Action action) {

                    ChemicalStack returner = stack.copy();


                    if (!tile.conversionIsFlipped.get()) {
                        return returner;
                    }
                    if (stack.isEmpty() || !isValid(tank, stack)) {
                        return returner;
                    }
                    if (action.simulate()) {
                        if (prop.get().isEmpty()) {
                            returner.shrink(Math.min(TileRotaryUnifier.MAX_CHEM_AMOUNT, stack.getAmount()));
                            return returner;
                        }
                        if (!ChemicalStack.isSameChemical(prop.get(), stack)) {
                            return stack;
                        }
                        returner.shrink(Math.min(TileRotaryUnifier.MAX_CHEM_AMOUNT - prop.get().getAmount(), stack.getAmount()));
                        return returner;
                    }
                    if (prop.get().isEmpty()) {
                        long accepted = Math.min(TileRotaryUnifier.MAX_CHEM_AMOUNT, stack.getAmount());
                        returner.shrink(accepted);
                        prop.set(stack.copyWithAmount(accepted));
                        return returner;
                    }
                    if (!ChemicalStack.isSameChemical(prop.get(), stack)) {
                        return stack;
                    }
                    long filled = TileRotaryUnifier.MAX_CHEM_AMOUNT - prop.get().getAmount();

                    ChemicalStack chem = prop.get().copy();

                    if (stack.getAmount() < filled) {
                        chem.grow(stack.getAmount());
                        prop.set(chem);
                        filled = stack.getAmount();
                    } else {
                        chem.setAmount(TileRotaryUnifier.MAX_CHEM_AMOUNT);
                        prop.set(chem);
                    }
                    returner.shrink(filled);
                    return returner;
                }

                @Override
                public ChemicalStack extractChemical(int tank, long amount, Action action) {
                    if (tile.conversionIsFlipped.get()) {
                        return ChemicalStack.EMPTY;
                    }
                    long drained = TileRotaryUnifier.MAX_CHEM_AMOUNT;
                    if (prop.get().getAmount() < drained) {
                        drained = prop.get().getAmount();
                    }
                    ChemicalStack stack = prop.get().copyWithAmount(drained);
                    if (action.execute() && drained > 0) {
                        ChemicalStack chem = prop.get().copy();
                        chem.shrink(drained);
                        prop.set(chem);
                    }
                    return stack;
                }
            };
        });

    }

    public static int addProperty(TileRotaryUnifier tile) {
        return tile.property(new Property<>(CHEMICAL_STACK, "chemicalstackprop", ChemicalStack.EMPTY)).getIndex();
    }

    public static Predicate<GasStack> getTankPredicate() {
        return stack -> GasMapReloadListener.INSTANCE.gasToChemicalMap.containsKey(stack.getGas());
    }

    public static Property<ChemicalStack> getProp(TileRotaryUnifier tile) {
        return (Property<ChemicalStack>) tile.getPropertyManager().getProperties().get(tile.chemStackIndex);
    }

    public static boolean canProcess(TileRotaryUnifier tile, ComponentProcessor proc) {

        Property<ChemicalStack> prop = getProp(tile);
        ;
        PropertyGasTank tank = tile.gasTank;
        int rate = (int) (Constants.ROTARY_UNIFIER_CONVERSION_RATE * proc.operatingSpeed.get());
        ComponentElectrodynamic electro = tile.getComponent(IComponentType.Electrodynamic);

        if (tile.conversionIsFlipped.get()) {

            GasUtilities.outputToPipe(tile, tank.asArray(), BlockEntityUtils.MachineDirection.RIGHT.mappedDir);

            if (electro.getJoulesStored() < proc.getUsage() || prop.get().isEmpty()) {
                return false;
            }

            Gas gas = GasMapReloadListener.INSTANCE.chemicalToGasMap.get(prop.get().getChemical());

            if (gas == null) {
                return false;
            }

            GasStack proposed = new GasStack(gas, rate, gas.getCondensationTemp() + 1, Gas.PRESSURE_AT_SEA_LEVEL);

            if (!tank.isGasValid(proposed)) {
                return false;
            }

            int accepted = tank.fill(proposed, GasAction.SIMULATE);

            return accepted > 0;

        } else {

            BlockEntity faceTile = tile.getLevel().getBlockEntity(tile.getBlockPos().relative(tile.chemicalIO));

            if (faceTile != null && !prop.get().isEmpty()) {
                IChemicalHandler handler = faceTile.getLevel().getCapability(Capabilities.CHEMICAL.block(), faceTile.getBlockPos(), faceTile.getBlockState(), faceTile, tile.chemicalIO.getOpposite());

                if (handler != null) {
                    for (int i = 0; i < handler.getChemicalTanks(); i++) {

                        ChemicalStack storedChem = prop.get().copy();

                        ChemicalStack accepted = handler.insertChemical(storedChem, Action.EXECUTE);

                        storedChem.shrink(accepted.getAmount());

                        prop.set(storedChem);
                    }
                }


            }

            if (electro.getJoulesStored() < proc.getUsage()) {
                return false;
            }

            GasStack gas = tank.getGas();

            if (gas.isEmpty()) {
                return false;
            }

            Chemical chemical = GasMapReloadListener.INSTANCE.gasToChemicalMap.get(gas.getGas());

            if (chemical == null || (!prop.get().isEmpty() && !prop.get().is(chemical)) || gas.getTemperature() > gas.getGas().getCondensationTemp() + 1) {
                return false;
            }

            return (Math.max(0, TileRotaryUnifier.MAX_CHEM_AMOUNT - prop.get().getAmount())) > 0;

        }

    }

    public static void process(TileRotaryUnifier tile, ComponentProcessor proc) {

        Property<ChemicalStack> prop = getProp(tile);
        ;
        PropertyGasTank tank = tile.gasTank;
        int rate = (int) (Constants.ROTARY_UNIFIER_CONVERSION_RATE * proc.operatingSpeed.get());

        if (tile.conversionIsFlipped.get()) {

            Gas gas = GasMapReloadListener.INSTANCE.chemicalToGasMap.get(prop.get().getChemical());
            GasStack proposed = new GasStack(gas, rate, gas.getCondensationTemp() + 1, Gas.PRESSURE_AT_SEA_LEVEL);
            int accepted = tank.fill(proposed, GasAction.EXECUTE);

            ChemicalStack chemicalStack = prop.get().copy();
            chemicalStack.shrink(accepted);
            prop.set(chemicalStack);


        } else {

            GasStack gas = tank.getGas();
            Chemical chemical = GasMapReloadListener.INSTANCE.gasToChemicalMap.get(gas.getGas());
            int accepted = (int) Math.min(rate, Math.max(gas.getAmount(), TileRotaryUnifier.MAX_CHEM_AMOUNT - prop.get().getAmount()));

            if (prop.get().isEmpty()) {
                prop.set(new ChemicalStack(chemical, accepted));
            } else {
                ChemicalStack stack = prop.get().copy();
                stack.grow(accepted);
                prop.set(stack);
            }
            tank.drain(accepted, GasAction.EXECUTE);

        }

    }

}
