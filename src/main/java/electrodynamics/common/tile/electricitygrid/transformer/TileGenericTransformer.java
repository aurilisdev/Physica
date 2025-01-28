package electrodynamics.common.tile.electricitygrid.transformer;

import electrodynamics.api.capability.types.electrodynamic.ICapabilityElectrodynamic;
import electrodynamics.api.capability.types.electrodynamic.ICapabilityElectrodynamic.LoadProfile;
import electrodynamics.common.settings.Constants;
import electrodynamics.prefab.properties.Property;
import electrodynamics.prefab.properties.PropertyTypes;
import electrodynamics.prefab.sound.SoundBarrierMethods;
import electrodynamics.prefab.sound.utils.ITickableSound;
import electrodynamics.prefab.tile.GenericTile;
import electrodynamics.prefab.tile.components.type.ComponentElectrodynamic;
import electrodynamics.prefab.tile.components.type.ComponentPacketHandler;
import electrodynamics.prefab.tile.components.type.ComponentTickable;
import electrodynamics.prefab.utilities.BlockEntityUtils;
import electrodynamics.prefab.utilities.ElectricityUtils;
import electrodynamics.prefab.utilities.object.CachedTileOutput;
import electrodynamics.prefab.utilities.object.TransferPack;
import electrodynamics.registers.ElectrodynamicsCapabilities;
import electrodynamics.registers.ElectrodynamicsSounds;
import electrodynamics.registers.ElectrodynamicsTiles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public abstract class TileGenericTransformer extends GenericTile implements ITickableSound {

    public static final double MAX_VOLTAGE_CAP = ElectrodynamicsCapabilities.DEFAULT_VOLTAGE * Math.pow(2, 8); // 120 * 2 ^ 8 = 30,720
    public static final double MIN_VOLTAGE_CAP = ElectrodynamicsCapabilities.DEFAULT_VOLTAGE / Math.pow(2, 8); // 120 / 2 ^ 8 = 0.46875

    public CachedTileOutput output;

    public final Property<TransferPack> lastTransfer = property(new Property<>(PropertyTypes.TRANSFER_PACK, "lasttransfer", TransferPack.EMPTY)).setNoSave();
    public final Property<Long> lastTransferTime = property(new Property<>(PropertyTypes.LONG, "lasttransfertime", 0L)).setNoSave();

    public boolean locked = false;

    private boolean isPlayingSound = false;

    public static final BlockEntityUtils.MachineDirection OUTPUT = BlockEntityUtils.MachineDirection.FRONT;
    public static final BlockEntityUtils.MachineDirection INPUT = BlockEntityUtils.MachineDirection.BACK;

    public TileGenericTransformer(BlockEntityType<?> type, BlockPos worldPosition, BlockState blockState) {
        super(type, worldPosition, blockState);
        addComponent(new ComponentPacketHandler(this));
        if (Constants.SHOULD_TRANSFORMER_HUM) {
            addComponent(new ComponentTickable(this).tickClient(this::tickClient));
        }
        addComponent(new ComponentElectrodynamic(this, true, true).receivePower(this::receivePower).getConnectedLoad(this::getConnectedLoad).setOutputDirections(OUTPUT).setInputDirections(INPUT).voltage(-1.0).getAmpacity(this::getAmpacity).getMinimumVoltage(this::getMinimumVoltage));
    }

    public void tickClient(ComponentTickable tickable) {
        if (level.getGameTime() - lastTransferTime.get() > 20L) {
            lastTransfer.set(TransferPack.EMPTY);
        }
        if (!isPlayingSound && shouldPlaySound()) {
            isPlayingSound = true;
            SoundBarrierMethods.playTransformerSound(ElectrodynamicsSounds.SOUND_TRANSFORMERHUM.get(), SoundSource.BLOCKS, this, 1.0F, 1.0F, true);
        }
    }

    // We can assume this runs on the server
    public TransferPack receivePower(TransferPack transfer, boolean debug) {
        Direction facing = getFacing();
        if (locked) {
            return TransferPack.EMPTY;
        }
        if (output == null) {
            output = new CachedTileOutput(level, worldPosition.relative(facing));
        }
        if (output.getSafe() == null) {
            return TransferPack.EMPTY;
        }
        double resultVoltage = transfer.getVoltage() * getCoilRatio();
        if (resultVoltage != 0) {
            resultVoltage = Mth.clamp(resultVoltage, MIN_VOLTAGE_CAP, MAX_VOLTAGE_CAP);
        }
        locked = true;
        TransferPack returner = ElectricityUtils.receivePower(output.getSafe(), facing.getOpposite(), TransferPack.joulesVoltage(transfer.getJoules() * Constants.TRANSFORMER_EFFICIENCY, resultVoltage), debug);
        locked = false;
        TransferPack toReturn = TransferPack.joulesVoltage(returner.getJoules() / Constants.TRANSFORMER_EFFICIENCY, returner.getVoltage() / getCoilRatio());
        if (!debug && toReturn.getVoltage() > 0) {
            lastTransfer.set(toReturn);
            lastTransferTime.set(level.getGameTime());

        }
        return toReturn;
    }

    public TransferPack getConnectedLoad(LoadProfile lastEnergy, Direction dir) {
        Direction facing = getFacing();
        if (facing.getOpposite() != dir) {
            return TransferPack.EMPTY;
        }
        if (locked) {
            return TransferPack.EMPTY;
        }
        if (output == null) {
            output = new CachedTileOutput(level, worldPosition.relative(facing));
        }
        if (output.getSafe() == null) {
            return TransferPack.EMPTY;
        }
        LoadProfile transformed = new LoadProfile(TransferPack.joulesVoltage(lastEnergy.lastUsage().getJoules() * Constants.TRANSFORMER_EFFICIENCY, lastEnergy.lastUsage().getVoltage() * getCoilRatio()), TransferPack.joulesVoltage(lastEnergy.maximumAvailable().getJoules() * Constants.TRANSFORMER_EFFICIENCY, lastEnergy.maximumAvailable().getVoltage() * getCoilRatio()));

        locked = true;

        BlockEntity outputTile = output.getSafe();

        ICapabilityElectrodynamic electro = level.getCapability(ElectrodynamicsCapabilities.CAPABILITY_ELECTRODYNAMIC_BLOCK, outputTile.getBlockPos(), outputTile.getBlockState(), outputTile, dir);

        TransferPack returner = TransferPack.EMPTY;

        if (electro != null) {
            returner = electro.getConnectedLoad(transformed, dir);
        }

        // TransferPack returner = ((BlockEntity) output.getSafe()).getCapability(ElectrodynamicsCapabilities.ELECTRODYNAMIC,
        // dir).map(cap -> cap.getConnectedLoad(transformed, dir)).orElse(TransferPack.EMPTY);
        locked = false;
        return TransferPack.joulesVoltage(returner.getJoules() / Constants.TRANSFORMER_EFFICIENCY, returner.getVoltage());
    }

    public double getMinimumVoltage() {
        Direction facing = getFacing();
        if (locked) {
            return 0;
        }
        if (output == null) {
            output = new CachedTileOutput(level, worldPosition.relative(facing));
        }
        if (output.getSafe() == null) {
            return -1;
        }
        locked = true;

        BlockEntity outputTile = output.getSafe();

        ICapabilityElectrodynamic electro = level.getCapability(ElectrodynamicsCapabilities.CAPABILITY_ELECTRODYNAMIC_BLOCK, outputTile.getBlockPos(), outputTile.getBlockState(), outputTile, facing.getOpposite());

        double minimumVoltage = -1;

        if (electro != null) {
            minimumVoltage = electro.getMinimumVoltage();
        }

        // double minimumVoltage = ((BlockEntity) output.getSafe()).getCapability(ElectrodynamicsCapabilities.ELECTRODYNAMIC,
        // facing).map(@NotNull ICapabilityElectrodynamic::getMinimumVoltage).orElse(-1.0) / getCoilRatio();
        locked = false;
        return minimumVoltage;
    }

    public double getAmpacity() {
        Direction facing = getFacing();
        if (locked) {
            return 0;
        }
        if (output == null) {
            output = new CachedTileOutput(level, worldPosition.relative(facing));
        }
        if (output.getSafe() == null) {
            return -1;
        }
        locked = true;

        BlockEntity outputTile = output.getSafe();

        ICapabilityElectrodynamic electro = level.getCapability(ElectrodynamicsCapabilities.CAPABILITY_ELECTRODYNAMIC_BLOCK, outputTile.getBlockPos(), outputTile.getBlockState(), outputTile, facing.getOpposite());

        double ampacity = -1;

        if (electro != null) {
            ampacity = electro.getAmpacity();
        }

        // double ampacity = ((BlockEntity) output.getSafe()).getCapability(ElectrodynamicsCapabilities.ELECTRODYNAMIC,
        // facing).map(@NotNull ICapabilityElectrodynamic::getAmpacity).orElse(-1.0) * getCoilRatio();
        locked = false;
        return ampacity;
    }

    @Override
    public void onEntityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (level.isClientSide || lastTransfer.get().getJoules() <= 0 || level.getGameTime() - lastTransferTime.get() > 20L) {
            return;
        }
        ElectricityUtils.electrecuteEntity(entity, lastTransfer.get());
        lastTransfer.set(TransferPack.EMPTY);
        lastTransferTime.set(0L);
    }

    @Override
    public void setNotPlaying() {
        isPlayingSound = false;
    }

    @Override
    public boolean shouldPlaySound() {
        return lastTransfer.get().getVoltage() > 0 && lastTransfer.get().getJoules() > 0;
    }

    // I eliminated world access as that is costly when it doesn't need to be in this case
    public abstract double getCoilRatio();

    public static final class TileDowngradeTransformer extends TileGenericTransformer {

        public TileDowngradeTransformer(BlockPos worldPosition, BlockState blockState) {
            super(ElectrodynamicsTiles.TILE_DOWNGRADETRANSFORMER.get(), worldPosition, blockState);
        }

        @Override
        public double getCoilRatio() {
            return 0.5;
        }

        @Override
        public InteractionResult useWithoutItem(Player player, BlockHitResult hit) {
            return InteractionResult.FAIL;
        }

        @Override
        public ItemInteractionResult useWithItem(ItemStack used, Player player, InteractionHand hand, BlockHitResult hit) {
            return ItemInteractionResult.FAIL;
        }

    }

    public static final class TileUpgradeTransformer extends TileGenericTransformer {

        public TileUpgradeTransformer(BlockPos worldPosition, BlockState blockState) {
            super(ElectrodynamicsTiles.TILE_UPGRADETRANSFORMER.get(), worldPosition, blockState);
        }

        @Override
        public double getCoilRatio() {
            return 2;
        }

        @Override
        public InteractionResult useWithoutItem(Player player, BlockHitResult hit) {
            return InteractionResult.FAIL;
        }

        @Override
        public ItemInteractionResult useWithItem(ItemStack used, Player player, InteractionHand hand, BlockHitResult hit) {
            return ItemInteractionResult.FAIL;
        }

    }

}
