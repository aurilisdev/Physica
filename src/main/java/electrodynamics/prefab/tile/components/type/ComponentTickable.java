package electrodynamics.prefab.tile.components.type;

import java.util.function.Consumer;

import javax.annotation.Nonnull;

import electrodynamics.prefab.tile.GenericTile;
import electrodynamics.prefab.tile.components.IComponent;
import electrodynamics.prefab.tile.components.IComponentType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class ComponentTickable implements IComponent {

	private GenericTile holder;

	protected Consumer<ComponentTickable> tickCommon;
	protected Consumer<ComponentTickable> tickClient;
	protected Consumer<ComponentTickable> tickServer;

	private long ticks = 0;

	public ComponentTickable(GenericTile holder) {
		this.holder = holder;
	}

	@Override
	public void holder(GenericTile holder) {
		this.holder = holder;
	}

	@Override
	public GenericTile getHolder() {
		return holder;
	}

	public ComponentTickable tickCommon(@Nonnull Consumer<ComponentTickable> consumer) {
		Consumer<ComponentTickable> safe = consumer;
		if (tickCommon != null) {
			safe = safe.andThen(tickCommon);
		}
		tickCommon = safe;
		return this;
	}

	public ComponentTickable tickClient(@Nonnull Consumer<ComponentTickable> consumer) {
		Consumer<ComponentTickable> safe = consumer;
		if (tickClient != null) {
			safe = safe.andThen(tickClient);
		}
		tickClient = safe;
		return this;
	}

	public ComponentTickable tickServer(@Nonnull Consumer<ComponentTickable> consumer) {
		Consumer<ComponentTickable> safe = consumer;
		if (tickServer != null) {
			safe = safe.andThen(tickServer);
		}
		tickServer = safe;
		return this;
	}

	public void tickCommon() {
		ticks++;
		if (tickCommon != null) {
			tickCommon.accept(this);
		}
	}

	public void tickServer() {
		if (tickServer != null) {
			tickServer.accept(this);
		}

	}

	public void tickClient() {
		if (tickClient != null) {
			tickClient.accept(this);
		}
	}

	public long getTicks() {
		return ticks;
	}

	public void performTick(Level level) {

		if (level == null) {
			return;
		}

		tickCommon();

		if (level.isClientSide) {

			tickClient();

		} else {

			tickServer();

			if (holder != null && (holder.getPropertyManager().isDirty() || holder.isChanged)) {

				holder.setChanged();

				holder.getLevel().sendBlockUpdated(holder.getBlockPos(), holder.getBlockState(), holder.getBlockState(), Block.UPDATE_CLIENTS);

				holder.isChanged = false;
			}
		}

	}

	@Override
	public IComponentType getType() {
		return IComponentType.Tickable;
	}
}
