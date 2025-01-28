package electrodynamics.api.registration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

import net.neoforged.neoforge.registries.DeferredHolder;

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
    private final List<A> extractedValues = new ArrayList<>();
    private A[] extractedValuesArray;

    public BulkDeferredHolder(SUBTYPE[] values, Function<SUBTYPE, DeferredHolder<T, A>> factory) {
        subtypeMap.clear();
        extractedValues.clear();
        for(SUBTYPE subtype : values){
            subtypeMap.put(subtype, factory.apply(subtype));
        }
    }

    public A getValue(SUBTYPE key){
        return subtypeMap.get(key).get();
    }

    public DeferredHolder<T, A> getHolder(SUBTYPE key){
        return subtypeMap.get(key);
    }

    public List<A> getAllValues() {
        if(extractedValues.isEmpty()){
            subtypeMap.values().forEach(register -> extractedValues.add(register.get()));
        }
        return extractedValues;
    }

    public A[] getAllValuesArray(A[] newArray) {
        if(extractedValuesArray == null) {
            extractedValuesArray = getAllValues().toArray(newArray);
        }
        return extractedValuesArray;
    }

    public A[] getSpecificValuesArray(A[] newArray, SUBTYPE... subtypes) {

        List<A> values = getSpecificValues(subtypes);
        return values.toArray(newArray);


    }

    public List<A> getSpecificValues(SUBTYPE... subtypes){
        List<A> values = new ArrayList<>();
        for(SUBTYPE subtype : subtypes){
            values.add(getValue(subtype));
        }
        return values;
    }

    public List<DeferredHolder<T, A>> getAllHolders() {
        return subtypeMap.values().stream().toList();
    }

    public boolean containsValue(SUBTYPE key){
        return subtypeMap.containsValue(key);
    }


}
