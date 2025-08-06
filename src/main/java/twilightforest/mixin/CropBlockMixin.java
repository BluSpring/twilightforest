package twilightforest.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.init.TFBlocks;

@Mixin(CropBlock.class)
public abstract class CropBlockMixin {
	@ModifyReturnValue(method = "mayPlaceOn", at = @At("RETURN"))
	private boolean tf$checkIsUberousSoil(boolean original, @Local(argsOnly = true) BlockState state) {
		return original || state.is(TFBlocks.UBEROUS_SOIL.get());
	}
}
