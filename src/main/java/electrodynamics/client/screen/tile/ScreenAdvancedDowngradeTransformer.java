package electrodynamics.client.screen.tile;

import electrodynamics.api.screen.ITexture.Textures;
import electrodynamics.common.inventory.container.tile.ContainerAdvancedDowngradeTransformer;
import electrodynamics.common.tile.electricitygrid.transformer.TileAdvancedTransformer.TileAdvancedDowngradeTransformer;
import electrodynamics.prefab.screen.GenericScreen;
import electrodynamics.prefab.screen.component.ScreenComponentGeneric;
import electrodynamics.prefab.screen.component.button.ScreenComponentButton;
import electrodynamics.prefab.screen.component.types.ScreenComponentMultiLabel;
import electrodynamics.prefab.utilities.ElectroTextUtils;
import electrodynamics.prefab.utilities.math.Color;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ScreenAdvancedDowngradeTransformer extends GenericScreen<ContainerAdvancedDowngradeTransformer> {

	public ScreenAdvancedDowngradeTransformer(ContainerAdvancedDowngradeTransformer container, Inventory inv, Component title) {
		super(container, inv, title);
		imageHeight += 30;
		inventoryLabelY += 30;
		addComponent(new ScreenComponentGeneric(Textures.TRANSFORMER_SYMBOL, 20, 43));
		addComponent(new ScreenComponentMultiLabel(0, 0, graphics -> {

			int width = Textures.TRANSFORMER_SYMBOL.textureWidth();
			int xStart = 20;

			Component top = ElectroTextUtils.gui("coilratio");

			int offset = (int) ((width - font.width(top)) / 2.0F);

			graphics.drawString(font, top, xStart + offset, 28, Color.TEXT_GRAY.color(), false);

			TileAdvancedDowngradeTransformer xfmr = menu.getSafeHost();
			if (xfmr == null) {
				return;
			}
			double coilRatio = xfmr.coilRatio.get();
			if (coilRatio <= 0) {
				coilRatio = xfmr.defaultCoilRatio;
			}
			int wholeRatio;
			if (coilRatio < 1) {
				wholeRatio = (int) (1.0 / coilRatio);
			} else {
				wholeRatio = (int) coilRatio;
			}

			Component bottom = Component.literal(wholeRatio + " : 1").withStyle(ChatFormatting.BOLD);

			offset = (int) ((width - font.width(bottom)) / 2.0F);

			graphics.drawString(font, bottom, xStart + offset, 81, Color.TEXT_GRAY.color(), false);

		}));
		addComponent(new ScreenComponentButton<>(75, 20, 40, 20).setLabel(Component.literal("2 : 1")).setOnPress(button -> {
			TileAdvancedDowngradeTransformer xfmr = menu.getSafeHost();
			if (xfmr == null) {
				return;
			}
			xfmr.coilRatio.set(1.0 / 2.0);
		}));
		addComponent(new ScreenComponentButton<>(75, 40, 40, 20).setLabel(Component.literal("4 : 1")).setOnPress(button -> {
			TileAdvancedDowngradeTransformer xfmr = menu.getSafeHost();
			if (xfmr == null) {
				return;
			}
			xfmr.coilRatio.set(1.0 / 4.0);
		}));
		addComponent(new ScreenComponentButton<>(75, 60, 40, 20).setLabel(Component.literal("8 : 1")).setOnPress(button -> {
			TileAdvancedDowngradeTransformer xfmr = menu.getSafeHost();
			if (xfmr == null) {
				return;
			}
			xfmr.coilRatio.set(1.0 / 8.0);
		}));
		addComponent(new ScreenComponentButton<>(75, 80, 40, 20).setLabel(Component.literal("16 : 1")).setOnPress(button -> {
			TileAdvancedDowngradeTransformer xfmr = menu.getSafeHost();
			if (xfmr == null) {
				return;
			}
			xfmr.coilRatio.set(1.0 / 16.0);
		}));
		addComponent(new ScreenComponentButton<>(120, 20, 40, 20).setLabel(Component.literal("32 : 1")).setOnPress(button -> {
			TileAdvancedDowngradeTransformer xfmr = menu.getSafeHost();
			if (xfmr == null) {
				return;
			}
			xfmr.coilRatio.set(1.0 / 32.0);
		}));
		addComponent(new ScreenComponentButton<>(120, 40, 40, 20).setLabel(Component.literal("64 : 1")).setOnPress(button -> {
			TileAdvancedDowngradeTransformer xfmr = menu.getSafeHost();
			if (xfmr == null) {
				return;
			}
			xfmr.coilRatio.set(1.0 / 64.0);
		}));
		addComponent(new ScreenComponentButton<>(120, 60, 40, 20).setLabel(Component.literal("128 : 1")).setOnPress(button -> {
			TileAdvancedDowngradeTransformer xfmr = menu.getSafeHost();
			if (xfmr == null) {
				return;
			}
			xfmr.coilRatio.set(1.0 / 128.0);
		}));
		addComponent(new ScreenComponentButton<>(120, 80, 40, 20).setLabel(Component.literal("256 : 1")).setOnPress(button -> {
			TileAdvancedDowngradeTransformer xfmr = menu.getSafeHost();
			if (xfmr == null) {
				return;
			}
			xfmr.coilRatio.set(1.0 / 256.0);
		}));
	}

}
