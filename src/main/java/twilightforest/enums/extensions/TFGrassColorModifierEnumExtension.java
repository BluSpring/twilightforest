package twilightforest.enums.extensions;

import net.minecraft.world.level.biome.BiomeSpecialEffects;
import twilightforest.TFEnumExtensions;
import twilightforest.fabric.ColorModifier;
import twilightforest.fabric.EnumUtils;
import twilightforest.fabric.GrassColorModifierExtension;
import twilightforest.mixin.ee.GrassColorModifierAccessor;
import twilightforest.util.ModidPrefixUtil;
import twilightforest.world.components.BiomeColorAlgorithms;

public class TFGrassColorModifierEnumExtension {
	private static BiomeSpecialEffects.GrassColorModifier create(String name, String id, ColorModifier colorModifier) {
		var modifier = EnumUtils.addEnumToClass(BiomeSpecialEffects.GrassColorModifier.class, GrassColorModifierAccessor.getValues(), name, size -> GrassColorModifierAccessor.create(name, size, id), values -> GrassColorModifierAccessor.setValues(values.toArray(new BiomeSpecialEffects.GrassColorModifier[0])));

		((GrassColorModifierExtension) (Object) modifier).tf$setDelegate(colorModifier);

		return modifier;
	}

	public final BiomeSpecialEffects.GrassColorModifier ENCHANTED_FOREST = create("TWILIGHTFOREST_ENCHANTED_FOREST", ModidPrefixUtil.INSTANCE.stringPrefix("enchanted_forest"), (x, z, color) -> BiomeColorAlgorithms.INSTANCE.enchanted(color, (int) x, (int) z));

	public final BiomeSpecialEffects.GrassColorModifier SWAMP = create("TWILIGHTFOREST_SWAMP", ModidPrefixUtil.INSTANCE.stringPrefix("swamp"), (x, z, color) -> BiomeColorAlgorithms.INSTANCE.swamp(BiomeColorAlgorithms.Type.Grass));

	public final BiomeSpecialEffects.GrassColorModifier DARK_FOREST = create("TWILIGHTFOREST_DARK_FOREST", ModidPrefixUtil.INSTANCE.stringPrefix("dark_forest"), (x, z, color) -> BiomeColorAlgorithms.INSTANCE.darkForest(BiomeColorAlgorithms.Type.Grass));

	public final BiomeSpecialEffects.GrassColorModifier DARK_FOREST_CENTER = create("TWILIGHTFOREST_DARK_FOREST_CENTER", ModidPrefixUtil.INSTANCE.stringPrefix("dark_forest_center"), (x, z, color) -> BiomeColorAlgorithms.INSTANCE.darkForestCenterGrass((int) x, (int) z));

	public final BiomeSpecialEffects.GrassColorModifier SPOOKY_FOREST = create("TWILIGHTFOREST_SPOOKY_FOREST", ModidPrefixUtil.INSTANCE.stringPrefix("spooky_forest"), (x, z, color) -> BiomeColorAlgorithms.INSTANCE.spookyGrass((int) x, (int) z));

}
