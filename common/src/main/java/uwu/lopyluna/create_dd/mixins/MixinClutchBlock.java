package uwu.lopyluna.create_dd.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.simibubi.create.content.kinetics.transmission.ClutchBlock;
import com.simibubi.create.content.kinetics.transmission.GearshiftBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ClutchBlock.class)
public class MixinClutchBlock extends GearshiftBlock {
    public MixinClutchBlock(Properties properties) {
        super(properties);
    }

    @Inject(method = "neighborChanged", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z"))
    private void create_dd$callDetachKineticsEarly(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving, CallbackInfo ci) {
        detachKinetics(worldIn, pos, true);
    }
    
    @Dynamic(value = "skip the detachKinetics call as it is called earlier down the line by another injection and needs to be called before the setBlock instead of after")
    @Redirect(method = "neighborChanged", at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/kinetics/transmission/ClutchBlock;detachKinetics(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Z)V"), remap = false)
    private void create_dd$skipDetachKinetics(ClutchBlock instance, Level level, BlockPos blockPos, boolean b) {}

    @WrapOperation(method = "neighborChanged", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z"))
    private boolean create_dd$fixSetBlockFlags(Level instance, BlockPos pPos, BlockState pNewState, int pFlags, Operation<Boolean> original) {
        return original.call(instance, pPos, pNewState, 2);
    }
}
