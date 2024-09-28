package uwu.lopyluna.create_dd.content.blocks.kinetics.multimeter;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;

public class GaugeObservedPacket extends CustomBlockEntityConfigurationPacket<MultiMeterBlockEntity> {

	public GaugeObservedPacket(BlockPos pos) {
		super(pos);
	}

	public GaugeObservedPacket(FriendlyByteBuf buffer) {
		super(buffer);
	}

	@Override
	protected void writeSettings(FriendlyByteBuf buffer) {}

	@Override
	protected void readSettings(FriendlyByteBuf buffer) {}

	@Override
	protected void applySettings(MultiMeterBlockEntity be) {
		be.onObserved();
	}
	
	@Override
	protected boolean causeUpdate() {
		return false;
	}

}
