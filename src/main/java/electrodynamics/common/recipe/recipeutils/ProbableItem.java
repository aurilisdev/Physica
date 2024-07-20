package electrodynamics.common.recipe.recipeutils;

import java.util.ArrayList;
import java.util.List;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import electrodynamics.Electrodynamics;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

public class ProbableItem {

    public static final Codec<ProbableItem> CODEC = RecordCodecBuilder.create(instance ->
            //
            instance.group(
                            //
                            ItemStack.CODEC.fieldOf("item").forGetter(instance0 -> instance0.item),
                            //
                            Codec.DOUBLE.fieldOf("chance").forGetter(instance0 -> instance0.chance)

                    )
                    //
                    .apply(instance, ProbableItem::new)

    );

    public static final Codec<List<ProbableItem>> LIST_CODEC = CODEC.listOf();

    public static final StreamCodec<RegistryFriendlyByteBuf, ProbableItem> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public ProbableItem decode(RegistryFriendlyByteBuf buf) {
            return new ProbableItem(ItemStack.STREAM_CODEC.decode(buf), buf.readDouble());
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, ProbableItem item) {
            ItemStack.STREAM_CODEC.encode(buf, item.item);
            buf.writeDouble(item.chance);
        }
    };

    public static final StreamCodec<RegistryFriendlyByteBuf, List<ProbableItem>> LIST_STREAM_CODEC = new StreamCodec<>() {
        @Override
        public List<ProbableItem> decode(RegistryFriendlyByteBuf buf) {
            int count = buf.readInt();
            List<ProbableItem> items = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                items.add(STREAM_CODEC.decode(buf));
            }
            return items;
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, List<ProbableItem> probable) {
            buf.writeInt(probable.size());
            for (ProbableItem item : probable) {
                STREAM_CODEC.encode(buf, item);
            }
        }
    };

    public static final List<ProbableItem> NONE = new ArrayList<>();

    private ItemStack item;
    // 0: 0% chance
    // 1: 100% chance
    private double chance;

    public ProbableItem(ItemStack stack, double chance) {
        item = stack;
        setChance(chance);
    }

    public ItemStack getFullStack() {
        return item;
    }

    private void setChance(double chance) {
        this.chance = chance > 1 ? 1 : chance < 0 ? 0 : chance;
    }

    public double getChance() {
        return chance;
    }

    public ItemStack roll() {
        double random = Electrodynamics.RANDOM.nextDouble();
        if (random > 1 - chance) {
            double amount = chance >= 1 ? item.getCount() : item.getCount() * random;
            int itemCount = (int) Math.ceil(amount);
            return new ItemStack(item.getItem(), itemCount);
        }
        return ItemStack.EMPTY;
    }

}
