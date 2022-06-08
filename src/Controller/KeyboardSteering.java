package Controller;

import Model.SpaceCraft;
import javafx.scene.canvas.Canvas;

public class KeyboardSteering extends Steering {

    public KeyboardSteering(Canvas canvas, SpaceCraft spaceCraft) {
        super("Keyboard Steering", canvas);
        setImpl(new KeyboardImplementor(canvas, spaceCraft));
    }

    @Override
    public boolean getShooting() {
        return getImpl().getShooting();
    }

    @Override
    public double[] getDir() {
        return getImpl().getDir();
    }
}
