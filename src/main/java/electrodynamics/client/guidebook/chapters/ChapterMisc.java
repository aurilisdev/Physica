package electrodynamics.client.guidebook.chapters;

import electrodynamics.Electrodynamics;
import electrodynamics.client.guidebook.utils.components.Chapter;
import electrodynamics.client.guidebook.utils.components.Module;
import electrodynamics.client.guidebook.utils.pagedata.graphics.ImageWrapperObject;
import electrodynamics.client.guidebook.utils.pagedata.text.TextWrapperObject;
import electrodynamics.prefab.utilities.ElectroTextUtils;
import net.minecraft.network.chat.MutableComponent;

public class ChapterMisc extends Chapter {

	private static final ImageWrapperObject LOGO = new ImageWrapperObject(0, 0, 0, 0, 32, 32, 32, 32, Electrodynamics.rl("textures/item/coil.png"));

	public ChapterMisc(Module module) {
		super(module);
	}

	@Override
	public ImageWrapperObject getLogo() {
		return LOGO;
	}

	@Override
	public MutableComponent getTitle() {
		return ElectroTextUtils.guidebook("chapter.misc");
	}

	@Override
	public void addData() {
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.misc.l1")).setIndentions(1).setSeparateStart());
		pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 75, 150, 75, Electrodynamics.rl("textures/screen/guidebook/cablecamo1.png")));
		pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 75, 150, 75, Electrodynamics.rl("textures/screen/guidebook/cablecamo2.png")));
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.misc.l2")).setSeparateStart());
		pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 75, 150, 75, Electrodynamics.rl("textures/screen/guidebook/cablecamo3.png")));
		pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 75, 150, 75, Electrodynamics.rl("textures/screen/guidebook/cablecamo4.png")));
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.misc.l3")).setSeparateStart());
		pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 75, 150, 75, Electrodynamics.rl("textures/screen/guidebook/cablecamo5.png")));
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.misc.l4")).setSeparateStart());
		pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 75, 150, 75, Electrodynamics.rl("textures/screen/guidebook/cablecamo6.png")));
		pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 75, 150, 75, Electrodynamics.rl("textures/screen/guidebook/cablecamo7.png")));
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.misc.l5")).setSeparateStart());
		pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 75, 150, 75, Electrodynamics.rl("textures/screen/guidebook/cablecamo8.png")));
		pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 75, 150, 75, Electrodynamics.rl("textures/screen/guidebook/cablecamo9.png")));
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.misc.l6")).setSeparateStart());
		pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 75, 150, 75, Electrodynamics.rl("textures/screen/guidebook/cablecamo10.png")));
	}

}
