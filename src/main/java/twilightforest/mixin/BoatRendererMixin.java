package twilightforest.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.renderer.entity.BoatRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.vehicle.Boat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BoatRenderer.class)
public class BoatRendererMixin {
	@WrapOperation(method = {"getTextureLocation(Lnet/minecraft/world/entity/vehicle/Boat$Type;Z)Lnet/minecraft/resources/ResourceLocation;"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/resources/ResourceLocation;withDefaultNamespace(Ljava/lang/String;)Lnet/minecraft/resources/ResourceLocation;"))
	private static ResourceLocation tf$fixBoatTypeArg(String location, Operation<ResourceLocation> original, @Local(argsOnly = true) Boat.Type type) {
		if (!location.contains(":"))
			return original.call(location);

		return ResourceLocation.parse(type.getName()).withPrefix(location.replace(type.getName(), "").replace(".png", "")).withSuffix(".png");
	}
}
