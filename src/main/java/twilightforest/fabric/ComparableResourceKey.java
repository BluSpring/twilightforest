package twilightforest.fabric;

import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.NotNull;

public record ComparableResourceKey<T>(ResourceKey<T> key) implements Comparable<ComparableResourceKey<?>> {
	@Override
	public int compareTo(@NotNull ComparableResourceKey<?> o) {
		int ret = this.key().registry().compareTo(o.key().registry());
		if (ret == 0)
			ret = this.key().location().compareTo(o.key().location());

		return ret;
	}
}
