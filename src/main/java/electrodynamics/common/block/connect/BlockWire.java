package electrodynamics.common.block.connect;

import java.util.HashSet;

import com.mojang.serialization.MapCodec;

import electrodynamics.api.References;
import electrodynamics.api.electricity.IInsulator;
import electrodynamics.api.network.cable.IRefreshableCable;
import electrodynamics.api.network.cable.type.IConductor;
import electrodynamics.common.block.connect.util.AbstractRefreshingConnectBlock;
import electrodynamics.common.block.connect.util.EnumConnectType;
import electrodynamics.common.block.states.ElectrodynamicsBlockStates;
import electrodynamics.common.block.subtype.SubtypeWire;
import electrodynamics.common.block.subtype.SubtypeWire.InsulationMaterial;
import electrodynamics.common.block.subtype.SubtypeWire.WireClass;
import electrodynamics.common.block.subtype.SubtypeWire.WireColor;
import electrodynamics.common.network.type.ElectricNetwork;
import electrodynamics.common.settings.Constants;
import electrodynamics.common.tile.electricitygrid.GenericTileWire;
import electrodynamics.common.tile.electricitygrid.TileLogisticalWire;
import electrodynamics.common.tile.electricitygrid.TileWire;
import electrodynamics.common.tile.electricitygrid.transformer.TileGenericTransformer;
import electrodynamics.prefab.tile.types.GenericConnectTile;
import electrodynamics.prefab.utilities.ElectricityUtils;
import electrodynamics.prefab.utilities.Scheduler;
import electrodynamics.prefab.utilities.math.Color;
import electrodynamics.prefab.utilities.object.TransferPack;
import electrodynamics.registers.ElectrodynamicsBlocks;
import electrodynamics.registers.ElectrodynamicsItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.common.Tags;

public class BlockWire extends AbstractRefreshingConnectBlock {

    public static final HashSet<Block> WIRES = new HashSet<>();

    public final SubtypeWire wire;

    public BlockWire(SubtypeWire wire) {
        super(wire.insulation.material.sound(wire.insulation.soundType).strength(0.15f).dynamicShape().noOcclusion().randomTicks(), wire.insulation.radius);
        this.wire = wire;

        if (wire.wireClass != WireClass.LOGISTICAL) {
            WIRES.add(this);
        }
    }

    @Override
    public boolean isFlammable(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
        return !wire.insulation.fireProof;
    }

    @Override
    public void entityInside(BlockState state, Level worldIn, BlockPos pos, Entity entityIn) {
        TileWire tile = (TileWire) worldIn.getBlockEntity(pos);
        if (tile != null && tile.getNetwork() != null && tile.getNetwork().getActiveTransmitted() > 0) {
            int shockVoltage = tile.wire.insulation.shockVoltage;
            if (shockVoltage == 0 || tile.getNetwork().getActiveVoltage() > shockVoltage) {
                ElectricityUtils.electrecuteEntity(entityIn, TransferPack.joulesVoltage(tile.getNetwork().getActiveTransmitted(), tile.getNetwork().getActiveVoltage()));
            }
        }
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (stack.isEmpty() || state.isAir()) {
            return ItemInteractionResult.FAIL;
        }

        Item item = stack.getItem();

        boolean isServerSide = !level.isClientSide;

        BlockPlaceContext newCtx = new BlockPlaceContext(player, hand, stack, hitResult);

        if (item == Items.SHEARS) {

            if (wire.insulation == InsulationMaterial.CERAMIC) {

                if (isServerSide) {

                    Block newWire = ElectrodynamicsBlocks.getBlock(SubtypeWire.getWire(wire.conductor, InsulationMaterial.WOOL, wire.wireClass, WireColor.BLACK));

                    handleDataCopyAndSet(newWire.getStateForPlacement(newCtx), level, pos, player, hand, stack, state);

                    if (!player.isCreative()) {

                        handlePlayerItemDrops(player, ElectrodynamicsItems.ITEM_CERAMICINSULATION.get());

                        stack.hurtAndBreak(1, player, hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);

                    }

                    level.playSound(null, pos, SoundEvents.TUFF_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);
                }

                return ItemInteractionResult.CONSUME;

            }

            if (wire.insulation == InsulationMaterial.WOOL) {

                if (isServerSide) {

                    Block newWire = ElectrodynamicsBlocks.getBlock(SubtypeWire.getWire(wire.conductor, InsulationMaterial.BARE, WireClass.BARE, WireColor.NONE));

                    handleDataCopyAndSet(newWire.getStateForPlacement(newCtx), level, pos, player, hand, stack, state);

                    if (!player.isCreative()) {

                        handlePlayerItemDrops(player, ElectrodynamicsItems.ITEM_INSULATION.get());

                        if (wire.wireClass == WireClass.LOGISTICAL) {

                            handlePlayerItemDrops(player, Items.REDSTONE);

                        }

                        stack.hurtAndBreak(1, player, hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);

                    }

                    level.playSound(null, pos, SoundEvents.SHEEP_SHEAR, SoundSource.BLOCKS, 1.0F, 1.0F);

                }

                return ItemInteractionResult.CONSUME;

            }

            return ItemInteractionResult.FAIL;

        }

        if (item == ElectrodynamicsItems.ITEM_INSULATION.get()) {

            if (wire.insulation == InsulationMaterial.BARE) {

                if (isServerSide) {

                    Block newWire = ElectrodynamicsBlocks.getBlock(SubtypeWire.getWire(wire.conductor, InsulationMaterial.WOOL, WireClass.INSULATED, WireColor.BLACK));

                    handleDataCopyAndSet(newWire.getStateForPlacement(newCtx), level, pos, player, hand, stack, state);

                    if (!player.isCreative()) {

                        stack.shrink(1);

                        player.setItemInHand(hand, stack);

                    }

                    level.playSound(null, pos, SoundEvents.WOOL_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
                }

                return ItemInteractionResult.CONSUME;

            }

            return ItemInteractionResult.FAIL;

        }

        if (item == ElectrodynamicsItems.ITEM_CERAMICINSULATION.get() && wire.insulation == InsulationMaterial.WOOL && wire.wireClass == WireClass.INSULATED) {

            if (isServerSide) {

                Block newWire = ElectrodynamicsBlocks.getBlock(SubtypeWire.getWire(wire.conductor, InsulationMaterial.CERAMIC, WireClass.CERAMIC, WireColor.BLACK));

                handleDataCopyAndSet(newWire.getStateForPlacement(newCtx), level, pos, player, hand, stack, state);

                if (!player.isCreative()) {

                    stack.shrink(1);

                    player.setItemInHand(hand, stack);

                }

                level.playSound(null, pos, SoundEvents.TUFF_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
            }

            return ItemInteractionResult.CONSUME;

        }

        if (item.builtInRegistryHolder().is(Tags.Items.DUSTS_REDSTONE) && wire.insulation == InsulationMaterial.WOOL && wire.wireClass == WireClass.INSULATED) {

            if (isServerSide) {

                Block newWire = ElectrodynamicsBlocks.getBlock(SubtypeWire.getWire(wire.conductor, InsulationMaterial.WOOL, WireClass.LOGISTICAL, WireColor.BLACK));

                handleDataCopyAndSet(newWire.getStateForPlacement(newCtx), level, pos, player, hand, stack, state);

                if (!player.isCreative()) {

                    stack.shrink(1);

                    player.setItemInHand(hand, stack);

                }

                level.playSound(null, pos, SoundEvents.STONE_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
            }

            return ItemInteractionResult.CONSUME;

        }

        WireColor dyeColor = WireColor.getColorFromDye(item);

        if (dyeColor != null && (wire.wireClass == WireClass.INSULATED || wire.wireClass == WireClass.THICK || wire.wireClass == WireClass.CERAMIC || wire.wireClass == WireClass.LOGISTICAL)) {

            if (isServerSide) {

                Block newWire = ElectrodynamicsBlocks.getBlock(SubtypeWire.getWire(wire.conductor, wire.insulation, wire.wireClass, dyeColor));

                handleDataCopyAndSet(newWire.getStateForPlacement(newCtx), level, pos, player, hand, stack, state);

                if (!player.isCreative()) {

                    stack.shrink(1);

                    player.setItemInHand(hand, stack);

                }

                level.playSound(null, pos, SoundEvents.DYE_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
            }

            return ItemInteractionResult.CONSUME;

        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    private void handleDataCopyAndSet(BlockState newWire, Level level, BlockPos pos, Player player, InteractionHand hand, ItemStack stack, BlockState oldWire) {
        BlockState curCamo = Blocks.AIR.defaultBlockState();
        BlockState curScaffold = Blocks.AIR.defaultBlockState();
        BlockEntity entity = level.getBlockEntity(pos);
        if (entity != null && entity instanceof GenericTileWire generic) {
            curCamo = generic.getCamoBlock();
            curScaffold = generic.getScaffoldBlock();
        }
        newWire = Block.updateFromNeighbourShapes(newWire, level, pos);
        newWire = newWire.setValue(ElectrodynamicsBlockStates.HAS_SCAFFOLDING, oldWire.getValue(ElectrodynamicsBlockStates.HAS_SCAFFOLDING));
        level.setBlockAndUpdate(pos, newWire);
        if (level.getBlockEntity(pos) instanceof GenericTileWire generic) {
            generic.camoflaugedBlock.set(curCamo);
            if (!curScaffold.isAir()) {
                generic.scaffoldBlock.set(curScaffold);
            }
        }
    }

    private void handlePlayerItemDrops(Player player, Item... items) {

        for (Item item : items) {

            ItemStack stack = new ItemStack(item);

            if (!player.addItem(stack)) {

                player.level().addFreshEntity(new ItemEntity(player.level(), (int) player.getX(), (int) player.getY(), (int) player.getZ(), stack));

            }

        }
    }

    @Override
    public boolean isSignalSource(BlockState state) {
        return ((BlockWire) state.getBlock()).wire.wireClass.conductsRedstone;
    }

    @Override
    public int getDirectSignal(BlockState blockState, BlockGetter blockAccess, BlockPos pos, Direction side) {
        return blockState.getSignal(blockAccess, pos, side);
    }

    @Override
    public int getSignal(BlockState blockState, BlockGetter blockAccess, BlockPos pos, Direction side) {
        BlockEntity tile = blockAccess.getBlockEntity(pos);
        if (tile instanceof TileLogisticalWire w) {
            return w.isPowered ? 15 : 0;
        }
        return 0;
    }

    @Override
    public int getFlammability(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
        if (wire.insulation.fireProof) {
            return 0;
        }

        return state.hasProperty(BlockStateProperties.WATERLOGGED) && state.getValue(BlockStateProperties.WATERLOGGED) ? 0 : 150;
    }

    @Override
    public int getFireSpreadSpeed(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
        if (wire.insulation.fireProof) {
            return 0;
        }

        return state.hasProperty(BlockStateProperties.WATERLOGGED) && state.getValue(BlockStateProperties.WATERLOGGED) ? 0 : 400;
    }

    @Override
    public void onCaughtFire(BlockState state, Level world, BlockPos pos, Direction face, LivingEntity igniter) {
        super.onCaughtFire(state, world, pos, face, igniter);
        Scheduler.schedule(5, () -> {
            SubtypeWire wire = SubtypeWire.getWire(this.wire.conductor, InsulationMaterial.BARE, WireClass.BARE, WireColor.NONE);
            if (wire == null) {
                world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
            } else {
                world.setBlockAndUpdate(pos, ElectrodynamicsBlocks.getBlock(wire).defaultBlockState());
            }

        });
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TileWire(pos, state);
    }

    @Override
    public BlockState refreshConnections(BlockState otherState, BlockEntity otherTile, BlockState state, BlockEntity thisTile, Direction dir) {
        if(!(thisTile instanceof GenericConnectTile)) {
            return state;
        }
        GenericConnectTile thisConnect = (GenericConnectTile) thisTile;
        EnumConnectType connection = EnumConnectType.NONE;
        if (otherTile instanceof IConductor conductor) {
            if(conductor.getWireType().isDefaultColor() || wire.isDefaultColor() || conductor.getWireColor() == wire.color) {
                connection = EnumConnectType.WIRE;
            } else {
                connection = EnumConnectType.NONE;
            }
        } else if (ElectricityUtils.isElectricReceiver(otherTile, dir.getOpposite()) || checkRedstone(otherState)) {
            connection = EnumConnectType.INVENTORY;
        }
        thisConnect.writeConnection(dir, connection);
        return state;
    }

    private boolean checkRedstone(BlockState otherState) {
        return otherState.isSignalSource() && wire.wireClass == WireClass.LOGISTICAL;
    }

    @Override
    public IRefreshableCable getCableIfValid(BlockEntity tile) {
        if (tile instanceof IConductor conductor && (conductor.getWireType().isDefaultColor() || wire.isDefaultColor() || conductor.getWireColor() == wire.color)) {
            return conductor;
        }
        return null;
    }


    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {

        if (!Constants.CONDUCTORS_BURN_SURROUNDINGS) {
            return;
        }

        if (level.getBlockEntity(pos) instanceof GenericTileWire tile) {

            ElectricNetwork network = tile.getNetwork();

            double voltage = network.getActiveVoltage();

            if (voltage <= 0 || voltage <= wire.insulation.shockVoltage || network.getActiveTransmitted() <= 0) {
                return;
            }

            boolean overMaxVoltage = voltage > TileGenericTransformer.MAX_VOLTAGE_CAP;

            double wireShockVoltage = Math.max(wire.insulation.shockVoltage, 1);

            BlockPos relativePos, firePos;
            BlockState relative;

            for (Direction dir : Direction.values()) {

                relativePos = pos.relative(dir);
                relative = level.getBlockState(relativePos);

                if (relative.isAir()) {
                    continue;
                }

                boolean isFlammable = relative.isFlammable(level, relativePos, dir);

                if (relative.getBlock() instanceof BlockWire) {

                    continue;

                }
                if (relative.getBlock() instanceof IInsulator insulator) {

                    if (overMaxVoltage && voltage > insulator.getMaximumVoltage()) {

                        level.playSound(null, relativePos, insulator.getBreakingSound(), SoundSource.BLOCKS, 1.0F, 1.0F);
                        level.destroyBlock(relativePos, false);

                    }

                    continue;

                }
                if (overMaxVoltage) {

                    if (isFlammable || relative.getBlock().getExplosionResistance() < Constants.BLOCK_VAPORIZATION_HARDNESS) {

                        level.playSound(null, relativePos, SoundEvents.LAVA_EXTINGUISH, SoundSource.BLOCKS, 1.0F, 1.0F);
                        level.destroyBlock(relativePos, false);

                    }

                    continue;

                } else if (!isFlammable) {

                    continue;

                }

                int flamability = relative.getFlammability(level, relativePos, dir);

                if (flamability <= 0) {
                    continue;
                }

                int overvoltage = (int) Math.ceil((voltage / wireShockVoltage));

                if (flamability > overvoltage) {
                    continue;
                }

                boolean blockCaughtFire = false;

                for (Direction relDir : Direction.values()) {

                    firePos = relativePos.relative(relDir);

                    if (firePos.equals(pos) || !BaseFireBlock.canBePlacedAt(level, firePos, (relDir == Direction.DOWN || relDir == Direction.UP) ? dir : relDir.getOpposite())) {
                        continue;
                    }

                    level.setBlock(firePos, BaseFireBlock.getState(level, firePos), 11);

                    blockCaughtFire = true;

                    break;

                }

                if (blockCaughtFire) {
                    continue;
                }

                level.playSound(null, pos, SoundEvents.LAVA_EXTINGUISH, SoundSource.BLOCKS, 1.0F, 1.0F);
                level.destroyBlock(pos, false);

                break;

            }

        }

    }

    @EventBusSubscriber(value = Dist.CLIENT, modid = References.ID, bus = EventBusSubscriber.Bus.MOD)
    private static class ColorHandlerInternal {

        @SubscribeEvent
        public static void registerColoredBlocks(RegisterColorHandlersEvent.Block event) {
            WIRES.forEach(block -> event.register((state, level, pos, tintIndex) -> {
                if (tintIndex == 0) {
                    return ((BlockWire) block).wire.color.color.color();
                }
                return Color.WHITE.color();
            }, block));
        }
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        throw new UnsupportedOperationException("Need to implement CODEC");
    }

}
