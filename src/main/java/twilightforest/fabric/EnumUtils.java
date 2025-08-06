package twilightforest.fabric;

import com.chocohead.mm.CasualStreamHandler;
import com.chocohead.mm.api.ClassTinkerers;
import net.minecraftforge.fml.unsafe.UnsafeHacks;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class EnumUtils {
	public static <T extends Enum<?>> T addEnumToClass(Class<T> clazz, T[] values, String name, Function<Integer, T> createValue, Consumer<List<T>> setValues) {
		var list = new ArrayList<>(List.of(values));

		for (T enumValue : values) {
			if (!enumValue.name().equalsIgnoreCase(name))
				continue;

			return enumValue;
		}

		var value = createValue.apply(values.length);

		if (value instanceof IExtensibleEnum extensibleEnum)
			extensibleEnum.init();

		list.add(value);
		setValues.accept(list);

		try {
			UnsafeHacks.cleanEnumCache(clazz);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return value;
	}

	public static Class<?> loadClass(String name, byte[] byteArray) {
		try {
			var className = name.replace("/", ".");
			var url = CasualStreamHandler.create(className, byteArray);
			if (ClassTinkerers.addURL(url))
				return Class.forName(className);

			return null;
		} catch (Exception e) {
			System.out.println("Failed to dynamically load class " + name + "!");
			e.printStackTrace();

			return null;
		}
	}
}
