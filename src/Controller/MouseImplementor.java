package Controller;

import Model.SpaceCraft;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

// Mouse Steering Class

public class MouseImplementor implements SteeringImplementation {

    private static final int ANGLE_90_DEGREES = 0;
    private static final int ANGLE_270_DEGREES = 180;

    private final SpaceCraft userCraft;
    private boolean isShooting;
    private boolean paused;
    private double[] dir;

    public MouseImplementor(Canvas canvas, SpaceCraft userCraft) {
        this.userCraft = userCraft;
        dir = userCraft.getDirV();
        paused = false;
        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, this::mousePressed);
        canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, this::mouseReleased);
        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, this::mouseDragged);
    }

    private void mousePressed(MouseEvent clickEvent) {
        if (clickEvent.getButton() == MouseButton.SECONDARY) { // if right-click, shoot laser
            isShooting = true;
        }

        if (clickEvent.getButton() == MouseButton.PRIMARY) {
            Point2D craftPosition = userCraft.getPosition();
            Point2D clickPosition = new Point2D(clickEvent.getX(), clickEvent.getY());
            double[] v = new double[2];
            double deltaX = clickPosition.getX() - craftPosition.getX();
            v[0] = deltaX;
            deltaX = Math.abs(deltaX);
            double deltaY = clickPosition.getY() - craftPosition.getY();
            //implementation with vectors
            v[1] = deltaY;
            dir = v;
        }
    }

    private void mouseDragged(MouseEvent clickEvent) {
        if (clickEvent.getButton() == MouseButton.PRIMARY) {
            Point2D craftPosition = userCraft.getPosition();
            Point2D clickPosition = new Point2D(clickEvent.getX(), clickEvent.getY());
            double[] v = new double[2];
            double deltaX = clickPosition.getX() - craftPosition.getX();
            v[0] = deltaX;
            deltaX = Math.abs(deltaX);
            double deltaY = clickPosition.getY() - craftPosition.getY();
            //implementation with vectors
            v[1] = deltaY;
            dir = v;
        }
    }

    private void mouseReleased(MouseEvent mouseEvent) {
        if (mouseEvent.getButton().equals(MouseButton.SECONDARY)) {
            isShooting = false;
        }
        if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
            dir = userCraft.getDirV();
        }
    }


    @Override
    public boolean getShooting() {
        return isShooting;
    }

    @Override
    public boolean getPausePressed() {
        if (paused) {
            paused = false;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void setPausePressed() {
        paused = true;
    }

    @Override
    public double[] getDir() {
        return dir;
    }

}
