package Controller;

import Model.SpaceCraft;
import javafx.scene.canvas.Canvas;

public class MouseSteering extends Steering {

    public MouseSteering(Canvas canvas, SpaceCraft userCraft) {
        super("Mouse Steering", canvas);
        setImpl(new MouseImplementor(canvas, userCraft));
    }

    @Override
    public boolean getShooting() {
        return getImpl().getShooting();
    }

    public double[] getDir() {
        return getImpl().getDir();
    }

}
