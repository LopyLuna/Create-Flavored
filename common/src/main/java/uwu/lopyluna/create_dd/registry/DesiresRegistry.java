package uwu.lopyluna.create_dd.registry;

import uwu.lopyluna.create_dd.Desires;
import uwu.lopyluna.create_dd.content.entities.inert_blazeling.InertBlazeModel;
import uwu.lopyluna.create_dd.content.entities.inert_blazeling.InertBlazeRenderer;

// TODO: I hate forge
@Mod.EventBusSubscriber(modid = Desires.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DesiresRegistry {


    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(DesiresEntityTypes.INERT_BLAZELING.get(), InertBlazeRenderer::new);

    }

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(InertBlazeModel.LAYER_LOCATION, InertBlazeModel::createBodyLayer);

    }
}
