package electrodynamics.common.tile.pipelines.gas.gastransformer;

public interface IMultiblockGasTransformer {

    void updateAddonTanks(int count, boolean isLeft);

    boolean hasBeenDestroyed();

}
