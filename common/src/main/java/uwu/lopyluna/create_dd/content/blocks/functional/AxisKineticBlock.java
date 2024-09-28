package uwu.lopyluna.create_dd.content.blocks.functional;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.base.RotatedPillarKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import uwu.lopyluna.create_dd.registry.DesiresBlockEntityTypes;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@SuppressWarnings({"unused", "deprecation"})
public class AxisKineticBlock extends RotatedPillarKineticBlock implements IBE<KineticBlockEntity> {

    boolean isLarge;

    public AxisKineticBlock(boolean large, Properties properties) {
        super(properties);
        isLarge = large;
    }

    public static AxisKineticBlock small(Properties properties) {
        return new AxisKineticBlock(false, properties);
    }

    public static AxisKineticBlock large(Properties properties) {
        return new AxisKineticBlock(true, properties);
    }

    public boolean isLarge() {
        return isLarge;
    }

    public boolean isSmall() {
        return !isLarge;
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return state.getValue(AXIS) == face.getAxis();
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return state.getValue(AXIS);
    }

    @Override
    public float getParticleTargetRadius() {
        return isLarge() ? 2.5f : 1.125f;
    }

    @Override
    public float getParticleInitialRadius() {
        return isLarge() ? 2.25f : 1f;
    }

    @Override
    public boolean hideStressImpact() {
        return true;
    }


    @Override
    public Class<KineticBlockEntity> getBlockEntityClass() {
        return KineticBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends KineticBlockEntity> getBlockEntityType() {
        return DesiresBlockEntityTypes.AXIS_BLOCK.get();
    }
}
