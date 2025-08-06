package twilightforest.fabric;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import java.util.Map;
import java.util.function.Supplier;

public interface FlowerPotBlockExtension {
	Map<ResourceLocation, Supplier<? extends Block>> tf$getPlants();
	void addPlant(ResourceLocation flower, Supplier<? extends Block> fullBlock);
}
