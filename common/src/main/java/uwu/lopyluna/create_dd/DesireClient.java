package uwu.lopyluna.create_dd;

import uwu.lopyluna.create_dd.infrastructure.client.DebugOutliner;
import uwu.lopyluna.create_dd.infrastructure.gui.DesiresBaseConfigScreen;
import uwu.lopyluna.create_dd.infrastructure.ponder.DesirePonderTags;
import uwu.lopyluna.create_dd.infrastructure.ponder.DesiresPonderIndex;
import uwu.lopyluna.create_dd.registry.DesiresParticleTypes;

import static uwu.lopyluna.create_dd.Desires.MOD_ID;

import dev.architectury.platform.Mod;
import dev.architectury.platform.Platform;

@SuppressWarnings({"unused"})
public class DesireClient {
    public static DebugOutliner DEBUG_OUTLINER = new DebugOutliner();

    public static void onCtorClient(IEventBus modEventBus, IEventBus forgeEventBus) {
        modEventBus.addListener(DesireClient::clientInit);
        modEventBus.addListener(DesiresParticleTypes::registerFactories);
    }

    public static void clientInit(final FMLClientSetupEvent event) {

        ModLoadingContext.get().getActiveContainer().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory((minecraft, screen) -> new DesiresBaseConfigScreen(screen, MOD_ID)));

        DesirePonderTags.register();
        DesiresPonderIndex.register();
    }


    @Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModBusEvents {
        @SubscribeEvent
        public static void onLoadComplete(FMLLoadCompleteEvent event) {
            Mod createContainer = Platform
                    .getOptionalMod(MOD_ID)
                    .orElseThrow(() -> new IllegalStateException("Create mod missing (somehow?)!"));
            createContainer.registerConfigurationScreen(() -> new ConfigScreenHandler.ConfigScreenFactory(
                            (mc, previousScreen) -> DesiresBaseConfigScreen.forDesires(previousScreen)));
        }
    }
}
