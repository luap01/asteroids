package Model;

import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;


public abstract class PhysicalObject {

    private final static float SLOW_DOWN_FACTOR = 0.98F;

    private Dimension2D size;
    private Point2D position;
    private String iconLocation;
    protected int mass;
    private double[] dirV;

    public PhysicalObject(Point2D position, Dimension2D size, String iconLocation, double[] dirV) {
        this.position = position;
        this.size = size;
        this.iconLocation = iconLocation;
        this.dirV = dirV;
    }

    public PhysicalObject() {
        size = new Dimension2D(0, 0);
        position = new Point2D(0, 0);
        this.dirV = new double[]{0, 0};
        iconLocation = "media/default-icon.png";
    }

    public String getIconLocation() {
        return iconLocation;
    }

    public void setIconLocation(String iconLocation) {
        this.iconLocation = iconLocation;
    }

    public Point2D getPosition() {
        return position;
    }

    public void setPosition(Point2D position) {
        this.position = position;
    }

    public Dimension2D getSize() {
        return size;
    }

    public void setSize(Dimension2D size) {
        this.size = size;
    }

    public int getMass() {
        return mass;
    }

    public void setMass(int mass) {
        this.mass = mass;
    }

    /**
     * fly method to update position
     */
    public void fly(Dimension2D gameBoardSize) {
        double maxX = gameBoardSize.getWidth();
        double maxY = gameBoardSize.getHeight();

        //update position
        double newX = this.getPosition().getX() + getDirV()[0];
        double newY = this.getPosition().getY() + getDirV()[1];

        // calculate position in case the boarder of the game board has been reached
        if (newX < 0) {
            newX = maxX+newX;
        } else if (newX + this.getSize().getWidth() > maxX) {
            newX = newX - maxX;
        }

        if (newY < 0) {
            newY = maxY+newY;
        } else if (newY + this.getSize().getHeight() > maxY) {
            newY=newY-maxY;
        }

        //update position with new vector
        this.setPosition(new Point2D(newX, newY));

        //slow down the speed of physicalobject
        this.getDirV()[0] *= SLOW_DOWN_FACTOR;
        this.getDirV()[1] *= SLOW_DOWN_FACTOR;
    }

    public double[] getDirV() {
        return dirV;
    }

    public void setDirV(double[] dirV) {
        if(dirV[0]!=dirV[0]) {
            dirV[0]=0;
        }
        if(dirV[1]!=dirV[1]) {
            dirV[1]=0;
        }

        this.dirV = dirV;
    }
}
