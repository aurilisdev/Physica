package electrodynamics.client.guidebook.chapters;

import java.util.ArrayList;
import java.util.List;

import electrodynamics.Electrodynamics;
import electrodynamics.api.electricity.formatting.ChatFormatter;
import electrodynamics.api.electricity.formatting.DisplayUnit;
import electrodynamics.client.guidebook.ScreenGuidebook;
import electrodynamics.client.guidebook.utils.components.Chapter;
import electrodynamics.client.guidebook.utils.components.Module;
import electrodynamics.client.guidebook.utils.pagedata.OnTooltip;
import electrodynamics.client.guidebook.utils.pagedata.graphics.AbstractGraphicWrapper;
import electrodynamics.client.guidebook.utils.pagedata.graphics.ImageWrapperObject;
import electrodynamics.client.guidebook.utils.pagedata.graphics.ItemWrapperObject;
import electrodynamics.client.guidebook.utils.pagedata.text.TextWrapperObject;
import electrodynamics.common.block.subtype.SubtypeGlass;
import electrodynamics.common.block.subtype.SubtypeMachine;
import electrodynamics.common.settings.Constants;
import electrodynamics.prefab.utilities.ElectroTextUtils;
import electrodynamics.registers.ElectrodynamicsBlocks;
import electrodynamics.registers.ElectrodynamicsItems;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.level.block.Blocks;

public class ChapterElectrolosisChamber extends Chapter {

    private static final ItemWrapperObject LOGO = new ItemWrapperObject(7, 10, 32, 32, 32, 2.0F, ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.electrolosischamber));

    public ChapterElectrolosisChamber(Module module) {
        super(module);
    }

    @Override
    public AbstractGraphicWrapper<?> getLogo() {
        return LOGO;
    }

    @Override
    public MutableComponent getTitle() {
        return ElectroTextUtils.guidebook("chapter.electrolosischamber");
    }

    @Override
    public void addData() {
        pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electrolosischamber.l1", ElectroTextUtils.guidebook("chapter.electrolosischamber.multiply").withStyle(ChatFormatting.BOLD), ChatFormatter.getChatDisplayShort(Constants.ELECTROLOSIS_CHAMBER_TARGET_JOULES * 20, DisplayUnit.WATT).withStyle(ChatFormatting.BOLD))).setIndentions(1).setSeparateStart());
        pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electrolosischamber.l2", ElectroTextUtils.guidebook("chapter.electrolosischamber.impurefluid").withStyle(ChatFormatting.BOLD))).setIndentions(1).setSeparateStart());
        pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electrolosischamber.l3",
                ElectroTextUtils.guidebook("chapter.electrolosischamber.count", Component.literal("49").withStyle(ChatFormatting.BOLD), ElectrodynamicsBlocks.BLOCK_STEELSCAFFOLDING.get().getName()),
                ElectroTextUtils.guidebook("chapter.electrolosischamber.count", Component.literal("17").withStyle(ChatFormatting.BOLD), ElectrodynamicsBlocks.BLOCKS_CUSTOMGLASS.getValue(SubtypeGlass.aluminum).getName()),
                ElectroTextUtils.guidebook("chapter.electrolosischamber.count", Component.literal("8").withStyle(ChatFormatting.BOLD), ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.tanksteel).getName()),
                ElectroTextUtils.guidebook("chapter.electrolosischamber.count", Component.literal("3").withStyle(ChatFormatting.BOLD), Blocks.GOLD_BLOCK.getName()),
                ElectroTextUtils.guidebook("chapter.electrolosischamber.count", Component.literal("1").withStyle(ChatFormatting.BOLD), ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.electrolyticseparator).getName()),
                ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.electrolosischamber).getName()
        )).setIndentions(1).setSeparateStart());
        pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electrolosischamber.step", 1).withStyle(ChatFormatting.BOLD)).setNewPage());
        pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 79, 150, 79, 81, Electrodynamics.rl("textures/screen/guidebook/electrolosischamber1.png")).onTooltip(new OnTooltip() {

            @Override
            public void onTooltip(GuiGraphics graphics, int xAxis, int yAxis, ScreenGuidebook screen) {

                graphics.renderTooltip(screen.getFontRenderer(), Component.literal("25 ").append(ElectrodynamicsBlocks.BLOCK_STEELSCAFFOLDING.get().getName()), xAxis, yAxis);
            }

        }));
        pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electrolosischamber.step", 2).withStyle(ChatFormatting.BOLD)).setNewPage());
        pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 79, 150, 79, 81, Electrodynamics.rl("textures/screen/guidebook/electrolosischamber2.png")).onTooltip(new OnTooltip() {

            @Override
            public void onTooltip(GuiGraphics graphics, int xAxis, int yAxis, ScreenGuidebook screen) {
                List<FormattedCharSequence> tooltips = new ArrayList<>();
                tooltips.add(Component.literal("4 ").append(ElectrodynamicsBlocks.BLOCK_STEELSCAFFOLDING.get().getName()).getVisualOrderText());
                tooltips.add(Component.literal("8 ").append(ElectrodynamicsBlocks.BLOCKS_CUSTOMGLASS.getValue(SubtypeGlass.aluminum).getName()).getVisualOrderText());

                graphics.renderTooltip(screen.getFontRenderer(), tooltips, xAxis, yAxis);
            }

        }));
        pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electrolosischamber.step", 3).withStyle(ChatFormatting.BOLD)).setNewPage());
        pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 79, 150, 79, 81, Electrodynamics.rl("textures/screen/guidebook/electrolosischamber3.png")).onTooltip(new OnTooltip() {

            @Override
            public void onTooltip(GuiGraphics graphics, int xAxis, int yAxis, ScreenGuidebook screen) {

                graphics.renderTooltip(screen.getFontRenderer(), Component.literal("8 ").append(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.tanksteel).getName()), xAxis, yAxis);
            }

        }));
        pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electrolosischamber.step", 4).withStyle(ChatFormatting.BOLD)).setNewPage());
        pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 79, 150, 79, 81, Electrodynamics.rl("textures/screen/guidebook/electrolosischamber4.png")).onTooltip(new OnTooltip() {

            @Override
            public void onTooltip(GuiGraphics graphics, int xAxis, int yAxis, ScreenGuidebook screen) {
                List<FormattedCharSequence> tooltips = new ArrayList<>();
                tooltips.add(Component.literal("3 ").append(Blocks.GOLD_BLOCK.getName()).getVisualOrderText());
                tooltips.add(Component.literal("1 ").append(ElectrodynamicsBlocks.BLOCKS_MACHINE.getValue(SubtypeMachine.electrolyticseparator).getName()).getVisualOrderText());

                graphics.renderTooltip(screen.getFontRenderer(), tooltips, xAxis, yAxis);
            }

        }));
        pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electrolosischamber.seperatororient").withStyle(ChatFormatting.ITALIC)));
        pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electrolosischamber.step", 5).withStyle(ChatFormatting.BOLD)).setNewPage());
        pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 79, 150, 79, 81, Electrodynamics.rl("textures/screen/guidebook/electrolosischamber5.png")).onTooltip(new OnTooltip() {

            @Override
            public void onTooltip(GuiGraphics graphics, int xAxis, int yAxis, ScreenGuidebook screen) {

                graphics.renderTooltip(screen.getFontRenderer(), Component.literal("16 ").append(ElectrodynamicsBlocks.BLOCK_STEELSCAFFOLDING.get().getName()), xAxis, yAxis);
            }

        }));
        pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electrolosischamber.step", 6).withStyle(ChatFormatting.BOLD)).setNewPage());
        pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 79, 150, 79, 81, Electrodynamics.rl("textures/screen/guidebook/electrolosischamber6.png")).onTooltip(new OnTooltip() {

            @Override
            public void onTooltip(GuiGraphics graphics, int xAxis, int yAxis, ScreenGuidebook screen) {

                graphics.renderTooltip(screen.getFontRenderer(), Component.literal("9 ").append(ElectrodynamicsBlocks.BLOCKS_CUSTOMGLASS.getValue(SubtypeGlass.aluminum).getName()), xAxis, yAxis);
            }

        }));
        pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electrolosischamber.l4")).setSeparateStart());
        pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 75, 150, 75, 75, Electrodynamics.rl("textures/screen/guidebook/electrolosischamber7.png")));
        pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 75, 150, 75, 75, Electrodynamics.rl("textures/screen/guidebook/electrolosischamber8.png")));
        pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electrolosischamber.l5")).setIndentions(1).setSeparateStart());
        pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 79, 150, 79, 81, Electrodynamics.rl("textures/screen/guidebook/electrolosischamber9.png")));
        pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 79, 150, 79, 81, Electrodynamics.rl("textures/screen/guidebook/electrolosischamber10.png")));
        pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electrolosischamber.l6")).setSeparateStart());
        pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electrolosischamber.l7")).setIndentions(1).setSeparateStart());
        pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 79, 150, 79, 81, Electrodynamics.rl("textures/screen/guidebook/electrolosischambergui1.png")));
        pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.electrolosischamber.l8")).setIndentions(1).setSeparateStart());


    }


}
