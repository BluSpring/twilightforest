package twilightforest.mixin.asm.chunk;

import com.llamalad7.mixinextras.sugar.Local;
import java.util.concurrent.CompletableFuture;
import net.minecraft.server.level.GenerationChunkHolder;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.util.StaticCache2D;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.status.ChunkStatusTasks;
import net.minecraft.world.level.chunk.status.ChunkStep;
import net.minecraft.world.level.chunk.status.WorldGenContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.ASMHooks;

@Mixin({ChunkStatusTasks.class})
public abstract class ChunkStatusTaskMixin {
   @Inject(
      method = {"generateSurface"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/world/level/chunk/ChunkGenerator;buildSurface(Lnet/minecraft/server/level/WorldGenRegion;Lnet/minecraft/world/level/StructureManager;Lnet/minecraft/world/level/levelgen/RandomState;Lnet/minecraft/world/level/chunk/ChunkAccess;)V"
      )}
   )
   private static void tf$chunkBlanketing(
      WorldGenContext worldGenContext,
      ChunkStep step,
      StaticCache2D<GenerationChunkHolder> cache,
      ChunkAccess chunk,
      CallbackInfoReturnable<CompletableFuture<ChunkAccess>> cir,
      @Local WorldGenRegion region
   ) {
      ASMHooks.chunkBlanketing(chunk, region);
   }
}
