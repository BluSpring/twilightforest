package twilightforest.client.model.block.leaves;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import twilightforest.fabric.BakedModelWrapper;

import java.util.List;

public class BakedLeavesModel extends BakedModelWrapper<BakedModel> {

	public BakedLeavesModel(BakedModel originalModel) {
		super(originalModel);
	}

	@Override
	public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull RandomSource rand) {
		return super.getQuads(state, side, rand);
	}

	/*@Override
	public ChunkRenderTypeSet getRenderTypes(@NotNull BlockState state, @NotNull RandomSource rand, @NotNull ModelData data) {
		return ChunkRenderTypeSet.of(Minecraft.useFancyGraphics() ? RenderType.cutoutMipped() : RenderType.solid());
	}*/
}
