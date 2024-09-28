package uwu.lopyluna.create_dd.mixins;

import com.simibubi.create.content.contraptions.MountedFluidStorage;
import com.simibubi.create.foundation.fluid.SmartFluidTank;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;

import io.github.fabricators_of_create.porting_lib.transfer.fluid.FluidTank;
import io.github.fabricators_of_create.porting_lib.util.FluidStack;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import uwu.lopyluna.create_dd.content.blocks.logistics.fluid_reservoir.FluidReservoirBlockEntity;


@Mixin(value = MountedFluidStorage.class, remap = false)
public class MixinMountedFluidStorage {
    @Shadow SmartFluidTank tank;
    @Shadow private BlockEntity blockEntity;
    @Shadow private boolean sendPacket = false;

    @Inject(at = @At("RETURN"), method = "tick(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/core/BlockPos;Z)V")
    public void tick(Entity entity, BlockPos pos, boolean isRemote, CallbackInfo ci) {
        if (blockEntity instanceof FluidReservoirBlockEntity fluidReservoirBlockEntity) {
            fluidReservoirBlockEntity.getFluidLevel().tickChaser();
        }
    }

    @Inject(at = @At("HEAD"), method = "canUseAsStorage(Lnet/minecraft/world/level/block/entity/BlockEntity;)Z", cancellable = true)
    private static void canUseAsStorage(BlockEntity be, CallbackInfoReturnable<Boolean> cir) {
        if (be instanceof FluidReservoirBlockEntity)
            cir.setReturnValue(((FluidReservoirBlockEntity) be).isController());
    }

    @Inject(at = @At("HEAD"), method = "createMountedTank(Lnet/minecraft/world/level/block/entity/BlockEntity;)Lcom/simibubi/create/foundation/fluid/SmartFluidTank;", cancellable = true)
    private void createMountedTank(BlockEntity be, CallbackInfoReturnable<SmartFluidTank> cir) {
        if (be instanceof FluidReservoirBlockEntity)
            cir.setReturnValue(new SmartFluidTank(
                    ((FluidReservoirBlockEntity) be).getTotalTankSize() * FluidReservoirBlockEntity.getCapacityMultiplier(),
                    this::onFluidStackChanged));
    }

    // TODO: Move this mixin to platform-specific stuff
    @Inject(at = @At("HEAD"), method = "updateFluid(Lio/github/fabricators_of_create/porting_lib/util/FluidStack;)V")
    public void updateFluid(FluidStack fluid, CallbackInfo ci) {
        tank.setFluid(fluid);
        if (!(blockEntity instanceof FluidReservoirBlockEntity tankR))
            return;
        float fillState = tank.getFluidAmount() / (float) tank.getCapacity();
        if (tankR.getFluidLevel() == null)
            tankR.setFluidLevel(LerpedFloat.linear()
                    .startWithValue(fillState));
        tankR.getFluidLevel()
                .chase(fillState, 0.5, LerpedFloat.Chaser.EXP);
        FluidTank tankRInventory = tankR.getTankInventory();
        if (tankRInventory instanceof SmartFluidTank smartTank)
            smartTank.setFluid(fluid);
    }

    @Shadow
    private void onFluidStackChanged(FluidStack fs) {
        sendPacket = true;
    }
}
