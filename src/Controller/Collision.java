package Controller;

import Model.*;
import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;

import java.util.List;

public class Collision {

    protected final PhysicalObject object1;
    protected final PhysicalObject object2;
    private boolean crash;

    public Collision(PhysicalObject one, PhysicalObject two) {
        this.object1 = one;
        this.object2 = two;
        this.crash = detectCollision();
    }

    public boolean isCrash() {
        return crash;
    }

    /**
     * copied from bumpers, could be refined even more
     *
     * @return
     */
    public boolean detectCollision() {
        Point2D p1 = object1.getPosition();
        Dimension2D d1 = object1.getSize();

        Point2D p2 = object2.getPosition();
        Dimension2D d2 = object2.getSize();

        if (p1.getX() != p1.getX() || p1.getY() != p1.getY() || p2.getX() != p2.getX() || p2.getY() != p2.getY()) {
            return false;
        }

        boolean above = p1.getY() + d1.getHeight() < p2.getY();
        boolean below = p1.getY() > p2.getY() + d2.getHeight();
        boolean right = p1.getX() + d1.getWidth() < p2.getX();
        boolean left = p1.getX() > p2.getX() + d2.getWidth();

        return !above && !below && !right && !left;
    }

    public void evaluate() {
        //obstacle and starcraft: participant loses
        if ((object1 instanceof Obstacle || object1 instanceof LargeDebris)
                && object2 instanceof SpaceCraft) {
            ((SpaceCraft) object2).destroy();
        } else if ((object2 instanceof Obstacle || object2 instanceof LargeDebris)
                && object1 instanceof SpaceCraft) {
            ((SpaceCraft) object1).destroy();
        }
        // LASER COLLISIONS
        // with spacecraft: do nothing
        else if ((object1 instanceof Laser && object2 instanceof SpaceCraft) || (object2 instanceof Laser && object1 instanceof SpaceCraft)) {
            // do nothing
        }
        // with large debris: break down large debris
        else if (object1 instanceof Laser
                && object2 instanceof LargeDebris) {
            ((LargeDebris) object2).breakDown(object1.getDirV());
        } else if (object2 instanceof Laser
                && object1 instanceof LargeDebris) {
            ((LargeDebris) object1).breakDown(object2.getDirV());
        }
        // with all other objects: Laser disappears
        else if (object1 instanceof Laser) {
            ((Laser) object1).disappear();
        } else if (object2 instanceof Laser) {
            ((Laser) object2).disappear();
        } else if (object2.getClass().equals(Model.Laser.class)
                && object1.getClass().equals(Model.LargeDebris.class)) {
            ((LargeDebris) object1).breakDown(object2.getDirV());
        }
        //small debris and obstacle: small debris evaporates
        else if (object1 instanceof SmallDebris
                && object2 instanceof Obstacle) {
            ((SmallDebris) object1).evaporate();
        } else if (object2 instanceof SmallDebris
                && object1 instanceof Obstacle) {
            ((SmallDebris) object2).evaporate();
        }
        // spacecraft and small debris: push small debris
        else if (object1 instanceof SpaceCraft
                && object2 instanceof SmallDebris) {
            ((SmallDebris) object2).push(object1.getDirV(), object2.getPosition());
        }
        else if (object2 instanceof SpaceCraft
                && object1 instanceof SmallDebris) {
            ((SmallDebris) object1).push(object2.getDirV(), object2.getPosition());
        }
        //else: crash() except for laser, which just disappears
        else {
            crash(object1, object2);
        }
    }


    /**
     * setting new directions and speed for objects
     *
     * @param one
     * @param two
     */
    public void crash(PhysicalObject one, PhysicalObject two) {
        //stossnormale
        double[] pt1 = new double[2];
        pt1[0] = two.getPosition().getX() - one.getPosition().getX();
        pt1[1] = two.getPosition().getY() - one.getPosition().getY();
        //stossnormale normieren
        double[] p1 = new double[2];
        double lengthpt1 = Math.sqrt(Math.pow(pt1[0], 2) + Math.pow(pt1[1], 2));
        p1[0] = pt1[0] / lengthpt1;
        p1[1] = pt1[1] / lengthpt1;

        //newV1 = v2p + v1o
        double mult1 = p1[0] * one.getDirV()[0] + p1[1] * one.getDirV()[1];
        double mult2 = p1[0] * two.getDirV()[0] + p1[1] * two.getDirV()[1];

        double[] v1o = new double[2];
        v1o[0] = one.getDirV()[0] - mult1 * p1[0];
        v1o[1] = one.getDirV()[1] - mult1 * p1[1];

        double[] v2p = new double[2];
        v2p[0] = p1[0] * mult2;
        v2p[1] = p1[1] * mult2;

        //new vector for one
        one.getDirV()[0] = v2p[0] + v1o[0];
        one.getDirV()[1] = v2p[1] * v2p[1];


        //update speed and direction of two
        double[] v1p = new double[2];
        v1p[0] = one.getDirV()[0] - v1o[0];
        v1p[1] = one.getDirV()[1] - v1o[1];

        double[] v2o = new double[2];
        v2o[0] = two.getDirV()[0] - v2p[0];
        v2o[1] = two.getDirV()[1] - v2p[1];

        //new vector for two
        two.getDirV()[0] = v2o[0] + v1p[0];
        two.getDirV()[1] = v2o[1] + v1p[1];
    }
}
