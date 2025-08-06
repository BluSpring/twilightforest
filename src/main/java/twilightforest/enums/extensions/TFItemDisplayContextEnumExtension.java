package twilightforest.enums.extensions;

import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import twilightforest.TFEnumExtensions;
import twilightforest.fabric.EnumUtils;
import twilightforest.mixin.ee.GrassColorModifierAccessor;
import twilightforest.mixin.ee.ItemDisplayContextAccessor;
import twilightforest.util.ModidPrefixUtil;

public class TFItemDisplayContextEnumExtension {
	public static final TFItemDisplayContextEnumExtension INSTANCE = new TFItemDisplayContextEnumExtension();

	private TFItemDisplayContextEnumExtension() {}

	private static ItemDisplayContext create(String name, String named) {
		return EnumUtils.addEnumToClass(ItemDisplayContext.class, ItemDisplayContextAccessor.getValues(), name, size -> ItemDisplayContextAccessor.create(name, size, size, named), values -> ItemDisplayContextAccessor.setValues(values.toArray(new ItemDisplayContext[0])));
	}

	public final ItemDisplayContext JARRED = create("TWILIGHTFOREST_JARRED", ModidPrefixUtil.INSTANCE.stringPrefix("jarred"));

}
