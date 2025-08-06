package twilightforest.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.loader.api.FabricLoader;
import twilightforest.client.event.ClientEvents;
import twilightforest.client.event.RegistrationEvents;
import twilightforest.compat.curios.TrinketsCompat;

public class TwilightForestClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		RegistrationEvents.initModBusEvents();
		ClientEvents.initGameEvents();

		if (FabricLoader.getInstance().isModLoaded("trinkets")) {
			TrinketsCompat.registerCurioLayers();
			TrinketsCompat.registerCurioRenderers();
		}
	}
}
