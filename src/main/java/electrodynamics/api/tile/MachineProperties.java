package electrodynamics.api.tile;

import electrodynamics.api.multiblock.subnodebased.Subnode;
import electrodynamics.common.block.voxelshapes.VoxelShapeProvider;
import net.minecraft.world.level.block.RenderShape;

public class MachineProperties {

    public boolean isMultiblock = false;
    public int litBrightness = 0;
    public RenderShape renderShape = RenderShape.MODEL;
    public boolean propegatesLightDown = false;
    public boolean isPlayerStorable = false;
    public Subnode[] subnodes = new Subnode[0];
    public VoxelShapeProvider provider = VoxelShapeProvider.DEFAULT;

    public static final MachineProperties DEFAULT = new MachineProperties();

    private MachineProperties() {

    }

    public MachineProperties setLitBrightness(int brightness) {
        this.litBrightness = brightness;
        return this;
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

    public MachineProperties setSubnodes(Subnode[] subnodes) {
        isMultiblock = true;
        this.subnodes = subnodes;
        return this;
    }

    public MachineProperties setShapeProvider(VoxelShapeProvider provider) {
        this.provider = provider;
        return this;
    }

    public static MachineProperties builder() {
        return new MachineProperties();
    }

}
