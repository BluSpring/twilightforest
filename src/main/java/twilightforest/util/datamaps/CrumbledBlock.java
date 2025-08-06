package twilightforest.util.datamaps;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;

import java.util.HashMap;
import java.util.Map;

public record CrumbledBlock(Block result, float chanceToCrumble) {
	public static final Map<Block, CrumbledBlock> CRUMBLE_HORN = new HashMap<>();

	public static final Codec<CrumbledBlock> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		BuiltInRegistries.BLOCK.byNameCodec().fieldOf("crumble_to").forGetter(CrumbledBlock::result),
		Codec.FLOAT.fieldOf("crumble_chance").forGetter(CrumbledBlock::chanceToCrumble)
	).apply(instance, CrumbledBlock::new));
}
