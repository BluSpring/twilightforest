package twilightforest.client.event;

import io.github.fabricators_of_create.porting_lib.event.client.FogEvents;
import io.github.fabricators_of_create.porting_lib.level.events.LevelEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.level.material.FogType;
import twilightforest.client.TwilightForestRenderInfo;
import twilightforest.init.TFBiomes;

import org.jetbrains.annotations.Nullable;

public class FogHandler {

	private static boolean SKY_CHUNK_LOADED = false;

	private static float SKY_FAR = 0.0F;
	private static float SKY_NEAR = 0.0F;

	private static boolean TERRAIN_CHUNK_LOADED = false;

	private static float TERRAIN_FAR = 0.0F;
	private static float TERRAIN_NEAR = 0.0F;

	protected static void renderFog() {
		FogEvents.RENDER_FOG.register((mode, type, camera, partialTick, renderDistance, nearDistance, farDistance, shape, fogData) -> {
			if (type.equals(FogType.NONE) && Minecraft.getInstance().cameraEntity instanceof LocalPlayer player && player.level() instanceof ClientLevel clientLevel && clientLevel.effects() instanceof TwilightForestRenderInfo) {
				if (mode.equals(FogRenderer.FogMode.FOG_SKY)) {
					if (SKY_CHUNK_LOADED) {
						boolean spooky = isSpooky(clientLevel, player);

						float far = spooky ? farDistance * 0.5F : farDistance;
						float near = spooky ? 0.0F : nearDistance;

						SKY_FAR = Mth.lerp(0.003F, SKY_FAR, far);
						SKY_NEAR = Mth.lerp(0.003F * (SKY_NEAR < near ? 0.5F : 2.0F), SKY_NEAR, near);

						fogData.setFarPlaneDistance(SKY_FAR);
						fogData.setNearPlaneDistance(SKY_NEAR);

						return true;
					} else if (clientLevel.isLoaded(player.blockPosition())) { //We do a first-time set up after the chunk the player is in is loaded
						SKY_CHUNK_LOADED = true;
						SKY_FAR = isSpooky(clientLevel, player) ? farDistance * 0.5F : farDistance;
						SKY_NEAR = isSpooky(clientLevel, player) ? 0.0F : nearDistance;
					}
				} else {
					if (TERRAIN_CHUNK_LOADED) {
						boolean spooky = isSpooky(clientLevel, player);

						float far = spooky ? farDistance * 0.5F : farDistance;
						float near = spooky ? far * 0.75F : nearDistance;

						TERRAIN_FAR = Mth.lerp(0.003F, TERRAIN_FAR, far);
						TERRAIN_NEAR = Mth.lerp(0.003F * (TERRAIN_NEAR < near ? 0.5F : 2.0F), TERRAIN_NEAR, near);

						fogData.setFarPlaneDistance(TERRAIN_FAR);
						fogData.setNearPlaneDistance(TERRAIN_NEAR);

						return true;
					} else if (SKY_CHUNK_LOADED || clientLevel.isLoaded(player.blockPosition())) { //SKY is always called first in vanilla, so we only need to check if the SKY flag is true, but just in case
						TERRAIN_CHUNK_LOADED = true;
						TERRAIN_FAR = isSpooky(clientLevel, player) ? farDistance * 0.5F : farDistance;
						TERRAIN_NEAR = isSpooky(clientLevel, player) ? TERRAIN_FAR * 0.75F : nearDistance;
					}
				}
			}

			return false;
		});
	}

	protected static void unloadFog() { //As supernatural as the fog is, it shouldn't follow the player between worlds
		LevelEvent.Unload.EVENT.register(event -> {
			SKY_CHUNK_LOADED = false;
			TERRAIN_CHUNK_LOADED = false;
		});
	}

	private static boolean isSpooky(@Nullable ClientLevel level, @Nullable LocalPlayer player) {
		return level != null && player != null && level.getBiome(player.blockPosition()).is(TFBiomes.SPOOKY_FOREST);
	}
}
