package twilightforest.mixin.asm.shroom;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.MushroomBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.ASMHooks;

@Mixin({MushroomBlock.class})
public class MushroomBlockMixin {
   @ModifyExpressionValue(
      method = {"canSurvive"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/world/level/block/MushroomBlock;mayPlaceOn(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)Z"
      )}
   )
   private boolean tf$modifySoilDecisionForMushroomBlockSurvivability(
      boolean original, @Local(argsOnly = true) LevelReader level, @Local(argsOnly = true) BlockPos pos
   ) {
      return ASMHooks.modifySoilDecisionForMushroomBlockSurvivability(TriState.of(original), level, pos).orElse(original);
   }
}
