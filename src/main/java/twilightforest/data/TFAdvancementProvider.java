package twilightforest.data;

import io.github.fabricators_of_create.porting_lib.data.ExistingFileHelper;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import io.github.fabricators_of_create.porting_lib.util.DeferredSpawnEggItem;
import net.minecraft.data.advancements.AdvancementProvider;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class TFAdvancementProvider extends AdvancementProvider {

	public TFAdvancementProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, ExistingFileHelper existingFileHelper) {
		super(output, registries, List.of());
	}
}
