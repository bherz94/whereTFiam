package dfgben.wheretfiam.mixin.client;

import dfgben.wheretfiam.gui.renderer.NavigationRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    @Unique
    private NavigationRenderer navRenderer;

    @Inject(at = @At("TAIL"), method = "<init>")
    private void init(CallbackInfo info) {
        this.navRenderer = new NavigationRenderer();
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void onRender(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        this.navRenderer.render(context, tickCounter);
    }
}
