package uwu.lopyluna.create_dd.content.blocks.functional.sliding_door;

import com.simibubi.create.content.decoration.slidingDoor.SlidingDoorBlock;
import com.simibubi.create.content.decoration.slidingDoor.SlidingDoorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import uwu.lopyluna.create_dd.registry.DesiresWoodType;

import javax.annotation.Nullable;

public class WoodenSlidingDoorBlock extends SlidingDoorBlock {
    public WoodenSlidingDoorBlock(Properties p_52737_, boolean folds) {
        super(p_52737_, folds);
    }

    @Override
    public void setOpen(@Nullable Entity entity, Level level, BlockState state, BlockPos pos, boolean open) {
        if (!state.is(this))
            return;
        if (state.getValue(OPEN) == open)
            return;
        BlockState changedState = state.setValue(OPEN, open);
        if (open)
            changedState = changedState.setValue(VISIBLE, false);
        level.setBlock(pos, changedState, 10);

        DoorHingeSide hinge = changedState.getValue(HINGE);
        Direction facing = changedState.getValue(FACING);
        BlockPos otherPos =
                pos.relative(hinge == DoorHingeSide.LEFT ? facing.getClockWise() : facing.getCounterClockWise());
        BlockState otherDoor = level.getBlockState(otherPos);
        if (isDoubleDoor(changedState, hinge, facing, otherDoor))
            setOpen(entity, level, otherDoor, otherPos, open);

        playSound(level, pos, open);
        level.gameEvent(entity, open ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, pos);
    }

    @Override
    public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pBlock, BlockPos pFromPos,
                                boolean pIsMoving) {
        boolean lower = pState.getValue(HALF) == DoubleBlockHalf.LOWER;
        boolean isPowered = isDoorPowered(pLevel, pPos, pState);
        if (defaultBlockState().is(pBlock))
            return;
        if (isPowered == pState.getValue(POWERED))
            return;

        WoodenSlidingDoorBlockEntity be = (WoodenSlidingDoorBlockEntity) getBlockEntity(pLevel, lower ? pPos : pPos.below());
        if (be != null && be.deferUpdate)
            return;

        BlockState changedState = pState.setValue(POWERED, isPowered)
                .setValue(OPEN, isPowered);
        if (isPowered)
            changedState = changedState.setValue(VISIBLE, false);

        if (isPowered != pState.getValue(OPEN)) {
            playSound(pLevel, pPos, isPowered);
            pLevel.gameEvent(null, isPowered ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, pPos);

            DoorHingeSide hinge = changedState.getValue(HINGE);
            Direction facing = changedState.getValue(FACING);
            BlockPos otherPos =
                    pPos.relative(hinge == DoorHingeSide.LEFT ? facing.getClockWise() : facing.getCounterClockWise());
            BlockState otherDoor = pLevel.getBlockState(otherPos);

            if (isDoubleDoor(changedState, hinge, facing, otherDoor)) {
                otherDoor = otherDoor.setValue(POWERED, isPowered)
                        .setValue(OPEN, isPowered);
                if (isPowered)
                    otherDoor = otherDoor.setValue(VISIBLE, false);
                pLevel.setBlock(otherPos, otherDoor, 2);
            }

            pLevel.setBlock(pPos, changedState, 2);
        } else {
            super.neighborChanged(pState, pLevel, pPos, pBlock, pFromPos, pIsMoving);
        }
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand,
                                 BlockHitResult pHit) {

        pState = pState.cycle(OPEN);
        if (pState.getValue(OPEN))
            pState = pState.setValue(VISIBLE, false);
        pLevel.setBlock(pPos, pState, 10);
        pLevel.gameEvent(pPlayer, isOpen(pState) ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, pPos);

        DoorHingeSide hinge = pState.getValue(HINGE);
        Direction facing = pState.getValue(FACING);
        BlockPos otherPos =
                pPos.relative(hinge == DoorHingeSide.LEFT ? facing.getClockWise() : facing.getCounterClockWise());
        BlockState otherDoor = pLevel.getBlockState(otherPos);
        if (isDoubleDoor(pState, hinge, facing, otherDoor))
            use(otherDoor, pLevel, otherPos, pPlayer, pHand, pHit);
        else if (pState.getValue(OPEN))
            pLevel.levelEvent(pPlayer, getOpenSound(), pPos, 0);

        return InteractionResult.sidedSuccess(pLevel.isClientSide);
    }

    private void playSound(Level pLevel, BlockPos pPos, boolean pIsOpening) {
        if (pIsOpening) {
            pLevel.levelEvent(null, getOpenSound(), pPos, 0);
        }
    }

    private int getOpenSound() {
        return 1006;
    }

    @Override
    public BlockEntityType<? extends SlidingDoorBlockEntity> getBlockEntityType() {
        return DesiresWoodType.SLIDING_DOOR.get();
    }
}
