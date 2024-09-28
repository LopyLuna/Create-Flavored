package uwu.lopyluna.create_dd.content.blocks.kinetics.cog_crank;

import com.jozufozu.flywheel.api.Instancer;
import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.api.instance.DynamicInstance;
import com.jozufozu.flywheel.core.materials.model.ModelData;
import com.simibubi.create.content.kinetics.base.SingleRotatingInstance;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;


public class CogCrankInstance extends SingleRotatingInstance<CogCrankBlockEntity> implements DynamicInstance {

    private final ModelData crank;

    public CogCrankInstance(MaterialManager modelManager, CogCrankBlockEntity blockEntity) {
        super(modelManager, blockEntity);
        Instancer<ModelData> model = blockEntity.getRenderedHandleInstance(getTransformMaterial());
        crank = model.createInstance();
        rotateCrank();
    }

    @Override
    public void beginFrame() {
        if (crank == null)
            return;

        rotateCrank();
    }

    private void rotateCrank() {
        float angle = blockEntity.getIndependentAngle(AnimationTickHolder.getPartialTicks());
        Direction.Axis axis = blockState.getValue(BlockStateProperties.AXIS);

        crank.loadIdentity()
                .translate(getInstancePosition())
                .centre()
                .rotate(Direction.get(Direction.AxisDirection.POSITIVE, axis), angle)
                .unCentre();
    }

    @Override
    public void init() {
        if (blockEntity.shouldRenderCog())
            super.init();
    }

    @Override
    public void remove() {
        if (blockEntity.shouldRenderCog())
            super.remove();
        if (crank != null)
            crank.delete();
    }

    @Override
    public void update() {
        if (blockEntity.shouldRenderCog())
            super.update();
    }

    @Override
    public void updateLight() {
        if (blockEntity.shouldRenderCog())
            super.updateLight();
        if (crank != null)
            relight(pos, crank);
    }
}