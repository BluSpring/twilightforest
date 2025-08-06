package twilightforest.fabric;

public class DeferredBus {
	public void addListener(Runnable runnable) {
		runnable.run();
	}
}
