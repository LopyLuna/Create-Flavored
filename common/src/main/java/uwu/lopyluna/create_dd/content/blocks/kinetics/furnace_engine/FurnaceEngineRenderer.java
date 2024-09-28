package uwu.lopyluna.create_dd.content.blocks.kinetics.furnace_engine;

import com.jozufozu.flywheel.backend.Backend;
import com.jozufozu.flywheel.core.PartialModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.utility.AngleHelper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import uwu.lopyluna.create_dd.registry.DesiresPartialModels;

@SuppressWarnings({"unused"})
public class FurnaceEngineRenderer extends SafeBlockEntityRenderer<FurnaceEngineBlockEntity> {
    public FurnaceEngineRenderer(BlockEntityRendererProvider.Context context) {
    }

    protected void renderSafe(FurnaceEngineBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        if (!Backend.canUseInstancing(be.getLevel())) {
            Float angle = be.getTargetAngle();
            if (angle != null) {
                BlockState blockState = be.getBlockState();
                Direction facing = FurnaceEngineBlock.getFacing(blockState);
                Direction.Axis facingAxis = facing.getAxis();
                Direction.Axis axis = Axis.Y;
                PoweredFlywheelBlockEntity flywheel = be.getFlywheel();
                if (flywheel != null) {
                    axis = KineticBlockEntityRenderer.getRotationAxisOf(flywheel);
                }

                boolean roll90 = facingAxis.isHorizontal() && axis == Axis.Y || facingAxis.isVertical() && axis == Axis.Z;
                float sine = Mth.sin(angle);
                float sine2 = Mth.sin(angle - 1.5707964F);
                float piston = (1.0F - sine) / 4.0F * 24.0F / 16.0F;
                VertexConsumer vb = buffer.getBuffer(RenderType.solid());
                this.transformed(DesiresPartialModels.ENGINE_PISTON, blockState, facing, roll90).translate(0.0, piston, 0.0).light(light).renderInto(ms, vb);
                this.transformed(DesiresPartialModels.ENGINE_LINKAGE, blockState, facing, roll90).centre().translate(0.0, 1.0, 0.0).unCentre().translate(0.0, piston, 0.0).translate(0.0, 0.25, 0.5).rotateX(sine2 * 23.0F).translate(0.0, -0.25, -0.5).light(light).renderInto(ms, vb);
                this.transformed(DesiresPartialModels.ENGINE_CONNECTOR, blockState, facing, roll90).translate(0.0, 2.0, 0.0).centre().rotateXRadians(-angle + 1.5707964F).unCentre().light(light).renderInto(ms, vb);
            }
        }
    }

    private SuperByteBuffer transformed(PartialModel model, BlockState blockState, Direction facing, boolean roll90) {
        return CachedBufferer.partial(model, blockState).centre().rotateY(AngleHelper.horizontalAngle(facing)).rotateX(AngleHelper.verticalAngle(facing) + 90.0F).rotateY(roll90 ? -90.0 : 0.0).unCentre();
    }

    public int getViewDistance() {
        return 128;
    }
}
