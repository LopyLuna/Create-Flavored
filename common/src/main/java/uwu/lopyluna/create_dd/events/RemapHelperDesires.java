package uwu.lopyluna.create_dd.events;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.MissingMappingsEvent;
import uwu.lopyluna.create_dd.Desires;

import java.util.HashMap;
import java.util.Map;

import static uwu.lopyluna.create_dd.Desires.MOD_ID;
import static uwu.lopyluna.create_dd.registry.DesiresBlockEntityTypes.*;
import static uwu.lopyluna.create_dd.registry.DesiresItems.*;
import static uwu.lopyluna.create_dd.registry.DesiresPaletteBlocks.*;

@Mod.EventBusSubscriber
public class RemapHelperDesires {
    private static final Map<String, ResourceLocation> reMap = new HashMap<>();
    static {
        reMap.put("reversed_gearshift", INVERSE_BOX.getId());
        reMap.put("steel_block", DARK_METAL_BLOCK.getId());
        reMap.put("steel_casing", DARK_METAL_PLATING.getId());
        reMap.put("steel_polished_block", DARK_METAL_BLOCK.getId());
        reMap.put("steel_polished_stairs", DARK_METAL_STAIRS.getId());
        reMap.put("steel_polished_slab", DARK_METAL_SLAB.getId());
        reMap.put("steel_tiled_block", DARK_METAL_BRICKS.getId());
        reMap.put("steel_tiled_stairs", DARK_METAL_BRICK_STAIRS.getId());
        reMap.put("steel_tiled_slab", DARK_METAL_BRICK_STAIRS.getId());
        reMap.put("horizontal_hazard_block", HAZARD_BLOCK.getId());
        reMap.put("hazard_block_r", HAZARD_BLOCK.getId());
        reMap.put("horizontal_hazard_block_r", HAZARD_BLOCK.getId());
        reMap.put("inductive_mechanism", KINETIC_MECHANISM.getId());
        reMap.put("incomplete_inductive_mechanism", INCOMPLETE_KINETIC_MECHANISM.getId());
        reMap.put("lapis_alloy", BURY_BLEND.getId());

        reMap.put("magnet", HANDHELD_NOZZLE.getId());
        reMap.put("portable_fan", HANDHELD_NOZZLE.getId());
        reMap.put("block_zapper", BLOCK_ZAPPER.getId());
    }

    @SubscribeEvent
    public static void remapBlocks(MissingMappingsEvent event) {
        for (MissingMappingsEvent.Mapping<Block> mapping : event.getMappings(Registry.BLOCK_REGISTRY, MOD_ID)) {
            ResourceLocation key = mapping.getKey();
            String path = key.getPath();
            ResourceLocation remappedId = reMap.get(path);
            if (remappedId != null) {
                Block remapped = ForgeRegistries.BLOCKS.getValue(remappedId);
                if (remapped != null) {
                    Desires.LOGGER.warn("Remapping block '{}' to '{}'", key, remappedId);
                    try {
                        mapping.remap(remapped);
                    } catch (Throwable t) {
                        Desires.LOGGER.warn("Remapping block '{}' to '{}' failed: {}", key, remappedId, t);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void remapItems(MissingMappingsEvent event) {
        for (MissingMappingsEvent.Mapping<Item> mapping : event.getMappings(Registry.ITEM_REGISTRY, MOD_ID)) {
            ResourceLocation key = mapping.getKey();
            String path = key.getPath();
            ResourceLocation remappedId = reMap.get(path);
            if (remappedId != null) {
                Item remapped = ForgeRegistries.ITEMS.getValue(remappedId);
                if (remapped != null) {
                    Desires.LOGGER.warn("Remapping item '{}' to '{}'", key, remappedId);
                    try {
                        mapping.remap(remapped);
                    } catch (Throwable t) {
                        Desires.LOGGER.warn("Remapping item '{}' to '{}' failed: {}", key, remappedId, t);
                    }
                }
            }
        }
    }
}
