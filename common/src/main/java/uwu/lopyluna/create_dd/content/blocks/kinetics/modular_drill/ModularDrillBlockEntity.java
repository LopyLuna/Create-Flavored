package uwu.lopyluna.create_dd.content.blocks.kinetics.modular_drill;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.kinetics.base.BlockBreakingKineticBlockEntity;
import com.simibubi.create.content.kinetics.crusher.CrushingRecipe;
import com.simibubi.create.content.kinetics.drill.DrillBlock;
import com.simibubi.create.content.kinetics.millstone.MillingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.foundation.utility.BlockHelper;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.core.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import uwu.lopyluna.create_dd.registry.DesiresTags;

import java.util.*;
import java.util.function.Consumer;

import static uwu.lopyluna.create_dd.infrastructure.utility.VeinMining.findVein;

public class ModularDrillBlockEntity extends BlockBreakingKineticBlockEntity {

    private ModularDrillHeadItem.DrillType currentDrillHeadType;
    private Map<Enchantment, Integer> drillHeadEnchantments = new HashMap<>();

    public ModularDrillBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.currentDrillHeadType = state.getValue(ModularDrillBlock.DRILL_TYPE);
    }

    public void setDrillHeadType(ModularDrillHeadItem.DrillType drillType) {
        this.currentDrillHeadType = drillType;
        setChanged();
    }

    public ModularDrillHeadItem.DrillType getCurrentDrillHeadType() {
        return this.currentDrillHeadType;
    }

    @Override
    protected BlockPos getBreakingPos() {
        return getBlockPos().relative(getBlockState().getValue(DrillBlock.FACING));
    }

    public void updateDrillHeadEnchantmentsFromItem(ItemStack drillHeadItem) {
        if (currentDrillHeadType == ModularDrillHeadItem.DrillType.ENCHANTABLE) {
            drillHeadEnchantments = EnchantmentHelper.getEnchantments(drillHeadItem);
        } else {
            drillHeadEnchantments.clear();
        }
    }

    @Override
    public void write(CompoundTag tag, boolean clientPacket) {
        super.write(tag, clientPacket);
        tag.putString("DrillHeadType", currentDrillHeadType.name());

        ListTag enchantmentList = new ListTag();
        for (Map.Entry<Enchantment, Integer> entry : drillHeadEnchantments.entrySet()) {
            CompoundTag enchantmentTag = new CompoundTag();
            ResourceLocation enchantmentKey = Registry.ENCHANTMENT.getKey(entry.getKey());
            if (enchantmentKey != null) {
                enchantmentTag.putString("id", enchantmentKey.toString());
                enchantmentTag.putInt("lvl", entry.getValue());
                enchantmentList.add(enchantmentTag);
            }
        }
        tag.put("DrillEnchantments", enchantmentList);
    }

    @Override
    protected void read(CompoundTag tag, boolean clientPacket) {
        super.read(tag, clientPacket);
        if (tag.contains("DrillHeadType")) {
            currentDrillHeadType = ModularDrillHeadItem.DrillType.valueOf(tag.getString("DrillHeadType"));
        }
        if (tag.contains("DrillEnchantments")) {
            ListTag enchantmentList = tag.getList("DrillEnchantments", 10);
            drillHeadEnchantments.clear();
            for (int i = 0; i < enchantmentList.size(); i++) {
                CompoundTag enchantmentTag = enchantmentList.getCompound(i);
                Enchantment enchantment = Registry.ENCHANTMENT.get(new ResourceLocation(enchantmentTag.getString("id")));
                int level = enchantmentTag.getInt("lvl");
                if (enchantment != null) {
                    drillHeadEnchantments.put(enchantment, level);
                }
            }
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (Objects.requireNonNull(getLevel()).isClientSide)
            return;
        if (!shouldRun())
            return;
        if (getSpeed() == 0)
            return;

        if (currentDrillHeadType != ModularDrillHeadItem.DrillType.ENCHANTABLE) {
            drillHeadEnchantments.clear();
        }
    }


    @Override
    public void onBlockBroken(BlockState stateToBreak) {
        switch (currentDrillHeadType) {
            case VEIN:
                performVeinMining(breakingPos, stateToBreak);
                break;
            case ENCHANTABLE:
                performEnchantableMining(breakingPos, stateToBreak);
                break;
            case CRUSHER:
                performCrushing(breakingPos, stateToBreak);
                break;
            case SMELTING:
                performAutoSmelting(breakingPos, stateToBreak);
                break;
            default:
                defaultBlockBreaking(stateToBreak);
                break;
        }
    }

    @SuppressWarnings("unused")
    private void defaultBlockBreaking(BlockState stateToBreak) {
        Vec3 vec = VecHelper.offsetRandomly(VecHelper.getCenterOf(breakingPos), Objects.requireNonNull(getLevel()).random, .125f);

        BlockHelper.destroyBlock(getLevel(), breakingPos, 1f, (stack) -> {
            if (stack.isEmpty()) return;
            if (!getLevel().getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS)) return;
            if (getLevel().restoringBlockSnapshots) return;

            ItemEntity itemEntity = new ItemEntity(getLevel(), vec.x, vec.y, vec.z, stack);
            itemEntity.setDefaultPickUpDelay();
            itemEntity.setDeltaMovement(Vec3.ZERO);
            getLevel().addFreshEntity(itemEntity);
        });
    }

    private void performVeinMining(BlockPos breakingPos, BlockState stateToBreak) {
        if (stateToBreak.is(DesiresTags.AllBlockTags.MODULAR_VEIN.tag) ||
                stateToBreak.is(DesiresTags.AllBlockTags.MODULAR_VEIN_SMALL.tag) ||
                stateToBreak.is(DesiresTags.AllBlockTags.MODULAR_VEIN_MEDIUM.tag) ||
                stateToBreak.is(DesiresTags.AllBlockTags.MODULAR_VEIN_LARGE.tag)) {

            if (stateToBreak.is(DesiresTags.AllBlockTags.MODULAR_VEIN_LARGE.tag)) {
                veinMine(Objects.requireNonNull(getLevel()), breakingPos, DesiresTags.AllBlockTags.MODULAR_VEIN_LARGE.tag, 64);
                defaultBlockBreaking(stateToBreak);
            } else if (stateToBreak.is(DesiresTags.AllBlockTags.MODULAR_VEIN_MEDIUM.tag)) {
                veinMine(Objects.requireNonNull(getLevel()), breakingPos, DesiresTags.AllBlockTags.MODULAR_VEIN_MEDIUM.tag, 32);
                defaultBlockBreaking(stateToBreak);
            } else if (stateToBreak.is(DesiresTags.AllBlockTags.MODULAR_VEIN_SMALL.tag)) {
                veinMine(Objects.requireNonNull(getLevel()), breakingPos, DesiresTags.AllBlockTags.MODULAR_VEIN_SMALL.tag, 16);
                defaultBlockBreaking(stateToBreak);
            } else {
                veinMine(Objects.requireNonNull(getLevel()), breakingPos, DesiresTags.AllBlockTags.MODULAR_VEIN.tag, 8);
                defaultBlockBreaking(stateToBreak);
            }
        } else {
            defaultBlockBreaking(stateToBreak);
        }
    }

    public static void veinMine(Level pLevel, BlockPos startPos, TagKey<Block> filterTag, int maxBlocks) {
        if (!(pLevel.getBlockState(startPos).is(filterTag)))
            return;

        findVein(pLevel, startPos, filterTag, maxBlocks).destroyBlocks(pLevel, null, (dropPos, item) -> dropItemFromExcavatedVein(pLevel, dropPos, item));
    }
    public static void dropItemFromExcavatedVein(Level world, BlockPos pos,
                                                 ItemStack stack) {
        Vec3 dropPos = VecHelper.getCenterOf(pos);
        ItemEntity entity = new ItemEntity(world, dropPos.x, dropPos.y, dropPos.z, stack);
        world.addFreshEntity(entity);
    }

    private void performEnchantableMining(BlockPos breakingPos, BlockState stateToBreak) {
        if (drillHeadEnchantments.isEmpty()) {
            defaultBlockBreaking(stateToBreak);
            return;
        }

        Vec3 vec = VecHelper.offsetRandomly(VecHelper.getCenterOf(breakingPos), Objects.requireNonNull(getLevel()).random, .125f);
        ItemStack drill = ItemStack.EMPTY;
        drillHeadEnchantments.forEach(drill::enchant);

        destroyBlock(getLevel(), breakingPos, drill, 1f, (stack) -> {
            if (stack.isEmpty() || !getLevel().getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS)) return;
            if (getLevel().restoringBlockSnapshots) return;

            ItemEntity itemEntity = new ItemEntity(getLevel(), vec.x, vec.y, vec.z, stack);
            itemEntity.setDefaultPickUpDelay();
            itemEntity.setDeltaMovement(Vec3.ZERO);
            getLevel().addFreshEntity(itemEntity);
        });
    }

    public static void destroyBlock(Level world, BlockPos pos, ItemStack pTool, float effectChance, Consumer<ItemStack> droppedItemCallback) {
        BlockHelper.destroyBlockAs(world, pos, null, pTool, effectChance, droppedItemCallback);
    }

    private void performCrushing(BlockPos breakingPos, BlockState stateToBreak) {
        ItemStackHandler inventory = new ItemStackHandler(1);
        inventory.setStackInSlot(0, new ItemStack(stateToBreak.getBlock().asItem()));

        RecipeWrapper recipeWrapper = new RecipeWrapper(inventory);

        Optional<CrushingRecipe> crushingRecipe = Objects.requireNonNull(getLevel()).getRecipeManager()
                .getRecipeFor(AllRecipeTypes.CRUSHING.getType(), recipeWrapper, getLevel())
                .map(recipe -> (CrushingRecipe) recipe);

        Optional<MillingRecipe> millingRecipe = getLevel().getRecipeManager()
                .getRecipeFor(AllRecipeTypes.MILLING.getType(), recipeWrapper, getLevel())
                .map(recipe -> (MillingRecipe) recipe);

        if (crushingRecipe.isPresent()) {
            List<ItemStack> results = processRecipeOutputs(crushingRecipe.get(), inventory);
            if (!results.isEmpty()) {
                dropItems(results, breakingPos);
            } else if (millingRecipe.isPresent()) {
                List<ItemStack> innerResults = processRecipeOutputs(millingRecipe.get(), inventory);
                if (!innerResults.isEmpty()) {
                    dropItems(innerResults, breakingPos);
                } else {
                    BlockHelper.destroyBlock(getLevel(), breakingPos, 1f, (stack) -> {});
                }
            } else {
                BlockHelper.destroyBlock(getLevel(), breakingPos, 1f, (stack) -> {});
            }
        } else if (millingRecipe.isPresent()) {
            List<ItemStack> results = processRecipeOutputs(millingRecipe.get(), inventory);
            if (!results.isEmpty()) {
                dropItems(results, breakingPos);
            } else {
                BlockHelper.destroyBlock(getLevel(), breakingPos, 1f, (stack) -> {});
            }
        } else {
            BlockHelper.destroyBlock(getLevel(), breakingPos, 1f, (stack) -> {});
        }


    }

    private <T extends ProcessingRecipe<RecipeWrapper>> List<ItemStack> processRecipeOutputs(T recipe, ItemStackHandler inventory) {
        List<ItemStack> results = new ArrayList<>();
        int rolls = inventory.getStackInSlot(0).getCount();

        for (int roll = 0; roll < rolls; roll++) {
            List<ItemStack> rolledResults = recipe.rollResults();
            results.addAll(rolledResults);
        }
        return results;
    }

    private void dropItems(List<ItemStack> results, BlockPos breakingPos) {
        Vec3 vec = VecHelper.offsetRandomly(VecHelper.getCenterOf(breakingPos), Objects.requireNonNull(getLevel()).random, .125f);

        BlockHelper.destroyBlock(getLevel(), breakingPos, 1f, (stack) -> {});

        for (ItemStack resultStack : results) {
            if (!resultStack.isEmpty()) {
                ItemEntity itemEntity = new ItemEntity(getLevel(), vec.x, vec.y, vec.z, resultStack);
                itemEntity.setDefaultPickUpDelay();
                itemEntity.setDeltaMovement(Vec3.ZERO);
                getLevel().addFreshEntity(itemEntity);
            }
        }
    }


    private void performAutoSmelting(BlockPos breakingPos, BlockState stateToBreak) {
        RecipeManager recipeManager = Objects.requireNonNull(getLevel()).getRecipeManager();
        ItemStack blockItem = new ItemStack(stateToBreak.getBlock().asItem());
        RecipeWrapper inventory = new RecipeWrapper(new ItemStackHandler(1));
        inventory.setItem(0, blockItem);

        Optional<SmeltingRecipe> smeltingRecipe = recipeManager.getRecipeFor(RecipeType.SMELTING, inventory, getLevel());

        if (smeltingRecipe.isPresent()) {
            ItemStack smeltedResult = smeltingRecipe.get().getResultItem();
            Vec3 vec = VecHelper.offsetRandomly(VecHelper.getCenterOf(breakingPos), getLevel().random, .125f);

            BlockHelper.destroyBlock(getLevel(), breakingPos, 1f, (stack) -> {
                if (stack.isEmpty() || !getLevel().getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS)) return;
                if (getLevel().restoringBlockSnapshots) return;

                ItemEntity itemEntity = new ItemEntity(getLevel(), vec.x, vec.y, vec.z, smeltedResult);
                itemEntity.setDefaultPickUpDelay();
                itemEntity.setDeltaMovement(Vec3.ZERO);
                getLevel().addFreshEntity(itemEntity);
            });
        } else {
            defaultBlockBreaking(stateToBreak);
        }
    }
}