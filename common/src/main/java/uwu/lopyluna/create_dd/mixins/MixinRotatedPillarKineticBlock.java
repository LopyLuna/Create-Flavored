package uwu.lopyluna.create_dd.mixins;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.kinetics.base.RotatedPillarKineticBlock;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import uwu.lopyluna.create_dd.content.blocks.kinetics.furnace_engine.PoweredFlywheelBlock;

@Mixin(value = RotatedPillarKineticBlock.class)
public abstract class MixinRotatedPillarKineticBlock {
    @Inject(at = @At("HEAD"), method = "getStateForPlacement", cancellable = true)
    private void getStateForPlacement(BlockPlaceContext context, CallbackInfoReturnable<BlockState> cir){
        if (AllBlocks.FLYWHEEL.is(context.getItemInHand().getItem()) && PoweredFlywheelBlock.stillValid(AllBlocks.FLYWHEEL.getDefaultState().setValue(RotatedPillarKineticBlock.AXIS, context.getNearestLookingDirection().getAxis()),context.getLevel(),context.getClickedPos())){
            cir.setReturnValue(PoweredFlywheelBlock.getEquivalent(AllBlocks.FLYWHEEL.getDefaultState(),context.getNearestLookingDirection().getAxis()));
        }
    }
}
