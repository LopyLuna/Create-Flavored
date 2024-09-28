package uwu.lopyluna.create_dd.content.blocks.kinetics.hydraulic_press;

import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.api.instance.DynamicInstance;
import com.jozufozu.flywheel.core.Materials;
import com.jozufozu.flywheel.core.materials.oriented.OrientedData;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import com.simibubi.create.content.kinetics.base.ShaftInstance;
import com.simibubi.create.content.kinetics.press.MechanicalPressBlock;
import com.simibubi.create.content.kinetics.press.PressingBehaviour;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import uwu.lopyluna.create_dd.registry.DesiresPartialModels;

public class HydraulicPressInstance extends ShaftInstance<HydraulicPressBlockEntity> implements DynamicInstance {
    private final OrientedData pressHead;

    public HydraulicPressInstance(MaterialManager materialManager, HydraulicPressBlockEntity blockEntity) {
        super(materialManager, blockEntity);

        pressHead = materialManager.defaultSolid()
                .material(Materials.ORIENTED)
                .getModel(DesiresPartialModels.HYDRAULIC_PRESS_HEAD, blockState)
                .createInstance();

        Quaternion q = Vector3f.YP
                .rotationDegrees(AngleHelper.horizontalAngle(blockState.getValue(MechanicalPressBlock.HORIZONTAL_FACING)));

        pressHead.setRotation(q);

        transformModels();
    }

    @Override
    public void beginFrame() {
        transformModels();
    }

    private void transformModels() {
        float renderedHeadOffset = getRenderedHeadOffset(blockEntity);

        pressHead.setPosition(getInstancePosition())
                .nudge(0, (-renderedHeadOffset / 1.2f) - 0.1f, 0);
    }

    private float getRenderedHeadOffset(HydraulicPressBlockEntity press) {
        PressingBehaviour pressingBehaviour = press.getPressingBehaviour();
        return pressingBehaviour.getRenderedHeadOffset(AnimationTickHolder.getPartialTicks())
                * pressingBehaviour.mode.headOffset;
    }

    @Override
    public void updateLight() {
        super.updateLight();

        relight(pos, pressHead);
    }

    @Override
    public void remove() {
        super.remove();
        pressHead.delete();
    }
}
