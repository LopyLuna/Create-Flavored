package uwu.lopyluna.create_dd.content.blocks.logistics.burden_chute;

import com.simibubi.create.content.logistics.chute.ChuteBlock;
import com.simibubi.create.content.logistics.chute.ChuteBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import uwu.lopyluna.create_dd.registry.DesiresBlockEntityTypes;
import uwu.lopyluna.create_dd.registry.DesiresBlocks;

public class BurdenChuteBlock extends ChuteBlock {
    public BurdenChuteBlock(Properties p_i48440_1_) {
        super(p_i48440_1_);
    }

    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        Shape shape = state.getValue(SHAPE);
        boolean down = state.getValue(FACING) == Direction.DOWN;
        if (shape == Shape.INTERSECTION)
            return InteractionResult.PASS;
        Level level = context.getLevel();
        if (level.isClientSide)
            return InteractionResult.SUCCESS;
        if (shape == Shape.ENCASED) {
            level.setBlockAndUpdate(context.getClickedPos(), state.setValue(SHAPE, Shape.NORMAL));
            level.levelEvent(2001, context.getClickedPos(),
                    Block.getId(DesiresBlocks.OVERBURDEN_CASING.getDefaultState()));
            return InteractionResult.SUCCESS;
        }
        if (down)
            level.setBlockAndUpdate(context.getClickedPos(),
                    state.setValue(SHAPE, shape != Shape.NORMAL ? Shape.NORMAL : Shape.WINDOW));
        return InteractionResult.SUCCESS;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand,
                                 BlockHitResult hitResult) {
        Shape shape = state.getValue(SHAPE);
        if (!DesiresBlocks.OVERBURDEN_CASING.isIn(player.getItemInHand(hand)))
            return super.use(state, level, pos, player, hand, hitResult);
        if (shape == Shape.INTERSECTION || shape == Shape.ENCASED)
            return super.use(state, level, pos, player, hand, hitResult);
        if (level.isClientSide)
            return InteractionResult.SUCCESS;

        level.setBlockAndUpdate(pos, state.setValue(SHAPE, Shape.ENCASED));
        level.playSound(null, pos, SoundEvents.NETHERITE_BLOCK_HIT, SoundSource.BLOCKS, 0.5f, 1.05f);
        return InteractionResult.SUCCESS;
    }


    @Override
    public BlockEntityType<? extends ChuteBlockEntity> getBlockEntityType() {
        return DesiresBlockEntityTypes.BURDEN_CHUTE.get();
    }
}
