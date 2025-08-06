package twilightforest.fabric;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.MapDecorationTextureManager;
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import net.minecraft.world.level.saveddata.maps.MapDecorationType;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;

import java.util.HashMap;
import java.util.Map;

public interface IMapDecorationRenderer {
	static Map<MapDecorationType, IMapDecorationRenderer> RENDERERS = new HashMap<>();

	boolean render(MapDecoration decoration, PoseStack stack, MultiBufferSource bufferSource, MapItemSavedData mapData, MapDecorationTextureManager decorationTextures, boolean inItemFrame, int packedLight, int index);
}
