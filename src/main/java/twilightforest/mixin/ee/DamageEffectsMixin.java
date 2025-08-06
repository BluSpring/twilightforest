package twilightforest.mixin.ee;

import com.mojang.serialization.Codec;
import net.minecraft.world.damagesource.DamageEffects;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.fabric.IExtensibleEnum;

@Mixin(DamageEffects.class)
public abstract class DamageEffectsMixin {
	@Shadow
	@Final
	@Mutable
	public static Codec<DamageEffects> CODEC;

	@Inject(method = "<clinit>", at = @At("TAIL"))
	private static void tf$useExtensibleCodec(CallbackInfo ci) {
		CODEC = IExtensibleEnum.createCodecForExtensibleEnum(() -> DamageEffects.values(), name -> {
			for (DamageEffects value : DamageEffects.values()) {
				if (value.getSerializedName().equals(name))
					return value;
			}

			return null;
		});
	}
}
