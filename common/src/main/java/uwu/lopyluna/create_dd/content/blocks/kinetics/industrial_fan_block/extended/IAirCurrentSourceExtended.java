package uwu.lopyluna.create_dd.content.blocks.kinetics.industrial_fan_block.extended;

import com.simibubi.create.content.kinetics.fan.IAirCurrentSource;
import net.minecraft.util.Mth;
import uwu.lopyluna.create_dd.infrastructure.config.DKinetics;
import uwu.lopyluna.create_dd.infrastructure.config.DesiresConfigs;

public interface IAirCurrentSourceExtended extends IAirCurrentSource {

    @Override
    default float getMaxDistance() {
        float speed = Math.abs(this.getSpeed());
        DKinetics config = DesiresConfigs.server().kinetics;
        float distanceFactor = Math.min(speed / config.fanRotationArgmax.get(), 1);
        float pushDistance = Mth.lerp(distanceFactor, 3, config.fanPushDistance.get());
        float pullDistance = Mth.lerp(distanceFactor, 3f, config.fanPullDistance.get());
        return this.getSpeed() > 0 ? pushDistance : pullDistance;
    }

}
