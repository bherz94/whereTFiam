package dfgben.wheretfiam.gui.lists;

import dfgben.wheretfiam.WhereTFiamClient;
import dfgben.wheretfiam.gui.screens.navigation.NavigationDataScreen;
import dfgben.wheretfiam.models.navigation.NavigationCoordsModel;
import dfgben.wheretfiam.models.navigation.NavigationDataModel;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;

import java.util.List;
import java.util.Objects;

public class NavigationCoordsWidget extends AlwaysSelectedEntryListWidget<NavigationCoordsEntry> {
    private final NavigationDataScreen parent;

    public NavigationCoordsWidget(MinecraftClient client, NavigationDataScreen parent, int width, int height, int top, int itemHeight) {
        super(client, width, height, top, itemHeight);
        this.parent = parent;
    }

    public void refreshEntries() {
        clearEntries();
        NavigationDataModel navData = WhereTFiamClient.NAVMANAGER.getNavigationDataForServerIdentifier();
        if(navData != null) {
            List<NavigationCoordsModel> list = navData.getCoordsList();
            if (list != null) {
                for (NavigationCoordsModel set : list) {
                    this.addEntry(new NavigationCoordsEntry(set, this));
                }
            }
        }
    }

    @Override
    public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderList(context, mouseX, mouseY, delta);
        this.renderScrollBar(context);
    }

    public int getLeft() {
        return getX();
    }

    public int getRight() {
        return getX() + width;
    }

    public int getTop() {
        return getY();
    }

    public int getBottom() {
        return getY() + height;
    }

    public int getButtonWidth() {
        return Objects.requireNonNull(client.currentScreen).width - getX() - width - 10;
    }

//    public void newCoordinate(CoordinatesManagerScreen screen) {
//        client.setScreen(new CoordinateCreationScreen(this.getSelectedOrNull() != null ? ((CoordinateEntry) this.getSelectedOrNull()).coordinate : null, screen));
//    }

    public void removeCoordinate() {
        refreshEntries();
        this.setSelected(null);
    }

    public void copyCoordinate() {
        refreshEntries();
    }

    public boolean isEntrySelected(NavigationCoordsEntry entry) {
        return this.getSelectedOrNull() == entry;
    }

    public void setOverlayToSelected() {
        //CoordinateHudOverlay.INSTANCE.setCurrentCoordinate(((CoordinateEntry) Objects.requireNonNull(this.getSelectedOrNull())).coordinate);
    }

//    public void teleportToCoordinate() {
//        Objects.requireNonNull(client.player);
//        if (client.player.isCreativeLevelTwoOp()) {
//            Coordinate coordinateSet = ((CoordinateEntry) Objects.requireNonNull(this.getSelectedOrNull())).coordinate;
//            client.player.networkHandler.sendChatCommand("tp %s %s %s".formatted(coordinateSet.x, coordinateSet.y, coordinateSet.z));
//        } else {
//            //parent.reportError(I18n.translate("commands.help.failed"));
//        }
//    }

    public void select(NavigationCoordsEntry entry) {
        this.setSelected(entry);
        this.parent.selectionChanged(entry);
    }

    private void renderScrollBar(DrawContext context) {
        int scrollbarXStart = this.getScrollbarX();
        int scrollbarXEnd = scrollbarXStart + 6;
        int maxScroll = this.getMaxScrollY();
        if (maxScroll > this.getBottom() - this.getTop()) {
            int scrollHeight = this.getBottom() - this.getTop();
            int scrollBarHeight = (scrollHeight * scrollHeight) / maxScroll;
            scrollBarHeight = Math.max(32, scrollBarHeight);
            int scrollBarTop = (int) this.getScrollY() * (scrollHeight - scrollBarHeight) / (maxScroll - scrollHeight) + this.getTop();
            if (scrollBarTop < this.getTop()) {
                scrollBarTop = this.getTop();
            }

            context.fill(scrollbarXStart, scrollBarTop, scrollbarXEnd, scrollBarTop + scrollBarHeight, 0xFF000000);
            context.fill(scrollbarXStart, scrollBarTop, scrollbarXEnd - 1, scrollBarTop + scrollBarHeight - 1, 0xFF808080);
        }
    }

    @Override
    protected int getScrollbarX() {
        return this.getRight() - 6;
    }

    @Override
    public int getRowWidth() {
        return width;
    }
}