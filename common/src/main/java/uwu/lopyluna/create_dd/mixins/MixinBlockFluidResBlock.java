package uwu.lopyluna.create_dd.mixins;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import uwu.lopyluna.create_dd.content.blocks.logistics.fluid_reservoir.FluidReservoirBlock;
import uwu.lopyluna.create_dd.content.blocks.logistics.fluid_reservoir.FluidReservoirBlockEntity;

@Mixin(value = Block.class)
public class MixinBlockFluidResBlock {
    @Inject(at = @At("HEAD"), method = "shouldRenderFace", cancellable = true)
    private static void create_dd$shouldRenderFace(BlockState pState, BlockGetter pLevel, BlockPos pOffset, Direction pFace, BlockPos pPos, CallbackInfoReturnable<Boolean> cir) {
        if (pState.getBlock() instanceof FluidReservoirBlock) {
            boolean isConnected = create_DnD$isConnected(pLevel, pPos, pPos.relative(pFace));
            boolean isConnectedOpposite = create_DnD$isConnected(pLevel, pPos, pPos.relative(pFace.getOpposite()));
            cir.setReturnValue(!(isConnected || isConnectedOpposite));
        }
    }


    @Unique
    private static boolean create_DnD$isConnected(BlockGetter level, BlockPos pos, BlockPos other) {
        BlockEntity one = level.getBlockEntity(pos);
        BlockEntity two = level.getBlockEntity(other);
        if (one instanceof FluidReservoirBlockEntity be1 && two instanceof FluidReservoirBlockEntity be2) {
            return be1.getController().equals(be2.getController());
        } else return false;
    }
}
