package twilightforest.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.vehicle.Boat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ModelLayers.class)
public class ModelLayersMixin {
	@WrapOperation(method = {"createRaftModelName", "createBoatModelName", "createChestBoatModelName", "createChestRaftModelName"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/geom/ModelLayers;createLocation(Ljava/lang/String;Ljava/lang/String;)Lnet/minecraft/client/model/geom/ModelLayerLocation;"))
	private static ModelLayerLocation tf$fixBoatTypeArg(String path, String model, Operation<ModelLayerLocation> original, @Local(argsOnly = true) Boat.Type type) {
		if (!path.contains(":"))
			return original.call(path, model);

		var location = ResourceLocation.parse(type.getName());
		return new ModelLayerLocation(location.withPrefix(path.replace(type.getName(), "")), model);
	}
}
