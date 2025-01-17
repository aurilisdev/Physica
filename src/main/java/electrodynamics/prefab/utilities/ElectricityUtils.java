package electrodynamics.prefab.utilities;

import electrodynamics.api.capability.types.electrodynamic.ICapabilityElectrodynamic;
import electrodynamics.common.tags.ElectrodynamicsTags;
import electrodynamics.common.tile.electricitygrid.GenericTileWire;
import electrodynamics.prefab.utilities.object.TransferPack;
import electrodynamics.registers.ElectrodynamicsCapabilities;
import electrodynamics.registers.ElectrodynamicsDamageTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.Level.ExplosionInteraction;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;

public class ElectricityUtils {

    public static void electrecuteEntity(Entity entity, TransferPack transfer) {
        if (transfer.getVoltage() <= 960.0 && entity instanceof LivingEntity living) {
            Ingredient insulatingItems = Ingredient.of(ElectrodynamicsTags.Items.INSULATES_PLAYER_FEET);
            for (ItemStack armor : living.getArmorSlots()) {
                if (ItemUtils.isIngredientMember(insulatingItems, armor.getItem())) {
                    float damage = (float) transfer.getAmps() / 10.0f;
                    if (Math.random() < damage) {
                        int integerDamage = (int) Math.max(1, damage);
                        if (armor.getDamageValue() > armor.getMaxDamage()) {
                            armor.setCount(0);
                        }
                    }
                    return;
                }
            }
        }
        entity.hurt(entity.damageSources().source(ElectrodynamicsDamageTypes.ELECTRICITY, entity), (float) Math.min(9999, Math.max(0, transfer.getAmps())));
    }

    public static boolean isElectricReceiver(BlockEntity tile, Direction dir) {
        return tile != null && (tile.getLevel().getCapability(ElectrodynamicsCapabilities.CAPABILITY_ELECTRODYNAMIC_BLOCK, tile.getBlockPos(), tile.getBlockState(), tile, dir) != null || tile.getLevel().getCapability(Capabilities.EnergyStorage.BLOCK, tile.getBlockPos(), tile.getBlockState(), tile, dir) != null);
    }

    public static boolean isConductor(BlockEntity acceptor, GenericTileWire requesterWire) {
        if (acceptor instanceof GenericTileWire conductor) {
            return conductor.getCableType().isDefaultColor() || requesterWire.getCableType().isDefaultColor() || conductor.getWireColor() == requesterWire.getWireColor();
        }
        return false;
    }

    public static TransferPack receivePower(BlockEntity tile, Direction direction, TransferPack transfer, boolean debug) {

        if (tile == null) {
            return TransferPack.EMPTY;
        }

        ICapabilityElectrodynamic electro = tile.getLevel().getCapability(ElectrodynamicsCapabilities.CAPABILITY_ELECTRODYNAMIC_BLOCK, tile.getBlockPos(), tile.getBlockState(), tile, direction);

        if (electro != null) {

            return electro.receivePower(transfer, debug);

        }

        IEnergyStorage fe = tile.getLevel().getCapability(Capabilities.EnergyStorage.BLOCK, tile.getBlockPos(), tile.getBlockState(), tile, direction);

        if (fe != null) {
            TransferPack returner = TransferPack.joulesVoltage(fe.receiveEnergy((int) Math.min(Integer.MAX_VALUE, transfer.getJoules()), debug), transfer.getVoltage());
            if (transfer.getVoltage() > ElectrodynamicsCapabilities.DEFAULT_VOLTAGE) {
                Level world = tile.getLevel();
                BlockPos pos = tile.getBlockPos();
                world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
                world.explode(null, pos.getX(), pos.getY(), pos.getZ(), (float) Math.log10(10 + transfer.getVoltage() / ElectrodynamicsCapabilities.DEFAULT_VOLTAGE), ExplosionInteraction.BLOCK);
            }
            return returner;
        }

        return TransferPack.EMPTY;

    }

}
