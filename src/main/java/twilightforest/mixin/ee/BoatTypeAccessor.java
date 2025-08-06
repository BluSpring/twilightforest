package twilightforest.mixin.ee;

import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Boat.Type.class)
public interface BoatTypeAccessor {
	@Invoker("<init>")
	static Boat.Type create(String name, int idx, Block planks, String id) {
		throw new UnsupportedOperationException();
	}

	@Accessor("$VALUES")
	static Boat.Type[] getValues() {
		throw new IllegalStateException();
	}

	@Accessor("$VALUES")
	@Mutable
	static void setValues(Boat.Type[] values) {
		throw new IllegalStateException();
	}
}
