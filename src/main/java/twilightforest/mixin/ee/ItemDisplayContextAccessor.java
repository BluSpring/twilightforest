package twilightforest.mixin.ee;

import net.minecraft.world.item.ItemDisplayContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ItemDisplayContext.class)
public interface ItemDisplayContextAccessor {
	@Invoker("<init>")
	static ItemDisplayContext create(String name, int idx, int idx2, String id) {
		throw new UnsupportedOperationException();
	}

	@Accessor("$VALUES")
	static ItemDisplayContext[] getValues() {
		throw new IllegalStateException();
	}

	@Accessor("$VALUES")
	@Mutable
	static void setValues(ItemDisplayContext[] values) {
		throw new IllegalStateException();
	}
}
