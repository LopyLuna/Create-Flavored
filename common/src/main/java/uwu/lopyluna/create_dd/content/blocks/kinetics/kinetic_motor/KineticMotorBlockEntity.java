//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package uwu.lopyluna.create_dd.content.blocks.kinetics.kinetic_motor;

import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.kinetics.base.GeneratingKineticBlockEntity;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollValueBehaviour;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.VecHelper;
import java.util.List;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import uwu.lopyluna.create_dd.registry.DesiresBlocks;

@SuppressWarnings({"unused"})
public class KineticMotorBlockEntity extends GeneratingKineticBlockEntity {
    public static final int DEFAULT_SPEED = 16;
    public static final int MAX_SPEED = 32;
    protected ScrollValueBehaviour generatedSpeed;

    public KineticMotorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
        int max = 32;
        this.generatedSpeed = new KineticMotorScrollValueBehaviour(Lang.translateDirect("kinetics.creative_motor.rotation_speed"), this, new MotorValueBox());
        this.generatedSpeed.between(-max, max);
        this.generatedSpeed.value = 16;
        this.generatedSpeed.withCallback((i) -> this.updateGeneratedRotation());
        behaviours.add(this.generatedSpeed);
    }

    public void initialize() {
        super.initialize();
        if (!this.hasSource() || this.getGeneratedSpeed() > this.getTheoreticalSpeed()) {
            this.updateGeneratedRotation();
        }

    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        if (!IRotate.StressImpact.isEnabled())
            return super.addToGoggleTooltip(tooltip, isPlayerSneaking);

        float stressBase = calculateAddedStressCapacity();
        if (Mth.equal(stressBase, 0))
            return super.addToGoggleTooltip(tooltip, isPlayerSneaking);

        Lang.translate("gui.goggles.generator_stats")
                .forGoggles(tooltip);
        Lang.translate("tooltip.capacityProvided")
                .style(ChatFormatting.GRAY)
                .forGoggles(tooltip);


        Lang.number(stressBase)
                .translate("generic.unit.stress")
                .style(ChatFormatting.AQUA)
                .space()
                .add(Lang.translate("gui.goggles.at_current_speed")
                        .style(ChatFormatting.DARK_GRAY))
                .forGoggles(tooltip, 1);

        return true;
    }
    public float getGeneratedSpeed() {
        return !DesiresBlocks.KINETIC_MOTOR.has(this.getBlockState()) ? 0.0F : convertToDirection((float)this.generatedSpeed.getValue(), this.getBlockState().getValue(KineticMotorBlock.FACING));
    }

    static class MotorValueBox extends ValueBoxTransform.Sided {
        MotorValueBox() {
        }

        protected Vec3 getSouthLocation() {
            return VecHelper.voxelSpace(8.0, 8.0, 12.5);
        }

        public Vec3 getLocalOffset(BlockState state) {
            Direction facing = state.getValue(KineticMotorBlock.FACING);
            return super.getLocalOffset(state).add(Vec3.atLowerCornerOf(facing.getNormal()).scale(-0.0625));
        }

        public void rotate(BlockState state, PoseStack ms) {
            super.rotate(state, ms);
            Direction facing = state.getValue(KineticMotorBlock.FACING);
            if (facing.getAxis() != Axis.Y) {
                if (this.getSide() == Direction.UP) {
                    TransformStack.cast(ms).rotateZ(-AngleHelper.horizontalAngle(facing) + 180.0F);
                }
            }
        }

        protected boolean isSideActive(BlockState state, Direction direction) {
            Direction facing = state.getValue(KineticMotorBlock.FACING);
            if (facing.getAxis() != Axis.Y && direction == Direction.DOWN) {
                return false;
            } else {
                return direction.getAxis() != facing.getAxis();
            }
        }
    }
}
