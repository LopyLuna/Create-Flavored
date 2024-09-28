package uwu.lopyluna.create_dd.content.blocks.kinetics.furnace_engine;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.simibubi.create.foundation.particle.ICustomParticleDataWithSprite;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import uwu.lopyluna.create_dd.registry.DesiresParticleTypes;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Locale;

@SuppressWarnings({"deprecation"})
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SmokeJetParticleData implements ParticleOptions, ICustomParticleDataWithSprite<SmokeJetParticleData> {

    public static final Codec<SmokeJetParticleData> CODEC = RecordCodecBuilder.create(i -> i
            .group(Codec.FLOAT.fieldOf("speed")
                    .forGetter(p -> p.speed))
            .apply(i, SmokeJetParticleData::new));

    public static final ParticleOptions.Deserializer<SmokeJetParticleData> DESERIALIZER =
            new ParticleOptions.Deserializer<>() {
                public SmokeJetParticleData fromCommand(ParticleType<SmokeJetParticleData> particleTypeIn, StringReader reader) throws CommandSyntaxException {
                    reader.expect(' ');
                    float speed = reader.readFloat();
                    return new SmokeJetParticleData(speed);
                }

                public SmokeJetParticleData fromNetwork(ParticleType<SmokeJetParticleData> particleTypeIn, FriendlyByteBuf buffer) {
                    return new SmokeJetParticleData(buffer.readFloat());
                }
            };

    float speed;

    public SmokeJetParticleData(float speed) {
        this.speed = speed;
    }

    public SmokeJetParticleData() {
        this(0);
    }

    @Override
    public ParticleType<?> getType() {
        return DesiresParticleTypes.SMOKE_JET.get();
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buffer) {
        buffer.writeFloat(speed);
    }

    @Override
    public String writeToString() {
        return String.format(Locale.ROOT, "%s %f", DesiresParticleTypes.SMOKE_JET.parameter(), speed);
    }

    @Override
    public Deserializer<SmokeJetParticleData> getDeserializer() {
        return DESERIALIZER;
    }

    @Override
    public Codec<SmokeJetParticleData> getCodec(ParticleType<SmokeJetParticleData> type) {
        return CODEC;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public ParticleEngine.SpriteParticleRegistration<SmokeJetParticleData> getMetaFactory() {
        return SmokeJetParticle.Factory::new;
    }
}
