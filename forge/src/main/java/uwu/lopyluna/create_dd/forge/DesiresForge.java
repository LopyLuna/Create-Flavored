package uwu.lopyluna.create_dd.forge;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import uwu.lopyluna.create_dd.DesireClient;
import uwu.lopyluna.create_dd.Desires;
import uwu.lopyluna.create_dd.infrastructure.config.DesiresConfigs;
import uwu.lopyluna.create_dd.infrastructure.data.DesiresDatagen;
import uwu.lopyluna.create_dd.registry.DesiresFluids;
import uwu.lopyluna.create_dd.registry.DesiresPartialModels;
import uwu.lopyluna.create_dd.registry.DesiresWoodType;

@Mod(Desires.MOD_ID)
public class DesiresForge {
    public DesiresForge() {
        ModLoadingContext modLoadingContext = ModLoadingContext.get();

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;

        Desires.init();
        Desires.REGISTRATE.registerEventListeners(modEventBus);

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> DesiresPartialModels::init);

        modEventBus.addListener(DesiresForge::init);
        modEventBus.addListener(EventPriority.LOWEST, DesiresDatagen::gatherData);
        //modEventBus.addListener(DesiresAllSoundEvents::register);

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> DesireClient.onCtorClient(modEventBus, forgeEventBus));

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        DesiresConfigs.register(modLoadingContext);
    }

    private static void init(final FMLCommonSetupEvent event) {
        DesiresFluids.registerFluidInteractions();

        event.enqueueWork(() -> DesiresWoodType.regStrippables());
    }
}
