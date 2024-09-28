package uwu.lopyluna.create_dd.content.blocks.contraptions.contraption_block;

import com.simibubi.create.content.contraptions.AssemblyException;
import com.simibubi.create.content.contraptions.IDisplayAssemblyExceptions;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.Objects;

@SuppressWarnings({"unused"})
public class HelmBlockEntity extends SmartBlockEntity implements IDisplayAssemblyExceptions {

    boolean assembleNextTick;
    boolean disassembleNextTick;
    protected AssemblyException lastException;

    public HelmBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        lastException = null;
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
    }

    @Override
    protected void read(CompoundTag tag, boolean clientPacket) {
        lastException = AssemblyException.read(tag);
        super.read(tag, clientPacket);
    }

    @Override
    protected void write(CompoundTag tag, boolean clientPacket) {
        AssemblyException.write(tag, lastException);
        super.write(tag, clientPacket);
    }

    @Override
    public void tick() {
        super.tick();

        if (Objects.requireNonNull(getLevel()).isClientSide)
            return;

        if (assembleNextTick) {
            tryAssemble(true);
            assembleNextTick = false;
            getBlockState().setValue(HelmBlock.ASSEMBLING, false);
        } 
        if (disassembleNextTick) {
            tryAssemble(false);
            disassembleNextTick = false;
            getBlockState().setValue(HelmBlock.DISASSEMBLING, false);
        }
    }




    private void tryAssemble(boolean assemble) {
        if (assemble) {
            assemble(getLevel(), getBlockPos());
        } else {
            if (shouldntAssemble())
                return;
            disassemble(getLevel());
        }


    }

    protected void assemble(Level world, BlockPos pos) {
        BoatContraption contraption = new BoatContraption();
        try {
            if (contraption.assemble(world, pos))
                return;
            lastException = null;
            sendData();
        } catch (AssemblyException e) {
            lastException = e;
            sendData();
            return;
        }

        contraption.removeBlocksFromWorld(world, BlockPos.ZERO);
        contraption.startMoving(world);
        contraption.expandBoundsAroundAxis(Direction.Axis.Y);

        BoatContraptionEntity entity = BoatContraptionEntity.create(world, contraption);
        entity.setPos(pos.getX() + .5, pos.getY(), pos.getZ() + .5);
        world.addFreshEntity(entity);

        if (contraption.containsBlockBreakers())
            award(AllAdvancements.CONTRAPTION_ACTORS);
    }
    protected void disassemble(Level world) {
        //BoatContraption contraption = new BoatContraption();*
        //BoatContraptionEntity entity = BoatContraptionEntity.create(world, contraption);*
        //entity.disassemble();*
    }

    private boolean shouldntAssemble() {
        BlockState blockState = getBlockState();
        return !(blockState.getBlock() instanceof HelmBlock);
    }




    @Override
    public AssemblyException getLastAssemblyException() {
        return lastException;
    }
}
