package twilightforest.mixin;

import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.main.GameConfig;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.client.JappaPackReloadListener;
import twilightforest.client.MagicPaintingTextureManager;
import twilightforest.client.TextureGeneratorReloadListener;
import twilightforest.client.renderer.TFSimpleArmorRenderer;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
	@Shadow
	public abstract ResourceManager getResourceManager();

	@Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/platform/Window;updateVsync(Z)V"))
	private void onClientInit(GameConfig gameConfig, CallbackInfo ci) {
		((ReloadableResourceManager)getResourceManager()).listeners.addFirst(JappaPackReloadListener.INSTANCE);
		MagicPaintingTextureManager.instance = new MagicPaintingTextureManager(Minecraft.getInstance().getTextureManager());

		var event = ResourceManagerHelper.get(PackType.CLIENT_RESOURCES);
		event.registerReloadListener(MagicPaintingTextureManager.instance);
		event.registerReloadListener(TextureGeneratorReloadListener.INSTANCE);
		event.registerReloadListener(new TFSimpleArmorRenderer.ResourceReloadListener());
	}
}
