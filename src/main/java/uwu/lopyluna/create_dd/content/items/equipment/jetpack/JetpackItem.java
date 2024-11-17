package uwu.lopyluna.create_dd.content.items.equipment.jetpack;

import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.equipment.armor.BacktankItem;
import com.simibubi.create.content.equipment.armor.BacktankUtil;
import com.simibubi.create.content.equipment.armor.BaseArmorItem;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Lang;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.protocol.game.ClientboundSetSubtitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitlesAnimationPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import uwu.lopyluna.create_dd.DesireUtil;
import uwu.lopyluna.create_dd.content.items.equipment.MinerArmorMaterial;
import uwu.lopyluna.create_dd.infrastructure.config.DesiresConfigs;

import java.util.List;
import java.util.function.Supplier;

import static net.minecraft.tags.FluidTags.LAVA;
import static uwu.lopyluna.create_dd.DesireUtil.*;

public class JetpackItem extends BaseArmorItem {
    public static final int BAR_COLOR = 0xEFEFEF;
    private final Supplier<JetpackBlockItem> blockItem;

    public JetpackItem(Properties properties, Supplier<JetpackBlockItem> placeable) {
        super(MinerArmorMaterial.MINER, EquipmentSlot.CHEST, properties, DesireUtil.asResource("miner"));
        this.blockItem = placeable;
    }

    @Nullable
    public static JetpackItem getWornBy(Entity entity) {
        if (!(entity instanceof LivingEntity livingEntity)) {
            return null;
        }
        if (!(livingEntity.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof JetpackItem item)) {
            return null;
        }
        return item;
    }
    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext ctx) {
        return blockItem.get()
                .useOn(ctx);
    }

    public Block getBlock() {
        return blockItem.get().getBlock();
    }

    @Override
    public void appendHoverText(@NotNull ItemStack pStack, @Nullable Level pLevel, @NotNull List<Component> pTooltipComponents, @NotNull TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        if (pLevel != null && pLevel.isClientSide()) {
            CompoundTag nbt = pStack.getOrCreateTag();

            String a = getAirVisual(nbt);
            String c = getCooldownVisual(nbt);
            String h = getHeatingVisual(nbt);

            MutableComponent textA = Component.literal(a);
            MutableComponent textC = Component.literal(c);
            MutableComponent textH = Component.literal(h);

            boolean cNA = !c.equals("NA");
            boolean hNA = !h.equals("NA");

            if (cNA)
                pTooltipComponents.add(textC.withStyle(ChatFormatting.GRAY));
            if (hNA)
                pTooltipComponents.add(textH.withStyle(ChatFormatting.GRAY));
            pTooltipComponents.add(textA.withStyle(ChatFormatting.GRAY));
        }
    }

    @Override
    public boolean isBarVisible(@NotNull ItemStack pStack) {
        CompoundTag nbt = pStack.getOrCreateTag();
        return nbt.getInt("Air") != 0 || super.isBarVisible(pStack);
    }

    @Override
    public int getBarWidth(ItemStack pStack) {
        CompoundTag nbt = pStack.getOrCreateTag();
        return nbt.getInt("Air") == 0 ? super.getBarWidth(pStack) : Math.round(13.0F - ((float)getMaxAir() - (float)nbt.getInt("Air")) * 13.0F / (float)getMaxAir());
    }

    @Override
    public int getBarColor(@NotNull ItemStack pStack) {
        CompoundTag nbt = pStack.getOrCreateTag();
        return nbt.getInt("Air") == 0 ? super.getBarColor(pStack) : BAR_COLOR;
    }

    @Override
    public void inventoryTick(@NotNull ItemStack pStack, @NotNull Level pLevel, @NotNull Entity pEntity, int pSlotId, boolean pIsSelected) {
        super.inventoryTick(pStack, pLevel, pEntity, pSlotId, pIsSelected);
        if (pEntity instanceof Player pPlayer) {
            CompoundTag nbt = pStack.getOrCreateTag();

            if (!nbt.contains("Cooldown")) nbt.putInt("Cooldown", 0);
            if (!nbt.contains("Heating")) nbt.putInt("Heating", 0);
            if (!nbt.contains("Cooling")) nbt.putInt("Cooling", 0);
            if (!nbt.contains("Air") && pPlayer.isCreative()) nbt.putInt("Air", getMaxAir());
            if (!nbt.contains("Air") && !pPlayer.isCreative()) nbt.putInt("Air", 0);

            if (nbt.getInt("Cooldown") > getMaxCooldown())
                nbt.putInt("Cooldown", getMaxCooldown());
            if (nbt.getInt("Heating") > getMaxHeating())
                nbt.putInt("Heating", getMaxHeating());
            if (nbt.getInt("Cooling") > getMaxCooling())
                nbt.putInt("Cooling", getMaxCooling());
            if (nbt.getInt("Air") > getMaxAir())
                nbt.putInt("Air", getMaxAir());

            if (!pPlayer.isSpectator() && !pPlayer.isCreative()) {

                if (!(pPlayer.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof BacktankItem))
                    return;
                ItemStack backtankStack = pPlayer.getItemBySlot(EquipmentSlot.CHEST);
                CompoundTag backTankNBT = backtankStack.getOrCreateTag();


                boolean heatFlag = pPlayer.isOnFire() || pLevel.getBiome(pPlayer.blockPosition()).is(BiomeTags.IS_NETHER) || (pPlayer.fireImmune() && pLevel.getFluidState(pPlayer.blockPosition()).is(LAVA));

                if (nbt.getInt("Heating") == getMaxHeating() && nbt.getInt("Cooldown") != getMaxCooldown()) {
                    pLevel.playSound(null, pPlayer.blockPosition(), AllSoundEvents.DENY.getMainEvent(), SoundSource.PLAYERS, .5f, 1.25f);
                    pLevel.playSound(null, pPlayer.blockPosition(), AllSoundEvents.STEAM.getMainEvent(), SoundSource.PLAYERS, 1f, .5f);
                    if (randomChance(50, pLevel)) {
                        pStack.hurtAndBreak(randomChance(25, pLevel) ? 50 : 25, pPlayer, e -> e.broadcastBreakEvent(net.minecraft.world.entity.EquipmentSlot.CHEST));
                    }
                    nbt.putInt("Cooldown", getMaxCooldown());
                }

                if (nbt.getInt("Heating") > 0) {
                    if (nbt.getInt("Cooling") >= getMaxCooling() && backTankNBT.getInt("Air") > 0) {
                        if (backTankNBT.getInt("Air") == BacktankUtil.maxAir(backtankStack) * 0.10) {
                            if (pPlayer instanceof ServerPlayer sp)
                                sendWarning(sp, false);
                            backTankNBT.putInt("Air", backTankNBT.getInt("Air") - 1);
                        } else if (nbt.getInt("Air") == 1) {
                            if (pPlayer instanceof ServerPlayer sp)
                                sendWarning(sp, true);
                            backTankNBT.putInt("Air", backTankNBT.getInt("Air") - 1);
                        } else {
                            backTankNBT.putInt("Air", backTankNBT.getInt("Air") - 1);
                        }

                        nbt.putInt("Cooling", 0);

                        if (nbt.getInt("Heating") > (heatFlag ? 1 : 2)) {
                            nbt.putInt("Heating", nbt.getInt("Heating") - (heatFlag ? 1 : 2));
                        } else {
                            nbt.putInt("Heating", 0);
                        }
                    } else if (nbt.getInt("Cooling") >= getMaxCooling() && randomChance(5, pLevel)) {
                        if (randomChance(25, pLevel)) {
                            pStack.hurtAndBreak(randomChance(50, pLevel) ? 2 : 4, pPlayer, e -> e.broadcastBreakEvent(net.minecraft.world.entity.EquipmentSlot.CHEST));
                        }

                        nbt.putInt("Cooling", 0);

                        if (nbt.getInt("Heating") > (heatFlag ? 1 : 2)) {
                            nbt.putInt("Heating", nbt.getInt("Heating") - (heatFlag ? 1 : 2));
                        } else {
                            nbt.putInt("Heating", 0);
                        }
                    } else if (nbt.getInt("Cooling") < getMaxCooling()) {
                        nbt.putInt("Cooling", nbt.getInt("Cooling") + 1);
                    }
                }

                if (nbt.getInt("Cooldown") > 0) {
                    if (nbt.getInt("Heating") == getMaxHeating()) {
                        pStack.hurtAndBreak(100, pPlayer, e -> e.broadcastBreakEvent(net.minecraft.world.entity.EquipmentSlot.CHEST));
                        nbt.putInt("Heating", nbt.getInt("Heating") - 1);
                    }

                    if (nbt.getInt("Heating") > 0 && backTankNBT.getInt("Air") > 0) {
                        nbt.putInt("Heating", nbt.getInt("Heating") - 1);
                    }
                    nbt.putInt("Cooldown", nbt.getInt("Cooldown") - 1);
                }
            }
        }

    }

    @Override
    public void onArmorTick(ItemStack pStack, Level pLevel, Player pPlayer) {
        super.onArmorTick(pStack, pLevel, pPlayer);
        if (!pPlayer.isSpectator() && !pPlayer.isCreative()) {
            boolean heatFlag = pPlayer.isOnFire() || pLevel.getBiome(pPlayer.blockPosition()).is(BiomeTags.IS_NETHER) || (pPlayer.fireImmune() && pLevel.getFluidState(pPlayer.blockPosition()).is(LAVA));
            CompoundTag nbt = pStack.getOrCreateTag();

            if (nbt.getInt("Heating") == getMaxHeating() && nbt.getInt("Cooldown") != getMaxCooldown()) {
                pLevel.playSound(null, pPlayer.blockPosition(), AllSoundEvents.DENY.getMainEvent(), SoundSource.PLAYERS, .5f, 1.25f);
                pLevel.playSound(null, pPlayer.blockPosition(), AllSoundEvents.STEAM.getMainEvent(), SoundSource.PLAYERS, 1f, .5f);
                if (randomChance(50, pLevel)) {
                    pStack.hurtAndBreak(randomChance(25, pLevel) ? 50 : 25, pPlayer, e -> e.broadcastBreakEvent(net.minecraft.world.entity.EquipmentSlot.CHEST));
                }
                nbt.putInt("Cooldown", getMaxCooldown());
            }

            if (nbt.getInt("Heating") > 0) {
                if (nbt.getInt("Cooling") >= getMaxCooling() && nbt.getInt("Air") > 0) {
                    if (nbt.getInt("Air") == getMaxAir() * 0.10) {
                        if (pPlayer instanceof ServerPlayer sp)
                            sendWarning(sp, false);
                        nbt.putInt("Air", nbt.getInt("Air") - 1);
                    } else if (nbt.getInt("Air") == 1) {
                        if (pPlayer instanceof ServerPlayer sp)
                            sendWarning(sp, true);
                        nbt.putInt("Air", nbt.getInt("Air") - 1);
                    } else {
                        nbt.putInt("Air", nbt.getInt("Air") - 1);
                    }

                    nbt.putInt("Cooling", 0);

                    if (nbt.getInt("Heating") > (heatFlag ? 1 : 2)) {
                        nbt.putInt("Heating", nbt.getInt("Heating") - (heatFlag ? 1 : 2));
                    } else {
                        nbt.putInt("Heating", 0);
                    }
                } else if (nbt.getInt("Cooling") >= getMaxCooling() && randomChance(5, pLevel)) {
                    if (randomChance(25, pLevel)) {
                        pStack.hurtAndBreak(randomChance(50, pLevel) ? 2 : 4, pPlayer, e -> e.broadcastBreakEvent(net.minecraft.world.entity.EquipmentSlot.CHEST));
                    }

                    nbt.putInt("Cooling", 0);

                    if (nbt.getInt("Heating") > (heatFlag ? 1 : 2)) {
                        nbt.putInt("Heating", nbt.getInt("Heating") - (heatFlag ? 1 : 2));
                    } else {
                        nbt.putInt("Heating", 0);
                    }
                } else if (nbt.getInt("Cooling") < getMaxCooling()) {
                    nbt.putInt("Cooling", nbt.getInt("Cooling") + 1);
                }
            }

            if (nbt.getInt("Cooldown") > 0) {
                if (nbt.getInt("Heating") == getMaxHeating()) {
                    pStack.hurtAndBreak(100, pPlayer, e -> e.broadcastBreakEvent(net.minecraft.world.entity.EquipmentSlot.CHEST));
                    nbt.putInt("Heating", nbt.getInt("Heating") - 1);
                }

                if (nbt.getInt("Heating") > 0 && nbt.getInt("Air") > 0) {
                    nbt.putInt("Heating", nbt.getInt("Heating") - 1);
                }
                nbt.putInt("Cooldown", nbt.getInt("Cooldown") - 1);
            }
        }
    }

    public void activateFlying(ItemStack pStack, Player pPlayer, boolean isSpacebarDown) {
        float range = DesiresConfigs.server().equipment.jetpackRange.getF();
        CompoundTag nbt = pStack.getOrCreateTag();
        if (!pPlayer.isPassenger() && !pPlayer.isSpectator() && !pPlayer.isCreative() && !pPlayer.isShiftKeyDown()) {
            boolean heatFlag = pPlayer.isOnFire() || pPlayer.getLevel().getBiome(pPlayer.blockPosition()).is(BiomeTags.IS_NETHER) || (pPlayer.fireImmune() && pPlayer.getLevel().getFluidState(pPlayer.blockPosition()).is(LAVA));
            boolean isAboveBlock = getBlockBelowPlayer(pPlayer, range) != null;
            if (isSpacebarDown && nbt.getInt("Air") > 0 && nbt.getInt("Heating") < getMaxHeating() && nbt.getInt("Cooldown") <= 0) {

                if (isAboveBlock || !DesiresConfigs.server().equipment.jetpackNeedsBlockBelow.get()) {
                    if (!pPlayer.getAbilities().flying) {
                        pPlayer.getAbilities().flying = true;
                        pPlayer.onUpdateAbilities();
                    }
                    nbt.putInt("Heating", nbt.getInt("Heating") + (heatFlag ? 6 : 1));
                    if (randomChance(1, pPlayer.getLevel())) {
                        for (int i = 10; i > 0; i--) {
                            if (randomChance(25, pPlayer.getLevel())) {
                                if (nbt.getInt("Air") == getMaxAir() * 0.10) {
                                    if (pPlayer instanceof ServerPlayer sp)
                                        sendWarning(sp, false);
                                    nbt.putInt("Air", nbt.getInt("Air") - 1);
                                } else if (nbt.getInt("Air") == 1) {
                                    if (pPlayer instanceof ServerPlayer sp)
                                        sendWarning(sp, true);
                                    nbt.putInt("Air", nbt.getInt("Air") - 1);
                                } else {
                                    nbt.putInt("Air", nbt.getInt("Air") - 1);
                                }
                            }
                        }

                        pStack.hurtAndBreak(randomChance(25, pPlayer.getLevel()) ? 2 : 1, pPlayer, e -> e.broadcastBreakEvent(net.minecraft.world.entity.EquipmentSlot.CHEST));
                    }
                    if (nbt.getInt("Cooling") > 0)
                        nbt.putInt("Cooling", nbt.getInt("Cooling") - 1);
                } else {
                    updateFlyingFlag(pPlayer);
                    if (pPlayer.getDeltaMovement().y > -0.25) {
                        nbt.putInt("Heating", nbt.getInt("Heating") + (heatFlag ? 6 : 1));
                        if (nbt.getInt("Cooling") > 0)
                            nbt.putInt("Cooling", nbt.getInt("Cooling") - 1);
                    }
                }
            } else updateFlyingFlag(pPlayer);
        } else updateFlyingFlag(pPlayer);
    }

    private void updateFlyingFlag(Player pPlayer) {
        if (pPlayer.isOnGround() || (pPlayer.getAbilities().flying && !pPlayer.isCreative() && !pPlayer.isSpectator())) {
            pPlayer.getAbilities().flying = false;
            pPlayer.onUpdateAbilities();
        }
    }

    public static Vec3 getBlockBelowPlayer(Player player, double maxRange) {
        Vec3 center = getCenterPos(player);
        Vec3 endPosition = center.subtract(0, maxRange, 0);

        BlockHitResult hitResult = player.level.clip(new ClipContext(
                center,
                endPosition,
                ClipContext.Block.OUTLINE,
                ClipContext.Fluid.NONE,
                player));

        if (hitResult.getType() == HitResult.Type.BLOCK) {
            return hitResult.getLocation();
        }
        return null;
    }

    public static Vec3 getCenterPos(Entity entity) {
        AABB boundingBox = entity.getBoundingBox();

        double centerX = (boundingBox.minX + boundingBox.maxX) / 2.0;
        double centerY = (boundingBox.minY + boundingBox.maxY) / 2.0;
        double centerZ = (boundingBox.minZ + boundingBox.maxZ) / 2.0;

        return new Vec3(centerX, centerY, centerZ);
    }

    public int getMaxCooldown() { return DesiresConfigs.server().equipment.jetpackCooldownMaxTick.get(); }
    public int getMaxHeating() { return DesiresConfigs.server().equipment.jetpackHeatingMaxTick.get(); }
    public int getMaxCooling() { return DesiresConfigs.server().equipment.jetpackCoolingMaxTick.get(); }
    public int getMaxAir() { return DesiresConfigs.server().equipment.jetpackAirMaxTick.get(); }

    public String getCooldownVisual(CompoundTag nbt) {
        return nbt.getInt("Cooldown") == 0 ? "NA" : "Cooldown: " + valueToTime(nbt.getInt("Cooldown"), OffsetTime.SECONDS);
    }
    public String getHeatingVisual(CompoundTag nbt) {
        return nbt.getInt("Heating") == 0 ? "NA" : "Heat: " + percentString(nbt.getInt("Heating"), getMaxHeating(), false);
    }
    public String getAirVisual(CompoundTag nbt) {
        return "Air: " + percentString(nbt.getInt("Air"), getMaxAir(), false);
    }
    @SuppressWarnings("unused")
    public String getEstimatedFlightTimeVisual(CompoundTag nbt) {
        return "Estimated Flight Time: " + valueToTime((getMaxHeating() - nbt.getInt("Heating")) + 20, OffsetTime.SECONDS);
    }


    @SuppressWarnings("all")
    private static void sendWarning(ServerPlayer player, boolean depleted) {

        MutableComponent component = Lang.translateDirect(depleted ? "backtank.depleted" : "backtank.low");

        AllSoundEvents.DENY.play(player.level, null, player.blockPosition(), 1, 1.25f);
        AllSoundEvents.STEAM.play(player.level, null, player.blockPosition(), .5f, .5f);

        player.connection.send(new ClientboundSetTitlesAnimationPacket(10, 40, 10));
        player.connection.send(new ClientboundSetSubtitleTextPacket(
                Components.literal("\u26A0 ").withStyle(depleted ? ChatFormatting.RED : ChatFormatting.GOLD)
                        .append(component.withStyle(ChatFormatting.GRAY))));
        player.connection.send(new ClientboundSetTitleTextPacket(Components.immutableEmpty()));
    }

    public static class JetpackBlockItem extends BlockItem {
        private final Supplier<? extends JetpackItem> actualItem;

        public JetpackBlockItem(Block block, Supplier<? extends JetpackItem> actualItem, Properties properties) {
            super(block, properties);
            this.actualItem = actualItem;
        }

        @Override
        public @NotNull JetpackItem asItem() {
            return actualItem.get();
        }

        @Override
        public @NotNull ItemStack getDefaultInstance() {
            return new ItemStack(asItem());
        }

        @Override
        public void fillItemCategory(@NotNull CreativeModeTab group, @NotNull NonNullList<ItemStack> items) {}

        @Override
        public @NotNull String getDescriptionId() {
            return this.getOrCreateDescriptionId();
        }
    }
}
