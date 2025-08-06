package twilightforest.compat.curios.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.client.TrinketRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.TFModelLayers;
import twilightforest.compat.curios.model.CharmOfLifeNecklaceModel;

public class CharmOfLifeNecklaceRenderer implements TrinketRenderer {

	private final CharmOfLifeNecklaceModel model;
	private final int necklaceColor;

	public CharmOfLifeNecklaceRenderer(int necklaceColor) {
		this.model = new CharmOfLifeNecklaceModel(Minecraft.getInstance().getEntityModels().bakeLayer(TFModelLayers.CHARM_OF_LIFE));
		this.necklaceColor = necklaceColor;
	}

	@Override
	public void render(ItemStack item, SlotReference slotReference, EntityModel<? extends LivingEntity> contextModel, PoseStack stack, MultiBufferSource vertexConsumers, int light, LivingEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
		if (contextModel instanceof HumanoidModel<?> model) {
			stack.pushPose();
			model.body.translateAndRotate(stack);
			stack.translate(-0.0D, 0.23D, -0.135D);
			stack.mulPose(Axis.YP.rotationDegrees(0.0F));
			stack.scale(-0.4F, -0.4F, 0.4F);
			ItemInHandRenderer renderer = new ItemInHandRenderer(Minecraft.getInstance(), Minecraft.getInstance().getEntityRenderDispatcher(), Minecraft.getInstance().getItemRenderer());
			renderer.renderItem(entity, item, ItemDisplayContext.FIXED, false, stack, vertexConsumers, light);
			stack.popPose();
		}
		this.model.setupAnim(entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
		this.model.prepareMobModel(entity, limbAngle, limbAngle, tickDelta);
		TrinketRenderer.followBodyRotations(entity, this.model);
		VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderType.entityCutout(TwilightForestMod.getModelTexture("charm_of_life_necklace.png")));
		this.model.renderToBuffer(stack, vertexConsumer, light, OverlayTexture.NO_OVERLAY, this.necklaceColor);
	}
}
