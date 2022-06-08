package Controller;

import Model.SpaceCraft;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.LinkedList;
import java.util.Queue;

public class KeyboardImplementor implements SteeringImplementation {

    private static final int ANGLE_0_DEGREES = 0;
    private static final int ANGLE_45_DEGREES = 45;
    private static final int ANGLE_90_DEGREES = 90;
    private static final int ANGLE_135_DEGREES = 135;
    private static final int ANGLE_180_DEGREES = 180;
    private static final int ANGLE_225_DEGREES = 225;
    private static final int ANGLE_270_DEGREES = 270;
    private static final int ANGLE_315_DEGREES = 315;
    private int enforcer = 20;
    private final KeyCode[] dirKeys = new KeyCode[]{KeyCode.W, KeyCode.A, KeyCode.S, KeyCode.D};
    private final Queue<KeyCode> keysPressed = new LinkedList<>();
    private boolean paused;
    private boolean isShooting;
    private double[] dir;

    public KeyboardImplementor(Canvas canvas, SpaceCraft spaceCraft) {
        dir = spaceCraft.getDirV();
        paused = false;
        canvas.addEventHandler(KeyEvent.KEY_PRESSED, this::keyPressed);
        canvas.addEventHandler(KeyEvent.KEY_RELEASED, this::keyReleased);
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

    private void keyPressed(KeyEvent e) {
        KeyCode code = e.getCode();
        System.out.println(code.toString() + " pressed.");
        if (code.equals(KeyCode.SPACE)) {
            isShooting = true;
            return;
        }
        if (keysContain(code) && !keysPressed.contains(code)) {
            keysPressed.add(code);
        }
        if (keysPressed.size() > 2) {
            //remove latest keyPress
            keysPressed.poll();
        }
        //remove conflicting directions
        switch (code) {
            case W -> keysPressed.remove(KeyCode.S);
            case A -> keysPressed.remove(KeyCode.D);
            case S -> keysPressed.remove(KeyCode.W);
            case D -> keysPressed.remove(KeyCode.A);
        }
        updateDir();
    }

    private void keyReleased(KeyEvent e) {
        KeyCode code = e.getCode();
        if (code.equals(KeyCode.SPACE)) {
            isShooting = false;
            return;
        }
        if (keysContain(code)) {
            keysPressed.remove(code);
        }
        updateDir();
    }

    private void updateDir() {
        if (keysPressed.size() == 2) {
            KeyCode[] pressed = new KeyCode[2];
            pressed = keysPressed.toArray(pressed);
            if (arrayContains(pressed, KeyCode.W)) {
                if (arrayContains(pressed, KeyCode.A)) {
                    dir = new double[]{-1, -1};
                } else if (arrayContains(pressed, KeyCode.D)) {
                    dir = new double[]{1, -1};
                }
            } else if (arrayContains(pressed, KeyCode.A)) {
                if (arrayContains(pressed, KeyCode.S)) {
                    dir = new double[]{-1, 1};
                }
            } else if (arrayContains(pressed, KeyCode.S)) {
                if (arrayContains(pressed, KeyCode.D)) {
                    dir = new double[]{1, 1};
                }
            }

        } else if (keysPressed.size() == 1) {
            switch (keysPressed.peek()) {
                case W:
                    dir = new double[]{0, -1};
                    break;
                case A:
                    dir = new double[]{-1, 0};
                    break;
                case S:
                    dir = new double[]{0, 1};
                    break;
                case D:
                    dir = new double[]{1, 0};
                    break;
                default:
                    break;
            }
        } else if (keysPressed.size() == 0) {
            dir = new double[]{0, 0};
        }
    }

        private boolean keysContain (KeyCode e){
            for (KeyCode keyCode : dirKeys) {
                if (keyCode.equals(e)) {
                    return true;
                }
            }
            return false;
        }

        private boolean arrayContains (KeyCode[]pressed, KeyCode searched){
            if (pressed[0].equals(searched)) {
                return true;
            } else return pressed[1].equals(searched);
        }

        public double[] getDir () {
            double[] help = new double[]{dir[0] * enforcer, dir[1] * enforcer};
            return help;
        }
    }
