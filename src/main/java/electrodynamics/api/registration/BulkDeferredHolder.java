package electrodynamics.api.registration;

import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

/**
 * A wrapper class intended to house much of the functionality of the Subtype HashMaps
 * @param <T> The base class of the registered object i.e. Fluid, Item, etc.
 * @param <A> The class of the registered object; must extend the base class
 * @param <SUBTYPE> The class of the value being iterated over
 *
 * @Author skip999
 */
public class BulkDeferredHolder<T, A extends T, SUBTYPE> {

    private final HashMap<SUBTYPE, DeferredHolder<T, A>> subtypeMap = new HashMap<>();

    public BulkDeferredHolder(SUBTYPE[] values, Function<SUBTYPE, DeferredHolder<T, A>> factory) {
        subtypeMap.clear();
        for(SUBTYPE subtype : values){
            subtypeMap.put(subtype, factory.apply(subtype));
        }
    }

    public DeferredHolder<T, A> getValue(SUBTYPE key){
        return subtypeMap.get(key);
    }

    public List<DeferredHolder<T, A>> getAllValues() {
        return subtypeMap.values().stream().toList();
    }

    public boolean containsValue(SUBTYPE key){
        return subtypeMap.containsValue(key);
    }


}
