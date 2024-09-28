package uwu.lopyluna.create_dd.content.blocks.kinetics.modular_drill;

import com.jozufozu.flywheel.core.virtual.VirtualRenderWorld;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import com.simibubi.create.content.contraptions.render.ContraptionMatrices;
import com.simibubi.create.content.contraptions.render.ContraptionRenderDispatcher;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public class ModularDrillRenderer extends KineticBlockEntityRenderer<ModularDrillBlockEntity> {

    public ModularDrillRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected SuperByteBuffer getRotatedModel(ModularDrillBlockEntity be, BlockState state) {
        ModularDrillHeadItem.DrillType drillType = be.getCurrentDrillHeadType();
        return switch (drillType) {
            case VEIN -> CachedBufferer.partialFacing(AllPartialModels.DRILL_HEAD, state); //vein
            case ENCHANTABLE -> CachedBufferer.partialFacing(AllPartialModels.DRILL_HEAD, state); //enchant
            case CRUSHER -> CachedBufferer.partialFacing(AllPartialModels.DRILL_HEAD, state);  //crusher
            case SMELTING -> CachedBufferer.partialFacing(AllPartialModels.DRILL_HEAD, state); //smelting
        };
    }


    public static void renderInContraption(MovementContext context, VirtualRenderWorld renderWorld,
                                           ContraptionMatrices matrices, MultiBufferSource buffer) {
        BlockState state = context.state;
        SuperByteBuffer superBuffer = CachedBufferer.partial(AllPartialModels.DRILL_HEAD, state);
        Direction facing = state.getValue(ModularDrillBlock.FACING);

        float speed = context.contraption.stalled
                || !VecHelper.isVecPointingTowards(context.relativeMotion, facing
                .getOpposite()) ? context.getAnimationSpeed() : 0;
        float time = AnimationTickHolder.getRenderTime() / 20;
        float angle = ((time * speed) % 360);

        superBuffer
                .transform(matrices.getModel())
                .centre()
                .rotateY(AngleHelper.horizontalAngle(facing))
                .rotateX(AngleHelper.verticalAngle(facing))
                .rotateZ(angle)
                .unCentre()
                .light(matrices.getWorld(),
                        ContraptionRenderDispatcher.getContraptionWorldLight(context, renderWorld))
                .renderInto(matrices.getViewProjection(), buffer.getBuffer(RenderType.solid()));
    }
}
