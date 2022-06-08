package Model;

import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;

public class SmallDebris extends Debris {
    private boolean evaporated;

    public SmallDebris(Point2D position, Dimension2D size, String iconLocation, double[] dirV) {
        super(position, size, iconLocation, false, dirV);
    }

    public SmallDebris() {
        super(false);
    }

    @Override
    public boolean isLarge() {
        return false;
    }

    //methods
    public void push(double[] dirV, Point2D pos) {
//        setDirV(dirV);
        double dirX = getPosition().getX() - pos.getX();
        double dirY = getPosition().getY() - pos.getY();
        double length = Math.sqrt(Math.pow(dirX,2) + Math.pow(dirY,2));
        double lengthSpaceCraft = Math.sqrt(Math.pow(dirV[0],2) + Math.pow(dirV[1],2));
        if (length>0) {
            dirX = dirX / length * lengthSpaceCraft;
            dirY = dirY / length * lengthSpaceCraft;
        }
        setDirV(new double[] {dirX, dirY});

    }

    public void evaporate() {
        evaporated=true;
    }

    public boolean isEvaporated() {
        return evaporated;
    }
}
