package uwu.lopyluna.create_dd.content.blocks.kinetics.multimeter;

import com.simibubi.create.foundation.blockEntity.SyncedBlockEntity;

import dev.architectury.networking.NetworkManager.PacketContext;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import uwu.lopyluna.create_dd.infrastructure.ponder.PacketBase;

public abstract class CustomBlockEntityConfigurationPacket<BE extends SyncedBlockEntity> extends PacketBase {
    protected BlockPos pos;

	public CustomBlockEntityConfigurationPacket(FriendlyByteBuf buffer) {
		pos = buffer.readBlockPos();
		readSettings(buffer);
	}

	public CustomBlockEntityConfigurationPacket(BlockPos pos) {
		this.pos = pos;
	}

	@Override
	public void write(FriendlyByteBuf buffer) {
		buffer.writeBlockPos(pos);
		writeSettings(buffer);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean handle(PacketContext context) {
		context.queue(() -> {
			ServerPlayer player = (ServerPlayer) context.getPlayer();
			if (player == null)
				return;
			Level world = player.level;
			if (world == null || !world.isLoaded(pos))
				return;
			if (!pos.closerThan(player.blockPosition(), maxRange()))
				return;
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof SyncedBlockEntity) {
				applySettings(player, (BE) blockEntity);
				if (!causeUpdate())
					return;
				((SyncedBlockEntity) blockEntity).sendData();
				blockEntity.setChanged();
			}
		});
		return true;
	}

	protected int maxRange() {
		return 20;
	}

	protected abstract void writeSettings(FriendlyByteBuf buffer);

	protected abstract void readSettings(FriendlyByteBuf buffer);

	protected void applySettings(ServerPlayer player, BE be) {
		applySettings(be);
	}

	protected boolean causeUpdate() {
		return true;
	}

	protected abstract void applySettings(BE be);
}
