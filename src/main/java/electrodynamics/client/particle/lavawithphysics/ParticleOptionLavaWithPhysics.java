package electrodynamics.client.particle.lavawithphysics;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import electrodynamics.prefab.utilities.CodecUtils;
import electrodynamics.registers.ElectrodynamicsParticles;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public class ParticleOptionLavaWithPhysics extends ParticleType<ParticleOptionLavaWithPhysics> implements ParticleOptions {

    public float scale;
    public double bounceFactor;

    public static final MapCodec<ParticleOptionLavaWithPhysics> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.FLOAT.fieldOf("scale").forGetter(instance0 -> instance0.scale),
                    Codec.DOUBLE.fieldOf("bouncefactor").forGetter(instance0 -> instance0.bounceFactor)
            ).apply(instance, (scale, bounceFactor) -> new ParticleOptionLavaWithPhysics().setParameters(scale, bounceFactor)));

    public static final StreamCodec<RegistryFriendlyByteBuf, ParticleOptionLavaWithPhysics> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT, instance0 -> instance0.scale,
            ByteBufCodecs.DOUBLE, instance0 -> instance0.bounceFactor,
            (scale, bounceFactor) -> new ParticleOptionLavaWithPhysics().setParameters(scale, bounceFactor)
    );

    public ParticleOptionLavaWithPhysics() {
        super(false);
    }

    public ParticleOptionLavaWithPhysics setParameters(float scale, double bounceFactor) {
        this.scale = scale;
        return this;
    }

    @Override
    public ParticleType<?> getType() {
        return ElectrodynamicsParticles.PARTICLE_LAVAWITHPHYSICS.get();
    }

    @Override
    public MapCodec<ParticleOptionLavaWithPhysics> codec() {
        return CODEC;
    }

    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, ParticleOptionLavaWithPhysics> streamCodec() {
        return STREAM_CODEC;
    }
}
