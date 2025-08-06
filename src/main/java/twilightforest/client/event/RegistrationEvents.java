package twilightforest.client.event;

import dev.architectury.registry.client.gui.ClientTooltipComponentRegistry;
import dev.architectury.registry.client.particle.ParticleProviderRegistry;
import io.github.fabricators_of_create.porting_lib.event.client.EntityAddedLayerCallback;
import io.github.fabricators_of_create.porting_lib.models.geometry.RegisterGeometryLoadersCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.mixin.client.rendering.LivingEntityRendererAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.LayerDefinitions;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.FlameParticle;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.maps.MapDecorationTypes;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.client.*;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.armor.*;
import twilightforest.client.model.block.BrazierModel;
import twilightforest.client.model.block.ReactorDebrisModel;
import twilightforest.client.model.block.aurorablock.NoiseVaryingModelLoader;
import twilightforest.client.model.block.carpet.RoyalRagsModelLoader;
import twilightforest.client.model.block.connected.ConnectedTextureModelLoader;
import twilightforest.client.model.block.forcefield.ForceFieldModelLoader;
import twilightforest.client.model.block.giantblock.GiantBlockModelLoader;
import twilightforest.client.model.block.leaves.BakedLeavesModel;
import twilightforest.client.model.block.patch.PatchModelLoader;
import twilightforest.client.model.entity.*;
import twilightforest.client.model.item.TrollsteinnModel;
import twilightforest.client.particle.*;
import twilightforest.client.renderer.PotionFlaskTooltipComponent;
import twilightforest.client.renderer.TFSimpleArmorRenderer;
import twilightforest.client.renderer.TFSkyRenderer;
import twilightforest.client.renderer.block.*;
import twilightforest.client.renderer.entity.*;
import twilightforest.client.renderer.entity.layers.IceLayer;
import twilightforest.client.renderer.entity.layers.ShieldLayer;
import twilightforest.client.renderer.map.ConqueredMapIconRenderer;
import twilightforest.client.renderer.map.MagicMapPlayerIconRenderer;
import twilightforest.components.item.PotionFlaskComponent;
import twilightforest.fabric.DeferredBus;
import twilightforest.fabric.IMapDecorationRenderer;
import twilightforest.init.*;
import twilightforest.item.*;
import twilightforest.mixin.DimensionSpecialEffectsAccessor;

import java.util.List;
import java.util.Map;

public class RegistrationEvents {

	private static boolean optifinePresent = false;

	public static void initModBusEvents() {
		var bus = new DeferredBus();

		RegistrationEvents.attachRenderLayers();
		bus.addListener(RegistrationEvents::bakeCustomModels);
		bus.addListener(RegistrationEvents::cacheJarLids);
		bus.addListener(RegistrationEvents::clientSetup);
		bus.addListener(RegistrationEvents::registerAdditionalModels);
		bus.addListener(RegistrationEvents::registerClientReloadListeners);
		bus.addListener(RegistrationEvents::registerDimEffects);
		bus.addListener(RegistrationEvents::registerEntityRenderers);
		bus.addListener(RegistrationEvents::registerLayerDefinitions);
		bus.addListener(RegistrationEvents::registerModelLoaders);
		bus.addListener(RegistrationEvents::registerScreens);
		bus.addListener(RegistrationEvents::registerClientExtensions);
		bus.addListener(RegistrationEvents::registerMapDecorators);
		bus.addListener(RegistrationEvents::registerParticleFactories);

		bus.addListener(ColorHandler::registerBlockColors);
		bus.addListener(ColorHandler::registerItemColors);

		bus.addListener(OverlayHandler::registerOverlays);

		bus.addListener(TFShaders::registerShaders);

		ClientTooltipComponentRegistry.register(BrittleFlaskItem.Tooltip.class, PotionFlaskTooltipComponent::new);
	}

	private static void registerModelLoaders() {
		RegisterGeometryLoadersCallback.EVENT.register(loaders -> {
			loaders.put(TwilightForestMod.prefix("patch"), PatchModelLoader.INSTANCE);
			loaders.put(TwilightForestMod.prefix("giant_block"), GiantBlockModelLoader.INSTANCE);
			loaders.put(TwilightForestMod.prefix("force_field"), ForceFieldModelLoader.INSTANCE);
			loaders.put(TwilightForestMod.prefix("connected_texture_block"), ConnectedTextureModelLoader.INSTANCE);
			loaders.put(TwilightForestMod.prefix("noise_varying"), NoiseVaryingModelLoader.INSTANCE);
			loaders.put(TwilightForestMod.prefix("royal_rags"), RoyalRagsModelLoader.INSTANCE);
		});
	}

	private static void bakeCustomModels() {
		ModelLoadingPlugin.register(pluginContext -> {
			pluginContext.modifyModelAfterBake().register((model, context) -> {
				if (context.resourceId() != null && context.resourceId().equals(TwilightForestMod.prefix("reactor_debris"))) {
					return new ReactorDebrisModel(model);
				} else if (ModelResourceLocation.inventory(TwilightForestMod.prefix("trollsteinn")).equals(context.topLevelId())) {
					return new TrollsteinnModel(model);
				} else if (context.resourceId() != null && context.resourceId().getNamespace().equals(TwilightForestMod.ID) && context.resourceId().getPath().contains("leaves") && !context.resourceId().getPath().contains("dark")) {
					return new BakedLeavesModel(model);
				}

				return model;
			});
		});
		ItemProperties.register(TFItems.CUBE_OF_ANNIHILATION.get(), TwilightForestMod.prefix("thrown"), (stack, level, entity, idk) ->
			stack.get(TFDataComponents.THROWN_PROJECTILE.get()) != null ? 1 : 0);

		ItemProperties.register(TFItems.KNIGHTMETAL_SHIELD.get(), ResourceLocation.parse("blocking"), (stack, level, entity, idk) ->
			entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);

		ItemProperties.register(TFItems.MOON_DIAL.get(), ResourceLocation.parse("phase"), new ClampedItemPropertyFunction() {
			@Override
			public float unclampedCall(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entityBase, int idk) {
				boolean flag = entityBase != null;
				Entity entity = flag ? entityBase : stack.getFrame();

				if (level == null && entity != null) level = (ClientLevel) entity.level();

				return level == null ? 0.0F : (float) (level.dimensionType().natural() ? Mth.frac(level.getMoonPhase() / 8.0f) : this.wobble(level, Math.random()));
			}

			double rotation;
			double rota;
			long lastUpdateTick;

			private double wobble(Level level, double rotation) {
				if (level.getGameTime() != this.lastUpdateTick) {
					this.lastUpdateTick = level.getGameTime();
					double delta = rotation - this.rotation;
					delta = Mth.positiveModulo(delta + 0.5D, 1.0D) - 0.5D;
					this.rota += delta * 0.1D;
					this.rota *= 0.9D;
					this.rotation = Mth.positiveModulo(this.rotation + this.rota, 1.0D);
				}
				return this.rotation;
			}
		});

		ItemProperties.register(TFItems.ORE_METER.get(), TwilightForestMod.prefix("active"), (stack, level, entity, idk) -> {
			if (OreMeterItem.isLoading(stack)) {
				int totalLoadTime = OreMeterItem.LOAD_TIME + OreMeterItem.getRange(stack) * 25;
				int progress = OreMeterItem.getLoadProgress(stack);
				return progress % 5 >= 2 + (int) (Math.random() * 2) && progress <= totalLoadTime - 15 ? 1 : 0;
			}
			return stack.has(TFDataComponents.ORE_DATA.get()) ? 1 : 0;
		});

		ItemProperties.register(TFItems.MOONWORM_QUEEN.get(), TwilightForestMod.prefix("alt"), (stack, level, entity, idk) -> {
			if (entity != null && entity.getUseItem() == stack) {
				int useTime = stack.getUseDuration(entity) - entity.getUseItemRemainingTicks();
				if (useTime >= MoonwormQueenItem.FIRING_TIME && (useTime >>> 1) % 2 == 0) {
					return 1;
				}
			}
			return 0;
		});

		ItemProperties.register(TFItems.ENDER_BOW.get(), ResourceLocation.parse("pull"), (stack, level, entity, idk) -> {
			if (entity == null) return 0.0F;
			else
				return entity.getUseItem() != stack ? 0.0F : (stack.getUseDuration(entity) - entity.getUseItemRemainingTicks()) / 20.0F;
		});

		ItemProperties.register(TFItems.ENDER_BOW.get(), ResourceLocation.parse("pulling"), (stack, level, entity, idk) ->
			entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);

		ItemProperties.register(TFItems.ICE_BOW.get(), ResourceLocation.parse("pull"), (stack, level, entity, idk) -> {
			if (entity == null) return 0.0F;
			else
				return entity.getUseItem() != stack ? 0.0F : (stack.getUseDuration(entity) - entity.getUseItemRemainingTicks()) / 20.0F;
		});

		ItemProperties.register(TFItems.ICE_BOW.get(), ResourceLocation.parse("pulling"), (stack, level, entity, idk) ->
			entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);

		ItemProperties.register(TFItems.SEEKER_BOW.get(), ResourceLocation.parse("pull"), (stack, level, entity, idk) -> {
			if (entity == null) return 0.0F;
			else
				return entity.getUseItem() != stack ? 0.0F : (stack.getUseDuration(entity) - entity.getUseItemRemainingTicks()) / 20.0F;
		});

		ItemProperties.register(TFItems.SEEKER_BOW.get(), ResourceLocation.parse("pulling"), (stack, level, entity, idk) ->
			entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);

		ItemProperties.register(TFItems.TRIPLE_BOW.get(), ResourceLocation.parse("pull"), (stack, level, entity, idk) -> {
			if (entity == null) return 0.0F;
			else
				return entity.getUseItem() != stack ? 0.0F : (stack.getUseDuration(entity) - entity.getUseItemRemainingTicks()) / 20.0F;
		});

		ItemProperties.register(TFItems.TRIPLE_BOW.get(), ResourceLocation.parse("pulling"), (stack, level, entity, idk) ->
			entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);

		ItemProperties.register(TFItems.ORE_MAGNET.get(), ResourceLocation.parse("pull"), (stack, level, entity, idk) -> {
			if (entity == null) return 0.0F;
			else {
				ItemStack itemstack = entity.getUseItem();
				return !itemstack.isEmpty() ? (stack.getUseDuration(entity) - entity.getUseItemRemainingTicks()) / 20.0F : 0.0F;
			}
		});

		ItemProperties.register(TFBlocks.RED_THREAD.get().asItem(), TwilightForestMod.prefix("size"), (stack, level, entity, idk) -> {
			if (stack.getCount() >= 32) {
				return 1.0F;
			} else if (stack.getCount() >= 16) {
				return 0.5F;
			} else if (stack.getCount() >= 4) {
				return 0.25F;
			}
			return 0.0F;
		});

		ItemProperties.register(TFItems.ORE_MAGNET.get(), ResourceLocation.parse("pulling"), (stack, level, entity, idk) ->
			entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);

		ItemProperties.register(TFItems.BLOCK_AND_CHAIN.get(), TwilightForestMod.prefix("thrown"), (stack, level, entity, idk) ->
			stack.get(TFDataComponents.THROWN_PROJECTILE.get()) != null ? 1 : 0);

		ItemProperties.register(TFItems.EXPERIMENT_115.get(), Experiment115Item.THINK, (stack, level, entity, idk) ->
			stack.get(TFDataComponents.EXPERIMENT_115_VARIANTS.get()) != null && stack.get(TFDataComponents.EXPERIMENT_115_VARIANTS.get()).equals("think") ? 1 : 0);

		ItemProperties.register(TFItems.EXPERIMENT_115.get(), Experiment115Item.FULL, (stack, level, entity, idk) ->
			stack.get(TFDataComponents.EXPERIMENT_115_VARIANTS.get()) != null && stack.get(TFDataComponents.EXPERIMENT_115_VARIANTS.get()).equals("full") ? 1 : 0);

		ItemProperties.register(TFItems.BRITTLE_FLASK.get(), TwilightForestMod.prefix("breakage"), (stack, level, entity, i) ->
			stack.getOrDefault(TFDataComponents.POTION_FLASK_CONTENTS.get(), PotionFlaskComponent.EMPTY).breakage());

		ItemProperties.register(TFItems.BRITTLE_FLASK.get(), TwilightForestMod.prefix("potion_level"), (stack, level, entity, i) ->
			stack.getOrDefault(TFDataComponents.POTION_FLASK_CONTENTS.get(), PotionFlaskComponent.EMPTY).doses());

		ItemProperties.register(TFItems.GREATER_FLASK.get(), TwilightForestMod.prefix("potion_level"), (stack, level, entity, i) ->
			stack.getOrDefault(TFDataComponents.POTION_FLASK_CONTENTS.get(), PotionFlaskComponent.EMPTY).doses());

		ItemProperties.register(TFItems.CRUMBLE_HORN.get(), TwilightForestMod.prefix("tooting"), (stack, world, entity, i) ->
			entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F
		);
	}

	private static void registerAdditionalModels() {
		ModelLoadingPlugin.register(pluginContext -> {
			pluginContext.addModels(ShieldLayer.LOC.id(), TwilightForestMod.prefix("item/trophy"), TwilightForestMod.prefix("item/trophy_minor"), TwilightForestMod.prefix("item/trophy_quest"));
			pluginContext.addModels(TrollsteinnModel.LIT_TROLLSTEINN.id());

			for (JarRenderer.LidResource lid : JarRenderer.LID_LOCATION_LIST.get()) {
				ResourceLocation location = lid.resourceLocation();
				String name = location.getPath();
				if (lid.customPath() != null) name = lid.customPath();
				pluginContext.addModels(TwilightForestMod.prefix("block/lid/" + name));
			}
		});
	}

	private static void cacheJarLids() {
		ModelLoadingPlugin.register(pluginContext -> {
			pluginContext.modifyModelAfterBake().register((model, context) -> {
				JarRenderer.LID_LOCATION_LIST.get().forEach((lid) -> {
					String name = lid.resourceLocation().getPath();
					if (lid.customPath() != null) name = lid.customPath();

					if (context.resourceId() != null && context.resourceId().equals(TwilightForestMod.prefix("block/lid/" + name))) {
						JarRenderer.LIDS.put(lid.lid(), model);
					} else if (context.topLevelId() != null && context.topLevelId().id().equals(TwilightForestMod.prefix("block/lid/" + name))) {
						JarRenderer.LIDS.put(lid.lid(), model);
					}
				});

				return model;
			});
		});
	}

	private static void registerDimEffects() {
		ClientLifecycleEvents.CLIENT_STARTED.register(((client) -> {
			TFSkyRenderer.createStars();
			DimensionSpecialEffectsAccessor.getEffects().put(TFDimension.DIMENSION_RENDERER, new TwilightForestRenderInfo(128.0F, false, DimensionSpecialEffects.SkyType.NONE, false, false));
		}));
	}

	private static void clientSetup() {
		try {
			Class.forName("net.optifine.Config");
			optifinePresent = true;
		} catch (ClassNotFoundException e) {
			optifinePresent = false;
		}

		/*evt.enqueueWork(() -> {
			Sheets.addWoodType(TFWoodTypes.TWILIGHT_OAK_WOOD_TYPE);
			Sheets.addWoodType(TFWoodTypes.CANOPY_WOOD_TYPE);
			Sheets.addWoodType(TFWoodTypes.MANGROVE_WOOD_TYPE);
			Sheets.addWoodType(TFWoodTypes.DARK_WOOD_TYPE);
			Sheets.addWoodType(TFWoodTypes.TIME_WOOD_TYPE);
			Sheets.addWoodType(TFWoodTypes.TRANSFORMATION_WOOD_TYPE);
			Sheets.addWoodType(TFWoodTypes.MINING_WOOD_TYPE);
			Sheets.addWoodType(TFWoodTypes.SORTING_WOOD_TYPE);
		});*/
	}

	private static void registerClientReloadListeners() {

	}

	private static void registerScreens() {
		MenuScreens.register(TFMenuTypes.UNCRAFTING.get(), UncraftingScreen::new);
	}

	private static void registerEntityRenderers() {
		EntityRendererRegistry.register(TFEntities.BOAR.get(), m -> new BoarRenderer<>(m, new BoarModel<>(m.bakeLayer(TFModelLayers.BOAR))));
		EntityRendererRegistry.register(TFEntities.BIGHORN_SHEEP.get(), m -> new BighornRenderer(m, new BighornModel<>(m.bakeLayer(TFModelLayers.BIGHORN_SHEEP)), 0.7F));
		EntityRendererRegistry.register(TFEntities.DEER.get(), m -> new TFGenericMobRenderer<>(m, new DeerModel(m.bakeLayer(TFModelLayers.DEER)), 0.7F, "wilddeer.png"));
		EntityRendererRegistry.register(TFEntities.REDCAP.get(), m -> new TFBipedRenderer<>(m, new RedcapModel<>(m.bakeLayer(TFModelLayers.REDCAP)), new RedcapModel<>(m.bakeLayer(TFModelLayers.REDCAP_ARMOR_INNER)), new RedcapModel<>(m.bakeLayer(TFModelLayers.REDCAP_ARMOR_OUTER)), 0.4F, "redcap.png"));
		EntityRendererRegistry.register(TFEntities.SKELETON_DRUID.get(), m -> new TFBipedRenderer<>(m, new SkeletonDruidModel(m.bakeLayer(TFModelLayers.SKELETON_DRUID)), 0.5F, "skeletondruid.png"));
		EntityRendererRegistry.register(TFEntities.HOSTILE_WOLF.get(), HostileWolfRenderer::new);
		EntityRendererRegistry.register(TFEntities.WRAITH.get(), m -> new WraithRenderer(m, new WraithModel(m.bakeLayer(TFModelLayers.WRAITH)), 0.5F));
		EntityRendererRegistry.register(TFEntities.HYDRA.get(), m -> new HydraRenderer<>(m, new HydraModel(m.bakeLayer(TFModelLayers.HYDRA)), 4.0F));
		EntityRendererRegistry.register(TFEntities.LICH.get(), m -> new LichRenderer<>(m, new LichModel<>(m.bakeLayer(TFModelLayers.LICH)), 0.6F));
		EntityRendererRegistry.register(TFEntities.PENGUIN.get(), m -> new BirdRenderer<>(m, new PenguinModel(m.bakeLayer(TFModelLayers.PENGUIN)), 0.375F, "penguin.png"));
		EntityRendererRegistry.register(TFEntities.LICH_MINION.get(), m -> new TFBipedRenderer<>(m, new LichMinionModel(m.bakeLayer(TFModelLayers.LICH_MINION)), new LichMinionModel(m.bakeLayer(ModelLayers.ZOMBIE_INNER_ARMOR)), new LichMinionModel(m.bakeLayer(ModelLayers.ZOMBIE_OUTER_ARMOR)), 0.5F, "textures/entity/zombie/zombie.png"));
		EntityRendererRegistry.register(TFEntities.LOYAL_ZOMBIE.get(), m -> new TFBipedRenderer<>(m, new LoyalZombieModel(m.bakeLayer(TFModelLayers.LOYAL_ZOMBIE)), new LoyalZombieModel(m.bakeLayer(ModelLayers.ZOMBIE_INNER_ARMOR)), new LoyalZombieModel(m.bakeLayer(ModelLayers.ZOMBIE_OUTER_ARMOR)), 0.5F, "textures/entity/zombie/zombie.png"));
		EntityRendererRegistry.register(TFEntities.TINY_BIRD.get(), m -> new TinyBirdRenderer<>(m, new TinyBirdModel(m.bakeLayer(TFModelLayers.TINY_BIRD)), 0.3F));
		EntityRendererRegistry.register(TFEntities.SQUIRREL.get(), m -> new TFGenericMobRenderer<>(m, new SquirrelModel(m.bakeLayer(TFModelLayers.SQUIRREL)), 0.3F, "squirrel2.png"));
		EntityRendererRegistry.register(TFEntities.DWARF_RABBIT.get(), m -> new BunnyRenderer(m, new BunnyModel(m.bakeLayer(TFModelLayers.BUNNY)), 0.3F));
		EntityRendererRegistry.register(TFEntities.RAVEN.get(), m -> new BirdRenderer<>(m, new RavenModel(m.bakeLayer(TFModelLayers.RAVEN)), 0.3F, "raven.png"));
		EntityRendererRegistry.register(TFEntities.QUEST_RAM.get(), m -> new QuestRamRenderer<>(m, new QuestRamModel<>(m.bakeLayer(TFModelLayers.QUEST_RAM))));
		EntityRendererRegistry.register(TFEntities.KOBOLD.get(), m -> new TFBipedRenderer<>(m, new KoboldModel(m.bakeLayer(TFModelLayers.KOBOLD)), 0.4F, "kobold.png"));
		//EntityRendererRegistry.register(TFEntities.BOGGARD.get(), m -> new RenderTFBiped<>(m, new BipedModel<>(0), 0.625F, "kobold.png"));
		EntityRendererRegistry.register(TFEntities.MOSQUITO_SWARM.get(), MosquitoSwarmRenderer::new);
		EntityRendererRegistry.register(TFEntities.DEATH_TOME.get(), m -> new TFGenericMobRenderer<>(m, new DeathTomeModel(m.bakeLayer(TFModelLayers.DEATH_TOME)), 0.3F, "textures/entity/enchanting_table_book.png"));
		EntityRendererRegistry.register(TFEntities.MINOTAUR.get(), m -> new TFBipedRenderer<>(m, new MinotaurModel(m.bakeLayer(TFModelLayers.MINOTAUR)), 0.625F, "minotaur.png"));
		EntityRendererRegistry.register(TFEntities.MINOSHROOM.get(), m -> new MinoshroomRenderer<>(m, new MinoshroomModel<>(m.bakeLayer(TFModelLayers.MINOSHROOM)), 0.625F));
		EntityRendererRegistry.register(TFEntities.FIRE_BEETLE.get(), m -> new TFGenericMobRenderer<>(m, new FireBeetleModel(m.bakeLayer(TFModelLayers.FIRE_BEETLE)), 0.8F, "firebeetle.png"));
		EntityRendererRegistry.register(TFEntities.SLIME_BEETLE.get(), m ->  new SlimeBeetleRenderer<>(m, new SlimeBeetleModel<>(m.bakeLayer(TFModelLayers.SLIME_BEETLE)), m.bakeLayer(TFModelLayers.SLIME_BEETLE_TAIL), 0.6F));
		EntityRendererRegistry.register(TFEntities.PINCH_BEETLE.get(), m -> new TFGenericMobRenderer<>(m, new PinchBeetleModel(m.bakeLayer(TFModelLayers.PINCH_BEETLE)), 0.6F, "pinchbeetle.png"));
		EntityRendererRegistry.register(TFEntities.MIST_WOLF.get(), MistWolfRenderer::new);
		EntityRendererRegistry.register(TFEntities.CARMINITE_GHASTLING.get(), m -> new TFGhastRenderer<>(m, new TFGhastModel<>(m.bakeLayer(TFModelLayers.CARMINITE_GHASTLING)), 0.625F));
		EntityRendererRegistry.register(TFEntities.CARMINITE_GOLEM.get(), m -> new CarminiteGolemRenderer<>(m, new CarminiteGolemModel<>(m.bakeLayer(TFModelLayers.CARMINITE_GOLEM)), 0.75F));
		EntityRendererRegistry.register(TFEntities.TOWERWOOD_BORER.get(), m -> new TFGenericMobRenderer<>(m, new SilverfishModel<>(m.bakeLayer(ModelLayers.SILVERFISH)), 0.3F, "towertermite.png"));
		EntityRendererRegistry.register(TFEntities.CARMINITE_GHASTGUARD.get(), m -> new CarminiteGhastRenderer<>(m, new TFGhastModel<>(m.bakeLayer(TFModelLayers.CARMINITE_GHASTGUARD)), 3.0F));
		EntityRendererRegistry.register(TFEntities.UR_GHAST.get(), m -> new UrGhastRenderer<>(m, new UrGhastModel(m.bakeLayer(TFModelLayers.UR_GHAST)), 8.0F, 24.0F));
		EntityRendererRegistry.register(TFEntities.BLOCKCHAIN_GOBLIN.get(), m -> new BlockChainGoblinRenderer<>(m, new BlockChainGoblinModel<>(m.bakeLayer(TFModelLayers.BLOCKCHAIN_GOBLIN)), 0.4F));
		EntityRendererRegistry.register(TFEntities.UPPER_GOBLIN_KNIGHT.get(), m -> new UpperGoblinKnightRenderer<>(m, new UpperGoblinKnightModel(m.bakeLayer(TFModelLayers.UPPER_GOBLIN_KNIGHT)), 0.625F));
		EntityRendererRegistry.register(TFEntities.LOWER_GOBLIN_KNIGHT.get(), m -> new TFBipedRenderer<>(m, new LowerGoblinKnightModel(m.bakeLayer(TFModelLayers.LOWER_GOBLIN_KNIGHT)), 0.625F, "doublegoblin.png"));
		EntityRendererRegistry.register(TFEntities.HELMET_CRAB.get(), m -> new TFGenericMobRenderer<>(m, new HelmetCrabModel(m.bakeLayer(TFModelLayers.HELMET_CRAB)), 0.625F, "helmetcrab.png"));
		EntityRendererRegistry.register(TFEntities.KNIGHT_PHANTOM.get(), m -> new KnightPhantomRenderer(m, new KnightPhantomModel(m.bakeLayer(TFModelLayers.KNIGHT_PHANTOM)), 0.625F));
		EntityRendererRegistry.register(TFEntities.NAGA.get(), m -> new NagaRenderer<>(m, new NagaModel<>(m.bakeLayer(TFModelLayers.NAGA)), 1.45F));
		EntityRendererRegistry.register(TFEntities.SWARM_SPIDER.get(), m -> new TFSpiderRenderer<>(m, 0.25F, "swarmspider.png", 0.5F));
		EntityRendererRegistry.register(TFEntities.KING_SPIDER.get(), m -> new TFSpiderRenderer<>(m, 1.25F, "kingspider.png", 1.9F));
		EntityRendererRegistry.register(TFEntities.CARMINITE_BROODLING.get(), m -> new TFSpiderRenderer<>(m, 0.6F, "towerbroodling.png", 0.7F));
		EntityRendererRegistry.register(TFEntities.HEDGE_SPIDER.get(), m -> new TFSpiderRenderer<>(m, 0.8F, "hedgespider.png", 1.0F));
		EntityRendererRegistry.register(TFEntities.REDCAP_SAPPER.get(), m -> new TFBipedRenderer<>(m, new RedcapModel<>(m.bakeLayer(TFModelLayers.REDCAP)), new RedcapModel<>(m.bakeLayer(TFModelLayers.REDCAP_ARMOR_INNER)), new RedcapModel<>(m.bakeLayer(TFModelLayers.REDCAP_ARMOR_OUTER)), 0.4F, "redcapsapper.png"));
		EntityRendererRegistry.register(TFEntities.MAZE_SLIME.get(), m -> new MazeSlimeRenderer(m, 0.625F));
		EntityRendererRegistry.register(TFEntities.YETI.get(), m -> new TFBipedRenderer<>(m, new YetiModel<>(m.bakeLayer(TFModelLayers.YETI)), 0.625F, "yeti2.png"));
		EntityRendererRegistry.register(TFEntities.PROTECTION_BOX.get(), ProtectionBoxRenderer::new);
		EntityRendererRegistry.register(TFEntities.MAGIC_PAINTING.get(), MagicPaintingRenderer::new);
		EntityRendererRegistry.register(TFEntities.ALPHA_YETI.get(), m -> new TFBipedRenderer<>(m, new AlphaYetiModel(m.bakeLayer(TFModelLayers.ALPHA_YETI)), 1.75F, "yetialpha.png"));
		EntityRendererRegistry.register(TFEntities.WINTER_WOLF.get(), WinterWolfRenderer::new);
		EntityRendererRegistry.register(TFEntities.SNOW_GUARDIAN.get(), m -> new SnowGuardianRenderer(m, new NoopModel<>(m.bakeLayer(TFModelLayers.NOOP))));
		EntityRendererRegistry.register(TFEntities.STABLE_ICE_CORE.get(), m -> new StableIceCoreRenderer(m, new StableIceCoreModel(m.bakeLayer(TFModelLayers.STABLE_ICE_CORE))));
		EntityRendererRegistry.register(TFEntities.UNSTABLE_ICE_CORE.get(), m -> new UnstableIceCoreRenderer<>(m, new UnstableIceCoreModel<>(m.bakeLayer(TFModelLayers.UNSTABLE_ICE_CORE))));
		EntityRendererRegistry.register(TFEntities.SNOW_QUEEN.get(), m -> new SnowQueenRenderer<>(m, new SnowQueenModel(m.bakeLayer(TFModelLayers.SNOW_QUEEN))));
		EntityRendererRegistry.register(TFEntities.TROLL.get(), m -> new TFBipedRenderer<>(m, new TrollModel(m.bakeLayer(TFModelLayers.TROLL)), 0.625F, "troll.png"));
		EntityRendererRegistry.register(TFEntities.GIANT_MINER.get(), TFGiantRenderer::new);
		EntityRendererRegistry.register(TFEntities.ARMORED_GIANT.get(), TFGiantRenderer::new);
		EntityRendererRegistry.register(TFEntities.ICE_CRYSTAL.get(), IceCrystalRenderer::new);
		EntityRendererRegistry.register(TFEntities.CHAIN_BLOCK.get(), BlockChainRenderer::new);
		EntityRendererRegistry.register(TFEntities.CUBE_OF_ANNIHILATION.get(), CubeOfAnnihilationRenderer::new);
		EntityRendererRegistry.register(TFEntities.HARBINGER_CUBE.get(), m -> new TFGenericMobRenderer<>(m, new HarbingerCubeModel<>(m.bakeLayer(TFModelLayers.HARBINGER_CUBE)), 1.0F, "apocalypse2.png"));
		EntityRendererRegistry.register(TFEntities.ADHERENT.get(), AdherentRenderer::new);
		EntityRendererRegistry.register(TFEntities.ROVING_CUBE.get(), RovingCubeRenderer::new);
		EntityRendererRegistry.register(TFEntities.RISING_ZOMBIE.get(), RisingZombieRenderer::new);
		EntityRendererRegistry.register(TFEntities.PLATEAU_BOSS.get(), NoopRenderer::new);

		// projectiles
		EntityRendererRegistry.register(TFEntities.NATURE_BOLT.get(), ThrownItemRenderer::new);
		EntityRendererRegistry.register(TFEntities.LICH_BOLT.get(), c -> new CustomProjectileTextureRenderer(c, TwilightForestMod.prefix("textures/particle/twilight_orb.png"), 1.0F, true, false));
		EntityRendererRegistry.register(TFEntities.WAND_BOLT.get(), c -> new CustomProjectileTextureRenderer(c, TwilightForestMod.prefix("textures/particle/twilight_orb.png"), 1.0F, true, false));
		EntityRendererRegistry.register(TFEntities.LICH_BOMB.get(), c -> new CustomProjectileTextureRenderer(c, ResourceLocation.withDefaultNamespace("textures/item/magma_cream.png"), 1.0F, true, true));
		EntityRendererRegistry.register(TFEntities.TOME_BOLT.get(), ThrownItemRenderer::new);
		EntityRendererRegistry.register(TFEntities.HYDRA_MORTAR.get(), HydraMortarRenderer::new);
		EntityRendererRegistry.register(TFEntities.SLIME_BLOB.get(), ThrownItemRenderer::new);
		EntityRendererRegistry.register(TFEntities.MOONWORM_SHOT.get(), MoonwormShotRenderer::new);
		EntityRendererRegistry.register(TFEntities.CHARM_EFFECT.get(), ThrownItemRenderer::new);
		EntityRendererRegistry.register(TFEntities.THROWN_WEP.get(), ThrownWepRenderer::new);
		EntityRendererRegistry.register(TFEntities.FALLING_ICE.get(), FallingIceRenderer::new);
		EntityRendererRegistry.register(TFEntities.THROWN_ICE.get(), ThrownIceRenderer::new);
		EntityRendererRegistry.register(TFEntities.THROWN_BLOCK.get(), ThrownBlockRenderer::new);
		EntityRendererRegistry.register(TFEntities.ICE_SNOWBALL.get(), ThrownItemRenderer::new);
		EntityRendererRegistry.register(TFEntities.SLIDER.get(), SlideBlockRenderer::new);
		EntityRendererRegistry.register(TFEntities.SEEKER_ARROW.get(), DefaultArrowRenderer::new);
		EntityRendererRegistry.register(TFEntities.ICE_ARROW.get(), DefaultArrowRenderer::new);

		// Block Entities
		BlockEntityRenderers.register(TFBlockEntities.FIREFLY.get(), FireflyRenderer::new);
		BlockEntityRenderers.register(TFBlockEntities.CICADA.get(), CicadaRenderer::new);
		BlockEntityRenderers.register(TFBlockEntities.MOONWORM.get(), MoonwormRenderer::new);
		BlockEntityRenderers.register(TFBlockEntities.TROPHY.get(), TrophyRenderer::new);
		BlockEntityRenderers.register(TFBlockEntities.TF_CHEST.get(), TFChestRenderer::new);
		BlockEntityRenderers.register(TFBlockEntities.TF_TRAPPED_CHEST.get(), TFChestRenderer::new);
		BlockEntityRenderers.register(TFBlockEntities.SKULL_CHEST.get(), SkullChestRenderer::new);
		BlockEntityRenderers.register(TFBlockEntities.KEEPSAKE_CASKET.get(), KeepsakeCasketRenderer::new);
		BlockEntityRenderers.register(TFBlockEntities.SKULL_CANDLE.get(), SkullCandleRenderer::new);
		BlockEntityRenderers.register(TFBlockEntities.REACTOR_DEBRIS.get(), ReactorDebrisRenderer::new);
		BlockEntityRenderers.register(TFBlockEntities.RED_THREAD.get(), RedThreadRenderer::new);
		BlockEntityRenderers.register(TFBlockEntities.CANDELABRA.get(), CandelabraRenderer::new);
		BlockEntityRenderers.register(TFBlockEntities.JAR.get(), JarRenderer::new);
		BlockEntityRenderers.register(TFBlockEntities.MASON_JAR.get(), JarRenderer.MasonJarRenderer::new);
		BlockEntityRenderers.register(TFBlockEntities.OMINOUS_CANDLE.get(), OminousCandleRenderer::new);
		BlockEntityRenderers.register(TFBlockEntities.SINISTER_SPAWNER.get(), SinisterSpawnerRenderer::new);
		BlockEntityRenderers.register(TFBlockEntities.BRAZIER.get(), BrazierRenderer::new);
	}

	private static void registerLayerDefinitions() {
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.ARCTIC_ARMOR_INNER, () -> LayerDefinition.create(ArcticArmorModel.addPieces(LayerDefinitions.INNER_ARMOR_DEFORMATION), 64, 32));
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.ARCTIC_ARMOR_OUTER, () -> LayerDefinition.create(ArcticArmorModel.addPieces(LayerDefinitions.OUTER_ARMOR_DEFORMATION), 64, 32));
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.FIERY_ARMOR_INNER, () -> LayerDefinition.create(FieryArmorModel.createMesh(LayerDefinitions.INNER_ARMOR_DEFORMATION, 0.0F), 64, 32));
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.FIERY_ARMOR_OUTER, () -> LayerDefinition.create(FieryArmorModel.createMesh(LayerDefinitions.OUTER_ARMOR_DEFORMATION, 0.0F), 64, 32));
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.KNIGHTMETAL_ARMOR_INNER, () -> LayerDefinition.create(KnightmetalArmorModel.addPieces(LayerDefinitions.INNER_ARMOR_DEFORMATION), 64, 32));
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.KNIGHTMETAL_ARMOR_OUTER, () -> LayerDefinition.create(KnightmetalArmorModel.addPieces(LayerDefinitions.OUTER_ARMOR_DEFORMATION), 64, 32));
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.PHANTOM_ARMOR_INNER, () -> LayerDefinition.create(PhantomArmorModel.addPieces(LayerDefinitions.INNER_ARMOR_DEFORMATION), 64, 32));
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.PHANTOM_ARMOR_OUTER, () -> LayerDefinition.create(PhantomArmorModel.addPieces(LayerDefinitions.OUTER_ARMOR_DEFORMATION), 64, 32));
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.YETI_ARMOR_INNER, () -> LayerDefinition.create(YetiArmorModel.addPieces(LayerDefinitions.INNER_ARMOR_DEFORMATION), 64, 32));
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.YETI_ARMOR_OUTER, () -> LayerDefinition.create(YetiArmorModel.addPieces(LayerDefinitions.OUTER_ARMOR_DEFORMATION), 64, 32));

		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.ALPHA_YETI_TROPHY, AlphaYetiModel::createTrophy);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.HYDRA_TROPHY, HydraHeadModel::checkForPack);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.KNIGHT_PHANTOM_TROPHY, KnightPhantomModel::createTrophy);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.LICH_TROPHY, LichModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.MINOSHROOM_TROPHY, MinoshroomModel::checkForPack);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.NAGA_TROPHY, NagaModel::checkForPack);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.QUEST_RAM_TROPHY, QuestRamModel::checkForPackTrophyEdition);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.SNOW_QUEEN_TROPHY, SnowQueenModel::checkForPack);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.UR_GHAST_TROPHY, UrGhastModel::create);

		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.ADHERENT, AdherentModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.ALPHA_YETI, AlphaYetiModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.ARMORED_GIANT, () -> LayerDefinition.create(HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F), 64, 32));
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.BIGHORN_SHEEP, BighornModel::checkForPack);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.BLOCKCHAIN_GOBLIN, BlockChainGoblinModel::checkForPack);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.BOAR, BoarModel::checkForPack);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.BUNNY, BunnyModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.CARMINITE_BROODLING, SpiderModel::createSpiderBodyLayer);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.CARMINITE_GOLEM, CarminiteGolemModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.CARMINITE_GHASTGUARD, TFGhastModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.CARMINITE_GHASTLING, TFGhastModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.CHAIN, ChainModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.CUBE_OF_ANNIHILATION, CubeOfAnnihilationModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.DEATH_TOME, DeathTomeModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.DEER, DeerModel::checkForPack);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.FIRE_BEETLE, FireBeetleModel::checkForPack);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.GIANT_MINER, () -> LayerDefinition.create(HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F), 64, 32));
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.HARBINGER_CUBE, HarbingerCubeModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.HEDGE_SPIDER, SpiderModel::createSpiderBodyLayer);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.HELMET_CRAB, HelmetCrabModel::checkForPack);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.HOSTILE_WOLF, () -> LayerDefinition.create(WolfModel.createMeshDefinition(CubeDeformation.NONE), 64, 32));
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.HYDRA_HEAD, HydraHeadModel::checkForPack);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.HYDRA, HydraModel::checkForPack);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.HYDRA_MORTAR, HydraMortarModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.HYDRA_NECK, HydraNeckModel::checkForPack);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.ICE_CRYSTAL, IceCrystalModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.KING_SPIDER, SpiderModel::createSpiderBodyLayer);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.KNIGHT_PHANTOM, KnightPhantomModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.KOBOLD, KoboldModel::checkForPack);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.LICH_MINION, () -> LayerDefinition.create(HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F), 64, 64));
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.LICH, LichModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.LOWER_GOBLIN_KNIGHT, LowerGoblinKnightModel::checkForPack);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.LOYAL_ZOMBIE, () -> LayerDefinition.create(HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F), 64, 64));
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.MAZE_SLIME, SlimeModel::createInnerBodyLayer);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.MAZE_SLIME_OUTER, SlimeModel::createOuterBodyLayer);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.MINOSHROOM, MinoshroomModel::checkForPack);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.MINOTAUR, MinotaurModel::checkForPack);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.MIST_WOLF, () -> LayerDefinition.create(WolfModel.createMeshDefinition(CubeDeformation.NONE), 64, 32));
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.MOSQUITO_SWARM, MosquitoSwarmModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.NAGA, NagaModel::checkForPack);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.NAGA_BODY, NagaModel::checkForPack);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.NOOP, () -> LayerDefinition.create(HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F), 0, 0));
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.PENGUIN, PenguinModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.PINCH_BEETLE, PinchBeetleModel::checkForPack);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.PROTECTION_BOX, () -> LayerDefinition.create(ProtectionBoxModel.createMesh(), 16, 16));
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.QUEST_RAM, QuestRamModel::checkForPack);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.RAVEN, RavenModel::checkForPack);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.REDCAP, RedcapModel::checkForPack);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.REDCAP_ARMOR_INNER, () -> LayerDefinition.create(HumanoidModel.createMesh(new CubeDeformation(0.25F), 0.7F), 64, 32));
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.REDCAP_ARMOR_OUTER, () -> LayerDefinition.create(HumanoidModel.createMesh(new CubeDeformation(0.65F), 0.7F), 64, 32));
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.RISING_ZOMBIE, () -> LayerDefinition.create(HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F), 64, 64));
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.ROVING_CUBE, CubeOfAnnihilationModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.SKELETON_DRUID, SkeletonDruidModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.SLIME_BEETLE, SlimeBeetleModel::checkForPack);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.SLIME_BEETLE_TAIL, SlimeBeetleModel::checkForPack);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.SNOW_QUEEN, SnowQueenModel::checkForPack);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.CHAIN_BLOCK, SpikeBlockModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.SQUIRREL, SquirrelModel::checkForPack);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.STABLE_ICE_CORE, StableIceCoreModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.SWARM_SPIDER, SpiderModel::createSpiderBodyLayer);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.TINY_BIRD, TinyBirdModel::checkForPack);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.TOWERWOOD_BORER, SilverfishModel::createBodyLayer);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.TROLL, TrollModel::checkForPack);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.UNSTABLE_ICE_CORE, UnstableIceCoreModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.UPPER_GOBLIN_KNIGHT, UpperGoblinKnightModel::checkForPack);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.UR_GHAST, UrGhastModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.WINTER_WOLF, () -> LayerDefinition.create(WolfModel.createMeshDefinition(CubeDeformation.NONE), 64, 32));
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.WRAITH, WraithModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.YETI, YetiModel::create);

		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.CICADA, CicadaModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.FIREFLY, FireflyModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.KEEPSAKE_CASKET, () -> SkullChestRenderer.create(true));
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.SKULL_CHEST, () -> SkullChestRenderer.create(false));
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.MOONWORM, MoonwormModel::create);
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.BRAZIER, BrazierModel::create);

		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.RED_THREAD, RedThreadModel::create);

		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.KNIGHTMETAL_SHIELD, KnightmetalShieldModel::create);
	}

	private static void registerParticleFactories() {
		ParticleProviderRegistry.register(TFParticleType.LARGE_FLAME.get(), LargeFlameParticle.Factory::new);
		ParticleProviderRegistry.register(TFParticleType.LEAF_RUNE.get(), LeafRuneParticle.Factory::new);
		ParticleProviderRegistry.register(TFParticleType.BOSS_TEAR.get(), new GhastTearParticle.Factory());
		ParticleProviderRegistry.register(TFParticleType.GHAST_TRAP.get(), GhastTrapParticle.Factory::new);
		ParticleProviderRegistry.register(TFParticleType.PROTECTION.get(), ProtectionParticle.Factory::new); //probably not a good idea, but worth a shot
		ParticleProviderRegistry.register(TFParticleType.SNOW.get(), SnowParticle.Factory::new);
		ParticleProviderRegistry.register(TFParticleType.SNOW_GUARDIAN.get(), SnowGuardianParticle.Factory::new);
		ParticleProviderRegistry.register(TFParticleType.SNOW_WARNING.get(), SnowWarningParticle.SimpleFactory::new);
		ParticleProviderRegistry.register(TFParticleType.EXTENDED_SNOW_WARNING.get(), SnowWarningParticle.ExtendedFactory::new);
		ParticleProviderRegistry.register(TFParticleType.ICE_BEAM.get(), IceBeamParticle.Factory::new);
		ParticleProviderRegistry.register(TFParticleType.ANNIHILATE.get(), AnnihilateParticle.Factory::new);
		ParticleProviderRegistry.register(TFParticleType.HUGE_SMOKE.get(), SmokeScaleParticle.Factory::new);
		ParticleProviderRegistry.register(TFParticleType.FIREFLY.get(), FireflyParticle.StationaryProvider::new);
		ParticleProviderRegistry.register(TFParticleType.WANDERING_FIREFLY.get(), FireflyParticle.WanderingProvider::new);
		ParticleProviderRegistry.register(TFParticleType.PARTICLE_SPAWNER_FIREFLY.get(), FireflyParticle.ParticleSpawnerProvider::new);
		ParticleProviderRegistry.register(TFParticleType.FALLEN_LEAF.get(), LeafParticle.Factory::new);
		ParticleProviderRegistry.register(TFParticleType.DIM_FLAME.get(), FlameParticle.SmallFlameProvider::new);
		ParticleProviderRegistry.register(TFParticleType.OMINOUS_FLAME.get(), FlameParticle.SmallFlameProvider::new);
		ParticleProviderRegistry.register(TFParticleType.SORTING_PARTICLE.get(), SortingParticle.Factory::new);
		ParticleProviderRegistry.register(TFParticleType.TRANSFORMATION_PARTICLE.get(), TransformationParticle.Factory::new);
		ParticleProviderRegistry.register(TFParticleType.LOG_CORE_PARTICLE.get(), LogCoreParticle.Factory::new);
		ParticleProviderRegistry.register(TFParticleType.CLOUD_PUFF.get(), CloudPuffParticle.Factory::new);
		ParticleProviderRegistry.register(TFParticleType.MAGIC_EFFECT.get(), MagicEffectParticle.Factory::new);
		ParticleProviderRegistry.register(TFParticleType.ANGRY_LICH.get(), AngryLichParticle.Factory::new);
		ParticleProviderRegistry.register(TFParticleType.TWILIGHT_ORB.get(), (sprite) -> new CustomTextureParticle.Factory(sprite, true));
		ParticleProviderRegistry.register(TFParticleType.SHIELD_BREAK.get(), CustomTextureParticle.ShieldBreak::new);
	}

	private static void registerClientExtensions() {
		//event.registerItem(ISTER.CLIENT_ITEM_EXTENSION,
		for (Item item : List.of(
			TFBlocks.CICADA.asItem(), TFBlocks.FIREFLY.asItem(), TFBlocks.MOONWORM.asItem(), TFBlocks.SKULL_CHEST.asItem(), TFBlocks.KEEPSAKE_CASKET.asItem(), TFBlocks.CANDELABRA.asItem(), TFBlocks.BRAZIER.asItem(),
			TFItems.CICADA_JAR.get(), TFItems.FIREFLY_JAR.get(), TFItems.MASON_JAR.get(), TFItems.KNIGHTMETAL_SHIELD.get(), TFItems.MYSTIC_CROWN.value(),
			TFBlocks.TWILIGHT_OAK_CHEST.asItem(), TFBlocks.CANOPY_CHEST.asItem(), TFBlocks.MANGROVE_CHEST.asItem(), TFBlocks.DARK_CHEST.asItem(), TFBlocks.TIME_CHEST.asItem(), TFBlocks.TRANSFORMATION_CHEST.asItem(), TFBlocks.MINING_CHEST.asItem(), TFBlocks.SORTING_CHEST.asItem(),
			TFBlocks.TWILIGHT_OAK_TRAPPED_CHEST.asItem(), TFBlocks.CANOPY_TRAPPED_CHEST.asItem(), TFBlocks.MANGROVE_TRAPPED_CHEST.asItem(), TFBlocks.DARK_TRAPPED_CHEST.asItem(), TFBlocks.TIME_TRAPPED_CHEST.asItem(), TFBlocks.TRANSFORMATION_TRAPPED_CHEST.asItem(), TFBlocks.MINING_TRAPPED_CHEST.asItem(), TFBlocks.SORTING_TRAPPED_CHEST.asItem(),
			TFItems.NAGA_TROPHY.get(), TFItems.LICH_TROPHY.get(), TFItems.MINOSHROOM_TROPHY.get(), TFItems.HYDRA_TROPHY.get(), TFItems.KNIGHT_PHANTOM_TROPHY.get(), TFItems.UR_GHAST_TROPHY.get(), TFItems.ALPHA_YETI_TROPHY.get(), TFItems.SNOW_QUEEN_TROPHY.get(), TFItems.QUEST_RAM_TROPHY.get(),
			TFItems.CREEPER_SKULL_CANDLE.get(), TFItems.PIGLIN_SKULL_CANDLE.get(), TFItems.PLAYER_SKULL_CANDLE.get(), TFItems.SKELETON_SKULL_CANDLE.get(), TFItems.WITHER_SKELETON_SKULL_CANDLE.get(), TFItems.ZOMBIE_SKULL_CANDLE.get()
		)) {
			BuiltinItemRendererRegistry.INSTANCE.register(item, ISTER.CLIENT_ITEM_EXTENSION);
		}

		ArmorRenderer.register(
			new ArcticArmorItem.ArmorRender(),
			TFItems.ARCTIC_HELMET.get(), TFItems.ARCTIC_CHESTPLATE.get(), TFItems.ARCTIC_LEGGINGS.get(), TFItems.ARCTIC_BOOTS.get());
		ArmorRenderer.register(
			new TFSimpleArmorRenderer(FieryArmorModel::new, TFModelLayers.FIERY_ARMOR_INNER, TFModelLayers.FIERY_ARMOR_OUTER),
			TFItems.FIERY_CHESTPLATE.get(), TFItems.FIERY_LEGGINGS.get(), TFItems.FIERY_BOOTS.get()
		);
		ArmorRenderer.register(
			new TFSimpleArmorRenderer(TFArmorModel::new, TFModelLayers.KNIGHTMETAL_ARMOR_INNER, TFModelLayers.KNIGHTMETAL_ARMOR_OUTER),
			TFItems.KNIGHTMETAL_HELMET.get(), TFItems.KNIGHTMETAL_CHESTPLATE.get(), TFItems.KNIGHTMETAL_LEGGINGS.get(), TFItems.KNIGHTMETAL_BOOTS.get()
		);
		ArmorRenderer.register(
			new TFSimpleArmorRenderer(TFArmorModel::new, TFModelLayers.PHANTOM_ARMOR_INNER, TFModelLayers.PHANTOM_ARMOR_OUTER),
			TFItems.PHANTOM_HELMET.get(), TFItems.PHANTOM_CHESTPLATE.get()
		);
		ArmorRenderer.register(
			new TFSimpleArmorRenderer(YetiArmorModel::new, TFModelLayers.YETI_ARMOR_INNER, TFModelLayers.YETI_ARMOR_OUTER),
			TFItems.YETI_HELMET.get(), TFItems.YETI_CHESTPLATE.get(), TFItems.YETI_LEGGINGS.get(), TFItems.YETI_BOOTS.get()
		);
	}

	private static void registerMapDecorators() {
		IMapDecorationRenderer.RENDERERS.put(MapDecorationTypes.PLAYER.value(), new MagicMapPlayerIconRenderer());
		IMapDecorationRenderer.RENDERERS.put(TFMapDecorations.QUEST_GROVE.get(), new ConqueredMapIconRenderer());
		IMapDecorationRenderer.RENDERERS.put(TFMapDecorations.NAGA_COURTYARD.get(), new ConqueredMapIconRenderer());
		IMapDecorationRenderer.RENDERERS.put(TFMapDecorations.LICH_TOWER.get(), new ConqueredMapIconRenderer());
		IMapDecorationRenderer.RENDERERS.put(TFMapDecorations.LABYRINTH.get(), new ConqueredMapIconRenderer());
		IMapDecorationRenderer.RENDERERS.put(TFMapDecorations.HYDRA_LAIR.get(), new ConqueredMapIconRenderer());
		IMapDecorationRenderer.RENDERERS.put(TFMapDecorations.KNIGHT_STRONGHOLD.get(), new ConqueredMapIconRenderer());
		IMapDecorationRenderer.RENDERERS.put(TFMapDecorations.DARK_TOWER.get(), new ConqueredMapIconRenderer());
		IMapDecorationRenderer.RENDERERS.put(TFMapDecorations.YETI_LAIR.get(), new ConqueredMapIconRenderer());
		IMapDecorationRenderer.RENDERERS.put(TFMapDecorations.AURORA_PALACE.get(), new ConqueredMapIconRenderer());
		IMapDecorationRenderer.RENDERERS.put(TFMapDecorations.FINAL_CASTLE.get(), new ConqueredMapIconRenderer());
	}

	private static void attachRenderLayers() {
		EntityAddedLayerCallback.EVENT.register((renderers, brokenSkinMap) -> {
			var context = new EntityRendererProvider.Context(Minecraft.getInstance().getEntityRenderDispatcher(), Minecraft.getInstance().getItemRenderer(), Minecraft.getInstance().getBlockRenderer(), Minecraft.getInstance().getEntityRenderDispatcher().getItemInHandRenderer(), Minecraft.getInstance().getResourceManager(), Minecraft.getInstance().getEntityModels(), Minecraft.getInstance().font);

			BakedMultiPartRenderers.bakeMultiPartRenderers(context);

			for (EntityType<?> type : renderers.keySet()) {
				var renderer = renderers.get(type);
				if (renderer instanceof LivingEntityRenderer<?, ?> living) {
					attachRenderLayers(living);
				}
			}

			var skinMap = (Map<PlayerSkin.Model, EntityRenderer<? extends Player>>) (Object) brokenSkinMap;

			skinMap.forEach((renderer, skin) -> {
				if (skin instanceof LivingEntityRenderer<?, ?> entityModelLivingEntityRenderer)
					attachRenderLayers(entityModelLivingEntityRenderer);
			});
		});
	}

	private static <T extends LivingEntity, M extends EntityModel<T>> void attachRenderLayers(LivingEntityRenderer<T, M> renderer) {
		((LivingEntityRendererAccessor) renderer).callAddFeature(new ShieldLayer(renderer));
		((LivingEntityRendererAccessor) renderer).callAddFeature(new IceLayer(renderer));
	}

	public static boolean isOptifinePresent() {
		return optifinePresent;
	}
}
