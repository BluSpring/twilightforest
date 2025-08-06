package twilightforest.mixin.ee;

import net.minecraft.ChatFormatting;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Rarity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Rarity.class)
public interface RarityAccessor {
	@Invoker("<init>")
	static Rarity create(String name, int idx, int rarityId, String rarityName, ChatFormatting color) {
		throw new UnsupportedOperationException();
	}

	@Accessor("$VALUES")
	static Rarity[] getValues() {
		throw new IllegalStateException();
	}

	@Accessor("$VALUES")
	@Mutable
	static void setValues(Rarity[] values) {
		throw new IllegalStateException();
	}
}
