package electrodynamics.prefab.utilities.math;

import org.joml.Quaternionf;
import org.joml.Vector3f;

import electrodynamics.prefab.utilities.object.Location;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class MathUtils {

	public static final Vector3f XN = new Vector3f(-1.0F, 0.0F, 0.0F);
	public static final Vector3f XP = new Vector3f(1.0F, 0.0F, 0.0F);
	public static final Vector3f YN = new Vector3f(0.0F, -1.0F, 0.0F);
	public static final Vector3f YP = new Vector3f(0.0F, 1.0F, 0.0F);
	public static final Vector3f ZN = new Vector3f(0.0F, 0.0F, -1.0F);
	public static final Vector3f ZP = new Vector3f(0.0F, 0.0F, 1.0F);
	public static final Vector3f ZERO = new Vector3f(0.0F, 0.0F, 0.0F);
	public static final Vector3f ONE = new Vector3f(1.0F, 1.0F, 1.0F);

	public static final CollisionContext EMPTY = new EntityCollisionContext(false, -Double.MAX_VALUE, ItemStack.EMPTY, fluidState -> false, null) {
		@Override
		public boolean isAbove(VoxelShape shape, BlockPos pos, boolean canAscend) {
			return canAscend;
		}
	};

	public static Location getRaytracedBlock(Entity entity) {
		return getRaytracedBlock(entity, 100);
	}

	public static Location getRaytracedBlock(Entity entity, double rayLength) {
		return getRaytracedBlock(entity.level(), entity.getLookAngle(), entity.getEyePosition(0), rayLength);
	}

	public static Location getRaytracedBlock(Level world, Vec3 direction, Vec3 from, double rayLength) {
		// Just normalize for safety. Allows the direction
		// vector to be from some block to another with no
		// consideration for more math.
		Vec3 rayPath = direction.normalize().scale(rayLength);
		Vec3 to = from.add(rayPath);
		ClipContext rayContext = new ClipContext(from, to, ClipContext.Block.OUTLINE, ClipContext.Fluid.ANY, EMPTY);
		BlockHitResult rayHit = world.clip(rayContext);

		return rayHit.getType() != Type.BLOCK ? null : new Location(rayHit.getBlockPos());
	}

	public static int logBase2(int value) {
		return (int) (Math.log(value) / Math.log(2) + 1e-10);
	}

	public static int nearestPowerOf10(double value, boolean roundUp) {
		double power = Math.log10(value);
		if(roundUp) {
			return (int) Math.ceil(power);
		} else {
			return (int) Math.floor(power);
		}
	}

	/**
	 * Performs the same function the vanilla class contructor had
	 * 
	 * @param angleX The amount the x axis should rotate
	 * @param angleY The amount the y axis should rotate
	 * @param angleZ The amount the z axis should rotate
	 * @return A rotated quaternion
	 */
	public static Quaternionf rotQuaternionRad(float angleX, float angleY, float angleZ) {
		float f = Mth.sin(0.5F * angleX);
		float f1 = Mth.cos(0.5F * angleX);
		float f2 = Mth.sin(0.5F * angleY);
		float f3 = Mth.cos(0.5F * angleY);
		float f4 = Mth.sin(0.5F * angleZ);
		float f5 = Mth.cos(0.5F * angleZ);
		float i = f * f3 * f5 + f1 * f2 * f4;
		float j = f1 * f2 * f5 - f * f3 * f4;
		float k = f * f2 * f5 + f1 * f3 * f4;
		float r = f1 * f3 * f5 - f * f2 * f4;
		return new Quaternionf(i, j, k, r);
	}

	public static Quaternionf rotQuaternionDeg(float angleX, float angleY, float angleZ) {
		return rotQuaternionRad(angleX / 180.0F * Mth.PI, angleY / 180.0F * Mth.PI, angleZ / 180.0F * Mth.PI);
	}

	/**
	 * 
	 * @param angle The angle to rotate the vector
	 * @param vec   The vector to rotate
	 * @return The resulting quaternion
	 */
	public static Quaternionf rotVectorQuaternionRad(float angle, Vector3f vec) {
		return new Quaternionf().setAngleAxis(angle, vec.x(), vec.y(), vec.z());
	}

	public static Quaternionf rotVectorQuaternionDeg(float angle, Vector3f vec) {
		return rotVectorQuaternionRad(angle / 180.0F * Mth.PI, vec);
	}
}
