package twilightforest.mixin;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.fabric.CustomMapItem;

@Mixin(MapItem.class)
public class MapItemMixin {
	@Inject(method = "getSavedData(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/Level;)Lnet/minecraft/world/level/saveddata/maps/MapItemSavedData;", at = @At("HEAD"), cancellable = true)
	private static void tf$getCustomSavedData(ItemStack stack, Level level, CallbackInfoReturnable<MapItemSavedData> cir) {
		if (stack.getItem() instanceof CustomMapItem mapItem) {
			cir.setReturnValue(mapItem.getCustomMapData(stack, level));
		}
	}
}
