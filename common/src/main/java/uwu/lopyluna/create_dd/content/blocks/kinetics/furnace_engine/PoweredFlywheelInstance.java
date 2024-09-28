package uwu.lopyluna.create_dd.content.blocks.kinetics.furnace_engine;

import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.api.instance.DynamicInstance;
import com.jozufozu.flywheel.core.materials.model.ModelData;
import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityInstance;
import com.simibubi.create.content.kinetics.base.flwdata.RotatingData;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import net.minecraft.core.Direction;

public class PoweredFlywheelInstance extends KineticBlockEntityInstance<PoweredFlywheelBlockEntity> implements DynamicInstance {
    protected final RotatingData shaft = this.setup(this.getRotatingMaterial().getModel(this.shaft()).createInstance());
    protected final ModelData wheel;
    protected float lastAngle = Float.NaN;

    public PoweredFlywheelInstance(MaterialManager materialManager, PoweredFlywheelBlockEntity blockEntity) {
        super(materialManager, blockEntity);
        this.wheel = this.getTransformMaterial().getModel(this.blockState).createInstance();
        this.animate(blockEntity.angle);
    }

    public void beginFrame() {
        float partialTicks = AnimationTickHolder.getPartialTicks();
        float speed = this.blockEntity.visualSpeed.getValue(partialTicks) * 3.0F / 10.0F;
        float angle = this.blockEntity.angle + speed * partialTicks;
        if (!((double)Math.abs(angle - this.lastAngle) < 0.001)) {
            this.animate(angle);
            this.lastAngle = angle;
        }
    }

    private void animate(float angle) {
        PoseStack ms = new PoseStack();
        TransformStack msr = TransformStack.cast(ms);
        msr.translate(this.getInstancePosition());
        msr.centre().rotate(Direction.get(Direction.AxisDirection.POSITIVE, this.axis), AngleHelper.rad(angle)).unCentre();
        this.wheel.setTransform(ms);
    }

    public void update() {
        this.updateRotation(this.shaft);
    }

    public void updateLight() {
        this.relight(this.pos, this.shaft, this.wheel);
    }

    public void remove() {
        this.shaft.delete();
        this.wheel.delete();
    }
}
