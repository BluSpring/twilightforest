package twilightforest.util.datamaps;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;

import java.util.HashMap;
import java.util.Map;

public record EntityTransformation(EntityType<?> result) {
	public static final Map<EntityType<?>, EntityTransformation> TRANSFORMATION_POWDER = new HashMap<>();
	public static final Map<EntityType<?>, EntityTransformation> OMINOUS_FIRE = new HashMap<>();

	public static final Codec<EntityTransformation> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("transform_to").forGetter(EntityTransformation::result)
	).apply(instance, EntityTransformation::new));
}
