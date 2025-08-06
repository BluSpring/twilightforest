package twilightforest.events;

import io.github.fabricators_of_create.porting_lib.entity.events.living.LivingHurtEvent;
import io.github.fabricators_of_create.porting_lib.entity.events.player.PlayerEvents;
import io.github.fabricators_of_create.porting_lib.entity.events.tick.EntityTickEvent;
import io.github.fabricators_of_create.porting_lib.entity.events.tick.PlayerTickEvent;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.levelgen.Heightmap;
import twilightforest.components.entity.FortificationShieldAttachment;
import twilightforest.config.TFConfig;
import twilightforest.init.TFDataAttachments;
import twilightforest.init.TFDimension;
import twilightforest.network.UpdateShieldPacket;
import twilightforest.world.NoReturnTeleporter;
import twilightforest.world.TFTeleporter;

public class CapabilityEvents {
	public static void init() {
		EntityTickEvent.Post.EVENT.register(event -> {
			updateShields(event);
		});

		PlayerTickEvent.Post.EVENT.register(event -> updatePlayerCaps(event));

		LivingHurtEvent.EVENT.register(event -> livingAttack(event));

		dev.architectury.event.events.common.PlayerEvent.PLAYER_RESPAWN.register((newPlayer, conqueredEnd, removalReason) -> {
			onPlayerRespawn(newPlayer);
		});

		PlayerEvents.PlayerLoggedInEvent.EVENT.register(event -> playerLogsIn(event));

		dev.architectury.event.events.common.PlayerEvent.CHANGE_DIMENSION.register((player, oldLevel, newLevel) -> playerPortals(player));

		PlayerEvents.StartTracking.EVENT.register(event -> onStartTracking(event));
	}

	public static void updateShields(EntityTickEvent.Post event) {
		if (event.getEntity() instanceof LivingEntity living && !living.level().isClientSide() && living.hasAttached(TFDataAttachments.FORTIFICATION_SHIELDS.get())) {
			event.getEntity().getAttachedOrCreate(TFDataAttachments.FORTIFICATION_SHIELDS.get()).tick(living);
		}
	}

	public static void updatePlayerCaps(PlayerTickEvent.Post event) {
		if (event.getEntity().getAttachedOrCreate(TFDataAttachments.FEATHER_FAN.get())) {
			event.getEntity().setIgnoreFallDamageFromCurrentImpulse(true);
			event.getEntity().currentImpulseImpactPos = event.getEntity().position();

			if (event.getEntity().onGround() || event.getEntity().isSwimming() || event.getEntity().isInWater()) {
				event.getEntity().setAttached(TFDataAttachments.FEATHER_FAN.get(), false);
			}
		}
		event.getEntity().getAttachedOrCreate(TFDataAttachments.YETI_THROWING.get()).tick(event.getEntity());
		event.getEntity().getAttachedOrCreate(TFDataAttachments.TF_PORTAL_COOLDOWN.get()).tick(event.getEntity());
	}

	public static void livingAttack(LivingHurtEvent event) {
		LivingEntity living = event.getEntity();
		// shields
		if (!living.level().isClientSide() && !event.getSource().is(DamageTypeTags.BYPASSES_ARMOR)) {
            FortificationShieldAttachment attachment = living.getAttachedOrCreate(TFDataAttachments.FORTIFICATION_SHIELDS.get());
			if (attachment.shieldsLeft() > 0) {
				if (living.invulnerableTime <= 0) {
					attachment.breakShield(living, false);
					FortificationShieldAttachment.addShieldBreakParticles(event.getSource(), living);
					living.invulnerableTime = living.invulnerableDuration;
				}
				event.setCanceled(true);
			}
		}
	}

	public static void onPlayerRespawn(ServerPlayer serverPlayer) {
		if (serverPlayer.getRespawnPosition() == null) {
			newSpawnInTwilightForest(serverPlayer);
		}
	}

	/**
	 * When player logs in, report conflict status, set progression status
	 */
	public static void playerLogsIn(PlayerEvents.PlayerLoggedInEvent event) {
		if (event.getEntity().level().isClientSide() || !(event.getEntity() instanceof ServerPlayer player))
			return;
		updateCapabilities(player, event.getEntity());
		dataFixLegacyBanish(player);
		if (!player.hasAttached(TFDataAttachments.BANISHED_TO_TWILIGHT_FOREST.get()))
			newSpawnInTwilightForest(player);
	}

	public static void playerPortals(ServerPlayer player) {
		updateCapabilities(player, player);
	}

	public static void onStartTracking(PlayerEvents.StartTracking event) {
		updateCapabilities((ServerPlayer) event.getEntity(), event.getTarget());
	}

	// send any capabilities that are needed client-side
	private static void updateCapabilities(ServerPlayer clientTarget, Entity shielded) {
		var attachment = shielded.getAttachedOrCreate(TFDataAttachments.FORTIFICATION_SHIELDS.get());
		if (attachment.shieldsLeft() > 0) {
			ServerPlayNetworking.send(clientTarget, new UpdateShieldPacket(shielded.getId(), attachment.temporaryShieldsLeft(), attachment.permanentShieldsLeft()));
		}
	}

	private static void newSpawnInTwilightForest(ServerPlayer player) {
		if (!TFConfig.newPlayersSpawnInTF)
			return;
		ServerLevel level = player.getServer().getLevel(TFDimension.DIMENSION_KEY);
		if (level == null)
			return;

		BlockPos newDefaultSpawn = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, player.blockPosition());

		player.changeDimension(TFConfig.portalForNewPlayerSpawn ?
			TFTeleporter.createTransition(player, level, newDefaultSpawn, true) :
			NoReturnTeleporter.createNoPortalTransition(level, player, newDefaultSpawn));
		player.setRespawnPosition(TFDimension.DIMENSION_KEY, newDefaultSpawn, player.getYRot(), true, false);

		player.setAttached(TFDataAttachments.BANISHED_TO_TWILIGHT_FOREST.get(), Unit.INSTANCE);
	}

	private static void dataFixLegacyBanish(ServerPlayer player) {
		CompoundTag tagCompound = player.getCustomData();
		if (!tagCompound.contains("PlayerPersisted"))
			return;
		CompoundTag playerData = tagCompound.getCompound("PlayerPersisted");
		if (!playerData.contains("twilightforest_banished"))
			return;

		playerData.remove("twilightforest_banished");
		tagCompound.put("PlayerPersisted", playerData);

		if (player.hasAttached(TFDataAttachments.BANISHED_TO_TWILIGHT_FOREST.get()))
			return;

		player.setAttached(TFDataAttachments.BANISHED_TO_TWILIGHT_FOREST.get(), Unit.INSTANCE);
	}
}
