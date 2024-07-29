package dfgben.wheretfiam.models.navigation;

import java.util.List;

public class NavigationDataModel {

    private final String serverIdentifier;
    private String customName;
    private List<NavigationCoordsModel> coordsList;

    public NavigationDataModel(String serverIdentifier, String customName, List<NavigationCoordsModel> coordsList) {
        this.serverIdentifier = serverIdentifier;
        this.customName = customName;
        this.coordsList = coordsList;
    }

    public List<NavigationCoordsModel> getCoordsList() {
        return coordsList;
    }

    public void removeCoords(NavigationCoordsModel coords) {
        coordsList.remove(coords);
    }

    public void setCoordsList(List<NavigationCoordsModel> coordsList) {
        this.coordsList = coordsList;
    }

    public void addCoords(NavigationCoordsModel coords) {
        this.coordsList.add(coords);
    }

    public String getServerIdentifier() {
        return serverIdentifier;
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }

    public String getCustomName() {
        return customName;
    }
}
