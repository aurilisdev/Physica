package electrodynamics.datagen.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import javax.annotation.Nullable;

import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;

import electrodynamics.Electrodynamics;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.extensions.IAdvancementBuilderExtension;

public class AdvancementBuilder implements IAdvancementBuilderExtension {

    public final ResourceLocation id;

    @Nullable
    private ResourceLocation parentId;
    @Nullable
    private AdvancementHolder parent;
    @Nullable
    private DisplayInfo display;
    private AdvancementRewards rewards = AdvancementRewards.EMPTY;
    private Map<String, Criterion<?>> criteria = Maps.newLinkedHashMap();
    @Nullable
    private AdvancementRequirements requirements;
    private AdvancementRequirements.Strategy requirementsStrategy = AdvancementRequirements.Strategy.AND;
    @Nullable
    private String comment;
    @Nullable
    private String author;

    @Nullable
    private AdvancementHolder holder;

    @Nullable
    private List<ICondition> conditions;

    private AdvancementBuilder(ResourceLocation id) {
        this.id = id;
    }

    public static AdvancementBuilder create(ResourceLocation id) {
        return new AdvancementBuilder(id);
    }

    public AdvancementBuilder parent(AdvancementHolder parent) {
        this.parent = parent;
        return this;
    }

    public AdvancementBuilder parent(ResourceLocation parentId) {
        this.parentId = parentId;
        return this;
    }

    public AdvancementBuilder display(Item item, Component title, Component description, AdvancementBackgrounds background, AdvancementType frame, boolean showToast, boolean announceToChat, boolean hidden) {
        return this.display(new DisplayInfo(new ItemStack(item), title, description, Optional.ofNullable(background.loc), frame, showToast, announceToChat, hidden));
    }

    public AdvancementBuilder display(ItemStack stack, Component title, Component description, AdvancementBackgrounds background, AdvancementType frame, boolean showToast, boolean announceToChat, boolean hidden) {
        return this.display(new DisplayInfo(stack, title, description, Optional.of(background.loc), frame, showToast, announceToChat, hidden));
    }

    public AdvancementBuilder display(ItemStack stack, Component title, Component description, @Nullable ResourceLocation background, AdvancementType frame, boolean showToast, boolean announceToChat, boolean hidden) {
        return this.display(new DisplayInfo(stack, title, description, Optional.of(background), frame, showToast, announceToChat, hidden));
    }

    public AdvancementBuilder display(ItemLike item, Component title, Component description, @Nullable ResourceLocation background, AdvancementType frame, boolean showToast, boolean announceToChat, boolean hidden) {
        return this.display(new DisplayInfo(new ItemStack(item.asItem()), title, description, Optional.of(background), frame, showToast, announceToChat, hidden));
    }

    public AdvancementBuilder display(DisplayInfo display) {
        this.display = display;
        return this;
    }

    public AdvancementBuilder rewards(AdvancementRewards.Builder rewardsBuilder) {
        return this.rewards(rewardsBuilder.build());
    }

    public AdvancementBuilder rewards(AdvancementRewards rewards) {
        this.rewards = rewards;
        return this;
    }

    public AdvancementBuilder addCriterion(String key, Criterion<?> criterion) {
        if (this.criteria.containsKey(key)) {
            throw new IllegalArgumentException("Duplicate criterion " + key);
        }
        this.criteria.put(key, criterion);
        return this;
    }

    public AdvancementBuilder requirements(AdvancementRequirements.Strategy strategy) {
        this.requirementsStrategy = strategy;
        return this;
    }

    public AdvancementBuilder requirements(AdvancementRequirements requirements) {
        this.requirements = requirements;
        return this;
    }

    public AdvancementBuilder condition(ICondition condition) {
        if (conditions == null) {
            conditions = new ArrayList<>();
        }
        conditions.add(condition);
        return this;
    }

    public AdvancementBuilder comment(String comment) {
        this.comment = comment;
        return this;
    }

    public AdvancementBuilder author(String author) {
        this.author = author;
        return this;
    }

    /**
     * Tries to resolve the parent of this advancement, if possible. Returns {@code true} on success.
     */
    public boolean canBuild(Function<ResourceLocation, AdvancementHolder> parentLookup) {
        if (this.parentId == null) {
            return true;
        }
        if (this.parent == null) {
            this.parent = parentLookup.apply(this.parentId);
        }

        return this.parent != null;
    }

    public AdvancementHolder build() {
        if (!this.canBuild(resourceLocation -> null)) {
            throw new IllegalStateException("Tried to build incomplete advancement!");
        }
        if (this.requirements == null) {
            this.requirements = this.requirementsStrategy.create(this.criteria.keySet());
        }

        return holder = new AdvancementHolder(id, new Advancement(Optional.ofNullable(parent == null ? parentId : this.parent.id()), Optional.ofNullable(this.display), this.rewards, this.criteria, this.requirements, false));
    }

    public JsonObject serializeToJson(HolderLookup.Provider registries) {
        if (holder == null) {
            build();
        }

        RegistryOps<JsonElement> registryops = registries.createSerializationContext(JsonOps.INSTANCE);

        JsonElement jsonElement = Advancement.CODEC.encodeStart(registryops, holder.value()).getOrThrow();

        if (!jsonElement.isJsonObject()) {
            throw new UnsupportedOperationException("Advancement " + holder.id().toString() + " is not a Json Object!");
        }

        JsonObject jsonObject = jsonElement.getAsJsonObject();

        if (author != null) {
            jsonObject.addProperty("__author", author);
        }

        if (comment != null) {
            jsonObject.addProperty("__comment", comment);
        }

        return jsonObject;
    }

    public static enum AdvancementBackgrounds {

        NONE(null),
        // Vanilla
        ADVENTURE(Electrodynamics.vanillarl("textures/gui/advancements/backgrounds/adventure.png")), //
        END(Electrodynamics.vanillarl("textures/gui/advancements/backgrounds/end.png")), //
        HUSBANDRY(Electrodynamics.vanillarl("textures/gui/advancements/backgrounds/husbandry.png")), //
        NETHER(Electrodynamics.vanillarl("textures/gui/advancements/backgrounds/nether.png")), //
        STONE(Electrodynamics.vanillarl("textures/gui/advancements/backgrounds/stone.png")); //

        public final ResourceLocation loc;

        private AdvancementBackgrounds(ResourceLocation loc) {
            this.loc = loc;
        }

    }

}
