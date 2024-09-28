package uwu.lopyluna.create_dd.content.blocks.kinetics.brass_gearbox;

import com.jozufozu.flywheel.backend.Backend;
import com.jozufozu.flywheel.core.PartialModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import uwu.lopyluna.create_dd.registry.DesiresPartialModels;

import java.util.*;

public class BrassGearboxRenderer extends KineticBlockEntityRenderer<BrassGearboxBlockEntity> {
    public BrassGearboxRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }



    @Override
    protected void renderSafe(BrassGearboxBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer,
                              int light, int overlay) {

        if (Backend.canUseInstancing(be.getLevel()))
            return;

        BlockState blockState = be.getBlockState();
        Direction.Axis axisX =  Direction.Axis.X;
        Direction.Axis axisY =  Direction.Axis.Y;
        Direction.Axis axisZ =  Direction.Axis.Z;
        BlockPos pos = be.getBlockPos();
        float angleX = getAngleForTe(be, pos, axisX);
        float angleY = getAngleForTe(be, pos, axisY);
        float angleZ = getAngleForTe(be, pos, axisZ);

        Map<BooleanProperty, PartialModel> brassPanels = new HashMap<>(6);
        brassPanels.put(BrassGearboxBlock.UP_SHAFT, DesiresPartialModels.TOP_BRASS_PANEL);
        brassPanels.put(BrassGearboxBlock.DOWN_SHAFT, DesiresPartialModels.BOTTOM_BRASS_PANEL);
        brassPanels.put(BrassGearboxBlock.NORTH_SHAFT, DesiresPartialModels.NORTH_BRASS_PANEL);
        brassPanels.put(BrassGearboxBlock.SOUTH_SHAFT, DesiresPartialModels.SOUTH_BRASS_PANEL);
        brassPanels.put(BrassGearboxBlock.EAST_SHAFT, DesiresPartialModels.EAST_BRASS_PANEL);
        brassPanels.put(BrassGearboxBlock.WEST_SHAFT, DesiresPartialModels.WEST_BRASS_PANEL);

        List<BooleanProperty> shaftBlockStates = new ArrayList<>(6);
        shaftBlockStates.add(BrassGearboxBlock.DOWN_SHAFT);
        shaftBlockStates.add(BrassGearboxBlock.UP_SHAFT);
        shaftBlockStates.add(BrassGearboxBlock.NORTH_SHAFT);
        shaftBlockStates.add(BrassGearboxBlock.SOUTH_SHAFT);
        shaftBlockStates.add(BrassGearboxBlock.WEST_SHAFT);
        shaftBlockStates.add(BrassGearboxBlock.EAST_SHAFT);

        renderBrassPanels(brassPanels, ms, be, blockState, buffer, light);

        renderShafts(shaftBlockStates, ms, be, blockState, buffer, light);

        shaftBlockStates.clear();
        brassPanels.clear();
    }

    private void renderShafts(List<BooleanProperty> shaftBlockStates, PoseStack ms, BrassGearboxBlockEntity be,
                              BlockState blockState, MultiBufferSource buffer, int light) {
        BlockPos pos = be.getBlockPos();

        int i = 0;
        for (Direction direction : Direction.values()) {
            if (blockState.getValue(shaftBlockStates.get(i))) {
                Direction.Axis axis = direction.getAxis();
                float angle = getAngleForTe(be, pos, axis);

                ms.pushPose();

                SuperByteBuffer shaft = CachedBufferer.partialFacing(AllPartialModels.SHAFT_HALF, be.getBlockState(), direction);
                kineticRotationTransform(shaft, be, axis, angle, light);
                shaft.renderInto(ms, buffer.getBuffer(RenderType.solid()));

                ms.popPose();
            }
            i += 1;
        }
    }

    private void renderBrassPanels(Map<BooleanProperty, PartialModel> brassPanels, PoseStack ms,
                                   BrassGearboxBlockEntity be, BlockState blockState, MultiBufferSource buffer, int light) {
        for (BooleanProperty bool : brassPanels.keySet()) {
            if (!blockState.getValue(bool)) {
                ms.pushPose();

                CachedBufferer.partial(brassPanels.get(bool), be.getBlockState())
                                .light(light)
                                .renderInto(ms, buffer.getBuffer(RenderType.solid())
                                );

                ms.popPose();
            }
        }
    }
}
