package twilightforest.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.fabricators_of_create.porting_lib.core.util.Lazy;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.armor.TFArmorModel;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class TFSimpleArmorRenderer implements ArmorRenderer {
	public static final List<TFSimpleArmorRenderer> INSTANCES = new ArrayList<>();
	protected final Lazy<HumanoidModel<LivingEntity>> INNER_ARMOR_MODEL;
	protected final Lazy<HumanoidModel<LivingEntity>> OUTER_ARMOR_MODEL;

	private final ModelLayerLocation innerLayerLocation;
	private final ModelLayerLocation outerLayerLocation;

	public TFSimpleArmorRenderer(Function<ModelPart, TFArmorModel> createModelInstance, ModelLayerLocation innerLayerLocation, ModelLayerLocation outerLayerLocation) {
		this.innerLayerLocation = innerLayerLocation;
		this.outerLayerLocation = outerLayerLocation;

		INSTANCES.add(this);
		this.INNER_ARMOR_MODEL = Lazy.of(() -> {
			ModelPart baked = Minecraft.getInstance().getEntityModels().bakeLayer(innerLayerLocation);
			return createModelInstance.apply(baked);
		});
		this.OUTER_ARMOR_MODEL = Lazy.of(() -> {
			ModelPart baked = Minecraft.getInstance().getEntityModels().bakeLayer(outerLayerLocation);
			return createModelInstance.apply(baked);
		});
	}

	// can be overridden
	public void resetModelCache() {
		INNER_ARMOR_MODEL.invalidate();
		OUTER_ARMOR_MODEL.invalidate();
	}

	public static void resetAllModelCache() {
		INSTANCES.forEach(TFSimpleArmorRenderer::resetModelCache);
	}

	@Override
	public void render(PoseStack matrices, MultiBufferSource vertexConsumers, ItemStack stack, LivingEntity entity, EquipmentSlot slot, int light, HumanoidModel<LivingEntity> contextModel) {
		var armorModel = slot == EquipmentSlot.LEGS ? INNER_ARMOR_MODEL.get() : OUTER_ARMOR_MODEL.get();
		var texture = slot == EquipmentSlot.LEGS ? innerLayerLocation : outerLayerLocation;
		contextModel.copyPropertiesTo(armorModel);

		armorModel.renderToBuffer(matrices, vertexConsumers.getBuffer(armorModel.renderType(texture.getModel())), light, OverlayTexture.NO_OVERLAY);
	}

	public static final class ResourceReloadListener implements ResourceManagerReloadListener, IdentifiableResourceReloadListener {
		@Override
		public void onResourceManagerReload(ResourceManager resourceManager) {
			TFSimpleArmorRenderer.resetAllModelCache();
		}

		@Override
		public ResourceLocation getFabricId() {
			return TwilightForestMod.prefix("simple_armor");
		}
	}
}
