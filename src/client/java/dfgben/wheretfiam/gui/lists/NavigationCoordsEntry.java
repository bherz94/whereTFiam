package dfgben.wheretfiam.gui.lists;

import dfgben.wheretfiam.models.navigation.NavigationCoordsModel;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import net.minecraft.text.Text;

public class NavigationCoordsEntry extends AlwaysSelectedEntryListWidget.Entry<NavigationCoordsEntry> {
    private final NavigationCoordsWidget parent;
    private MinecraftClient client;
    public NavigationCoordsModel coords;

    public NavigationCoordsEntry(MinecraftClient client, NavigationCoordsModel coords, NavigationCoordsWidget parent) {
        this.client = client;
        this.parent = parent;
        this.coords = coords;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, boolean hovered, float deltaTicks) {
        int drawY = parent.getY() / 2 - 5;
        int drawableWidth = parent.getWidth() - 100;

//        String entryText = String.format("%s: X: %s, Y: %s, Z:%s", coords.getName(), coords.getxPos(), coords.getyPos(), coords.getzPos());
//
//        context.drawCenteredTextWithShadow(this.client.textRenderer, entryText, entryWidth/2, drawY, 0xFFFFFF);

        context.drawTextWithShadow(client.textRenderer, coords.getName(), getEntryTextPosForIndex(drawableWidth, 1) + 50, drawY, 0xFFFFFF);
        context.drawTextWithShadow(client.textRenderer, Float.toString(coords.getxPos()), getEntryTextPosForIndex(drawableWidth, 2) + 50, drawY, 0xFFFFFF);
        context.drawTextWithShadow(client.textRenderer, Float.toString(coords.getyPos()),  getEntryTextPosForIndex(drawableWidth, 3) + 50, drawY, 0xFFFFFF);
        context.drawTextWithShadow(client.textRenderer, Float.toString(coords.getzPos()),  getEntryTextPosForIndex(drawableWidth, 4) + 50, drawY, 0xFFFFFF);
    }

    private int getEntryTextPosForIndex(int entryWidth, int index) {
        return (entryWidth / 4 * index - entryWidth / 4);
    }

    @Override
    public boolean mouseClicked(Click click, boolean doubled) {
        if (parent.isEntrySelected(this)) {
            parent.setOverlayToSelected();
        } else {
            parent.select(this);
        }
        return false;
    }

    @Override
    public Text getNarration() {
        return Text.translatable("addServer.add", coords.getName(), coords.getxPos(), coords.getyPos(), coords.getzPos(), coords.getName());
    }
}