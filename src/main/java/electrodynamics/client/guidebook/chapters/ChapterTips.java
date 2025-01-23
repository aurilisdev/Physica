package electrodynamics.client.guidebook.chapters;

import electrodynamics.Electrodynamics;
import electrodynamics.client.guidebook.utils.components.Chapter;
import electrodynamics.client.guidebook.utils.components.Module;
import electrodynamics.client.guidebook.utils.pagedata.graphics.ImageWrapperObject;
import electrodynamics.client.guidebook.utils.pagedata.text.TextWrapperObject;
import electrodynamics.prefab.utilities.ElectroTextUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;

public class ChapterTips extends Chapter {

	private static final ImageWrapperObject LOGO = new ImageWrapperObject(0, 0, 0, 0, 32, 32, 32, 32, Electrodynamics.rl("textures/item/nugget/nuggetsilver.png"));

	public ChapterTips(Module module) {
		super(module);
	}

	@Override
	public ImageWrapperObject getLogo() {
		return LOGO;
	}

	@Override
	public MutableComponent getTitle() {
		return ElectroTextUtils.guidebook("chapter.tips");
	}

	@Override
	public void addData() {

		// Energy storage tip
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.tips.tip", 1).withStyle(ChatFormatting.UNDERLINE)).setSeparateStart());
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.tips.tip1")).setIndentions(1).setSeparateStart());

		// Transformer Tip
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.tips.tip", 2).withStyle(ChatFormatting.UNDERLINE)).setNewPage());
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.tips.tip2")).setIndentions(1).setSeparateStart());

		// Tool Battery Replacement Tip
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.tips.tip", 3).withStyle(ChatFormatting.UNDERLINE)).setNewPage());
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.tips.tip3")).setIndentions(1).setSeparateStart());

		// Ctrl hover over upgrade slots in GUI
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.tips.tip", 4).withStyle(ChatFormatting.UNDERLINE)).setNewPage());
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.tips.tip4")).setIndentions(1).setSeparateStart());

	}

}
