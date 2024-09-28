package uwu.lopyluna.create_dd.registry;

import com.simibubi.create.api.connectivity.ConnectivityHandler;
import com.simibubi.create.content.contraptions.BlockMovementChecks;
import net.minecraft.world.level.block.Block;
import uwu.lopyluna.create_dd.content.blocks.logistics.fluid_reservoir.FluidReservoirBlock;
import uwu.lopyluna.create_dd.content.blocks.logistics.item_stockpile.ItemStockpileBlock;

public class DesiresBlockMovementChecks {
    public static void register() {
        BlockMovementChecks.registerAttachedCheck((state, world, pos, direction) -> {
            Block block = state.getBlock();
            if ((block instanceof FluidReservoirBlock || block instanceof ItemStockpileBlock)
                    && ConnectivityHandler.isConnected(world, pos, pos.relative(direction)))
                return BlockMovementChecks.CheckResult.SUCCESS;

            return BlockMovementChecks.CheckResult.PASS;
        });
    }
}
