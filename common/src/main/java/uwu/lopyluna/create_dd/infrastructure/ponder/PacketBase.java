package uwu.lopyluna.create_dd.infrastructure.ponder;

import dev.architectury.networking.NetworkManager.PacketContext;
import net.minecraft.network.FriendlyByteBuf;

public abstract class PacketBase {
    public abstract void write(FriendlyByteBuf buffer);
	
    public boolean handle(PacketContext context) {
        return true;
    }

	public final void encode(FriendlyByteBuf buffer) {
		write(buffer);
	}
}
