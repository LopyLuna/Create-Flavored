package uwu.lopyluna.create_dd.content.blocks.kinetics.giant_gear;

import com.simibubi.create.content.equipment.goggles.IProxyHoveringInformation;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.render.MultiPosDestructionHandler;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import uwu.lopyluna.create_dd.registry.DesiresBlocks;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@SuppressWarnings({"deprecation"})
public class GiantGearStructuralBlock extends DirectionalBlock implements IWrenchable, IProxyHoveringInformation {

    public GiantGearStructuralBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder.add(FACING));
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.INVISIBLE;
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState pState) {
        return PushReaction.BLOCK;
    }

    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        return InteractionResult.PASS;
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter pLevel, BlockPos pPos, BlockState pState) {
        return DesiresBlocks.GIANT_GEAR.asStack();
    }

    @Override
    public InteractionResult onSneakWrenched(BlockState state, UseOnContext context) {
        BlockPos clickedPos = context.getClickedPos();
        Level level = context.getLevel();

        if (stillValid(level, clickedPos, state, false)) {
            BlockPos masterPos = getOwner(level, clickedPos, state);
            context = new UseOnContext(level, context.getPlayer(), context.getHand(), context.getItemInHand(),
                    new BlockHitResult(context.getClickLocation(), context.getClickedFace(), masterPos,
                            context.isInside()));
            state = level.getBlockState(masterPos);
        }

        return IWrenchable.super.onSneakWrenched(state, context);
    }
    
    @Override
    public void playerWillDestroy(Level pLevel, BlockPos pPos, BlockState pState, Player pPlayer) {
        destroyParentStructure(pState, pLevel, pPos, pPlayer.isCreative());
        super.playerWillDestroy(pLevel, pPos, pState, pPlayer);
    }
    
    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
//        destroyParentStructure(pState, pLevel, pPos, true);
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }
    
    private void destroyParentStructure(BlockState pState, Level pLevel, BlockPos pPos, boolean drops) {
        BlockPos parentPos = getOwner(pLevel, pPos, pState);
        BlockState parent = pLevel.getBlockState(parentPos);
        if (!parent.is(DesiresBlocks.GIANT_GEAR.get())) return;
        Direction.Axis parentAxis = parent.getValue(GiantGearBlock.AXIS);
        
        GiantGearBlock.destroyStructure(parentPos, parentAxis, pLevel, drops);
    }
    
    @Override
    public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel,
                                  BlockPos pCurrentPos, BlockPos pFacingPos) {
        if (stillValid(pLevel, pCurrentPos, pState, false)) {
            return pState;
        }
        if (!(pLevel instanceof Level level) || level.isClientSide())
            return pState;
        if (!level.getBlockTicks()
                .hasScheduledTick(pCurrentPos, this))
            level.scheduleTick(pCurrentPos, this, 1);
        return pState;
    }

    public static BlockPos getOwner(BlockGetter level, BlockPos pos, BlockState state) {
        Direction direction = state.getValue(FACING);
        BlockPos targetedPos = pos.relative(direction);
        BlockState targetedState = level.getBlockState(targetedPos);
        if (targetedState.is(DesiresBlocks.GIANT_GEAR_STRUCTURAL.get()))
            return getOwner(level, targetedPos, targetedState);
        return targetedPos;
    }

    public boolean stillValid(BlockGetter level, BlockPos pos, BlockState state, boolean directlyAdjacent) {
        if (!state.is(this))
            return false;

        Direction direction = state.getValue(FACING);
        BlockPos targetedPos = pos.relative(direction);
        BlockState targetedState = level.getBlockState(targetedPos);

        if (!directlyAdjacent && stillValid(level, targetedPos, targetedState, true))
            return true;
        return targetedState.getBlock() instanceof GiantGearBlock
                && targetedState.getValue(GiantGearBlock.AXIS) != direction.getAxis();
    }

    @Environment(EnvType.CLIENT)
    public void initializeClient(Consumer<IClientBlockExtensions> consumer) {
        consumer.accept(new GiantGearStructuralBlock.RenderProperties());
    }
    
    private static final SoundType silentSoundType = new SoundType(0F, 1.0F, SoundEvents.STONE_BREAK, SoundEvents.STONE_STEP, SoundEvents.STONE_PLACE, SoundEvents.STONE_HIT, SoundEvents.STONE_FALL);
   
    @Override
    public SoundType getSoundType(BlockState state) {
        return silentSoundType;
    }
    
    // TODO
    // @Override
    // public boolean addLandingEffects(BlockState state1, ServerLevel level, BlockPos pos, BlockState state2,
    //                                  LivingEntity entity, int numberOfParticles) {
    //     return true;
    // }
    
    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        Vec3 min = new Vec3(0, 0, 0);
        Vec3 max = new Vec3(1, 1, 1);
        
        BlockPos ownerPos = getOwner(pLevel, pPos, pState);
        BlockState ownerState = pLevel.getBlockState(ownerPos);
        if (!ownerState.is(DesiresBlocks.GIANT_GEAR.get())) return Shapes.block();
        
        Direction.Axis axis = ownerState.getValue(GiantGearBlock.AXIS);
        
        min = min.with(axis, 3/16f);
        max = max.with(axis, 13/16f);
        
        return Shapes.box(
            min.x, min.y, min.z,
            max.x, max.y, max.z
        );
    }
    
    @Override
    public boolean hasDynamicShape() {
        return true;
    }
    
    public static class RenderProperties implements IClientBlockExtensions, MultiPosDestructionHandler {

        @Override
        public boolean addDestroyEffects(BlockState state, Level Level, BlockPos pos, ParticleEngine manager) {
            return true;
        }

        @Override
        public boolean addHitEffects(BlockState state, Level level, HitResult target, ParticleEngine manager) {
            if (target instanceof BlockHitResult bhr) {
                BlockPos targetPos = bhr.getBlockPos();
                GiantGearStructuralBlock waterWheelStructuralBlock = DesiresBlocks.GIANT_GEAR_STRUCTURAL.get();
                if (waterWheelStructuralBlock.stillValid(level, targetPos, state, false))
                    manager.crack(GiantGearStructuralBlock.getOwner(level, targetPos, state), bhr.getDirection());
                return true;
            }
            return IClientBlockExtensions.super.addHitEffects(state, level, target, manager);
        }

        @Override
        @Nullable
        public Set<BlockPos> getExtraPositions(ClientLevel level, BlockPos pos, BlockState blockState, int progress) {
            GiantGearStructuralBlock waterWheelStructuralBlock = DesiresBlocks.GIANT_GEAR_STRUCTURAL.get();
            if (!waterWheelStructuralBlock.stillValid(level, pos, blockState, false))
                return null;
            HashSet<BlockPos> set = new HashSet<>();
            set.add(GiantGearStructuralBlock.getOwner(level, pos, blockState));
            return set;
        }
    }

    @Override
    public BlockPos getInformationSource(Level level, BlockPos pos, BlockState state) {
        return stillValid(level, pos, state, false) ? getOwner(level, pos, state) : pos;
    }

}
