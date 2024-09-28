package uwu.lopyluna.create_dd.content.blocks.kinetics.multimeter;

import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.utility.Color;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.LangBuilder;
import com.simibubi.create.infrastructure.config.AllConfigs;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import uwu.lopyluna.create_dd.registry.DesiresPackets;

import java.util.List;

@SuppressWarnings({"deprecation"})
public class MultiMeterBlockEntity extends KineticBlockEntity implements IHaveGoggleInformation {

    public float dialTarget;
    public int color;

    static BlockPos lastSent;

    public MultiMeterBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }
    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
        registerAwardables(behaviours, AllAdvancements.STRESSOMETER, AllAdvancements.STRESSOMETER_MAXED);
    }

    @Override
    public void updateFromNetwork(float maxStress, float currentStress, int networkSize) {
        super.updateFromNetwork(maxStress, currentStress, networkSize);

        if (!IRotate.StressImpact.isEnabled())
            dialTarget = 0;
        else if (isOverStressed())
            dialTarget = 1.125f;
        else if (maxStress == 0)
            dialTarget = 0;
        else
            dialTarget = currentStress / maxStress;

        if (dialTarget > 0) {
            if (dialTarget < .5f)
                color = Color.mixColors(0x00FF00, 0xFFFF00, dialTarget * 2);
            else if (dialTarget < 1)
                color = Color.mixColors(0xFFFF00, 0xFF0000, (dialTarget) * 2 - 1);
            else
                color = 0xFF0000;
        }

        sendData();
        setChanged();
    }

    @Override
    public void onSpeedChanged(float prevSpeed) {
        super.onSpeedChanged(prevSpeed);
        float speed = Math.abs(getSpeed());

        dialTarget = getDialTarget(speed);
        color = Color.mixColors(IRotate.SpeedLevel.of(speed)
                .getColor(), 0xffffff, .25f);

        if (getSpeed() == 0) {
            dialTarget = 0;
            setChanged();
            return;
        }

        setChanged();
        updateFromNetwork(capacity, stress, getOrCreateNetwork().getSize());
    }

    public static float getDialTarget(float speed) {
        speed = Math.abs(speed);
        float medium = AllConfigs.server().kinetics.mediumSpeed.get()
                .floatValue();
        float fast = AllConfigs.server().kinetics.fastSpeed.get()
                .floatValue();
        float max = AllConfigs.server().kinetics.maxRotationSpeed.get()
                .floatValue();
        float target;
        if (speed == 0)
            target = 0;
        else if (speed < medium)
            target = Mth.lerp(speed / medium, 0, .45f);
        else if (speed < fast)
            target = Mth.lerp((speed - medium) / (fast - medium), .45f, .75f);
        else
            target = Mth.lerp((speed - fast) / (max - fast), .75f, 1.125f);
        return target;
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        compound.putFloat("Value", dialTarget);
        compound.putInt("Color", color);
        super.write(compound, clientPacket);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        dialTarget = compound.getFloat("Value");
        color = compound.getInt("Color");
        super.read(compound, clientPacket);

        if (clientPacket && worldPosition.equals(lastSent))
            lastSent = null;
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        if (!IRotate.StressImpact.isEnabled())
            return false;

        tooltip.add(componentSpacing.plainCopy().append(Lang.translateDirect("gui.gauge.info_header")));

        Lang.translate("gui.speedometer.title")
                .style(ChatFormatting.GRAY)
                .forGoggles(tooltip);
        IRotate.SpeedLevel.getFormattedSpeedText(speed, isOverStressed())
                .forGoggles(tooltip);


        double capacity = getNetworkCapacity();
        double stressFraction = getNetworkStress() / (capacity == 0 ? 1 : capacity);

        Lang.translate("gui.stressometer.title")
                .style(ChatFormatting.GRAY)
                .forGoggles(tooltip);

        if (getTheoreticalSpeed() == 0)
            Lang.text(TooltipHelper.makeProgressBar(3, 0))
                    .translate("gui.stressometer.no_rotation")
                    .style(ChatFormatting.DARK_GRAY)
                    .forGoggles(tooltip);
        else {
            IRotate.StressImpact.getFormattedStressText(stressFraction)
                    .forGoggles(tooltip);
            Lang.translate("gui.stressometer.capacity")
                    .style(ChatFormatting.GRAY)
                    .forGoggles(tooltip);

            double remainingCapacity = capacity - getNetworkStress();

            LangBuilder su = Lang.translate("generic.unit.stress");
            LangBuilder stressTip = Lang.number(remainingCapacity)
                    .add(su)
                    .style(IRotate.StressImpact.of(stressFraction)
                            .getRelativeColor());

            if (remainingCapacity != capacity)
                stressTip.text(ChatFormatting.GRAY, " / ")
                        .add(Lang.number(capacity)
                                .add(su)
                                .style(ChatFormatting.DARK_GRAY));

            stressTip.forGoggles(tooltip, 1);
        }

        if (!worldPosition.equals(lastSent))
            DesiresPackets.getChannel().sendToServer(new GaugeObservedPacket(lastSent = worldPosition));

        return true;
    }


    public float getNetworkStress() {
        return stress;
    }

    public float getNetworkCapacity() {
        return capacity;
    }

    //UNUSED
    public void onObserved() {
        award(AllAdvancements.STRESSOMETER);
        if (Mth.equal(dialTarget, 1))
            award(AllAdvancements.STRESSOMETER_MAXED);
    }
}
