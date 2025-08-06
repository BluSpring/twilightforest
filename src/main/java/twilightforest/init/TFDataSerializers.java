package twilightforest.init;

import io.github.fabricators_of_create.porting_lib.core.util.Lazy;
import io.github.fabricators_of_create.porting_lib.registry.DeferredHolder;
import io.github.fabricators_of_create.porting_lib.registry.DeferredRegister;
import net.minecraft.core.Holder;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import twilightforest.TFRegistries;
import twilightforest.TwilightForestMod;
import twilightforest.entity.MagicPaintingVariant;
import twilightforest.entity.passive.DwarfRabbitVariant;
import twilightforest.entity.passive.TinyBirdVariant;

import java.util.List;

public class TFDataSerializers {

	//public static final DeferredRegister<EntityDataSerializer<?>> DATA_SERIALIZERS = DeferredRegister.create(NeoForgeRegistries.ENTITY_DATA_SERIALIZERS, TwilightForestMod.ID);

	public static final Lazy<EntityDataSerializer<List<String>>> STRING_LIST = Lazy.of(() -> EntityDataSerializer.forValueType(ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list())));
	public static final Lazy<EntityDataSerializer<Holder<DwarfRabbitVariant>>> DWARF_RABBIT_VARIANT = Lazy.of(() -> EntityDataSerializer.forValueType(ByteBufCodecs.holderRegistry(TFRegistries.Keys.DWARF_RABBIT_VARIANT)));
	public static final Lazy<EntityDataSerializer<Holder<TinyBirdVariant>>> TINY_BIRD_VARIANT = Lazy.of(() -> EntityDataSerializer.forValueType(ByteBufCodecs.holderRegistry(TFRegistries.Keys.TINY_BIRD_VARIANT)));
	public static final Lazy<EntityDataSerializer<Holder<MagicPaintingVariant>>> MAGIC_PAINTING_VARIANT = Lazy.of(() -> EntityDataSerializer.forValueType(ByteBufCodecs.holderRegistry(TFRegistries.Keys.MAGIC_PAINTINGS)));

	public static void init() {
		EntityDataSerializers.registerSerializer(STRING_LIST.get());
		EntityDataSerializers.registerSerializer(DWARF_RABBIT_VARIANT.get());
		EntityDataSerializers.registerSerializer(TINY_BIRD_VARIANT.get());
		EntityDataSerializers.registerSerializer(MAGIC_PAINTING_VARIANT.get());
	}
}
