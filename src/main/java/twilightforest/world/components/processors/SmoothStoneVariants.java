package twilightforest.world.components.processors;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.Nullable;
import twilightforest.init.TFStructureProcessors;
import twilightforest.util.features.FeaturePlacers;

public class SmoothStoneVariants extends StructureProcessor {
	public static final SmoothStoneVariants INSTANCE = new SmoothStoneVariants();
	public static final MapCodec<SmoothStoneVariants> CODEC = MapCodec.unit(() -> INSTANCE);

	private SmoothStoneVariants() {
	}

	@Override
	public @Nullable StructureTemplate.StructureBlockInfo processBlock(LevelReader level, BlockPos offset, BlockPos pos, StructureTemplate.StructureBlockInfo blockInfo, StructureTemplate.StructureBlockInfo relativeBlockInfo, StructurePlaceSettings settings) {
		RandomSource random = settings.getRandom(blockInfo.pos());

		// We use nextBoolean in other processors so this lets us re-seed deterministically
		random.setSeed(random.nextLong() * 4);

		if (blockInfo.state().is(Blocks.SMOOTH_STONE_SLAB) && random.nextBoolean())
			return new StructureTemplate.StructureBlockInfo(blockInfo.pos(), FeaturePlacers.transferAllStateKeys(blockInfo.state(), Blocks.COBBLESTONE_SLAB), null);

		if (blockInfo.state().is(Blocks.SMOOTH_STONE) && random.nextBoolean())
			return new StructureTemplate.StructureBlockInfo(blockInfo.pos(), Blocks.COBBLESTONE.defaultBlockState(), null);

		return blockInfo;
	}

	@Override
	protected StructureProcessorType<?> getType() {
		return TFStructureProcessors.SMOOTH_STONE_VARIANTS.get();
	}
}
