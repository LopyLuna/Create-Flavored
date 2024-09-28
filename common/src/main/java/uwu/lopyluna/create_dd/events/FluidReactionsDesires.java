package uwu.lopyluna.create_dd.events;

import com.simibubi.create.api.event.PipeCollisionEvent;
import com.simibubi.create.foundation.fluid.FluidHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import uwu.lopyluna.create_dd.registry.DesiresFluids;

@Mod.EventBusSubscriber
public class FluidReactionsDesires {


    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void handlePipeFlowCollisionFallback(PipeCollisionEvent.Flow event) {
        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        Fluid f1 = event.getFirstFluid();
        Fluid f2 = event.getSecondFluid();

        if (f1 == Fluids.WATER && f2 == Fluids.LAVA || f2 == Fluids.WATER && f1 == Fluids.LAVA) {
            event.setState(Blocks.COBBLESTONE.defaultBlockState());
        } else if (f1 == Fluids.LAVA && FluidHelper.hasBlockState(f2)) {
            BlockState lavaInteraction = DesiresFluids.getLavaInteraction(FluidHelper.convertToFlowing(f2).defaultFluidState(), level, pos);
            if (lavaInteraction != null) {
                event.setState(lavaInteraction);
            }
        } else if (f2 == Fluids.LAVA && FluidHelper.hasBlockState(f1)) {
            BlockState lavaInteraction = DesiresFluids.getLavaInteraction(FluidHelper.convertToFlowing(f1).defaultFluidState(), level, pos);
            if (lavaInteraction != null) {
                event.setState(lavaInteraction);
            }
        }
    }


    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void handlePipeSpillCollisionFallback(PipeCollisionEvent.Spill event) {
        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        Fluid pf = event.getPipeFluid();
        Fluid wf = event.getWorldFluid();

        if (FluidHelper.isTag(pf, FluidTags.WATER) && wf == Fluids.LAVA) {
            event.setState(Blocks.OBSIDIAN.defaultBlockState());
        } else if (pf == Fluids.WATER && wf == Fluids.FLOWING_LAVA) {
            event.setState(Blocks.COBBLESTONE.defaultBlockState());
        } else if (pf == Fluids.LAVA && wf == Fluids.WATER) {
            event.setState(Blocks.STONE.defaultBlockState());
        } else if (pf == Fluids.LAVA && wf == Fluids.FLOWING_WATER) {
            event.setState(Blocks.COBBLESTONE.defaultBlockState());
        }

        if (pf == Fluids.LAVA) {
            BlockState lavaInteraction = DesiresFluids.getLavaInteraction(wf.defaultFluidState(), level, pos);
            if (lavaInteraction != null) {
                event.setState(lavaInteraction);
            }
        } else if (wf == Fluids.FLOWING_LAVA && FluidHelper.hasBlockState(pf)) {
            BlockState lavaInteraction = DesiresFluids.getLavaInteraction(FluidHelper.convertToFlowing(pf).defaultFluidState(), level, pos);
            if (lavaInteraction != null) {
                event.setState(lavaInteraction);
            }
        }
    }
}
