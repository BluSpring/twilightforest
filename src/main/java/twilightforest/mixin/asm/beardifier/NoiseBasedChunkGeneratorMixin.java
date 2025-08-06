package twilightforest.mixin.asm.beardifier;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.Beardifier;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.ASMHooks;
import twilightforest.fabric.BeardifierExtension;

@Mixin({NoiseBasedChunkGenerator.class})
public class NoiseBasedChunkGeneratorMixin {
   @ModifyExpressionValue(
      method = {"createNoiseChunk"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/world/level/levelgen/Beardifier;forStructuresInChunk(Lnet/minecraft/world/level/StructureManager;Lnet/minecraft/world/level/ChunkPos;)Lnet/minecraft/world/level/levelgen/Beardifier;"
      )}
   )
   private Beardifier tf$initializeCustomFields(
      Beardifier original, @Local(argsOnly = true) StructureManager structureManager, @Local(argsOnly = true) ChunkAccess chunkAccess
   ) {
      ((BeardifierExtension)original).tf$setCustomStructureDensities(ASMHooks.gatherCustomTerrain(structureManager, chunkAccess.getPos()));
      return original;
   }
}
