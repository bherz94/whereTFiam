package dfgben.wheretfiam.models.navigation;

public class CoordsComparisonResultModel {
    private boolean x;
    private boolean y;
    private boolean z;

    public CoordsComparisonResultModel(boolean x, boolean y, boolean z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public boolean isCloserX() {
        return x;
    }

    public void setX(boolean x) {
        this.x = x;
    }

    public boolean isCloserY() {
        return y;
    }

    public void setY(boolean y) {
        this.y = y;
    }

    public boolean isCloserZ() {
        return z;
    }

    public void setZ(boolean z) {
        this.z = z;
    }
}
