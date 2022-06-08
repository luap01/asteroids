package Model;

import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;

public class SpaceCraft extends PhysicalObject {

    private final static float DENSITY = 0.7F;
    private double[] headedV;
    private boolean destroyed;

    private static final String IMAGE_FILE = "spacecraft_01.png";

    public SpaceCraft(Point2D position, float speed, float direction, Dimension2D size, String iconLocation, double[] dirV) {
        super(position, size, iconLocation, dirV);
        this.mass = (int) (DENSITY* size.getHeight()*size.getWidth());
        headedV = getDirV();
        setIconLocation(IMAGE_FILE);
    }

    public SpaceCraft() {
        super();
        setIconLocation(IMAGE_FILE);
    }

    public void fly(Dimension2D gameBoardSize, float slowingPower, float turningPower) {
        double maxX = gameBoardSize.getWidth();
        double maxY = gameBoardSize.getHeight();

        //resulting vector of direction
        double[] resultingV = new double[] {slowingPower*getDirV()[0] + turningPower*getHeadedV()[0], slowingPower*getDirV()[1] + turningPower*getHeadedV()[1]};

        double vLength = Math.sqrt(Math.pow(resultingV[0],2) + Math.pow(resultingV[1],2));

        if (vLength>30 && vLength>0) {
            resultingV[0] = resultingV[0] / vLength * 30;
            resultingV[1] = resultingV[1] / vLength * 30;
        }

        //update direction vector
        this.setDirV(resultingV);

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

        if (newX!=newX || newY !=newY) {
            System.out.println("Test");
        }

        //update position with new vector
        this.setPosition(new Point2D(newX, newY));
    }

    public void destroy() {
        destroyed=true;
    }

    public void shoot() {

    }

    public double[] getHeadedV() {
        return headedV;
    }

    public void setHeadedV(double[] headedV) {
        if(headedV[0]!=headedV[0]) {
            headedV[0]=0;
        }
        if(headedV[1]!=headedV[1]) {
            headedV[1]=0;
        }
        this.headedV = headedV;
    }

    public boolean isDestroyed() {
        return destroyed;
    }
}
