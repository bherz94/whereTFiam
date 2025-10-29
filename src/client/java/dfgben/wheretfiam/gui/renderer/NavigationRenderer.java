package dfgben.wheretfiam.gui.renderer;

import dfgben.wheretfiam.WhereTFiamClient;
import dfgben.wheretfiam.models.navigation.CoordsComparisonResultModel;
import dfgben.wheretfiam.models.navigation.NavigationCoordsModel;
import dfgben.wheretfiam.models.navigation.SimpleCoordsModel;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.WorldSavePath;
import net.minecraft.util.math.Vec3d;

import java.util.Objects;

public class NavigationRenderer {

    private boolean targetReached;
    private int showLength = 300;
    private int ticks = 0;

    public NavigationRenderer() {}

    public void render(DrawContext context, RenderTickCounter tickCounter) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) {
            return;
        }

        String worldInfo;
        ServerInfo serverInfo = client.getCurrentServerEntry();
        if (serverInfo == null) {
            worldInfo = Objects.requireNonNull(client.getServer())
                    .getSavePath(WorldSavePath.ROOT).getParent().getFileName().toString();
        } else {
            worldInfo = serverInfo.name;
        }

        float clientX = (float) client.player.getX();
        float clientY = (float) client.player.getY();
        float clientZ = (float) client.player.getZ();
        String clientPos = String.format("%.2f/%.2f/%.2f", clientX, clientY, clientZ);
        Text clientPosText = Text.translatable("gui.navmanager.currentPositionText", clientPos);

        int xPos = 5;
        int yPos = 5;

        context.drawText(client.textRenderer, worldInfo, xPos, yPos, 0xFFFFFF, false);
        context.drawText(client.textRenderer, clientPosText, xPos, yPos + 15, 0xFFFFFF, false);

        if (targetReached) {
            Text reachedText = Text.translatable("gui.navmanager.reachedDestinationText");
            int screenWidth = client.getWindow().getScaledWidth();
            int screenHeight = client.getWindow().getScaledHeight();
            context.drawText(client.textRenderer, reachedText, screenWidth / 2 - client.textRenderer.getWidth(reachedText) / 2, 20, 0xFFFFFF, true);
            if (ticks <= showLength) {
                ticks++;
            } else {
                this.targetReached = false;
                ticks = 0;
            }
        }

        NavigationCoordsModel navTo = WhereTFiamClient.NAVMANAGER.getNavigateTo();
        if (navTo == null) {
            return;
        }

        CoordsComparisonResultModel isCloserTo = WhereTFiamClient.NAVMANAGER.compareToLastCoords(new SimpleCoordsModel(
                clientX,
                clientY,
                clientZ
        ));

        Style xTextStyle = isCloserTo.isCloserX() ?
                Style.EMPTY.withColor(TextColor.fromRgb(0x00FF00)) : Style.EMPTY.withColor(TextColor.fromRgb(0xFF0000));

        Style yTextStyle = isCloserTo.isCloserY() ?
                Style.EMPTY.withColor(TextColor.fromRgb(0x00FF00)) : Style.EMPTY.withColor(TextColor.fromRgb(0xFF0000));

        Style zTextStyle = isCloserTo.isCloserZ() ?
                Style.EMPTY.withColor(TextColor.fromRgb(0x00FF00)) : Style.EMPTY.withColor(TextColor.fromRgb(0xFF0000));

        Text infoText = Text.translatable("gui.navmanager.navgationActiveText", navTo.getName()).setStyle(Style.EMPTY.withColor(TextColor.fromRgb(0xFFFFFF)));
        Text xText = Text.literal(String.format("%.2f/", navTo.getxPos())).setStyle(xTextStyle);
        Text yText = Text.literal(String.format("%.2f/", navTo.getyPos())).setStyle(yTextStyle);
        Text zText = Text.literal(String.format("%.2f", navTo.getzPos())).setStyle(zTextStyle);

        MutableText navigationText = Text.literal("").append(infoText).append(xText).append(yText).append(zText);

        context.drawText(client.textRenderer, navigationText, xPos, yPos + 30, 0xFFFFFF, true);

        Vec3d targetVec = new Vec3d(navTo.getxPos(), navTo.getyPos(), navTo.getzPos());
        Vec3d playerPos = new Vec3d(clientX, clientY, clientZ);

        if (playerPos.distanceTo(targetVec) < 10) {
            WhereTFiamClient.NAVMANAGER.stopNavigation();
            this.targetReached = true;
        }

        Vec3d lookVec = client.player.getRotationVec(tickCounter.getDynamicDeltaTicks());
        Vec3d directionVec = targetVec.subtract(playerPos).normalize();

        // Calculate horizontal and vertical angles
        double horizontalAngle = Math.toDegrees(Math.atan2(directionVec.z, directionVec.x)) - Math.toDegrees(Math.atan2(lookVec.z, lookVec.x));
        double verticalAngle = Math.toDegrees(Math.asin(directionVec.y)) - Math.toDegrees(Math.asin(lookVec.y));

        // Normalize angles to the range [-180, 180]
        horizontalAngle = (horizontalAngle + 360) % 360;
        if (horizontalAngle > 180) horizontalAngle -= 360;

        verticalAngle = (verticalAngle + 360) % 360;
        if (verticalAngle > 180) verticalAngle -= 360;

        // Provide directional feedback
        String horizontalDirection = horizontalAngle > 20 ? "               >" : horizontalAngle < -20 ? "<              " : "       •       ";
        //String verticalDirection = verticalAngle > 15 ? "^" : verticalAngle < -15 ? "v" : "•";

        String directionHint = horizontalDirection;

        // Render the direction hint
        renderDirectionHint(context, directionHint);
    }

    private void renderDirectionHint(DrawContext context, String directionHint) {
        MinecraftClient client = MinecraftClient.getInstance();
        int screenWidth = client.getWindow().getScaledWidth();
        int screenHeight = client.getWindow().getScaledHeight();

        context.drawText(client.textRenderer, directionHint, screenWidth / 2 - client.textRenderer.getWidth(directionHint) / 2, 20, 0xFFFF00, true);
    }
}
