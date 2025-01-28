package electrodynamics.api.multiblock.assemblybased;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.serialization.JsonOps;

import electrodynamics.api.References;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.Coordinates;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * The two block positions are the bounds represented by the whole number (floor) of the coordinate at a given position.
 * 
 * For example, -4.505, -59.0, -4.504 would be entered as:
 * 
 * -4, -59, -4
 * 
 * and -2.392, -59.0, -0.501 would be entered as:
 * 
 * -2, -59, 0
 * 
 * @author skip999
 *
 */
public class CommandScanMultiblock {

	private static final String MULTIBLOCK = "multiblock";
	private static final String SCAN = "scan";

	private static final String CONTROLLER_POS = "controllerpos";
	private static final String START_POS = "startpos";
	private static final String END_POS = "endpos";
	private static final String IGNORE_AIR = "ignoreair";
	private static final String NAME = "name";

	private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();

	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {

		dispatcher.register(Commands.literal(References.ID).requires(source -> {
			return source.hasPermission(0);
		}).then(Commands.literal(MULTIBLOCK).requires(source -> source.hasPermission(2)).then(Commands.literal(SCAN)).then(Commands.argument(CONTROLLER_POS, Vec3Argument.vec3()).then(Commands.argument(START_POS, Vec3Argument.vec3()).then(Commands.argument(END_POS, Vec3Argument.vec3()).then(Commands.argument(IGNORE_AIR, IntegerArgumentType.integer(0, 1))
				.then(Commands.argument(NAME, StringArgumentType.string()).executes(source -> parseCommand(source.getSource(), Vec3Argument.getCoordinates(source, CONTROLLER_POS), Vec3Argument.getCoordinates(source, START_POS), Vec3Argument.getCoordinates(source, END_POS), IntegerArgumentType.getInteger(source, IGNORE_AIR), StringArgumentType.getString(source, NAME))))))))));

	}

	private static int parseCommand(CommandSourceStack source, Coordinates contPos, Coordinates stCorner, Coordinates stpCorner, int air, String name) {

		BlockPos controllerPos = contPos.getBlockPos(source);

		BlockPos startCorner = stCorner.getBlockPos(source);

		BlockPos stopCorner = stpCorner.getBlockPos(source);

		boolean includeAir = air == 1;

		ServerLevel level = source.getLevel();

		List<MemberHolder> members = new ArrayList<>();

		BlockPos.betweenClosed(startCorner, stopCorner).forEach(pos -> {

			if (pos.equals(controllerPos)) {
				return;
			}

			BlockState state = level.getBlockState(pos);

			if (!includeAir && state.isAir()) {
				return;
			}

			members.add(new MemberHolder(state, pos.subtract(controllerPos), state.getVisualShape(level, pos, CollisionContext.empty())));

		});

		JsonObject json = new JsonObject();

		int index = 0;

		for (MemberHolder member : members) {

			JsonObject element = new JsonObject();

			element.add("placestate", BlockState.CODEC.encode(member.state(), JsonOps.INSTANCE, new JsonObject()).getOrThrow());

			element.addProperty("offset", "new Vec3i(" + member.offset().getX() + ", " + member.offset().getY() + ", " + member.offset().getZ() + ");");

			String shape = "";
			
			List<String> aabbs = new ArrayList<>();

			for (AABB aabb : member.shape().toAabbs()) {

				aabbs.add("new AABB(" + aabb.minX + ", " + aabb.minY + ", " + aabb.minZ + ", " + aabb.maxX + ", " + aabb.maxY + ", " + aabb.maxZ + ")");

			}
			
			if(aabbs.size() == 1) {
				shape = "Shapes.create(" + aabbs.get(0) + ")";
			} else if (aabbs.size() > 1) {
				shape = "Shapes.or(";
				for(int i = 0; i < aabbs.size(); i++) {
					
					shape += aabbs.get(i);
					
					if(i + 1 != aabbs.size()) {
						shape += ", ";
					} 
					
				}
				shape += ")";
			}

			element.addProperty("shape", shape);

			json.add(index + "", element);

			index++;
		}

		Path path = Paths.get("Electrodynamics/" + name + ".json");

		try {
			String s = GSON.toJson(json);

			if (!Files.exists(path.getParent())) {
				Files.createDirectories(path.getParent());
			}

			BufferedWriter bufferedwriter = Files.newBufferedWriter(path);

			try {
				bufferedwriter.write(s);
			} catch (Throwable throwable1) {
				if (bufferedwriter != null) {
					try {
						bufferedwriter.close();
					} catch (Throwable throwable) {
						throwable1.addSuppressed(throwable);
					}
				}

				throw throwable1;
			}

			if (bufferedwriter != null) {
				bufferedwriter.close();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		source.sendSuccess(() -> Component.literal("written"), true);

		return 1;
	}

	private static record MemberHolder(BlockState state, BlockPos offset, VoxelShape shape) {

	}

}
