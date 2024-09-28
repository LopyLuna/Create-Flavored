package uwu.lopyluna.create_dd.infrastructure.utility;

import com.simibubi.create.foundation.utility.AbstractBlockBreakQueue;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.BiConsumer;

public class VeinMining {

    public static final Vein NO_VEIN = new Vein(Collections.emptyList());

    @Nonnull
    public static Vein findVein(@Nullable BlockGetter reader, BlockPos startPos, TagKey<Block> filterTag, int maxBlocks) {
        if (reader == null) return NO_VEIN;

        List<BlockPos> matchingBlocks = new ArrayList<>();
        Set<BlockPos> visited = new HashSet<>();
        Queue<BlockPos> frontier = new LinkedList<>();

        frontier.add(startPos);
        visited.add(startPos);

        while (!frontier.isEmpty() && matchingBlocks.size() < maxBlocks) {
            BlockPos currentPos = frontier.poll();
            if (currentPos == null) continue;

            BlockState currentState = reader.getBlockState(currentPos);
            if (currentState.is(filterTag) && ((Level)reader).getWorldBorder().isWithinBounds(currentPos)) {
                matchingBlocks.add(currentPos);

                for (int dx = -1; dx <= 1; dx++) {
                    for (int dy = -1; dy <= 1; dy++) {
                        for (int dz = -1; dz <= 1; dz++) {
                            if (dx == 0 && dy == 0 && dz == 0) continue;

                            BlockPos adjacentPos = currentPos.offset(dx, dy, dz);
                            if (!visited.contains(adjacentPos)) {
                                BlockState adjacentState = reader.getBlockState(adjacentPos);
                                if (adjacentState.is(filterTag)) {
                                    visited.add(adjacentPos);
                                    frontier.add(adjacentPos);
                                }
                            }
                        }
                    }
                }
            }
        }
        return new Vein(matchingBlocks);
    }

    public static class Vein extends AbstractBlockBreakQueue {
        private final List<BlockPos> ores;

        public Vein(List<BlockPos> ores) {
            this.ores = ores;
        }

        @Override
        public void destroyBlocks(Level world, ItemStack tool, @Nullable Player player, BiConsumer<BlockPos, ItemStack> dropConsumer) {
            ores.forEach(makeCallbackFor(world, 0.5f, tool, player, dropConsumer));
        }
    }
}
