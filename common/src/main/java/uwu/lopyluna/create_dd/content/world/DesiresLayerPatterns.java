package uwu.lopyluna.create_dd.content.world;

import com.simibubi.create.infrastructure.worldgen.LayerPattern;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.world.level.block.Blocks;
import uwu.lopyluna.create_dd.registry.DesiresPaletteStoneTypes;

public class DesiresLayerPatterns {

    public static final NonNullSupplier<LayerPattern> SPACE_ROCK = () -> LayerPattern.builder()
            .layer(l -> l.weight(2)
                    .block(DesiresPaletteStoneTypes.BRECCIA.getBaseBlock().get())
                    .size(2, 5))
            .layer(l -> l.weight(1)
                    .block(DesiresPaletteStoneTypes.GABBRO.getBaseBlock().get())
                    .block(Blocks.GRANITE)
                    .size(2, 3))
            .layer(l -> l.weight(1)
                    .blocks(Blocks.GRANITE, DesiresPaletteStoneTypes.GABBRO.getBaseBlock().get())
                    .size(2, 2))
            .layer(l -> l.weight(1)
                    .block(Blocks.GLOWSTONE)
                    .size(1, 2))
            .build();


    public static final NonNullSupplier<LayerPattern> WEATHERED_LIMESTONE = () -> LayerPattern.builder()
            .layer(l -> l.weight(1)
                    .passiveBlock())
            .layer(l -> l.weight(2)
                    .block(Blocks.CALCITE))
            .layer(l -> l.weight(1)
                    .block(DesiresPaletteStoneTypes.DOLOMITE.getBaseBlock().get()))
            .layer(l -> l.weight(2)
                    .block(DesiresPaletteStoneTypes.WEATHERED_LIMESTONE.getBaseBlock().get())
                    .size(2, 4))
            .build();
}
