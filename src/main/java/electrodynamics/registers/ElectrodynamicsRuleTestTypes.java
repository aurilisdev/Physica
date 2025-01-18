package electrodynamics.registers;

import electrodynamics.api.References;
import electrodynamics.common.world.ruletests.RuleTestOre;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTestType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ElectrodynamicsRuleTestTypes {

	public static final DeferredRegister<RuleTestType<?>> RULE_TEST_TYPES = DeferredRegister.create(Registries.RULE_TEST, References.ID);

	public static final DeferredHolder<RuleTestType<?>, RuleTestType<?>> TEST_CONFIG_ORESPAWN = RULE_TEST_TYPES.register("configorespawn", () -> (RuleTestType<RuleTestOre>) () -> RuleTestOre.CODEC);

}
