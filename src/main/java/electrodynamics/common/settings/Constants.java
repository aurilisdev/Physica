package electrodynamics.common.settings;

import electrodynamics.api.configuration.BooleanValue;
import electrodynamics.api.configuration.Configuration;
import electrodynamics.api.configuration.DoubleValue;
import electrodynamics.api.configuration.FloatValue;
import electrodynamics.api.configuration.IntValue;
import electrodynamics.prefab.utilities.object.TransferPack;

@Configuration(name = "Electrodynamics")
public class Constants {

	@FloatValue(def = 0.9925f)
	public static float TRANSFORMER_EFFICIENCY = 0.9925f;
	@FloatValue(def = 0.995f)
	public static float CIRCUITBREAKER_EFFICIENCY = 0.995f;
	@FloatValue(def = 1.0F)
	public static float RELAY_EFFICIENCY = 1.0F; // the relay is a dumb switch; no need to penalize its use
	@FloatValue(def = 0.995f)
	public static float CURRENTREGULATOR_EFFICIENCY = 0.995f;
	@DoubleValue(def = 34)
	public static double COALGENERATOR_AMPERAGE = 34.0;
	@DoubleValue(def = 4.5)
	public static double THERMOELECTRICGENERATOR_AMPERAGE = 4.5;
	@DoubleValue(def = 6)
	public static double HYDROELECTRICGENERATOR_AMPERAGE = 6;
	@DoubleValue(def = 10)
	public static double WINDMILL_MAX_AMPERAGE = 10;
	@DoubleValue(def = 7)
	public static double SOLARPANEL_AMPERAGE = 7;
	@DoubleValue(def = 50.0)
	public static double ELECTRICPUMP_USAGE_PER_TICK = 50.0;
	@DoubleValue(def = 25.0)
	public static double ADVANCEDSOLARPANEL_AMPERAGE = 25.0;
	@DoubleValue(def = 175)
	public static double ELECTRICFURNACE_USAGE_PER_TICK = 175.0;
	@IntValue(def = 100)
	public static int ELECTRICFURNACE_REQUIRED_TICKS = 100;
	@DoubleValue(def = 175)
	public static double ELECTRICARCFURNACE_USAGE_PER_TICK = 175.0;
	@IntValue(def = 50)
	public static int ELECTRICARCFURNACE_REQUIRED_TICKS = 50;
	@DoubleValue(def = 125.0)
	public static double WIREMILL_USAGE_PER_TICK = 125.0;
	@IntValue(def = 200)
	public static int WIREMILL_REQUIRED_TICKS = 200;
	@DoubleValue(def = 350.0)
	public static double COMBUSTIONCHAMBER_JOULES_PER_TICK = 350.0;
	@IntValue(def = 10)
	public static int CHARGER_REQUIRED_TICKS = 100;
	@DoubleValue(def = 1000.0)
	public static double CHARGER_USAGER_PER_TICK = 1000.0;
	@DoubleValue(def = 100)
	public static double GAS_COLLECTOR_USAGE_PER_TICK = 100;
	@DoubleValue(def = 100)
	public static double MOTORCOMPLEX_USAGE_PER_TICK = 100.0;
	@DoubleValue(def = 10)
	public static double PIPE_PUMP_USAGE_PER_TICK = 10.0;
	@DoubleValue(def = 100)
	public static double QUARRY_USAGE_PER_TICK = 100.0;
	@IntValue(def = 10)
	public static int QUARRY_WATERUSAGE_PER_BLOCK = 10;
	@IntValue(def = 64, comment = "max radius = 128, min radius = 2")
	public static int MARKER_RADIUS = 64;
	@IntValue(def = 1, comment = "max possible is 1 tick / block")
	public static int MAX_QUARRYBLOCKS_PER_TICK = 1;
	@IntValue(def = 100, comment = "min possible speed is 100 ticks / block")
	public static int MIN_QUARRYBLOCKS_PER_TICK = 100;
	@IntValue(def = 64, comment = "how many air blocks the quarry can skip over in one clearing tick; max is 128, min is zero")
	public static int CLEARING_AIR_SKIP = 64;
	@BooleanValue(def = true, comment = "controls whether or not the quarry will mine blocks that have been placed into the mining area")
	public static boolean MAINTAIN_MINING_AREA = true;
	@BooleanValue(def = false, comment = "Controls whether the quarry can bypass claims or not")
	public static boolean BYPASS_CLAIMS = false;
	@BooleanValue(def = true)
	public static boolean DISPENSE_GUIDEBOOK = true;
	@BooleanValue(def = true, comment = "When set to true, this will make transformers tick, but give them the ability to hum as they do in real life. If you need to gain performance, you can disable this to stop transformers from ticking and thus producing sound.")
	public static boolean SHOULD_TRANSFORMER_HUM = true;
	@DoubleValue(def = 5000, comment = "The Watts a transformer needs to see to be considered under \"full load\" and thus hum as loud as it can. Set to 0 to have it hum under any load greater than 0 Watts")
	public static double TRANSFORMER_SOUND_LOAD_TARGET = 5000;
	@BooleanValue(def = true, comment = "Whether or not wires should set things on fire around them if their voltage exceeds their insulation value.")
	public static boolean CONDUCTORS_BURN_SURROUNDINGS = true;
	@FloatValue(def = 6.0F, comment = "The hardness value that a block must have to not be instantly vaporized by a wire over 30,720V, which is the maximum voltage achievable by default electrodynamics. 6 is the explosion resistance of an Iron Block")
	public static float BLOCK_VAPORIZATION_HARDNESS = 6.0F;

	@BooleanValue(def = true, comment = "Set to false to disable the HUD rendering for combat armor.")
	public static boolean RENDER_COMBAT_ARMOR_STATUS = true;

	public static TransferPack COALGENERATOR_MAX_OUTPUT = TransferPack.ampsVoltage(COALGENERATOR_AMPERAGE, 120);
}
