package dfgben.wheretfiam.gui.lists;

import dfgben.wheretfiam.models.navigation.NavigationCoordsModel;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import net.minecraft.text.Text;

public class NavigationCoordsEntry extends AlwaysSelectedEntryListWidget.Entry<NavigationCoordsEntry> {
    private final NavigationCoordsWidget parent;
    public NavigationCoordsModel coords;

    public NavigationCoordsEntry(NavigationCoordsModel coords, NavigationCoordsWidget parent) {
        this.parent = parent;
        this.coords = coords;
        System.out.println(coords.getName());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, boolean hovered, float deltaTicks) {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        TextRenderer textRenderer = minecraftClient.textRenderer;
        int drawY = this.getY() / 2 - 5;
        int drawableWidth = parent.getWidth() - 100;
        System.out.println(drawY);
        System.out.println(drawableWidth);

        //String entryText = String.format("%s: X: %s, Y: %s, Z:%s", coords.getName(), coords.getxPos(), coords.getyPos(), coords.getzPos());

        //context.drawCenteredTextWithShadow(this.client.textRenderer, entryText, 50, 10, 0xFFFFFF);


        context.drawTextWithShadow(textRenderer, coords.getName(), getEntryTextPosForIndex(drawableWidth, 2) + 50, drawY, 0xFFFFFF);
        context.drawTextWithShadow(textRenderer, Float.toString(coords.getxPos()), getEntryTextPosForIndex(drawableWidth, 2) + 50, drawY, 0xFFFFFF);
        context.drawTextWithShadow(textRenderer, Float.toString(coords.getyPos()),  getEntryTextPosForIndex(drawableWidth, 3) + 50, drawY, 0xFFFFFF);
        context.drawTextWithShadow(textRenderer, Float.toString(coords.getzPos()),  getEntryTextPosForIndex(drawableWidth, 4) + 50, drawY, 0xFFFFFF);
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