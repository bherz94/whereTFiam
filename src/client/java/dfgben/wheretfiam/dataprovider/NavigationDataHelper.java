package dfgben.wheretfiam.dataprovider;

import dfgben.wheretfiam.models.navigation.CoordsComparisonResultModel;
import dfgben.wheretfiam.models.navigation.NavigationCoordsModel;
import dfgben.wheretfiam.models.navigation.NavigationDataModel;
import dfgben.wheretfiam.models.navigation.SimpleCoordsModel;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.util.WorldSavePath;
import org.spongepowered.include.com.google.gson.Gson;
import org.spongepowered.include.com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NavigationDataHelper {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File DATA_FILE = new File(MinecraftClient.getInstance().runDirectory, "navigation_data.json");

    public List<NavigationDataModel> navigationData = new ArrayList<>();

    private NavigationCoordsModel navigateTo;
    private SimpleCoordsModel lastCoords;
    private CoordsComparisonResultModel lastResult;

    public NavigationDataHelper() {
    }

    public List<NavigationDataModel> getNavigationData() {
        return navigationData;
    }

    public int getNavDataCount() {
        return this.navigationData.size();
    }

    public void setNavigationData(List<NavigationDataModel> navigationData) {
        this.navigationData = navigationData;
        save();
    }

    public void addNavigationData(NavigationDataModel navigationData) {
        this.navigationData.add(navigationData);
        save();
    }

    public NavigationDataModel getNavigationDataForServerIdentifier() {
        if (this.navigationData != null) {
            return this.navigationData.stream()
                    .filter(data -> data.getServerIdentifier().equalsIgnoreCase(getCurrentServerName()))
                    .findFirst()
                    .orElse(null);
        } else {
            return null;
        }
    }

    public void updateNavigationCoords(NavigationCoordsModel newCoords) {
        var navModel = getNavDataForCurrentWorld();
        if (navModel != null) {
            var found = navModel.getCoordsList().stream()
                    .filter(coord -> coord.getIdentifier().equals(newCoords.getIdentifier()))
                    .findFirst()
                    .orElse(null);
            var index = navModel.getCoordsList().indexOf(found);
            if (index != -1) {
                navModel.getCoordsList().set(index, newCoords);
                save();
            }
        }
    }

    public void addNavigationCoords(NavigationCoordsModel newCoords) {
        var navModel = getNavDataForCurrentWorld();
        //never saved any in this world yet
        if (navModel == null) {
            var newNavModel = new NavigationDataModel(getCurrentServerName(), "", new ArrayList<NavigationCoordsModel>(Arrays.asList(newCoords)));
            addNavigationData(newNavModel);
        } else {
            navModel.addCoords(newCoords);
            save();
        }
    }

    private NavigationDataModel getNavDataForCurrentWorld() {
        if (this.navigationData == null) return null;

        return this.navigationData.stream().filter(data -> data.getServerIdentifier().equalsIgnoreCase(getCurrentServerName()))
                .findFirst()
                .orElse(null);
    }

    public void removeCoords(NavigationCoordsModel coords) {
        NavigationDataModel navData = this.getNavigationDataForServerIdentifier();
        if (navData != null) {
            if(this.navigateTo != null && this.navigateTo.getIdentifier().equals(coords.getIdentifier())) {
                this.navigateTo = null;
            }
            navData.removeCoords(coords);
            save();
        }
    }

    public void save() {
        try (FileWriter writer = new FileWriter(DATA_FILE)) {
            GSON.toJson(this, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static NavigationDataHelper load() {
        if (DATA_FILE.exists()) {
            try (FileReader reader = new FileReader(DATA_FILE)) {
                return GSON.fromJson(reader, NavigationDataHelper.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new NavigationDataHelper();
    }

    public static String getCurrentServerName() {
        var client = MinecraftClient.getInstance();

        String worldInfo;
        ServerInfo serverInfo = client.getCurrentServerEntry();
        if (serverInfo == null) {
            worldInfo = client.getServer().getSavePath(WorldSavePath.ROOT).getParent().getFileName().toString();
        } else {
            worldInfo = serverInfo.name;
        }

        return worldInfo;
    }

    public void stopNavigation() {
        this.navigateTo = null;
    }

    public NavigationCoordsModel getNavigateTo() {
        return navigateTo;
    }

    public void setNavigateTo(NavigationCoordsModel navigateTo) {
        this.navigateTo = navigateTo;
    }

    public SimpleCoordsModel getLastCoords() {
        return lastCoords;
    }

    public void setLastCoords(SimpleCoordsModel lastCoords) {
        this.lastCoords = lastCoords;
    }

    public CoordsComparisonResultModel compareToLastCoords(SimpleCoordsModel playerPosition) {
        SimpleCoordsModel distanceFromLastPos = this.calculateDistanceToTarget(this.lastCoords);
        SimpleCoordsModel distanceFromPlayerPos = this.calculateDistanceToTarget(playerPosition);

        // make player cords we compared with to new old coords
        this.lastCoords = playerPosition;

        if (distanceFromPlayerPos.getX() == distanceFromLastPos.getX() &&
                distanceFromPlayerPos.getY() == distanceFromLastPos.getY() &&
                distanceFromLastPos.getZ() == distanceFromLastPos.getZ() &&
                this.lastResult != null) {
            return this.lastResult;
        }

        this.lastResult = new CoordsComparisonResultModel(
                distanceFromPlayerPos.getX() < distanceFromLastPos.getX(),
                distanceFromPlayerPos.getY() < distanceFromLastPos.getY(),
                distanceFromPlayerPos.getZ() < distanceFromLastPos.getZ()
        );

        return this.lastResult;
    }

    private SimpleCoordsModel calculateDistanceToTarget(SimpleCoordsModel coordsToCompare) {
        float distX = coordsToCompare.getX() - this.navigateTo.getxPos();
        if (distX < 0) distX = distX * -1;

        float distY = coordsToCompare.getY() - this.navigateTo.getyPos();
        if (distY < 0) distY = distY * -1;

        float distZ = coordsToCompare.getZ() - this.navigateTo.getzPos();
        if (distZ < 0) distZ = distZ * -1;

        return new SimpleCoordsModel(distX, distY, distZ);
    }
}