package uwu.lopyluna.create_dd.content.items.equipment.block_zapper;

import com.simibubi.create.AllSpecialTextures;
import com.simibubi.create.CreateClient;
import com.simibubi.create.foundation.utility.NBTHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraft.world.phys.Vec3;
import uwu.lopyluna.create_dd.content.items.equipment.block_zapper.additional.*;
import uwu.lopyluna.create_dd.registry.DesiresItems;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Supplier;

public class BlockZapperRenderHandler {

	private static Supplier<Collection<BlockPos>> renderedPositions;

	public static void tick() {
		gatherSelectedBlocks();
		if (renderedPositions == null)
			return;

		CreateClient.OUTLINER.showCluster("terrainZapper", renderedPositions.get())
				.colored(0xbfbfbf)
				.disableLineNormals()
				.lineWidth(1 / 32f)
				.withFaceTexture(AllSpecialTextures.CHECKERED);
	}

	protected static void gatherSelectedBlocks() {
		LocalPlayer player = Minecraft.getInstance().player;
        assert player != null;
        ItemStack heldMain = player.getMainHandItem();
		ItemStack heldOff = player.getOffhandItem();
		boolean zapperInMain = DesiresItems.BLOCK_ZAPPER.isIn(heldMain);
		boolean zapperInOff = DesiresItems.BLOCK_ZAPPER.isIn(heldOff);

		if (zapperInMain) {
			CompoundTag tag = heldMain.getOrCreateTag();
			if (!tag.contains("_Swap") || !zapperInOff) {
				createBrushOutline(tag, player, heldMain);
				return;
			}
		}

		if (zapperInOff) {
			CompoundTag tag = heldOff.getOrCreateTag();
			createBrushOutline(tag, player, heldOff);
			return;
		}

		renderedPositions = null;
	}

	public static void createBrushOutline(CompoundTag tag, LocalPlayer player, ItemStack zapper) {
		if (!tag.contains("BrushParams")) {
			renderedPositions = null;
			return;
		}

		CompoundTag nbt = zapper.getOrCreateTag();
		Brush brush = NBTHelper.readEnum(tag, "Brush", TerrainBrushes.class)
			.get();
		PlacementOptions placement = NBTHelper.readEnum(tag, "Placement", PlacementOptions.class);
		TerrainTools tool = NBTHelper.readEnum(tag, "Tool", TerrainTools.class);
		BlockPos params = NbtUtils.readBlockPos(tag.getCompound("BrushParams"));
		brush.set(params.getX(), params.getY(), params.getZ(), zapper.getItem() instanceof BlockZapperItem item && item.getSizeModifier(nbt));

		Vec3 start = player.position()
			.add(0, player.getEyeHeight(), 0);
		Vec3 range = player.getLookAngle()
			.scale(BlockZapperItem.getZappingRangeValue(zapper));
		BlockHitResult raytrace = player.level
			.clip(new ClipContext(start, start.add(range), Block.OUTLINE, Fluid.NONE, player));
		if (raytrace.getType() == Type.MISS) {
			renderedPositions = null;
			return;
		}

		BlockPos pos = raytrace.getBlockPos()
			.offset(brush.getOffset(player.getLookAngle(), raytrace.getDirection(), placement));
		renderedPositions =
			() -> brush.addToGlobalPositions(player.level, pos, raytrace.getDirection(), new ArrayList<>(), tool);
	}

}
