package dfgben.wheretfiam.models.navigation;

import java.util.UUID;

public class NavigationCoordsModel {
    private UUID identifier;
    private String name;
    private float xPos;
    private float yPos;
    private float zPos;

    public NavigationCoordsModel(String name, float xPos, float yPos, float zPos) {
        identifier = UUID.randomUUID();
        this.name = name;
        this.xPos = xPos;
        this.yPos = yPos;
        this.zPos = zPos;
    }

    public float getxPos() {
        return xPos;
    }

    public float getyPos() {
        return yPos;
    }

    public float getzPos() {
        return zPos;
    }

    public void setxPos(float value) {
        this.xPos = value;
    }

    public void setyPos(float value) {
        this.yPos = value;
    }

    public void setzPos(float value) {
        this.zPos = value;
    }

    public UUID getIdentifier() {
        return identifier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
