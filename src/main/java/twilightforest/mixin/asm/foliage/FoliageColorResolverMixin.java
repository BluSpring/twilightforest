package twilightforest.mixin.asm.foliage;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.ASMHooks;

@Mixin({BiomeColors.class})
public class FoliageColorResolverMixin {
   @ModifyReturnValue(
      method = {"method_23791"},
      at = {@At("RETURN")}
   )
   private static int tf$resolveFoliageColor(
      int original, @Local(argsOnly = true) Biome biome, @Local(argsOnly = true,ordinal = 0) double x, @Local(argsOnly = true,ordinal = 1) double z
   ) {
      return ASMHooks.resolveFoliageColor(original, biome, x, z);
   }
}
