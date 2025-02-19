package electrodynamics.prefab.properties;

import electrodynamics.api.References;
import electrodynamics.api.gas.GasStack;
import electrodynamics.prefab.utilities.object.Location;
import electrodynamics.prefab.utilities.object.TransferPack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;

public enum PropertyType implements IPropertyType {
	
	Byte(writer -> writer.buf().writeByte((Byte) writer.value()), reader -> reader.buf().readByte(), writer -> writer.tag().putByte(writer.prop().getName(), (Byte) writer.prop().get()), reader -> reader.tag().getByte(reader.prop().getName()), val -> ((Number) val).byteValue()),
	//
	Boolean(writer -> writer.buf().writeBoolean((Boolean) writer.value()), reader -> reader.buf().readBoolean(), writer -> writer.tag().putBoolean(writer.prop().getName(), (Boolean) writer.prop().get()), reader -> reader.tag().getBoolean(reader.prop().getName())),
	//
	Integer(writer -> writer.buf().writeInt((Integer) writer.value()), reader -> reader.buf().readInt(), writer -> writer.tag().putInt(writer.prop().getName(), (Integer) writer.prop().get()), reader -> reader.tag().getInt(reader.prop().getName()), val -> ((Number) val).intValue()),
	//
	Long(writer -> writer.buf().writeLong((Long) writer.value()), reader -> reader.buf().readLong(), writer -> writer.tag().putLong(writer.prop().getName(), (Long) writer.prop().get()), reader -> reader.tag().getLong(reader.prop().getName()), val -> ((Number) val).longValue()),
	//
	Float(writer -> writer.buf().writeFloat((Float) writer.value()), reader -> reader.buf().readFloat(), writer -> writer.tag().putFloat(writer.prop().getName(), (Float) writer.prop().get()), reader -> reader.tag().getFloat(reader.prop().getName()), val -> ((Number) val).floatValue()),
	//
	Double(writer -> writer.buf().writeDouble((Double) writer.value()), reader -> reader.buf().readDouble(), writer -> writer.tag().putDouble(writer.prop().getName(), (Double) writer.prop().get()), reader -> reader.tag().getDouble(reader.prop().getName()), val -> ((Number) val).doubleValue()),
	//
	UUID(writer -> writer.buf().writeUUID((UUID) writer.value()), reader -> reader.buf().readUUID(), writer -> writer.tag().putUUID(writer.prop().getName(), (UUID) writer.prop().get()), reader -> reader.tag().getUUID(reader.prop().getName())),
	//
	CompoundTag(writer -> writer.buf().writeNbt((CompoundTag) writer.value()), reader -> reader.buf().readNbt(), writer -> writer.tag().put(writer.prop().getName(), (CompoundTag) writer.prop().get()), reader -> reader.tag().getCompound(reader.prop().getName())),
	//
	BlockPos(writer -> writer.buf().writeBlockPos((BlockPos) writer.value()), reader -> reader.buf().readBlockPos(), writer -> writer.tag().put(writer.prop().getName(), NbtUtils.writeBlockPos((BlockPos) writer.prop().get())), reader -> NbtUtils.readBlockPos(reader.tag().getCompound(reader.prop().getName()))),
	//
	InventoryItems((thisList, otherList) -> {
		NonNullList<ItemStack> thisCasted = (NonNullList<ItemStack>) thisList;
		NonNullList<ItemStack> otherCasted = (NonNullList<ItemStack>) otherList;
		if (thisCasted.size() != otherCasted.size()) {
			return false;
		}
		ItemStack a, b;
		for (int i = 0; i < thisCasted.size(); i++) {
			a = thisCasted.get(i);
			b = otherCasted.get(i);
			if (!ItemStack.isSameItemSameTags(a, b)) {
				return false;
			}
		}
		return true;
	}, writer -> {
		//
		NonNullList<ItemStack> list = (NonNullList<ItemStack>) writer.value();
		int size = list.size();
		writer.buf().writeInt(size);
		CompoundTag tag = new CompoundTag();
		ContainerHelper.saveAllItems(tag, list);
		writer.buf().writeNbt(tag);
	}, reader -> {
		//
		int size = reader.buf().readInt();
		NonNullList<ItemStack> toBeFilled = NonNullList.<ItemStack>withSize(size, ItemStack.EMPTY);
		ContainerHelper.loadAllItems(reader.buf().readNbt(), toBeFilled);
		return toBeFilled;
	}, writer -> {
		//
		NonNullList<ItemStack> list = (NonNullList<ItemStack>) writer.prop().get();
		writer.tag().putInt(writer.prop().getName() + "_size", list.size());
		ContainerHelper.saveAllItems(writer.tag(), list);
	}, reader -> {
		//
		int size = reader.tag().getInt(reader.prop().getName() + "_size");
		if (size == 0 || ((NonNullList<ItemStack>) reader.prop().get()).size() != size) {
			return null; // null is handled in function method caller and signals a bad writer.value() to be ignored
		}
		NonNullList<ItemStack> toBeFilled = NonNullList.<ItemStack>withSize(size, ItemStack.EMPTY);
		ContainerHelper.loadAllItems(reader.tag(), toBeFilled);
		return toBeFilled;
	}),
	//
	Fluidstack((thisStack, otherStack) -> {
		FluidStack thisCasted = (FluidStack) thisStack;
		FluidStack otherCasted = (FluidStack) otherStack;
		if (thisCasted.getAmount() != otherCasted.getAmount()) {
			return false;
		}
		return thisCasted.getFluid().isSame(otherCasted.getFluid());
	}, writer -> writer.buf().writeFluidStack((FluidStack) writer.value()), reader -> reader.buf().readFluidStack(), writer -> {
		CompoundTag fluidTag = new CompoundTag();
		((FluidStack) writer.prop().get()).writeToNBT(fluidTag);
		writer.tag().put(writer.prop().getName(), fluidTag);
	}, reader -> FluidStack.loadFluidStackFromNBT(reader.tag().getCompound(reader.prop().getName()))),
	//
	BlockPosList((thisList, otherList) -> {
		List<BlockPos> thisCasted = (List<BlockPos>) thisList;
		List<BlockPos> otherCasted = (List<BlockPos>) otherList;
		if (thisCasted.size() != otherCasted.size()) {
			return false;
		}
		BlockPos a, b;
		for (int i = 0; i < thisCasted.size(); i++) {
			a = thisCasted.get(i);
			b = otherCasted.get(i);
			if (!a.equals(b)) {
				return false;
			}
		}
		return true;
	}, writer -> {
		List<BlockPos> posList = (List<BlockPos>) writer.value();
		writer.buf().writeInt(posList.size());
		posList.forEach(pos -> writer.buf().writeBlockPos(pos));
	}, reader -> {
		List<BlockPos> list = new ArrayList<>();
		int size = reader.buf().readInt();
		for (int i = 0; i < size; i++) {
			list.add(reader.buf().readBlockPos());
		}
		return list;
	}, writer -> {
		List<BlockPos> posList = (List<BlockPos>) writer.prop().get();
		CompoundTag data = new CompoundTag();
		data.putInt("size", posList.size());
		for (int i = 0; i < posList.size(); i++) {
			data.put("pos" + i, NbtUtils.writeBlockPos(posList.get(i)));
		}
		writer.tag().put(writer.prop().getName(), data);
	}, reader -> {
		List<BlockPos> list = new ArrayList<>();
		CompoundTag data = reader.tag().getCompound(reader.prop().getName());
		int size = data.getInt("size");
		for (int i = 0; i < size; i++) {
			list.add(NbtUtils.readBlockPos(data.getCompound("pos" + i)));
		}
		return list;
	}),
	//
	Location(writer -> ((Location) writer.value()).toBuffer(writer.buf()), reader -> electrodynamics.prefab.utilities.object.Location.fromBuffer(reader.buf()), writer -> ((Location) writer.prop().get()).writeToNBT(writer.tag(), writer.prop().getName()), reader -> electrodynamics.prefab.utilities.object.Location.readFromNBT(reader.tag(), reader.prop().getName())),
	//
	Gasstack(writer -> ((GasStack) writer.value()).writeToBuffer(writer.buf()), reader -> GasStack.readFromBuffer(reader.buf()), writer -> writer.tag().put(writer.prop().getName(), ((GasStack) writer.prop().get()).writeToNbt()), reader -> GasStack.readFromNbt(reader.tag().getCompound(reader.prop().getName()))),
	//
	Itemstack((thisStack, otherStack) -> ((ItemStack) thisStack).equals((ItemStack) otherStack, false), writer -> writer.buf().writeItem((ItemStack) writer.value()), reader -> reader.buf().readItem(), writer -> writer.tag().put(writer.prop().getName(), ((ItemStack) writer.prop().get()).save(new CompoundTag())), reader -> ItemStack.of(reader.tag().getCompound(reader.prop().getName()))),
	//
	Block(writer -> {
		writer.buf().writeItem(new ItemStack(((net.minecraft.world.level.block.Block) writer.value()).asItem()));
	}, reader -> {
		ItemStack stack = reader.buf().readItem();
		if (stack.isEmpty()) {
			return Blocks.AIR;
		}
		return ((BlockItem) stack.getItem()).getBlock();
	}, writer -> {
		writer.tag().put(writer.prop().getName(), new ItemStack(((net.minecraft.world.level.block.Block) writer.prop().get()).asItem()).save(new CompoundTag()));
	}, reader -> {
		ItemStack stack = ItemStack.of(reader.tag().getCompound(reader.prop().getName()));
		if (stack.isEmpty()) {
			return Blocks.AIR;
		}
		return ((BlockItem) stack.getItem()).getBlock();
	}),
	//
	Blockstate(writer -> writer.buf().writeNbt(NbtUtils.writeBlockState((BlockState) writer.value())), reader -> NbtUtils.readBlockState(reader.buf().readNbt()), writer -> writer.tag().put(writer.prop().getName(), NbtUtils.writeBlockState((BlockState) writer.prop().get())), reader -> NbtUtils.readBlockState(reader.tag().getCompound(reader.prop().getName()))),
	//
	Transferpack(writer -> ((TransferPack) writer.value()).writeToBuffer(writer.buf()), reader -> TransferPack.readFromBuffer(reader.buf()), writer -> writer.tag().put(writer.prop().getName(), ((TransferPack) writer.prop().get()).writeToTag()), reader -> TransferPack.readFromTag(reader.tag().getCompound(reader.prop().getName()))),
	//
	Vec3(
			//
			writer -> {
				net.minecraft.world.phys.Vec3 val = (net.minecraft.world.phys.Vec3) writer.value();
				writer.buf().writeDouble(val.x);
				writer.buf().writeDouble(val.y);
				writer.buf().writeDouble(val.z);
			}, reader -> {
				return new net.minecraft.world.phys.Vec3(reader.buf().readDouble(), reader.buf().readDouble(), reader.buf().readDouble());
			},
			//
			writer -> net.minecraft.world.phys.Vec3.CODEC.encodeStart(NbtOps.INSTANCE, (net.minecraft.world.phys.Vec3) writer.prop().get()).result().ifPresent(tag -> writer.tag().put(writer.prop().getName(), tag)),
			//
			reader -> net.minecraft.world.phys.Vec3.CODEC.decode(NbtOps.INSTANCE, reader.tag().get(reader.prop().getName())).result().get().getFirst()
	//
	),
	//
	IntegerList(
			//
			(thisList, otherList) -> {
				List<Integer> thisCasted = (List<Integer>) thisList;
				List<Integer> otherCasted = (List<Integer>) otherList;
				if (thisCasted.size() != otherCasted.size()) {
					return false;
				}
				int a, b;
				for (int i = 0; i < thisCasted.size(); i++) {
					a = thisCasted.get(i);
					b = otherCasted.get(i);
					if (a != b) {
						return false;
					}
				}
				return true;
			},
			//
			writer -> {
				List<Integer> list = (List<java.lang.Integer>) writer.value();
				writer.buf().writeInt(list.size());
				for (int i : list) {
					writer.buf().writeInt(i);
				}
			}, reader -> {
				List<Integer> list = new ArrayList<>();
				int size = reader.buf().readInt();
				for (int i = 0; i < size; i++) {
					list.add(reader.buf().readInt());
				}
				return list;
			},
			//
			writer -> {
				List<Integer> list = (List<java.lang.Integer>) writer.prop().get();
				CompoundTag data = new CompoundTag();
				data.putInt("size", list.size());
				for (int i = 0; i < list.size(); i++) {
					data.putInt("" + i, list.get(i));
				}
				writer.tag().put(writer.prop().getName(), data);
			},
			//
			reader -> {
				List<Integer> list = new ArrayList<>();
				CompoundTag data = reader.tag().getCompound(reader.prop().getName());
				int size = data.getInt("size");
				for (int i = 0; i < size; i++) {
					list.add(data.getInt("" + i));
				}
				return list;
			}
	//
	),
	//
	StringList(
			///
			(thisList, otherList) -> {
				List<String> thisCasted = (List<String>) thisList;
				List<String> otherCasted = (List<String>) otherList;
				if (thisCasted.size() != otherCasted.size()) {
					return false;
				}
				String a, b;
				for (int i = 0; i < thisCasted.size(); i++) {
					a = thisCasted.get(i);
					b = otherCasted.get(i);
					if (a != b) {
						return false;
					}
				}
				return true;
			},
			//
			writer -> {
				List<String> list = (List<String>) writer.value();
				writer.buf().writeInt(list.size());
				for (String i : list) {
					writer.buf().writeUtf(i);
				}
			}, reader -> {
				List<String> list = new ArrayList<>();
				int size = reader.buf().readInt();
				for (int i = 0; i < size; i++) {
					list.add(reader.buf().readUtf());
				}
				return list;
			},
			//
			writer -> {
				List<String> list = (List<String>) writer.prop().get();
				CompoundTag data = new CompoundTag();
				data.putInt("size", list.size());
				for (int i = 0; i < list.size(); i++) {
					data.putString("" + i, list.get(i));
				}
				writer.tag().put(writer.prop().getName(), data);
			},
			//
			reader -> {
				List<String> list = new ArrayList<>();
				CompoundTag data = reader.tag().getCompound(reader.prop().getName());
				int size = data.getInt("size");
				for (int i = 0; i < size; i++) {
					list.add(data.getString("" + i));
				}
				return list;
			}
	//
	);

	private final ResourceLocation id;

	@Nonnull
	private final BiPredicate<Object, Object> predicate;

	@Nonnull
	private final Consumer<BufferWriter> writeToBuffer;
	@Nonnull
	private final Function<BufferReader, Object> readFromBuffer;

	@Nonnull
	private final Consumer<TagWriter> writeToNbt;
	@Nonnull
	private final Function<TagReader, Object> readFromNbt;

	@Nonnull
	private final Function<Object, Object> attemptCast;

	private PropertyType(@Nonnull BiPredicate<Object, Object> predicate, @Nonnull Consumer<BufferWriter> writeToBuffer, @Nonnull Function<BufferReader, Object> readFromBuffer, @Nonnull Consumer<TagWriter> writeToNbt, @Nonnull Function<TagReader, Object> readFromNbt) {
		this(predicate, writeToBuffer, readFromBuffer, writeToNbt, readFromNbt, val -> val);
	}

	private PropertyType(@Nonnull Consumer<BufferWriter> writeToBuffer, @Nonnull Function<BufferReader, Object> readFromBuffer, @Nonnull Consumer<TagWriter> writeToNbt, @Nonnull Function<TagReader, Object> readFromNbt) {
		this(writeToBuffer, readFromBuffer, writeToNbt, readFromNbt, val -> val);
	}

	private PropertyType(@Nonnull Consumer<BufferWriter> writeToBuffer, @Nonnull Function<BufferReader, Object> readFromBuffer, @Nonnull Consumer<TagWriter> writeToNbt, @Nonnull Function<TagReader, Object> readFromNbt, @Nonnull Function<Object, Object> attemptCast) {
		this(Object::equals, writeToBuffer, readFromBuffer, writeToNbt, readFromNbt, attemptCast);
	}

	private PropertyType(@Nonnull BiPredicate<Object, Object> predicate, @Nonnull Consumer<BufferWriter> writeToBuffer, @Nonnull Function<BufferReader, Object> readFromBuffer, @Nonnull Consumer<TagWriter> writeToNbt, @Nonnull Function<TagReader, Object> readFromNbt, @Nonnull Function<Object, Object> attemptCast) {
		id = new ResourceLocation(References.ID, name().toLowerCase(Locale.ROOT));
		this.predicate = predicate;
		this.writeToBuffer = writeToBuffer;
		this.readFromBuffer = readFromBuffer;
		this.writeToNbt = writeToNbt;
		this.readFromNbt = readFromNbt;
		this.attemptCast = attemptCast;
	}

	@Override
	public boolean hasChanged(Object currentValue, Object newValue) {
		return predicate.test(currentValue, newValue);
	}

	@Override
	public void writeToBuffer(BufferWriter writer) {
		writeToBuffer.accept(writer);
	}

	@Override
	public Object readFromBuffer(BufferReader reader) {
		return readFromBuffer.apply(reader);
	}

	@Override
	public void writeToTag(TagWriter writer) {
		writeToNbt.accept(writer);
	}

	@Override
	public Object readFromTag(TagReader reader) {
		return readFromNbt.apply(reader);
	}

	@Override
	public Object attemptCast(Object value) {
		return attemptCast.apply(value);
	}

	@Override
	public ResourceLocation getId() {
		return id;
	}

}
