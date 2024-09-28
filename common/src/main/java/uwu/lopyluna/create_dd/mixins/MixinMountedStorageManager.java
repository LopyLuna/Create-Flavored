package uwu.lopyluna.create_dd.mixins;

import com.simibubi.create.content.contraptions.MountedFluidStorage;
import com.simibubi.create.content.contraptions.MountedStorageManager;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import uwu.lopyluna.create_dd.content.blocks.logistics.fluid_reservoir.FluidReservoirBlockEntity;
import uwu.lopyluna.create_dd.mixins.accessor.AccessorMountedFluidStorage;

import java.util.Map;

@Mixin(value = MountedStorageManager.class, remap = false)
public class MixinMountedStorageManager {

    @Shadow
    protected Map<BlockPos, MountedFluidStorage> fluidStorage;

    @Inject(at = @At("HEAD"), method = "bindTanks(Ljava/util/Map;)V")
    public void bindTanks(Map<BlockPos, BlockEntity> presentBlockEntities, CallbackInfo ci) {
        fluidStorage.forEach((pos, mfs) -> {
            BlockEntity blockEntity = presentBlockEntities.get(pos);
            if (!(blockEntity instanceof FluidReservoirBlockEntity tank))
                return;
            IFluidTank tankInventory = tank.getTankInventory();
            if (tankInventory instanceof FluidTank)
                ((FluidTank) tankInventory).setFluid(((AccessorMountedFluidStorage) mfs).create_dd$getTank().getFluid());
            tank.getFluidLevel().startWithValue(tank.getFillState());
            mfs.assignBlockEntity(tank);
        });
    }

}
