package electrodynamics.api.network.cable.type;

import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

public interface IGasPipe {

    long getMaxTransfer();

    double getRadius();

    BlockBehaviour.Properties getProperties();

    SoundType getSoundType();

    IPipeMaterial getPipeMaterial();

    public static interface IPipeMaterial {

        /**
         * units of ATM
         * @return
         */
        int getMaxPressuire();


        /**
         * returns whether this pipe material can be destroyed by corrosive gasses.
         * @return
         */
        boolean canBeCorroded();

        Component getName();

    }

}
