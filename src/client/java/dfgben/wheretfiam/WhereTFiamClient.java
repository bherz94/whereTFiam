package dfgben.wheretfiam;

import dfgben.wheretfiam.dataprovider.NavigationDataHelper;
import dfgben.wheretfiam.gui.renderer.NavigationRenderer;
import dfgben.wheretfiam.gui.screens.navigation.NavigationDataScreen;
import dfgben.wheretfiam.models.navigation.NavigationCoordsModel;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;

public class WhereTFiamClient implements ClientModInitializer {
    private static KeyBinding addCoords;
    public static KeyBinding openUI;
    public static boolean closeUI = true;

    public final static String MOD_ID = "WTFIam";

    public static final KeyBinding.Category NAV_MOD_CATEGORY =
            // Pass the translation key string for the category here
            KeyBinding.Category.create((Identifier.of("navigationmod", "keybinds")));

    public final static NavigationDataHelper NAVMANAGER = NavigationDataHelper.load();
    public NavigationRenderer navRenderer = new NavigationRenderer();

    @Override
    public void onInitializeClient() {
        addCoords = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "gui.navmanager.coords.add",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_PAGE_UP,
                NAV_MOD_CATEGORY
        ));
        openUI = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.navigation.openNavGui",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_V,
                NAV_MOD_CATEGORY
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

        MinecraftClient client = MinecraftClient.getInstance();

        HudElementRegistry.attachElementBefore(
				VanillaHudElements.CHAT,
				Identifier.of(WhereTFiamClient.MOD_ID,"before_chat"),
				navRenderer.render(client));
    }
}