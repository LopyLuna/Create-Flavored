package uwu.lopyluna.create_dd.content.blocks.contraptions.bore_block;

import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import com.simibubi.create.content.contraptions.mounted.MountedContraption;
import com.simibubi.create.content.kinetics.base.BlockBreakingMovementBehaviour;
import com.simibubi.create.content.kinetics.drill.DrillBlock;
import com.simibubi.create.content.trains.entity.CarriageContraption;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;

public class BoreBlockMovementBehaviour extends BlockBreakingMovementBehaviour {

    @Override
    protected DamageSource getDamageSource() {
        return DrillBlock.damageSourceDrill;
    }

    protected float getBlockBreakingSpeed(MovementContext context) {
        float lowerLimit = 1 / 128f;
        if (context.contraption instanceof MountedContraption)
            lowerLimit = 1f;
        if (context.contraption instanceof CarriageContraption)
            lowerLimit = 2f;
        return Mth.clamp(Math.abs(context.getAnimationSpeed()) / 350f, lowerLimit, 16f);
    }
}
