package electrodynamics.common.block.subtype;

import electrodynamics.api.ISubtype;
import electrodynamics.api.network.cable.type.IFluidPipe;

public enum SubtypeFluidPipe implements ISubtype, IFluidPipe {
    copper(5000),
    steel(10000);

    private final long maxTransfer;

    SubtypeFluidPipe(long maxTransfer) {
        this.maxTransfer = maxTransfer;

    }

    @Override
    public String tag() {
        return "pipe" + name();
    }

    @Override
    public String forgeTag() {
        return tag();
    }

    @Override
    public boolean isItem() {
        return false;
    }

    @Override
    public long getMaxTransfer() {
        return maxTransfer;
    }
}
