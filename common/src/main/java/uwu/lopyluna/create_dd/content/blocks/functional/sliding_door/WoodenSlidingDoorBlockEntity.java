package uwu.lopyluna.create_dd.content.blocks.functional.sliding_door;

import com.simibubi.create.content.decoration.slidingDoor.SlidingDoorBlock;
import com.simibubi.create.content.decoration.slidingDoor.SlidingDoorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Objects;

public class WoodenSlidingDoorBlockEntity extends SlidingDoorBlockEntity {

    boolean deferUpdate;

    public WoodenSlidingDoorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void tick() {
        if (deferUpdate && !Objects.requireNonNull(getLevel()).isClientSide()) {
            deferUpdate = false;
        }
        super.tick();
    }

    @Override
    protected void showBlockModel() {
        Objects.requireNonNull(getLevel()).setBlock(worldPosition, getBlockState().setValue(SlidingDoorBlock.VISIBLE, true), 3);
        getLevel().playSound(null, worldPosition, SoundEvents.WOODEN_DOOR_CLOSE, SoundSource.BLOCKS, .5f, 0.9f);
    }
}
