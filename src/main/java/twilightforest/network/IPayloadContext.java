package twilightforest.network;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;

public interface IPayloadContext {
	void enqueueWork(Runnable runnable);
	Flow flow();
	Player player();

	enum Flow {
		CLIENTBOUND, SERVERBOUND;

		public boolean isClientbound() {
			return this == CLIENTBOUND;
		}

		public boolean isServerbound() {
			return this == SERVERBOUND;
		}
	}

	record Impl(Flow flow, Player player) implements IPayloadContext {
		@Override
		public void enqueueWork(Runnable runnable) {
			if (this.flow().isServerbound()) {
				this.player().level().getServer().execute(runnable);
			} else {
				enqueueWorkClient(runnable);
			}
		}

		private void enqueueWorkClient(Runnable runnable) {
			Minecraft.getInstance().execute(runnable);
		}
	}
}
