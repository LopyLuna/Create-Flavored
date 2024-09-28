package uwu.lopyluna.create_dd.content.blocks.kinetics.cog_crank;

import com.jozufozu.flywheel.api.Instancer;
import com.jozufozu.flywheel.api.Material;
import com.jozufozu.flywheel.core.materials.model.ModelData;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.kinetics.base.GeneratingKineticBlockEntity;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.simpleRelays.ICogWheel;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.utility.AnimationTickHolder;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import uwu.lopyluna.create_dd.registry.DesiresBlocks;
import uwu.lopyluna.create_dd.registry.DesiresPartialModels;

import java.util.List;

public class CogCrankBlockEntity extends GeneratingKineticBlockEntity {

    public int inUse;
    public boolean backwards;
    public float independentAngle;
    public float chasingVelocity;

    public CogCrankBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public void turn(boolean back) {
        boolean update = getGeneratedSpeed() == 0 || back != backwards;

        inUse = 10;
        this.backwards = back;
        if (update) {
            assert level != null;
            if (!level.isClientSide) updateGeneratedRotation();
        }
    }

    public float getIndependentAngle(float partialTicks) {
        return (independentAngle + partialTicks * chasingVelocity) / 360;
    }

    @Override
    public float getGeneratedSpeed() {
        Block block = getBlockState().getBlock();
        if (!(block instanceof CogCrankBlock crank))
            return 0;
        return (inUse == 0 ? 0 : clockwise() ? -1 : 1) * crank.getRotationSpeed();
    }

    protected boolean clockwise() {
        return backwards;
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        compound.putInt("InUse", inUse);
        compound.putBoolean("Backwards", backwards);
        super.write(compound, clientPacket);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        inUse = compound.getInt("InUse");
        backwards = compound.getBoolean("Backwards");
        super.read(compound, clientPacket);
    }

    @Override
    public void tick() {
        super.tick();

        float actualSpeed = getSpeed();
        chasingVelocity += ((actualSpeed * 10 / 3f) - chasingVelocity) * .25f;
        independentAngle += chasingVelocity;

        if (inUse > 0) {
            inUse--;

            if (inUse == 0) {
                assert level != null;
                if (!level.isClientSide) {
                    sequenceContext = null;
                    updateGeneratedRotation();
                }
            }
        }
    }
    
    @Environment(EnvType.CLIENT)
    public SuperByteBuffer getRenderedHandle() {
        BlockState blockState = getBlockState();
        Direction.Axis axis = blockState.getOptionalValue(CogCrankBlock.AXIS).orElse(Direction.Axis.Y);
        return CachedBufferer.partialFacing(DesiresPartialModels.COG_CRANK_HANDLE, blockState, Direction.get(Direction.AxisDirection.POSITIVE, axis));
    }


    @Environment(EnvType.CLIENT)
    public Instancer<ModelData> getRenderedHandleInstance(Material<ModelData> material) {
        BlockState blockState = getBlockState();
        Direction.Axis axis = blockState.getOptionalValue(CogCrankBlock.AXIS).orElse(Direction.Axis.Y);
        return material.getModel(DesiresPartialModels.COG_CRANK_HANDLE, blockState, Direction.get(Direction.AxisDirection.POSITIVE, axis).getOpposite());
    }

    @Environment(EnvType.CLIENT)
    public boolean shouldRenderCog() {
        return true;
    }
    
    @Override
    protected Block getStressConfigKey() {
        return DesiresBlocks.COG_CRANK.has(getBlockState()) ? DesiresBlocks.COG_CRANK.get()
                : AllBlocks.COPPER_VALVE_HANDLE.get();
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void tickAudio() {
        super.tickAudio();
        if (inUse > 0 && AnimationTickHolder.getTicks() % 10 == 0) {
            if (!(getBlockState().getBlock() instanceof CogCrankBlock))
                return;
            AllSoundEvents.CRANKING.playAt(level, worldPosition, (inUse) / 2.5f, .65f + (10 - inUse) / 10f, true);
        }
    }
    
    @Override
    protected boolean canPropagateDiagonally(IRotate block, BlockState state) {
        return state.getBlock() instanceof ICogWheel || block instanceof ICogWheel;
    }

    @Override
    public List<BlockPos> addPropagationLocations(IRotate block, BlockState state, List<BlockPos> neighbours) {
        if (!ICogWheel.isLargeCog(state))
            return super.addPropagationLocations(block, state, neighbours);

        BlockPos.betweenClosedStream(new BlockPos(-1, -1, -1), new BlockPos(1, 1, 1))
                .forEach(offset -> {
                    if (offset.distSqr(BlockPos.ZERO) == 2)
                        neighbours.add(worldPosition.offset(offset));
                });
        return neighbours;
    }
}