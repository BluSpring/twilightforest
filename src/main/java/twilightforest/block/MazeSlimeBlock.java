package twilightforest.block;

import io.github.fabricators_of_create.porting_lib.blocks.extensions.StickToBlock;
import io.github.fabricators_of_create.porting_lib.blocks.extensions.StickyBlock;
import net.minecraft.world.level.block.SlimeBlock;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.init.TFBlocks;

public class MazeSlimeBlock extends SlimeBlock implements StickToBlock, StickyBlock {
	public MazeSlimeBlock(Properties properties) {
		super(properties);
	}

	@Override
	public boolean canStickTo(BlockState state, BlockState other) {
		return other.is(TFBlocks.MAZE_SLIME_BLOCK) || !(other.getBlock() instanceof StickyBlock stickyBlock && stickyBlock.isStickyBlock(other));
	}

	@Override
	public boolean isStickyBlock(BlockState state) {
		return true;
	}
}
