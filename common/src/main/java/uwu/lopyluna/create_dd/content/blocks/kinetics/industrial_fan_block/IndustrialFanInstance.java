package uwu.lopyluna.create_dd.content.blocks.kinetics.industrial_fan_block;

import com.jozufozu.flywheel.api.MaterialManager;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityInstance;
import com.simibubi.create.content.kinetics.base.flwdata.RotatingData;
import com.simibubi.create.foundation.render.AllMaterialSpecs;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import uwu.lopyluna.create_dd.registry.DesiresPartialModels;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.FACING;

public class IndustrialFanInstance extends KineticBlockEntityInstance<IndustrialFanBlockEntity> {

    protected final RotatingData shaft;
    protected final RotatingData fan;
    final Direction direction;
    private final Direction opposite;

    public IndustrialFanInstance(MaterialManager materialManager, IndustrialFanBlockEntity blockEntity) {
        super(materialManager, blockEntity);

        direction = blockState.getValue(FACING);


        opposite = direction.getOpposite();
        shaft = materialManager.defaultCutout()
                .material(AllMaterialSpecs.ROTATING)
                .getModel(DesiresPartialModels.INDUSTRIAL_FAN_POWER, blockState, opposite)
                .createInstance();

        fan = materialManager.defaultCutout()
                .material(AllMaterialSpecs.ROTATING)
                .getModel(DesiresPartialModels.INDUSTRIAL_FAN_INNER, blockState, opposite)
                .createInstance();

        setup(shaft);
        setup(fan, getFanSpeed());
    }

    private float getFanSpeed() {
        float speed = blockEntity.getSpeed() * 5;
        if (speed > 0)
            speed = Mth.clamp(speed, 80, 64 * 20);
        if (speed < 0)
            speed = Mth.clamp(speed, -64 * 20, -80);
        return speed;
    }

    @Override
    public void update() {
        updateRotation(shaft);
        updateRotation(fan, getFanSpeed());
    }

    @Override
    public void updateLight() {
        relight(pos, shaft);

        BlockPos inFront = pos.relative(direction);
        relight(inFront, fan);
    }

    @Override
    public void remove() {
        shaft.delete();
        fan.delete();
    }
}