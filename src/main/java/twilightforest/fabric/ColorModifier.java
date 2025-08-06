package twilightforest.fabric;

@FunctionalInterface
public interface ColorModifier {
	int modifyGrassColor(double x, double z, int color);
}
