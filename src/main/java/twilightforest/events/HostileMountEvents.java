package twilightforest.events;

import io.github.fabricators_of_create.porting_lib.entity.events.EntityMountEvent;
import io.github.fabricators_of_create.porting_lib.entity.events.EntityTeleportEvent;
import io.github.fabricators_of_create.porting_lib.entity.events.living.LivingHurtEvent;
import io.github.fabricators_of_create.porting_lib.entity.events.tick.EntityTickEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import twilightforest.TwilightForestMod;
import twilightforest.entity.IHostileMount;
import twilightforest.init.TFDamageTypes;
import twilightforest.init.TFDataAttachments;

public class HostileMountEvents {

	public static volatile boolean allowDismount = false;

	public static void init() {
		LivingHurtEvent.EVENT.register(event -> entityHurts(event));
		EntityTeleportEvent.EnderEntity.EVENT.register(event -> entityTeleports(event));
		EntityTeleportEvent.TeleportCommand.EVENT.register(event -> entityTeleports(event));
		EntityTeleportEvent.EnderPearl.EVENT.register(event -> entityTeleports(event));
		EntityTeleportEvent.ChorusFruit.EVENT.register(event -> entityTeleports(event));
		EntityTeleportEvent.SpreadPlayersCommand.EVENT.register(event -> entityTeleports(event));

		EntityMountEvent.EVENT.register(event -> preventMountDismount(event));
		EntityTickEvent.Post.EVENT.register(event -> livingUpdate(event));
	}

	public static void entityHurts(LivingHurtEvent event) {
		LivingEntity living = event.getEntity();
		DamageSource damageSource = event.getSource();
		// lets not make the player take suffocation damage if riding something
		if (living instanceof Player && isRidingUnfriendly(living) && damageSource.is(DamageTypes.IN_WALL)) {
			event.setCanceled(true);
		}

		if (damageSource.is(DamageTypes.FALL) && living.getAttachedOrCreate(TFDataAttachments.YETI_THROWING.get()).getThrown()) {
			float amount = event.getAmount();
			event.setCanceled(true);
			living.hurt(TFDamageTypes.getEntityDamageSource(living.level(), TFDamageTypes.YEETED, living.getAttachedOrCreate(TFDataAttachments.YETI_THROWING.get()).getThrower()), amount);
		}
	}

	public static void entityTeleports(EntityTeleportEvent event) {
		// if our grabbed target tries to teleport dont let them
		if (event.getEntity() instanceof LivingEntity living && isRidingUnfriendly(living)) {
			event.setCanceled(true);
		}
	}

	public static void hostileDismount(Entity rider) {
		HostileMountEvents.allowDismount = true;
		rider.stopRiding();
		HostileMountEvents.allowDismount = false;
	}

	public static void preventMountDismount(EntityMountEvent event) {
		if (!event.getLevel().isClientSide() &&
			!event.isMounting() && event.getEntityBeingMounted().isAlive() &&
			event.getEntityMounting() instanceof Player player && player.isAlive() &&
			isRidingUnfriendly(player) && !allowDismount && !player.getAbilities().invulnerable)
			event.setCanceled(true);
	}

	public static void livingUpdate(EntityTickEvent.Post event) {
		if (event.getEntity() instanceof IHostileMount)
			event.getEntity().getPassengers().forEach(e -> e.setShiftKeyDown(false));
	}

	public static boolean isRidingUnfriendly(LivingEntity entity) {
		return entity.isPassenger() && entity.getVehicle() instanceof IHostileMount;
	}
}
