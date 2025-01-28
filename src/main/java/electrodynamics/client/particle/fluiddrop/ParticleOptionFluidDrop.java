package electrodynamics.client.particle.fluiddrop;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import electrodynamics.registers.ElectrodynamicsParticles;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public class ParticleOptionFluidDrop extends ParticleType<ParticleOptionFluidDrop> implements ParticleOptions {

    public static final MapCodec<ParticleOptionFluidDrop> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.FLOAT.fieldOf("r").forGetter(instance0 -> instance0.r),
                    Codec.FLOAT.fieldOf("g").forGetter(instance0 -> instance0.g),
                    Codec.FLOAT.fieldOf("b").forGetter(instance0 -> instance0.b),
                    Codec.FLOAT.fieldOf("scale").forGetter(instance0 -> instance0.scale)
            ).apply(instance, (r, g, b, scale) -> new ParticleOptionFluidDrop().setParameters(r, g, b, scale)));

    public static final StreamCodec<RegistryFriendlyByteBuf, ParticleOptionFluidDrop> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT, instance0 -> instance0.r,
            ByteBufCodecs.FLOAT, instance0 -> instance0.g,
            ByteBufCodecs.FLOAT, instance0 -> instance0.b,
            ByteBufCodecs.FLOAT, instance0 -> instance0.scale,
            (r, g, b, scale) -> new ParticleOptionFluidDrop().setParameters(r, g, b, scale)
    );

    public float r;
    public float g;
    public float b;

    public float scale;

    public ParticleOptionFluidDrop() {
        super(false);
    }

    public ParticleOptionFluidDrop setParameters(float r, float g, float b, float scale) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.scale = scale;
        return this;
    }


    @Override
    public ParticleType<?> getType() {
        return ElectrodynamicsParticles.PARTICLE_FLUIDDROP.get();
    }

    @Override
    public MapCodec<ParticleOptionFluidDrop> codec() {
        return CODEC;
    }

    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, ParticleOptionFluidDrop> streamCodec() {
        return STREAM_CODEC;
    }
}
