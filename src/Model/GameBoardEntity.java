package Model;

import Game.Observer;

import javafx.geometry.Dimension2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameBoardEntity {
    List<PhysicalObject> entities;
    private SpaceCraft spaceCraft;
    private GameOutcome gameOutcome;
    private List<Observer> observers;
    private int score = 0;

    public GameBoardEntity() {
        entities = new ArrayList<>();
        observers = new ArrayList<>();
    }

    public List<PhysicalObject> getEntities() {
        return entities;
    }

    public SpaceCraft getSpaceCraft() {
        return spaceCraft;
    }

    public void setSpaceCraft(SpaceCraft spaceCraft) {
        this.spaceCraft = spaceCraft;
        observers.forEach(obs -> obs.update());
    }

    public GameOutcome getGameOutcome() {
        return gameOutcome;
    }

    public void setGameOutcome(GameOutcome gameOutcome) {
        this.gameOutcome = gameOutcome;
        observers.forEach(obs -> obs.update());
    }

    public void attach(Observer observer) {
        observers.add(observer);
    }

    public void detach(Observer observer) {
        observers.remove(observer);
    }

    public void updatePhysicalObjects(Dimension2D gameBoardSize, double[] dirV, float slowingPower, float turningPower) {
        entities.forEach(object -> object.fly(gameBoardSize));
        spaceCraft.setHeadedV(dirV);
        spaceCraft.fly(gameBoardSize, slowingPower, turningPower);
        score++;
        observers.forEach(obs -> obs.update());
    }

    public void addEntity(PhysicalObject entity) {
        if(entity!=null) {
            entities.add(entity);
        }
    }

    public void checkEvaporations() {
        for(Iterator<PhysicalObject> it = entities.iterator(); it.hasNext();) {
            PhysicalObject entity = it.next();
            if(entity instanceof SmallDebris) {
                if(((SmallDebris) entity).isEvaporated()) {
                    it.remove();
                }
            }
        }
    }

    public boolean debrisLeft() {
        for (PhysicalObject entity : entities) {
            if (entity instanceof Debris) {
                return true;
            }
        }
        return false;
    }

    public boolean hasLaser() {
        for(PhysicalObject entity:entities) {
            if(entity instanceof Laser) {
                return true;
            }
        }
        return false;
    }

    public void checkLaser() {
        for(Iterator<PhysicalObject> it = entities.iterator(); it.hasNext();) {
            PhysicalObject entity = it.next();
            if(entity instanceof Laser) {
                if(((Laser) entity).hasDisappeared()) {
                    it.remove();
                }
            }
        }
    }

    public void checkBreakdowns() {
        List<Debris> newSmallDebris = new ArrayList<>();

        for(Iterator<PhysicalObject> it = entities.iterator(); it.hasNext();) {
            PhysicalObject entity = it.next();
            if(entity instanceof LargeDebris) {
                if(((LargeDebris) entity).isBrokenDown()) {
                    it.remove();
                    newSmallDebris.addAll(((LargeDebris) entity).getSmallDebris());
                }
            }
        }

        entities.addAll(newSmallDebris);
    }

    public int getScore() {
        return score;
    }
}
