package twilightforest.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.fabric.FlowerPotBlockExtension;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@Mixin(FlowerPotBlock.class)
public class FlowerPotBlockMixin implements FlowerPotBlockExtension {
	@Unique private final Map<ResourceLocation, Supplier<? extends Block>> tf$fullPots = new HashMap<>();

	@ModifyExpressionValue(method = "useItemOn", at = @At(value = "INVOKE", target = "Ljava/util/Map;getOrDefault(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"))
	private <V> V tf$tryGetFromFullPots(V original, @Local BlockItem blockItem) {
		var pot = (FlowerPotBlockExtension) Blocks.FLOWER_POT;
		var value = pot.tf$getPlants().get(BuiltInRegistries.BLOCK.getKey(blockItem.getBlock()));

		if (value != null)
			return (V) value.get();

		return original;
	}

	@Override
	public void addPlant(ResourceLocation flower, Supplier<? extends Block> fullBlock) {
		this.tf$fullPots.put(flower, fullBlock);
	}

	@Override
	public Map<ResourceLocation, Supplier<? extends Block>> tf$getPlants() {
		return tf$fullPots;
	}
}
