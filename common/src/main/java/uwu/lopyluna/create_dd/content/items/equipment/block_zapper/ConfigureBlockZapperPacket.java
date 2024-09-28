package uwu.lopyluna.create_dd.content.items.equipment.block_zapper;

import com.simibubi.create.content.equipment.zapper.PlacementPatterns;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import uwu.lopyluna.create_dd.content.items.equipment.block_zapper.additional.*;

public class ConfigureBlockZapperPacket extends CustomConfigureZapperPacket {

	protected TerrainBrushes brush;
	protected int brushParamX;
	protected int brushParamY;
	protected int brushParamZ;
	protected TerrainTools tool;
	protected PlacementOptions placement;

	public ConfigureBlockZapperPacket(InteractionHand hand, PlacementPatterns pattern, TerrainBrushes brush, int brushParamX, int brushParamY, int brushParamZ, TerrainTools tool, PlacementOptions placement) {
		super(hand, pattern);
		this.brush = brush;
		this.brushParamX = brushParamX;
		this.brushParamY = brushParamY;
		this.brushParamZ = brushParamZ;
		this.tool = tool;
		this.placement = placement;
	}

	public ConfigureBlockZapperPacket(FriendlyByteBuf buffer) {
		super(buffer);
		brush = buffer.readEnum(TerrainBrushes.class);
		brushParamX = buffer.readVarInt();
		brushParamY = buffer.readVarInt();
		brushParamZ = buffer.readVarInt();
		tool = buffer.readEnum(TerrainTools.class);
		placement = buffer.readEnum(PlacementOptions.class);
	}

	@Override
	public void write(FriendlyByteBuf buffer) {
		super.write(buffer);
		buffer.writeEnum(brush);
		buffer.writeVarInt(brushParamX);
		buffer.writeVarInt(brushParamY);
		buffer.writeVarInt(brushParamZ);
		buffer.writeEnum(tool);
		buffer.writeEnum(placement);
	}

	@Override
	public void configureZapper(ItemStack stack) {
		BlockZapperItem.configureSettings(stack, pattern, brush, brushParamX, brushParamY, brushParamZ, tool, placement);
	}

}
