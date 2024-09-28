package uwu.lopyluna.create_dd.content.items.equipment.block_zapper;

import com.simibubi.create.content.equipment.zapper.PlacementPatterns;
import com.simibubi.create.content.equipment.zapper.ZapperItem;

import dev.architectury.networking.NetworkManager.PacketContext;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import uwu.lopyluna.create_dd.infrastructure.ponder.PacketBase;

public abstract class CustomConfigureZapperPacket extends PacketBase {
    protected InteractionHand hand;
	protected PlacementPatterns pattern;

	public CustomConfigureZapperPacket(InteractionHand hand, PlacementPatterns pattern) {
		this.hand = hand;
		this.pattern = pattern;
	}

	public CustomConfigureZapperPacket(FriendlyByteBuf buffer) {
		hand = buffer.readEnum(InteractionHand.class);
		pattern = buffer.readEnum(PlacementPatterns.class);
	}

	@Override
	public void write(FriendlyByteBuf buffer) {
		buffer.writeEnum(hand);
		buffer.writeEnum(pattern);
	}

	@Override
	public boolean handle(PacketContext context) {
		context.queue(() -> {
			ServerPlayer player = (ServerPlayer) context.getPlayer();
			
            if (player == null) {
				return;
			}
			
            ItemStack stack = player.getItemInHand(hand);

			if (stack.getItem() instanceof ZapperItem) {
				configureZapper(stack);
			}
		});

		return true;
	}

	public abstract void configureZapper(ItemStack stack);
}
