package twilightforest.network;

import io.netty.buffer.ByteBuf;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.function.BiConsumer;

public class PayloadRegistrar {
	public <T extends CustomPacketPayload> void playToClient(CustomPacketPayload.Type<T> type, StreamCodec<? super RegistryFriendlyByteBuf, T> codec, BiConsumer<T, IPayloadContext> consumer) {
		PayloadTypeRegistry.playS2C().register(type, codec);

		if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
			ClientHandler.registerClient(type, consumer);
		}
	}

	private static class ClientHandler {
		private static <T extends CustomPacketPayload> void registerClient(CustomPacketPayload.Type<T> type, BiConsumer<T, IPayloadContext> consumer) {
			ClientPlayNetworking.registerGlobalReceiver(type, (payload, ctx) -> {
				consumer.accept(payload, new IPayloadContext.Impl(IPayloadContext.Flow.CLIENTBOUND, ctx.player()));
			});
		}
	}

	public <T extends CustomPacketPayload> void playToServer(CustomPacketPayload.Type<T> type, StreamCodec<? super RegistryFriendlyByteBuf, T> codec, BiConsumer<T, IPayloadContext> consumer) {
		PayloadTypeRegistry.playC2S().register(type, codec);

		ServerPlayNetworking.registerGlobalReceiver(type, (payload, ctx) -> {
			consumer.accept(payload, new IPayloadContext.Impl(IPayloadContext.Flow.SERVERBOUND, ctx.player()));
		});
	}
}
