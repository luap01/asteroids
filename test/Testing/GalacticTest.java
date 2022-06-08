package Testing;

import Controller.*;
import Game.LeaderboardControl;
import Game.Participant;
import Model.*;
import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class GalacticTest {

    @Test
    public void mockCollisionIsCollision() {
        GameBoardEntity gameBoardEntity = new GameBoardEntity();
        Canvas canvas = new Canvas();
        GameBoardController gameBoardController = new GameBoardController(gameBoardEntity, canvas);

        SpaceCraft spaceCraft = new SpaceCraft();
        spaceCraft.setPosition(new Point2D(750, 800));
        spaceCraft.setSize(new Dimension2D(50, 50));
        gameBoardEntity.setSpaceCraft(spaceCraft);

        Planet planet = new Planet();
        planet.setPosition(new Point2D(750, 800));
        planet.setSize(new Dimension2D(50, 50));
        gameBoardEntity.addEntity(planet);

        List<Collision> collisions = gameBoardController.detectCollisions();

        assert(collisions.size()==1);
    }

    @Test
    public void testIsLargeSmallDebris() {
        Debris debris = new SmallDebris();
        assert (!debris.isLarge());
    }

    @Test
    public void testIsLargeLargeDebris() {
        Debris debris = new LargeDebris();
        assert (debris.isLarge());
    }

    @Test
    public void checkPositionChangeOnFly() {
        SpaceCraft spaceCraft = new SpaceCraft();
        Point2D oldPos = spaceCraft.getPosition();
        spaceCraft.fly(new Dimension2D(100,100));
        assert (!oldPos.equals(spaceCraft.getPosition()));
    }

    @Test
    void testMouseSteering() {
        GameBoardEntity entity = new GameBoardEntity();
        Canvas canvas = new Canvas();
        GameBoardController gameBoardController = new GameBoardController(entity, canvas);
        gameBoardController.setMouseSteering();
        MouseSteering steering = new MouseSteering(canvas, entity.getSpaceCraft());
        assert(steering.equals(gameBoardController.getSteering()));
    }

    @Test
    void testKeyBoardSteering() {
        GameBoardEntity entity = new GameBoardEntity();
        Canvas canvas = new Canvas();
        GameBoardController gameBoardController = new GameBoardController(entity, canvas);
        gameBoardController.setKeyboardSteering();
        KeyboardSteering steering = new KeyboardSteering(canvas, entity.getSpaceCraft());
        assert(steering.equals(gameBoardController.getSteering()));

    }

    /**checks wether a participant is stored in database
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void testLeaderboard1() throws ExecutionException, InterruptedException {
        LeaderboardControl l = new LeaderboardControl();
        Participant one = new Participant("loser", 3);
        l.addParticipant(one);

        boolean check = false;
        List<Participant> list = l.getSortedList();
        check = list.stream().anyMatch(p -> p.getName().equals("loser"));

        assert(check);
    }

    /**
     * tests wether getSortedList() actually gives back a sorted list according to highscore
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void testLeaderboard2() throws ExecutionException, InterruptedException {
        LeaderboardControl l = new LeaderboardControl();

        List<Participant> list = l.getSortedList();
        boolean check = true;
        for (int i = 0; i < list.size() - 1; i++) {
            if (list.get(i).getHighscore() > list.get(i + 1).getHighscore())
                check = false;
        }

        assert(check);
    }

    @Test
    public void testIsCrash() {
        GameBoardEntity gameBoardEntity = new GameBoardEntity();

        SpaceCraft spaceCraft = new SpaceCraft();
        spaceCraft.setPosition(new Point2D(750, 800));
        spaceCraft.setSize(new Dimension2D(50, 50));
        gameBoardEntity.setSpaceCraft(spaceCraft);

        Planet planet = new Planet();
        planet.setPosition(new Point2D(750, 800));
        planet.setSize(new Dimension2D(50, 50));
        gameBoardEntity.addEntity(planet);

        Collision collision = new Collision(spaceCraft, planet);

        assert(collision.isCrash());
    }

    @Test
    public void testIsNotCrash() {
        GameBoardEntity gameBoardEntity = new GameBoardEntity();

        SpaceCraft spaceCraft = new SpaceCraft();
        spaceCraft.setPosition(new Point2D(750, 800));
        spaceCraft.setSize(new Dimension2D(50, 50));
        gameBoardEntity.setSpaceCraft(spaceCraft);

        Planet planet = new Planet();
        planet.setPosition(new Point2D(1, 3));
        planet.setSize(new Dimension2D(50, 50));
        gameBoardEntity.addEntity(planet);

        Collision collision = new Collision(spaceCraft, planet);

        assert(!collision.isCrash());
    }
}
