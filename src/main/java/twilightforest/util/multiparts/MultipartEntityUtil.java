package twilightforest.util.multiparts;

import io.github.fabricators_of_create.porting_lib.entity.MultiPartEntity;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;
import twilightforest.client.BakedMultiPartRenderers;
import twilightforest.entity.TFPart;
import twilightforest.network.UpdateTFMultipartPacket;

import java.util.Iterator;

public class MultipartEntityUtil {

	public Iterator<Entity> injectTFPartEntities(Iterator<Entity> iter) {
		return new MultipartEntityIteratorWrapper(iter);
	}

	@Nullable
	public EntityRenderer<?> tryLookupTFPartRenderer(@Nullable EntityRenderer<?> renderer, Entity entity) {
		if (entity instanceof TFPart<?> part)
			return BakedMultiPartRenderers.lookup(part.renderer());
		return renderer;
	}

	public Entity sendDirtyMultipartEntityData(Entity entity) {
		if (entity instanceof MultiPartEntity)
			for (ServerPlayer player : PlayerLookup.tracking(entity)) {
				ServerPlayNetworking.send(player, new UpdateTFMultipartPacket(entity));
			}
		return entity;
	}

}
