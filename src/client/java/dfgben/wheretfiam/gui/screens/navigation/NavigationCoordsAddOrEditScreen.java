package dfgben.wheretfiam.gui.screens.navigation;

import dfgben.wheretfiam.WhereTFiamClient;
import dfgben.wheretfiam.gui.inputs.ValidationTextFieldWidget;
import dfgben.wheretfiam.models.navigation.NavigationCoordsModel;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.input.CharInput;
import net.minecraft.client.input.KeyInput;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NavigationCoordsAddOrEditScreen extends Screen {
    private NavigationCoordsModel coords;
    private Screen parent;
    private final boolean isEditScreen;

    private ValidationTextFieldWidget name;
    private ValidationTextFieldWidget xPos;
    private ValidationTextFieldWidget yPos;
    private ValidationTextFieldWidget zPos;

    private String inputTextName;
    private String inputTextXpos;
    private String inputTextYPos;
    private String inputTextZpos;

    private List<ValidationTextFieldWidget> screenTextFields =
            new ArrayList<>();


    public NavigationCoordsAddOrEditScreen(Text title, Screen parent, NavigationCoordsModel coords, boolean isEditScreen) {
        super(title);
        this.isEditScreen = isEditScreen;
        this.coords = coords;
        this.parent = parent;
        this.inputTextName = coords.getName();
        this.inputTextXpos = Float.toString(coords.getxPos());
        this.inputTextYPos = Float.toString(coords.getyPos());
        this.inputTextZpos = Float.toString(coords.getzPos());
    }

    @Override
    public void init() {
        int fieldWidth = 200;
        int fieldHeight = 20;
        int centerX = this.width / 2 - fieldWidth / 2;

        this.name = new ValidationTextFieldWidget(
                this.textRenderer,
                centerX,
                40,
                fieldWidth, fieldHeight, Text.literal("input_Name"));
        this.name.setText(this.inputTextName);
        this.name.setPlaceholder(Text.translatable("gui.navmanager.placeholder.name"));

        this.xPos = new ValidationTextFieldWidget(
                this.textRenderer,
                centerX,
                this.name.getBottom() + 10,
                fieldWidth, fieldHeight, Text.literal("input_Xpos"));
        this.xPos.setText(this.inputTextXpos);
        this.xPos.setPlaceholder(Text.translatable("gui.navmanager.placeholder.xPos"));

        this.yPos = new ValidationTextFieldWidget(
                this.textRenderer,
                centerX,
                this.xPos.getBottom() + 10,
                fieldWidth, fieldHeight, Text.literal("input_Ypos"));
        this.yPos.setText(this.inputTextYPos);
        this.yPos.setPlaceholder(Text.translatable("gui.navmanager.placeholder.yPos"));

        this.zPos = new ValidationTextFieldWidget(
                this.textRenderer,
                centerX,
                this.yPos.getBottom() + 10,
                fieldWidth, fieldHeight, Text.literal("input_Zpos"));
        this.zPos.setText(this.inputTextZpos);
        this.zPos.setPlaceholder(Text.translatable("gui.navmanager.placeholder.zPos"));

        this.screenTextFields.addAll(Arrays.asList(name, xPos, yPos, zPos));

        for (TextFieldWidget textField : screenTextFields) {
            textField.setMaxLength(32); // Max length of the text
            textField.setEditableColor(0xFFFFFF); // Text color
            textField.setVisible(true); // Make the text field visible
            textField.setEditable(true);
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        String headline = this.isEditScreen ? "Edit coordinates for " : "Add coordinates for ";
        context.drawCenteredTextWithShadow(textRenderer, headline + this.coords.getName(), width / 2, 10, 0xFFFFFF);

        for (TextFieldWidget textField : screenTextFields) {
            textField.render(context, mouseX, mouseY, delta);
        }
        ButtonWidget myButton;
        this.addDrawableChild(myButton = ButtonWidget.builder(Text.translatable("gui.navmanager.coords.save"), press -> this.save())
                .dimensions(width / 2 - 105, this.zPos.getBottom() + 10, 100, 20)
                .build());

        this.addDrawableChild(ButtonWidget.builder(Text.translatable("gui.cancel"), press -> this.cancelPressed())
                .dimensions(myButton.getRight() + 10, this.zPos.getBottom() + 10, 100, 20)
                .build());
    }

    @Override
    public boolean keyPressed(KeyInput input) {
        if (WhereTFiamClient.openUI.matchesKey(input) && !isAnyTextFieldFocused()) {
            if (client != null) {
                this.client.setScreen(null);
                WhereTFiamClient.closeUI = false;
            }
        }

        for (TextFieldWidget textField : screenTextFields) {
            if (textField.keyPressed(input)) {
                return true;
            }
        }
        return super.keyPressed(input);
    }

    @Override
    public boolean charTyped(CharInput input) {
        for (TextFieldWidget textField : screenTextFields) {
            if (textField.charTyped(input)) {
                return true;
            }
        }

        return super.charTyped(input);
    }

    @Override
    public boolean mouseClicked(Click click, boolean doubled) {
        for (TextFieldWidget textField : screenTextFields) {
            if (textField.mouseClicked(click, doubled)) {
                this.setFocused(textField);
                return true;
            }
        }
        return super.mouseClicked(click, doubled);
    }

    private boolean isAnyTextFieldFocused() {
        return this.name.isFocused() || this.xPos.isFocused() || this.yPos.isFocused() || this.zPos.isFocused();
    }

    private void save() {
        if (this.isValid()) {
            if (this.isEditScreen) {
                this.coords.setName(this.name.getText());
                this.coords.setxPos(Float.parseFloat(this.xPos.getText()));
                this.coords.setyPos(Float.parseFloat(this.yPos.getText()));
                this.coords.setzPos(Float.parseFloat(this.zPos.getText()));

                WhereTFiamClient.NAVMANAGER.updateNavigationCoords(this.coords);
            } else {
                WhereTFiamClient.NAVMANAGER.addNavigationCoords(new NavigationCoordsModel(
                        this.name.getText(),
                        Float.parseFloat(this.xPos.getText()),
                        Float.parseFloat(this.yPos.getText()),
                        Float.parseFloat(this.zPos.getText())
                ));
            }
            this.close();
        }
    }

    private void cancelPressed() {
        this.close();
    }

    private boolean isValid() {
        if (this.name.getText().trim().isEmpty()) {
            this.name.setValid(false);
        } else if (this.xPos.getText().trim().isEmpty()) {
            this.xPos.setValid(false);
        } else if (this.yPos.getText().trim().isEmpty()) {
            this.yPos.setValid(false);
        } else if (this.zPos.getText().trim().isEmpty()) {
            this.zPos.setValid(false);
        }

        return this.name.isValid()
                && this.xPos.isValid()
                && this.yPos.isValid()
                && this.zPos.isValid();
    }

    @Override
    public void close() {
        if (this.client != null) {
            if (this.parent != null) {
                this.client.setScreen(parent);
            } else {
                this.client.setScreen(null);
            }
        }
    }
}
