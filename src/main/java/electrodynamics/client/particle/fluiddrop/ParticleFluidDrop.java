package electrodynamics.client.particle.fluiddrop;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.util.Mth;

public class ParticleFluidDrop extends TextureSheetParticle {

    private final SpriteSet sprites;

    public ParticleFluidDrop(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, ParticleOptionFluidDrop options, SpriteSet set) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        sprites = set;
        this.friction = 0.96F;
        this.speedUpWhenYMotionIsBlocked = true;
        this.xd = 0;
        this.yd = ySpeed;
        this.zd = 0;
        //this.quadSize = this.quadSize * 0.75F * options.scale;
        setSpriteFromAge(sprites);
        setColor(options.r, options.g, options.b);
        int i = (int)(8.0 / (this.random.nextDouble() * 0.8 + 0.2));
        this.lifetime = (int)Math.max(i * options.scale, 1.0F);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public float getQuadSize(float scaleFactor) {
        return this.quadSize * Mth.clamp((this.age + scaleFactor) / this.lifetime * 32.0F, 0.0F, 1.0F);
    }

    @Override
    public void tick() {
        super.tick();
        this.setSpriteFromAge(this.sprites);
    }

    public static class Factory implements ParticleProvider<ParticleOptionFluidDrop>, ParticleEngine.SpriteParticleRegistration<ParticleOptionFluidDrop> {

        private final SpriteSet sprites;

        public Factory(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(ParticleOptionFluidDrop type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new ParticleFluidDrop(level, x, y, z, xSpeed, ySpeed, zSpeed, type, sprites);
        }

        @Override
        public ParticleProvider<ParticleOptionFluidDrop> create(SpriteSet sprites) {
            return new ParticleFluidDrop.Factory(sprites);
        }

    }
}
