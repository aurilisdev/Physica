package electrodynamics.client.particle.lavawithphysics;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.ParticleTypes;

public class ParticleLavaWithPhysics extends TextureSheetParticle {

    private final SpriteSet sprites;
    private final double bounceFactor;

    public ParticleLavaWithPhysics(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, ParticleOptionLavaWithPhysics options, SpriteSet sprites) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        this.sprites = sprites;
        this.gravity = 0.75F;
        this.friction = 0.999F;
        this.hasPhysics = true;
        this.bounceFactor = options.bounceFactor;
        this.xd = xSpeed;
        this.yd = ySpeed;
        this.zd = zSpeed;
        this.quadSize = options.scale;
        this.lifetime = options.lifetime;
        this.setSpriteFromAge(this.sprites);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public int getLightColor(float partialTick) {
        int i = super.getLightColor(partialTick);
        int j = 240;
        int k = i >> 16 & 0xFF;
        return 240 | k << 16;
    }

    @Override
    public float getQuadSize(float scaleFactor) {
        float f = ((float) this.age + scaleFactor) / (float) this.lifetime;
        return this.quadSize * (1.0F - f * f);
    }

    @Override
    public void tick() {
        super.tick();
        this.setSpriteFromAge(sprites);
        if (!this.removed) {
            if(stoppedByCollision) {
                this.xd = -xd * bounceFactor;
                this.yd = -yd * bounceFactor;
                this.zd = -zd * bounceFactor;
                stoppedByCollision = false;
            }

            float f = (float) this.age / (float) this.lifetime;
            if (this.random.nextFloat() > f) {
                this.level.addParticle(ParticleTypes.SMOKE, this.x, this.y, this.z, this.xd, this.yd, this.zd);
            }
        }
    }

    public static class Factory implements ParticleProvider<ParticleOptionLavaWithPhysics>, ParticleEngine.SpriteParticleRegistration<ParticleOptionLavaWithPhysics> {

        private final SpriteSet sprites;

        public Factory(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(ParticleOptionLavaWithPhysics type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new ParticleLavaWithPhysics(level, x, y, z, xSpeed, ySpeed, zSpeed, type, sprites);
        }

        @Override
        public ParticleProvider<ParticleOptionLavaWithPhysics> create(SpriteSet sprites) {
            return new ParticleLavaWithPhysics.Factory(sprites);
        }

    }
}
