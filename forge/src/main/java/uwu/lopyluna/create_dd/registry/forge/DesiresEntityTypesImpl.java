package uwu.lopyluna.create_dd.registry.forge;

import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.foundation.data.CreateEntityBuilder;
import com.tterrag.registrate.util.nullness.NonNullConsumer;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import uwu.lopyluna.create_dd.Desires;
import uwu.lopyluna.create_dd.registry.helper.Lang;

public class DesiresEntityTypesImpl {
    public static <T extends Entity> CreateEntityBuilder<T, ?> contraption(String name, EntityType.EntityFactory<T> factory, NonNullSupplier<NonNullFunction<EntityRendererProvider.Context, EntityRenderer<? super T>>> renderer, int range, int updateFrequency, boolean sendVelocity) {
        return register(name, factory, renderer, MobCategory.MISC, range, updateFrequency, sendVelocity, true, AbstractContraptionEntity::build);
    }

    public static <T extends Entity> CreateEntityBuilder<T, ?> register(String name, EntityType.EntityFactory<T> factory, NonNullSupplier<NonNullFunction<EntityRendererProvider.Context, EntityRenderer<? super T>>> renderer, MobCategory group, int clientTrackingRange, int updateFrequency, boolean sendVelocity, boolean immuneToFire, NonNullConsumer<EntityType.Builder<T>> propertyBuilder) {
        String id = Lang.asId(name);

        return (CreateEntityBuilder<T, ?>) Desires.REGISTRATE.entity(id, factory, group).properties(b -> b.clientTrackingRange(clientTrackingRange).updateInterval(updateFrequency).setShouldReceiveVelocityUpdates(sendVelocity)).properties(propertyBuilder).properties(b -> {
            if (immuneToFire) b.fireImmune();
        }).renderer(renderer);
    }
}
