package twilightforest.mixin.ee;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.fabric.ColorModifier;
import twilightforest.fabric.GrassColorModifierExtension;
import twilightforest.fabric.IExtensibleEnum;

@Mixin(BiomeSpecialEffects.GrassColorModifier.class)
public abstract class GrassColorModifierMixin implements GrassColorModifierExtension {
	@Shadow
	public static BiomeSpecialEffects.GrassColorModifier[] values() {
		throw new IllegalStateException();
	}

	@Shadow
	public static BiomeSpecialEffects.GrassColorModifier valueOf(String name) throws IllegalArgumentException {
		throw new IllegalArgumentException();
	}

	@Shadow
	@Final
	@Mutable
	public static Codec<BiomeSpecialEffects.GrassColorModifier> CODEC;
	@Unique private ColorModifier tf$delegate;

	@Override
	public void tf$setDelegate(ColorModifier delegate) {
		this.tf$delegate = delegate;
	}

	@Override
	public ColorModifier tf$getDelegate() {
		return this.tf$delegate;
	}

	@Inject(method = "<clinit>", at = @At("TAIL"))
	private static void tf$useEnumExtendedCodec(CallbackInfo ci) {
		CODEC = IExtensibleEnum.createCodecForExtensibleEnum(GrassColorModifierMixin::values, name -> {
			for (BiomeSpecialEffects.GrassColorModifier value : values()) {
				if (value.getName().equals(name))
					return value;
			}

			return null;
		});
	}
}
