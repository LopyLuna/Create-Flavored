package uwu.lopyluna.create_dd.content.items.equipment;

import dev.architectury.networking.NetworkManager.PacketContext;
import net.fabricmc.api.EnvType;
import net.minecraft.network.FriendlyByteBuf;
import uwu.lopyluna.create_dd.infrastructure.config.DesiresConfigs;
import uwu.lopyluna.create_dd.infrastructure.ponder.PacketBase;

public class InvertFunctionPacket extends PacketBase {

    public InvertFunctionPacket() {}
    public InvertFunctionPacket(FriendlyByteBuf buffer) {}

    @Override
    public void write(FriendlyByteBuf buffer) {}

    @Override
    public boolean handle(PacketContext context) {
        if (context.getEnv() != EnvType.SERVER)
            return false;

        context.queue(() -> {
            DesiresConfigs.client().invertDeforesterSawFunction.get();
            DesiresConfigs.client().invertExcavationDrillFunction.get();
        });

        return true;
    }
}
