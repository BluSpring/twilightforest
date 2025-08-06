package twilightforest.client.event;

import com.ibm.icu.text.RuleBasedNumberFormat;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import dev.architectury.event.events.client.ClientTooltipEvent;
import io.github.fabricators_of_create.porting_lib.event.client.DrawSelectionEvents;
import io.github.fabricators_of_create.porting_lib.event.client.FieldOfViewEvents;
import io.github.fabricators_of_create.porting_lib.event.client.LivingEntityRenderEvents;
import io.github.fabricators_of_create.porting_lib.event.client.RenderFrameEvent;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.SplashRenderer;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.WrittenBookItem;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import twilightforest.TwilightForestMod;
import twilightforest.block.GiantBlock;
import twilightforest.block.MiniatureStructureBlock;
import twilightforest.block.entity.GrowingBeanstalkBlockEntity;
import twilightforest.client.BugModelAnimationHelper;
import twilightforest.client.ISTER;
import twilightforest.client.OptifineWarningScreen;
import twilightforest.client.TFShaders;
import twilightforest.client.renderer.entity.MagicPaintingRenderer;
import twilightforest.compat.curios.TrinketsCompat;
import twilightforest.config.TFConfig;
import twilightforest.data.tags.ItemTagGenerator;
import twilightforest.events.HostileMountEvents;
import twilightforest.fabric.DeferredBus;
import twilightforest.init.TFDataComponents;
import twilightforest.init.TFDimension;
import twilightforest.item.*;
import twilightforest.util.HolderMatcher;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

public class ClientEvents {
	private static final VoxelShape GIANT_BLOCK = Shapes.box(0.0D, 0.0D, 0.0D, 4.0D, 4.0D, 4.0D);
	private static final MutableComponent WIP_TEXT = Component.translatable("misc.twilightforest.wip").withStyle(ChatFormatting.RED);
	private static final MutableComponent EMPERORS_CLOTH_TOOLTIP = Component.translatable("item.twilightforest.emperors_cloth.desc").withStyle(ChatFormatting.GRAY);

	private static boolean firstTitleScreenShown = false;

	public static int time = 0;
	private static float shakeIntensity = 0.0F;

	private static int aurora = 0;
	private static int lastAurora = 0;

	private static HolderMatcher holderMatcher = new HolderMatcher();

	public static void initGameEvents() {
		var bus = new DeferredBus();
		bus.addListener(ClientEvents::addCustomTooltips);
		bus.addListener(ClientEvents::clientTick);
		bus.addListener(ClientEvents::customizeSplashes);
		bus.addListener(ClientEvents::handleGameBootup);
		bus.addListener(ClientEvents::killVignette);
		bus.addListener(ClientEvents::removeHostileMountHealth);
		bus.addListener(ClientEvents::renderAurora);
		bus.addListener(ClientEvents::renderCustomBossbars);
		bus.addListener(ClientEvents::renderGiantBlockOutlines);
		bus.addListener(ClientEvents::setMusicInDimension);
		bus.addListener(ClientEvents::shakeCamera);
		bus.addListener(ClientEvents::translateBookAuthor);
		bus.addListener(ClientEvents::unrenderHeadWithTrophies);
		bus.addListener(ClientEvents::updateBowFOV);

		bus.addListener(CloudEvents::renderPrecipitation);
		bus.addListener(CloudEvents::tickWeatherEffects);

		bus.addListener(FogHandler::renderFog);
		bus.addListener(FogHandler::unloadFog);

		bus.addListener(LockedBiomeToastHandler::tickLockedToastLogic);
	}

	private static void handleGameBootup() {
		ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
			if (firstTitleScreenShown || !(screen instanceof TitleScreen)) return;

			// Registering this resource listener earlier than the main screen will cause a crash
			// Yes, crashing happens if registered to RegisterClientReloadListenersEvent
			if (Minecraft.getInstance().getResourceManager() instanceof ReloadableResourceManager resourceManager) {
				resourceManager.registerReloadListener(ISTER.INSTANCE.get());
				TwilightForestMod.LOGGER.debug("Registered ISTER listener");
			}

			if (RegistrationEvents.isOptifinePresent() && !TFConfig.disableOptifineNagScreen) {
				Minecraft.getInstance().setScreen(new OptifineWarningScreen(screen));
			}

			firstTitleScreenShown = true;
		});
	}

	private static void customizeSplashes() {
		ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
			if (screen instanceof TitleScreen title) {
				SplashRenderer renderer = title.splash;
				if (renderer != null) {
					LocalDate date = LocalDate.now();
					if (date.getMonth() == Month.AUGUST && date.getDayOfMonth() == 19) {
						RuleBasedNumberFormat formatter = new RuleBasedNumberFormat(Locale.US, RuleBasedNumberFormat.ORDINAL);
						renderer.splash = String.format("Happy %s birthday to the Twilight Forest!", formatter.format(date.getYear() - 2011));
					}
				}
			}
		});
	}

	private static void setMusicInDimension() {
		/*Music music = event.getOriginalMusic();
		if (Minecraft.getInstance().level != null && Minecraft.getInstance().player != null && (music == Musics.CREATIVE || music == Musics.UNDER_WATER) && TFDimension.isTwilightWorldOnClient(Minecraft.getInstance().level)) {
			event.setMusic(Minecraft.getInstance().level.getBiomeManager().getNoiseBiomeAtPosition(Minecraft.getInstance().player.blockPosition()).value().getBackgroundMusic().orElse(Musics.GAME));
		}*/
	}

	/**
	 * Stop the game from rendering the mount health for unfriendly creatures
	 */
	private static void removeHostileMountHealth() {
		/*if (VanillaGuiLayers.VEHICLE_HEALTH == event.getName()) {
			if (HostileMountEvents.isRidingUnfriendly(Minecraft.getInstance().player)) {
				event.setCanceled(true);
			}
		}*/
	}

	/**
	 * Render aurora effect as needed
	 */
	private static void renderAurora() {
		if (Minecraft.getInstance().level == null) return;

		WorldRenderEvents.LAST.register(context -> {
			if ((aurora > 0 || lastAurora > 0) && TFShaders.AURORA != null) {
				Tesselator tesselator = Tesselator.getInstance();
				BufferBuilder buffer = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

				final float scale = 2048F * (Minecraft.getInstance().gameRenderer.getRenderDistance() / 32F);
				Vec3 pos = context.camera().getPosition();
				float y = (float) (256F - pos.y());
				buffer.addVertex(-scale, y, scale).setColor(1F, 1F, 1F, 1F);
				buffer.addVertex(-scale, y, -scale).setColor(1F, 1F, 1F, 1F);
				buffer.addVertex(scale, y, -scale).setColor(1F, 1F, 1F, 1F);
				buffer.addVertex(scale, y, scale).setColor(1F, 1F, 1F, 1F);

				RenderSystem.enableBlend();
				RenderSystem.enableDepthTest();
				RenderSystem.setShaderColor(1F, 1F, 1F, (Mth.lerp(context.tickCounter().getGameTimeDeltaTicks(), lastAurora, aurora)) / 60F * 0.5F);
				/*TFShaders.AURORA.invokeThenEndTesselator(
					Minecraft.getInstance().level == null ? 0 : Mth.abs((int) Minecraft.getInstance().level.getBiomeManager().biomeZoomSeed),
					(float) pos.x(), (float) pos.y(), (float) pos.z(), buffer);*/
				RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
				RenderSystem.disableDepthTest();
				RenderSystem.disableBlend();
			}
		});
	}

	private static void killVignette() {
		RenderFrameEvent.PRE.register(deltaTracker -> {
			Minecraft minecraft = Minecraft.getInstance();
			// only fire if we're in the twilight forest
			if (minecraft.level != null && TFDimension.DIMENSION_KEY.equals(minecraft.level.dimension())) {
				minecraft.gui.vignetteBrightness = 0.0F;
			}

			if (minecraft.player != null && HostileMountEvents.isRidingUnfriendly(minecraft.player)) {
				minecraft.gui.setOverlayMessage(Component.empty(), false);
			}
		});
	}

	private static void clientTick() {
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			Minecraft mc = Minecraft.getInstance();

			if (!mc.isPaused()) {
				time++;

				lastAurora = aurora;
				if (mc.level != null && mc.cameraEntity != null && !TFConfig.getValidAuroraBiomes(mc.level.registryAccess()).isEmpty()) {
					RegistryAccess access = mc.level.registryAccess();
					Holder<Biome> biome = mc.level.getBiome(mc.cameraEntity.blockPosition());
					if (TFConfig.getValidAuroraBiomes(access).stream().anyMatch(c -> holderMatcher.match(c, biome)))
						aurora++;
					else
						aurora--;
					aurora = Mth.clamp(aurora, 0, 60);
				} else {
					aurora = 0;
				}

				BugModelAnimationHelper.animate();

				if (mc.level != null) {
					if (mc.level.getSkyFlashTime() > 0) {
						MagicPaintingRenderer.lastLightning = mc.level.getGameTime();
					}

					if (TFConfig.firstPersonEffects && mc.player != null) {
						HashSet<ChunkPos> chunksInRange = new HashSet<>();
						for (int x = -16; x <= 16; x += 16) {
							for (int z = -16; z <= 16; z += 16) {
								chunksInRange.add(new ChunkPos((int) (mc.player.getX() + x) >> 4, (int) (mc.player.getZ() + z) >> 4));
							}
						}
						for (ChunkPos pos : chunksInRange) {
							if (mc.level.getChunk(pos.x, pos.z, ChunkStatus.FULL, false) != null) {
								List<BlockEntity> beanstalksInChunk = mc.level.getChunk(pos.x, pos.z).getBlockEntities().values().stream()
									.filter(blockEntity -> blockEntity instanceof GrowingBeanstalkBlockEntity beanstalkBlock && beanstalkBlock.isBeanstalkRumbling())
									.toList();
								if (!beanstalksInChunk.isEmpty()) {
									BlockEntity beanstalk = beanstalksInChunk.getFirst();
									Player player = mc.player;
									shakeIntensity = (float) (1.0F - mc.player.distanceToSqr(Vec3.atCenterOf(beanstalk.getBlockPos())) / Math.pow(16, 2));
									if (shakeIntensity > 0) {
										player.moveTo(player.getX(), player.getY(), player.getZ(),
											player.getYRot() + (player.getRandom().nextFloat() - 0.5F) * shakeIntensity,
											player.getXRot() + (player.getRandom().nextFloat() * 2.5F - 1.25F) * shakeIntensity);
										shakeIntensity = 0.0F;
										break;
									}
								}
							}
						}
					}
				}
			}
		});
	}

	private static void shakeCamera() {
		/*if (TFConfig.firstPersonEffects && !Minecraft.getInstance().isPaused() && shakeIntensity > 0 && Minecraft.getInstance().player != null) {
			event.setYaw((float) Mth.lerp(event.getPartialTick(), event.getYaw(), event.getYaw() + (Minecraft.getInstance().player.getRandom().nextFloat() * 2F - 1F) * shakeIntensity));
			event.setPitch((float) Mth.lerp(event.getPartialTick(), event.getPitch(), event.getPitch() + (Minecraft.getInstance().player.getRandom().nextFloat() * 2F - 1F) * shakeIntensity));
			event.setRoll((float) Mth.lerp(event.getPartialTick(), event.getRoll(), event.getRoll() + (Minecraft.getInstance().player.getRandom().nextFloat() * 2F - 1F) * shakeIntensity));
			shakeIntensity = 0F;
		}*/
	}

	private static void addCustomTooltips() {
		ClientTooltipEvent.ITEM.register((item, lines, tooltipContext, flag) -> {
			if (item.has(TFDataComponents.EMPERORS_CLOTH.get())) {
				lines.add(1, EMPERORS_CLOTH_TOOLTIP);
			}

			if (item.is(ItemTagGenerator.WIP)) {
				lines.add(WIP_TEXT);
			}
		});
	}

	/**
	 * Zooms in the FOV while using a bow, just like vanilla does in the AbstractClientPlayer's getFieldOfViewModifier() method (1.18.2)
	 */
	private static void updateBowFOV() {
		FieldOfViewEvents.MODIFY.register((player, fovModifier) -> {
			if (player.isUsingItem()) {
				Item useItem = player.getUseItem().getItem();
				if (useItem instanceof TripleBowItem || useItem instanceof EnderBowItem || useItem instanceof IceBowItem || useItem instanceof SeekerBowItem) {
					float f = player.getTicksUsingItem() / 20.0F;
					f = f > 1.0F ? 1.0F : f * f;
					return ((float) Mth.lerp(Minecraft.getInstance().options.fovEffectScale().get(), 1.0F, (fovModifier * (1.0F - f * 0.15F))));
				}
			}

			return fovModifier;
		});
	}

	private static void unrenderHeadWithTrophies() {
		LivingEntityRenderEvents.PRE.register((entity, renderer, partialRenderTick, matrixStack, buffers, light) -> {
			ItemStack stack = entity.getItemBySlot(EquipmentSlot.HEAD);
			boolean visible = !(stack.getItem() instanceof TrophyItem) && !areTrinketsEquipped(entity);
			boolean isPlayer = entity instanceof Player;
			if (renderer.getModel() instanceof HeadedModel headedModel) {
				headedModel.getHead().visible = visible && (!isPlayer || headedModel.getHead().visible);  // some mods like Better Combat can move player's head and hide it in the first person view
				if (renderer.getModel() instanceof HumanoidModel<?> humanoidModel) {
					humanoidModel.hat.visible = visible && (!isPlayer || humanoidModel.hat.visible);
				}
			}

			return false;
		});
	}

	private static boolean areTrinketsEquipped(LivingEntity entity) {
		if (FabricLoader.getInstance().isModLoaded("trinkets")) {
			return TrinketsCompat.isCurioEquippedAndVisible(entity, stack -> stack.getItem() instanceof TrophyItem);
		}
		return false;
	}

	private static void translateBookAuthor() {
		ClientTooltipEvent.ITEM.register((stack, components, tooltipContext, flag) -> {
			if (stack.getItem() instanceof WrittenBookItem && stack.has(DataComponents.WRITTEN_BOOK_CONTENT)) {
				if (stack.has(TFDataComponents.TRANSLATABLE_BOOK.get())) {
					for (int i = 0; i < components.size(); i++) {
						Component component = components.get(i);
						if (component.toString().contains("book.byAuthor")) {
							components.set(i, (Component.translatable("book.byAuthor", Component.translatable(TwilightForestMod.ID + ".book.author"))).withStyle(component.getStyle()));
						}
					}
				}
			}
		});
	}

	private static void renderGiantBlockOutlines() {
		DrawSelectionEvents.BLOCK.register((context, info, target, deltaTracker, matrix, buffers) -> {
			if (!(target instanceof BlockHitResult hitResult))
				return false;

			BlockPos pos = hitResult.getBlockPos();
			BlockState state = info.getEntity().level().getBlockState(pos);

			if (state.getBlock() instanceof MiniatureStructureBlock) {
				return true;
			}

			LocalPlayer player = Minecraft.getInstance().player;
			if (player != null && (player.getMainHandItem().getItem() instanceof GiantPickItem || (player.getMainHandItem().getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof GiantBlock))) {
				if (!state.isAir() && player.level().getWorldBorder().isWithinBounds(pos)) {
					BlockPos offsetPos = new BlockPos(pos.getX() & ~0b11, pos.getY() & ~0b11, pos.getZ() & ~0b11);
					VertexConsumer consumer = buffers.getBuffer(RenderType.lines());
					Vec3 xyz = Vec3.atLowerCornerOf(offsetPos).subtract(info.getPosition());
					LevelRenderer.renderShape(matrix, consumer, GIANT_BLOCK, xyz.x(), xyz.y(), xyz.z(), 0.0F, 0.0F, 0.0F, 0.45F);
				}

				return true;
			}

			return false;
		});

	}

	private static void renderCustomBossbars() {
		/*if (event.getBossEvent() instanceof ClientTFBossBar bossEvent) {
			event.setCanceled(true);
			bossEvent.renderBossBar(event.getGuiGraphics(), event.getX(), event.getY());
		}*/
	}
}
