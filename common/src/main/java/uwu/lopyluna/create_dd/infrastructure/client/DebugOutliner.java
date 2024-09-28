package uwu.lopyluna.create_dd.infrastructure.client;

import com.simibubi.create.foundation.outliner.Outline;
import com.simibubi.create.foundation.outliner.Outliner;
import com.simibubi.create.foundation.utility.Color;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;

import java.util.function.Consumer;

/**
 * Extension of creates outline to give some quick one line indicators
 * */
public class DebugOutliner extends Outliner {
    
    public Outline.OutlineParams showBlockPos(Object slot, BlockPos pos) {
        return showAABB(slot, new AABB(pos))
            .colored(Color.WHITE)
            .lineWidth(1 / 16f);
    }
    
    /**Same action as {@link DebugOutliner#showBlockPos(Object, BlockPos)}, but is only linked to position, should only be used for static / non-moving positions*/
    public Outline.OutlineParams showBlockPos(BlockPos pos) {
        return showBlockPos(pos, pos);
    }
    
    /**Performs {@link DebugOutliner#showBlockPos(BlockPos)} for each block*/
    public void showBlockPositions(Iterable<BlockPos> positions) {
        for (BlockPos pos : positions) {
            showBlockPos(pos);
        }
    }
    
    /**Performs {@link DebugOutliner#showBlockPos(BlockPos)} for each block, and applies params using the paramsApplier argument*/
    public void showBlockPositions(Iterable<BlockPos> positions, Consumer<Outline.OutlineParams> paramsApplier) {
        for (BlockPos pos : positions) {
            paramsApplier.accept(showBlockPos(pos));
        }
    }
    
}
