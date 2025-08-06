package twilightforest.fabric;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.fabricators_of_create.porting_lib.models.TransformTypeDependentItemBakedModel;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Wrapper for {@link BakedModel} which delegates all operations to its parent.
 * <p>
 * Useful for creating wrapper baked models which only override certain properties.
 */
public abstract class BakedModelWrapper<T extends BakedModel> implements BakedModel, TransformTypeDependentItemBakedModel {
	protected final T originalModel;

	public BakedModelWrapper(T originalModel) {
		this.originalModel = originalModel;
	}

	@Override
	public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource rand) {
		return originalModel.getQuads(state, side, rand);
	}

	@Override
	public boolean useAmbientOcclusion() {
		return originalModel.useAmbientOcclusion();
	}

	@Override
	public boolean isGui3d() {
		return originalModel.isGui3d();
	}

	@Override
	public boolean usesBlockLight() {
		return originalModel.usesBlockLight();
	}

	@Override
	public boolean isCustomRenderer() {
		return originalModel.isCustomRenderer();
	}

	@Override
	public TextureAtlasSprite getParticleIcon() {
		return originalModel.getParticleIcon();
	}

	@Override
	public ItemTransforms getTransforms() {
		return originalModel.getTransforms();
	}

	@Override
	public ItemOverrides getOverrides() {
		return originalModel.getOverrides();
	}

	@Override
	public BakedModel applyTransform(ItemDisplayContext context, PoseStack poseStack, boolean leftHand, DefaultTransform defaultTransform) {
		return TransformTypeDependentItemBakedModel.maybeApplyTransform(originalModel, context, poseStack, leftHand, defaultTransform);
	}
}
