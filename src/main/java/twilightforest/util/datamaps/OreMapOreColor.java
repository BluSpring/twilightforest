package twilightforest.util.datamaps;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.MapColor;
import twilightforest.util.Codecs;

import java.util.HashMap;
import java.util.Map;

public record OreMapOreColor(MapColor color) {
	public static final Map<TagKey<Block>, OreMapOreColor> ORE_COLOR_MAP = new HashMap<>();

	public static final Codec<OreMapOreColor> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		Codecs.COLOR_CODEC.fieldOf("map_color").forGetter(OreMapOreColor::color)
	).apply(instance, OreMapOreColor::new));
}
