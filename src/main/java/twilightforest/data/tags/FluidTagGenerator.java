package twilightforest.data.tags;

import io.github.fabricators_of_create.porting_lib.data.ExistingFileHelper;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.FluidTagsProvider;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;

import java.util.concurrent.CompletableFuture;

public class FluidTagGenerator extends FabricTagProvider.FluidTagProvider {

	public static final TagKey<Fluid> FIRE_JET_FUEL = TagKey.create(Registries.FLUID, TwilightForestMod.prefix("fire_jet_fuel"));

	public FluidTagGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper existingFileHelper) {
		super(output, provider);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		getOrCreateTagBuilder(FIRE_JET_FUEL).forceAddTag(FluidTags.LAVA);
	}

	@Override
	public String getName() {
		return "Twilight Forest Fluid Tags";
	}
}
