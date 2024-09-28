package uwu.lopyluna.create_dd.content.blocks.kinetics.furnace_engine;

import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.contraptions.bearing.WindmillBearingBlockEntity;
import com.simibubi.create.content.kinetics.base.GeneratingKineticBlockEntity;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.content.kinetics.steamEngine.SteamEngineBlock;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollOptionBehaviour;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.VecHelper;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import uwu.lopyluna.create_dd.registry.DesiresBlocks;

import javax.annotation.Nullable;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Objects;

@SuppressWarnings({"unchecked", "unused", "rawtypes"})
public class FurnaceEngineBlockEntity extends SmartBlockEntity {
    protected ScrollOptionBehaviour<WindmillBearingBlockEntity.RotationDirection> movementDirection;
    public WeakReference<PoweredFlywheelBlockEntity> target = new WeakReference(null);
    public WeakReference<AbstractFurnaceBlockEntity> source = new WeakReference(null);
    float prevAngle = 0.0F;
    int delayedTimer;

    public FurnaceEngineBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        this.movementDirection = new ScrollOptionBehaviour<>(WindmillBearingBlockEntity.RotationDirection.class, Lang.translateDirect("contraptions.windmill.rotation_direction"), this, new FurnaceEngineValueBox());
        this.movementDirection.onlyActiveWhen(() -> {
            PoweredFlywheelBlockEntity flywheel = this.getFlywheel();
            return flywheel == null || !flywheel.hasSource();
        });
        this.movementDirection.withCallback(($) -> this.onDirectionChanged());
        behaviours.add(this.movementDirection);
    }

    @Override
    public void tick() {
        super.tick();
        //this might help with the fps drop
        if (Objects.requireNonNull(getLevel()).isClientSide || (getLevel().getGameTime() % 10 != 0)) {
            return;
        }

        PoweredFlywheelBlockEntity flywheel = this.getFlywheel();
        AbstractFurnaceBlockEntity furnace = this.getFurnace();

        if (furnace != null && flywheel != null) {
            float efficiency = furnace.cookingProgress > 0 && furnace.cookingTotalTime > 0 ? 1.0f : 0.0f;
            if (efficiency > 0.0F && delayedTimer < 5) {
                delayedTimer++;
            } else if (delayedTimer > 0) {
                delayedTimer--;
            }
            boolean verticalTarget;
            BlockState flywheelState = flywheel.getBlockState();
            Direction.Axis targetAxis = Direction.Axis.X;
            Block var7 = flywheelState.getBlock();
            if (var7 instanceof IRotate ir) {
                targetAxis = ir.getRotationAxis(flywheelState);
            }

            verticalTarget = targetAxis == Direction.Axis.Y;
            BlockState blockState = this.getBlockState();
            if (DesiresBlocks.FURNACE_ENGINE.has(blockState)) {
                Direction facing = SteamEngineBlock.getFacing(blockState);
                if (facing.getAxis() == Direction.Axis.Y) {
                    facing = blockState.getValue(SteamEngineBlock.FACING);
                }

                float delayedEfficiency = delayedTimer > 1 ? 1.0f : 0.0f;

                if (delayedEfficiency > 0.0F) {
                    this.award(AllAdvancements.STEAM_ENGINE);
                }
                int conveyedSpeedLevel = delayedEfficiency == 0.0F ? 1 : (verticalTarget ? 1 : (int) GeneratingKineticBlockEntity.convertToDirection(1.0F, facing));
                if (targetAxis == Direction.Axis.Z) {
                    conveyedSpeedLevel *= -1;
                }

                if (this.movementDirection.get() == WindmillBearingBlockEntity.RotationDirection.COUNTER_CLOCKWISE) {
                    conveyedSpeedLevel *= -1;
                }

                float flywheelSpeed = flywheel.getTheoreticalSpeed();
                if (flywheel.hasSource() && flywheelSpeed != 0.0F && conveyedSpeedLevel != 0 && flywheelSpeed > 0.0F != conveyedSpeedLevel > 0) {
                    this.movementDirection.setValue(1 - this.movementDirection.get().ordinal());
                    conveyedSpeedLevel *= -1;
                }

                flywheel.update(this.worldPosition, conveyedSpeedLevel, delayedEfficiency);
                assert this.level != null;
                if (this.level.isClientSide) {
                    this.spawnParticles();
                }
            }
        } else {
            assert this.level != null;
            if (!this.level.isClientSide()) {
                if (flywheel != null) {
                    if (flywheel.getBlockPos().subtract(this.worldPosition).equals(flywheel.enginePos)) {
                        if (flywheel.engineEfficiency != 0.0F) {
                            Direction facing = SteamEngineBlock.getFacing(this.getBlockState());
                            if (this.level.isLoaded(this.worldPosition.relative(facing.getOpposite()))) {
                                flywheel.update(this.worldPosition, 0, 0.0F);
                            }

                        }
                    }
                }
            }
        }

    }

    @Environment(EnvType.CLIENT)
    private void spawnParticles() {
        Float targetAngle = this.getTargetAngle();
        PoweredFlywheelBlockEntity ste = this.target.get();
        if (ste != null) {
            if (ste.isPoweredBy(this.worldPosition)) {
                if (targetAngle != null) {
                    float angle = AngleHelper.deg((double) targetAngle);
                    angle += angle < 0.0F ? -105.0F : 285.0F;
                    angle %= 360.0F;
                    PoweredFlywheelBlockEntity flywheel = this.getFlywheel();
                    if (flywheel != null && flywheel.getSpeed() != 0.0F) {
                        if (!(angle >= 0.0F) || this.prevAngle > 180.0F && angle < 180.0F) {
                            if (angle < 0.0F && (!(this.prevAngle < -180.0F) || !(angle > -180.0F))) {
                                this.prevAngle = angle;
                            } else {
                                AbstractFurnaceBlockEntity sourceBE = this.source.get();
                                if (sourceBE != null) {
                                    float volume = 3.0F / 2;
                                    assert this.level != null;
                                    float pitch = 0.28F + this.level.random.nextFloat() * 0.1F;
                                    this.level.playLocalSound(this.worldPosition.getX(), this.worldPosition.getY(), this.worldPosition.getZ(), SoundEvents.CANDLE_EXTINGUISH, SoundSource.BLOCKS, volume, pitch, false);
                                    AllSoundEvents.STEAM.playAt(this.level, this.worldPosition, volume / 16.0F, 0.25F, false);
                                }

                                Direction facing = FurnaceEngineBlock.getFacing(this.getBlockState());
                                assert this.level != null;
                                Vec3 offset = VecHelper.rotate((new Vec3(0.0, 0.0, 1.0)).add(VecHelper.offsetRandomly(Vec3.ZERO, this.level.random, 1.0F).multiply(1.0, 1.0, 0.0).normalize().scale(0.5)), AngleHelper.verticalAngle(facing), Direction.Axis.X);
                                offset = VecHelper.rotate(offset, AngleHelper.horizontalAngle(facing), Direction.Axis.Y);
                                Vec3 v = offset.scale(0.5).add(Vec3.atCenterOf(this.worldPosition));
                                Vec3 m = offset.subtract(Vec3.atLowerCornerOf(facing.getNormal()).scale(0.75));
                                this.level.addParticle(new SmokeJetParticleData(0.5F), v.x, v.y, v.z, m.x, m.y, m.z);
                                this.prevAngle = angle;
                            }
                        } else {
                            this.prevAngle = angle;
                        }
                    }
                }
            }
        }
    }

    private void onDirectionChanged() {}

    public PoweredFlywheelBlockEntity getFlywheel() {
        PoweredFlywheelBlockEntity flywheel = this.target.get();
        if (flywheel == null || flywheel.isRemoved() || !flywheel.canBePoweredBy(this.worldPosition)) {
            if (flywheel != null) {
                this.target = new WeakReference(null);
            }

            Direction facing = FurnaceEngineBlock.getFacing(this.getBlockState());
            assert this.level != null;
            BlockEntity anyShaftAt = this.level.getBlockEntity(this.worldPosition.relative(facing, 2));
            if (anyShaftAt instanceof PoweredFlywheelBlockEntity ps) {
                if (ps.canBePoweredBy(this.worldPosition)) {
                    flywheel = ps;
                    this.target = new WeakReference(ps);
                }
            }
        }

        return flywheel;
    }

    public AbstractFurnaceBlockEntity getFurnace() {
        AbstractFurnaceBlockEntity furnace = this.source.get();
        if (furnace == null || furnace.isRemoved()) {
            if (furnace != null) {
                this.source = new WeakReference(null);
            }

            Direction facing = FurnaceEngineBlock.getFacing(this.getBlockState());
            assert this.level != null;
            BlockEntity be = this.level.getBlockEntity(this.worldPosition.relative(facing.getOpposite()));
            if (be instanceof AbstractFurnaceBlockEntity furnaceBe) {
                furnace = furnaceBe;
                this.source = new WeakReference(furnaceBe);
            }
        }

        return furnace;
    }

    @Nullable
    @Environment(EnvType.CLIENT)
    public Float getTargetAngle() {
        float angle;
        BlockState blockState = this.getBlockState();
        if (!DesiresBlocks.FURNACE_ENGINE.has(blockState)) {
            return null;
        } else {
            Direction facing = SteamEngineBlock.getFacing(blockState);
            PoweredFlywheelBlockEntity flywheel = this.getFlywheel();
            Direction.Axis facingAxis = facing.getAxis();
            Direction.Axis axis;
            if (flywheel == null) {
                return null;
            } else {
                axis = KineticBlockEntityRenderer.getRotationAxisOf(flywheel);
                angle = KineticBlockEntityRenderer.getAngleForTe(flywheel, flywheel.getBlockPos(), axis);
                if (axis == facingAxis) {
                    return null;
                } else {
                    if (axis.isHorizontal() && facingAxis == Direction.Axis.X ^ facing.getAxisDirection() == Direction.AxisDirection.POSITIVE) {
                        angle *= -1.0F;
                    }

                    if (axis == Direction.Axis.X && facing == Direction.DOWN) {
                        angle *= -1.0F;
                    }

                    return angle;
                }
            }
        }
    }

    @Override
    public void remove() {
        PoweredFlywheelBlockEntity flywheel = getFlywheel();
        if (flywheel != null)
            flywheel.remove(worldPosition);
        super.remove();
    }
}
