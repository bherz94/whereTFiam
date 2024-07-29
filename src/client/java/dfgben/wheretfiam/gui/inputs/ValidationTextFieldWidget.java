package dfgben.wheretfiam.gui.inputs;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

public class ValidationTextFieldWidget extends TextFieldWidget {

    private boolean isValid = true;
    private final int x;
    private final int y;

    public ValidationTextFieldWidget(TextRenderer textRenderer, int x, int y, int width, int height, Text text) {
        super(textRenderer, x, y, width, height, text);
        this.x = x;
        this.y = y;
    }

    public void setValid(boolean isValid) {
        this.isValid = isValid;
    }

    public boolean isValid() {
        return this.isValid;
    }

    @Override
    public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        if (!this.isValid) {
            // Render the widget with a red border if the input is invalid
            context.fill(this.x - 1, this.y - 1, this.x + this.width + 1, this.y + this.height + 1, 0xFFFF0000);
        }
        super.renderWidget(context, mouseX, mouseY, delta);
    }
}
