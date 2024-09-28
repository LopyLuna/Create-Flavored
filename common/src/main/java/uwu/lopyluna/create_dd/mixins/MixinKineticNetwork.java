package uwu.lopyluna.create_dd.mixins;

import com.simibubi.create.content.kinetics.KineticNetwork;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import uwu.lopyluna.create_dd.content.blocks.kinetics.kinetic_motor.KineticMotorBlockEntity;

@Mixin(value = KineticNetwork.class,remap = false)
public class MixinKineticNetwork {
    @Inject(method = "getActualCapacityOf",at=@At("HEAD"),cancellable = true)
    private void getActualCapacityOf(KineticBlockEntity be, CallbackInfoReturnable<Float> cir){
        if (be instanceof KineticMotorBlockEntity motor)
            cir.setReturnValue(motor.calculateAddedStressCapacity());
    }
}
