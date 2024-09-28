package uwu.lopyluna.create_dd.content.items.equipment.block_zapper;

import com.simibubi.create.*;
import com.simibubi.create.content.equipment.zapper.PlacementPatterns;
import com.simibubi.create.content.equipment.zapper.ShootableGadgetItemMethods;
import com.simibubi.create.content.equipment.zapper.ZapperBeamPacket;
import com.simibubi.create.content.equipment.zapper.ZapperItem;
import com.simibubi.create.foundation.gui.ScreenOpener;
import com.simibubi.create.foundation.item.render.SimpleCustomRenderer;
import com.simibubi.create.foundation.utility.BlockHelper;
import com.simibubi.create.foundation.utility.NBTHelper;
import com.simibubi.create.foundation.utility.Lang;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.fml.DistExecutor;
import org.jetbrains.annotations.NotNull;
import uwu.lopyluna.create_dd.content.items.equipment.block_zapper.additional.*;
import uwu.lopyluna.create_dd.infrastructure.config.DesiresConfigs;
import uwu.lopyluna.create_dd.registry.DesiresItems;
import uwu.lopyluna.create_dd.registry.DesiresSoundEvents;
import uwu.lopyluna.create_dd.registry.DesiresTags;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.simibubi.create.foundation.item.TooltipHelper.styleFromColor;
import static com.simibubi.create.foundation.utility.BlockHelper.getRequiredItem;
import static net.minecraft.client.gui.screens.Screen.hasControlDown;
import static net.minecraft.client.gui.screens.Screen.hasShiftDown;

public class BlockZapperItem extends ZapperItem implements Vanishable {

    public BlockZapperItem(Properties properties) {
        super(properties.rarity(Rarity.UNCOMMON));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        if (stack.hasTag()) {
            assert stack.getTag() != null;
            CompoundTag nbt = stack.getOrCreateTag();
            if (!hasShiftDown()) {
                tooltip.add(holdCtrl());
            }
            if (!hasControlDown() && !(stack.getTag().contains("BreakerModifier") && nbt.getBoolean("BreakerModifier")) && !hasShiftDown() && !DesiresConfigs.client().disableBlocksVoidZapperMessage.get()) {
                tooltip.add(Component.translatable("create_dd.block_zapper.void_notice").withStyle(styleFromColor(0xFF5454)));
                tooltip.add(Component.translatable("create_dd.block_zapper.breaker_notice").withStyle(styleFromColor(0xB57A4C)));
            }
            if (hasControlDown() && !hasShiftDown()) {
                if ((stack.getTag().contains("BreakerModifier") && nbt.getBoolean("BreakerModifier")) || (stack.getTag().contains("SizeModifier") && nbt.getBoolean("SizeModifier")) || (stack.getTag().contains("RangeModifier") && nbt.getInt("RangeModifier") > 0) || (stack.getTag().contains("SpeedModifier") && nbt.getInt("SpeedModifier") > 0)) {
                    tooltip.add(Component.translatable("create_dd.block_zapper.modifiers").withStyle(ChatFormatting.GRAY));
                } else {
                    tooltip.add(Component.translatable("create_dd.block_zapper.no_modifiers").withStyle(ChatFormatting.GRAY));
                }
                if (stack.getTag().contains("BreakerModifier") && nbt.getBoolean("BreakerModifier")) {
                    tooltip.add(Component.translatable("create_dd.block_zapper.breaker_modifier").withStyle(styleFromColor(0xFFCF54)));
                    tooltip.add(Component.translatable("create_dd.block_zapper.breaker_modifier.desc").withStyle(styleFromColor(0xB57A4C)));
                }
                if (stack.getTag().contains("SizeModifier") && nbt.getBoolean("SizeModifier")) {
                    tooltip.add(Component.translatable("create_dd.block_zapper.size_modifier").withStyle(styleFromColor(0xFFCF54)));
                    tooltip.add(Component.translatable("create_dd.block_zapper.size_modifier.desc").withStyle(styleFromColor(0xB57A4C)));
                }
                if (stack.getTag().contains("RangeModifier") && nbt.getInt("RangeModifier") > 0) {
                    int r = getRangeModifier(nbt);
                    int range = r == 1 ? 16 : r == 2 ? 24 : r == 3 ? 32 : 8;
                    tooltip.add(Component.translatable("create_dd.block_zapper.range_modifier").append(r == 3 ? " III" : r == 2 ? " II" : r == 1 ? " I" : "").withStyle(r == 3 ? styleFromColor(0xFFCF54) : styleFromColor(0xF1DD79)));
                    tooltip.add(Component.translatable("create_dd.block_zapper.range_modifier.desc").append(String.valueOf(range)).append(Component.translatable("create_dd.block_zapper.range_modifier.desc2")).withStyle(styleFromColor(0xB57A4C)));
                }
                if (stack.getTag().contains("SpeedModifier") && nbt.getInt("SpeedModifier") > 0) {
                    int c = getSpeedModifier(nbt);
                    double cooldown = c == 1 ? 0.8 : c == 2 ? 0.6 : c == 3 ? 0.4 : 1.2;
                    tooltip.add(Component.translatable("create_dd.block_zapper.speed_modifier").append(c == 3 ? " III" : c == 2 ? " II" : c == 1 ? " I" : "").withStyle(c == 3 ? styleFromColor(0xFFCF54) : styleFromColor(0xF1DD79)));
                    tooltip.add(Component.translatable("create_dd.block_zapper.speed_modifier.desc").append(String.valueOf(cooldown)).append(Component.translatable("create_dd.block_zapper.speed_modifier.desc2")).withStyle(styleFromColor(0xB57A4C)));
                }
                if (!(stack.getTag().contains("SpeedModifier") || stack.getTag().contains("RangeModifier") || stack.getTag().contains("SizeModifier") || stack.getTag().contains("BreakerModifier"))) {
                    tooltip.add(Component.literal(""));
                }
            }
            if (stack.getTag().contains("BlockUsed") && !hasShiftDown() && !hasControlDown()) {
                MutableComponent usedBlock = NbtUtils.readBlockState(stack.getTag().getCompound("BlockUsed")).getBlock().getName();
                tooltip.add(Lang.translateDirect("terrainzapper.usingBlock", usedBlock.withStyle(ChatFormatting.GRAY))
                        .append(" : ")
                        .append(Lang.number(getAmountFound(stack)).style(getAmountFound(stack) >= getAmountOfBlocks(stack) ? ChatFormatting.GOLD : ChatFormatting.RED).text(ChatFormatting.GRAY, " / ").component())
                        .append(Lang.number(getAmountOfBlocks(stack)).style(ChatFormatting.GOLD).component())
                        .append(".").withStyle(ChatFormatting.DARK_GRAY));
            }
        }
    }

    public static MutableComponent holdCtrl() {
        return Component.translatable("create_dd.tooltip.holdForModifiers").append("[").withStyle(ChatFormatting.DARK_GRAY)
                .append(Component.translatable("create.tooltip.keyCtrl").withStyle(hasControlDown() ? ChatFormatting.WHITE : ChatFormatting.GRAY))
                .append("]").append(Component.translatable("create_dd.block_zapper.ctrl")).withStyle(ChatFormatting.DARK_GRAY);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack item = player.getItemInHand(hand);
        ItemStack itemStackOffhand = player.getItemInHand(InteractionHand.MAIN_HAND == hand ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND);
        Item itemOffhand = itemStackOffhand.getItem();
        CompoundTag nbt = item.getOrCreateTag();
        boolean mainHand = hand == InteractionHand.MAIN_HAND;
        //ik lmao
        //OBSIDIAN TEMP FOR FUTURE UPGRADE TREE
        boolean hasHardnessModifier1 = itemOffhand == Items.ENDER_PEARL; boolean hasHardnessModifier2 = itemOffhand == Items.OBSIDIAN; boolean hasHardnessModifier3 = itemOffhand == Items.NETHER_STAR;
        boolean hasBreakerModifier = itemOffhand == DesiresItems.BURY_BLEND.get();
        boolean hasSizeModifier = itemOffhand == Items.SHULKER_SHELL;
        boolean hasScopeModifier1 = itemOffhand == Items.AMETHYST_SHARD; boolean hasScopeModifier2 = itemOffhand == Items.SPYGLASS; boolean hasScopeModifier3 = itemOffhand == Items.DIAMOND;
        boolean hasSpeedModifier1 = itemOffhand == Items.FEATHER; boolean hasSpeedModifier2 = itemOffhand == Items.GOLD_INGOT; boolean hasSpeedModifier3 = itemOffhand == AllBlocks.EXPERIENCE_BLOCK.get().asItem();
        boolean anyModifiers = hasHardnessModifier3 || hasHardnessModifier2 || hasHardnessModifier1 || hasBreakerModifier || hasSizeModifier || hasScopeModifier1 || hasScopeModifier2 || hasScopeModifier3 || hasSpeedModifier1 || hasSpeedModifier2 || hasSpeedModifier3;

        // Shift -> Open GUI
        if (player.isShiftKeyDown() && !anyModifiers) {
            if (world.isClientSide) {
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> openHandgunGUI(item, hand));
                player.getCooldowns()
                        .addCooldown(item.getItem(), 10);
            }
            return new InteractionResultHolder<>(InteractionResult.SUCCESS, item);
        }

        if (ShootableGadgetItemMethods.shouldSwap(player, item, hand, this::isZapper))
            return new InteractionResultHolder<>(InteractionResult.FAIL, item);

        Component msg = validateUsage(item);
        if (msg != null && !anyModifiers) {
            AllSoundEvents.DENY.play(world, player, player.blockPosition());
            player.displayClientMessage(msg.plainCopy()
                    .withStyle(ChatFormatting.RED), true);
            return new InteractionResultHolder<>(InteractionResult.FAIL, item);
        }

        BlockState stateToUse = Blocks.AIR.defaultBlockState();
        if (nbt.contains("BlockUsed"))
            stateToUse = NbtUtils.readBlockState(nbt.getCompound("BlockUsed"));
        stateToUse = BlockHelper.setZeroAge(stateToUse);
        CompoundTag data = null;
        if (AllTags.AllBlockTags.SAFE_NBT.matches(stateToUse) && nbt.contains("BlockData", Tag.TAG_COMPOUND)) {
            data = nbt.getCompound("BlockData");
        }

        // Raytrace - Find the target
        Vec3 start = player.position()
                .add(0, player.getEyeHeight(), 0);
        Vec3 range = player.getLookAngle()
                .scale(getZappingRange(item));
        BlockHitResult raytrace =
                world.clip(new ClipContext(start, start.add(range), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player));
        BlockPos pos = raytrace.getBlockPos();
        BlockState stateReplaced = world.getBlockState(pos);

        if (player.isShiftKeyDown() && anyModifiers) {

            Map<Item, BiConsumer<CompoundTag, Integer>> itemToModifierMap = new HashMap<>();

            itemToModifierMap.put(Items.SHULKER_SHELL, (nbtTag, playerEntity) -> {
                if (!getBreakerModifier(nbtTag)) {
                    setBreakerModifier(nbtTag, true);
                    itemStackOffhand.shrink(1);
                    player.playSound(DesiresSoundEvents.BLOCK_ZAPPER_UPGRADE.get(), 0.8f, 0.8f);
                    player.playSound(SoundEvents.SHULKER_SHOOT, 0.7f, 0.8f);
                } else {
                    AllSoundEvents.DENY.play(world, player, player.blockPosition());
                    player.displayClientMessage(Component.translatable("create_dd.block_zapper.too_much").withStyle(ChatFormatting.RED), true);
                }
            });
            itemToModifierMap.put(DesiresItems.BURY_BLEND.get(), (nbtTag, playerEntity) -> {
                if (!getSizeModifier(nbtTag)) {
                    setSizeModifier(nbtTag, true);
                    itemStackOffhand.shrink(1);
                    player.playSound(DesiresSoundEvents.BLOCK_ZAPPER_UPGRADE.get(), 0.8f, 0.8f);
                    player.playSound(SoundEvents.AXE_SCRAPE, 0.7f, 0.5f);
                } else {
                    AllSoundEvents.DENY.play(world, player, player.blockPosition());
                    player.displayClientMessage(Component.translatable("create_dd.block_zapper.too_much").withStyle(ChatFormatting.RED), true);
                }
            });
            itemToModifierMap.put(Items.AMETHYST_SHARD, (nbtTag, playerEntity) -> {
                boolean success = upgradeRangeModifier(nbtTag, 1);
                if (success) itemStackOffhand.shrink(1);
                if (success && world.isClientSide) { player.playSound(DesiresSoundEvents.BLOCK_ZAPPER_UPGRADE.get(), 0.8f, 0.8f);
                    player.playSound(SoundEvents.COPPER_BREAK, 0.7f, 0.5f);
                } else if (world.isClientSide && getRangeModifier(nbtTag) < 1) { AllSoundEvents.DENY.play(world, player, player.blockPosition());
                    player.displayClientMessage(Component.translatable("create_dd.block_zapper.need_upgrade_below").withStyle(ChatFormatting.RED), true);
                } else if (world.isClientSide) { AllSoundEvents.DENY.play(world, player, player.blockPosition());
                    player.displayClientMessage(Component.translatable("create_dd.block_zapper.too_much").withStyle(ChatFormatting.RED), true);
                }
            });
            itemToModifierMap.put(Items.SPYGLASS, (nbtTag, playerEntity) -> {
                boolean success = upgradeRangeModifier(nbtTag, 2);
                if (success) itemStackOffhand.shrink(1);
                if (success && world.isClientSide) { player.playSound(DesiresSoundEvents.BLOCK_ZAPPER_UPGRADE.get(), 0.8f, 0.8f);
                    player.playSound(SoundEvents.COPPER_BREAK, 0.7f, 0.5f);
                } else if (world.isClientSide && getRangeModifier(nbtTag) < 2) { AllSoundEvents.DENY.play(world, player, player.blockPosition());
                    player.displayClientMessage(Component.translatable("create_dd.block_zapper.need_upgrade_below").withStyle(ChatFormatting.RED), true);
                } else if (world.isClientSide) { AllSoundEvents.DENY.play(world, player, player.blockPosition());
                    player.displayClientMessage(Component.translatable("create_dd.block_zapper.too_much").withStyle(ChatFormatting.RED), true);
                }
            });
            itemToModifierMap.put(Items.DIAMOND, (nbtTag, playerEntity) -> {
                boolean success = upgradeRangeModifier(nbtTag, 3);
                if (success) itemStackOffhand.shrink(1);
                if (success && world.isClientSide) { player.playSound(DesiresSoundEvents.BLOCK_ZAPPER_UPGRADE.get(), 0.8f, 0.8f);
                    player.playSound(SoundEvents.COPPER_BREAK, 0.7f, 0.5f);
                } else if (world.isClientSide && getRangeModifier(nbtTag) < 3) { AllSoundEvents.DENY.play(world, player, player.blockPosition());
                    player.displayClientMessage(Component.translatable("create_dd.block_zapper.need_upgrade_below").withStyle(ChatFormatting.RED), true);
                } else if (world.isClientSide) { AllSoundEvents.DENY.play(world, player, player.blockPosition());
                    player.displayClientMessage(Component.translatable("create_dd.block_zapper.too_much").withStyle(ChatFormatting.RED), true);
                }
            });
            itemToModifierMap.put(Items.FEATHER, (nbtTag, playerEntity) -> {
                boolean success = upgradeSpeedModifier(nbtTag, 1);
                if (success) itemStackOffhand.shrink(1);
                if (success && world.isClientSide) { player.playSound(DesiresSoundEvents.BLOCK_ZAPPER_UPGRADE.get(), 0.8f, 0.8f);
                    player.playSound(SoundEvents.ARMOR_EQUIP_LEATHER, 0.7f, 0.6f);
                } else if (world.isClientSide && getSpeedModifier(nbtTag) < 1) { AllSoundEvents.DENY.play(world, player, player.blockPosition());
                    player.displayClientMessage(Component.translatable("create_dd.block_zapper.need_upgrade_below").withStyle(ChatFormatting.RED), true);
                } else if (world.isClientSide) { AllSoundEvents.DENY.play(world, player, player.blockPosition());
                    player.displayClientMessage(Component.translatable("create_dd.block_zapper.too_much").withStyle(ChatFormatting.RED), true);
                }
            });
            itemToModifierMap.put(Items.GOLD_INGOT, (nbtTag, playerEntity) -> {
                boolean success = upgradeSpeedModifier(nbtTag, 2);
                if (success) itemStackOffhand.shrink(1);
                if (success && world.isClientSide) { player.playSound(DesiresSoundEvents.BLOCK_ZAPPER_UPGRADE.get(), 0.8f, 0.8f);
                    player.playSound(SoundEvents.ARMOR_EQUIP_LEATHER, 0.7f, 0.6f);
                } else if (world.isClientSide && getSpeedModifier(nbtTag) < 2) { AllSoundEvents.DENY.play(world, player, player.blockPosition());
                    player.displayClientMessage(Component.translatable("create_dd.block_zapper.need_upgrade_below").withStyle(ChatFormatting.RED), true);
                } else if (world.isClientSide) { AllSoundEvents.DENY.play(world, player, player.blockPosition());
                    player.displayClientMessage(Component.translatable("create_dd.block_zapper.too_much").withStyle(ChatFormatting.RED), true);
                }
            });
            itemToModifierMap.put(AllBlocks.EXPERIENCE_BLOCK.get().asItem(), (nbtTag, playerEntity) -> {
                boolean success = upgradeSpeedModifier(nbtTag, 3);
                if (success) itemStackOffhand.shrink(1);
                if (success && world.isClientSide) { player.playSound(DesiresSoundEvents.BLOCK_ZAPPER_UPGRADE.get(), 0.8f, 0.8f);
                    player.playSound(SoundEvents.ARMOR_EQUIP_LEATHER, 0.7f, 0.6f);
                } else if (world.isClientSide && getSpeedModifier(nbtTag) < 3) { AllSoundEvents.DENY.play(world, player, player.blockPosition());
                    player.displayClientMessage(Component.translatable("create_dd.block_zapper.need_upgrade_below").withStyle(ChatFormatting.RED), true);
                } else if (world.isClientSide) { AllSoundEvents.DENY.play(world, player, player.blockPosition());
                    player.displayClientMessage(Component.translatable("create_dd.block_zapper.too_much").withStyle(ChatFormatting.RED), true);
                }
            });
            itemToModifierMap.put(Items.ENDER_PEARL, (nbtTag, playerEntity) -> {
                boolean success = upgradeHardnessModifier(nbtTag, 1) && getSpeedModifier(nbtTag) == 3 && getRangeModifier(nbtTag) == 3 && getSizeModifier(nbtTag) && getBreakerModifier(nbtTag);
                if (success) itemStackOffhand.shrink(1);
                if (success && world.isClientSide) { player.playSound(DesiresSoundEvents.BLOCK_ZAPPER_UPGRADE.get(), 0.8f, 0.8f);
                    player.playSound(SoundEvents.GLOW_INK_SAC_USE, 0.7f, 0.5f);
                } else if (world.isClientSide && getHardnessModifier(nbtTag) < 1) { AllSoundEvents.DENY.play(world, player, player.blockPosition());
                    player.displayClientMessage(Component.translatable("create_dd.block_zapper.need_upgrade_below").withStyle(ChatFormatting.RED), true);
                } else if (world.isClientSide) { AllSoundEvents.DENY.play(world, player, player.blockPosition());
                    player.displayClientMessage(Component.translatable("create_dd.block_zapper.too_much").withStyle(ChatFormatting.RED), true);
                }
            });
            itemToModifierMap.put(Items.NETHERITE_INGOT, (nbtTag, playerEntity) -> {
                boolean success = upgradeHardnessModifier(nbtTag, 2);
                if (success) itemStackOffhand.shrink(1);
                if (success && world.isClientSide) { player.playSound(DesiresSoundEvents.BLOCK_ZAPPER_UPGRADE.get(), 0.8f, 0.8f);
                    player.playSound(SoundEvents.ARMOR_EQUIP_NETHERITE, 0.7f, 0.8f);
                } else if (world.isClientSide && getHardnessModifier(nbtTag) < 2) { AllSoundEvents.DENY.play(world, player, player.blockPosition());
                    player.displayClientMessage(Component.translatable("create_dd.block_zapper.need_upgrade_below").withStyle(ChatFormatting.RED), true);
                } else if (world.isClientSide) { AllSoundEvents.DENY.play(world, player, player.blockPosition());
                    player.displayClientMessage(Component.translatable("create_dd.block_zapper.too_much").withStyle(ChatFormatting.RED), true);
                }
            });
            itemToModifierMap.put(Items.NETHER_STAR, (nbtTag, playerEntity) -> {
                boolean success = upgradeHardnessModifier(nbtTag, 3);
                if (success) itemStackOffhand.shrink(1);
                if (success && world.isClientSide) { player.playSound(DesiresSoundEvents.BLOCK_ZAPPER_UPGRADE.get(), 0.8f, 0.8f);
                    player.playSound(SoundEvents.BEACON_ACTIVATE, 0.7f, 0.8f);
                } else if (world.isClientSide && getHardnessModifier(nbtTag) < 3) { AllSoundEvents.DENY.play(world, player, player.blockPosition());
                    player.displayClientMessage(Component.translatable("create_dd.block_zapper.need_upgrade_below").withStyle(ChatFormatting.RED), true);
                } else if (world.isClientSide) { AllSoundEvents.DENY.play(world, player, player.blockPosition());
                    player.displayClientMessage(Component.translatable("create_dd.block_zapper.too_much").withStyle(ChatFormatting.RED), true);
                }
            });
            itemToModifierMap.forEach((items, modifierLogic) -> {
                if (itemOffhand == items) {
                    modifierLogic.accept(nbt, 0);
                }
            });

            activateForValue(world, player, item, stateToUse, raytrace);

            return new InteractionResultHolder<>(InteractionResult.SUCCESS, item);
        }

        // No target
        if (stateReplaced.getBlock() == Blocks.AIR) {
            ShootableGadgetItemMethods.applyCooldown(player, item, hand, this::isZapper, getCooldownDelay(item));
            return new InteractionResultHolder<>(InteractionResult.SUCCESS, item);
        }

        // Find exact position of gun barrel for VFX
        Vec3 barrelPos = ShootableGadgetItemMethods.getGunBarrelVec(player, mainHand, new Vec3(.35f, -0.1f, 1));


        // Client side
        if (world.isClientSide) {
            if (getAmountFound(item) < getAmountOfBlocks(item) && !player.isCreative()) {
                AllSoundEvents.DENY.play(world, player, player.blockPosition());
                player.displayClientMessage(Component.translatable("create_dd.block_zapper.not_enough_blocks").withStyle(ChatFormatting.RED), true);
            }

            CreateClient.ZAPPER_RENDER_HANDLER.dontAnimateItem(hand);
            return new InteractionResultHolder<>(InteractionResult.SUCCESS, item);
        }

        CompoundTag tag = item.getOrCreateTag();
        TerrainTools tool = NBTHelper.readEnum(tag, "Tool", TerrainTools.class);

        Item required = getRequiredItem(stateToUse).getItem();

        // Server side
        if (player.getInventory().contains(new ItemStack(required)) && tool.requiresSelectedBlock() && !player.isCreative()) {
            if (activateForValue(world, player, item, stateToUse, raytrace)) {
                if (getAmountFound(item) >= getAmountOfBlocks(item)) {
                    if (activate(world, player, item, stateToUse, raytrace, data)) {
                        ShootableGadgetItemMethods.applyCooldown(player, item, hand, this::isZapper, getCooldownDelay(item));
                        ShootableGadgetItemMethods.sendPackets(player,
                                b -> new ZapperBeamPacket(barrelPos, raytrace.getLocation(), hand, b));
                    }
                }
            }
        } else if (tool == TerrainTools.Clear || player.isCreative()) {
            if (activateForValue(world, player, item, stateToUse, raytrace)) {
                if (activate(world, player, item, stateToUse, raytrace, data)) {
                    ShootableGadgetItemMethods.applyCooldown(player, item, hand, this::isZapper, getCooldownDelay(item));
                    ShootableGadgetItemMethods.sendPackets(player,
                            b -> new ZapperBeamPacket(barrelPos, raytrace.getLocation(), hand, b));
                }
            }
        }

        return new InteractionResultHolder<>(InteractionResult.SUCCESS, item);
    }


    @Override
    public boolean isDamageable(ItemStack stack) {
        return true;
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return 1024;
    }

    @Override
    public Component validateUsage(ItemStack item) {
        if (!item.getOrCreateTag()
                .contains("BrushParams"))
            return Lang.translateDirect("terrainzapper.shiftRightClickToSet");
        return super.validateUsage(item);
    }

    @Override
    protected boolean canActivateWithoutSelectedBlock(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        TerrainTools tool = NBTHelper.readEnum(tag, "Tool", TerrainTools.class);
        return !tool.requiresSelectedBlock();
    }

    @Override
    protected boolean activate(Level world, Player player, ItemStack stack, BlockState stateToUse,
                               BlockHitResult raytrace, CompoundTag data) {

        BlockPos targetPos = raytrace.getBlockPos();
        List<BlockPos> affectedPositions = new ArrayList<>();

        CompoundTag tag = stack.getOrCreateTag();
        boolean toVoid = tag.getBoolean("toVoid");
        Brush brush = NBTHelper.readEnum(tag, "Brush", TerrainBrushes.class).get();
        BlockPos params = NbtUtils.readBlockPos(tag.getCompound("BrushParams"));
        PlacementOptions option = NBTHelper.readEnum(tag, "Placement", PlacementOptions.class);
        TerrainTools tool = NBTHelper.readEnum(tag, "Tool", TerrainTools.class);

        brush.set(params.getX(), params.getY(), params.getZ(), getSizeModifier(tag));
        targetPos = targetPos.offset(brush.getOffset(player.getLookAngle(), raytrace.getDirection(), option));
        brush.addToGlobalPositions(world, targetPos, raytrace.getDirection(), affectedPositions, tool);
        PlacementPatterns.applyPattern(affectedPositions, stack);
        brush.redirectTool(tool).run(world, affectedPositions, raytrace.getDirection(), stateToUse, data, player, raytrace, stack, toVoid);

        return true;
    }

    public static void configureSettings(ItemStack stack, PlacementPatterns pattern, TerrainBrushes brush,
                                         int brushParamX, int brushParamY, int brushParamZ, TerrainTools tool, PlacementOptions placement) {

        ZapperItem.configureSettings(stack, pattern);
        CompoundTag nbt = stack.getOrCreateTag();
        NBTHelper.writeEnum(nbt, "Brush", brush);
        nbt.put("BrushParams", NbtUtils.writeBlockPos(new BlockPos(brushParamX, brushParamY, brushParamZ)));
        NBTHelper.writeEnum(nbt, "Tool", tool);
        NBTHelper.writeEnum(nbt, "Placement", placement);
        brush.get().setBulk(nbt.getBoolean("SizeModifier"));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(SimpleCustomRenderer.create(this, new BlockZapperItemRenderer()));
    }

    @Override
    @OnlyIn(value = Dist.CLIENT)
    protected void openHandgunGUI(ItemStack item, InteractionHand hand) {
        ScreenOpener.open(new BlockZapperScreen(item, hand));
    }

    @Override
    protected int getCooldownDelay(ItemStack item) {
        CompoundTag nbt = item.getOrCreateTag();
        int c = getSpeedModifier(nbt);
        return c == 1 ? 16 : c == 2 ? 12 : c == 3 ? 8 : 24;
    }

    @Override
    protected int getZappingRange(ItemStack stack) {
        CompoundTag nbt = stack.getOrCreateTag();
        int r = getRangeModifier(nbt);
        return r == 1 ? 16 : r == 2 ? 24 : r == 3 ? 32 : 8;
    }

    public static int getZappingRangeValue(ItemStack stack) {
        CompoundTag nbt = stack.getOrCreateTag();
        int r = nbt.getInt("RangeModifier");
        return r == 1 ? 16 : r == 2 ? 24 : r == 3 ? 32 : 8;
    }

    @Override
    public void inventoryTick(@NotNull ItemStack pStack, @NotNull Level pLevel, @NotNull Entity pEntity, int pSlotId, boolean pIsSelected) {
        pStack.getOrCreateTagElement("Valid");
        super.inventoryTick(pStack, pLevel, pEntity, pSlotId, pIsSelected);

        if (!pIsSelected && pLevel.getGameTime() % 10 != 0)
            return;
        if (!(pEntity instanceof Player player))
            return;

        CompoundTag nbt = pStack.getOrCreateTag();

        nbt.putBoolean("toVoid", !getBreakerModifier(nbt));

        BlockState stateToUse = Blocks.AIR.defaultBlockState();
        if (nbt.contains("BlockUsed"))
            stateToUse = NbtUtils.readBlockState(nbt.getCompound("BlockUsed"));
        stateToUse = BlockHelper.setZeroAge(stateToUse);

        Vec3 start = player.position()
                .add(0, player.getEyeHeight(), 0);
        Vec3 range = player.getLookAngle()
                .scale(getZappingRange(pStack));
        BlockHitResult raytrace = pLevel.clip(new ClipContext(start, start.add(range), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player));


        //getInventoryAmount
        Item required = getRequiredItem(stateToUse).getItem();

        int totalCount = 0;
        for (ItemStack itemStack : player.getInventory().items) {
            if (itemStack.getItem() == required) {
                totalCount += itemStack.getCount();
            }
        }
        setAmountFound(pStack, totalCount);



        if (!player.isCreative()) {
            activateForValue(pLevel, player, pStack, stateToUse, raytrace);
        } else {
            setAmountOfBlocks(pStack, 0);
            setAmountOfAllBlocks(pStack, 0);
        }

    }

    public boolean getBreakerModifier(CompoundTag nbt) {
        return nbt.getBoolean("BreakerModifier");
    }
    public boolean getSizeModifier(CompoundTag nbt) {
        return nbt.getBoolean("SizeModifier");
    }
    public int getRangeModifier(CompoundTag nbt) {
        return nbt.getInt("RangeModifier");
    }
    public int getSpeedModifier(CompoundTag nbt) {
        return nbt.getInt("SpeedModifier");
    }
    public int getHardnessModifier(CompoundTag nbt) {
        return nbt.getInt("HardnessModifier");
    }

    public void setBreakerModifier(CompoundTag nbt, boolean value) {
        nbt.putBoolean("BreakerModifier", value);
    }
    public void setSizeModifier(CompoundTag nbt, boolean value) {
        nbt.putBoolean("SizeModifier", value);
    }
    public boolean upgradeRangeModifier(CompoundTag nbt, int targetValue) {
        int currentValue = getRangeModifier(nbt);
        if (targetValue == currentValue + 1 && currentValue < 3) {
            nbt.putInt("RangeModifier", targetValue);
            return true;
        }
        return false;
    }
    public boolean upgradeSpeedModifier(CompoundTag nbt, int targetValue) {
        int currentValue = getSpeedModifier(nbt);
        if (targetValue == currentValue + 1 && currentValue < 3) {
            nbt.putInt("SpeedModifier", targetValue);
            return true;
        }
        return false;
    }
    public boolean upgradeHardnessModifier(CompoundTag nbt, int targetValue) {
        int currentValue = getHardnessModifier(nbt);
        if (targetValue == currentValue + 1 && currentValue < 3) {
            nbt.putInt("HardnessModifier", targetValue);
            return true;
        }
        return false;
    }

    @Override
    public boolean isFoil(@NotNull ItemStack pStack) {
        return false;
    }

    public int getAmountFound(ItemStack pStack) {
        CompoundTag nbt = pStack.getOrCreateTag();
        return nbt.getInt("InventoryAmount");
    }

    public void setAmountFound(ItemStack pStack, int amountFound) {
        CompoundTag nbt = pStack.getOrCreateTag();
        nbt.putInt("InventoryAmount", amountFound);
    }

    protected boolean activateForValue(Level world, Player player, ItemStack stack, BlockState stateToUse, BlockHitResult raytrace) {

        BlockPos targetPos = raytrace.getBlockPos();
        List<BlockPos> affectedPositions = new ArrayList<>();

        CompoundTag tag = stack.getOrCreateTag();
        Brush brush = NBTHelper.readEnum(tag, "Brush", TerrainBrushes.class).get();
        BlockPos params = NbtUtils.readBlockPos(tag.getCompound("BrushParams"));
        PlacementOptions option = NBTHelper.readEnum(tag, "Placement", PlacementOptions.class);
        TerrainTools tool = NBTHelper.readEnum(tag, "Tool", TerrainTools.class);

        brush.set(params.getX(), params.getY(), params.getZ(), getSizeModifier(tag));
        targetPos = targetPos.offset(brush.getOffset(player.getLookAngle(), raytrace.getDirection(), option));
        brush.addToGlobalPositions(world, targetPos, raytrace.getDirection(), affectedPositions, tool);
        PlacementPatterns.applyPattern(affectedPositions, stack);
        runForValue(tool, world, affectedPositions, stateToUse, stack);

        return true;
    }


    public void runForValue(TerrainTools tools, Level world, List<BlockPos> targetPositions, @Nullable BlockState paintedState, ItemStack zapper) {
        assert paintedState != null;
        switch (tools) {
            case Clear:
                setAmountOfBlocks(zapper, 0);
                setAmountOfAllBlocks(zapper, 0);
                targetPositions.forEach(p -> {
                    BlockState toReplace = world.getBlockState(p);
                    if (blacklist(toReplace))
                        return;
                    if (toReplace.getBlock().equals(Blocks.AIR) || toReplace.getBlock().equals(Blocks.CAVE_AIR) || toReplace.getBlock().equals(Blocks.VOID_AIR))
                        return;
                    if (!paintedState.canSurvive(world, p))
                        return;
                    if (!world.getWorldBorder().isWithinBounds(p))
                        return;
                    setAmountOfAllBlocks(zapper, getAmountOfAllBlocks(zapper) + 1);
                });
                break;
            case Fill:
                setAmountOfBlocks(zapper,0);
                setAmountOfAllBlocks(zapper,0);
                targetPositions.forEach(p -> {
                    setAmountOfAllBlocks(zapper, getAmountOfAllBlocks(zapper) + 1);
                    BlockState toReplace = world.getBlockState(p);
                    if (blacklist(toReplace))
                        return;
                    if (!isReplaceable(toReplace))
                        return;
                    if (toReplace == paintedState)
                        return;
                    if (!paintedState.canSurvive(world, p))
                        return;
                    if (!world.getWorldBorder().isWithinBounds(p))
                        return;
                    setAmountOfBlocks(zapper, getAmountOfBlocks(zapper) + 1);
                });
                break;
            case Overlay:
                setAmountOfBlocks(zapper, 0);
                setAmountOfAllBlocks(zapper, 0);
                targetPositions.forEach(p -> {
                    setAmountOfAllBlocks(zapper, getAmountOfAllBlocks(zapper) + 1);
                    BlockState toOverlay = world.getBlockState(p);
                    if (blacklist(toOverlay))
                        return;
                    if (isReplaceable(toOverlay))
                        return;
                    if (toOverlay == paintedState)
                        return;
                    if (!paintedState.canSurvive(world, p))
                        return;
                    if (!world.getWorldBorder().isWithinBounds(p))
                        return;
                    p = p.above();
                    BlockState toReplace = world.getBlockState(p);
                    if (blacklist(toReplace))
                        return;
                    if (isReplaceable(toReplace))
                        return;
                    if (toReplace == paintedState)
                        return;
                    if (!paintedState.canSurvive(world, p))
                        return;
                    if (!world.getWorldBorder().isWithinBounds(p))
                        return;
                    setAmountOfBlocks(zapper, getAmountOfBlocks(zapper) + 1);

                });
                break;
            case Place:
                setAmountOfBlocks(zapper, 0);
                setAmountOfAllBlocks(zapper, 0);
                targetPositions.forEach(p -> {
                    BlockState toReplace = world.getBlockState(p);
                    if (blacklist(toReplace))
                        return;
                    if (toReplace == paintedState)
                        return;
                    if (!paintedState.canSurvive(world, p))
                        return;
                    if (!world.getWorldBorder().isWithinBounds(p))
                        return;
                    setAmountOfBlocks(zapper, getAmountOfBlocks(zapper) + 1);
                    setAmountOfAllBlocks(zapper, getAmountOfAllBlocks(zapper) + 1);
                });
                break;
            case Replace:
                setAmountOfBlocks(zapper, 0);
                setAmountOfAllBlocks(zapper, 0);
                targetPositions.forEach(p -> {
                    setAmountOfAllBlocks(zapper, getAmountOfAllBlocks(zapper) + 1);
                    BlockState toReplace = world.getBlockState(p);
                    if (blacklist(toReplace))
                        return;
                    if (isReplaceable(toReplace))
                        return;
                    if (toReplace == paintedState)
                        return;
                    if (!paintedState.canSurvive(world, p))
                        return;
                    if (!world.getWorldBorder().isWithinBounds(p))
                        return;
                    setAmountOfBlocks(zapper, getAmountOfBlocks(zapper) + 1);
                });
                break;
        }
    }

    public int getAmountOfBlocks(ItemStack pStack) {
        CompoundTag nbt = pStack.getOrCreateTag();
        return nbt.getInt("AmountOfBlocks");
    }

    public int getAmountOfAllBlocks(ItemStack pStack) {
        CompoundTag nbt = pStack.getOrCreateTag();
        return nbt.getInt("AmountOfAllBlocks");
    }

    public void setAmountOfBlocks(ItemStack pStack, int amountOfBlocks) {
        CompoundTag nbt = pStack.getOrCreateTag();
        nbt.putInt("AmountOfBlocks", amountOfBlocks);
    }

    public void setAmountOfAllBlocks(ItemStack pStack, int amountOfAllBlocks) {
        CompoundTag nbt = pStack.getOrCreateTag();
        nbt.putInt("AmountOfAllBlocks", amountOfAllBlocks);
    }

    public static boolean isReplaceable(BlockState toReplace) {
        return toReplace.getMaterial().isReplaceable() || toReplace.is(DesiresTags.AllBlockTags.BLOCK_ZAPPER_REPLACEABLE.tag);
    }
    public static boolean blacklist(BlockState toBlacklist) {
        return toBlacklist.is(DesiresTags.AllBlockTags.BLOCK_ZAPPER_BLACKLIST.tag) ||
                toBlacklist.is(AllTags.AllBlockTags.NON_MOVABLE.tag);
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        if (enchantment == Enchantments.UNBREAKING)
            return true;
        if (enchantment == Enchantments.MENDING)
            return false;
        return enchantment == Enchantments.VANISHING_CURSE;
    }

    @Override
    public boolean isEnchantable(@NotNull ItemStack pStack) {
        assert pStack.getTag() != null;
        return !pStack.getTag().contains("Damage") || pStack.getTag().contains("Valid");
    }
}
