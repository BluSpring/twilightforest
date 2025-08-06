package twilightforest.enums.extensions;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageEffects;
import twilightforest.TFEnumExtensions;
import twilightforest.fabric.EnumUtils;
import twilightforest.init.TFSounds;
import twilightforest.mixin.ee.DamageEffectsAccessor;
import twilightforest.util.ModidPrefixUtil;

public class TFDamageEffectsEnumExtension {
	private static DamageEffects create(String name, String id, SoundEvent soundEvent) {
		return EnumUtils.addEnumToClass(DamageEffects.class, DamageEffectsAccessor.getValues(), name, size -> DamageEffectsAccessor.create(name, size, id, soundEvent), values -> DamageEffectsAccessor.setValues(values.toArray(new DamageEffects[0])));
	}

	public final DamageEffects PINCH = create("TWILIGHTFOREST_PINCH", ModidPrefixUtil.INSTANCE.stringPrefix("pinch"), TFSounds.PINCH_BEETLE_ATTACK.value());

}
