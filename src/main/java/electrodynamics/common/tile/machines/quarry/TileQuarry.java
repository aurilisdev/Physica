package electrodynamics.common.tile.machines.quarry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.stream.Stream;

import javax.annotation.Nullable;

import electrodynamics.api.tile.IPlayerStorable;
import electrodynamics.client.render.event.levelstage.HandlerQuarryArm;
import electrodynamics.common.block.BlockFrame;
import electrodynamics.common.block.states.ElectrodynamicsBlockStates;
import electrodynamics.common.block.subtype.SubtypeMachine;
import electrodynamics.common.inventory.container.tile.ContainerQuarry;
import electrodynamics.common.item.ItemDrillHead;
import electrodynamics.common.item.ItemUpgrade;
import electrodynamics.common.item.subtype.SubtypeDrillHead;
import electrodynamics.common.settings.Constants;
import electrodynamics.prefab.block.GenericMachineBlock;
import electrodynamics.prefab.properties.Property;
import electrodynamics.prefab.properties.PropertyTypes;
import electrodynamics.prefab.tile.GenericTile;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.tile.components.type.ComponentContainerProvider;
import electrodynamics.prefab.tile.components.type.ComponentElectrodynamic;
import electrodynamics.prefab.tile.components.type.ComponentInventory;
import electrodynamics.prefab.tile.components.type.ComponentInventory.InventoryBuilder;
import electrodynamics.prefab.tile.components.type.ComponentPacketHandler;
import electrodynamics.prefab.tile.components.type.ComponentTickable;
import electrodynamics.prefab.utilities.BlockEntityUtils;
import electrodynamics.prefab.utilities.ItemUtils;
import electrodynamics.registers.ElectrodynamicsBlocks;
import electrodynamics.registers.ElectrodynamicsCapabilities;
import electrodynamics.registers.ElectrodynamicsTiles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.common.util.FakePlayerFactory;

/**
 * Loosely based on Quarry concept from Buildcraft with aspects of Extra Utilities's Ender Quarry
 * 
 * @author skip999
 *
 */
public class TileQuarry extends GenericTile implements IPlayerStorable {

	private static final int CAPACITY = 10000;
	private static final BlockState AIR = Blocks.AIR.defaultBlockState();
	private static final int CLEAR_SKIP = Math.max(Math.min(Constants.CLEARING_AIR_SKIP, 128), 0);

	public static final int DRILL_HEAD_INDEX = 0;

	@Nullable
	private UUID placedBy = null;

	/* FRAME PARAMETERS */

	public final Property<Boolean> hasCoolantResavoir;
	public final Property<Boolean> hasMotorComplex;
	public final Property<Boolean> hasSeismicRelay;
	public final Property<Boolean> hasRing;

	private TileMotorComplex complex = null;
	private TileCoolantResavoir resavoir = null;
	private TileSeismicRelay relay = null;

	private boolean hasBottomStrip = false;
	private boolean hasTopStrip = false;
	private boolean hasLeftStrip = false;
	private boolean hasRightStrip = false;

	private BlockPos currPos = null;
	private BlockPos prevPos = null;

	private boolean prevIsCorner = false;

	private boolean lastIsCorner = false;

	private HashMap<BlockPos, BlockState> brokenFrames = new HashMap<>();
	private HashSet<BlockPos> repairedFrames = new HashSet<>();

	public final Property<List<BlockPos>> corners;
	public final Property<Boolean> cornerOnRight;

	private boolean hasHandledDecay = false;

	public final Property<Boolean> isAreaCleared;

	private int heightShiftCA = 0;
	private int widthShiftCA = 0;
	private int tickDelayCA = 0;

	private int widthShiftCR = 0;

	/* MINING PARAMETERS */

	// want these seperate to prevent potential mixups
	private int lengthShiftMiner = 0;
	private int heightShiftMiner = 1;
	private int widthShiftMiner = 0;
	private int tickDelayMiner = 0;

	public final Property<BlockPos> miningPos;
	public final Property<BlockPos> prevMiningPos;

	public final Property<Boolean> isFinished;

	private boolean widthReverse = false;
	private boolean lengthReverse = false;

	public final Property<Double> quarryPowerUsage;
	public final Property<Double> setupPowerUsage;
	public final Property<Boolean> isPowered;
	public final Property<Boolean> hasHead;
	public final Property<Integer> currHead;

	public final Property<Boolean> hasItemVoid;
	public final Property<Integer> fortuneLevel;
	public final Property<Integer> silkTouchLevel;
	public final Property<Integer> unbreakingLevel;

	// these values are used to deal with client tick desync and provide more
	// complex information
	// on how the quarry should be rendered
	public final Property<Integer> speed;
	public final Property<Integer> progressCounter;
	public final Property<Boolean> running;
	public final Property<Boolean> isTryingToMineFrame;

	private int widthShiftMaintainMining = 0;
	private boolean cont = false;

	// Client Parameters
	private QuarryRenderManger renderHandler = null;

	public TileQuarry(BlockPos pos, BlockState state) {
		super(ElectrodynamicsTiles.TILE_QUARRY.get(), pos, state);

		hasMotorComplex = property(new Property<>(PropertyTypes.BOOLEAN, "hasmotorcomplex", false));
		hasCoolantResavoir = property(new Property<>(PropertyTypes.BOOLEAN, "hascoolantresavoir", false));
		hasSeismicRelay = property(new Property<>(PropertyTypes.BOOLEAN, "hasseismicrelay", false));
		hasRing = property(new Property<>(PropertyTypes.BOOLEAN, "hasring", false));
		cornerOnRight = property(new Property<>(PropertyTypes.BOOLEAN, "corneronright", false));
		isAreaCleared = property(new Property<>(PropertyTypes.BOOLEAN, "areaClear", false));

		corners = property(new Property<>(PropertyTypes.BLOCK_POS_LIST, "corners", List.of(BlockEntityUtils.OUT_OF_REACH, BlockEntityUtils.OUT_OF_REACH, BlockEntityUtils.OUT_OF_REACH, BlockEntityUtils.OUT_OF_REACH)));
		miningPos = property(new Property<>(PropertyTypes.BLOCK_POS, "miningpos", BlockEntityUtils.OUT_OF_REACH));
		prevMiningPos = property(new Property<>(PropertyTypes.BLOCK_POS, "prevminingpos", BlockEntityUtils.OUT_OF_REACH));

		quarryPowerUsage = property(new Property<>(PropertyTypes.DOUBLE, "quarrypowerusage", 0.0));
		setupPowerUsage = property(new Property<>(PropertyTypes.DOUBLE, "setuppowerusage", 0.0));
		isPowered = property(new Property<>(PropertyTypes.BOOLEAN, "ispowered", false));
		hasHead = property(new Property<>(PropertyTypes.BOOLEAN, "hashead", false));
		currHead = property(new Property<>(PropertyTypes.INTEGER, "headtype", -1));

		hasItemVoid = property(new Property<>(PropertyTypes.BOOLEAN, "hasitemvoid", false));
		fortuneLevel = property(new Property<>(PropertyTypes.INTEGER, "fortunelevel", 0));
		silkTouchLevel = property(new Property<>(PropertyTypes.INTEGER, "silktouchlevel", 0));
		unbreakingLevel = property(new Property<>(PropertyTypes.INTEGER, "unbreakinglevel", 0));

		isFinished = property(new Property<>(PropertyTypes.BOOLEAN, "isfinished", false));
		speed = property(new Property<>(PropertyTypes.INTEGER, "speed", 0));
		progressCounter = property(new Property<>(PropertyTypes.INTEGER, "progresscounter", 0));
		running = property(new Property<>(PropertyTypes.BOOLEAN, "isrunning", false));
		isTryingToMineFrame = property(new Property<>(PropertyTypes.BOOLEAN, "istryingtomineframe", false));

		addComponent(new ComponentPacketHandler(this));
		addComponent(new ComponentTickable(this).tickServer(this::tickServer).tickClient(this::tickClient));
		addComponent(new ComponentElectrodynamic(this, false, true).setInputDirections(BlockEntityUtils.MachineDirection.BOTTOM).voltage(ElectrodynamicsCapabilities.DEFAULT_VOLTAGE * 2).maxJoules(Constants.QUARRY_USAGE_PER_TICK * CAPACITY));
		addComponent(new ComponentInventory(this, InventoryBuilder.newInv().inputs(7).outputs(9).upgrades(3)).validUpgrades(ContainerQuarry.VALID_UPGRADES).valid(machineValidator()).setSlotsByDirection(BlockEntityUtils.MachineDirection.FRONT, 0, 7, 8, 9, 10, 11, 12, 13, 14, 15));
		addComponent(new ComponentContainerProvider(SubtypeMachine.quarry, this).createMenu((id, player) -> new ContainerQuarry(id, player, getComponent(IComponentType.Inventory), getCoordsArray())));
	}

	private void tickServer(ComponentTickable tick) {
		if (isFinished.get()) {
			running.set(false);
			if (!hasHandledDecay) {
				handleFramesDecay();
				hasHandledDecay = true;
			}
			return;
		}
		speed.set(complex == null ? 0 : complex.speed.get() + tickDelayMiner);
		BlockPos pos = getBlockPos();
		if (GenericMachineBlock.IPLAYERSTORABLE_MAP.containsKey(pos)) {
			setPlayer(GenericMachineBlock.IPLAYERSTORABLE_MAP.get(pos));
			GenericMachineBlock.IPLAYERSTORABLE_MAP.remove(pos);
		}

		// check surrounding components every 5 ticks
		if ((tick.getTicks() + 1) % 5 == 0) {
			checkComponents();
		}

		// return if the quarry still does not have components
		if (!hasSeismicRelay.get()) {
			running.set(false);
			if (hasCorners() && !hasHandledDecay && isAreaCleared.get()) {
				handleFramesDecay();
			}
			return;
		}

		// return if the quarry still does not have components
		if (!hasSeismicRelay.get()) {
			running.set(false);
			return;
		}

		// if the quarry can still mine and doesn't have a ring, remedy that
		if (!hasRing.get() && tick.getTicks() % (3 + tickDelayCA) == 0 && !isFinished.get()) {
			if (isAreaCleared.get()) {
				checkRing();
			} else {
				clearArea();
			}
		}

		// if the quarry still doesn't have a ring return
		if (!hasRing.get()) {
			running.set(false);
			return;
		}

		Level world = getLevel();
		// clean the ring for obstructions
		if (tick.getTicks() % 4 == 0) {
			cleanRing();
		}
		// if frames were broken purposefully repair them
		if (!brokenFrames.isEmpty()) {
			handleBrokenFrames();
		}
		// set the tile data for the repaired frames
		if (!repairedFrames.isEmpty()) {
			handleRepairedFrames();
		}

		// if the quarry components are invalid, return
		if (areComponentsNull()) {
			running.set(false);
			return;
		}

		// remove blocks from the mined area
		if (tick.getTicks() % 4 == 0 && Constants.MAINTAIN_MINING_AREA) {
			maintainMiningArea();
		}

		boolean shouldFail = false;
		ComponentElectrodynamic electro = getComponent(IComponentType.Electrodynamic);

		isPowered.set(electro.getJoulesStored() >= quarryPowerUsage.get());

		// if there isn't enough power don't do anything
		if (!isPowered.get()) {
			running.set(false);
			shouldFail = true;
		}

		// if the motor complex is in an invalid state return
		if (!complex.isPowered.get() || complex.speed.get() <= 0) {
			running.set(false);
			shouldFail = true;
		}

		int fluidUse = (int) (complex.powerMultiplier.get() * Constants.QUARRY_WATERUSAGE_PER_BLOCK);
		ComponentInventory inv = getComponent(IComponentType.Inventory);
		if (inv.getItem(DRILL_HEAD_INDEX).getItem() instanceof ItemDrillHead head) {
			hasHead.set(true);
			writeHeadType(head.head);
		} else {
			hasHead.set(false);
			writeHeadType(null);
			running.set(false);
		}

		if (shouldFail) {
			return;
		}

		if (!inv.areOutputsEmpty() || !resavoir.hasEnoughFluid(fluidUse) || !hasHead.get()) {
			running.set(false);
			return;
		}

		running.set(true);
		progressCounter.set(progressCounter.get() + 1);

		if (progressCounter.get() < speed.get()) {
			return;
		}

		// if there is no room for mined blocks, the fluid resavoir doesn't have enough
		// fluid, or there isn't a drill head, return

		progressCounter.set(0);

		if (canMineIfFrame(world.getBlockState(miningPos.get()), miningPos.get())) {
			isTryingToMineFrame.set(true);
			return;
		}
		isTryingToMineFrame.set(false);

		resavoir.drainFluid(fluidUse);
		BlockPos cornerStart = corners.get().get(3);
		BlockPos cornerEnd = corners.get().get(0);
		int deltaW = (int) Math.signum(cornerStart.getX() - cornerEnd.getX());
		int deltaL = (int) Math.signum(cornerStart.getZ() - cornerEnd.getZ());
		int width = cornerStart.getX() - cornerEnd.getX() - 2 * deltaW;
		int length = cornerStart.getZ() - cornerEnd.getZ() - 2 * deltaL;
		// if we have the quarry mine the current block on the next tick, it
		// deals with the issue of the client pos always being one tick behind
		// the server's
		cont = true;
		if (!miningPos.get().equals(BlockEntityUtils.OUT_OF_REACH)) {
			BlockState miningState = world.getBlockState(miningPos.get());
			float strength = miningState.getDestroySpeed(world, miningPos.get());
			if (!skipBlock(miningState) && strength >= 0) {
				cont = mineBlock(miningPos.get(), miningState, strength, world, inv.getItem(0), inv, getPlayer((ServerLevel) world));
			}
		}
		prevMiningPos.set(new BlockPos(miningPos.get()));

		miningPos.set(new BlockPos(cornerStart.getX() - widthShiftMiner - deltaW, cornerStart.getY() - heightShiftMiner, cornerStart.getZ() - lengthShiftMiner - deltaL));

		BlockState state = world.getBlockState(miningPos.get());
		boolean shouldSkip = true;

		// if the mined block shouldn't be skipped then don't skip it
		if (!cont) {
			return;
		}
		// loop until either the mining skip limit is reached or a non-skipable block is
		// found
		while (shouldSkip) {
			if (miningPos.get().getY() <= world.getMinBuildHeight()) {
				heightShiftMiner = 1;
				isFinished.set(true);
				progressCounter.set(0);
				running.set(false);
				break;
			}
			if (lengthReverse ? lengthShiftMiner == 0 : lengthShiftMiner == length) {
				lengthReverse = !lengthReverse;
				if (widthReverse ? widthShiftMiner == 0 : widthShiftMiner == width) {
					widthReverse = !widthReverse;
					heightShiftMiner++;
				} else if (widthReverse) {
					widthShiftMiner -= deltaW;
				} else {
					widthShiftMiner += deltaW;
				}
			} else if (lengthReverse) {
				lengthShiftMiner -= deltaL;
			} else {
				lengthShiftMiner += deltaL;
			}
			miningPos.set(new BlockPos(cornerStart.getX() - widthShiftMiner - deltaW, cornerStart.getY() - heightShiftMiner, cornerStart.getZ() - lengthShiftMiner - deltaL));
			state = world.getBlockState(miningPos.get());
			shouldSkip = skipBlock(state);
		}
		float strength = state.getDestroySpeed(world, miningPos.get());
		tickDelayMiner = (int) strength;
		if (!shouldSkip && strength >= 0) {
			electro.joules(electro.getJoulesStored() - quarryPowerUsage.get());
		}
		if (shouldSkip) {
			if (lengthReverse ? lengthShiftMiner == 0 : lengthShiftMiner == length) {
				lengthReverse = !lengthReverse;
				if (widthReverse ? widthShiftMiner == 0 : widthShiftMiner == width) {
					widthReverse = !widthReverse;
					heightShiftMiner++;
					if (miningPos.get().getY() <= world.getMinBuildHeight()) {
						heightShiftMiner = 1;
						isFinished.set(true);
						progressCounter.set(0);
						running.set(false);
					}
				} else if (widthReverse) {
					widthShiftMiner -= deltaW;
				} else {
					widthShiftMiner += deltaW;
				}
			} else if (lengthReverse) {
				lengthShiftMiner -= deltaL;
			} else {
				lengthShiftMiner += deltaL;
			}
		}

	}

	public void addBrokenFrame(BlockPos frame, BlockState frameState) {
		brokenFrames.put(frame, frameState);
	}

	/**
	 * Looks for obstructions that have been placed in the previously mined area If one is found, the mining position is set to that position
	 */
	private void maintainMiningArea() {
		Level world = getLevel();
		BlockPos cornerStart = corners.get().get(3);
		BlockPos cornerEnd = corners.get().get(0);
		int deltaW = (int) Math.signum(cornerStart.getX() - cornerEnd.getX());
		int deltaL = (int) Math.signum(cornerStart.getZ() - cornerEnd.getZ());
		int width = cornerStart.getX() - cornerEnd.getX() - 2 * deltaW;
		int length = cornerStart.getZ() - cornerEnd.getZ();
		BlockPos startPos = new BlockPos(cornerStart.getX() - widthShiftMaintainMining - deltaW, cornerStart.getY(), cornerStart.getZ() - deltaL);
		BlockPos endPos = new BlockPos(cornerStart.getX() - widthShiftMaintainMining - deltaW, miningPos.get().getY() + 1, cornerStart.getZ() - length + deltaL);
		Iterator<BlockPos> positions = BlockPos.betweenClosedStream(startPos, endPos).iterator();
		BlockPos pos;
		while (positions.hasNext()) {
			pos = positions.next();
			BlockState state = world.getBlockState(pos);
			if (!skipBlock(state)) {
				int newWidthShift = -1 * (pos.getX() - (cornerStart.getX() - deltaW));
				int newLengthShift = -1 * (pos.getZ() - (cornerStart.getZ() - deltaL));
				widthShiftMiner = newWidthShift;
				lengthShiftMiner = newLengthShift;
				widthShiftMaintainMining = 0;
				heightShiftMiner = startPos.getY() - pos.getY() - 1;
				prevMiningPos.set(new BlockPos(miningPos.get()));
				miningPos.set(pos);
				widthReverse = false;
				lengthReverse = false;
				progressCounter.set(0);
				return;
			}
		}

		if (widthShiftMaintainMining == width) {
			widthShiftMaintainMining = 0;
		} else {
			widthShiftMaintainMining += deltaW;
		}

	}

	/**
	 * clears obstructions from the inside of the frame ring
	 */
	private void cleanRing() {
		Level world = getLevel();
		BlockPos cornerStart = corners.get().get(3);
		BlockPos cornerEnd = corners.get().get(0);
		int deltaW = (int) Math.signum(cornerStart.getX() - cornerEnd.getX());
		int deltaL = (int) Math.signum(cornerStart.getZ() - cornerEnd.getZ());
		int width = cornerStart.getX() - cornerEnd.getX() - 2 * deltaW;
		int length = cornerStart.getZ() - cornerEnd.getZ();
		BlockPos startPos = new BlockPos(cornerStart.getX() - widthShiftCR - deltaW, cornerStart.getY(), cornerStart.getZ() - deltaL);
		BlockPos endPos = new BlockPos(cornerStart.getX() - widthShiftCR - deltaW, cornerStart.getY(), cornerStart.getZ() - length + deltaL);
		Stream<BlockPos> positions = BlockPos.betweenClosedStream(startPos, endPos);
		positions.forEach(pos -> {
			BlockState state = world.getBlockState(pos);
			if (!skipBlock(state)) {
				boolean canMine = world.destroyBlock(pos, false, getPlayer((ServerLevel) world)) || Constants.BYPASS_CLAIMS;
				if (canMine) {
					world.setBlockAndUpdate(pos, AIR);
					world.playSound(null, pos, state.getSoundType().getBreakSound(), SoundSource.BLOCKS, 0.5F, 1.0F);
				}
			}
		});
		if (widthShiftCR == width) {
			widthShiftCR = 0;
		} else {
			widthShiftCR += deltaW;
		}
	}

	private boolean mineBlock(BlockPos pos, BlockState state, float strength, Level world, ItemStack drillHead, ComponentInventory inv, Player player) {
		boolean sucess = world.destroyBlock(pos, false, player);
		if (sucess) {
			SubtypeDrillHead head = ((ItemDrillHead) drillHead.getItem()).head;
			if (!head.isUnbreakable) {
				int durabilityUsed = (int) (Math.ceil(strength) / (unbreakingLevel.get() + 1.0F));
				if (drillHead.getDamageValue() + durabilityUsed >= drillHead.getMaxDamage()) {
					world.playSound(null, getBlockPos(), SoundEvents.ITEM_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);
					drillHead.shrink(1);
				} else {
					drillHead.setDamageValue(drillHead.getDamageValue() + durabilityUsed);
				}
			}
			// TODO make this work with custom mining tiers
			ItemStack pickaxe = new ItemStack(Items.NETHERITE_PICKAXE);
			if (silkTouchLevel.get() > 0) {
				pickaxe.enchant(level.registryAccess().registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(Enchantments.SILK_TOUCH), silkTouchLevel.get());
			} else if (fortuneLevel.get() > 0) {
				pickaxe.enchant(level.registryAccess().registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(Enchantments.FORTUNE), fortuneLevel.get());
			}
			List<ItemStack> lootItems = Block.getDrops(state, (ServerLevel) world, pos, null, null, pickaxe);
			List<ItemStack> voidItemStacks = inv.getInputContents().subList(1, inv.getInputContents().size());
			List<Item> voidItems = new ArrayList<>();
			voidItemStacks.forEach(h -> voidItems.add(h.getItem()));
			List<ItemStack> items = new ArrayList<>();

			if (hasItemVoid.get()) {
				lootItems.forEach(lootItem -> {
					if (!voidItems.contains(lootItem.getItem())) {
						items.add(lootItem);
					}
				});
			} else {
				items.addAll(lootItems);
			}

			int max = inv.getOutputStartIndex() + inv.getOutputContents().size();

			for(ItemStack item : items) {

				for (int i = inv.getOutputStartIndex(); i < max; i++) {

					ItemStack contained = inv.getItem(i);

					int room = contained.getMaxStackSize() - contained.getCount();

					int amtAccepted = Math.min(room, item.getCount());

					if(amtAccepted == 0) {
						continue;
					}

					if (contained.isEmpty()) {

						inv.setItem(i, new ItemStack(item.getItem(), amtAccepted));

						item.shrink(amtAccepted);

					} else if (ItemUtils.testItems(item.getItem(), contained.getItem())) {

						contained.grow(amtAccepted);

						item.shrink(amtAccepted);

						inv.setChanged();

					}
					if(item.isEmpty()) {
						break;
					}
				}

			}

			world.playSound(null, pos, state.getSoundType().getBreakSound(), SoundSource.BLOCKS, 1.0F, 1.0F);
		}
		return sucess;
	}

	// responsible for clearing initial obstructions from the mining area
	private void clearArea() {
		ComponentElectrodynamic electro = getComponent(IComponentType.Electrodynamic);
		setupPowerUsage.set(Constants.QUARRY_USAGE_PER_TICK);
		isPowered.set(electro.getJoulesStored() >= setupPowerUsage.get());
		if (hasCorners() && isPowered.get()) {
			Level world = getLevel();
			BlockPos start = corners.get().get(3);
			BlockPos end = corners.get().get(0);
			int width = start.getX() - end.getX();
			int height = start.getZ() - end.getZ();
			int deltaW = (int) Math.signum(width);
			int deltaH = (int) Math.signum(height);
			BlockPos checkPos = new BlockPos(start.getX() - widthShiftCA, start.getY(), start.getZ() - heightShiftCA);
			BlockState state = world.getBlockState(checkPos);
			if (canMineIfFrame(state, checkPos)) {
				isTryingToMineFrame.set(true);
				return;
			}
			isTryingToMineFrame.set(true);
			float strength = state.getDestroySpeed(world, checkPos);
			int blockSkip = 0;
			while (skipBlock(state) && blockSkip < CLEAR_SKIP) {
				if (heightShiftCA == height) {
					heightShiftCA = 0;
					if (widthShiftCA == width) {
						isAreaCleared.set(true);
						widthShiftCA = 0;
						tickDelayCA = 0;
						return;
					}
					widthShiftCA += deltaW;
				} else {
					heightShiftCA += deltaH;
				}
				checkPos = new BlockPos(start.getX() - widthShiftCA, start.getY(), start.getZ() - heightShiftCA);
				state = world.getBlockState(checkPos);
				blockSkip++;
			}
			if (strength >= 0 && electro.getJoulesStored() >= setupPowerUsage.get() * strength) {
				boolean sucess = false;
				if (!skipBlock(state)) {
					tickDelayCA = (int) Math.ceil(strength / 5.0F);
					electro.joules(electro.getJoulesStored() - setupPowerUsage.get() * strength);
					sucess = world.destroyBlock(checkPos, false, getPlayer((ServerLevel) world));
					if (sucess) {
						world.playSound(null, checkPos, state.getSoundType().getBreakSound(), SoundSource.BLOCKS, 1.0F, 1.0F);
					}
				}
				if (sucess) {
					if (heightShiftCA == height) {
						heightShiftCA = 0;
						if (widthShiftCA == width) {
							isAreaCleared.set(true);
							widthShiftCA = 0;
							tickDelayCA = 0;
						} else {
							widthShiftCA += deltaW;
						}
					} else {
						heightShiftCA += deltaH;
					}
				}
			}
		}
	}

	/**
	 * Replaces all broken frames
	 */
	private void handleBrokenFrames() {
		Level world = getLevel();
		brokenFrames.forEach((pos, state) -> {
			world.setBlockAndUpdate(pos, state);
			world.playSound(null, pos, SoundEvents.ANVIL_PLACE, SoundSource.BLOCKS, 0.5F, 1.0F);
			repairedFrames.add(new BlockPos(pos));
		});
		brokenFrames.clear();
		isChanged = true;
	}

	/**
	 * Sets the quarry owner pos in the repaired frames
	 */
	private void handleRepairedFrames() {
		Level world = getLevel();
		Iterator<BlockPos> it = repairedFrames.iterator();
		BlockPos pos;
		BlockEntity entity;
		while (it.hasNext()) {
			pos = it.next();
			entity = world.getBlockEntity(pos);
			if (entity != null && entity instanceof TileFrame frame) {
				frame.setQuarryPos(getBlockPos());
				it.remove();
			}
		}
		isChanged = true;
	}

	private void checkRing() {
		ComponentElectrodynamic electro = getComponent(IComponentType.Electrodynamic);
		if (electro.getJoulesStored() < Constants.QUARRY_USAGE_PER_TICK && hasCorners()) {
			return;
		}
		electro.joules(electro.getJoulesStored() - Constants.QUARRY_USAGE_PER_TICK);
		BlockState cornerState = ElectrodynamicsBlocks.BLOCK_FRAME_CORNER.get().defaultBlockState().setValue(ElectrodynamicsBlockStates.WATERLOGGED, false);
		Level world = getLevel();
		BlockPos frontOfQuarry = corners.get().get(0);
		BlockPos foqFar = corners.get().get(1);
		BlockPos foqCorner = corners.get().get(2);
		BlockPos farCorner = corners.get().get(3);
		Direction facing = getFacing().getOpposite();
		if (prevPos != null) {
			if (hasAllStrips()) {
				hasRing.set(true);
				prevPos = null;
				isFinished.set(false);
				heightShiftMiner = 1;
				widthShiftMiner = 0;
				lengthShiftMiner = 0;
				quarryPowerUsage.set(0.0);
				setupPowerUsage.set(0.0);
				BlockPos cornerStart = corners.get().get(3);
				BlockPos cornerEnd = corners.get().get(0);
				int deltaW = (int) Math.signum(cornerStart.getX() - cornerEnd.getX());
				int deltaL = (int) Math.signum(cornerStart.getZ() - cornerEnd.getZ());
				miningPos.set(new BlockPos(cornerStart.getX() - deltaW, cornerStart.getY() - heightShiftMiner, cornerStart.getZ() - deltaL));
				prevMiningPos.set(new BlockPos(miningPos.get()));
			}
		}
		switch (facing) {
		case EAST:
			if (!hasBottomStrip) {
				stripWithCorners(world, foqCorner, frontOfQuarry, foqCorner.getZ(), frontOfQuarry.getZ(), Direction.SOUTH, Direction.EAST, cornerState, false, false);
				return;
			}
			if (!hasTopStrip) {
				stripWithCorners(world, farCorner, foqFar, farCorner.getZ(), foqFar.getZ(), Direction.SOUTH, Direction.WEST, cornerState, false, true);
				return;
			}
			if (!hasLeftStrip) {
				strip(world, foqCorner, farCorner.getX(), Direction.EAST, Direction.SOUTH, true, true);
				return;
			}
			if (!hasRightStrip) {
				strip(world, frontOfQuarry, foqFar.getX(), Direction.EAST, Direction.NORTH, true, false);
			}
			break;
		case WEST:
			if (!hasBottomStrip) {
				stripWithCorners(world, foqCorner, frontOfQuarry, foqCorner.getZ(), frontOfQuarry.getZ(), Direction.NORTH, Direction.WEST, cornerState, false, false);
				return;
			}
			if (!hasTopStrip) {
				stripWithCorners(world, farCorner, foqFar, foqCorner.getZ(), frontOfQuarry.getZ(), Direction.NORTH, Direction.EAST, cornerState, false, true);
				return;
			}
			if (!hasLeftStrip) {
				strip(world, foqCorner, farCorner.getX(), Direction.WEST, Direction.NORTH, true, true);
				return;
			}
			if (!hasRightStrip) {
				strip(world, frontOfQuarry, foqFar.getX(), Direction.WEST, Direction.SOUTH, true, false);
			}
			break;
		case SOUTH:
			if (!hasBottomStrip) {
				stripWithCorners(world, foqCorner, frontOfQuarry, foqCorner.getX(), frontOfQuarry.getX(), Direction.WEST, Direction.SOUTH, cornerState, true, false);
				return;
			}
			if (!hasTopStrip) {
				stripWithCorners(world, farCorner, foqFar, farCorner.getX(), foqFar.getX(), Direction.WEST, Direction.NORTH, cornerState, true, true);
				return;
			}
			if (!hasLeftStrip) {
				strip(world, foqCorner, farCorner.getZ(), Direction.SOUTH, Direction.WEST, false, true);
				return;
			}
			if (!hasRightStrip) {
				strip(world, frontOfQuarry, foqFar.getZ(), Direction.SOUTH, Direction.EAST, false, false);
			}
			break;
		case NORTH:
			if (!hasBottomStrip) {
				stripWithCorners(world, foqCorner, frontOfQuarry, foqCorner.getX(), frontOfQuarry.getX(), Direction.EAST, Direction.NORTH, cornerState, true, false);
				return;
			}
			if (!hasTopStrip) {
				stripWithCorners(world, farCorner, foqFar, farCorner.getX(), foqFar.getX(), Direction.EAST, Direction.SOUTH, cornerState, true, true);
				return;
			}
			if (!hasLeftStrip) {
				strip(world, foqCorner, farCorner.getZ(), Direction.NORTH, Direction.EAST, false, true);
				return;
			}
			if (!hasRightStrip) {
				strip(world, frontOfQuarry, foqFar.getZ(), Direction.NORTH, Direction.WEST, false, false);
			}
			break;
		default:
			break;
		}

	}

	private void stripWithCorners(Level world, BlockPos startPos, BlockPos endPos, int startCV, int endCV, Direction relative, Direction frameFace, BlockState cornerState, boolean currPosX, boolean top) {
		if (currPos == null) {
			currPos = startPos;
		}
		if (canMineIfFrame(world.getBlockState(currPos), currPos)) {
			isTryingToMineFrame.set(true);
			return;
		}
		isTryingToMineFrame.set(false);

		if ((currPosX ? currPos.getX() : currPos.getZ()) == startCV) {
			world.setBlockAndUpdate(startPos, cornerState);
			repairedFrames.add(startPos);
			prevIsCorner = true;
		} else if ((currPosX ? currPos.getX() : currPos.getZ()) == endCV) {
			world.setBlockAndUpdate(endPos, cornerState);
			repairedFrames.add(endPos);
			if (top) {
				hasTopStrip = true;
			} else {
				hasBottomStrip = true;
			}
			prevPos = new BlockPos(currPos.getX(), currPos.getY(), currPos.getZ());
			prevIsCorner = true;
			currPos = null;
			return;
		} else {
			world.setBlockAndUpdate(currPos, ElectrodynamicsBlocks.BLOCK_FRAME.get().defaultBlockState().setValue(ElectrodynamicsBlockStates.FACING, frameFace).setValue(ElectrodynamicsBlockStates.WATERLOGGED, Boolean.FALSE));
			repairedFrames.add(currPos);
			prevIsCorner = false;
		}
		world.playSound(null, currPos, SoundEvents.ANVIL_PLACE, SoundSource.BLOCKS, 0.5F, 1.0F);
		prevPos = new BlockPos(currPos.getX(), currPos.getY(), currPos.getZ());
		currPos = currPos.relative(cornerOnRight.get() ? relative.getOpposite() : relative);
	}

	private void strip(Level world, BlockPos startPos, int endCV, Direction relative, Direction frameFace, boolean currPosX, boolean left) {
		if (currPos == null) {
			currPos = startPos.relative(relative);
		}
		if (canMineIfFrame(world.getBlockState(currPos), currPos)) {
			isTryingToMineFrame.set(true);
			return;
		}
		isTryingToMineFrame.set(false);
		world.setBlockAndUpdate(currPos, ElectrodynamicsBlocks.BLOCK_FRAME.get().defaultBlockState().setValue(ElectrodynamicsBlockStates.FACING, cornerOnRight.get() ? frameFace.getOpposite() : frameFace).setValue(ElectrodynamicsBlockStates.WATERLOGGED, Boolean.FALSE));
		repairedFrames.add(currPos);
		world.playSound(null, currPos, SoundEvents.ANVIL_PLACE, SoundSource.BLOCKS, 0.5F, 1.0F);
		prevPos = new BlockPos(currPos.getX(), currPos.getY(), currPos.getZ());
		prevIsCorner = false;
		currPos = currPos.relative(relative);
		if ((currPosX ? currPos.getX() : currPos.getZ()) == endCV) {
			currPos = null;
			if (left) {
				hasLeftStrip = true;
			} else {
				hasRightStrip = true;
			}
		}
	}

	private void checkComponents() {
		Direction facing = getFacing().getOpposite();
		Level world = getLevel();
		BlockPos machinePos = getBlockPos();
		Direction left = facing.getCounterClockWise();
		Direction right = facing.getClockWise();
		BlockEntity leftEntity = world.getBlockEntity(machinePos.relative(left));
		BlockEntity rightEntity = world.getBlockEntity(machinePos.relative(right));
		BlockEntity aboveEntity = world.getBlockEntity(machinePos.above());

		// reformatted to allow for individual components to be missing

		if (leftEntity != null && leftEntity instanceof TileMotorComplex complexin && complexin.getFacing() == left) {
			complex = complexin;
			hasMotorComplex.set(true);
		} else if (rightEntity != null && rightEntity instanceof TileMotorComplex complexin && complexin.getFacing() == right) {
			complex = complexin;
			hasMotorComplex.set(true);
		} else {
			complex = null;
			hasMotorComplex.set(false);
		}

		if (leftEntity != null && leftEntity instanceof TileSeismicRelay relayin && relayin.getFacing() == facing.getOpposite()) {
			corners.set(relayin.markerLocs.get());
			corners.forceDirty();
			cornerOnRight.set(relayin.cornerOnRight);
			relay = relayin;
			hasSeismicRelay.set(true);
		} else if (rightEntity != null && rightEntity instanceof TileSeismicRelay relayin && relayin.getFacing() == facing.getOpposite()) {
			corners.set(relayin.markerLocs.get());
			corners.forceDirty();
			cornerOnRight.set(relayin.cornerOnRight);
			relay = relayin;
			hasSeismicRelay.set(true);
		} else {
			relay = null;
			hasSeismicRelay.set(false);
		}

		if (aboveEntity != null && aboveEntity instanceof TileCoolantResavoir resavoirin) {
			resavoir = resavoirin;
			hasCoolantResavoir.set(true);
		} else {
			resavoir = null;
			hasCoolantResavoir.set(false);
		}

	}

	private boolean areComponentsNull() {
		return complex == null || resavoir == null || relay == null;
	}

	private boolean hasAllStrips() {
		return hasBottomStrip && hasTopStrip && hasLeftStrip && hasRightStrip;
	}

	public boolean hasCorners() {
		return corners.get().size() > 3;
	}

	private boolean skipBlock(BlockState state) {
		return state.isAir() || !state.getFluidState().is(Fluids.EMPTY) || state.is(Blocks.BEDROCK) || miningPos.get().getY() == level.getMinBuildHeight();
	}

	private void tickClient(ComponentTickable tick) {
		if (renderHandler == null) {
			renderHandler = new QuarryRenderManger();
		}
		renderHandler.render(this);
	}

	@Override
	protected void saveAdditional(CompoundTag compound, HolderLookup.Provider registries) {
		super.saveAdditional(compound, registries);
		CompoundTag data = new CompoundTag();

		data.putBoolean("bottomStrip", hasBottomStrip);
		data.putBoolean("topStrip", hasTopStrip);
		data.putBoolean("leftStrip", hasLeftStrip);
		data.putBoolean("rightStrip", hasRightStrip);

		if (currPos != null) {
			data.putInt("currX", currPos.getX());
			data.putInt("currY", currPos.getY());
			data.putInt("currZ", currPos.getZ());
		}
		if (prevPos != null) {
			data.putInt("prevX", prevPos.getX());
			data.putInt("prevY", prevPos.getY());
			data.putInt("prevZ", prevPos.getZ());
		}

		data.putBoolean("prevIsCorner", prevIsCorner);
		data.putBoolean("lastIsCorner", lastIsCorner);

		data.putBoolean("hasDecayed", hasHandledDecay);

		data.putInt("heightShiftCA", heightShiftCA);
		data.putInt("widthShiftCA", widthShiftCA);
		data.putInt("tickDelayCA", tickDelayCA);

		data.putInt("lengthShiftMiner", lengthShiftMiner);
		data.putInt("heightShiftMiner", heightShiftMiner);
		data.putInt("widthShiftMiner", widthShiftMiner);
		data.putInt("tickDelayMiner", tickDelayMiner);

		data.putBoolean("lengthReverse", lengthReverse);
		data.putBoolean("widthReverse", widthReverse);

		data.putInt("widthShiftCR", widthShiftCR);

		data.putInt("widthShiftMaintainMining", widthShiftMaintainMining);

		if (placedBy != null) {
			data.putUUID("placedBy", placedBy);
		}

		data.putBoolean("continue", cont);
		int i = 0;
		for (Entry<BlockPos, BlockState> entry : brokenFrames.entrySet()) {
			data.put("brokenframe" + i, NbtUtils.writeBlockPos(entry.getKey()));
			BlockFrame.writeToNbt(data, "brokenstate" + i, entry.getValue());
			i++;
		}
		data.putInt("brokenframecount", i);

		i = 0;
		for (BlockPos pos : repairedFrames) {
			data.put("repairedframe" + i, NbtUtils.writeBlockPos(pos));
			i++;
		}
		data.putInt("repairedframecount", i);

		compound.put("quarrydata", data);
	}

	@Override
	protected void loadAdditional(CompoundTag compound, HolderLookup.Provider registries) {
		super.loadAdditional(compound, registries);
		CompoundTag data = compound.getCompound("quarrydata");

		hasBottomStrip = data.getBoolean("bottomStrip");
		hasTopStrip = data.getBoolean("topStrip");
		hasLeftStrip = data.getBoolean("leftStrip");
		hasRightStrip = data.getBoolean("rightStrip");

		if (data.contains("currX")) {
			currPos = new BlockPos(data.getInt("currX"), data.getInt("currY"), data.getInt("currZ"));
		} else {
			currPos = null;
		}

		if (data.contains("prevY")) {
			prevPos = new BlockPos(data.getInt("prevX"), data.getInt("prevY"), data.getInt("prevZ"));
		} else {
			prevPos = null;
		}

		prevIsCorner = data.getBoolean("prevIsCorner");
		lastIsCorner = data.getBoolean("lastIsCorner");

		hasHandledDecay = data.getBoolean("hasDecayed");

		heightShiftCA = data.getInt("heightShiftCA");
		widthShiftCA = data.getInt("widthShiftCA");
		tickDelayCA = data.getInt("tickDelayCA");

		lengthShiftMiner = data.getInt("lengthShiftMiner");
		heightShiftMiner = data.getInt("heightShiftMiner");
		widthShiftMiner = data.getInt("widthShiftMiner");
		tickDelayMiner = data.getInt("tickDelayMiner");

		lengthReverse = data.getBoolean("lengthReverse");
		widthReverse = data.getBoolean("widthReverse");

		widthShiftCR = data.getInt("widthShiftCR");

		widthShiftMaintainMining = data.getInt("widthShiftMaintainMining");

		if (data.contains("placedBy")) {
			placedBy = data.getUUID("placedBy");
		}

		cont = data.getBoolean("continue");

		int brokenSize = data.getInt("brokenframecount");
		for (int i = 0; i < brokenSize; i++) {
			BlockPos pos = NbtUtils.readBlockPos(data, "brokenframe" + i).get();
			BlockState state = BlockFrame.readFromNbt(data.getCompound("brokenstate" + i));
			brokenFrames.put(pos, state);
		}

		int repairSize = data.getInt("repairedframecount");
		for (int i = 0; i < repairSize; i++) {
			repairedFrames.add(NbtUtils.readBlockPos(data, "repairedframe" + i).get());
		}

		compound.remove("quarrydata");
	}

	@Override
	public void setRemoved() {
		if (getLevel().isClientSide) {
			HandlerQuarryArm.removeRenderData(getBlockPos());
		}
		super.setRemoved();
	}

	@Override
	public void onBlockDestroyed() {
		if (level.isClientSide) {
			return;
		}
		handleFramesDecayNoVarUpdate();
	}

	public void handleFramesDecay() {
		miningPos.set(BlockEntityUtils.OUT_OF_REACH);
		prevMiningPos.set(BlockEntityUtils.OUT_OF_REACH);
		hasHandledDecay = true;
		isAreaCleared.set(false);
		hasRing.set(false);
		hasBottomStrip = false;
		hasTopStrip = false;
		hasLeftStrip = false;
		hasRightStrip = false;
		lengthReverse = false;
		widthReverse = false;
		isFinished.set(true);
		isChanged = true;
		progressCounter.set(0);
		running.set(false);
		brokenFrames.clear();
		repairedFrames.clear();
		corners.set(new ArrayList<>());
		corners.forceDirty();
		handleFramesDecayNoVarUpdate();
	}

	public void handleFramesDecayNoVarUpdate() {
		if (!hasCorners()) {
			return;
		}
		Level world = getLevel();
		BlockPos frontOfQuarry = corners.get().get(0);
		BlockPos foqFar = corners.get().get(1);
		BlockPos foqCorner = corners.get().get(2);
		BlockPos farCorner = corners.get().get(3);
		for (BlockPos pos : corners.get()) {
			world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
		}
		BlockPos.betweenClosedStream(foqCorner, frontOfQuarry).forEach(pos -> updateState(world, pos));
		BlockPos.betweenClosedStream(farCorner, foqFar).forEach(pos -> updateState(world, pos));
		BlockPos.betweenClosedStream(foqCorner, farCorner).forEach(pos -> updateState(world, pos));
		BlockPos.betweenClosedStream(frontOfQuarry, foqFar).forEach(pos -> updateState(world, pos));
	}

	private static void updateState(Level world, BlockPos pos) {
		BlockState state = world.getBlockState(pos);
		if (state.is(ElectrodynamicsBlocks.BLOCK_FRAME) || state.is(ElectrodynamicsBlocks.BLOCK_FRAME_CORNER)) {
			world.setBlockAndUpdate(pos, state.setValue(ElectrodynamicsBlockStates.QUARRY_FRAME_DECAY, Boolean.TRUE));
		}
	}

	@Override
	public void setPlayer(LivingEntity player) {
		placedBy = player == null ? null : player.getUUID();
	}

	@Override
	public UUID getPlayerID() {
		return placedBy;
	}

	@Nullable
	private FakePlayer getPlayer(ServerLevel world) {
		if (placedBy == null) {
			return null;
		}
		Player player = world.getPlayerByUUID(placedBy);
		if (player != null) {
			return FakePlayerFactory.get(world, player.getGameProfile());
		}
		return null;
	}

	@Override
	public void onInventoryChange(ComponentInventory inv, int slot) {
		super.onInventoryChange(inv, slot);
		if (!inv.getUpgradeContents().isEmpty() && (slot >= inv.getUpgradeSlotStartIndex() || slot == -1)) {
			double quarryPowerMultiplier = 0;
			int silkTouchLevel = 0;
			int fortuneLevel = 0;
			int unbreakingLevel = 0;
			boolean hasItemVoid = false;
			for (ItemStack stack : inv.getUpgradeContents()) {
				if (!stack.isEmpty()) {
					ItemUpgrade upgrade = (ItemUpgrade) stack.getItem();
					for (int i = 0; i < stack.getCount(); i++) {
						switch (upgrade.subtype) {
						case itemvoid:
							hasItemVoid = true;
							if (quarryPowerMultiplier < 1) {
								quarryPowerMultiplier = 1;
							}
							break;
						case silktouch:
							if (fortuneLevel == 0 && silkTouchLevel < 1) {
								silkTouchLevel++;
							}
							if (quarryPowerMultiplier < 1) {
								quarryPowerMultiplier = 1;
							}
							quarryPowerMultiplier = Math.min(quarryPowerMultiplier *= 4, CAPACITY / 10);
							break;
						case fortune:
							if (silkTouchLevel == 0 && fortuneLevel < 3) {
								fortuneLevel++;
							}
							if (quarryPowerMultiplier < 1) {
								quarryPowerMultiplier = 1;
							}
							quarryPowerMultiplier = Math.min(quarryPowerMultiplier *= 2, CAPACITY / 10);
							break;
						case unbreaking:
							if (quarryPowerMultiplier < 1) {
								quarryPowerMultiplier = 1;
							}
							if (unbreakingLevel < 3) {
								unbreakingLevel++;
							}
							quarryPowerMultiplier = Math.min(quarryPowerMultiplier *= 1.5, CAPACITY / 10);
							break;
						default:
							break;
						}
					}
				}
			}
			this.hasItemVoid.set(hasItemVoid);
			this.fortuneLevel.set(fortuneLevel);
			this.silkTouchLevel.set(silkTouchLevel);
			this.unbreakingLevel.set(unbreakingLevel);
			quarryPowerUsage.set(Constants.QUARRY_USAGE_PER_TICK * quarryPowerMultiplier);
		}
	}

	private void writeHeadType(SubtypeDrillHead head) {
		if (head == null) {
			currHead.set(-1);
		} else {
			currHead.set(head.ordinal());
		}
	}

	@Nullable
	public SubtypeDrillHead readHeadType() {
		return currHead.get() == -1 ? null : SubtypeDrillHead.values()[currHead.get()];
	}

	@Nullable
	public TileMotorComplex getMotorComplex() {
		Direction facing = getFacing().getOpposite();
		BlockEntity entity = level.getBlockEntity(getBlockPos().relative(facing.getClockWise()));
		if (entity != null && entity instanceof TileMotorComplex complex) {
			return complex;
		}
		entity = level.getBlockEntity(getBlockPos().relative(facing.getCounterClockWise()));
		if (entity != null && entity instanceof TileMotorComplex complex) {
			return complex;
		}
		return null;
	}

	@Nullable
	public TileCoolantResavoir getFluidResavoir() {
		BlockEntity entity = level.getBlockEntity(getBlockPos().offset(0, 1, 0));
		if (entity != null && entity instanceof TileCoolantResavoir resavoir) {
			return resavoir;
		}

		return null;
	}

	@Nullable
	public TileSeismicRelay getSeismicRelay() {
		Direction facing = getFacing().getOpposite();
		BlockEntity entity = level.getBlockEntity(getBlockPos().relative(facing.getClockWise()));
		if (entity != null && entity instanceof TileSeismicRelay relay) {
			return relay;
		}
		entity = level.getBlockEntity(getBlockPos().relative(facing.getCounterClockWise()));
		if (entity != null && entity instanceof TileSeismicRelay relay) {
			return relay;
		}
		return null;
	}

	public boolean isMotorComplexPowered() {
		if (!level.isClientSide) {
			return complex != null && complex.isPowered.get();
		}
		TileMotorComplex complex = getMotorComplex();
		if (complex == null) {
			return false;
		}
		return complex.isPowered.get();
	}

	public boolean canMineIfFrame(BlockState state, BlockPos pos) {
		if (state.is(ElectrodynamicsBlocks.BLOCK_FRAME) || state.is(ElectrodynamicsBlocks.BLOCK_FRAME_CORNER)) {
			BlockEntity entity = level.getBlockEntity(pos);
			if (entity != null && entity instanceof TileFrame frame) {
				return frame.ownerQuarryPos != null;
			}
		}
		return false;
	}

	@Override
	public int getComparatorSignal() {
		return isFinished.get() ? 15 : 0;
	}

}
