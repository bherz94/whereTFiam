package dfgben.wheretfiam.gui.screens.navigation;

import dfgben.wheretfiam.WhereTFiamClient;
import dfgben.wheretfiam.dataprovider.NavigationDataHelper;
import dfgben.wheretfiam.gui.lists.NavigationCoordsEntry;
import dfgben.wheretfiam.gui.lists.NavigationCoordsWidget;
import dfgben.wheretfiam.models.navigation.NavigationCoordsModel;
import dfgben.wheretfiam.models.navigation.SimpleCoordsModel;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class NavigationDataScreen extends Screen {
    private NavigationCoordsWidget navCoords;
    private ButtonWidget addButton;
    private ButtonWidget removeButton;
    private ButtonWidget editButton;
    private ButtonWidget toggleNavigationButton;
    private NavigationCoordsModel currentCoords;

    public NavigationDataScreen(Text title, NavigationCoordsModel currentCoords) {
        super(title);
        this.currentCoords = currentCoords;
    }

    @Override
    public void init() {
        navCoords = new NavigationCoordsWidget(client, this, width, height / 3 * 2 - 40, 40, 20);
        navCoords.refreshEntries();
        this.addSelectableChild(navCoords);

        int buttonHeight = (height / 3 - 40) / 2;

        toggleNavigationButton = ButtonWidget.builder(Text.translatable(this.getToggleNavTextKey()), press -> this.toggleNavigation())
                .dimensions(5, 10, 100, buttonHeight)
                .build();
        this.addDrawableChild(toggleNavigationButton);

        addButton = ButtonWidget.builder(Text.translatable("gui.navmanager.coords.add"), press -> setAddCoordsScreen())
                .dimensions(width / 2 - 100, navCoords.getBottom() + 20, 100, buttonHeight)
                .build();
        this.addDrawableChild(addButton);

        ButtonWidget back = ButtonWidget.builder(Text.translatable("gui.cancel"), press -> this.close())
                .dimensions(addButton.getRight() + 5, navCoords.getBottom() + 20, 100, buttonHeight)
                .build();
        this.addDrawableChild(back);

        this.editButton = ButtonWidget.builder(Text.translatable("selectServer.edit"), press -> setEditCoordsScreen())
                .dimensions(width / 2 - 100, addButton.getBottom() + 5, 100, buttonHeight)
                .build();
        this.addDrawableChild(editButton);

        this.removeButton = ButtonWidget.builder(Text.translatable("selectWorld.delete"), press -> removeSelectedCoords())
                .dimensions(editButton.getRight() + 5, back.getBottom() + 5, 100, buttonHeight)
                .build();
        this.addDrawableChild(this.removeButton);

        if (navCoords.getSelectedOrNull() == null) {
            this.removeButton.active = false;
            this.editButton.active = false;
            if (WhereTFiamClient.NAVMANAGER.getNavigateTo() == null) {
                this.toggleNavigationButton.active = false;
            }
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(textRenderer, "Saved coordinates for " + NavigationDataHelper.getCurrentServerName(), width / 2, 10, 0xFFFFFF);
        navCoords.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int int_3) {
        if (WhereTFiamClient.openUI.matchesKey(keyCode, scanCode)) {
            if (client != null) {
                this.client.setScreen(null);
                WhereTFiamClient.closeUI = false;
            }
        }
        if (keyCode == GLFW.GLFW_KEY_DELETE) {
            var selected = this.navCoords.getSelectedOrNull();
            if (selected != null) {
                this.removeSelectedCoords();
            }
        }

        return super.keyPressed(keyCode, scanCode, int_3);
    }

    public void selectionChanged(NavigationCoordsEntry entry) {
        if (entry == null) {
            this.removeButton.active = false;
            this.editButton.active = false;
            if (WhereTFiamClient.NAVMANAGER.getNavigateTo() == null && this.navCoords.getSelectedOrNull() == null) {
                this.toggleNavigationButton.active = false;
            }
        } else {
            this.removeButton.active = true;
            this.editButton.active = true;
            this.toggleNavigationButton.active = true;
        }
        this.toggleNavigationButton.setMessage(Text.translatable(this.getToggleNavTextKey()));
    }

    private void removeSelectedCoords() {
        NavigationCoordsEntry selectedEntry = this.navCoords.getSelectedOrNull();
        if (selectedEntry != null) {
            WhereTFiamClient.NAVMANAGER.removeCoords(selectedEntry.coords);
            this.navCoords.select(null);
            this.navCoords.refreshEntries();
        }
    }

    private void setAddCoordsScreen() {
        if (this.client != null) {
            this.client.setScreen(new NavigationCoordsAddOrEditScreen(Text.literal("Add"), this, currentCoords, false));
        }
    }

    private void toggleNavigation() {
        NavigationCoordsEntry selectedEntry = this.navCoords.getSelectedOrNull();

        if (WhereTFiamClient.NAVMANAGER.getNavigateTo() != null) {
            WhereTFiamClient.NAVMANAGER.setNavigateTo(null);
            this.selectionChanged(selectedEntry);
        } else if (selectedEntry != null) {
            WhereTFiamClient.NAVMANAGER.setNavigateTo(selectedEntry.coords);
            WhereTFiamClient.NAVMANAGER.setLastCoords(new SimpleCoordsModel(
                    this.currentCoords.getxPos(),
                    this.currentCoords.getyPos(),
                    this.currentCoords.getzPos()
            ));
            this.close();
        }
    }

    private void setEditCoordsScreen() {
        NavigationCoordsEntry selectedEntry = this.navCoords.getSelectedOrNull();
        if (selectedEntry != null && this.client != null) {
            this.client.setScreen(new NavigationCoordsAddOrEditScreen(Text.literal("Edit"), this, selectedEntry.coords, true));
        }
    }

    private String getToggleNavTextKey() {
        return WhereTFiamClient.NAVMANAGER.getNavigateTo() != null ? "gui.navmanager.stop" : "gui.navmanager.start";
    }
}
