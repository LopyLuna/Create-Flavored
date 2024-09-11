package uwu.lopyluna.create_dd.infrastructure.utility;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.ArrayList;
import java.util.List;

public class BoreMining {

    public static List<BlockPos> getBlocksToBeDestroyed(int range, BlockPos initalBlockPos, ServerPlayer player) {

        List<BlockPos> positions = new ArrayList<>();

        BlockHitResult traceResult = player.level.clip(new ClipContext(player.getEyePosition(1f),
                (player.getEyePosition(1f).add(player.getViewVector(1f).scale(6f))),
                ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));
        if(traceResult.getType() == HitResult.Type.MISS) {
            return null;
        }

        if(traceResult.getDirection() == Direction.DOWN || traceResult.getDirection() == Direction.UP) {
            for(int x = -range; x <= range; x++) {
                for(int y = -range; y <= range; y++) {
                    BlockPos pos = expandPos(x, 0, y, initalBlockPos);
                    if (player.level.getWorldBorder().isWithinBounds(pos)) {
                        positions.add(pos);
                    }
                }
            }
        }

        if(traceResult.getDirection() == Direction.NORTH || traceResult.getDirection() == Direction.SOUTH) {
            for(int x = -range; x <= range; x++) {
                for(int y = -range; y <= range; y++) {
                    BlockPos pos = expandPos(x, y, 0, initalBlockPos);
                    if (player.level.getWorldBorder().isWithinBounds(pos)) {
                        positions.add(pos);
                    }
                }
            }
        }

        if(traceResult.getDirection() == Direction.EAST || traceResult.getDirection() == Direction.WEST) {
            for(int x = -range; x <= range; x++) {
                for(int y = -range; y <= range; y++) {
                    BlockPos pos = expandPos(0, y, x, initalBlockPos);
                    if (player.level.getWorldBorder().isWithinBounds(pos)) {
                        positions.add(pos);
                    }
                }
            }
        }
        return positions;
    }

    public static BlockPos expandPos(int x, int y, int z, BlockPos initalBlockPos) {
        return new BlockPos(initalBlockPos.getX() + x, initalBlockPos.getY() + y, initalBlockPos.getZ() + z);
    }
}
