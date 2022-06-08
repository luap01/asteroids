package Model;

import com.google.errorprone.annotations.DoNotMock;
import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;

public abstract class Debris extends PhysicalObject {

    private boolean isLarge;
    protected final static float DENSITY = 0.5F;

    public Debris(Point2D position, Dimension2D size, String iconLocation, boolean isLarge, double[] dirV) {
        super(position, size, iconLocation, dirV);
        this.isLarge = isLarge;
        this.mass = (int) (DENSITY* size.getHeight()*size.getWidth());
    }

    public Debris(boolean isLarge) {
        super();
        this.isLarge = isLarge;
    }

    public boolean isLarge() {
        return isLarge;
    }

    public void setLarge(boolean large) {
        isLarge = large;
    }

}
