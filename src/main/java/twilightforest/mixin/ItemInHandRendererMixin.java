package twilightforest.mixin;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.fabric.CustomMapItem;

@Mixin(ItemInHandRenderer.class)
public class ItemInHandRendererMixin {
	@WrapOperation(method = "renderMap", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/MapItem;getSavedData(Lnet/minecraft/world/level/saveddata/maps/MapId;Lnet/minecraft/world/level/Level;)Lnet/minecraft/world/level/saveddata/maps/MapItemSavedData;"))
	private MapItemSavedData tf$useCustomMapSaveData(MapId mapId, Level level, Operation<MapItemSavedData> original, @Local(argsOnly = true) ItemStack stack) {
		if (stack.getItem() instanceof CustomMapItem) {
			return MapItem.getSavedData(stack, level);
		}

		return original.call(mapId, level);
	}

	@Definition(id = "stack", local = @Local(type = ItemStack.class, argsOnly = true))
	@Definition(id = "is", method = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z")
	@Definition(id = "FILLED_MAP", field = "Lnet/minecraft/world/item/Items;FILLED_MAP:Lnet/minecraft/world/item/Item;")
	@Expression("stack.is(FILLED_MAP)")
	@ModifyExpressionValue(method = "renderArmWithItem", at = @At("MIXINEXTRAS:EXPRESSION"))
	private boolean tf$useCustomMapRenderer(boolean original, @Local(argsOnly = true) ItemStack stack) {
		return original || stack.getItem() instanceof CustomMapItem;
	}
}
