package twilightforest.init;

import com.mojang.authlib.GameProfile;
import com.mojang.serialization.Codec;
import io.github.fabricators_of_create.porting_lib.registry.DeferredHolder;
import io.github.fabricators_of_create.porting_lib.registry.DeferredRegister;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.UUIDUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Unit;
import twilightforest.TwilightForestMod;
import twilightforest.components.entity.*;
import twilightforest.components.item.OreScannerComponent;
import twilightforest.util.Codecs;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class TFDataAttachments {
	//public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, TwilightForestMod.ID);

	public static final Supplier<AttachmentType<Boolean>> FEATHER_FAN = register("feather_fan_falling", builder -> builder.initializer(() -> false).persistent(Codec.BOOL));
	public static final Supplier<AttachmentType<PotionFlaskTrackingAttachment>> FLASK_DOSES = register("flask_doses", builder -> builder.initializer(PotionFlaskTrackingAttachment::new).persistent(PotionFlaskTrackingAttachment.CODEC));
	public static final Supplier<AttachmentType<FortificationShieldAttachment>> FORTIFICATION_SHIELDS = register("fortification_shields", builder -> builder.initializer(FortificationShieldAttachment::new).persistent(FortificationShieldAttachment.CODEC));
	public static final Supplier<AttachmentType<GiantPickaxeMiningAttachment>> GIANT_PICKAXE_MINING = register("giant_pickaxe_mining", builder -> builder.initializer(GiantPickaxeMiningAttachment::new));
	public static final Supplier<AttachmentType<OreScannerComponent>> ORE_SCANNER = register("ore_scanner", builder -> builder.initializer(OreScannerComponent::getEmpty).persistent(OreScannerComponent.CODEC));
	public static final Supplier<AttachmentType<YetiThrowAttachment>> YETI_THROWING = register("yeti_throwing", builder -> builder.initializer(YetiThrowAttachment::new));
	public static final Supplier<AttachmentType<MultiplayerInclusivityAttachment>> MULTIPLAYER_FIGHT = register("multiplayer_fight", builder -> builder.initializer(MultiplayerInclusivityAttachment::new));
	public static final Supplier<AttachmentType<TFPortalAttachment>> TF_PORTAL_COOLDOWN = register("tf_portal_cooldown", builder -> builder.initializer(TFPortalAttachment::new));
	public static final Supplier<AttachmentType<SmashBlocksEnchantmentAttachment>> SMASH_BLOCKS = register("smash_blocks", builder -> builder.initializer(() -> new SmashBlocksEnchantmentAttachment()).persistent(SmashBlocksEnchantmentAttachment.CODEC));
	public static final Supplier<AttachmentType<GameProfile>> ZOMBIFIED_PLAYER = register("zombified_player", builder -> builder.initializer(() -> UUIDUtil.createOfflineProfile("GizmoTheMoonPig")).persistent(Codecs.SIMPLE_GAME_PROFILE));
	public static final Supplier<AttachmentType<Unit>> LEASH_PATHFINDER_OVERRIDE = register("leashed_pathfinder_override", builder -> builder.initializer(() -> Unit.INSTANCE).persistent(Codec.unit(Unit.INSTANCE)));
	public static final Supplier<AttachmentType<Unit>> BANISHED_TO_TWILIGHT_FOREST = register("twilightforest_banished", builder -> builder.initializer(() -> Unit.INSTANCE).persistent(Codec.unit(Unit.INSTANCE)).copyOnDeath());

	/*private static <T> T directCopy(T attachment, IAttachmentHolder holder, HolderLookup.Provider provider) {
		return attachment;
	}*/

	private static <T> Supplier<AttachmentType<T>> register(String name, Consumer<AttachmentRegistry.Builder<T>> consumer) {
		var builder = AttachmentRegistry.<T>builder();
		consumer.accept(builder);
		var registered = builder.buildAndRegister(ResourceLocation.fromNamespaceAndPath(TwilightForestMod.ID, name));

		return () -> registered;
	}
}
