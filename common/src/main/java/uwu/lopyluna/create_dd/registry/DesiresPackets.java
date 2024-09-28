package uwu.lopyluna.create_dd.registry;

import com.simibubi.create.foundation.mixin.fabric.BlockableEventLoopAccessor;
import com.simibubi.create.foundation.networking.SimplePacketBase;
import com.simibubi.create.foundation.networking.SimplePacketBase.Context;

import dev.architectury.networking.NetworkChannel;
import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.NetworkManager.PacketContext;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import uwu.lopyluna.create_dd.DesireUtil;
import uwu.lopyluna.create_dd.content.blocks.kinetics.multimeter.GaugeObservedPacket;
import uwu.lopyluna.create_dd.content.items.equipment.InvertFunctionPacket;
import uwu.lopyluna.create_dd.content.items.equipment.block_zapper.ConfigureBlockZapperPacket;
import uwu.lopyluna.create_dd.content.packet.SConfigureConfigPacket;
import uwu.lopyluna.create_dd.infrastructure.ponder.PacketBase;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static dev.architectury.networking.NetworkManager.Side.C2S;
import static dev.architectury.networking.NetworkManager.Side.S2C;

@SuppressWarnings({"unused"})
public enum DesiresPackets {

	// Client to Server
	OBSERVER_GAUGEOMETER(GaugeObservedPacket.class, GaugeObservedPacket::new, C2S),

	CONFIGURE_BLOCKZAPPER(ConfigureBlockZapperPacket.class, ConfigureBlockZapperPacket::new, C2S),
	INVERT_CONFIGURE(InvertFunctionPacket.class, InvertFunctionPacket::new, C2S),
	//TOOLBOX_EQUIP(ToolboxEquipPacket.class, ToolboxEquipPacket::new, C2S),
	//TOOLBOX_DISPOSE_ALL(ToolboxDisposeDesiresPacket.class, ToolboxDisposeDesiresPacket::new, C2S),

	// Server to Client
	S_CONFIGURE_CONFIG(SConfigureConfigPacket.class, SConfigureConfigPacket::new, S2C),
	//BEAM_EFFECT(ZapperBeamPacket.class, ZapperBeamPacket::new, S2C),
	//POTATO_CANNON(PotatoCannonPacket.class, PotatoCannonPacket::new, S2C),
	//SYNC_POTATO_PROJECTILE_TYPES(PotatoProjectileTypeManager.SyncPacket.class,
	//	PotatoProjectileTypeManager.SyncPacket::new, S2C),
	;

	public static final ResourceLocation CHANNEL_NAME = DesireUtil.asResource("main");
	public static final int NETWORK_VERSION = 3;
	public static final String NETWORK_VERSION_STR = String.valueOf(NETWORK_VERSION);
	private static NetworkChannel channel;

	private final PacketType<?> packetType;

	<T extends PacketBase> DesiresPackets(Class<T> type, Function<FriendlyByteBuf, T> factory,
		NetworkManager.Side direction) {
		packetType = new PacketType<>(type, factory, direction);
	}

	public static void registerPackets() {
		channel = NetworkChannel.create(CHANNEL_NAME);
			// .serverAcceptedVersions(NETWORK_VERSION_STR::equals)
			// .clientAcceptedVersions(NETWORK_VERSION_STR::equals)
			// .networkProtocolVersion(() -> NETWORK_VERSION_STR)
			// .simpleChannel();

		for (DesiresPackets packet : values())
			packet.packetType.register();
	}

	public static NetworkChannel getChannel() {
		return channel;
	}

	public static void sendToNear(Level world, BlockPos pos, int range, Object message) {
		getChannel().send(
			PacketDistributor.NEAR.with(TargetPoint.p(pos.getX(), pos.getY(), pos.getZ(), range, world.dimension())),
			message);
	}

	private static class PacketType<T extends PacketBase> {
		private static int index = 0;

		private final BiConsumer<T, FriendlyByteBuf> encoder;
		private final Function<FriendlyByteBuf, T> decoder;
		private final BiConsumer<T, Supplier<PacketContext>> handler;
		private final Class<T> type;
		private final NetworkManager.Side direction;

		private PacketType(Class<T> type, Function<FriendlyByteBuf, T> factory, NetworkManager.Side direction) {
			encoder = T::write;
			decoder = factory;
			handler = (packet, contextSupplier) -> {
				PacketContext context = contextSupplier.get();
				if (packet.handle(context)) {
					// context.setPacketHandled(true); // TODO: Is this forge only?
				}
			};
			this.type = type;
			this.direction = direction;
		}

		private void register() {
			// getChannel().messageBuilder(type, index++, direction)
			// 	.encoder(encoder)
			// 	.decoder(decoder)
			// 	.consumerNetworkThread(handler)
			// 	.add();

			getChannel().register(type, encoder, decoder, handler);
		}
	}

}
