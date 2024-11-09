package electrodynamics.common.block.subtype;

import java.util.Locale;

import electrodynamics.api.ISubtype;
import electrodynamics.api.gas.Gas;
import electrodynamics.prefab.utilities.ElectroTextUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public enum SubtypeGasPipe implements ISubtype {

    /*
     * UNINSULATED
     */
    UNINSULATEDCOPPER(PipeMaterial.COPPER, InsulationMaterial.NONE, 10000, 2.5, Blocks.IRON_BLOCK.properties(), SoundType.METAL), //
    UNINSULATEDSTEEL(PipeMaterial.STEEL, InsulationMaterial.NONE, 30000, 2.5, Blocks.IRON_BLOCK.properties(), SoundType.METAL), //
    UNINSULATEDPLASTIC(PipeMaterial.HDPE, InsulationMaterial.NONE, 1000, 2.5, Blocks.IRON_BLOCK.properties(), SoundType.STONE);//

    /*
     * CERAMIC INSULATED
     */
    // CERAMICINSULATEDCOPPER(PipeMaterial.COPPER, InsulationMaterial.CERAMIC, 10000, 3, Material.METAL, SoundType.TUFF),//
    // CERAMICINSULATEDSTEEL(PipeMaterial.STEEL, InsulationMaterial.CERAMIC, 30000, 3, Material.METAL, SoundType.TUFF),//
    // CERAMICINSULATEDPLASTIC(PipeMaterial.HDPE, InsulationMaterial.NONE, 1000, 3, Material.STONE, SoundType.TUFF),//

    /*
     * WOOL INSULATED
     */
    // WOOLINSULATEDCOPPER(PipeMaterial.COPPER, InsulationMaterial.WOOL, 10000, 3, Material.METAL, SoundType.WOOL),
    // WOOLINSULATEDSTEEL(PipeMaterial.STEEL, InsulationMaterial.WOOL, 30000, 3, Material.METAL, SoundType.WOOL),
    // WOOLINSULATEDPLASTIC(PipeMaterial.HDPE, InsulationMaterial.WOOL, 1000, 3, Material.STONE, SoundType.WOOL);

    public final PipeMaterial pipeMaterial;
    public final InsulationMaterial insulationMaterial;
    public final double maxTransfer;
    public final double effectivePipeHeatLoss;

    public final double radius;
    public final Properties material;
    public final SoundType soundType;

    private SubtypeGasPipe(PipeMaterial pipeMaterial, InsulationMaterial insulationMaterial, double maxTransfer, double radius, Properties material, SoundType soundType) {
        this.pipeMaterial = pipeMaterial;
        this.insulationMaterial = insulationMaterial;
        this.maxTransfer = maxTransfer;
        effectivePipeHeatLoss = pipeMaterial.heatLoss * insulationMaterial.heatLoss;

        this.radius = radius;
        this.material = material;
        this.soundType = soundType;
    }

    @Override
    public String tag() {
        return "gaspipe" + name().toLowerCase(Locale.ROOT);
    }

    @Override
    public String forgeTag() {
        return tag();
    }

    @Override
    public boolean isItem() {
        return false;
    }

    public static SubtypeGasPipe getPipeForType(PipeMaterial material, InsulationMaterial insulation) {
        for (SubtypeGasPipe pipe : SubtypeGasPipe.values()) {
            if (pipe.pipeMaterial == material && pipe.insulationMaterial == insulation) {
                return pipe;
            }
        }
        return SubtypeGasPipe.UNINSULATEDCOPPER;
    }

    /**
     * Gets the effective heat loss through a pipe taking into account the temperature of the material with relation to room
     * temperature. As the gas temperature approaches room temperature, the heat loss approaches zero
     * 
     * @param pipeHeatLoss
     * @param gasTemperature
     * @return A positive value if the material temperature is greater than room temperature i.e. it cools or a negative
     *         value if the gas temperature is less than room temperature i.e. it warms
     */
    public static double getEffectiveHeatLoss(SubtypeGasPipe pipe, double gasTemperature) {

        double tempDifference = 1;// Math.abs(gasTemperature - Gas.ROOM_TEMPERATURE) / ((gasTemperature + Gas.ROOM_TEMPERATURE) / 2.0);

        double aboveOrBelow = gasTemperature > Gas.ROOM_TEMPERATURE ? 1 : -1;

        return pipe.effectivePipeHeatLoss * tempDifference * aboveOrBelow;
    }

    /**
     * 
     * Heat loss in material is dependent on many real-time factors, and involves performing a logarithm, which is an
     * expensive calculation when you consider it will have to be performed hundreds of times per tick. As a result, it is
     * not feasible performance-wise to accurately determine what the true heat loss in a material will be as it flows
     * through a pipe. As a result, these values are linear coefficients based upon the Thermal Conductivity of metals. In
     * other words, you expect a material with a higher conductivity to lose more heat.
     * 
     * Thermal Conductivity sources:
     * 
     * https://www.engineeringtoolbox.com/thermal-conductivity-metals-d_858.html
     * https://www.substech.com/dokuwiki/doku.php?id=thermoplastic_high_density_polyethylene_hdpe
     * 
     * 
     * The tensile strength of a material is not dependent on temperature. However, temperature affects the density of a
     * metal, reducing the effective tensile strength. This again would be dependent on real-time factors and thus would be
     * too costly to calculate accurately. As a result, the max pressure is based loosely off of the max yield strength of
     * the material.
     * 
     * Tensile Strength sources:
     * 
     * https://www.engineeringtoolbox.com/young-modulus-d_417.html
     * https://www.substech.com/dokuwiki/doku.php?id=thermoplastic_high_density_polyethylene_hdpe
     * 
     * @author skip999
     *
     */
    public static enum PipeMaterial {

        COPPER(10, 16, true, "pipematerialcopper"), // Them Con: 383, Ten Str: 70MPa (690 ATM)
        STEEL(1, 128, true, "pipematerialsteel"), // Them Con: 54, Ten Str: 250MPa (2467 ATM)
        HDPE(0.05, 16, false, "pipematerialplastic"); // Them Con: 0.48, Ten Str: 31MPa (305 ATM)

        public final double heatLoss;// degree Kelvin per Pipe
        public final int maxPressure;// atm

        public final boolean corrodedByAcid;

        private final String tooltipName;

        private PipeMaterial(double heatLoss, int maxPressure, boolean corrodedByAcid, String tooltipName) {

            this.heatLoss = heatLoss;
            this.maxPressure = maxPressure;
            this.corrodedByAcid = corrodedByAcid;
            this.tooltipName = tooltipName;

        }

        public Component getTranslatedName() {
            return ElectroTextUtils.tooltip(tooltipName);
        }

    }

    /**
     * 
     * The same heat loss issues crop up here as with the pipe materials. Again the heat loss is generalized to a constant
     * multiplier
     * 
     * @author skip999
     *
     */

    public static enum InsulationMaterial {

        NONE(1, false, "pipeinsulationnone"), // i.e. air
        WOOL(0.005, true, "pipeinsulationwool"), // Them Con: ~0.040
        CERAMIC(0.05, false, "pipeinsulationceramic"); // Them Con: ~0.2

        public final double heatLoss;
        public final boolean canCombust;
        private final String tooltipName;

        private InsulationMaterial(double heatLoss, boolean canCombust, String tooltipName) {
            this.heatLoss = heatLoss;
            this.canCombust = canCombust;
            this.tooltipName = tooltipName;
        }

        public Component getTranslatedName() {
            return ElectroTextUtils.tooltip(tooltipName);
        }

    }

}
