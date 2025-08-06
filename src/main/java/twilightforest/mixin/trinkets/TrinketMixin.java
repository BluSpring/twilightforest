package twilightforest.mixin.trinkets;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import dev.emi.trinkets.api.Trinket;
import dev.emi.trinkets.api.TrinketEnums;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.events.CharmEvents;

@Mixin(Trinket.class)
public interface TrinketMixin {
	@ModifyReturnValue(method = "getDropRule", at = @At("RETURN"))
	private TrinketEnums.DropRule tf$checkConsumedCharmKeeping(TrinketEnums.DropRule original, @Local(argsOnly = true) LivingEntity entity) {
		if (!(entity instanceof Player player))
			return original;

		CompoundTag playerData = CharmEvents.getPlayerData(player);
		if (!player.level().isClientSide() && playerData.contains(CharmEvents.CONSUMED_CHARM_TAG) && playerData.contains(CharmEvents.CHARM_INV_TAG) && !playerData.getList(CharmEvents.CHARM_INV_TAG, 10).isEmpty()) {
			return TrinketEnums.DropRule.KEEP;
		}

		return original;
	}
}
