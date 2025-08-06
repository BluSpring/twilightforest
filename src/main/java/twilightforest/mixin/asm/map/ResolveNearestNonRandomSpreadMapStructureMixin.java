package twilightforest.mixin.asm.map;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.Structure;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.ASMHooks;

@Mixin({ChunkGenerator.class})
public class ResolveNearestNonRandomSpreadMapStructureMixin {
   @ModifyReturnValue(
      method = {"findNearestMapStructure"},
      at = {@At("RETURN")}
   )
   private Pair<BlockPos, Holder<Structure>> tf$resolveNearestNonRandomSpreadMapStructure(
      Pair<BlockPos, Holder<Structure>> original,
      ServerLevel level,
      HolderSet<Structure> structureSet,
      BlockPos pos,
      int searchRadius,
      boolean skipKnownStructures
   ) {
      return ASMHooks.resolveNearestNonRandomSpreadMapStructure(original, level, structureSet, pos, searchRadius, skipKnownStructures);
   }
}
