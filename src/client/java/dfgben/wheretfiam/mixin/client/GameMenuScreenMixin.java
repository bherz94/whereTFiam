package dfgben.wheretfiam.mixin.client;

import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameMenuScreen.class)
public class GameMenuScreenMixin extends Screen {

    protected GameMenuScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "initWidgets", at=@At("HEAD"))
    private void initWidgets(CallbackInfo ci) {
//        this.addDrawableChild(ButtonWidget.builder(Text.literal("BenHax"), b -> {
//                    if (this.client != null) {
//                        this.client.setScreen(new BenHaxModScreen(Text.literal("Ben Hax - My own mod"), this));
//                    }
//                }).dimensions(10, 10, 90, 20).build());
    }
}
