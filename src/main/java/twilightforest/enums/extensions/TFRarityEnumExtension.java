package twilightforest.enums.extensions;

import net.minecraft.ChatFormatting;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Rarity;
import twilightforest.TFEnumExtensions;
import twilightforest.fabric.EnumUtils;
import twilightforest.mixin.ee.DamageEffectsAccessor;
import twilightforest.mixin.ee.RarityAccessor;
import twilightforest.util.ModidPrefixUtil;

public class TFRarityEnumExtension {
	private static Rarity create(String name, int id, String rarityName, ChatFormatting formatting) {
		return EnumUtils.addEnumToClass(Rarity.class, RarityAccessor.getValues(), name, size -> RarityAccessor.create(name, size, id, rarityName, formatting), values -> RarityAccessor.setValues(values.toArray(new Rarity[0])));
	}

	public final Rarity TWILIGHT = create("TWILIGHTFOREST_TWILIGHT", -1, ModidPrefixUtil.INSTANCE.stringPrefix("twilight"), ChatFormatting.DARK_GREEN);

}
