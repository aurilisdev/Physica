package electrodynamics.client.guidebook.chapters;

import electrodynamics.Electrodynamics;
import electrodynamics.client.guidebook.utils.components.Chapter;
import electrodynamics.client.guidebook.utils.components.Module;
import electrodynamics.client.guidebook.utils.pagedata.graphics.ImageWrapperObject;
import electrodynamics.client.guidebook.utils.pagedata.graphics.ItemWrapperObject;
import electrodynamics.client.guidebook.utils.pagedata.text.TextWrapperObject;
import electrodynamics.common.block.subtype.SubtypeMachine;
import electrodynamics.prefab.utilities.ElectroTextUtils;
import electrodynamics.registers.ElectrodynamicsItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;

public class ChapterMachines extends Chapter {

	private static final ItemWrapperObject LOGO = new ItemWrapperObject(7, 10, 32, 32, 32, 2.0F, ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.electricfurnace));

	public ChapterMachines(Module module) {
		super(module);
	}

	@Override
	public ItemWrapperObject getLogo() {
		return LOGO;
	}

	@Override
	public MutableComponent getTitle() {
		return ElectroTextUtils.guidebook("chapter.machines");
	}

	@Override
	public void addData() {

		/* Inventory IO Viewer */

		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.machines.inventoryio.header").withStyle(ChatFormatting.UNDERLINE, ChatFormatting.BOLD)).setSeparateStart());
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.machines.inventoryio.1")).setIndentions(1).setSeparateStart());
		pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 75, 150, 75, Electrodynamics.rl("textures/screen/guidebook/inventoryio1.png")));
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.machines.inventoryio.2")).setSeparateStart());
		pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 75, 150, 75, Electrodynamics.rl("textures/screen/guidebook/inventoryio2.png")));
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.machines.inventoryio.3")).setSeparateStart());
		pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 150, 150, 150, Electrodynamics.rl("textures/screen/guidebook/inventoryio3.png")));
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.machines.inventoryio.4")).setSeparateStart());
		pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 75, 150, 75, Electrodynamics.rl("textures/screen/guidebook/inventoryio4.png")));
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.machines.inventoryio.5")).setSeparateStart());

		/* Charger Tutorial */

		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.machines.charger.header").withStyle(ChatFormatting.UNDERLINE, ChatFormatting.BOLD)).setNewPage());
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.machines.charger.1")).setIndentions(1).setSeparateStart());
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.machines.chargeformula")).setIndentions(1).setSeparateStart());
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.machines.charger.2")).setSeparateStart());
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.machines.charger.3")).setIndentions(1).setSeparateStart());
		pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 70, 150, 70, Electrodynamics.rl("textures/screen/guidebook/chargerbatteryslots.png")));
		pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.machines.charger.4")).setSeparateStart());

	}

}
