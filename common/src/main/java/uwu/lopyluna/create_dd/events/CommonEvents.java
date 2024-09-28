package uwu.lopyluna.create_dd.events;

import com.simibubi.create.foundation.ModFilePackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forgespi.language.IModFileInfo;
import net.minecraftforge.forgespi.locating.IModFile;
import uwu.lopyluna.create_dd.Desires;
import uwu.lopyluna.create_dd.DesireUtil;

@Mod.EventBusSubscriber
public class CommonEvents {

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModBusEvents {

        @SubscribeEvent
        public static void addPackFinders(AddPackFindersEvent event) {
            if (event.getPackType() == PackType.CLIENT_RESOURCES) {
                IModFileInfo modFileInfo = ModList.get().getModFileById(Desires.MOD_ID);
                if (modFileInfo == null) {
                    Desires.LOGGER.error("Could not find Create DD mod file info; built-in resource packs will be missing!");
                    return;
                }
                IModFile modFile = modFileInfo.getFile();
                event.addRepositorySource((consumer, constructor) -> {
                    consumer.accept(Pack.create(DesireUtil.asResource("create_invert_boxes").toString(), true, () -> new ModFilePackResources("Create Invert Boxes", modFile, "resourcepacks/create_invert_boxes"), constructor, Pack.Position.TOP, PackSource.DEFAULT));
                });
            }
        }
    }
}
