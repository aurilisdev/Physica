package electrodynamics.client.guidebook.chapters;

import electrodynamics.Electrodynamics;
import electrodynamics.client.guidebook.utils.components.Chapter;
import electrodynamics.client.guidebook.utils.components.Module;
import electrodynamics.client.guidebook.utils.pagedata.graphics.ImageWrapperObject;
import electrodynamics.client.guidebook.utils.pagedata.graphics.ItemWrapperObject;
import electrodynamics.client.guidebook.utils.pagedata.text.TextWrapperObject;
import electrodynamics.common.block.subtype.SubtypeMachine;
import electrodynamics.common.item.subtype.SubtypeDrillHead;
import electrodynamics.common.item.subtype.SubtypeItemUpgrade;
import electrodynamics.prefab.utilities.ElectroTextUtils;
import electrodynamics.registers.ElectrodynamicsItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;

public class ChapterQuarry extends Chapter {

	private static final ItemWrapperObject LOGO = new ItemWrapperObject(7, 10, 32, 32, 32, 2.0F, ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.quarry));

	public ChapterQuarry(Module module) {
		super(module);
	}

	@Override
	public ItemWrapperObject getLogo() {
		return LOGO;
	}

	@Override
	public MutableComponent getTitle() {
		return ElectroTextUtils.guidebook("chapter.quarry");
	}

	@Override
	public void addData() {

		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.quarry.l1")).setIndentions(1).setSeparateStart());

		// Step 1
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.quarry.step", 1).withStyle(ChatFormatting.BOLD)).setSeparateStart().setNewPage());
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.quarry.step1l1")).setIndentions(1).setSeparateStart());
		pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 79, 150, 79, Electrodynamics.rl("textures/screen/guidebook/markerring.png")));

		// Step 2
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.quarry.step", 2).withStyle(ChatFormatting.BOLD)).setSeparateStart().setNewPage());
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.quarry.step2l1")).setIndentions(1).setSeparateStart());
		pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 75, 150, 79, Electrodynamics.rl("textures/screen/guidebook/seismicrelay1.png")));
		pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 75, 150, 79, Electrodynamics.rl("textures/screen/guidebook/seismicrelay2.png")));
		pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 75, 150, 79, Electrodynamics.rl("textures/screen/guidebook/seismicrelay3.png")));

		// Step 3
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.quarry.step", 3).withStyle(ChatFormatting.BOLD)).setSeparateStart().setNewPage());
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.quarry.step3l1")).setIndentions(1).setSeparateStart());
		pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 79, 150, 79, Electrodynamics.rl("textures/screen/guidebook/quarryplacement.png")));

		// Step 4
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.quarry.step", 4).withStyle(ChatFormatting.BOLD)).setSeparateStart().setNewPage());
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.quarry.step4l1")).setIndentions(1).setSeparateStart());
		pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 79, 150, 79, Electrodynamics.rl("textures/screen/guidebook/coolantresplacement.png")));

		// Step 5
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.quarry.step", 5).withStyle(ChatFormatting.BOLD)).setSeparateStart().setNewPage());
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.quarry.step5l1")).setIndentions(1).setSeparateStart());
		pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 75, 150, 79, Electrodynamics.rl("textures/screen/guidebook/motorcomplex1.png")));
		pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 75, 150, 79, Electrodynamics.rl("textures/screen/guidebook/motorcomplex2.png")));

		// Step 6
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.quarry.step", 6).withStyle(ChatFormatting.BOLD)).setSeparateStart().setNewPage());
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.quarry.step6l1")).setIndentions(1).setSeparateStart());
		pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 75, 150, 79, Electrodynamics.rl("textures/screen/guidebook/quarrypower1.png")));
		pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 75, 150, 79, Electrodynamics.rl("textures/screen/guidebook/quarrypower2.png")));
		pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 75, 150, 79, Electrodynamics.rl("textures/screen/guidebook/quarrypower3.png")));

		// Step 7
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.quarry.step", 7).withStyle(ChatFormatting.BOLD)).setSeparateStart().setNewPage());
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.quarry.step7l1")).setIndentions(1).setSeparateStart());
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.quarry.drillhead", ElectroTextUtils.guidebook("chapter.quarry.steelhead"), SubtypeDrillHead.steel.durability)).setIndentions(1).setSeparateStart());
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.quarry.drillhead", ElectroTextUtils.guidebook("chapter.quarry.stainlesshead"), SubtypeDrillHead.stainlesssteel.durability)).setIndentions(1).setSeparateStart());
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.quarry.drillhead", ElectroTextUtils.guidebook("chapter.quarry.hslahead"), SubtypeDrillHead.hslasteel.durability)).setIndentions(1).setSeparateStart());
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.quarry.drillhead", ElectroTextUtils.guidebook("chapter.quarry.titaniumhead"), SubtypeDrillHead.titanium.durability)).setIndentions(1).setSeparateStart());
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.quarry.drillhead", ElectroTextUtils.guidebook("chapter.quarry.carbidehead"), ElectroTextUtils.guidebook("chapter.quarry.infinitedurability"))).setIndentions(1).setSeparateStart());
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.quarry.step7l2")).setSeparateStart());
		pageData.add(new TextWrapperObject(ElectrodynamicsItems.ITEMS_UPGRADE.getValue(SubtypeItemUpgrade.unbreaking).getDescription()).setIndentions(1).setSeparateStart());
		pageData.add(new TextWrapperObject(ElectrodynamicsItems.ITEMS_UPGRADE.getValue(SubtypeItemUpgrade.fortune).getDescription()).setIndentions(1).setSeparateStart());
		pageData.add(new TextWrapperObject(ElectrodynamicsItems.ITEMS_UPGRADE.getValue(SubtypeItemUpgrade.silktouch).getDescription()).setIndentions(1).setSeparateStart());
		pageData.add(new TextWrapperObject(ElectrodynamicsItems.ITEMS_UPGRADE.getValue(SubtypeItemUpgrade.itemvoid).getDescription()).setIndentions(1).setSeparateStart());
		blankLine();
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.quarry.step7unbreaking", ElectrodynamicsItems.ITEMS_UPGRADE.getValue(SubtypeItemUpgrade.unbreaking).getDescription().copy().withStyle(ChatFormatting.BOLD))).setSeparateStart().setIndentions(1));
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.quarry.step7fortune", ElectrodynamicsItems.ITEMS_UPGRADE.getValue(SubtypeItemUpgrade.fortune).getDescription().copy().withStyle(ChatFormatting.BOLD))).setIndentions(1).setSeparateStart());
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.quarry.step7silktouch", ElectrodynamicsItems.ITEMS_UPGRADE.getValue(SubtypeItemUpgrade.silktouch).getDescription().copy().withStyle(ChatFormatting.BOLD))).setIndentions(1).setSeparateStart());
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.quarry.step7void", ElectrodynamicsItems.ITEMS_UPGRADE.getValue(SubtypeItemUpgrade.itemvoid).getDescription().copy().withStyle(ChatFormatting.BOLD))).setIndentions(1).setSeparateStart());
		pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 79, 150, 79, Electrodynamics.rl("textures/screen/guidebook/quarrygui1.png")));

		// Step 8
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.quarry.step", 8).withStyle(ChatFormatting.BOLD)).setSeparateStart().setNewPage());
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.quarry.step8l1")).setIndentions(1).setSeparateStart());
		pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 75, 150, 75, Electrodynamics.rl("textures/screen/guidebook/logisticalmanager1.png")));
		pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 75, 150, 75, Electrodynamics.rl("textures/screen/guidebook/logisticalmanager2.png")));

	}

}
