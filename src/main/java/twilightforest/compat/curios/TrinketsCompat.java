package twilightforest.compat.curios;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.Trinket;
import dev.emi.trinkets.api.TrinketsApi;
import dev.emi.trinkets.api.client.TrinketRenderer;
import dev.emi.trinkets.api.client.TrinketRendererRegistry;
import io.github.fabricators_of_create.porting_lib.registry.DeferredBlock;
import io.github.fabricators_of_create.porting_lib.registry.DeferredItem;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.FastColor;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.TFModelLayers;
import twilightforest.compat.curios.model.CharmOfLifeNecklaceModel;
import twilightforest.compat.curios.renderer.CharmOfKeepingRenderer;
import twilightforest.compat.curios.renderer.CharmOfLifeNecklaceRenderer;
import twilightforest.compat.curios.renderer.CurioHeadRenderer;
import twilightforest.events.CharmEvents;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFItems;
import twilightforest.network.CreateMovingCicadaSoundPacket;

import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class TrinketsCompat {

	public static void registerCuriosCapabilities() {
		var trinket = new Trinket() {
			@Override
			public Holder<SoundEvent> getEquipSound(ItemStack stack, SlotReference slot, LivingEntity entity) {
				return SoundEvents.ARMOR_EQUIP_GENERIC;
			}

			@Override
			public void onEquip(ItemStack stack, SlotReference slot, LivingEntity entity) {
				//check that we don't have a cicada already on our head before trying to start the sound
				if (!entity.getItemBySlot(EquipmentSlot.HEAD).is(TFBlocks.CICADA.get().asItem())) {
					if (stack.is(TFBlocks.CICADA.get().asItem()) && !entity.level().isClientSide()) {
						for (ServerPlayer player : PlayerLookup.tracking(entity)) {
							ServerPlayNetworking.send(player, new CreateMovingCicadaSoundPacket(entity.getId()));
						}

						if (entity instanceof ServerPlayer player) {
							ServerPlayNetworking.send(player, new CreateMovingCicadaSoundPacket(entity.getId()));
						}
					}
				}
			}

			@Override
			public boolean canEquipFromUse(ItemStack stack, LivingEntity entity) {
				return true;
			}
		};

		for (DeferredItem<Item> item : List.of(TFItems.CHARM_OF_KEEPING_1, TFItems.CHARM_OF_KEEPING_2, TFItems.CHARM_OF_KEEPING_3, TFItems.CHARM_OF_LIFE_1, TFItems.CHARM_OF_LIFE_2,
			TFItems.NAGA_TROPHY, TFItems.LICH_TROPHY, TFItems.MINOSHROOM_TROPHY, TFItems.HYDRA_TROPHY, TFItems.KNIGHT_PHANTOM_TROPHY,
			TFItems.UR_GHAST_TROPHY, TFItems.ALPHA_YETI_TROPHY, TFItems.SNOW_QUEEN_TROPHY, TFItems.QUEST_RAM_TROPHY, TFItems.SKELETON_SKULL_CANDLE, TFItems.WITHER_SKELETON_SKULL_CANDLE,
			TFItems.ZOMBIE_SKULL_CANDLE, TFItems.CREEPER_SKULL_CANDLE, TFItems.PLAYER_SKULL_CANDLE, TFItems.PIGLIN_SKULL_CANDLE)
		) {
			TrinketsApi.registerTrinket(item.value(), trinket);
		}

		for (DeferredBlock<Block> block : List.of(TFBlocks.CICADA, TFBlocks.FIREFLY, TFBlocks.MOONWORM)) {
			TrinketsApi.registerTrinket(block.asItem(), trinket);
		}
	}

	//if we have any curios and die with a charm of keeping on us, keep our curios instead of dropping them
	public static void keepCurios() {
		/*if (event.getEntity() instanceof Player player) {
			CompoundTag playerData = CharmEvents.getPlayerData(player);
			if (!player.level().isClientSide() && playerData.contains(CharmEvents.CONSUMED_CHARM_TAG) && playerData.contains(CharmEvents.CHARM_INV_TAG) && !playerData.getList(CharmEvents.CHARM_INV_TAG, 10).isEmpty()) {
				//Keep all Curios items
				CuriosApi.getCuriosInventory(player).ifPresent(modifiable -> {
					for (int i = 0; i < modifiable.getSlots(); ++i) {
						int finalI = i;
						event.addOverride(stack -> stack == modifiable.getEquippedCurios().getStackInSlot(finalI), ICurio.DropRule.ALWAYS_KEEP);
					}
				});
			}
		}*/
	}

	public static void registerCurioLayers() {
		EntityModelLayerRegistry.registerModelLayer(TFModelLayers.CHARM_OF_LIFE, CharmOfLifeNecklaceModel::create);
	}

	public static void registerCurioRenderers() {
		{
			registerRenderer(TFItems.CHARM_OF_LIFE_1.get(), () -> new CharmOfLifeNecklaceRenderer(FastColor.ARGB32.colorFromFloat(1.0F, 1.0F, 0.5F, 0.5F)));
			registerRenderer(TFItems.CHARM_OF_LIFE_2.get(), () -> new CharmOfLifeNecklaceRenderer(FastColor.ARGB32.colorFromFloat(1.0F, 1.0F, 0.9F, 0.0F)));
			registerRenderer(TFItems.CHARM_OF_KEEPING_1.get(), CharmOfKeepingRenderer::new);
			registerRenderer(TFItems.CHARM_OF_KEEPING_2.get(), CharmOfKeepingRenderer::new);
			registerRenderer(TFItems.CHARM_OF_KEEPING_3.get(), CharmOfKeepingRenderer::new);

			registerRenderer(TFItems.NAGA_TROPHY.get(), CurioHeadRenderer::new);
			registerRenderer(TFItems.LICH_TROPHY.get(), CurioHeadRenderer::new);
			registerRenderer(TFItems.MINOSHROOM_TROPHY.get(), CurioHeadRenderer::new);
			registerRenderer(TFItems.HYDRA_TROPHY.get(), CurioHeadRenderer::new);
			registerRenderer(TFItems.KNIGHT_PHANTOM_TROPHY.get(), CurioHeadRenderer::new);
			registerRenderer(TFItems.UR_GHAST_TROPHY.get(), CurioHeadRenderer::new);
			registerRenderer(TFItems.ALPHA_YETI_TROPHY.get(), CurioHeadRenderer::new);
			registerRenderer(TFItems.SNOW_QUEEN_TROPHY.get(), CurioHeadRenderer::new);
			registerRenderer(TFItems.QUEST_RAM_TROPHY.get(), CurioHeadRenderer::new);

			registerRenderer(TFBlocks.CICADA.get().asItem(), CurioHeadRenderer::new);
			registerRenderer(TFBlocks.FIREFLY.get().asItem(), CurioHeadRenderer::new);
			registerRenderer(TFBlocks.MOONWORM.get().asItem(), CurioHeadRenderer::new);

			registerRenderer(TFItems.CREEPER_SKULL_CANDLE.get(), CurioHeadRenderer::new);
			registerRenderer(TFItems.PIGLIN_SKULL_CANDLE.get(), CurioHeadRenderer::new);
			registerRenderer(TFItems.PLAYER_SKULL_CANDLE.get(), CurioHeadRenderer::new);
			registerRenderer(TFItems.SKELETON_SKULL_CANDLE.get(), CurioHeadRenderer::new);
			registerRenderer(TFItems.WITHER_SKELETON_SKULL_CANDLE.get(), CurioHeadRenderer::new);
			registerRenderer(TFItems.ZOMBIE_SKULL_CANDLE.get(), CurioHeadRenderer::new);
		}
	}

	private static void registerRenderer(Item item, Supplier<TrinketRenderer> supplier) {
		// fuck it we ball
		ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
			@Override
			public ResourceLocation getFabricId() {
				return TwilightForestMod.prefix("trinkets_setup");
			}

			@Override
			public void onResourceManagerReload(ResourceManager resourceManager) {
				TrinketRendererRegistry.registerRenderer(item, supplier.get());
			}
		});
	}

	public static boolean isCurioEquipped(LivingEntity entity, Predicate<ItemStack> stackPredicate) {
		return TrinketsApi.getTrinketComponent(entity).map(e -> e.isEquipped(stackPredicate)).orElse(false);
	}

	public static boolean isCurioEquippedAndVisible(LivingEntity entity, Predicate<ItemStack> stackPredicate) {
		var slot = TrinketsApi.getTrinketComponent(entity).map(handler -> handler.getEquipped(stackPredicate));
		return slot.isPresent() /*&& slot.get().slotContext() != null && slot.get().slotContext().visible()*/;
	}

	public static boolean findAndConsumeCurio(Item item, Player player) {
		var slot = TrinketsApi.getTrinketComponent(player).map(e -> e.getEquipped(item));
		if (slot.isPresent()) {
			for (Tuple<SlotReference, ItemStack> tuple : slot.orElseThrow()) {
				CharmEvents.getPlayerData(player).put(CharmEvents.CONSUMED_CHARM_TAG, tuple.getB().save(player.registryAccess()));
				tuple.getB().shrink(1);
				return true;
			}
		}
		return false;
	}
}
