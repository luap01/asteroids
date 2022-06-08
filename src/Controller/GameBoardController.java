package Controller;

import Game.Observer;
import Game.Participant;
import Model.*;
import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.*;

public class GameBoardController extends Observer {

    private final GameBoardEntity gameBoardEntity;
    private boolean isRunning;
    private Steering steering;
    private Canvas canvas;
    private HashMap<String, Image> imageCache;

    private final static float TURNING_POWER_MOUSE = 0.01F; //at 100% it turns directly
    private final static float SLOWING_POWER_MOUSE = 0.9F;
    private final static float TURNING_POWER_KEYBOARD = 0.000000001F; //at 100% it turns directly
    private final static float SLOWING_POWER_KEYBOARD = 1F-(1e-038F);

    /**
     * The update period of the game in ms, this gives us 25 fps.
     */
    private static final int UPDATE_PERIOD = 1000 / 25;
    /**
     * Timer responsible for updating the game every frame that runs in a separate
     * thread.
     */
    private Timer gameTimer;

    public GameBoardController(GameBoardEntity gameBoardEntity, Canvas canvas) {
        this.gameBoardEntity=gameBoardEntity;
        this.canvas = canvas;
        isRunning = false;
        setupGame();
    }

    public static void main(String[] args) {

    }

    public void startGame() {
        if (!isRunning) {
            isRunning=true;
            startTimer();
        }
    }

    private void startTimer() {
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                updateGame();
            }
        };
        if (this.gameTimer != null) {
            this.gameTimer.cancel();
        }
        this.gameTimer = new Timer();
        this.gameTimer.scheduleAtFixedRate(timerTask, UPDATE_PERIOD, UPDATE_PERIOD);
    }

    private void updateGame() {
        if (isRunning) {
            // updates positions
            Dimension2D gameBoardSize = new Dimension2D(canvas.getWidth(), canvas.getHeight());
            float slowingPower;
            float turningPower;
            switch (steering.getUserInput()) {
                case "Mouse steering":
                    slowingPower=SLOWING_POWER_MOUSE;
                    turningPower=TURNING_POWER_MOUSE;
                    break;
                case "Keyboard steering":
                    slowingPower=SLOWING_POWER_KEYBOARD;
                    turningPower=TURNING_POWER_KEYBOARD;
                    break;
                default:
                    slowingPower=SLOWING_POWER_MOUSE;
                    turningPower=TURNING_POWER_MOUSE;
            }
            if(steering.getShooting()) {
                double[] laserDirV = gameBoardEntity.getSpaceCraft().getHeadedV();
                double laserDirVLength = Math.sqrt(Math.pow(laserDirV[0],2) + Math.pow(laserDirV[1],2));
                laserDirV[0] = laserDirV[0] / laserDirVLength * 30;
                laserDirV[1] = laserDirV[1] / laserDirVLength * 30;

                Laser laser = new Laser(gameBoardEntity.getSpaceCraft().getPosition(), laserDirV);
                gameBoardEntity.addEntity(laser);
            }

            gameBoardEntity.updatePhysicalObjects(gameBoardSize, steering.getDir(), slowingPower, turningPower);

            // Check for collisions
            List<Collision> collisions = detectCollisions();
            collisions.forEach(col -> col.evaluate());

            if(gameBoardEntity.getSpaceCraft().isDestroyed()) {
                gameBoardEntity.setGameOutcome(GameOutcome.LOST);
            }

            gameBoardEntity.checkLaser();
            gameBoardEntity.checkBreakdowns();
            gameBoardEntity.checkEvaporations();

            if(!gameBoardEntity.debrisLeft()) {
                gameBoardEntity.setGameOutcome(GameOutcome.WON);
            }
        }
    }

    public void stopGame() {
        isRunning=false;
        this.gameTimer.cancel();
    }

    public void update() {
        // when this.gameBoard.getOutcome() is OPEN, do nothing
        if (this.gameBoardEntity.getGameOutcome() == GameOutcome.LOST) {
            //showAsyncAlert("Oh.. you lost.");
            this.stopGame();
        } else if (this.gameBoardEntity.getGameOutcome() == GameOutcome.WON) {
            //showAsyncAlert("Congratulations! You won!!");
            this.stopGame();
        }
    }

    public void setupGame() {
        SpaceCraft spaceCraft = new SpaceCraft();
        spaceCraft.setPosition(new Point2D(750, 800));
        spaceCraft.setSize(new Dimension2D(50, 50));
        spaceCraft.setDirV(new double[]{0,0});

        gameBoardEntity.setSpaceCraft(spaceCraft);

        Planet planet1 = new Planet(new Point2D(50, 50),
                new Dimension2D(70, 70),
                "planet_01.png",
                new double[]{0,0}
                );
        gameBoardEntity.addEntity(planet1);

        Planet planet2 = new Planet(new Point2D(900, 500),
                new Dimension2D(70, 70),
                "planet_02.png",
                new double[]{0,0}
                );
        gameBoardEntity.addEntity(planet2);

        Debris large1 = new LargeDebris(new Point2D(400, 300),
                new Dimension2D(87, 70),
                "debris_02.png",
                new double[]{0.3,0.2}
                );
        gameBoardEntity.addEntity(large1);

        Debris small1 = new SmallDebris(new Point2D(1000, 300),
                new Dimension2D(40, 30),
                "debris_03.png",
                new double[]{0.3,0.2}
                );
        gameBoardEntity.addEntity(small1);
    }

    public void setSteering(Steering steering) {
        this.steering = steering;
    }

    public Steering getSteering() {
        return this.steering;
    }

    public void setMouseSteering() {
        this.steering = new MouseSteering(canvas, gameBoardEntity.getSpaceCraft());
    }

    public void setKeyboardSteering() {
        this.steering = new KeyboardSteering(canvas, gameBoardEntity.getSpaceCraft());
    }

    private void pauseGame(KeyEvent e) {
        if (e.getCode().equals(KeyCode.ESCAPE)) {
            isRunning = !isRunning;
            if (isRunning)
                System.out.println("Unpaused!");
            else System.out.println("Paused!");
        }
    }

    public void paintSpaceCraft(SpaceCraft spaceCraft) {
        Point2D craftPosition = spaceCraft.getPosition();

        canvas.getGraphicsContext2D().drawImage(this.imageCache.get(spaceCraft.getIconLocation()), craftPosition.getX(),
                craftPosition.getY(), spaceCraft.getSize().getWidth(), spaceCraft.getSize().getHeight());
    }

    public List<Collision> detectCollisions() {
        List<PhysicalObject> allObjects = new ArrayList<>(gameBoardEntity.getEntities());
        allObjects.add(gameBoardEntity.getSpaceCraft());

        List<Collision> collisions = new ArrayList<>();

        int listSize = allObjects.size();

        for(int i=0; i<listSize; i++) {
            for(int j=i+1; j<listSize; j++) {
                Collision collision = new Collision(allObjects.get(i), allObjects.get(j));
                if (collision.detectCollision()) {
                    collisions.add(collision);
                }
            }
        }

        return collisions;
    }

}
