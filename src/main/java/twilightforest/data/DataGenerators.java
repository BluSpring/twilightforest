package twilightforest.data;

import io.github.fabricators_of_create.porting_lib.data.ExistingFileHelper;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import twilightforest.data.custom.QuestGenerator;
import twilightforest.data.custom.StructureTemplateDefinitionGenerator;
import twilightforest.data.custom.stalactites.StalactiteGenerator;
import twilightforest.data.tags.*;

import java.util.concurrent.atomic.AtomicReference;

public class DataGenerators implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator generator) {
		var pack = generator.createPack();
		ExistingFileHelper helper = ExistingFileHelper.withResourcesFromArg();

		//client generators
		pack.addProvider((output, lookupProvider) -> new BlockstateGenerator(output, helper));
		pack.addProvider((output, lookupProvider) -> new ItemModelGenerator(output, helper));
		//pack.addProvider((output, lookupProvider) -> new ParticleGenerator(output, helper));
		pack.addProvider((output, lookupProvider) -> new SoundGenerator(output, helper));

		//registry-based stuff
		pack.addProvider(RegistryDataGenerator::new);
		pack.addProvider((output, lookupProvider) -> new BiomeTagGenerator(output, lookupProvider, helper));
		pack.addProvider((output, lookupProvider) -> new CustomTagGenerator.BannerPatternTagGenerator(output, lookupProvider, helper));
		pack.addProvider((output, lookupProvider) -> new CustomTagGenerator.DimensionTypeTagGenerator(output, lookupProvider, helper));
		pack.addProvider((output, lookupProvider) -> new CustomTagGenerator.WoodPaletteTagGenerator(output, lookupProvider, helper));
		pack.addProvider((output, lookupProvider) -> new CustomTagGenerator.PaintingVariantTagGenerator(output, lookupProvider, helper));
		pack.addProvider((output, lookupProvider) -> new DamageTypeTagGenerator(output, lookupProvider, helper));
		pack.addProvider((output, lookupProvider) -> new StructureTagGenerator(output, lookupProvider, helper));
		pack.addProvider((output, lookupProvider) -> new TFAdvancementProvider(output, lookupProvider, helper));
		pack.addProvider((output, lookupProvider) -> new LootGenerator(output, lookupProvider));

		//server generators
		//pack.addProvider((output, lookupProvider) -> new DataMapGenerator(output, lookupProvider));
		pack.addProvider((output, lookupProvider) -> new StalactiteGenerator(output));
		pack.addProvider((output, lookupProvider) -> new TFStructureUpdater("structures", output, helper));

		//normal tags
		AtomicReference<BlockTagGenerator> tagGenerator = new AtomicReference<>();
		pack.addProvider((output, lookupProvider) -> {
			tagGenerator.set(new BlockTagGenerator(output, lookupProvider, helper));
			return tagGenerator.get();
		});
		pack.addProvider((output, lookupProvider) -> new CustomTagGenerator.BlockEntityTagGenerator(output, lookupProvider, helper));
		pack.addProvider((output, lookupProvider) -> new FluidTagGenerator(output, lookupProvider, helper));
		pack.addProvider((output, lookupProvider) -> new ItemTagGenerator(output, lookupProvider, tagGenerator.get().contentsGetter(), helper));
		pack.addProvider((output, lookupProvider) -> new EntityTagGenerator(output, lookupProvider, helper));
		pack.addProvider((output, lookupProvider) -> new CraftingGenerator(output, lookupProvider));
		pack.addProvider((output, lookupProvider) -> new LootModifierGenerator(output, lookupProvider));

		pack.addProvider((output, lookupProvider) -> new StructureTemplateDefinitionGenerator(output, lookupProvider, helper));

		//these have to go last due to magic paintings
		//when magic paintings are registered their atlas and lang content is too
		pack.addProvider((output, lookupProvider) -> new AtlasGenerator(output, lookupProvider, helper));
		pack.addProvider((output, lookupProvider) -> new LangGenerator(output));

		pack.addProvider((output, lookupProvider) -> new QuestGenerator(output));

		//pack.mcmeta
		/*pack.addProvider((output, lookupProvider) -> true, new PackMetadataGenerator(output).add(PackMetadataSection.TYPE, new PackMetadataSection(
			Component.literal("Resources for Twilight Forest"),
			DetectedVersion.BUILT_IN.getPackVersion(PackType.SERVER_DATA),
			Optional.of(new InclusiveRange<>(0, Integer.MAX_VALUE)))));*/
	}
}
