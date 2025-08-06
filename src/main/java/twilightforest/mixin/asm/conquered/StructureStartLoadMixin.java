package twilightforest.mixin.asm.conquered;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.pieces.PiecesContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.ASMHooks;

@Mixin({StructureStart.class})
public class StructureStartLoadMixin {
   @ModifyExpressionValue(
      method = {"loadStaticStart"},
      at = {@At(
         value = "NEW",
         target = "(Lnet/minecraft/world/level/levelgen/structure/Structure;Lnet/minecraft/world/level/ChunkPos;ILnet/minecraft/world/level/levelgen/structure/pieces/PiecesContainer;)Lnet/minecraft/world/level/levelgen/structure/StructureStart;"
      )}
   )
   private static StructureStart tf$handleLoadStaticStart(StructureStart original, @Local PiecesContainer container, @Local(argsOnly = true) CompoundTag tag) {
      return ASMHooks.loadStaticStart(original, container, tag);
   }
}
