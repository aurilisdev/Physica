package electrodynamics.prefab.utilities.object;

import org.joml.Vector3f;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public final class Location {

    public static final Codec<Location> CODEC = RecordCodecBuilder.create(instance ->
                    //
                    instance.group(
                            //
                            Codec.DOUBLE.fieldOf("x").forGetter(Location::x),
                            //
                            Codec.DOUBLE.fieldOf("y").forGetter(Location::y),
                            //
                            Codec.DOUBLE.fieldOf("z").forGetter(Location::z)
//
                    ).apply(instance, Location::new)
//
    );

    public static final StreamCodec<FriendlyByteBuf, Location> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.DOUBLE, Location::x,
            ByteBufCodecs.DOUBLE, Location::y,
            ByteBufCodecs.DOUBLE, Location::z,
            Location::new

    );

    protected double x;
    protected double y;
    protected double z;

    public Location() {
    }

    public Location(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double x() {
        return x;
    }

    public double y() {
        return y;
    }

    public double z() {
        return z;
    }

    public int intX() {
        return (int) Math.floor(x);
    }

    public int intY() {
        return (int) Math.floor(y);
    }

    public int intZ() {
        return (int) Math.floor(z);
    }

    public Location(BlockPos pos) {
        x = pos.getX() + 0.5;
        y = pos.getY() + 0.5;
        z = pos.getZ() + 0.5;
    }

    public Location(Vector3f vec) {
        x = vec.x();
        y = vec.y();
        z = vec.z();
    }

    public Location(Vec3 vec) {
        x = vec.x;
        y = vec.y;
        z = vec.z;
    }

    public Location(Location loc) {
        x = loc.x;
        y = loc.y;
        z = loc.z;
    }

    public Location(Entity entity) {
        x = entity.getX();
        y = entity.getY();
        z = entity.getZ();
    }

    public Location mul(double val) {
        x *= val;
        y *= val;
        z *= val;
        return this;
    }

    public Location mul(double xval, double yval, double zval) {
        x *= xval;
        y *= yval;
        z *= zval;
        return this;
    }

    public Location mul(Location loc) {
        x *= loc.x;
        y *= loc.y;
        z *= loc.z;
        return this;
    }

    public Location set(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public Location set(Location loc) {
        x = loc.x;
        y = loc.y;
        z = loc.z;
        return this;
    }

    public Location add(double x, double y, double z) {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    public Location add(Location loc) {
        x += loc.x;
        y += loc.y;
        z += loc.z;
        return this;
    }

    public Location normalize() {
        double dis = distance(new Location());
        x /= dis;
        y /= dis;
        z /= dis;
        return this;
    }

    public double distanceSq(Location loc) {
        return Math.pow(x - loc.x, 2) + Math.pow(y - loc.y, 2) + Math.pow(z - loc.z, 2);
    }

    public double distance(Location loc) {
        return Math.sqrt(distanceSq(loc));
    }

    public double distancelinear(Location loc) {
        return Math.abs(x - loc.x) + Math.abs(y - loc.y) + Math.abs(z - loc.z);
    }

    public BlockPos toBlockPos() {
        return new BlockPos((int) Math.floor(x), (int) Math.floor(y), (int) Math.floor(z));
    }

    public BlockState getBlockState(BlockGetter reader) {
        return reader.getBlockState(toBlockPos());
    }

    public Block getBlock(BlockGetter reader) {
        return getBlockState(reader).getBlock();
    }

    public BlockEntity getTile(BlockGetter reader) {
        return reader.getBlockEntity(toBlockPos());
    }

    public Location setBlockState(Level world, BlockState state) {
        world.setBlockAndUpdate(toBlockPos(), state);
        return this;
    }

    public Location setBlock(Level world, Block block) {
        return setBlockState(world, block.defaultBlockState());
    }

    public Location setAir(Level world) {
        return setBlock(world, Blocks.AIR);
    }

    public Location setAirFast(Level world) {
        world.setBlock(toBlockPos(), Blocks.AIR.defaultBlockState(), 2 | 16);
        return this;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp = Double.doubleToLongBits(x);
        result = prime * result + (int) (temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(y);
        result = prime * result + (int) (temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(z);
        return prime * result + (int) (temp ^ temp >>> 32);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Location other = (Location) obj;
        return Double.doubleToLongBits(x) == Double.doubleToLongBits(other.x) && Double.doubleToLongBits(y) == Double.doubleToLongBits(other.y) && Double.doubleToLongBits(z) == Double.doubleToLongBits(other.z);
    }

    @Override
    public String toString() {
        return "[" + intX() + ", " + intY() + ", " + intZ() + "]";
    }

    public static Location readFromNBT(CompoundTag nbt, String name) {
        return new Location(nbt.getDouble(name + "X"), nbt.getDouble(name + "Y"), nbt.getDouble(name + "Z"));
    }

    public void writeToNBT(CompoundTag nbt, String name) {
        nbt.putDouble(name + "X", x);
        nbt.putDouble(name + "Y", y);
        nbt.putDouble(name + "Z", z);
    }

    public void toBuffer(FriendlyByteBuf buffer) {
        STREAM_CODEC.encode(buffer, this);
    }

    public static Location fromBuffer(FriendlyByteBuf buffer) {
        return STREAM_CODEC.decode(buffer);
    }
}
