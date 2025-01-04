package electrodynamics.common.tile.machines.quarry;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.mojang.datafixers.util.Pair;

import electrodynamics.client.render.event.levelstage.HandlerQuarryArm;
import electrodynamics.common.item.subtype.SubtypeDrillHead;
import electrodynamics.prefab.utilities.BlockEntityUtils;
import electrodynamics.prefab.utilities.math.PrecisionVector;
import electrodynamics.prefab.utilities.object.Location;
import electrodynamics.prefab.utilities.object.QuarryArmDataHolder;
import electrodynamics.prefab.utilities.object.QuarryArmFrameWrapper;
import electrodynamics.prefab.utilities.object.QuarryWheelDataHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;

/**
 * Handles creation of all required AABBs and render data that is then used in the {@link HandlerQuarryArm} renderer
 * 
 * @author skip999
 *
 */
public class QuarryRenderManger {

	private QuarryArmFrameWrapper currentFrame = null;
	private boolean onRight = false;

	public QuarryRenderManger() {
	}

	public void render(TileQuarry quarry) {
		BlockPos pos = quarry.getBlockPos();
		HandlerQuarryArm.removeRenderData(pos);
		if (!quarry.hasCorners() || quarry.miningPos.get() == null || quarry.miningPos.get().equals(BlockEntityUtils.OUT_OF_REACH)) {
			return;
		}
		onRight = quarry.cornerOnRight.get();
		currentFrame = getCurrentFrame(quarry);
		if (currentFrame == null) {
			return;
		}

		BlockPos start = quarry.corners.get().get(3);
		BlockPos end = quarry.corners.get().get(0);

		double widthLeft, widthRight, widthTop, widthBottom;
		AABB headAabb = null;
		PrecisionVector headPos = null;

		QuarryWheelDataHolder leftWheel = null;
		QuarryWheelDataHolder rightWheel = null;
		QuarryWheelDataHolder topWheel = null;
		QuarryWheelDataHolder bottomWheel = null;

		List<Pair<PrecisionVector, AABB>> lightSegments = new ArrayList<>();
		List<Pair<PrecisionVector, AABB>> darkSegments = new ArrayList<>();
		List<Pair<PrecisionVector, AABB>> titanium = new ArrayList<>();

		double x = currentFrame.frame().x();
		double z = currentFrame.frame().z();
		double y = start.getY() + 0.5;

		/* Vertical Arm Segment */

		double deltaY = start.getY() - currentFrame.frame().y() - 1.2;

		vertical(x, y, z, deltaY, darkSegments, lightSegments, titanium);

		SubtypeDrillHead headType = quarry.readHeadType();

		if (headType != null) {
			headPos = new PrecisionVector(x, y - deltaY, z);
			headAabb = new AABB(0.3125, -0.2, 0.3125, 0.6875, -0.5 - 0.2, 0.6875);
		}

		/* Horizontal Arm Segment */

		Direction facing = quarry.getFacing().getOpposite();

		int wholeWidthLeft, wholeWidthRight, wholeWidthTop, wholeWidthBottom;
		double remainderWidthLeft, remainderWidthRight, remainderWidthTop, remainderWidthBottom;

		int[] markerLineSigns = new int[] { 1, 1, -1, -1 };

		switch (facing) {

		case NORTH, SOUTH:

			widthLeft = x - start.getX();
			widthRight = x - end.getX();
			widthTop = z - start.getZ();
			widthBottom = z - end.getZ();

			wholeWidthLeft = (int) (widthLeft / 1.0);
			remainderWidthLeft = widthLeft - wholeWidthLeft;

			wholeWidthRight = (int) (widthRight / 1.0);
			remainderWidthRight = widthRight - wholeWidthRight;

			wholeWidthBottom = (int) (widthBottom / 1.0);
			remainderWidthBottom = widthBottom - wholeWidthBottom;

			wholeWidthTop = (int) (widthTop / 1.0);
			remainderWidthTop = widthTop - wholeWidthTop;

			if (facing == Direction.SOUTH) {
				south(x, y, z, darkSegments, lightSegments, widthLeft, widthRight, widthTop, widthBottom, wholeWidthLeft, wholeWidthRight, wholeWidthTop, wholeWidthBottom, remainderWidthLeft, remainderWidthRight, remainderWidthTop, remainderWidthBottom);

				if (onRight) {
					leftWheel = new QuarryWheelDataHolder(new PrecisionVector(x - widthLeft + 0.5, y, z + 0.5), 0, currentFrame.degrees() * currentFrame.deltaZ(), 0);

					rightWheel = new QuarryWheelDataHolder(new PrecisionVector(x - widthRight + 0.5, y, z + 0.5), 180, currentFrame.degrees() * -currentFrame.deltaZ(), 0);

				} else {
					leftWheel = new QuarryWheelDataHolder(new PrecisionVector(x - widthLeft + 0.5, y, z + 0.5), 180, currentFrame.degrees() * -currentFrame.deltaZ(), 0);

					rightWheel = new QuarryWheelDataHolder(new PrecisionVector(x - widthRight + 0.5, y, z + 0.5), 0, currentFrame.degrees() * currentFrame.deltaZ(), 0);

				}

				bottomWheel = new QuarryWheelDataHolder(new PrecisionVector(x + 0.5, y, z - widthBottom + 0.5), 270, currentFrame.degrees() * -currentFrame.deltaX(), 0);

				topWheel = new QuarryWheelDataHolder(new PrecisionVector(x + 0.5, y, z - widthTop + 0.5), 90, currentFrame.degrees() * currentFrame.deltaX(), 0);

			} else {

				north(x, y, z, darkSegments, lightSegments, widthLeft, widthRight, widthTop, widthBottom, wholeWidthLeft, wholeWidthRight, wholeWidthTop, wholeWidthBottom, remainderWidthLeft, remainderWidthRight, remainderWidthTop, remainderWidthBottom);

				if (onRight) {
					leftWheel = new QuarryWheelDataHolder(new PrecisionVector(x - widthLeft + 0.5, y, z + 0.5), 180, currentFrame.degrees() * -currentFrame.deltaZ(), 0);

					rightWheel = new QuarryWheelDataHolder(new PrecisionVector(x - widthRight + 0.5, y, z + 0.5), 0, currentFrame.degrees() * currentFrame.deltaZ(), 0);

				} else {
					leftWheel = new QuarryWheelDataHolder(new PrecisionVector(x - widthLeft + 0.5, y, z + 0.5), 0, currentFrame.degrees() * currentFrame.deltaZ(), 0);

					rightWheel = new QuarryWheelDataHolder(new PrecisionVector(x - widthRight + 0.5, y, z + 0.5), 180, currentFrame.degrees() * -currentFrame.deltaZ(), 0);

				}

				bottomWheel = new QuarryWheelDataHolder(new PrecisionVector(x + 0.5, y, z - widthBottom + 0.5), 90, currentFrame.degrees() * currentFrame.deltaX(), 0);

				topWheel = new QuarryWheelDataHolder(new PrecisionVector(x + 0.5, y, z - widthTop + 0.5), 270, currentFrame.degrees() * -currentFrame.deltaX(), 0);

			}

			break;
		case EAST, WEST:

			widthLeft = z - start.getZ();
			widthRight = z - end.getZ();
			widthTop = x - start.getX();
			widthBottom = x - end.getX();

			wholeWidthLeft = (int) (widthLeft / 1.0);
			remainderWidthLeft = widthLeft - wholeWidthLeft;

			wholeWidthRight = (int) (widthRight / 1.0);
			remainderWidthRight = widthRight - wholeWidthRight;

			wholeWidthBottom = (int) (widthBottom / 1.0);
			remainderWidthBottom = widthBottom - wholeWidthBottom;

			wholeWidthTop = (int) (widthTop / 1.0);
			remainderWidthTop = widthTop - wholeWidthTop;

			if (facing == Direction.WEST) {
				west(x, y, z, darkSegments, lightSegments, widthLeft, widthRight, widthTop, widthBottom, wholeWidthLeft, wholeWidthRight, wholeWidthTop, wholeWidthBottom, remainderWidthLeft, remainderWidthRight, remainderWidthTop, remainderWidthBottom);

				if (onRight) {
					leftWheel = new QuarryWheelDataHolder(new PrecisionVector(x + 0.5, y, z - widthLeft + 0.5), 270, currentFrame.degrees() * -currentFrame.deltaX(), 0);

					rightWheel = new QuarryWheelDataHolder(new PrecisionVector(x + 0.5, y, z - widthRight + 0.5), 90, currentFrame.degrees() * currentFrame.deltaX(), 0);

				} else {
					leftWheel = new QuarryWheelDataHolder(new PrecisionVector(x + 0.5, y, z - widthLeft + 0.5), 90, currentFrame.degrees() * currentFrame.deltaX(), 0);

					rightWheel = new QuarryWheelDataHolder(new PrecisionVector(x + 0.5, y, z - widthRight + 0.5), 270, currentFrame.degrees() * -currentFrame.deltaX(), 0);

				}

				bottomWheel = new QuarryWheelDataHolder(new PrecisionVector(x - widthBottom + 0.5, y, z + 0.5), 180, currentFrame.degrees() * -currentFrame.deltaZ(), 0);

				topWheel = new QuarryWheelDataHolder(new PrecisionVector(x - widthTop + 0.5, y, z + 0.5), 0, currentFrame.degrees() * currentFrame.deltaZ(), 0);

			} else {
				east(x, y, z, darkSegments, lightSegments, widthLeft, widthRight, widthTop, widthBottom, wholeWidthLeft, wholeWidthRight, wholeWidthTop, wholeWidthBottom, remainderWidthLeft, remainderWidthRight, remainderWidthTop, remainderWidthBottom);

				if (onRight) {
					leftWheel = new QuarryWheelDataHolder(new PrecisionVector(x + 0.5, y, z - widthLeft + 0.5), 90, currentFrame.degrees() * currentFrame.deltaX(), 0);

					rightWheel = new QuarryWheelDataHolder(new PrecisionVector(x + 0.5, y, z - widthRight + 0.5), 270, currentFrame.degrees() * -currentFrame.deltaX(), 0);

				} else {
					leftWheel = new QuarryWheelDataHolder(new PrecisionVector(x + 0.5, y, z - widthLeft + 0.5), 270, currentFrame.degrees() * -currentFrame.deltaX(), 0);

					rightWheel = new QuarryWheelDataHolder(new PrecisionVector(x + 0.5, y, z - widthRight + 0.5), 90, currentFrame.degrees() * currentFrame.deltaX(), 0);

				}

				bottomWheel = new QuarryWheelDataHolder(new PrecisionVector(x - widthBottom + 0.5, y, z + 0.5), 0, currentFrame.degrees() * currentFrame.deltaZ(), 0);

				topWheel = new QuarryWheelDataHolder(new PrecisionVector(x - widthTop + 0.5, y, z + 0.5), 180, currentFrame.degrees() * -currentFrame.deltaZ(), 0);

			}

			break;
		default:
			break;
		}
		HandlerQuarryArm.addRenderData(pos, new QuarryArmDataHolder(lightSegments, darkSegments, titanium, Pair.of(headPos, headAabb), headType, leftWheel, rightWheel, topWheel, bottomWheel, quarry.running.get(), quarry.progressCounter.get(), quarry.speed.get(), quarry.corners.get(), markerLineSigns));

	}

	private void vertical(double x, double y, double z, double deltaY, List<Pair<PrecisionVector, AABB>> darkSegments, List<Pair<PrecisionVector, AABB>> lightSegments, List<Pair<PrecisionVector, AABB>> titanium) {
		int i = 0;
		/* Vertical Arm Segment */

		PrecisionVector pos = null;
		int wholeSegmentCount = (int) (deltaY / 1.0);
		double remainder = deltaY / 1.0 - wholeSegmentCount;
		for (i = 0; i < wholeSegmentCount; i++) {
			pos = new PrecisionVector(x, y - i * 1.0 - 0.3125, z);
			// vertical lines
			lightSegments.add(Pair.of(pos, new AABB(0.25, 0, 0.25, 0.3125, -1.0, 0.3125)));
			lightSegments.add(Pair.of(pos, new AABB(0.25, 0, 0.6875, 0.3125, -1.0, 0.75)));
			lightSegments.add(Pair.of(pos, new AABB(0.6875, 0, 0.25, 0.75, -1.0, 0.3125)));
			lightSegments.add(Pair.of(pos, new AABB(0.6875, 0, 0.6875, 0.75, -1.0, 0.75)));

			// horizontal lines
			lightSegments.add(Pair.of(pos, new AABB(0.25, -0.5 - 0.03125, 0.3125, 0.3125, -0.5 + 0.03125, 0.6875)));
			lightSegments.add(Pair.of(pos, new AABB(0.3125, -0.5 - 0.03125, 0.6875, 0.6875, -0.5 + 0.03125, 0.75)));
			lightSegments.add(Pair.of(pos, new AABB(0.6875, -0.5 - 0.03125, 0.3125, 0.75, -0.5 + 0.03125, 0.6875)));
			lightSegments.add(Pair.of(pos, new AABB(0.3125, -0.5 - 0.03125, 0.25, 0.6875, -0.5 + 0.03125, 0.3125)));

			// cylinder
			darkSegments.add(Pair.of(pos, new AABB(0.375, 0, 0.375, 0.625, -1.0, 0.625)));
		}
		int wholeOffset = wholeSegmentCount;
		pos = new PrecisionVector(x, y - wholeOffset, z);
		if (remainder > 0) {
			// vertical lines
			lightSegments.add(Pair.of(pos, new AABB(0.25, -0.3125, 0.25, 0.3125, -1.0 * remainder, 0.3125)));
			lightSegments.add(Pair.of(pos, new AABB(0.25, -0.3125, 0.6875, 0.3125, -1.0 * remainder, 0.75)));
			lightSegments.add(Pair.of(pos, new AABB(0.6875, -0.3125, 0.25, 0.75, -1.0 * remainder, 0.3125)));
			lightSegments.add(Pair.of(pos, new AABB(0.6875, -0.3125, 0.6875, 0.75, -1.0 * remainder, 0.75)));
			// cylinder
			darkSegments.add(Pair.of(pos, new AABB(0.375, -0.3125, 0.375, 0.625, -1.0 * remainder, 0.625)));
			// horizontal lines
			if (remainder > 0.5) {
				lightSegments.add(Pair.of(pos, new AABB(0.25, -0.75 - 0.03125, 0.3125, 0.3125, -0.75 + 0.03125, 0.6875)));
				lightSegments.add(Pair.of(pos, new AABB(0.3125, -0.75 - 0.03125, 0.6875, 0.6875, -0.75 + 0.03125, 0.75)));
				lightSegments.add(Pair.of(pos, new AABB(0.6875, -0.75 - 0.03125, 0.3125, 0.75, -0.75 + 0.03125, 0.6875)));
				lightSegments.add(Pair.of(pos, new AABB(0.3125, -0.75 - 0.03125, 0.25, 0.6875, -0.75 + 0.03125, 0.3125)));
			}
		}

		titanium.add(Pair.of(new PrecisionVector(x, y, z), new AABB(0.1875, 0.325, 0.1875, 0.8125, -0.325, 0.8125)));
		if (remainder > 0) {
			titanium.add(Pair.of(new PrecisionVector(x, y - deltaY, z), new AABB(0.20, 0, 0.20, 0.8, -0.2, 0.8)));
		}

	}

	private void north(double x, double y, double z, List<Pair<PrecisionVector, AABB>> darkSegments, List<Pair<PrecisionVector, AABB>> lightSegments, double widthLeft, double widthRight, double widthTop, double widthBottom, int wholeWidthLeft, int wholeWidthRight, int wholeWidthTop, int wholeWidthBottom, double remainderWidthLeft, double remainderWidthRight, double remainderWidthTop, double remainderWidthBottom) {

		if (onRight) {
			northOnRight(x, y, z, darkSegments, lightSegments, widthLeft, widthRight, wholeWidthLeft, wholeWidthRight, remainderWidthLeft, remainderWidthRight);
		} else {
			northOnLeft(x, y, z, darkSegments, lightSegments, widthLeft, widthRight, wholeWidthLeft, wholeWidthRight, remainderWidthLeft, remainderWidthRight);
		}

		/* BOTTOM */

		int i = 0;
		PrecisionVector pos = null;
		int removal = remainderWidthBottom > -0.625 ? 1 : 0;
		while (i > wholeWidthBottom + removal) {
			pos = new PrecisionVector(x, y, z - widthBottom + 1.0 * i + 0.1875);
			// horizontal lines
			lightSegments.add(Pair.of(pos, new AABB(0.25, -0.25, 0, 0.3125, -0.1875, -1.0)));
			lightSegments.add(Pair.of(pos, new AABB(0.25, 0.1875, 0, 0.3125, 0.25, -1.0)));
			lightSegments.add(Pair.of(pos, new AABB(0.6875, -0.25, 0, 0.75, -0.1875, -1.0)));
			lightSegments.add(Pair.of(pos, new AABB(0.6875, 0.1875, 0, 0.75, 0.25, -1.0)));
			// center
			darkSegments.add(Pair.of(pos, new AABB(0.375, -0.125, 0, 0.625, 0.125, -1.0)));
			// vertical lines
			lightSegments.add(Pair.of(pos, new AABB(0.25, -0.1875, -1.0 + 0.0625, 0.3125, 0.1875, -1.0 + 0)));
			lightSegments.add(Pair.of(pos, new AABB(0.6875, -0.1875, -1.0 + 0.0625, 0.75, 0.1875, -1.0 + 0)));
			lightSegments.add(Pair.of(pos, new AABB(0.3125, -0.25, -1.0 + 0.0625, 0.6875, -0.1875, -1.0 + 0)));
			lightSegments.add(Pair.of(pos, new AABB(0.3125, 0.1875, -1.0 + 0.0625, 0.6875, 0.25, -1.0 + 0)));

			i--;
		}
		pos = new PrecisionVector(x, y, z);

		if (removal == 1) {
			// horizontal lines
			lightSegments.add(Pair.of(pos, new AABB(0.25, -0.25, -remainderWidthBottom + 1.0 + 0.1875, 0.3125, -0.1875, 0.8125)));
			lightSegments.add(Pair.of(pos, new AABB(0.25, 0.1875, -remainderWidthBottom + 1.0 + 0.1875, 0.3125, 0.25, 0.8125)));
			lightSegments.add(Pair.of(pos, new AABB(0.6875, -0.25, -remainderWidthBottom + 1.0 + 0.1875, 0.75, -0.1875, 0.8125)));
			lightSegments.add(Pair.of(pos, new AABB(0.6875, 0.1875, -remainderWidthBottom + 1.0 + 0.1875, 0.75, 0.25, 0.8125)));
			// center
			darkSegments.add(Pair.of(pos, new AABB(0.375, -0.125, -remainderWidthBottom + 1.0 + 0.1875, 0.625, 0.125, 0.8125)));
			// vertical lines
			if (remainderWidthBottom < -0.375) {
				pos = new PrecisionVector(x, y, z - remainderWidthBottom - 1.0 + 0.1875);
				lightSegments.add(Pair.of(pos, new AABB(0.25, -0.1875, 1, 0.3125, 0.1875, 1.0625)));
				lightSegments.add(Pair.of(pos, new AABB(0.6875, -0.1875, 1, 0.75, 0.1875, 1.0625)));
				lightSegments.add(Pair.of(pos, new AABB(0.3125, -0.25, 1, 0.6875, -0.1875, 1.0625)));
				lightSegments.add(Pair.of(pos, new AABB(0.3125, 0.1875, 1, 0.6875, 0.25, 1.0625)));

			}

		} else {
			// horizontal lines
			lightSegments.add(Pair.of(pos, new AABB(0.25, -0.25, -remainderWidthBottom + 0.1875, 0.3125, -0.1875, 0.8125)));
			lightSegments.add(Pair.of(pos, new AABB(0.25, 0.1875, -remainderWidthBottom + 0.1875, 0.3125, 0.25, 0.8125)));
			lightSegments.add(Pair.of(pos, new AABB(0.6875, -0.25, -remainderWidthBottom + 0.1875, 0.75, -0.1875, 0.8125)));
			lightSegments.add(Pair.of(pos, new AABB(0.6875, 0.1875, -remainderWidthBottom + 0.1875, 0.75, 0.25, 0.8125)));
			// center
			darkSegments.add(Pair.of(pos, new AABB(0.375, -0.125, -remainderWidthBottom + 0.1875, 0.625, 0.125, 0.8125)));
			// vertical lines
			/*
			 * lightSegments.add(Pair.of(pos, new AABB(0.25, -0.1875, -remainderWidthBottom - 1.0 + 0.46875, 0.3125, 0.1875, -remainderWidthBottom - 1.0 + 0.53125))); lightSegments.add(Pair.of(pos, new AABB(0.6875, -0.1875, -remainderWidthBottom - 1.0 + 0.46875, 0.75, 0.1875, -remainderWidthBottom - 1.0 + + 0.53125))); lightSegments.add(Pair.of(pos, new AABB(0.3125, -0.25, -remainderWidthBottom - 1.0 + 0.46875, 0.6875, -0.1875, -remainderWidthBottom - 1.0 + 0.53125))); lightSegments.add(Pair.of(pos, new AABB(0.3125, 0.1875, -remainderWidthBottom - 1.0 + 0.46875, 0.6875, 0.25, -remainderWidthBottom - 1.0 + 0.53125)));
			 */
		}

		/* TOP */

		removal = remainderWidthTop < 0.625 ? 1 : 0;
		i = 0;

		while (i < wholeWidthTop - removal) {
			pos = new PrecisionVector(x, y, z - widthTop + 1.0 * i + 0.8125);
			// horizontal lines
			lightSegments.add(Pair.of(pos, new AABB(0.25, -0.25, 1.0, 0.3125, -0.1875, 0)));
			lightSegments.add(Pair.of(pos, new AABB(0.25, 0.1875, 1.0, 0.3125, 0.25, 0)));
			lightSegments.add(Pair.of(pos, new AABB(0.6875, -0.25, 1.0, 0.75, -0.1875, 0)));
			lightSegments.add(Pair.of(pos, new AABB(0.6875, 0.1875, 1.0, 0.75, 0.25, 0)));
			// center
			darkSegments.add(Pair.of(pos, new AABB(0.3875, -0.125, 1.0, 0.625, 0.125, 0)));
			// vertical lines
			lightSegments.add(Pair.of(pos, new AABB(0.25, -0.1875, 0.9375, 0.3125, 0.1875, 1)));
			lightSegments.add(Pair.of(pos, new AABB(0.6875, -0.1875, 0.9375, 0.75, 0.1875, 1)));
			lightSegments.add(Pair.of(pos, new AABB(0.3125, -0.25, 0.9375, 0.6875, -0.1875, 1)));
			lightSegments.add(Pair.of(pos, new AABB(0.3125, 0.1875, 0.9375, 0.6875, 0.25, 1)));

			i++;
		}
		pos = new PrecisionVector(x, y, z);
		if (removal == 1) {

			// horizontal lines
			lightSegments.add(Pair.of(pos, new AABB(0.25, -0.25, 0.8125 - 1.0 - remainderWidthTop, 0.3125, -0.1875, 0.1875)));
			lightSegments.add(Pair.of(pos, new AABB(0.6875, -0.25, 0.8125 - 1.0 - remainderWidthTop, 0.75, -0.1875, 0.1875)));
			lightSegments.add(Pair.of(pos, new AABB(0.25, 0.1875, 0.8125 - 1.0 - remainderWidthTop, 0.3125, 0.25, 0.1875)));
			lightSegments.add(Pair.of(pos, new AABB(0.6875, 0.1875, 0.8125 - 1.0 - remainderWidthTop, 0.75, 0.25, 0.1875)));
			// center
			darkSegments.add(Pair.of(pos, new AABB(0.3875, -0.125, 0.8125 - 1.0 - remainderWidthTop, 0.625, 0.125, 0.1875)));
			// horizontal lines
			if (remainderWidthTop > 0.5) {
				pos = new PrecisionVector(x, y, z - remainderWidthTop - 1.0 + 0.8125);
				lightSegments.add(Pair.of(pos, new AABB(0.25, -0.1875, 0.9375, 0.3125, 0.1875, 1)));
				lightSegments.add(Pair.of(pos, new AABB(0.6875, -0.1875, 0.9375, 0.75, 0.1875, 1)));
				lightSegments.add(Pair.of(pos, new AABB(0.3125, -0.25, 0.9375, 0.6875, -0.1875, 1)));
				lightSegments.add(Pair.of(pos, new AABB(0.3125, 0.1875, 0.9375, 0.6875, 0.25, 1)));

			}

		} else {

			// horizontal lines
			lightSegments.add(Pair.of(pos, new AABB(0.25, -0.25, 0.1875, 0.3125, -0.1875, 0.8125 - remainderWidthTop)));
			lightSegments.add(Pair.of(pos, new AABB(0.6875, -0.25, 0.1875, 0.75, -0.1875, 0.8125 - remainderWidthTop)));
			lightSegments.add(Pair.of(pos, new AABB(0.25, 0.1875, 0.1875, 0.3125, 0.25, 0.8125 - remainderWidthTop)));
			lightSegments.add(Pair.of(pos, new AABB(0.6875, 0.1875, 0.1875, 0.75, 0.25, 0.8125 - remainderWidthTop)));
			// center
			darkSegments.add(Pair.of(pos, new AABB(0.3875, -0.125, 0.1875, 0.625, 0.125, 0.8125 - remainderWidthTop)));
			// vertical lines
			/*
			 * darkSegments.add(Pair.of(pos, new AABB(0.25, -0.1875, -remainderWidthTop + (0.8125 + 1), 0.3125, 0.1875, -remainderWidthTop + (0.8125 + 1.0625)))); lightSegments.add(Pair.of(pos, new AABB(0.6875, -0.1875, -remainderWidthTop + (0.8125 + 0.468765), 0.75, 0.1875, -remainderWidthTop + (0.8125 + 0.53215)))); lightSegments.add(Pair.of(pos, new AABB(0.3125, -0.25, -remainderWidthTop + (0.8125 + 0.468765), 0.6875, -0.1875, -remainderWidthTop + (0.8125 + 0.53215)))); lightSegments.add(Pair.of(pos, new AABB(0.3125, 0.1875, -remainderWidthTop + (0.8125 + 0.468765), 0.6875, 0.25, -remainderWidthTop + (0.8125 + 0.53215))));
			 */
		}

	}

	private void northOnRight(double x, double y, double z, List<Pair<PrecisionVector, AABB>> darkSegments, List<Pair<PrecisionVector, AABB>> lightSegments, double widthLeft, double widthRight, int wholeWidthLeft, int wholeWidthRight, double remainderWidthLeft, double remainderWidthRight) {
		/* LEFT */

		int i = 1;
		PrecisionVector pos = null;
		while (i > wholeWidthLeft + 2) {
			pos = new PrecisionVector(x - widthLeft + 1.0 * i - 0.8125, y, z);
			// horizontal lines
			lightSegments.add(Pair.of(pos, new AABB(-1.0, 0.25, 0.25, 0, 0.1875, 0.3125)));
			lightSegments.add(Pair.of(pos, new AABB(-1.0, 0.25, 0.6875, 0, 0.1875, 0.75)));
			lightSegments.add(Pair.of(pos, new AABB(-1.0, -0.25, 0.25, 0, -0.1875, 0.3125)));
			lightSegments.add(Pair.of(pos, new AABB(-1.0, -0.25, 0.6875, 0, -0.1875, 0.75)));
			// center
			darkSegments.add(Pair.of(pos, new AABB(-1.0, 0.125, 0.375, 0, -0.125, 0.625)));
			// vertical lines
			lightSegments.add(Pair.of(pos, new AABB(-1.0 + 0.0625, -0.1875, 0.25, -1.0 + 0, 0.1875, 0.3125)));
			lightSegments.add(Pair.of(pos, new AABB(-1.0 + 0.0625, -0.1875, 0.6875, -1.0 + 0, 0.1875, 0.75)));
			lightSegments.add(Pair.of(pos, new AABB(-1.0 + 0.0625, -0.25, 0.3125, -1.0 + 0, -0.1875, 0.6875)));
			lightSegments.add(Pair.of(pos, new AABB(-1.0 + 0.0625, 0.1875, 0.3125, -1.0 + 0, 0.25, 0.6875)));

			i--;
		}
		pos = new PrecisionVector(x, y, z);
		// horizontal lines
		lightSegments.add(Pair.of(pos, new AABB(-remainderWidthLeft + 1.0 + 0.1875, 0.25, 0.25, 0.8125, 0.1875, 0.3125)));
		lightSegments.add(Pair.of(pos, new AABB(-remainderWidthLeft + 1.0 + 0.1875, 0.25, 0.6875, 0.8125, 0.1875, 0.75)));
		lightSegments.add(Pair.of(pos, new AABB(-remainderWidthLeft + 1.0 + 0.1875, -0.25, 0.25, 0.8125, -0.1875, 0.3125)));
		lightSegments.add(Pair.of(pos, new AABB(-remainderWidthLeft + 1.0 + 0.1875, -0.25, 0.6875, 0.8125, -0.1875, 0.75)));
		// center
		darkSegments.add(Pair.of(pos, new AABB(-remainderWidthLeft + 1.0 + 0.1875, 0.125, 0.375, 0.8125, -0.125, 0.625)));
		if (remainderWidthLeft < -0.5) {
			pos = new PrecisionVector(x - remainderWidthLeft + 1.0 + 0.1875, y, z);
			// vertical lines
			lightSegments.add(Pair.of(pos, new AABB(-0.9375, -0.1875, 0.25, -1, 0.1875, 0.3125)));
			lightSegments.add(Pair.of(pos, new AABB(-0.9375, -0.1875, 0.6875, -1, 0.1875, 0.75)));
			lightSegments.add(Pair.of(pos, new AABB(-0.9375, -0.25, 0.3125, -1, -0.1875, 0.6875)));
			lightSegments.add(Pair.of(pos, new AABB(-0.9375, 0.1875, 0.3125, -1, 0.25, 0.6875)));
		}

		/* RIGHT */

		i = 0;
		while (i < wholeWidthRight - 1) {
			pos = new PrecisionVector(x - widthRight + 1.0 * i + 0.8125, y, z);
			// horizontal lines
			lightSegments.add(Pair.of(pos, new AABB(0, 0.25, 0.25, 1.0, 0.1875, 0.3125)));
			lightSegments.add(Pair.of(pos, new AABB(0, 0.25, 0.6875, 1.0, 0.1875, 0.75)));
			lightSegments.add(Pair.of(pos, new AABB(0, -0.25, 0.25, 1.0, -0.1875, 0.3125)));
			lightSegments.add(Pair.of(pos, new AABB(0, -0.25, 0.6875, 1.0, -0.1875, 0.75)));
			// center
			darkSegments.add(Pair.of(pos, new AABB(0, 0.125, 0.375, 1.0, -0.125, 0.625)));
			// vertical lines
			lightSegments.add(Pair.of(pos, new AABB(0.9375, -0.1875, 0.25, 1, 0.1875, 0.3125)));
			lightSegments.add(Pair.of(pos, new AABB(0.9375, -0.1875, 0.6875, 1, 0.1875, 0.75)));
			lightSegments.add(Pair.of(pos, new AABB(0.9375, -0.25, 0.3125, 1, -0.1875, 0.6875)));
			lightSegments.add(Pair.of(pos, new AABB(0.9375, 0.1875, 0.3125, 1, 0.25, 0.6875)));

			i++;
		}
		pos = new PrecisionVector(x, y, z);
		// horizontal lines
		lightSegments.add(Pair.of(pos, new AABB(-remainderWidthRight - 1.0 + 0.8125, 0.25, 0.25, 0.1875, 0.1875, 0.3125)));
		lightSegments.add(Pair.of(pos, new AABB(-remainderWidthRight - 1.0 + 0.8125, 0.25, 0.6875, 0.1875, 0.1875, 0.75)));
		lightSegments.add(Pair.of(pos, new AABB(-remainderWidthRight - 1.0 + 0.8125, -0.25, 0.25, 0.1875, -0.1875, 0.3125)));
		lightSegments.add(Pair.of(pos, new AABB(-remainderWidthRight - 1.0 + 0.8125, -0.25, 0.6875, 0.1875, -0.1875, 0.75)));
		// center
		darkSegments.add(Pair.of(pos, new AABB(-remainderWidthRight - 1.0 + 0.8125, 0.125, 0.375, 0.1875, -0.125, 0.625)));
		// vertical lines
		if (remainderWidthRight > 0.5) {
			pos = new PrecisionVector(x - remainderWidthRight - 1.0 + 0.8125, y, z);
			lightSegments.add(Pair.of(pos, new AABB(0.9375, -0.1875, 0.25, 1, 0.1875, 0.3125)));
			lightSegments.add(Pair.of(pos, new AABB(0.9375, -0.1875, 0.6875, 1, 0.1875, 0.75)));
			lightSegments.add(Pair.of(pos, new AABB(0.9375, -0.25, 0.3125, 1, -0.1875, 0.6875)));
			lightSegments.add(Pair.of(pos, new AABB(0.9375, 0.1875, 0.3125, 1, 0.25, 0.6875)));

		}

	}

	private void northOnLeft(double x, double y, double z, List<Pair<PrecisionVector, AABB>> darkSegments, List<Pair<PrecisionVector, AABB>> lightSegments, double widthLeft, double widthRight, int wholeWidthLeft, int wholeWidthRight, double remainderWidthLeft, double remainderWidthRight) {
		/* LEFT */

		int i = wholeWidthLeft - 1;
		PrecisionVector pos = null;
		while (i > 0) {
			pos = new PrecisionVector(x - widthLeft + 1.0 * i - 0.1875, y, z);
			// horizontal lines
			lightSegments.add(Pair.of(pos, new AABB(0, 0.25, 0.25, 1.0, 0.1875, 0.3125)));
			lightSegments.add(Pair.of(pos, new AABB(0, 0.25, 0.6875, 1.0, 0.1875, 0.75)));
			lightSegments.add(Pair.of(pos, new AABB(0, -0.25, 0.25, 1.0, -0.1875, 0.3125)));
			lightSegments.add(Pair.of(pos, new AABB(0, -0.25, 0.6875, 1.0, -0.1875, 0.75)));
			// center
			darkSegments.add(Pair.of(pos, new AABB(0, 0.125, 0.375, 1.0, -0.125, 0.625)));
			// vertical lines
			lightSegments.add(Pair.of(pos, new AABB(1.0 - 0.0625, -0.1875, 0.25, 1.0 - 0, 0.1875, 0.3125)));
			lightSegments.add(Pair.of(pos, new AABB(1.0 - 0.0625, -0.1875, 0.6875, 1.0 - 0, 0.1875, 0.75)));
			lightSegments.add(Pair.of(pos, new AABB(1.0 - 0.0625, -0.25, 0.3125, 1.0 - 0, -0.1875, 0.6875)));
			lightSegments.add(Pair.of(pos, new AABB(1.0 - 0.0625, 0.1875, 0.3125, 1.0 - 0, 0.25, 0.6875)));

			i--;
		}
		pos = new PrecisionVector(x, y, z);
		// horizontal lines
		lightSegments.add(Pair.of(pos, new AABB(-remainderWidthLeft - 0.1875, 0.25, 0.25, 0.1875, 0.1875, 0.3125)));
		lightSegments.add(Pair.of(pos, new AABB(-remainderWidthLeft - 0.1875, 0.25, 0.6875, 0.1875, 0.1875, 0.75)));
		lightSegments.add(Pair.of(pos, new AABB(-remainderWidthLeft - 0.1875, -0.25, 0.25, 0.1875, -0.1875, 0.3125)));
		lightSegments.add(Pair.of(pos, new AABB(-remainderWidthLeft - 0.1875, -0.25, 0.6875, 0.1875, -0.1875, 0.75)));
		// center
		darkSegments.add(Pair.of(pos, new AABB(-remainderWidthLeft - 0.1875, 0.125, 0.375, 0.1875, -0.125, 0.625)));
		if (remainderWidthLeft > 0.5) {
			pos = new PrecisionVector(x - remainderWidthLeft - 0.1875, y, z);
			// vertical lines
			lightSegments.add(Pair.of(pos, new AABB(0.9375, -0.1875, 0.25, 1, 0.1875, 0.3125)));
			lightSegments.add(Pair.of(pos, new AABB(0.9375, -0.1875, 0.6875, 1, 0.1875, 0.75)));
			lightSegments.add(Pair.of(pos, new AABB(0.9375, -0.25, 0.3125, 1, -0.1875, 0.6875)));
			lightSegments.add(Pair.of(pos, new AABB(0.9375, 0.1875, 0.3125, 1, 0.25, 0.6875)));
		}

		/* RIGHT */

		i = 0;
		while (i > wholeWidthRight + 1) {
			pos = new PrecisionVector(x - widthRight + 1.0 * i + 0.1875, y, z);
			// horizontal lines
			lightSegments.add(Pair.of(pos, new AABB(0, 0.25, 0.25, -1.0, 0.1875, 0.3125)));
			lightSegments.add(Pair.of(pos, new AABB(0, 0.25, 0.6875, -1.0, 0.1875, 0.75)));
			lightSegments.add(Pair.of(pos, new AABB(0, -0.25, 0.25, -1.0, -0.1875, 0.3125)));
			lightSegments.add(Pair.of(pos, new AABB(0, -0.25, 0.6875, -1.0, -0.1875, 0.75)));
			// center
			darkSegments.add(Pair.of(pos, new AABB(0, 0.125, 0.375, -1.0, -0.125, 0.625)));
			// vertical lines
			lightSegments.add(Pair.of(pos, new AABB(-1.0 + 0, -0.1875, 0.25, -1.0 + 0.0625, 0.1875, 0.3125)));
			lightSegments.add(Pair.of(pos, new AABB(-1.0 + 0, -0.1875, 0.6875, -1.0 + 0.0625, 0.1875, 0.75)));
			lightSegments.add(Pair.of(pos, new AABB(-1.0 + 0, -0.25, 0.3125, -1.0 + 0.0625, -0.1875, 0.6875)));
			lightSegments.add(Pair.of(pos, new AABB(-1.0 + 0, 0.1875, 0.3125, -1.0 + 0.0625, 0.25, 0.6875)));

			i--;
		}
		pos = new PrecisionVector(x, y, z);
		// horizontal lines
		lightSegments.add(Pair.of(pos, new AABB(-remainderWidthRight + 1.0 + 0.1875, 0.25, 0.25, 0.8125, 0.1875, 0.3125)));
		lightSegments.add(Pair.of(pos, new AABB(-remainderWidthRight + 1.0 + 0.1875, 0.25, 0.6875, 0.8125, 0.1875, 0.75)));
		lightSegments.add(Pair.of(pos, new AABB(-remainderWidthRight + 1.0 + 0.1875, -0.25, 0.25, 0.8125, -0.1875, 0.3125)));
		lightSegments.add(Pair.of(pos, new AABB(-remainderWidthRight + 1.0 + 0.1875, -0.25, 0.6875, 0.8125, -0.1875, 0.75)));
		// center
		darkSegments.add(Pair.of(pos, new AABB(-remainderWidthRight + 1.0 + 0.1875, 0.125, 0.375, 0.8125, -0.125, 0.625)));
		if (remainderWidthRight < -0.5) {
			pos = new PrecisionVector(x - remainderWidthRight + 0.1875, y, z);
			// vertical lines
			lightSegments.add(Pair.of(pos, new AABB(0, -0.1875, 0.25, 0.0625, 0.1875, 0.3125)));
			lightSegments.add(Pair.of(pos, new AABB(0, -0.1875, 0.6875, 0.0625, 0.1875, 0.75)));
			lightSegments.add(Pair.of(pos, new AABB(0, -0.25, 0.3125, 0.0625, -0.1875, 0.6875)));
			lightSegments.add(Pair.of(pos, new AABB(0, 0.1875, 0.3125, 0.0625, 0.25, 0.6875)));
		}

	}

	private void south(double x, double y, double z, List<Pair<PrecisionVector, AABB>> darkSegments, List<Pair<PrecisionVector, AABB>> lightSegments, double widthLeft, double widthRight, double widthTop, double widthBottom, int wholeWidthLeft, int wholeWidthRight, int wholeWidthTop, int wholeWidthBottom, double remainderWidthLeft, double remainderWidthRight, double remainderWidthTop, double remainderWidthBottom) {

		if (onRight) {
			southOnRight(x, y, z, darkSegments, lightSegments, widthLeft, widthRight, wholeWidthLeft, wholeWidthRight, remainderWidthLeft, remainderWidthRight);
		} else {
			southOnLeft(x, y, z, darkSegments, lightSegments, widthLeft, widthRight, wholeWidthLeft, wholeWidthRight, remainderWidthLeft, remainderWidthRight);
		}

		/* BOTTOM */

		int i = 0;
		int removal = remainderWidthBottom < 0.625 ? 1 : 0;
		PrecisionVector pos;

		while (i < wholeWidthBottom - removal) {
			pos = new PrecisionVector(x, y, z - widthBottom + 1.0 * i + 0.8125);
			// horizontal lines
			lightSegments.add(Pair.of(pos, new AABB(0.25, -0.25, 0, 0.3125, -0.1875, 1.0)));
			lightSegments.add(Pair.of(pos, new AABB(0.25, 0.1875, 0, 0.3125, 0.25, 1.0)));
			lightSegments.add(Pair.of(pos, new AABB(0.6875, -0.25, 0, 0.75, -0.1875, 1.0)));
			lightSegments.add(Pair.of(pos, new AABB(0.6875, 0.1875, 0, 0.75, 0.25, 1.0)));
			// center
			darkSegments.add(Pair.of(pos, new AABB(0.375, -0.125, 0, 0.625, 0.125, 1.0)));
			// vertical lines
			lightSegments.add(Pair.of(pos, new AABB(0.25, -0.1875, 0.9375, 0.3125, 0.1875, 1)));
			lightSegments.add(Pair.of(pos, new AABB(0.6875, -0.1875, 0.9375, 0.75, 0.1875, 1)));
			lightSegments.add(Pair.of(pos, new AABB(0.3125, -0.25, 0.9375, 0.6875, -0.1875, 1)));
			lightSegments.add(Pair.of(pos, new AABB(0.3125, 0.1875, 0.9375, 0.6875, 0.25, 1)));

			i++;
		}

		pos = new PrecisionVector(x, y, z);
		if (removal == 0) {
			// horizontal lines
			lightSegments.add(Pair.of(pos, new AABB(0.25, -0.25, -remainderWidthBottom + 0.8125, 0.3125, -0.1875, 0.1875)));
			lightSegments.add(Pair.of(pos, new AABB(0.25, 0.1875, -remainderWidthBottom + 0.8125, 0.3125, 0.25, 0.1875)));
			lightSegments.add(Pair.of(pos, new AABB(0.6875, -0.25, -remainderWidthBottom + 0.8125, 0.75, -0.1875, 0.1875)));
			lightSegments.add(Pair.of(pos, new AABB(0.6875, 0.1875, -remainderWidthBottom + 0.8125, 0.75, 0.25, 0.1875)));
			// center
			darkSegments.add(Pair.of(pos, new AABB(0.375, -0.125, -remainderWidthBottom + 0.8125, 0.625, 0.125, 0.1875)));
			// vertical lines
			/*
			 * pos = new PrecisionVector(x, y, z - remainderWidthBottom + 0.8125); lightSegments.add(Pair.of(pos, new AABB(0.25, -0.1875, 0.9375, 0.3125, 0.1875, 1))); lightSegments.add(Pair.of(pos, new AABB(0.6875, -0.1875, 0.9375, 0.75, 0.1875, 1))); lightSegments.add(Pair.of(pos, new AABB(0.3125, -0.25, 0.9375, 0.6875, -0.1875, 1))); lightSegments.add(Pair.of(pos, new AABB(0.3125, 0.1875, 0.9375, 0.6875, 0.25, 1)));
			 */
		} else {
			// horizontal lines
			lightSegments.add(Pair.of(pos, new AABB(0.25, -0.25, -remainderWidthBottom - 1.0 + 0.8125, 0.3125, -0.1875, 0.1875)));
			lightSegments.add(Pair.of(pos, new AABB(0.25, 0.1875, -remainderWidthBottom - 1.0 + 0.8125, 0.3125, 0.25, 0.1875)));
			lightSegments.add(Pair.of(pos, new AABB(0.6875, -0.25, -remainderWidthBottom - 1.0 + 0.8125, 0.75, -0.1875, 0.1875)));
			lightSegments.add(Pair.of(pos, new AABB(0.6875, 0.1875, -remainderWidthBottom - 1.0 + 0.8125, 0.75, 0.25, 0.1875)));
			// center
			darkSegments.add(Pair.of(pos, new AABB(0.375, -0.125, -remainderWidthBottom - 1.0 + 0.8125, 0.625, 0.125, 0.1875)));
			// vertical lines
			if (remainderWidthBottom > 0.5) {
				pos = new PrecisionVector(x, y, z - remainderWidthBottom - 1.0 + 0.8125);
				lightSegments.add(Pair.of(pos, new AABB(0.25, -0.1875, 0.9375, 0.3125, 0.1875, 1)));
				lightSegments.add(Pair.of(pos, new AABB(0.6875, -0.1875, 0.9375, 0.75, 0.1875, 1)));
				lightSegments.add(Pair.of(pos, new AABB(0.3125, -0.25, 0.9375, 0.6875, -0.1875, 1)));
				lightSegments.add(Pair.of(pos, new AABB(0.3125, 0.1875, 0.9375, 0.6875, 0.25, 1)));
			}

		}

		/* TOP */

		removal = remainderWidthTop > -0.625 ? 1 : 0;
		i = 0;
		while (i > wholeWidthTop + removal) {
			pos = new PrecisionVector(x, y, z - widthTop + 1.0 * i + 0.1875);
			// horizontal lines
			lightSegments.add(Pair.of(pos, new AABB(0.25, -0.25, 0, 0.3125, -0.1875, -1.0)));
			lightSegments.add(Pair.of(pos, new AABB(0.25, 0.1875, 0, 0.3125, 0.25, -1.0)));
			lightSegments.add(Pair.of(pos, new AABB(0.6875, -0.25, 0, 0.75, -0.1875, -1.0)));
			lightSegments.add(Pair.of(pos, new AABB(0.6875, 0.1875, 0, 0.75, 0.25, -1.0)));
			// center
			darkSegments.add(Pair.of(pos, new AABB(0.3875, -0.125, 0, 0.625, 0.125, -1.0)));
			// vertical lines
			lightSegments.add(Pair.of(pos, new AABB(0.25, -0.1875, -1.0 + 0, 0.3125, 0.1875, -1.0 + 0.0625)));
			lightSegments.add(Pair.of(pos, new AABB(0.6875, -0.1875, -1.0 + 0, 0.75, 0.1875, -1.0 + 0.0625)));
			lightSegments.add(Pair.of(pos, new AABB(0.3125, -0.25, -1.0 + 0, 0.6875, -0.1875, -1.0 + 0.0625)));
			lightSegments.add(Pair.of(pos, new AABB(0.3125, 0.1875, -1.0 + 0, 0.6875, 0.25, -1.0 + 0.0625)));

			i--;
		}

		pos = new PrecisionVector(x, y, z);
		if (removal == 1) {
			// horizontal lines
			lightSegments.add(Pair.of(pos, new AABB(0.25, -0.25, -remainderWidthTop + 1.0 + 0.1875, 0.3125, -0.1875, 0.8125)));
			lightSegments.add(Pair.of(pos, new AABB(0.6875, -0.25, -remainderWidthTop + 1.0 + 0.1875, 0.75, -0.1875, 0.8125)));
			lightSegments.add(Pair.of(pos, new AABB(0.25, 0.1875, -remainderWidthTop + 1.0 + 0.1875, 0.3125, 0.25, 0.8125)));
			lightSegments.add(Pair.of(pos, new AABB(0.6875, 0.1875, -remainderWidthTop + 1.0 + 0.1875, 0.75, 0.25, 0.8125)));
			// center
			darkSegments.add(Pair.of(pos, new AABB(0.3875, -0.125, -remainderWidthTop + 1.0 + 0.1875, 0.625, 0.125, 0.8125)));
			// horizontal lines
			if (remainderWidthTop < -0.5) {
				pos = new PrecisionVector(x, y, z - remainderWidthTop + 0.1875);
				lightSegments.add(Pair.of(pos, new AABB(0.25, -0.1875, 0, 0.3125, 0.1875, 0.0625)));
				lightSegments.add(Pair.of(pos, new AABB(0.6875, -0.1875, 0, 0.75, 0.1875, 0.0625)));
				lightSegments.add(Pair.of(pos, new AABB(0.3125, -0.25, 0, 0.6875, -0.1875, 0.0625)));
				lightSegments.add(Pair.of(pos, new AABB(0.3125, 0.1875, 0, 0.6875, 0.25, 0.0625)));
			}

		} else {
			// horizontal lines
			lightSegments.add(Pair.of(pos, new AABB(0.25, -0.25, -remainderWidthTop + 0.1875, 0.3125, -0.1875, 0.8125)));
			lightSegments.add(Pair.of(pos, new AABB(0.6875, -0.25, -remainderWidthTop + 0.1875, 0.75, -0.1875, 0.8125)));
			lightSegments.add(Pair.of(pos, new AABB(0.25, 0.1875, -remainderWidthTop + 0.1875, 0.3125, 0.25, 0.8125)));
			lightSegments.add(Pair.of(pos, new AABB(0.6875, 0.1875, -remainderWidthTop + 0.1875, 0.75, 0.25, 0.8125)));
			// center
			darkSegments.add(Pair.of(pos, new AABB(0.3875, -0.125, -remainderWidthTop + 0.1875, 0.625, 0.125, 0.8125)));
			// vertical lines
			/*
			 * pos = new PrecisionVector(x, y, z - remainderWidthTop - 1.0 + 0.1875); lightSegments.add(Pair.of(pos, new AABB(0.25, -0.1875, 0, 0.3125, 0.1875, 0.0625))); lightSegments.add(Pair.of(pos, new AABB(0.6875, -0.1875, 0, 0.75, 0.1875, 0.0625))); lightSegments.add(Pair.of(pos, new AABB(0.3125, -0.25, 0, 0.6875, -0.1875, 0.0625))); lightSegments.add(Pair.of(pos, new AABB(0.3125, 0.1875, 0, 0.6875, 0.25, 0.0625)));
			 */
		}

	}

	private void southOnRight(double x, double y, double z, List<Pair<PrecisionVector, AABB>> darkSegments, List<Pair<PrecisionVector, AABB>> lightSegments, double widthLeft, double widthRight, int wholeWidthLeft, int wholeWidthRight, double remainderWidthLeft, double remainderWidthRight) {
		/* LEFT */

		int i = 0;
		PrecisionVector pos;

		while (i < wholeWidthLeft - 1) {
			pos = new PrecisionVector(x - widthLeft + 1.0 * i + 0.8125, y, z);
			// horizontal lines
			lightSegments.add(Pair.of(pos, new AABB(0, 0.25, 0.25, 1.0, 0.1875, 0.3125)));
			lightSegments.add(Pair.of(pos, new AABB(0, 0.25, 0.6875, 1.0, 0.1875, 0.75)));
			lightSegments.add(Pair.of(pos, new AABB(0, -0.25, 0.25, 1.0, -0.1875, 0.3125)));
			lightSegments.add(Pair.of(pos, new AABB(0, -0.25, 0.6875, 1.0, -0.1875, 0.75)));
			// center
			darkSegments.add(Pair.of(pos, new AABB(0, -0.125, 0.375, 1.0, 0.125, 0.625)));
			// vertical lines
			lightSegments.add(Pair.of(pos, new AABB(0.9375, -0.1875, 0.25, 1, 0.1875, 0.3125)));
			lightSegments.add(Pair.of(pos, new AABB(0.9375, -0.1875, 0.6875, 1, 0.1875, 0.75)));
			lightSegments.add(Pair.of(pos, new AABB(0.9375, -0.25, 0.3125, 1, -0.1875, 0.6875)));
			lightSegments.add(Pair.of(pos, new AABB(0.9375, 0.1875, 0.3125, 1, 0.25, 0.6875)));

			i++;
		}
		pos = new PrecisionVector(x, y, z);
		// horizontal lines
		lightSegments.add(Pair.of(pos, new AABB(-remainderWidthLeft - 1.0 + 0.8125, 0.25, 0.25, 0.25, 0.1875, 0.3125)));
		lightSegments.add(Pair.of(pos, new AABB(-remainderWidthLeft - 1.0 + 0.8125, 0.25, 0.6875, 0.25, 0.1875, 0.75)));
		lightSegments.add(Pair.of(pos, new AABB(-remainderWidthLeft - 1.0 + 0.8125, -0.25, 0.25, 0.25, -0.1875, 0.3125)));
		lightSegments.add(Pair.of(pos, new AABB(-remainderWidthLeft - 1.0 + 0.8125, -0.25, 0.6875, 0.25, -0.1875, 0.75)));
		// center
		darkSegments.add(Pair.of(pos, new AABB(-remainderWidthLeft - 1.0 + 0.8125, -0.125, 0.375, 0.25, 0.125, 0.625)));
		// vertical lines
		if (remainderWidthLeft > 0.5) {
			pos = new PrecisionVector(x - remainderWidthLeft - 1.0 + 0.8125, y, z);
			lightSegments.add(Pair.of(pos, new AABB(0.9375, -0.1875, 0.25, 1, 0.1875, 0.3125)));
			lightSegments.add(Pair.of(pos, new AABB(0.9375, -0.1875, 0.6875, 1, 0.1875, 0.75)));
			lightSegments.add(Pair.of(pos, new AABB(0.9375, -0.25, 0.3125, 1, -0.1875, 0.6875)));
			lightSegments.add(Pair.of(pos, new AABB(0.9375, 0.1875, 0.3125, 1, 0.25, 0.6875)));
		}

		/* RIGHT */

		i = 0;
		while (i > wholeWidthRight + 1) {
			pos = new PrecisionVector(x - widthRight + 1.0 * i + 0.1875, y, z);
			// horizontal lines
			lightSegments.add(Pair.of(pos, new AABB(0, 0.25, 0.25, -1, 0.1875, 0.3125)));
			lightSegments.add(Pair.of(pos, new AABB(0, 0.25, 0.6875, -1, 0.1875, 0.75)));
			lightSegments.add(Pair.of(pos, new AABB(0, -0.25, 0.25, -1, -0.1875, 0.3125)));
			lightSegments.add(Pair.of(pos, new AABB(0, -0.25, 0.6875, -1, -0.1875, 0.75)));
			// center
			darkSegments.add(Pair.of(pos, new AABB(0, 0.125, 0.375, -1, -0.125, 0.625)));
			// vertical lines
			lightSegments.add(Pair.of(pos, new AABB(-1.0 + 0, -0.1875, 0.25, -1.0 + 0.0625, 0.1875, 0.3125)));
			lightSegments.add(Pair.of(pos, new AABB(-1.0 + 0, -0.1875, 0.6875, -1.0 + 0.0625, 0.1875, 0.75)));
			lightSegments.add(Pair.of(pos, new AABB(-1.0 + 0, -0.25, 0.3125, -1.0 + 0.0625, -0.1875, 0.6875)));
			lightSegments.add(Pair.of(pos, new AABB(-1.0 + 0, 0.1875, 0.3125, -1.0 + 0.0625, 0.25, 0.6875)));

			i--;
		}

		pos = new PrecisionVector(x, y, z);
		// horizontal lines
		lightSegments.add(Pair.of(pos, new AABB(-remainderWidthRight + 1.0 + 0.1875, 0.25, 0.25, 0.75, 0.1875, 0.3125)));
		lightSegments.add(Pair.of(pos, new AABB(-remainderWidthRight + 1.0 + 0.1875, 0.25, 0.6875, 0.75, 0.1875, 0.75)));
		lightSegments.add(Pair.of(pos, new AABB(-remainderWidthRight + 1.0 + 0.1875, -0.25, 0.25, 0.75, -0.1875, 0.3125)));
		lightSegments.add(Pair.of(pos, new AABB(-remainderWidthRight + 1.0 + 0.1875, -0.25, 0.6875, 0.75, -0.1875, 0.75)));
		// center
		darkSegments.add(Pair.of(pos, new AABB(-remainderWidthRight + 1.0 + 0.1875, -0.125, 0.375, 0.75, 0.125, 0.625)));
		// vertical lines
		if (remainderWidthRight < -0.5) {
			pos = new PrecisionVector(x - remainderWidthRight + 0.1875, y, z);
			lightSegments.add(Pair.of(pos, new AABB(0, -0.1875, 0.25, 0.0625, 0.1875, 0.3125)));
			lightSegments.add(Pair.of(pos, new AABB(0, -0.1875, 0.6875, 0.0625, 0.1875, 0.75)));
			lightSegments.add(Pair.of(pos, new AABB(0, -0.25, 0.3125, 0.0625, -0.1875, 0.6875)));
			lightSegments.add(Pair.of(pos, new AABB(0, 0.1875, 0.3125, 0.0625, 0.25, 0.6875)));
		}

	}

	private void southOnLeft(double x, double y, double z, List<Pair<PrecisionVector, AABB>> darkSegments, List<Pair<PrecisionVector, AABB>> lightSegments, double widthLeft, double widthRight, int wholeWidthLeft, int wholeWidthRight, double remainderWidthLeft, double remainderWidthRight) {
		/* LEFT */

		int i = 0;
		PrecisionVector pos;

		while (i > wholeWidthLeft + 1) {
			pos = new PrecisionVector(x - widthLeft + 1.0 * i + 0.1875, y, z);
			// horizontal lines
			lightSegments.add(Pair.of(pos, new AABB(0, 0.25, 0.25, -1.0, 0.1875, 0.3125)));
			lightSegments.add(Pair.of(pos, new AABB(0, 0.25, 0.6875, -1.0, 0.1875, 0.75)));
			lightSegments.add(Pair.of(pos, new AABB(0, -0.25, 0.25, -1.0, -0.1875, 0.3125)));
			lightSegments.add(Pair.of(pos, new AABB(0, -0.25, 0.6875, -1.0, -0.1875, 0.75)));
			// center
			darkSegments.add(Pair.of(pos, new AABB(0, -0.125, 0.375, -1.0, 0.125, 0.625)));
			// vertical lines
			lightSegments.add(Pair.of(pos, new AABB(-0.9375, -0.1875, 0.25, -1, 0.1875, 0.3125)));
			lightSegments.add(Pair.of(pos, new AABB(-0.9375, -0.1875, 0.6875, -1, 0.1875, 0.75)));
			lightSegments.add(Pair.of(pos, new AABB(-0.9375, -0.25, 0.3125, -1, -0.1875, 0.6875)));
			lightSegments.add(Pair.of(pos, new AABB(-0.9375, 0.1875, 0.3125, -1, 0.25, 0.6875)));

			i--;
		}
		pos = new PrecisionVector(x, y, z);
		// horizontal lines
		lightSegments.add(Pair.of(pos, new AABB(-remainderWidthLeft + 1.0 + 0.1875, 0.25, 0.25, 0.8125, 0.1875, 0.3125)));
		lightSegments.add(Pair.of(pos, new AABB(-remainderWidthLeft + 1.0 + 0.1875, 0.25, 0.6875, 0.8125, 0.1875, 0.75)));
		lightSegments.add(Pair.of(pos, new AABB(-remainderWidthLeft + 1.0 + 0.1875, -0.25, 0.25, 0.8125, -0.1875, 0.3125)));
		lightSegments.add(Pair.of(pos, new AABB(-remainderWidthLeft + 1.0 + 0.1875, -0.25, 0.6875, 0.8125, -0.1875, 0.75)));
		// center
		darkSegments.add(Pair.of(pos, new AABB(-remainderWidthLeft + 1.0 + 0.1875, 0.125, 0.375, 0.8125, -0.125, 0.625)));
		// vertical lines
		if (remainderWidthLeft < -0.5) {
			pos = new PrecisionVector(x - remainderWidthLeft + 1.0 + 0.1875, y, z);
			lightSegments.add(Pair.of(pos, new AABB(-0.9375, -0.1875, 0.25, -1, 0.1875, 0.3125)));
			lightSegments.add(Pair.of(pos, new AABB(-0.9375, -0.1875, 0.6875, -1, 0.1875, 0.75)));
			lightSegments.add(Pair.of(pos, new AABB(-0.9375, -0.25, 0.3125, -1, -0.1875, 0.6875)));
			lightSegments.add(Pair.of(pos, new AABB(-0.9375, 0.1875, 0.3125, -1, 0.25, 0.6875)));
		}

		/* RIGHT */

		i = 0;
		while (i < wholeWidthRight - 1) {
			pos = new PrecisionVector(x - widthRight + 1.0 * i + 0.8125, y, z);
			// horizontal lines
			lightSegments.add(Pair.of(pos, new AABB(0, 0.25, 0.25, 1.0, 0.1875, 0.3125)));
			lightSegments.add(Pair.of(pos, new AABB(0, 0.25, 0.6875, 1.0, 0.1875, 0.75)));
			lightSegments.add(Pair.of(pos, new AABB(0, -0.25, 0.25, 1.0, -0.1875, 0.3125)));
			lightSegments.add(Pair.of(pos, new AABB(0, -0.25, 0.6875, 1.0, -0.1875, 0.75)));
			// center
			darkSegments.add(Pair.of(pos, new AABB(0, 0.125, 0.375, 1.0, -0.125, 0.625)));
			// vertical lines
			lightSegments.add(Pair.of(pos, new AABB(0.9375, -0.1875, 0.25, 1, 0.1875, 0.3125)));
			lightSegments.add(Pair.of(pos, new AABB(0.9375, -0.1875, 0.6875, 1, 0.1875, 0.75)));
			lightSegments.add(Pair.of(pos, new AABB(0.9375, -0.25, 0.3125, 1, -0.1875, 0.6875)));
			lightSegments.add(Pair.of(pos, new AABB(0.9375, 0.1875, 0.3125, 1, 0.25, 0.6875)));

			i++;
		}
		pos = new PrecisionVector(x, y, z);
		// horizontal lines
		lightSegments.add(Pair.of(pos, new AABB(-remainderWidthRight - 1.0 + 0.8125, 0.25, 0.25, 0.1875, 0.1875, 0.3125)));
		lightSegments.add(Pair.of(pos, new AABB(-remainderWidthRight - 1.0 + 0.8125, 0.25, 0.6875, 0.1875, 0.1875, 0.75)));
		lightSegments.add(Pair.of(pos, new AABB(-remainderWidthRight - 1.0 + 0.8125, -0.25, 0.25, 0.1875, -0.1875, 0.3125)));
		lightSegments.add(Pair.of(pos, new AABB(-remainderWidthRight - 1.0 + 0.8125, -0.25, 0.6875, 0.1875, -0.1875, 0.75)));
		// center
		darkSegments.add(Pair.of(pos, new AABB(-remainderWidthRight - 1.0 + 0.8125, 0.125, 0.375, 0.1875, -0.125, 0.625)));
		// vertical lines
		if (remainderWidthRight > 0.5) {
			pos = new PrecisionVector(x - remainderWidthRight - 1.0 + 0.8125, y, z);
			lightSegments.add(Pair.of(pos, new AABB(0.9375, -0.1875, 0.25, 1, 0.1875, 0.3125)));
			lightSegments.add(Pair.of(pos, new AABB(0.9375, -0.1875, 0.6875, 1, 0.1875, 0.75)));
			lightSegments.add(Pair.of(pos, new AABB(0.9375, -0.25, 0.3125, 1, -0.1875, 0.6875)));
			lightSegments.add(Pair.of(pos, new AABB(0.9375, 0.1875, 0.3125, 1, 0.25, 0.6875)));
		}

	}

	private void east(double x, double y, double z, List<Pair<PrecisionVector, AABB>> darkSegments, List<Pair<PrecisionVector, AABB>> lightSegments, double widthLeft, double widthRight, double widthTop, double widthBottom, int wholeWidthLeft, int wholeWidthRight, int wholeWidthTop, int wholeWidthBottom, double remainderWidthLeft, double remainderWidthRight, double remainderWidthTop, double remainderWidthBottom) {

		if (onRight) {
			eastOnRight(x, y, z, darkSegments, lightSegments, widthLeft, widthRight, wholeWidthLeft, wholeWidthRight, remainderWidthLeft, remainderWidthRight);
		} else {
			eastOnLeft(x, y, z, darkSegments, lightSegments, widthLeft, widthRight, wholeWidthLeft, wholeWidthRight, remainderWidthLeft, remainderWidthRight);
		}

		/* BOTTOM */

		int i = 0;
		PrecisionVector pos;
		while (i < wholeWidthBottom - 1) {
			pos = new PrecisionVector(x - widthBottom + i * 1.0 + 0.8125, y, z);
			// vertical lines
			lightSegments.add(Pair.of(pos, new AABB(0, -0.25, 0.25, 1.0, -0.1875, 0.3125)));
			lightSegments.add(Pair.of(pos, new AABB(0, -0.25, 0.6875, 1.0, -0.1875, 0.75)));
			lightSegments.add(Pair.of(pos, new AABB(0, 0.1875, 0.25, 1.0, 0.25, 0.3125)));
			lightSegments.add(Pair.of(pos, new AABB(0, 0.1875, 0.6875, 1.0, 0.25, 0.75)));
			// center
			darkSegments.add(Pair.of(pos, new AABB(0, -0.125, 0.3875, 1.0, 0.125, 0.625)));
			// vertical lines
			lightSegments.add(Pair.of(pos, new AABB(0.9375, -0.25, 0.3125, 1, -0.1875, 0.6875)));
			lightSegments.add(Pair.of(pos, new AABB(0.9375, 0.1875, 0.3125, 1, 0.25, 0.6875)));
			lightSegments.add(Pair.of(pos, new AABB(0.9375, -0.1875, 0.25, 1, 0.1875, 0.3125)));
			lightSegments.add(Pair.of(pos, new AABB(0.9375, -0.1875, 0.6875, 1, 0.1875, 0.75)));

			i++;
		}
		pos = new PrecisionVector(x, y, z);
		// vertical lines
		lightSegments.add(Pair.of(pos, new AABB(-remainderWidthBottom + 0.8125 - 1.0, -0.25, 0.25, 0.1875, -0.1875, 0.3125)));
		lightSegments.add(Pair.of(pos, new AABB(-remainderWidthBottom + 0.8125 - 1.0, -0.25, 0.6875, 0.1875, -0.1875, 0.75)));
		lightSegments.add(Pair.of(pos, new AABB(-remainderWidthBottom + 0.8125 - 1.0, 0.1875, 0.25, 0.1875, 0.25, 0.3125)));
		lightSegments.add(Pair.of(pos, new AABB(-remainderWidthBottom + 0.8125 - 1.0, 0.1875, 0.6875, 0.1875, 0.25, 0.75)));
		// center
		darkSegments.add(Pair.of(pos, new AABB(-remainderWidthBottom + 0.8125 - 1.0, -0.125, 0.3875, 0.1875, 0.125, 0.625)));
		// vertical lines
		if (remainderWidthBottom > 0.5) {
			pos = new PrecisionVector(x - remainderWidthBottom - 1.0 + 0.8125, y, z);
			lightSegments.add(Pair.of(pos, new AABB(0.9375, -0.25, 0.3125, 1, -0.1875, 0.6875)));
			lightSegments.add(Pair.of(pos, new AABB(0.9375, 0.1875, 0.3125, 1, 0.25, 0.6875)));
			lightSegments.add(Pair.of(pos, new AABB(0.9375, -0.1875, 0.25, 1, 0.1875, 0.3125)));
			lightSegments.add(Pair.of(pos, new AABB(0.9375, -0.1875, 0.6875, 1, 0.1875, 0.75)));
		}

		/* TOP */

		i = 0;
		while (i > wholeWidthTop + 1) {
			pos = new PrecisionVector(x - widthTop + i * 1.0 + 0.1875, y, z);
			// vertical lines
			lightSegments.add(Pair.of(pos, new AABB(0, -0.25, 0.25, -1.0, -0.1875, 0.3125)));
			lightSegments.add(Pair.of(pos, new AABB(0, -0.25, 0.6875, -1.0, -0.1875, 0.75)));
			lightSegments.add(Pair.of(pos, new AABB(0, 0.1875, 0.25, -1.0, 0.25, 0.3125)));
			lightSegments.add(Pair.of(pos, new AABB(0, 0.1875, 0.6875, -1.0, 0.25, 0.75)));
			// center
			darkSegments.add(Pair.of(pos, new AABB(0, -0.125, 0.3875, -1.0, 0.125, 0.625)));
			// vertical segments
			lightSegments.add(Pair.of(pos, new AABB(-1.0 + 0, -0.1875, 0.25, -1.0 + 0.0625, 0.1875, 0.3125)));
			lightSegments.add(Pair.of(pos, new AABB(-1.0 + 0, -0.1875, 0.6875, -1.0 + 0.0625, 0.1875, 0.75)));
			lightSegments.add(Pair.of(pos, new AABB(-1.0 + 0, -0.25, 0.3125, -1.0 + 0.0625, -0.1875, 0.6875)));
			lightSegments.add(Pair.of(pos, new AABB(-1.0 + 0, 0.1875, 0.3125, -1.0 + 0.0625, 0.25, 0.6875)));

			i--;
		}
		pos = new PrecisionVector(x, y, z);
		// vertical lines
		lightSegments.add(Pair.of(pos, new AABB(-remainderWidthTop + 1.0 + 0.1875, -0.25, 0.25, 0.8125, -0.1875, 0.3125)));
		lightSegments.add(Pair.of(pos, new AABB(-remainderWidthTop + 1.0 + 0.1875, -0.25, 0.6875, 0.8125, -0.1875, 0.75)));
		lightSegments.add(Pair.of(pos, new AABB(-remainderWidthTop + 1.0 + 0.1875, 0.1875, 0.25, 0.8125, 0.25, 0.3125)));
		lightSegments.add(Pair.of(pos, new AABB(-remainderWidthTop + 1.0 + 0.1875, 0.1875, 0.6875, 0.8125, 0.25, 0.75)));
		// center
		darkSegments.add(Pair.of(pos, new AABB(-remainderWidthTop + 1.0 + 0.1875, -0.125, 0.3875, 0.8125, 0.125, 0.625)));
		// vertical segments
		if (remainderWidthTop < -0.5) {
			pos = new PrecisionVector(x - remainderWidthTop + 0.1875, y, z);
			lightSegments.add(Pair.of(pos, new AABB(0, -0.1875, 0.25, 0.0625, 0.1875, 0.3125)));
			lightSegments.add(Pair.of(pos, new AABB(0, -0.1875, 0.6875, 0.0625, 0.1875, 0.75)));
			lightSegments.add(Pair.of(pos, new AABB(0, -0.25, 0.3125, 0.0625, -0.1875, 0.6875)));
			lightSegments.add(Pair.of(pos, new AABB(0, 0.1875, 0.3125, 0.0625, 0.25, 0.6875)));
		}

	}

	private void eastOnRight(double x, double y, double z, List<Pair<PrecisionVector, AABB>> darkSegments, List<Pair<PrecisionVector, AABB>> lightSegments, double widthLeft, double widthRight, int wholeWidthLeft, int wholeWidthRight, double remainderWidthLeft, double remainderWidthRight) {
		/* LEFT */

		int i = 0;
		int removal = remainderWidthLeft < -0.625 ? 1 : 0;
		PrecisionVector pos;

		while (i > wholeWidthLeft + 1 - removal) {
			pos = new PrecisionVector(x, y, z - widthLeft + i * 1.0 + 0.1875);
			// horizontal lines
			lightSegments.add(Pair.of(pos, new AABB(0.25, -0.25, 0, 0.3125, -0.1875, -1.0)));
			lightSegments.add(Pair.of(pos, new AABB(0.25, 0.1875, 0, 0.3125, 0.25, -1.0)));
			lightSegments.add(Pair.of(pos, new AABB(0.6875, -0.25, 0, 0.75, -0.1875, -1.0)));
			lightSegments.add(Pair.of(pos, new AABB(0.6875, 0.1875, 0, 0.75, 0.25, -1.0)));
			// center
			darkSegments.add(Pair.of(pos, new AABB(0.3875, -0.125, 0, 0.625, 0.125, -1.0)));
			// vertical lines
			lightSegments.add(Pair.of(pos, new AABB(0.3125, -0.25, -1.0 + 0, 0.6875, -0.1875, -1.0 + 0.0625)));
			lightSegments.add(Pair.of(pos, new AABB(0.3125, 0.1875, -1.0 + 0, 0.6875, 0.25, -1.0 + 0.0625)));
			lightSegments.add(Pair.of(pos, new AABB(0.25, -0.1875, -1.0 + 0, 0.3125, 0.1875, -1.0 + 0.0625)));
			lightSegments.add(Pair.of(pos, new AABB(0.6875, -0.1875, -1.0 + 0, 0.75, 0.1875, -1.0 + 0.0625)));

			i--;
		}
		pos = new PrecisionVector(x, y, z);
		if (removal == 1) {
			// horizontal lines
			lightSegments.add(Pair.of(pos, new AABB(0.25, -0.25, -remainderWidthLeft + 0.1875, 0.3125, -0.1875, 0.8125)));
			lightSegments.add(Pair.of(pos, new AABB(0.25, 0.1875, -remainderWidthLeft + 0.1875, 0.3125, 0.25, 0.8125)));
			lightSegments.add(Pair.of(pos, new AABB(0.6875, -0.25, -remainderWidthLeft + 0.1875, 0.75, -0.1875, 0.8125)));
			lightSegments.add(Pair.of(pos, new AABB(0.6875, 0.1875, -remainderWidthLeft + 0.1875, 0.75, 0.25, 0.8125)));
			// center
			darkSegments.add(Pair.of(pos, new AABB(0.3875, -0.125, -remainderWidthLeft + 0.1875, 0.625, 0.125, 0.8125)));
			// vertical lines
			// pos = new PrecisionVector(x, y, z - remainderWidthLeft - 1.0 + 0.1875);
			// lightSegments.add(Pair.of(pos, new AABB(0.3125, -0.25, 0.468765, 0.6875, -0.1875, 0.53215)));
			// lightSegments.add(Pair.of(pos, new AABB(0.3125, 0.1875, 0.468765, 0.6875, 0.25, 0.53215)));
			// lightSegments.add(Pair.of(pos, new AABB(0.25, -0.1875, 0.468765, 0.3125, 0.1875, 0.53215)));
			// lightSegments.add(Pair.of(pos, new AABB(0.6875, -0.1875, 0.468765, 0.75, 0.1875, 0.53215)));

		} else {
			// horizontal lines
			lightSegments.add(Pair.of(pos, new AABB(0.25, -0.25, -remainderWidthLeft + 1.0 + 0.1875, 0.3125, -0.1875, 0.8125)));
			lightSegments.add(Pair.of(pos, new AABB(0.25, 0.1875, -remainderWidthLeft + 1.0 + 0.1875, 0.3125, 0.25, 0.8125)));
			lightSegments.add(Pair.of(pos, new AABB(0.6875, -0.25, -remainderWidthLeft + 1.0 + 0.1875, 0.75, -0.1875, 0.8125)));
			lightSegments.add(Pair.of(pos, new AABB(0.6875, 0.1875, -remainderWidthLeft + 1.0 + 0.1875, 0.75, 0.25, 0.8125)));
			// center
			darkSegments.add(Pair.of(pos, new AABB(0.3875, -0.125, -remainderWidthLeft + 1.0 + 0.1875, 0.625, 0.125, 0.8125)));
			// vertical lines
			if (remainderWidthLeft < -0.5) {
				pos = new PrecisionVector(x, y, z - remainderWidthLeft + 0.1875);
				lightSegments.add(Pair.of(pos, new AABB(0.3125, -0.25, 0, 0.6875, -0.1875, 0.0625)));
				lightSegments.add(Pair.of(pos, new AABB(0.3125, 0.1875, 0, 0.6875, 0.25, 0.0625)));
				lightSegments.add(Pair.of(pos, new AABB(0.25, -0.1875, 0, 0.3125, 0.1875, 0.0625)));
				lightSegments.add(Pair.of(pos, new AABB(0.6875, -0.1875, 0, 0.75, 0.1875, 0.0625)));
			}

		}

		/* RIGHT */

		i = 0;
		removal = remainderWidthRight < 0.625 ? 1 : 0;
		while (i < wholeWidthRight - removal) {
			pos = new PrecisionVector(x, y, z - widthRight + 1.0 * i + 0.8125);
			// horizontal lines
			lightSegments.add(Pair.of(pos, new AABB(0.25, -0.25, 0, 0.3125, -0.1875, 1.0)));
			lightSegments.add(Pair.of(pos, new AABB(0.6875, -0.25, 0, 0.75, -0.1875, 1.0)));
			lightSegments.add(Pair.of(pos, new AABB(0.25, 0.1875, 0, 0.3125, 0.25, 1.0)));
			lightSegments.add(Pair.of(pos, new AABB(0.6875, 0.1875, 0, 0.75, 0.25, 1.0)));
			// center
			darkSegments.add(Pair.of(pos, new AABB(0.3875, -0.125, 0, 0.625, 0.125, 1.0)));
			// vertical lines
			lightSegments.add(Pair.of(pos, new AABB(0.3125, -0.25, 0.468765, 0.6875, -0.1875, 0.53215)));
			lightSegments.add(Pair.of(pos, new AABB(0.3125, 0.1875, 0.468765, 0.6875, 0.25, 0.53215)));
			lightSegments.add(Pair.of(pos, new AABB(0.25, -0.1875, 0.468765, 0.3125, 0.1875, 0.53215)));
			lightSegments.add(Pair.of(pos, new AABB(0.6875, -0.1875, 0.468765, 0.75, 0.1875, 0.53215)));

			i++;
		}

		pos = new PrecisionVector(x, y, z);
		if (removal == 1) {
			lightSegments.add(Pair.of(pos, new AABB(0.25, -0.25, -remainderWidthRight - 1.0 + 0.8125, 0.3125, -0.1875, 0.1875)));
			lightSegments.add(Pair.of(pos, new AABB(0.6875, -0.25, -remainderWidthRight - 1.0 + 0.8125, 0.75, -0.1875, 0.1875)));
			lightSegments.add(Pair.of(pos, new AABB(0.25, 0.1875, -remainderWidthRight - 1.0 + 0.8125, 0.3125, 0.25, 0.1875)));
			lightSegments.add(Pair.of(pos, new AABB(0.6875, 0.1875, -remainderWidthRight - 1.0 + 0.8125, 0.75, 0.25, 0.1875)));
			// center
			darkSegments.add(Pair.of(pos, new AABB(0.3875, -0.125, -remainderWidthRight - 1.0 + 0.8125, 0.625, 0.125, 0.1875)));
			// vertical lines
			pos = new PrecisionVector(x, y, z - remainderWidthRight - 1.0 + 0.8125);
			lightSegments.add(Pair.of(pos, new AABB(0.3125, -0.25, 0.468765, 0.6875, -0.1875, 0.53215)));
			lightSegments.add(Pair.of(pos, new AABB(0.3125, 0.1875, 0.468765, 0.6875, 0.25, 0.53215)));
			lightSegments.add(Pair.of(pos, new AABB(0.25, -0.1875, 0.468765, 0.3125, 0.1875, 0.53215)));
			lightSegments.add(Pair.of(pos, new AABB(0.6875, -0.1875, 0.468765, 0.75, 0.1875, 0.53215)));
		} else {
			lightSegments.add(Pair.of(pos, new AABB(0.25, -0.25, -remainderWidthRight + 0.8125, 0.3125, -0.1875, 0.1875)));
			lightSegments.add(Pair.of(pos, new AABB(0.6875, -0.25, -remainderWidthRight + 0.8125, 0.75, -0.1875, 0.1875)));
			lightSegments.add(Pair.of(pos, new AABB(0.25, 0.1875, -remainderWidthRight + 0.8125, 0.3125, 0.25, 0.1875)));
			lightSegments.add(Pair.of(pos, new AABB(0.6875, 0.1875, -remainderWidthRight + 0.8125, 0.75, 0.25, 0.1875)));
			// center
			darkSegments.add(Pair.of(pos, new AABB(0.3875, -0.125, -remainderWidthRight + 0.8125, 0.625, 0.125, 0.1875)));
			// vertical lines
			/*
			 * pos = new PrecisionVector(x, y, z - remainderWidthRight + 0.8125); lightSegments.add(Pair.of(pos, new AABB(0.3125, -0.25, 0.468765, 0.6875, -0.1875, 0.53215))); lightSegments.add(Pair.of(pos, new AABB(0.3125, 0.1875, 0.468765, 0.6875, 0.25, 0.53215))); lightSegments.add(Pair.of(pos, new AABB(0.25, -0.1875, 0.468765, 0.3125, 0.1875, 0.53215))); lightSegments.add(Pair.of(pos, new AABB(0.6875, -0.1875, 0.468765, 0.75, 0.1875, 0.53215)));
			 */
		}

	}

	private void eastOnLeft(double x, double y, double z, List<Pair<PrecisionVector, AABB>> darkSegments, List<Pair<PrecisionVector, AABB>> lightSegments, double widthLeft, double widthRight, int wholeWidthLeft, int wholeWidthRight, double remainderWidthLeft, double remainderWidthRight) {
		/* LEFT */

		int i = 0;
		int removal = remainderWidthLeft < 0.625 ? 1 : 0;
		PrecisionVector pos;

		while (i < wholeWidthLeft - removal) {
			pos = new PrecisionVector(x, y, z - widthLeft + i * 1.0 + 0.8125);
			// horizontal lines
			lightSegments.add(Pair.of(pos, new AABB(0.25, -0.25, 0, 0.3125, -0.1875, 1.0)));
			lightSegments.add(Pair.of(pos, new AABB(0.25, 0.1875, 0, 0.3125, 0.25, 1.0)));
			lightSegments.add(Pair.of(pos, new AABB(0.6875, -0.25, 0, 0.75, -0.1875, 1.0)));
			lightSegments.add(Pair.of(pos, new AABB(0.6875, 0.1875, 0, 0.75, 0.25, 1.0)));
			// center
			darkSegments.add(Pair.of(pos, new AABB(0.3875, -0.125, 0, 0.625, 0.125, 1.0)));
			// vertical lines
			lightSegments.add(Pair.of(pos, new AABB(0.3125, -0.25, 0.9375, 0.6875, -0.1875, 1)));
			lightSegments.add(Pair.of(pos, new AABB(0.3125, 0.1875, 0.9375, 0.6875, 0.25, 1)));
			lightSegments.add(Pair.of(pos, new AABB(0.25, -0.1875, 0.9375, 0.3125, 0.1875, 1)));
			lightSegments.add(Pair.of(pos, new AABB(0.6875, -0.1875, 0.9375, 0.75, 0.1875, 1)));

			i++;
		}

		pos = new PrecisionVector(x, y, z);
		if (removal == 1) {
			// horizontal lines
			lightSegments.add(Pair.of(pos, new AABB(0.25, -0.25, -remainderWidthLeft - 1.0 + 0.8125, 0.3125, -0.1875, 0.1875)));
			lightSegments.add(Pair.of(pos, new AABB(0.25, 0.1875, -remainderWidthLeft - 1.0 + 0.8125, 0.3125, 0.25, 0.1875)));
			lightSegments.add(Pair.of(pos, new AABB(0.6875, -0.25, -remainderWidthLeft - 1.0 + 0.8125, 0.75, -0.1875, 0.1875)));
			lightSegments.add(Pair.of(pos, new AABB(0.6875, 0.1875, -remainderWidthLeft - 1.0 + 0.8125, 0.75, 0.25, 0.1875)));
			// center
			darkSegments.add(Pair.of(pos, new AABB(0.3875, -0.125, -remainderWidthLeft - 1.0 + 0.8125, 0.625, 0.125, 0.1875)));
			// vertical lines
			if (remainderWidthLeft > 0.5) {
				pos = new PrecisionVector(x, y, z - remainderWidthLeft - 1.0 + 0.8125);
				lightSegments.add(Pair.of(pos, new AABB(0.3125, -0.25, 0.9375, 0.6875, -0.1875, 1)));
				lightSegments.add(Pair.of(pos, new AABB(0.3125, 0.1875, 0.9375, 0.6875, 0.25, 1)));
				lightSegments.add(Pair.of(pos, new AABB(0.25, -0.1875, 0.9375, 0.3125, 0.1875, 1)));
				lightSegments.add(Pair.of(pos, new AABB(0.6875, -0.1875, 0.9375, 0.75, 0.1875, 1)));
			}
		} else {
			// horizontal lines
			lightSegments.add(Pair.of(pos, new AABB(0.25, -0.25, -remainderWidthLeft + 0.8125, 0.3125, -0.1875, 0.1875)));
			lightSegments.add(Pair.of(pos, new AABB(0.25, 0.1875, -remainderWidthLeft + 0.8125, 0.3125, 0.25, 0.1875)));
			lightSegments.add(Pair.of(pos, new AABB(0.6875, -0.25, -remainderWidthLeft + 0.8125, 0.75, -0.1875, 0.1875)));
			lightSegments.add(Pair.of(pos, new AABB(0.6875, 0.1875, -remainderWidthLeft + 0.8125, 0.75, 0.25, 0.1875)));
			// center
			darkSegments.add(Pair.of(pos, new AABB(0.3875, -0.125, -remainderWidthLeft + 0.8125, 0.625, 0.125, 0.1875)));
			// vertical lines

			// pos = new PrecisionVector(x, y, z - remainderWidthLeft + 0.8125);
			// lightSegments.add(Pair.of(pos, new AABB(0.3125, -0.25, 0.9375, 0.6875, -0.1875, 1)));
			// lightSegments.add(Pair.of(pos, new AABB(0.3125, 0.1875, 0.9375, 0.6875, 0.25, 1)));
			// lightSegments.add(Pair.of(pos, new AABB(0.25, -0.1875, 0.9375, 0.3125, 0.1875, 1)));
			// lightSegments.add(Pair.of(pos, new AABB(0.6875, -0.1875, 0.9375, 0.75, 0.1875, 1)));
		}

		/* RIGHT */

		i = 0;
		removal = remainderWidthRight < -0.625 ? 1 : 0;
		while (i > wholeWidthRight + 1 - removal) {
			pos = new PrecisionVector(x, y, z - widthRight + 1.0 * i + 0.1875);
			// horizontal lines
			lightSegments.add(Pair.of(pos, new AABB(0.25, -0.25, 0, 0.3125, -0.1875, -1.0)));
			lightSegments.add(Pair.of(pos, new AABB(0.6875, -0.25, 0, 0.75, -0.1875, -1.0)));
			lightSegments.add(Pair.of(pos, new AABB(0.25, 0.1875, 0, 0.3125, 0.25, -1.0)));
			lightSegments.add(Pair.of(pos, new AABB(0.6875, 0.1875, 0, 0.75, 0.25, -1.0)));
			// center
			darkSegments.add(Pair.of(pos, new AABB(0.3875, -0.125, 0, 0.625, 0.125, -1.0)));
			// vertical lines
			lightSegments.add(Pair.of(pos, new AABB(0.3125, -0.25, -1.0 + 0, 0.6875, -0.1875, -1.0 + 0.0625)));
			lightSegments.add(Pair.of(pos, new AABB(0.3125, 0.1875, -1.0 + 0, 0.6875, 0.25, -1.0 + 0.0625)));
			lightSegments.add(Pair.of(pos, new AABB(0.25, -0.1875, -1.0 + 0, 0.3125, 0.1875, -1.0 + 0.0625)));
			lightSegments.add(Pair.of(pos, new AABB(0.6875, -0.1875, -1.0 + 0, 0.75, 0.1875, -1.0 + 0.0625)));

			i--;
		}

		pos = new PrecisionVector(x, y, z);
		if (removal == 1) {
			lightSegments.add(Pair.of(pos, new AABB(0.25, -0.25, -remainderWidthRight + 0.1875, 0.3125, -0.1875, 0.8125)));
			lightSegments.add(Pair.of(pos, new AABB(0.6875, -0.25, -remainderWidthRight + 0.1875, 0.75, -0.1875, 0.8125)));
			lightSegments.add(Pair.of(pos, new AABB(0.25, 0.1875, -remainderWidthRight + 0.1875, 0.3125, 0.25, 0.8125)));
			lightSegments.add(Pair.of(pos, new AABB(0.6875, 0.1875, -remainderWidthRight + 0.1875, 0.75, 0.25, 0.8125)));
			// center
			darkSegments.add(Pair.of(pos, new AABB(0.3875, -0.125, -remainderWidthRight + 0.1875, 0.625, 0.125, 0.8125)));
			// vertical lines
			/*
			 * pos = new PrecisionVector(x, y, z - remainderWidthRight - 1.0 + 0.1875); lightSegments.add(Pair.of(pos, new AABB(0.3125, -0.25, 0, 0.6875, -0.1875, 0.0625))); lightSegments.add(Pair.of(pos, new AABB(0.3125, 0.1875, 0, 0.6875, 0.25, 0.0625))); lightSegments.add(Pair.of(pos, new AABB(0.25, -0.1875, 0, 0.3125, 0.1875, 0.0625))); lightSegments.add(Pair.of(pos, new AABB(0.6875, -0.1875, 0, 0.75, 0.1875, 0.0625)));
			 */
		} else {
			lightSegments.add(Pair.of(pos, new AABB(0.25, -0.25, 1.0 - remainderWidthRight + 0.1875, 0.3125, -0.1875, 0.8125)));
			lightSegments.add(Pair.of(pos, new AABB(0.6875, -0.25, 1.0 - remainderWidthRight + 0.1875, 0.75, -0.1875, 0.8125)));
			lightSegments.add(Pair.of(pos, new AABB(0.25, 0.1875, 1.0 - remainderWidthRight + 0.1875, 0.3125, 0.25, 0.8125)));
			lightSegments.add(Pair.of(pos, new AABB(0.6875, 0.1875, 1.0 - remainderWidthRight + 0.1875, 0.75, 0.25, 0.8125)));
			// center
			darkSegments.add(Pair.of(pos, new AABB(0.3875, -0.125, 1.0 - remainderWidthRight + 0.1875, 0.625, 0.125, 0.8125)));
			// vertical lines
			if (remainderWidthRight < -0.5) {
				pos = new PrecisionVector(x, y, z - remainderWidthRight + 0.1875);
				lightSegments.add(Pair.of(pos, new AABB(0.3125, -0.25, 0, 0.6875, -0.1875, 0.0625)));
				lightSegments.add(Pair.of(pos, new AABB(0.3125, 0.1875, 0, 0.6875, 0.25, 0.0625)));
				lightSegments.add(Pair.of(pos, new AABB(0.25, -0.1875, 0, 0.3125, 0.1875, 0.0625)));
				lightSegments.add(Pair.of(pos, new AABB(0.6875, -0.1875, 0, 0.75, 0.1875, 0.0625)));
			}

		}

	}

	private void west(double x, double y, double z, List<Pair<PrecisionVector, AABB>> darkSegments, List<Pair<PrecisionVector, AABB>> lightSegments, double widthLeft, double widthRight, double widthTop, double widthBottom, int wholeWidthLeft, int wholeWidthRight, int wholeWidthTop, int wholeWidthBottom, double remainderWidthLeft, double remainderWidthRight, double remainderWidthTop, double remainderWidthBottom) {
		if (onRight) {
			westOnRight(x, y, z, darkSegments, lightSegments, widthLeft, widthRight, wholeWidthLeft, wholeWidthRight, remainderWidthLeft, remainderWidthRight);
		} else {
			westOnLeft(x, y, z, darkSegments, lightSegments, widthLeft, widthRight, wholeWidthLeft, wholeWidthRight, remainderWidthLeft, remainderWidthRight);
		}

		/* BOTTOM */

		int i = 0;
		PrecisionVector pos;

		while (i > wholeWidthBottom + 1) {
			pos = new PrecisionVector(x - widthBottom + i * 1.0 + 0.1875, y, z);
			// vertical lines
			lightSegments.add(Pair.of(pos, new AABB(0, -0.25, 0.25, -1.0, -0.1875, 0.3125)));
			lightSegments.add(Pair.of(pos, new AABB(0, -0.25, 0.6875, -1.0, -0.1875, 0.75)));
			lightSegments.add(Pair.of(pos, new AABB(0, 0.1875, 0.25, -1.0, 0.25, 0.3125)));
			lightSegments.add(Pair.of(pos, new AABB(0, 0.1875, 0.6875, -1.0, 0.25, 0.75)));
			// center
			darkSegments.add(Pair.of(pos, new AABB(0, -0.125, 0.3875, -1.0, 0.125, 0.625)));
			// vertical lines
			lightSegments.add(Pair.of(pos, new AABB(-1.0 + 0, -0.25, 0.3125, -1.0 + 0.0625, -0.1875, 0.6875)));
			lightSegments.add(Pair.of(pos, new AABB(-1.0 + 0, 0.1875, 0.3125, -1.0 + 0.0625, 0.25, 0.6875)));
			lightSegments.add(Pair.of(pos, new AABB(-1.0 + 0, -0.1875, 0.25, -1.0 + 0.0625, 0.1875, 0.3125)));
			lightSegments.add(Pair.of(pos, new AABB(-1.0 + 0, -0.1875, 0.6875, -1.0 + 0.0625, 0.1875, 0.75)));

			i--;
		}

		pos = new PrecisionVector(x, y, z);
		// vertical lines
		lightSegments.add(Pair.of(pos, new AABB(-remainderWidthBottom + 0.1875 + 1.0, -0.25, 0.25, 0.8125, -0.1875, 0.3125)));
		lightSegments.add(Pair.of(pos, new AABB(-remainderWidthBottom + 0.1875 + 1.0, -0.25, 0.6875, 0.8125, -0.1875, 0.75)));
		lightSegments.add(Pair.of(pos, new AABB(-remainderWidthBottom + 0.1875 + 1.0, 0.1875, 0.25, 0.8125, 0.25, 0.3125)));
		lightSegments.add(Pair.of(pos, new AABB(-remainderWidthBottom + 0.1875 + 1.0, 0.1875, 0.6875, 0.8125, 0.25, 0.75)));
		// center
		darkSegments.add(Pair.of(pos, new AABB(-remainderWidthBottom + 0.1875 + 1.0, -0.125, 0.3875, 0.8125, 0.125, 0.625)));
		// vertical lines
		if (remainderWidthBottom < -0.5) {
			pos = new PrecisionVector(x - remainderWidthBottom + 0.1875, y, z);
			lightSegments.add(Pair.of(pos, new AABB(0, -0.25, 0.3125, 0.0625, -0.1875, 0.6875)));
			lightSegments.add(Pair.of(pos, new AABB(0, 0.1875, 0.3125, 0.0625, 0.25, 0.6875)));
			lightSegments.add(Pair.of(pos, new AABB(0, -0.1875, 0.25, 0.0625, 0.1875, 0.3125)));
			lightSegments.add(Pair.of(pos, new AABB(0, -0.1875, 0.6875, 0.0625, 0.1875, 0.75)));
		}

		/* TOP */

		i = 0;
		while (i < wholeWidthTop - 1) {
			pos = new PrecisionVector(x - widthTop + i * 1.0 + 0.8125, y, z);
			// vertical lines
			lightSegments.add(Pair.of(pos, new AABB(0, -0.25, 0.25, 1.0, -0.1875, 0.3125)));
			lightSegments.add(Pair.of(pos, new AABB(0, -0.25, 0.6875, 1.0, -0.1875, 0.75)));
			lightSegments.add(Pair.of(pos, new AABB(0, 0.1875, 0.25, 1.0, 0.25, 0.3125)));
			lightSegments.add(Pair.of(pos, new AABB(0, 0.1875, 0.6875, 1.0, 0.25, 0.75)));
			// center
			darkSegments.add(Pair.of(pos, new AABB(0, -0.125, 0.3875, 1.0, 0.125, 0.625)));
			// vertical segments
			lightSegments.add(Pair.of(pos, new AABB(0.9375, -0.1875, 0.25, 1, 0.1875, 0.3125)));
			lightSegments.add(Pair.of(pos, new AABB(0.9375, -0.1875, 0.6875, 1, 0.1875, 0.75)));
			lightSegments.add(Pair.of(pos, new AABB(0.9375, -0.25, 0.3125, 1, -0.1875, 0.6875)));
			lightSegments.add(Pair.of(pos, new AABB(0.9375, 0.1875, 0.3125, 1, 0.25, 0.6875)));

			i++;
		}

		pos = new PrecisionVector(x, y, z);
		// vertical lines
		lightSegments.add(Pair.of(pos, new AABB(-remainderWidthTop - 1.0 + 0.8125, -0.25, 0.25, 0.1875, -0.1875, 0.3125)));
		lightSegments.add(Pair.of(pos, new AABB(-remainderWidthTop - 1.0 + 0.8125, -0.25, 0.6875, 0.1875, -0.1875, 0.75)));
		lightSegments.add(Pair.of(pos, new AABB(-remainderWidthTop - 1.0 + 0.8125, 0.1875, 0.25, 0.1875, 0.25, 0.3125)));
		lightSegments.add(Pair.of(pos, new AABB(-remainderWidthTop - 1.0 + 0.8125, 0.1875, 0.6875, 0.1875, 0.25, 0.75)));
		// center
		darkSegments.add(Pair.of(pos, new AABB(-remainderWidthTop - 1.0 + 0.8125, -0.125, 0.3875, 0.1875, 0.125, 0.625)));
		// vertical segments
		if (remainderWidthTop > 0.5) {
			pos = new PrecisionVector(x - remainderWidthTop - 1.0 + 0.8125, y, z);
			lightSegments.add(Pair.of(pos, new AABB(0.9375, -0.1875, 0.25, 1, 0.1875, 0.3125)));
			lightSegments.add(Pair.of(pos, new AABB(0.9375, -0.1875, 0.6875, 1, 0.1875, 0.75)));
			lightSegments.add(Pair.of(pos, new AABB(0.9375, -0.25, 0.3125, 1, -0.1875, 0.6875)));
			lightSegments.add(Pair.of(pos, new AABB(0.9375, 0.1875, 0.3125, 1, 0.25, 0.6875)));
		}

	}

	private void westOnRight(double x, double y, double z, List<Pair<PrecisionVector, AABB>> darkSegments, List<Pair<PrecisionVector, AABB>> lightSegments, double widthLeft, double widthRight, int wholeWidthLeft, int wholeWidthRight, double remainderWidthLeft, double remainderWidthRight) {
		/* LEFT */

		int i = 0;
		int removal = remainderWidthLeft < 0.625 ? 1 : 0;
		PrecisionVector pos;

		while (i < wholeWidthLeft - removal) {
			pos = new PrecisionVector(x, y, z - widthLeft + i * 1.0 + 0.8125);
			// horizontal lines
			lightSegments.add(Pair.of(pos, new AABB(0.25, -0.25, 0, 0.3125, -0.1875, 1.0)));
			lightSegments.add(Pair.of(pos, new AABB(0.25, 0.1875, 0, 0.3125, 0.25, 1.0)));
			lightSegments.add(Pair.of(pos, new AABB(0.6875, -0.25, 0, 0.75, -0.1875, 1.0)));
			lightSegments.add(Pair.of(pos, new AABB(0.6875, 0.1875, 0, 0.75, 0.25, 1.0)));
			// center
			darkSegments.add(Pair.of(pos, new AABB(0.3875, -0.125, 0, 0.625, 0.125, 1.0)));
			// vertical lines
			lightSegments.add(Pair.of(pos, new AABB(0.3125, -0.25, 0.9375, 0.6875, -0.1875, 1)));
			lightSegments.add(Pair.of(pos, new AABB(0.3125, 0.1875, 0.9375, 0.6875, 0.25, 1)));
			lightSegments.add(Pair.of(pos, new AABB(0.25, -0.1875, 0.9375, 0.3125, 0.1875, 1)));
			lightSegments.add(Pair.of(pos, new AABB(0.6875, -0.1875, 0.9375, 0.75, 0.1875, 1)));

			i++;
		}

		pos = new PrecisionVector(x, y, z);
		if (removal == 1) {
			// horizontal lines
			lightSegments.add(Pair.of(pos, new AABB(0.25, -0.25, -remainderWidthLeft - 1.0 + 0.8125, 0.3125, -0.1875, 0.1875)));
			lightSegments.add(Pair.of(pos, new AABB(0.25, 0.1875, -remainderWidthLeft - 1.0 + 0.8125, 0.3125, 0.25, 0.1875)));
			lightSegments.add(Pair.of(pos, new AABB(0.6875, -0.25, -remainderWidthLeft - 1.0 + 0.8125, 0.75, -0.1875, 0.1875)));
			lightSegments.add(Pair.of(pos, new AABB(0.6875, 0.1875, -remainderWidthLeft - 1.0 + 0.8125, 0.75, 0.25, 0.1875)));
			// center
			darkSegments.add(Pair.of(pos, new AABB(0.3875, -0.125, -remainderWidthLeft - 1.0 + 0.8125, 0.625, 0.125, 0.1875)));
			// vertical lines
			if (remainderWidthLeft > 0.5) {
				pos = new PrecisionVector(x, y, z - remainderWidthLeft - 1.0 + 0.8125);
				lightSegments.add(Pair.of(pos, new AABB(0.3125, -0.25, 0.9375, 0.6875, -0.1875, 1)));
				lightSegments.add(Pair.of(pos, new AABB(0.3125, 0.1875, 0.9375, 0.6875, 0.25, 1)));
				lightSegments.add(Pair.of(pos, new AABB(0.25, -0.1875, 0.9375, 0.3125, 0.1875, 1)));
				lightSegments.add(Pair.of(pos, new AABB(0.6875, -0.1875, 0.9375, 0.75, 0.1875, 1)));
			}

		} else {
			// horizontal lines
			lightSegments.add(Pair.of(pos, new AABB(0.25, -0.25, -remainderWidthLeft + 0.8125, 0.3125, -0.1875, 0.1875)));
			lightSegments.add(Pair.of(pos, new AABB(0.25, 0.1875, -remainderWidthLeft + 0.8125, 0.3125, 0.25, 0.1875)));
			lightSegments.add(Pair.of(pos, new AABB(0.6875, -0.25, -remainderWidthLeft + 0.8125, 0.75, -0.1875, 0.1875)));
			lightSegments.add(Pair.of(pos, new AABB(0.6875, 0.1875, -remainderWidthLeft + 0.8125, 0.75, 0.25, 0.1875)));
			// center
			darkSegments.add(Pair.of(pos, new AABB(0.3875, -0.125, -remainderWidthLeft + 0.8125, 0.625, 0.125, 0.1875)));
			// vertical lines

			// pos = new PrecisionVector(x, y, z - remainderWidthLeft + 0.8125);
			// lightSegments.add(Pair.of(pos, new AABB(0.3125, -0.25, 0.9375, 0.6875, -0.1875, 1)));
			// lightSegments.add(Pair.of(pos, new AABB(0.3125, 0.1875, 0.9375, 0.6875, 0.25, 1)));
			// lightSegments.add(Pair.of(pos, new AABB(0.25, -0.1875, 0.9375, 0.3125, 0.1875, 1)));
			// lightSegments.add(Pair.of(pos, new AABB(0.6875, -0.1875, 0.9375, 0.75, 0.1875, 1)));

		}

		/* RIGHT */

		i = 0;
		removal = remainderWidthRight < -0.625 ? 1 : 0;
		while (i > wholeWidthRight + 1 - removal) {
			pos = new PrecisionVector(x, y, z - widthRight + 1.0 * i + 0.1875);
			// horizontal lines
			lightSegments.add(Pair.of(pos, new AABB(0.25, -0.25, 0, 0.3125, -0.1875, -1.0)));
			lightSegments.add(Pair.of(pos, new AABB(0.6875, -0.25, 0, 0.75, -0.1875, -1.0)));
			lightSegments.add(Pair.of(pos, new AABB(0.25, 0.1875, 0, 0.3125, 0.25, -1.0)));
			lightSegments.add(Pair.of(pos, new AABB(0.6875, 0.1875, 0, 0.75, 0.25, -1.0)));
			// center
			darkSegments.add(Pair.of(pos, new AABB(0.3875, -0.125, 0, 0.625, 0.125, -1.0)));
			// vertical lines
			lightSegments.add(Pair.of(pos, new AABB(0.3125, -0.25, -1.0 + 0, 0.6875, -0.1875, -1.0 + 0.0625)));
			lightSegments.add(Pair.of(pos, new AABB(0.3125, 0.1875, -1.0 + 0, 0.6875, 0.25, -1.0 + 0.0625)));
			lightSegments.add(Pair.of(pos, new AABB(0.25, -0.1875, -1.0 + 0, 0.3125, 0.1875, -1.0 + 0.0625)));
			lightSegments.add(Pair.of(pos, new AABB(0.6875, -0.1875, -1.0 + 0, 0.75, 0.1875, -1.0 + 0.0625)));

			i--;
		}

		pos = new PrecisionVector(x, y, z);
		if (removal == 1) {
			lightSegments.add(Pair.of(pos, new AABB(0.25, -0.25, -remainderWidthRight + 0.1875, 0.3125, -0.1875, 0.8125)));
			lightSegments.add(Pair.of(pos, new AABB(0.6875, -0.25, -remainderWidthRight + 0.1875, 0.75, -0.1875, 0.8125)));
			lightSegments.add(Pair.of(pos, new AABB(0.25, 0.1875, -remainderWidthRight + 0.1875, 0.3125, 0.25, 0.8125)));
			lightSegments.add(Pair.of(pos, new AABB(0.6875, 0.1875, -remainderWidthRight + 0.1875, 0.75, 0.25, 0.8125)));
			// center
			darkSegments.add(Pair.of(pos, new AABB(0.3875, -0.125, -remainderWidthRight + 0.1875, 0.625, 0.125, 0.8125)));
			// vertical lines
			/*
			 * pos = new PrecisionVector(x, y, z - remainderWidthRight - 1.0 + 0.1875); lightSegments.add(Pair.of(pos, new AABB(0.3125, -0.25, 0, 0.6875, -0.1875, 0.0625))); lightSegments.add(Pair.of(pos, new AABB(0.3125, 0.1875, 0, 0.6875, 0.25, 0.0625))); lightSegments.add(Pair.of(pos, new AABB(0.25, -0.1875, 0, 0.3125, 0.1875, 0.0625))); lightSegments.add(Pair.of(pos, new AABB(0.6875, -0.1875, 0, 0.75, 0.1875, 0.0625)));
			 */
		} else {
			lightSegments.add(Pair.of(pos, new AABB(0.25, -0.25, 1.0 - remainderWidthRight + 0.1875, 0.3125, -0.1875, 0.8125)));
			lightSegments.add(Pair.of(pos, new AABB(0.6875, -0.25, 1.0 - remainderWidthRight + 0.1875, 0.75, -0.1875, 0.8125)));
			lightSegments.add(Pair.of(pos, new AABB(0.25, 0.1875, 1.0 - remainderWidthRight + 0.1875, 0.3125, 0.25, 0.8125)));
			lightSegments.add(Pair.of(pos, new AABB(0.6875, 0.1875, 1.0 - remainderWidthRight + 0.1875, 0.75, 0.25, 0.8125)));
			// center
			darkSegments.add(Pair.of(pos, new AABB(0.3875, -0.125, 1.0 - remainderWidthRight + 0.1875, 0.625, 0.125, 0.8125)));
			// vertical lines
			if (remainderWidthRight < -0.5) {
				pos = new PrecisionVector(x, y, z - remainderWidthRight + 0.1875);
				lightSegments.add(Pair.of(pos, new AABB(0.3125, -0.25, 0, 0.6875, -0.1875, 0.0625)));
				lightSegments.add(Pair.of(pos, new AABB(0.3125, 0.1875, 0, 0.6875, 0.25, 0.0625)));
				lightSegments.add(Pair.of(pos, new AABB(0.25, -0.1875, 0, 0.3125, 0.1875, 0.0625)));
				lightSegments.add(Pair.of(pos, new AABB(0.6875, -0.1875, 0, 0.75, 0.1875, 0.0625)));
			}

		}

	}

	private void westOnLeft(double x, double y, double z, List<Pair<PrecisionVector, AABB>> darkSegments, List<Pair<PrecisionVector, AABB>> lightSegments, double widthLeft, double widthRight, int wholeWidthLeft, int wholeWidthRight, double remainderWidthLeft, double remainderWidthRight) {
		/* LEFT */

		int i = 0;
		int removal = remainderWidthLeft < -0.625 ? 1 : 0;
		PrecisionVector pos;
		while (i > wholeWidthLeft + 1 - removal) {
			pos = new PrecisionVector(x, y, z - widthLeft + i * 1.0 + 0.1875);
			// horizontal lines
			lightSegments.add(Pair.of(pos, new AABB(0.25, -0.25, 0, 0.3125, -0.1875, -1.0)));
			lightSegments.add(Pair.of(pos, new AABB(0.25, 0.1875, 0, 0.3125, 0.25, -1.0)));
			lightSegments.add(Pair.of(pos, new AABB(0.6875, -0.25, 0, 0.75, -0.1875, -1.0)));
			lightSegments.add(Pair.of(pos, new AABB(0.6875, 0.1875, 0, 0.75, 0.25, -1.0)));
			// center
			darkSegments.add(Pair.of(pos, new AABB(0.3875, -0.125, 0, 0.625, 0.125, -1.0)));
			// vertical lines
			lightSegments.add(Pair.of(pos, new AABB(0.3125, -0.25, -1.0 + 0, 0.6875, -0.1875, -1.0 + 0.0625)));
			lightSegments.add(Pair.of(pos, new AABB(0.3125, 0.1875, -1.0 + 0, 0.6875, 0.25, -1.0 + 0.0625)));
			lightSegments.add(Pair.of(pos, new AABB(0.25, -0.1875, -1.0 + 0, 0.3125, 0.1875, -1.0 + 0.0625)));
			lightSegments.add(Pair.of(pos, new AABB(0.6875, -0.1875, -1.0 + 0, 0.75, 0.1875, -1.0 + 0.0625)));

			i--;
		}

		pos = new PrecisionVector(x, y, z);
		if (removal == 1) {
			// horizontal lines
			lightSegments.add(Pair.of(pos, new AABB(0.25, -0.25, -remainderWidthLeft + 0.1875, 0.3125, -0.1875, 0.8125)));
			lightSegments.add(Pair.of(pos, new AABB(0.25, 0.1875, -remainderWidthLeft + 0.1875, 0.3125, 0.25, 0.8125)));
			lightSegments.add(Pair.of(pos, new AABB(0.6875, -0.25, -remainderWidthLeft + 0.1875, 0.75, -0.1875, 0.8125)));
			lightSegments.add(Pair.of(pos, new AABB(0.6875, 0.1875, -remainderWidthLeft + 0.1875, 0.75, 0.25, 0.8125)));
			// center
			darkSegments.add(Pair.of(pos, new AABB(0.3875, -0.125, -remainderWidthLeft + 0.1875, 0.625, 0.125, 0.8125)));
			// vertical lines

			// pos = new PrecisionVector(x, y, z - 1.0 - remainderWidthLeft + 0.1875);
			// lightSegments.add(Pair.of(pos, new AABB(0.3125, -0.25, 0, 0.6875, -0.1875, 0.0625)));
			// lightSegments.add(Pair.of(pos, new AABB(0.3125, 0.1875, 0, 0.6875, 0.25, 0.0625)));
			// lightSegments.add(Pair.of(pos, new AABB(0.25, -0.1875, 0, 0.3125, 0.1875, 0.0625)));
			// lightSegments.add(Pair.of(pos, new AABB(0.6875, -0.1875, 0, 0.75, 0.1875, 0.0625)));

		} else {
			// horizontal lines
			lightSegments.add(Pair.of(pos, new AABB(0.25, -0.25, -remainderWidthLeft + 1.0 + 0.1875, 0.3125, -0.1875, 0.8125)));
			lightSegments.add(Pair.of(pos, new AABB(0.25, 0.1875, -remainderWidthLeft + 1.0 + 0.1875, 0.3125, 0.25, 0.8125)));
			lightSegments.add(Pair.of(pos, new AABB(0.6875, -0.25, -remainderWidthLeft + 1.0 + 0.1875, 0.75, -0.1875, 0.8125)));
			lightSegments.add(Pair.of(pos, new AABB(0.6875, 0.1875, -remainderWidthLeft + 1.0 + 0.1875, 0.75, 0.25, 0.8125)));
			// center
			darkSegments.add(Pair.of(pos, new AABB(0.3875, -0.125, -remainderWidthLeft + 1.0 + 0.1875, 0.625, 0.125, 0.8125)));
			// vertical lines
			if (remainderWidthLeft < -0.5) {
				pos = new PrecisionVector(x, y, z - remainderWidthLeft + 0.1875);
				lightSegments.add(Pair.of(pos, new AABB(0.3125, -0.25, 0, 0.6875, -0.1875, 0.0625)));
				lightSegments.add(Pair.of(pos, new AABB(0.3125, 0.1875, 0, 0.6875, 0.25, 0.0625)));
				lightSegments.add(Pair.of(pos, new AABB(0.25, -0.1875, 0, 0.3125, 0.1875, 0.0625)));
				lightSegments.add(Pair.of(pos, new AABB(0.6875, -0.1875, 0, 0.75, 0.1875, 0.0625)));
			}
		}

		/* RIGHT */

		i = 0;
		removal = remainderWidthRight < 0.625 ? 1 : 0;
		while (i < wholeWidthRight - removal) {
			pos = new PrecisionVector(x, y, z - widthRight + 1.0 * i + 0.8125);
			// horizontal lines
			lightSegments.add(Pair.of(pos, new AABB(0.25, -0.25, 0, 0.3125, -0.1875, 1.0)));
			lightSegments.add(Pair.of(pos, new AABB(0.6875, -0.25, 0, 0.75, -0.1875, 1.0)));
			lightSegments.add(Pair.of(pos, new AABB(0.25, 0.1875, 0, 0.3125, 0.25, 1.0)));
			lightSegments.add(Pair.of(pos, new AABB(0.6875, 0.1875, 0, 0.75, 0.25, 1.0)));
			// center
			darkSegments.add(Pair.of(pos, new AABB(0.3875, -0.125, 0, 0.625, 0.125, 1.0)));
			// vertical lines
			lightSegments.add(Pair.of(pos, new AABB(0.3125, -0.25, 0.9375, 0.6875, -0.1875, 1)));
			lightSegments.add(Pair.of(pos, new AABB(0.3125, 0.1875, 0.9375, 0.6875, 0.25, 1)));
			lightSegments.add(Pair.of(pos, new AABB(0.25, -0.1875, 0.9375, 0.3125, 0.1875, 1)));
			lightSegments.add(Pair.of(pos, new AABB(0.6875, -0.1875, 0.9375, 0.75, 0.1875, 1)));

			i++;
		}

		pos = new PrecisionVector(x, y, z);
		if (removal == 1) {
			lightSegments.add(Pair.of(pos, new AABB(0.25, -0.25, -remainderWidthRight - 1.0 + 0.8125, 0.3125, -0.1875, 0.1875)));
			lightSegments.add(Pair.of(pos, new AABB(0.6875, -0.25, -remainderWidthRight - 1.0 + 0.8125, 0.75, -0.1875, 0.1875)));
			lightSegments.add(Pair.of(pos, new AABB(0.25, 0.1875, -remainderWidthRight - 1.0 + 0.8125, 0.3125, 0.25, 0.1875)));
			lightSegments.add(Pair.of(pos, new AABB(0.6875, 0.1875, -remainderWidthRight - 1.0 + 0.8125, 0.75, 0.25, 0.1875)));
			// center
			darkSegments.add(Pair.of(pos, new AABB(0.3875, -0.125, -remainderWidthRight - 1.0 + 0.8125, 0.625, 0.125, 0.1875)));
			// vertical lines
			if (remainderWidthRight > 0.5) {
				pos = new PrecisionVector(x, y, z - remainderWidthRight - 1.0 + 0.8125);
				lightSegments.add(Pair.of(pos, new AABB(0.3125, -0.25, 0.9375, 0.6875, -0.1875, 1)));
				lightSegments.add(Pair.of(pos, new AABB(0.3125, 0.1875, 0.9375, 0.6875, 0.25, 1)));
				lightSegments.add(Pair.of(pos, new AABB(0.25, -0.1875, 0.9375, 0.3125, 0.1875, 1)));
				lightSegments.add(Pair.of(pos, new AABB(0.6875, -0.1875, 0.9375, 0.75, 0.1875, 1)));
			}
		} else {
			lightSegments.add(Pair.of(pos, new AABB(0.25, -0.25, -remainderWidthRight + 0.8125, 0.3125, -0.1875, 0.1875)));
			lightSegments.add(Pair.of(pos, new AABB(0.6875, -0.25, -remainderWidthRight + 0.8125, 0.75, -0.1875, 0.1875)));
			lightSegments.add(Pair.of(pos, new AABB(0.25, 0.1875, -remainderWidthRight + 0.8125, 0.3125, 0.25, 0.1875)));
			lightSegments.add(Pair.of(pos, new AABB(0.6875, 0.1875, -remainderWidthRight + 0.8125, 0.75, 0.25, 0.1875)));
			// center
			darkSegments.add(Pair.of(pos, new AABB(0.3875, -0.125, -remainderWidthRight + 0.8125, 0.625, 0.125, 0.1875)));
			// vertical lines
			/*
			 * pos = new PrecisionVector(x, y, z - remainderWidthRight + 0.8125); lightSegments.add(Pair.of(pos, new AABB(0.3125, -0.25, 0.9375, 0.6875, -0.1875, 1))); lightSegments.add(Pair.of(pos, new AABB(0.3125, 0.1875, 0.9375, 0.6875, 0.25, 1))); lightSegments.add(Pair.of(pos, new AABB(0.25, -0.1875, 0.9375, 0.3125, 0.1875, 1))); lightSegments.add(Pair.of(pos, new AABB(0.6875, -0.1875, 0.9375, 0.75, 0.1875, 1)));
			 */
		}

	}

	@Nullable
	private QuarryArmFrameWrapper getCurrentFrame(TileQuarry quarry) {
		if (quarry.miningPos.get().equals(BlockEntityUtils.OUT_OF_REACH)) {
			return new QuarryArmFrameWrapper(null, 0, 0, 0);
		}

		if (quarry.prevMiningPos.get().equals(BlockEntityUtils.OUT_OF_REACH) || quarry.prevMiningPos.get().equals(quarry.miningPos.get())) {
			return new QuarryArmFrameWrapper(new Location(quarry.miningPos.get()).add(-0.5, -0.5, -0.5), 0, 0, 0);
		}

		if (!quarry.hasHead.get() || !quarry.isMotorComplexPowered()) {
			return currentFrame;
		}

		int numberOfFrames = quarry.speed.get();
		if (numberOfFrames == 0) {
			numberOfFrames = 1;
		}

		double deltaX = (quarry.miningPos.get().getX() - quarry.prevMiningPos.get().getX()) / (double) numberOfFrames;
		double deltaY = (quarry.miningPos.get().getY() - quarry.prevMiningPos.get().getY()) / (double) numberOfFrames;
		double deltaZ = (quarry.miningPos.get().getZ() - quarry.prevMiningPos.get().getZ()) / (double) numberOfFrames;

		if (Math.abs(deltaX) + Math.abs(deltaY) + Math.abs(deltaZ) == 0) {
			return new QuarryArmFrameWrapper(new Location(quarry.miningPos.get().offset(0, -1, 0)), 0, 0, 0);
		}
		float degress = 360.0F * ((float) quarry.progressCounter.get() / (float) numberOfFrames);
		int currFrame = quarry.progressCounter.get() % numberOfFrames;

		int signX = (int) Math.signum(deltaX);
		int signZ = (int) Math.signum(deltaZ);

		return new QuarryArmFrameWrapper(new Location(quarry.prevMiningPos.get().getX() + deltaX * currFrame, quarry.prevMiningPos.get().getY() + deltaY * currFrame, quarry.prevMiningPos.get().getZ() + deltaZ * currFrame), signX, signZ, degress);

	}

}
