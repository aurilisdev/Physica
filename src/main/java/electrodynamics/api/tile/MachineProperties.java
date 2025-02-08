package electrodynamics.api.tile;

import electrodynamics.api.multiblock.subnodebased.parent.IMultiblockParentBlock;
import electrodynamics.common.block.voxelshapes.VoxelShapeProvider;
import net.minecraft.world.level.block.RenderShape;

public class MachineProperties {

    public boolean isMultiblock = false;
    public int litBrightness = 0;
    public boolean usesLit = false;
    public RenderShape renderShape = RenderShape.MODEL;
    public boolean propegatesLightDown = false;
    public boolean isPlayerStorable = false;
    public IMultiblockParentBlock.SubnodeWrapper wrapper = IMultiblockParentBlock.SubnodeWrapper.EMPTY;
    public VoxelShapeProvider provider = VoxelShapeProvider.DEFAULT;

    public static final MachineProperties DEFAULT = new MachineProperties();

    private MachineProperties() {
    }

    public MachineProperties setLitBrightness(int brightness) {
        this.litBrightness = brightness;
        return setUsesLit();
    }

    public MachineProperties setPropegateLightDown() {
        propegatesLightDown = true;
        return this;
    }

    public MachineProperties setRenderShape(RenderShape shape) {
        renderShape = shape;
        return this;
    }

    public MachineProperties setPlayerStorable() {
        isPlayerStorable = true;
        return this;
    }

    public MachineProperties setSubnodes(IMultiblockParentBlock.SubnodeWrapper wrapper) {
        isMultiblock = true;
        this.wrapper = wrapper;
        return this;
    }

    public MachineProperties setShapeProvider(VoxelShapeProvider provider) {
        this.provider = provider;
        return this;
    }

    public MachineProperties setUsesLit() {
        usesLit = true;
        return this;
    }

    public static MachineProperties builder() {
        return new MachineProperties();
    }

}
