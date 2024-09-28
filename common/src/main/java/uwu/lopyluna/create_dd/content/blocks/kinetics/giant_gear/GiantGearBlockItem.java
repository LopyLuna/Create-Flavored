package uwu.lopyluna.create_dd.content.blocks.kinetics.giant_gear;

import com.simibubi.create.CreateClient;
import com.simibubi.create.content.kinetics.base.RotatedPillarKineticBlock;
import com.simibubi.create.content.kinetics.simpleRelays.ICogWheel;
import com.simibubi.create.foundation.placement.IPlacementHelper;
import com.simibubi.create.foundation.placement.PlacementHelpers;
import com.simibubi.create.foundation.placement.PlacementOffset;
import com.simibubi.create.foundation.utility.Color;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import uwu.lopyluna.create_dd.DesireClient;
import uwu.lopyluna.create_dd.registry.DesiresBlocks;
import uwu.lopyluna.create_dd.registry.helper.Lang;
import com.simibubi.create.foundation.utility.Pair;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class GiantGearBlockItem extends BlockItem {
    
    public final int smallPlacementHelperId = PlacementHelpers.register(new GiantGearToSmallGearHelper());
    public final int bigPlacementHelperId = PlacementHelpers.register(new GiantGearToBigGearHelper());
    
    public GiantGearBlockItem(Block pBlock, Properties pProperties) {
        super(pBlock, pProperties);
    }
    
    @Override
    public InteractionResult useOn(UseOnContext context) {
        InteractionResult smallCogPlacementHelper = usePlacementHelper(smallPlacementHelperId, context.getItemInHand(), context);
        if (smallCogPlacementHelper != null)
            return smallCogPlacementHelper;
        
        InteractionResult bigCogPlacementHelper = usePlacementHelper(bigPlacementHelperId, context.getItemInHand(), context);
        if (bigCogPlacementHelper != null)
            return bigCogPlacementHelper;
        
        return super.useOn(context);
    }
    
    private InteractionResult usePlacementHelper(int placementHelperId, ItemStack stack, UseOnContext context) {
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = world.getBlockState(pos);
        Player player = context.getPlayer();
        
        //Placement helpers on other blocks
        IPlacementHelper helper = PlacementHelpers.get(placementHelperId);
        BlockHitResult ray = new BlockHitResult(context.getClickLocation(), context.getClickedFace(), context.getClickedPos(), true);
        if (helper.matchesState(state) && player != null && !player.isShiftKeyDown()) {
            Direction.Axis axis = state.getValue(RotatedPillarKineticBlock.AXIS);
            
            PlacementOffset offset = helper.getOffset(player, world, state, pos, ray);
            
            if (!GiantGearBlock.isClearForStructure(axis, offset.getBlockPos(), context.getLevel())) {
                if (context.getLevel().isClientSide())showBounds(context.getPlayer(), offset.getBlockPos(), axis);
                return InteractionResult.FAIL;
            }
            
            return offset
                .placeInWorld(world, this, player, context.getHand(), ray);
        }
        
        return null;
    }
    
    @Override
    public @NotNull InteractionResult place(@NotNull BlockPlaceContext ctx) {
        //Base placement
        InteractionResult result = super.place(ctx);
        if (result != InteractionResult.FAIL)
            return result;
        
        //Try again, but on a diff axis since last failed
        Direction clickedFace = ctx.getClickedFace();
        if (clickedFace.getAxis() != ((GiantGearBlock) getBlock()).getAxisForPlacement(ctx))
            result = super.place(BlockPlaceContext.at(ctx, ctx.getClickedPos()
                    .relative(clickedFace), clickedFace));
        
        //Still failed, so show bounds
        if (result == InteractionResult.FAIL && ctx.getLevel()
                .isClientSide())
            showBounds(ctx.getPlayer(), ctx.getClickedPos(), ((GiantGearBlock) getBlock()).getAxisForPlacement(ctx));
        
        return result;
    }

    @Environment(EnvType.CLIENT)
    public void showBounds(Player player, BlockPos pos, Direction.Axis axis) {
        if (!(player instanceof LocalPlayer localPlayer))
            return;
        
        List<Direction.Axis> perpendicularDirections = Arrays.stream(Direction.Axis.values()).filter(other -> other != axis).toList();
        
        CreateClient.OUTLINER.showAABB(Pair.of("waterwheel-core", pos), new AABB(
                pos
                    .relative(perpendicularDirections.get(0), -2)
                    .relative(perpendicularDirections.get(1), -2),
                pos
                    .relative(perpendicularDirections.get(0), 2)
                    .relative(perpendicularDirections.get(1), 2)
                    .offset(1, 1, 1)
            ))
            .colored(0xFF_ff5d6c);
        
        for (int i = 0; i < 2; i++) {
            Direction.Axis primaryAxis = perpendicularDirections.get(i);
            Direction.Axis secondaryAxis = perpendicularDirections.get(i == 0 ? 1 : 0);
            
            CreateClient.OUTLINER.showAABB(Pair.of("waterwheel-"+primaryAxis.getName(), pos), new AABB(
                    pos
                        .relative(primaryAxis, -3)
                        .relative(secondaryAxis, -1),
                    pos
                        .relative(primaryAxis, 3)
                        .relative(secondaryAxis, 1)
                        .offset(1, 1, 1)
                ))
                .colored(0xFF_ff5d6c);
        }
        
        Lang.translate("large_water_wheel.not_enough_space")
                .color(0xFF_ff5d6c)
                .sendStatus(localPlayer);
    }
    
    public static PlacementOffset getClosestGiantGearPlacementOffsets(
        Player player, Level world, BlockState state, BlockPos pos, BlockHitResult ray,
        boolean isForSmallCogConnection
    ) {
        Direction.Axis axis = state.getValue(RotatedPillarKineticBlock.AXIS);
        BlockPos origin = ray.getBlockPos();
        
        List<BlockPos> potentialGiantGearLocations = GiantGearBlockEntity.collectConnectionPositions(
            origin, axis, isForSmallCogConnection, !isForSmallCogConnection
        );
        
        BlockPos closestPos = null;
        double closestDistSqr = 0;
        
        for (BlockPos location : potentialGiantGearLocations) {
            Vec3 center = Vec3.atCenterOf(location);
            double distSqr = center.distanceToSqr(ray.getLocation());
            if (closestPos == null || distSqr < closestDistSqr) {
                closestPos = location;
                closestDistSqr = distSqr;
            }
        }
        return PlacementOffset.success(closestPos, resultState -> resultState.setValue(GiantGearBlock.AXIS, axis));
    }
    
    private static class GiantGearToSmallGearHelper implements IPlacementHelper {
        
        @Override
        public Predicate<ItemStack> getItemPredicate() {
            return stack -> stack.is(DesiresBlocks.GIANT_GEAR.get().asItem());
        }
        
        @Override
        public Predicate<BlockState> getStatePredicate() {
            return ICogWheel::isSmallCog;
        }
        
        @Override
        public PlacementOffset getOffset(Player player, Level world, BlockState state, BlockPos pos, BlockHitResult ray) {
            return getClosestGiantGearPlacementOffsets(player, world, state, pos, ray, true);
        }
        
    }
    
    private static class GiantGearToBigGearHelper implements IPlacementHelper {
        
        @Override
        public Predicate<ItemStack> getItemPredicate() {
            return stack -> stack.is(DesiresBlocks.GIANT_GEAR.get().asItem());
        }
        
        @Override
        public Predicate<BlockState> getStatePredicate() {
            return ICogWheel::isLargeCog;
        }
        
        @Override
        public PlacementOffset getOffset(Player player, Level world, BlockState state, BlockPos pos, BlockHitResult ray) {
            return getClosestGiantGearPlacementOffsets(player, world, state, pos, ray, false);
        }
        
    }
    
}
