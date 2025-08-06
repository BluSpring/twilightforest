package twilightforest.fabric;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.util.StringRepresentable;

import java.util.function.Function;
import java.util.function.Supplier;

public interface IExtensibleEnum {
	default void init() {}

	/**
	 * Use this instead of {@link StringRepresentable#fromEnum(Supplier)} for extensible enums because this not cache the enum values on construction
	 */
	static <E extends Enum<E> & StringRepresentable> Codec<E> createCodecForExtensibleEnum(Supplier<E[]> valuesSupplier, Function<? super String, ? extends E> enumValueFromNameFunction) {
		return Codec.either(Codec.STRING, Codec.INT).comapFlatMap(
			either -> either.map(
				str -> {
					var val = enumValueFromNameFunction.apply(str);
					return val != null ? DataResult.success(val) : DataResult.error(() -> "Unknown enum value name: " + str);
				},
				num -> {
					var values = valuesSupplier.get();
					return num >= 0 && num < values.length ? DataResult.success(values[num]) : DataResult.error(() -> "Unknown enum id: " + num);
				}
			),
			value -> Either.left(value.getSerializedName())
		);
	}
}
