package electrodynamics.client.guidebook;

import electrodynamics.api.References;
import electrodynamics.client.guidebook.chapters.*;
import electrodynamics.client.guidebook.utils.components.Module;
import electrodynamics.client.guidebook.utils.pagedata.graphics.ImageWrapperObject;
import electrodynamics.prefab.utilities.ElectroTextUtils;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

public final class ModuleElectrodynamics extends Module {

	private static final ImageWrapperObject LOGO = new ImageWrapperObject(0, 0, 0, 0, 32, 32, 32, 32, ResourceLocation.fromNamespaceAndPath(References.ID, "textures/screen/guidebook/electrodynamicslogo.png"));

	@Override
	public ImageWrapperObject getLogo() {
		return LOGO;
	}

	@Override
	public MutableComponent getTitle() {
		return ElectroTextUtils.guidebook(References.ID);
	}

	@Override
	public void addChapters() {
		chapters.add(new ChapterGettingStarted(this));
		chapters.add(new ChapterOre(this));
		chapters.add(new ChapterMetricPrefixes(this));
		chapters.add(new ChapterElectricity(this));
		chapters.add(new ChapterFluids(this));
		chapters.add(new ChapterGases(this));
		chapters.add(new ChapterGenerators(this));
		chapters.add(new ChapterMachines(this));
		chapters.add(new ChapterQuarry(this));
		chapters.add(new ChapterElectrolosisChamber(this));
		chapters.add(new ChapterUpgrades(this));
		chapters.add(new ChapterTools(this));
		chapters.add(new ChapterArmor(this));
		chapters.add(new ChapterMisc(this));
		chapters.add(new ChapterTips(this));
	}

}
