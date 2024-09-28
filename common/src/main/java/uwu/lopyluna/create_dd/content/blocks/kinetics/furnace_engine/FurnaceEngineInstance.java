package uwu.lopyluna.create_dd.content.blocks.kinetics.furnace_engine;

import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.api.instance.DynamicInstance;
import com.jozufozu.flywheel.backend.instancing.blockentity.BlockEntityInstance;
import com.jozufozu.flywheel.core.Materials;
import com.jozufozu.flywheel.core.materials.model.ModelData;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.content.kinetics.steamEngine.SteamEngineBlock;
import com.simibubi.create.foundation.utility.AngleHelper;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.util.Mth;
import uwu.lopyluna.create_dd.registry.DesiresPartialModels;

public class FurnaceEngineInstance extends BlockEntityInstance<FurnaceEngineBlockEntity> implements DynamicInstance {
    protected final ModelData piston;
    protected final ModelData linkage;
    protected final ModelData connector;

    public FurnaceEngineInstance(MaterialManager materialManager, FurnaceEngineBlockEntity blockEntity) {
        super(materialManager, blockEntity);
        this.piston = materialManager.defaultSolid().material(Materials.TRANSFORMED).getModel(DesiresPartialModels.ENGINE_PISTON, this.blockState).createInstance();
        this.linkage = materialManager.defaultSolid().material(Materials.TRANSFORMED).getModel(DesiresPartialModels.ENGINE_LINKAGE, this.blockState).createInstance();
        this.connector = materialManager.defaultSolid().material(Materials.TRANSFORMED).getModel(DesiresPartialModels.ENGINE_CONNECTOR, this.blockState).createInstance();
    }

    public void beginFrame() {
        Float angle = this.blockEntity.getTargetAngle();
        if (angle == null) {
            this.piston.setEmptyTransform();
            this.linkage.setEmptyTransform();
            this.connector.setEmptyTransform();
        } else {
            Direction facing = SteamEngineBlock.getFacing(this.blockState);
            Direction.Axis facingAxis = facing.getAxis();
            Direction.Axis axis = Axis.Z;
            PoweredFlywheelBlockEntity flywheel = this.blockEntity.getFlywheel();
            if (flywheel != null) {
                axis = KineticBlockEntityRenderer.getRotationAxisOf(flywheel);
            }

            boolean roll90 = facingAxis.isHorizontal() && axis == Axis.Y || facingAxis.isVertical() && axis == Axis.Z;
            float sine = Mth.sin(angle);
            float sine2 = Mth.sin(angle - 1.5707964F);
            float piston = (1.0F - sine) / 4.0F * 24.0F / 16.0F;
            this.transformed(this.piston, facing, roll90).translate(0.0, piston, 0.0);
            this.transformed(this.linkage, facing, roll90).centre().translate(0.0, 1.0, 0.0).unCentre().translate(0.0, piston, 0.0).translate(0.0, 0.25, 0.5).rotateX(sine2 * 23.0F).translate(0.0, -0.25, -0.5);
            this.transformed(this.connector, facing, roll90).translate(0.0, 2.0, 0.0).centre().rotateXRadians(-angle + 1.5707964F).unCentre();
        }
    }

    protected ModelData transformed(ModelData modelData, Direction facing, boolean roll90) {
        return modelData.loadIdentity().translate(this.getInstancePosition()).centre().rotateY(AngleHelper.horizontalAngle(facing)).rotateX(AngleHelper.verticalAngle(facing) + 90.0F).rotateY(roll90 ? -90.0 : 0.0).unCentre();
    }

    public void updateLight() {
        this.relight(this.pos, this.piston, this.linkage, this.connector);
    }

    protected void remove() {
        this.piston.delete();
        this.linkage.delete();
        this.connector.delete();
    }
}
