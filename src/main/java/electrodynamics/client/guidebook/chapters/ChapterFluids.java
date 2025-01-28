package electrodynamics.client.guidebook.chapters;

import java.util.ArrayList;
import java.util.List;

import electrodynamics.Electrodynamics;
import electrodynamics.client.guidebook.ScreenGuidebook;
import electrodynamics.client.guidebook.utils.components.Chapter;
import electrodynamics.client.guidebook.utils.components.Module;
import electrodynamics.client.guidebook.utils.pagedata.OnKeyPress;
import electrodynamics.client.guidebook.utils.pagedata.OnTooltip;
import electrodynamics.client.guidebook.utils.pagedata.graphics.AbstractGraphicWrapper.GraphicTextDescriptor;
import electrodynamics.client.guidebook.utils.pagedata.graphics.FluidWrapperObject;
import electrodynamics.client.guidebook.utils.pagedata.graphics.ImageWrapperObject;
import electrodynamics.client.guidebook.utils.pagedata.graphics.ItemWrapperObject;
import electrodynamics.client.guidebook.utils.pagedata.text.TextWrapperObject;
import electrodynamics.common.block.subtype.SubtypeFluidPipe;
import electrodynamics.common.block.subtype.SubtypeMachine;
import electrodynamics.compatibility.jei.JeiBuffer;
import electrodynamics.prefab.utilities.ElectroTextUtils;
import electrodynamics.registers.ElectrodynamicsFluids;
import electrodynamics.registers.ElectrodynamicsItems;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ChapterFluids extends Chapter {

    private static final ImageWrapperObject LOGO = new ImageWrapperObject(0, 0, 0, 0, 32, 32, 32, 32, Electrodynamics.rl("textures/item/pipe/pipesteel.png"));

    public ChapterFluids(Module module) {
        super(module);
    }

    @Override
    public ImageWrapperObject getLogo() {
        return LOGO;
    }

    @Override
    public MutableComponent getTitle() {
        return ElectroTextUtils.guidebook("chapter.fluids");
    }

    @Override
    public void addData() {
        pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.fluids.l1")).setIndentions(1).setSeparateStart());

        blankLine();
        pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.fluids.topic.header", 1, ElectroTextUtils.guidebook("chapter.fluids.topic.fluidlist"))).setSeparateStart());
        pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.fluids.topic.header", 2, ElectroTextUtils.guidebook("chapter.fluids.topic.fluidio"))).setSeparateStart());
        pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.fluids.topic.header", 3, ElectroTextUtils.guidebook("chapter.fluids.topic.fluidpipes"))).setSeparateStart());
        pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.fluids.topic.header", 4, ElectroTextUtils.guidebook("chapter.fluids.topic.fluidtools"))).setSeparateStart());
        pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.fluids.topic.header", 5, ElectroTextUtils.guidebook("chapter.fluids.topic.fluidgui"))).setSeparateStart());

        /* Fluid List */

        pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.fluids.topic.fluidlist").withStyle(ChatFormatting.BOLD, ChatFormatting.UNDERLINE)).setNewPage());

        pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.fluids.l2.1")).setIndentions(1).setSeparateStart());

        for (DeferredHolder<Fluid, ? extends Fluid> fluid : ElectrodynamicsFluids.FLUIDS.getEntries()) {
            pageData.add(new FluidWrapperObject(0, 0, 32, 32, 36, fluid.get(), new GraphicTextDescriptor(36, 11, fluid.get().getFluidType().getDescription()))

                    .onTooltip(new OnTooltip() {

                        @Override
                        public void onTooltip(GuiGraphics graphics, int xAxis, int yAxis, ScreenGuidebook screen) {
                            if (JeiBuffer.isJeiInstalled()) {
                                List<FormattedCharSequence> tooltips = new ArrayList<>();
                                tooltips.add(ElectroTextUtils.tooltip("guidebookjeirecipe").withStyle(ChatFormatting.GRAY).getVisualOrderText());
                                tooltips.add(ElectroTextUtils.tooltip("guidebookjeiuse").withStyle(ChatFormatting.GRAY).getVisualOrderText());
                                graphics.renderTooltip(screen.getFontRenderer(), tooltips, xAxis, yAxis);
                            }

                        }
                    }).onKeyPress(new OnKeyPress() {

                        @Override
                        public void onKeyPress(int keyCode, int scanCode, int modifiers, int x, int y, int xAxis, int yAxis, ScreenGuidebook screen) {

                        }

                        @Override
                        public Object getJeiLookup() {
                            return new FluidStack(fluid.get(), 1);
                        }

                    }));
        }
        pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.fluids.l2.2")).setSeparateStart());

        /* Fluid IO */

        pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.fluids.topic.fluidio").withStyle(ChatFormatting.BOLD, ChatFormatting.UNDERLINE)).setNewPage());

        pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.fluids.l3.1")).setIndentions(1).setSeparateStart());
        blankLine();
        pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.fluids.fluidinput")).setIndentions(1).setSeparateStart());
        pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.fluids.fluidoutput")).setIndentions(1).setSeparateStart());
        blankLine();
        pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.fluids.l3.2")).setSeparateStart());
        pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 79, 150, 79, 81, Electrodynamics.rl("textures/screen/guidebook/fluidio.png")).onTooltip(new OnTooltip() {

            @Override
            public void onTooltip(GuiGraphics graphics, int xAxis, int yAxis, ScreenGuidebook screen) {
                List<FormattedCharSequence> tooltips = new ArrayList<>();
                tooltips.add(ElectroTextUtils.guidebook("chapter.electricity.left", ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.mineralwasher).getDescription().copy().withStyle(ChatFormatting.DARK_GRAY)).withStyle(ChatFormatting.GRAY).getVisualOrderText());
                tooltips.add(ElectroTextUtils.guidebook("chapter.electricity.middle", ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.chemicalcrystallizer).getDescription().copy().withStyle(ChatFormatting.DARK_GRAY)).withStyle(ChatFormatting.GRAY).getVisualOrderText());
                tooltips.add(ElectroTextUtils.guidebook("chapter.electricity.right", ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.electricpump).getDescription().copy().withStyle(ChatFormatting.DARK_GRAY)).withStyle(ChatFormatting.GRAY).getVisualOrderText());

                graphics.renderTooltip(screen.getFontRenderer(), tooltips, xAxis, yAxis);
            }

        }));

        /* Fluid Pipes */

        pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.fluids.topic.fluidpipes").withStyle(ChatFormatting.BOLD, ChatFormatting.UNDERLINE)).setNewPage());

        pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.fluids.l4", ElectroTextUtils.guidebook("chapter.fluids.pipes").withStyle(ChatFormatting.BOLD))).setSeparateStart().setIndentions(1));
        blankLine();
        pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.fluids.pipecapacity", ElectroTextUtils.guidebook("chapter.fluids.pipecopper"), SubtypeFluidPipe.copper.getMaxTransfer())).setSeparateStart().setIndentions(1));
        pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.fluids.pipecapacity", ElectroTextUtils.guidebook("chapter.fluids.pipesteel"), SubtypeFluidPipe.steel.getMaxTransfer())).setSeparateStart().setIndentions(1));

        /* Fluid Tools */

        pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.fluids.topic.fluidtools").withStyle(ChatFormatting.BOLD, ChatFormatting.UNDERLINE)).setNewPage());

        pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.fluids.l5.1")).setSeparateStart().setIndentions(1));

        // Reinforced Canister

        pageData.add(new TextWrapperObject(ElectrodynamicsItems.ITEM_CANISTERREINFORCED.get().getDescription().copy().withStyle(ChatFormatting.BOLD)).setCentered().setNewPage());
        pageData.add(new ItemWrapperObject(7 + ScreenGuidebook.TEXT_WIDTH / 2 - 16, 5, 32, 30, 30, 2.0F, ElectrodynamicsItems.ITEM_CANISTERREINFORCED.get()).onTooltip(new OnTooltip() {

            @Override
            public void onTooltip(GuiGraphics graphics, int xAxis, int yAxis, ScreenGuidebook screen) {
                if (JeiBuffer.isJeiInstalled()) {
                    List<FormattedCharSequence> tooltips = new ArrayList<>();
                    tooltips.add(ElectroTextUtils.tooltip("guidebookjeirecipe").withStyle(ChatFormatting.GRAY).getVisualOrderText());
                    graphics.renderTooltip(screen.getFontRenderer(), tooltips, xAxis, yAxis);
                }

            }
        }).onKeyPress(new OnKeyPress() {

            @Override
            public void onKeyPress(int keyCode, int scanCode, int modifiers, int x, int y, int xAxis, int yAxis, ScreenGuidebook screen) {

            }

            @Override
            public Object getJeiLookup() {
                return new ItemStack(ElectrodynamicsItems.ITEM_CANISTERREINFORCED.get());
            }

        }));

        pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.fluids.l5.reinforcedcanister.1")).setSeparateStart().setIndentions(1));

        // Fluid Valve

        pageData.add(new TextWrapperObject(ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.fluidvalve).getDescription().copy().withStyle(ChatFormatting.BOLD)).setCentered().setNewPage());
        pageData.add(new ItemWrapperObject(7 + ScreenGuidebook.TEXT_WIDTH / 2 - 16, 5, 32, 30, 30, 2.0F, ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.fluidvalve)).onTooltip(new OnTooltip() {

            @Override
            public void onTooltip(GuiGraphics graphics, int xAxis, int yAxis, ScreenGuidebook screen) {
                if (JeiBuffer.isJeiInstalled()) {
                    List<FormattedCharSequence> tooltips = new ArrayList<>();
                    tooltips.add(ElectroTextUtils.tooltip("guidebookjeirecipe").withStyle(ChatFormatting.GRAY).getVisualOrderText());
                    graphics.renderTooltip(screen.getFontRenderer(), tooltips, xAxis, yAxis);
                }

            }
        }).onKeyPress(new OnKeyPress() {

            @Override
            public void onKeyPress(int keyCode, int scanCode, int modifiers, int x, int y, int xAxis, int yAxis, ScreenGuidebook screen) {

            }

            @Override
            public Object getJeiLookup() {
                return new ItemStack(ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.fluidvalve));
            }

        }));

        pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.fluids.l5.fluidvalve.1")).setSeparateStart().setIndentions(1));
        pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 75, 150, 75, Electrodynamics.rl("textures/screen/guidebook/fluidvalveoff.png")));
        pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 75, 150, 75, Electrodynamics.rl("textures/screen/guidebook/fluidvalveon.png")));
        pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.fluids.l5.fluidvalve.2")).setSeparateStart());

        // Fluid Pipe Pump

        pageData.add(new TextWrapperObject(ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.fluidpipepump).getDescription().copy().withStyle(ChatFormatting.BOLD)).setCentered().setNewPage());
        pageData.add(new ItemWrapperObject(7 + ScreenGuidebook.TEXT_WIDTH / 2 - 16, 5, 32, 30, 30, 2.0F, ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.fluidpipepump)).onTooltip(new OnTooltip() {

            @Override
            public void onTooltip(GuiGraphics graphics, int xAxis, int yAxis, ScreenGuidebook screen) {
                if (JeiBuffer.isJeiInstalled()) {
                    List<FormattedCharSequence> tooltips = new ArrayList<>();
                    tooltips.add(ElectroTextUtils.tooltip("guidebookjeirecipe").withStyle(ChatFormatting.GRAY).getVisualOrderText());
                    graphics.renderTooltip(screen.getFontRenderer(), tooltips, xAxis, yAxis);
                }

            }
        }).onKeyPress(new OnKeyPress() {

            @Override
            public void onKeyPress(int keyCode, int scanCode, int modifiers, int x, int y, int xAxis, int yAxis, ScreenGuidebook screen) {

            }

            @Override
            public Object getJeiLookup() {
                return new ItemStack(ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.fluidpipepump));
            }

        }));

        pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.fluids.l5.fluidpipepump.1")).setSeparateStart().setIndentions(1));
        pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 79, 150, 79, Electrodynamics.rl("textures/screen/guidebook/fluidpipepump.png")));
        pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.fluids.l5.fluidpipepump.2")).setSeparateStart());
        pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 79, 150, 79, Electrodynamics.rl("textures/screen/guidebook/fluidpipepumpgui.png")));
        pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.fluids.l5.fluidpipepump.3")).setSeparateStart());

        // Fluid Pipe Filter

        pageData.add(new TextWrapperObject(ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.fluidpipefilter).getDescription().copy().withStyle(ChatFormatting.BOLD)).setCentered().setNewPage());
        pageData.add(new ItemWrapperObject(7 + ScreenGuidebook.TEXT_WIDTH / 2 - 16, 5, 32, 30, 30, 2.0F, ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.fluidpipefilter)).onTooltip(new OnTooltip() {

            @Override
            public void onTooltip(GuiGraphics graphics, int xAxis, int yAxis, ScreenGuidebook screen) {
                if (JeiBuffer.isJeiInstalled()) {
                    List<FormattedCharSequence> tooltips = new ArrayList<>();
                    tooltips.add(ElectroTextUtils.tooltip("guidebookjeirecipe").withStyle(ChatFormatting.GRAY).getVisualOrderText());
                    graphics.renderTooltip(screen.getFontRenderer(), tooltips, xAxis, yAxis);
                }

            }
        }).onKeyPress(new OnKeyPress() {

            @Override
            public void onKeyPress(int keyCode, int scanCode, int modifiers, int x, int y, int xAxis, int yAxis, ScreenGuidebook screen) {

            }

            @Override
            public Object getJeiLookup() {
                return new ItemStack(ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.fluidpipefilter));
            }

        }));

        pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.fluids.l5.fluidfilterpipe.1")).setSeparateStart().setIndentions(1));
        pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 79, 150, 79, Electrodynamics.rl("textures/screen/guidebook/fluidfilterpipe.png")));
        pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.fluids.l5.fluidfilterpipe.2")).setSeparateStart());
        pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 79, 150, 79, Electrodynamics.rl("textures/screen/guidebook/fluidfilterpipegui1.png")));
        pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.fluids.l5.fluidfilterpipe.3", ElectroTextUtils.guidebook("chapter.fluids.blacklist").withStyle(ChatFormatting.BOLD), ElectroTextUtils.guidebook("chapter.fluids.whitelist").withStyle(ChatFormatting.BOLD))).setSeparateStart());
        pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.fluids.l5.fluidfilterpipe.4")).setSeparateStart().setIndentions(1));
        pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 75, 150, 75, Electrodynamics.rl("textures/screen/guidebook/fluidfilterpipegui2.png")));
        pageData.add(new ImageWrapperObject(0, 0, 0, 0, 150, 75, 150, 75, Electrodynamics.rl("textures/screen/guidebook/fluidfilterpipegui3.png")));
        pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.fluids.l5.fluidfilterpipe.5")).setSeparateStart());

        // Fluid Void

        pageData.add(new TextWrapperObject(ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.fluidvoid).getDescription().copy().withStyle(ChatFormatting.BOLD)).setCentered().setNewPage());
        pageData.add(new ItemWrapperObject(7 + ScreenGuidebook.TEXT_WIDTH / 2 - 16, 5, 32, 30, 30, 2.0F, ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.fluidvoid)).onTooltip(new OnTooltip() {

            @Override
            public void onTooltip(GuiGraphics graphics, int xAxis, int yAxis, ScreenGuidebook screen) {
                if (JeiBuffer.isJeiInstalled()) {
                    List<FormattedCharSequence> tooltips = new ArrayList<>();
                    tooltips.add(ElectroTextUtils.tooltip("guidebookjeirecipe").withStyle(ChatFormatting.GRAY).getVisualOrderText());
                    graphics.renderTooltip(screen.getFontRenderer(), tooltips, xAxis, yAxis);
                }

            }
        }).onKeyPress(new OnKeyPress() {

            @Override
            public void onKeyPress(int keyCode, int scanCode, int modifiers, int x, int y, int xAxis, int yAxis, ScreenGuidebook screen) {

            }

            @Override
            public Object getJeiLookup() {
                return new ItemStack(ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.fluidvoid));
            }

        }));

        pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.fluids.l5.fluidvoid.1")).setSeparateStart().setIndentions(1));

        // Fluid Tanks

        pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.fluids.fluidtanks").withStyle(ChatFormatting.BOLD)).setCentered().setNewPage());
        pageData.add(new ItemWrapperObject(7 + ScreenGuidebook.TEXT_WIDTH / 2 - 16, 5, 32, 30, 30, 2.0F, ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.tankhsla)).onTooltip(new OnTooltip() {

            @Override
            public void onTooltip(GuiGraphics graphics, int xAxis, int yAxis, ScreenGuidebook screen) {
                if (JeiBuffer.isJeiInstalled()) {
                    List<FormattedCharSequence> tooltips = new ArrayList<>();
                    tooltips.add(ElectroTextUtils.tooltip("guidebookjeirecipe").withStyle(ChatFormatting.GRAY).getVisualOrderText());
                    graphics.renderTooltip(screen.getFontRenderer(), tooltips, xAxis, yAxis);
                }

            }
        }).onKeyPress(new OnKeyPress() {

            @Override
            public void onKeyPress(int keyCode, int scanCode, int modifiers, int x, int y, int xAxis, int yAxis, ScreenGuidebook screen) {

            }

            @Override
            public Object getJeiLookup() {
                return new ItemStack(ElectrodynamicsItems.ITEMS_MACHINE.getValue(SubtypeMachine.tankhsla));
            }

        }));

        pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.fluids.l5.fluidtank.1")).setSeparateStart().setIndentions(1));

        /* Fluid GUIs */

        pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.fluids.topic.fluidgui").withStyle(ChatFormatting.BOLD, ChatFormatting.UNDERLINE)).setNewPage());

        pageData.add(new TextWrapperObject(ElectroTextUtils.guidebook("chapter.fluids.l6")).setSeparateStart().setIndentions(1));

    }

}
