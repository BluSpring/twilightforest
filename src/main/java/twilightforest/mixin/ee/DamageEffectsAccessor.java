package twilightforest.mixin.ee;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(DamageEffects.class)
public interface DamageEffectsAccessor {
	@Invoker("<init>")
	static DamageEffects create(String name, int idx, String id, SoundEvent soundEvent) {
		throw new UnsupportedOperationException();
	}

	@Accessor("$VALUES")
	static DamageEffects[] getValues() {
		throw new IllegalStateException();
	}

	@Accessor("$VALUES")
	@Mutable
	static void setValues(DamageEffects[] values) {
		throw new IllegalStateException();
	}
}
