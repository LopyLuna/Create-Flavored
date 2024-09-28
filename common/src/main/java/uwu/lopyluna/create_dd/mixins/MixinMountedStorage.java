package uwu.lopyluna.create_dd.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.simibubi.create.content.contraptions.MountedStorage;

import io.github.fabricators_of_create.porting_lib.transfer.item.ItemStackHandler;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import uwu.lopyluna.create_dd.content.blocks.logistics.item_stockpile.ItemStockpileBlockEntity;

@Mixin(value = MountedStorage.class, remap = false)
public class MixinMountedStorage {

    @Shadow boolean noFuel;
    @Shadow private BlockEntity blockEntity;
    @Shadow ItemStackHandler handler;
    @Shadow boolean valid;

    @Inject(at = @At("HEAD"), method = "canUseAsStorage(Lnet/minecraft/world/level/block/entity/BlockEntity;)Z", cancellable = true)
    private static void canUseAsStorage(BlockEntity be, CallbackInfoReturnable<Boolean> cir) {
        if (be instanceof ItemStockpileBlockEntity)
            cir.setReturnValue(true);
    }

    @WrapOperation(method = "<init>(Lnet/minecraft/world/level/block/entity/BlockEntity;)V", at = @At(value = "CONSTANT", args = "classValue=com/simibubi/create/content/logistics/vault/ItemVaultBlockEntity"))
    private boolean MountedStorage(Object object, Operation<Boolean> original) {
        return original.call(object) || object instanceof ItemStockpileBlockEntity;
    }
    
    // TODO: Move this to platform-specific stuff

    @Inject(at = @At("HEAD"), method = "removeStorageFromWorld()V", cancellable = true)
    private void removeStorageFromWorld(CallbackInfo ci) {
        if (blockEntity instanceof ItemStockpileBlockEntity) {
            handler = ((ItemStockpileBlockEntity) blockEntity).getInventoryOfBlock();
            valid = true;
            ci.cancel();
        }
    }

    @Inject(at = @At("HEAD"), method = "addStorageToWorld(Lnet/minecraft/world/level/block/entity/BlockEntity;)V", cancellable = true)
    private void addStorageToWorld(BlockEntity be, CallbackInfo ci) {
        if (be instanceof ItemStockpileBlockEntity) {
            ((ItemStockpileBlockEntity) be).applyInventoryToBlock(handler);
            ci.cancel();
        }
    }
}
