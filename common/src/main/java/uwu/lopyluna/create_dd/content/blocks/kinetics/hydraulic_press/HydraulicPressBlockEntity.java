package uwu.lopyluna.create_dd.content.blocks.kinetics.hydraulic_press;

import com.simibubi.create.content.kinetics.belt.transport.TransportedItemStack;
import com.simibubi.create.content.kinetics.press.MechanicalPressBlockEntity;
import com.simibubi.create.content.kinetics.press.PressingBehaviour;
import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.LangBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;
import uwu.lopyluna.create_dd.infrastructure.config.DesiresConfigs;
import uwu.lopyluna.create_dd.registry.DesiresRecipeTypes;

import java.util.List;

import static net.minecraft.ChatFormatting.GRAY;

@MethodsReturnNonnullByDefault
@SuppressWarnings({"removal"})
public class HydraulicPressBlockEntity extends MechanicalPressBlockEntity {

    SmartFluidTankBehaviour tank;

    public HydraulicPressBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        LangBuilder mb = Lang.translate("generic.unit.millibuckets");
        Lang.translate("gui.goggles.fluid_container")
                .forGoggles(tooltip);

        FluidStack fluidStack = tank.getPrimaryHandler().getFluidInTank(0);
        if (!fluidStack.isEmpty()) {
            Lang.fluidName(fluidStack)
                    .style(ChatFormatting.GRAY)
                    .forGoggles(tooltip, 1);

            Lang.builder()
                    .add(Lang.number(fluidStack.getAmount())
                            .add(mb)
                            .style(ChatFormatting.GOLD))
                    .text(ChatFormatting.GRAY, " / ")
                    .add(Lang.number(tank.getPrimaryHandler().getCapacity())
                            .add(mb)
                            .style(ChatFormatting.DARK_GRAY))
                    .forGoggles(tooltip, 1);

        } else if (fluidStack.isEmpty()) {
            Lang.translate("gui.goggles.fluid_container.capacity")
                    .add(Lang.number(tank.getPrimaryHandler().getCapacity())
                            .add(mb)
                            .style(ChatFormatting.GOLD))
                    .style(ChatFormatting.GRAY)
                    .forGoggles(tooltip, 1);
        }

        Lang.translate("tooltip.stressImpact")
                .style(GRAY)
                .forGoggles(tooltip);

        float stressTotal = calculateStressApplied() * Math.abs(getTheoreticalSpeed());

        Lang.number(stressTotal)
                .translate("generic.unit.stress")
                .style(ChatFormatting.AQUA)
                .space()
                .add(Lang.translate("gui.goggles.at_current_speed")
                        .style(ChatFormatting.DARK_GRAY))
                .forGoggles(tooltip, 1);
        return true;
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
        tank = SmartFluidTankBehaviour.single(this, 1000);
        behaviours.add(tank);
    }

    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, Direction side) {
        if (cap == net.minecraftforge.fluids.capability.CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY &&
                side.getAxis() == getBlockState().getValue(HydraulicPressBlock.HORIZONTAL_FACING).getClockWise().getAxis() &&
                side.getAxis() != Direction.Axis.Y)
            return tank.getCapability()
                    .cast();
        return super.getCapability(cap, side);
    }

    @Override
    protected <C extends Container> boolean matchStaticFilters(Recipe<C> recipe) {
        return recipe.getType() == DesiresRecipeTypes.HYDRAULIC_COMPACTING.getType();
    }

    @Override
    protected List<Recipe<?>> getMatchingRecipes() {
        return super.getMatchingRecipes();
    }

    @Override
    public boolean canProcessInBulk() {
        return DesiresConfigs.server().recipes.hydraulicBulkPressing.get();
    }

    @Override
    public void onItemPressed(ItemStack result) {
        super.onItemPressed(result);
        drainFluid();
        if (getProcessFluid(Fluids.LAVA))
            award(AllAdvancements.PRESS);
    }

    @Override
    protected void applyBasinRecipe() {
        drainFluid();
        super.applyBasinRecipe();
    }

    @Override
    protected boolean updateBasin() {
        if (canProcessWithFluid()) {
            return true;
        } else {
            return super.updateBasin();
        }
    }

    @Override
    public void onPressingCompleted() {
        if (pressingBehaviour.onBasin() && matchBasinRecipe(currentRecipe)
                && getBasin().filter(BasinBlockEntity::canContinueProcessing)
                .isPresent())
            startProcessingBasin();
        else
            basinChecker.scheduleUpdate();
    }

    @Override
    public void startProcessingBasin() {
        if (pressingBehaviour.running && pressingBehaviour.runningTicks <= PressingBehaviour.CYCLE / 2)
            return;
        super.startProcessingBasin();
        pressingBehaviour.start(PressingBehaviour.Mode.BASIN);
    }

    protected void drainFluid() {
        if (getProcessFluid(Fluids.LAVA)) {
            tank.getPrimaryHandler().drain(DesiresConfigs.server().recipes.hydraulicLavaDrainPressing.get(), IFluidHandler.FluidAction.EXECUTE);
        } else {
            tank.getPrimaryHandler().drain(DesiresConfigs.server().recipes.hydraulicFluidDrainPressing.get(), IFluidHandler.FluidAction.EXECUTE);
        }
    }

    public boolean getProcessFluid(Fluid fluid) {
        return tank.getPrimaryHandler().getFluid().getFluid() == fluid;
    }

    public boolean canProcessWithFluid() {
        return (tank.isEmpty() || (!getProcessFluid(Fluids.LAVA) && !getProcessFluid(Fluids.WATER))
                || (tank.getPrimaryHandler().getFluidAmount() < 1000));
    }

    @Override
    public boolean tryProcessInBasin(boolean simulate) {
        if (canProcessWithFluid()) {
            return false;
        } else {
            return super.tryProcessInBasin(simulate);
        }
    }

    @Override
    public boolean tryProcessInWorld(ItemEntity itemEntity, boolean simulate) {
        if (canProcessWithFluid()) {
            return false;
        } else {
            return super.tryProcessInWorld(itemEntity, simulate);
        }
    }

    @Override
    public boolean tryProcessOnBelt(TransportedItemStack input, List<ItemStack> outputList, boolean simulate) {
        if (canProcessWithFluid()) {
            return false;
        } else {
            return super.tryProcessOnBelt(input, outputList, simulate);
        }
    }
}
