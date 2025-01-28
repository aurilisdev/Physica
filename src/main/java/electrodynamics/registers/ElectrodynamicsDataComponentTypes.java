package electrodynamics.registers;

import java.util.List;

import com.mojang.serialization.Codec;

import electrodynamics.api.References;
import electrodynamics.api.fluid.FluidStackComponent;
import electrodynamics.api.gas.GasStack;
import electrodynamics.api.item.IItemElectric;
import electrodynamics.common.item.gear.armor.types.ItemJetpack;
import electrodynamics.prefab.utilities.NBTUtils;
import electrodynamics.prefab.utilities.object.Location;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ElectrodynamicsDataComponentTypes {

    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES = DeferredRegister.create(BuiltInRegistries.DATA_COMPONENT_TYPE, References.ID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<GasStack>> GAS_STACK = DATA_COMPONENT_TYPES.register("gasstack", () -> DataComponentType.<GasStack>builder().persistent(GasStack.CODEC).networkSynchronized(GasStack.STREAM_CODEC).cacheEncoding().build());
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<IItemElectric.ElectricItemData>> ELECTRIC_ITEM_DATA = DATA_COMPONENT_TYPES.register("electricitemdata", () -> DataComponentType.<IItemElectric.ElectricItemData>builder().persistent(IItemElectric.ElectricItemData.CODEC).networkSynchronized(IItemElectric.ElectricItemData.STREAM_CODEC).build());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Double>> HEAT_STORED = DATA_COMPONENT_TYPES.register("heatstored", () -> DataComponentType.<Double>builder().persistent(Codec.DOUBLE).networkSynchronized(ByteBufCodecs.DOUBLE).cacheEncoding().build());
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Double>> PATTERN_INTEGRITY = DATA_COMPONENT_TYPES.register("patternintegrity", () -> DataComponentType.<Double>builder().persistent(Codec.DOUBLE).networkSynchronized(ByteBufCodecs.DOUBLE).cacheEncoding().build());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Double>> SPEED = DATA_COMPONENT_TYPES.register("speed", () -> DataComponentType.<Double>builder().persistent(Codec.DOUBLE).networkSynchronized(ByteBufCodecs.DOUBLE).cacheEncoding().build());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Double>> POWER_USAGE = DATA_COMPONENT_TYPES.register("powerusage", () -> DataComponentType.<Double>builder().persistent(Codec.DOUBLE).networkSynchronized(ByteBufCodecs.DOUBLE).cacheEncoding().build());
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Double>> DELTA_Y = DATA_COMPONENT_TYPES.register("deltay", () -> DataComponentType.<Double>builder().persistent(Codec.DOUBLE).networkSynchronized(ByteBufCodecs.DOUBLE).cacheEncoding().build());
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Double>> JOULES = DATA_COMPONENT_TYPES.register("joules", () -> DataComponentType.<Double>builder().persistent(Codec.DOUBLE).networkSynchronized(ByteBufCodecs.DOUBLE).cacheEncoding().build());
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Double>> XP = DATA_COMPONENT_TYPES.register(NBTUtils.XP, () -> DataComponentType.<Double>builder().persistent(Codec.DOUBLE).networkSynchronized(ByteBufCodecs.DOUBLE).cacheEncoding().build());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> ON = DATA_COMPONENT_TYPES.register(NBTUtils.ON, () -> DataComponentType.<Boolean>builder().persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL).cacheEncoding().build());
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> HURT = DATA_COMPONENT_TYPES.register(ItemJetpack.WAS_HURT_KEY, () -> DataComponentType.<Boolean>builder().persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL).cacheEncoding().build());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> USED = DATA_COMPONENT_TYPES.register(NBTUtils.USED, () -> DataComponentType.<Boolean>builder().persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL).cacheEncoding().build());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> SMART = DATA_COMPONENT_TYPES.register(NBTUtils.SMART, () -> DataComponentType.<Boolean>builder().persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL).cacheEncoding().build());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> RESET = DATA_COMPONENT_TYPES.register("reset", () -> DataComponentType.<Boolean>builder().persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL).cacheEncoding().build());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> SUCESS = DATA_COMPONENT_TYPES.register(NBTUtils.SUCESS, () -> DataComponentType.<Boolean>builder().persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL).cacheEncoding().build());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> PLATES = DATA_COMPONENT_TYPES.register(NBTUtils.PLATES, () -> DataComponentType.<Integer>builder().persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT).cacheEncoding().build());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> ENUM = DATA_COMPONENT_TYPES.register("enumval", () -> DataComponentType.<Integer>builder().persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT).cacheEncoding().build());
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> RANGE = DATA_COMPONENT_TYPES.register("range", () -> DataComponentType.<Integer>builder().persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT).cacheEncoding().build());
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> MODE = DATA_COMPONENT_TYPES.register(NBTUtils.MODE, () -> DataComponentType.<Integer>builder().persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT).cacheEncoding().build());
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> TIMER = DATA_COMPONENT_TYPES.register(NBTUtils.TIMER, () -> DataComponentType.<Integer>builder().persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT).cacheEncoding().build());
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Location>> LOCATION_1 = DATA_COMPONENT_TYPES.register(NBTUtils.LOCATION + "1", () -> DataComponentType.<Location>builder().persistent(Location.CODEC).networkSynchronized(Location.STREAM_CODEC).cacheEncoding().build());
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Location>> LOCATION_2 = DATA_COMPONENT_TYPES.register(NBTUtils.LOCATION + "2", () -> DataComponentType.<Location>builder().persistent(Location.CODEC).networkSynchronized(Location.STREAM_CODEC).cacheEncoding().build());
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<FluidStackComponent>> FLUID_STACK = DATA_COMPONENT_TYPES.register("fluidstack", () -> DataComponentType.<FluidStackComponent>builder().persistent(FluidStackComponent.CODEC).networkSynchronized(FluidStackComponent.STREAM_CODEC).cacheEncoding().build());
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Block>> BLOCK = DATA_COMPONENT_TYPES.register("block", () -> DataComponentType.<Block>builder().persistent(BuiltInRegistries.BLOCK.byNameCodec()).networkSynchronized(ByteBufCodecs.fromCodec(BuiltInRegistries.BLOCK.byNameCodec())).cacheEncoding().build());
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<BlockPos>> BLOCK_POS = DATA_COMPONENT_TYPES.register("blockpos", () -> DataComponentType.<BlockPos>builder().persistent(BlockPos.CODEC).networkSynchronized(BlockPos.STREAM_CODEC).cacheEncoding().build());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ResourceLocation>> RESOURCE_LOCATION = DATA_COMPONENT_TYPES.register("resourcelocation", () -> DataComponentType.<ResourceLocation>builder().persistent(ResourceLocation.CODEC).networkSynchronized(ResourceLocation.STREAM_CODEC).cacheEncoding().build());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<List<Direction>>> DIRECTIONS = DATA_COMPONENT_TYPES.register("direction_list", () -> DataComponentType.<List<Direction>>builder().persistent(Direction.CODEC.listOf()).networkSynchronized(ByteBufCodecs.fromCodec(Direction.CODEC.listOf())).cacheEncoding().build());


}
