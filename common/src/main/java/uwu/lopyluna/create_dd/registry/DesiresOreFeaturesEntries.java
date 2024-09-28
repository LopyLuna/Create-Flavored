package uwu.lopyluna.create_dd.registry;

import com.simibubi.create.content.decoration.palettes.AllPaletteStoneTypes;
import com.simibubi.create.foundation.utility.Couple;
import com.simibubi.create.infrastructure.worldgen.OreFeatureConfigEntry;
import net.minecraft.tags.BiomeTags;
import net.minecraftforge.common.ForgeConfigSpec;
import uwu.lopyluna.create_dd.DesireUtil;
import uwu.lopyluna.create_dd.content.world.DesiresLayerPatterns;

import java.util.Objects;

import static uwu.lopyluna.create_dd.Desires.MOD_ID;

@SuppressWarnings({"unused", "SameParameterValue"})
public class DesiresOreFeaturesEntries {

    public static final OreFeatureConfigEntry GABBRO_BLOB =
            Objects.requireNonNull(create("gabbro_blob", 64, 0.85F, -64, 0).standardDatagenExt())
                    .withBlocks(Couple.create(AllPaletteStoneTypes.GRANITE.baseBlock, DesiresPaletteStoneTypes.GABBRO.baseBlock))
                    .biomeTag(BiomeTags.IS_OVERWORLD)
                    .parent();

    public static final OreFeatureConfigEntry DOLOMITE_BLOB =
            Objects.requireNonNull(create("dolomite_blob", 64, 0.85F, -64, 0).standardDatagenExt())
                    .withBlocks(Couple.create(AllPaletteStoneTypes.DIORITE.baseBlock, DesiresPaletteStoneTypes.DOLOMITE.baseBlock))
                    .biomeTag(BiomeTags.IS_OVERWORLD)
                    .parent();

    public static final OreFeatureConfigEntry STRIATED_ERODED_OVERWORLD =
            Objects.requireNonNull(create("striated_eroded_overworld", 24, 1 / 36f, -64, 0).layeredDatagenExt())
                    .withLayerPattern(DesiresLayerPatterns.SPACE_ROCK)
                    .biomeTag(BiomeTags.IS_OVERWORLD)
                    .parent();

    public static final OreFeatureConfigEntry STRIATED_ERODED_NETHER =
            Objects.requireNonNull(create("striated_eroded_nether", 24, 1 / 36f, 0, 100).layeredDatagenExt())
                    .withLayerPattern(DesiresLayerPatterns.SPACE_ROCK)
                    .biomeTag(BiomeTags.IS_NETHER)
                    .parent();

    public static final OreFeatureConfigEntry STRIATED_ORES_OCEANS =
            Objects.requireNonNull(create("striated_ores_oceans", 32, 1 / 18f, -30, 70).layeredDatagenExt())
                    .withLayerPattern(DesiresLayerPatterns.WEATHERED_LIMESTONE)
                    .biomeTag(BiomeTags.IS_OCEAN)
                    .parent();

    private static OreFeatureConfigEntry create(String name, int clusterSize, float frequency, int minHeight, int maxHeight) {
        return new OreFeatureConfigEntry(DesireUtil.asResource(name), clusterSize, frequency, minHeight, maxHeight);
    }

    public static void fillConfig(ForgeConfigSpec.Builder builder) {
        OreFeatureConfigEntry.ALL
                .forEach((id, entry) -> {
                    if (id.getNamespace().equals(MOD_ID)) {
                        builder.push(entry.getName());
                        entry.addToConfig(builder);
                        builder.pop();
                    }
                });
    }

    public static void init() {}
}
