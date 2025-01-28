package electrodynamics.api.electricity;

import java.util.HashMap;
import java.util.HashSet;

import electrodynamics.api.network.cable.type.IWire;
import electrodynamics.common.block.connect.BlockWire;
import electrodynamics.common.block.subtype.SubtypeWire;
import net.neoforged.bus.api.Event;
import net.neoforged.fml.event.IModBusEvent;

public class RegisterWiresEvent extends Event implements IModBusEvent {

    private final HashSet<BlockWire> wires = new HashSet<>();

    public void registerWire(BlockWire wire) {
        wires.add(wire);
    }


    public void process() {

        HashMap<IWire.IWireMaterial, HashMap<IWire.IInsulationMaterial, HashMap<IWire.IWireClass, HashMap<IWire.IWireColor, BlockWire>>>> wireToColorMapping = new HashMap<>();

        HashSet<IWire.IWireMaterial> wireMaterials = new HashSet<>();

        HashSet<IWire.IWireColor> wireColors = new HashSet<>();

        for(BlockWire wire : wires) {

            wireMaterials.add(wire.wire.getWireMaterial());

            HashMap<IWire.IInsulationMaterial, HashMap<IWire.IWireClass, HashMap<IWire.IWireColor, BlockWire>>> wTC1 = wireToColorMapping.getOrDefault(wire.wire.getWireMaterial(), new HashMap<>());

            HashMap<IWire.IWireClass, HashMap<IWire.IWireColor, BlockWire>> wTC2 = wTC1.getOrDefault(wire.wire.getInsulation(), new HashMap<>());

            HashMap<IWire.IWireColor, BlockWire> wTC3 = wTC2.getOrDefault(wire.wire.getWireColor(), new HashMap<>());

            wTC3.put(wire.wire.getWireColor(), wire);

            wTC2.put(wire.wire.getWireClass(), wTC3);

            wTC1.put(wire.wire.getInsulation(), wTC2);

            wireToColorMapping.put(wire.wire.getWireMaterial(), wTC1);

            wireColors.add(wire.wire.getWireColor());

        }

        SubtypeWire.WIRE_MATERIALS.clear();
        SubtypeWire.WIRE_MATERIALS.addAll(wireMaterials);

        SubtypeWire.WIRES.clear();
        SubtypeWire.WIRES.putAll(wireToColorMapping);

        SubtypeWire.WireColor.WIRE_COLORS.clear();
        SubtypeWire.WireColor.WIRE_COLORS.addAll(wireColors);

    }


}
