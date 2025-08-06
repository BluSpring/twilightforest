package twilightforest.world.components.processors;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.Nullable;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFStructureProcessors;
import twilightforest.util.RotationUtil;

public class SmartGrassProcessor extends StructureProcessor {
	public static final SmartGrassProcessor INSTANCE = new SmartGrassProcessor();
	public static final MapCodec<SmartGrassProcessor> CODEC = MapCodec.unit(() -> INSTANCE);

	private SmartGrassProcessor() {
	}

	@Override
	public @Nullable StructureTemplate.StructureBlockInfo processBlock(LevelReader level, BlockPos offset, BlockPos pos, StructureTemplate.StructureBlockInfo blockInfo, StructureTemplate.StructureBlockInfo relativeBlockInfo, StructurePlaceSettings settings) {
		if (blockInfo.state().getBlock() != Blocks.GRASS_BLOCK)
			return blockInfo;

		if (level.getBlockState(pos).is(BlockTags.DIRT) || !level.isEmptyBlock(pos.above()))
			return null;

		for (Direction direction : RotationUtil.CARDINALS) {
			BlockState stateAt = level.getBlockState(pos.relative(direction));

			if (stateAt.getBlock() == Blocks.PODZOL) return new StructureTemplate.StructureBlockInfo(blockInfo.pos(), Blocks.PODZOL.defaultBlockState(), null);
			if (stateAt.getBlock() == Blocks.GRASS_BLOCK) return blockInfo;
			if (stateAt.getBlock() == Blocks.MYCELIUM) return new StructureTemplate.StructureBlockInfo(blockInfo.pos(), Blocks.MYCELIUM.defaultBlockState(), null);
			if (stateAt.getBlock() == Blocks.DIRT_PATH) return new StructureTemplate.StructureBlockInfo(blockInfo.pos(), Blocks.DIRT_PATH.defaultBlockState(), null);
			if (stateAt.getBlock() == Blocks.COARSE_DIRT) return new StructureTemplate.StructureBlockInfo(blockInfo.pos(), Blocks.COARSE_DIRT.defaultBlockState(), null);
			if (stateAt.getBlock() == TFBlocks.UBEROUS_SOIL.get()) return new StructureTemplate.StructureBlockInfo(blockInfo.pos(), TFBlocks.UBEROUS_SOIL.get().defaultBlockState(), null);
		}

		return blockInfo;
	}

	@Override
	protected StructureProcessorType<?> getType() {
		return TFStructureProcessors.SMART_GRASS.get();
	}
}
