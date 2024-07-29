package dfgben.wheretfiam;

import dfgben.wheretfiam.dataprovider.NavigationDataHelper;
import dfgben.wheretfiam.gui.screens.navigation.NavigationDataScreen;
import dfgben.wheretfiam.models.navigation.NavigationCoordsModel;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class WhereTFiamClient implements ClientModInitializer {
	private static KeyBinding addCoords;
	public static KeyBinding openUI;
	public static boolean flyingEnabled;
	public static boolean closeUI = true;

	public final static NavigationDataHelper NAVMANAGER = NavigationDataHelper.load();

	@Override
	public void onInitializeClient() {
		addCoords = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"key.navigation.addCoords",
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_PAGE_UP,
				"category.navigationMod.keybinds"
		));
		openUI = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"key.navigation.openNavGui",
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_V,
				"category.navigationMod.keybinds"
		));

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (addCoords.wasPressed()) {
				if (client.player != null) {
					NAVMANAGER.addNavigationCoords(new NavigationCoordsModel("Temp",
							(float) client.player.getX(),
							(float) client.player.getY(),
							(float) client.player.getZ()
					));
					client.player.sendMessage(Text.literal("Coords added!"), false);
				}
			}
			while (openUI.wasPressed()) {
				if (client.player != null && closeUI) {
					client.setScreen(new NavigationDataScreen(Text.literal("noob"),
							new NavigationCoordsModel("CurrentPosition",
									(float) client.player.getX(),
									(float) client.player.getY(),
									(float) client.player.getZ())));
				}
				if (!closeUI) {
					closeUI = true;
				}
			}
		});
	}
}