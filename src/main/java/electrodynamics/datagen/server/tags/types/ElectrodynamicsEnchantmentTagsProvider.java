package electrodynamics.datagen.server.tags.types;

import java.util.concurrent.CompletableFuture;

import org.jetbrains.annotations.Nullable;

import electrodynamics.api.References;
import electrodynamics.common.tags.ElectrodynamicsTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EnchantmentTagsProvider;
import net.minecraft.world.item.enchantment.Enchantments;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ElectrodynamicsEnchantmentTagsProvider extends EnchantmentTagsProvider {
    public ElectrodynamicsEnchantmentTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, registries, References.ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(ElectrodynamicsTags.Enchantments.EFFICIENCY).add(Enchantments.EFFICIENCY);
        tag(ElectrodynamicsTags.Enchantments.UNBREAKING).add(Enchantments.UNBREAKING);
        tag(ElectrodynamicsTags.Enchantments.SILK_TOUCH).add(Enchantments.SILK_TOUCH);
    }
}
