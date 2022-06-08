package Model;

import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.List;

public class LargeDebris extends Debris {
    private boolean brokenDown;

    public LargeDebris(Point2D position, Dimension2D size, String iconLocation, double[] dirV) {
        super(position, size, iconLocation, true, dirV);
    }

    public LargeDebris() {
        super(true);
    }

    @Override
    public boolean isLarge() {
        return true;
    }

    //methods
    public void breakDown(double[] dirVHit) {
        brokenDown=true;
        setDirV(dirVHit);
    }

    public List<Debris> getSmallDebris() {
        List<Debris> result = new ArrayList<>();
        Debris small1 = new SmallDebris(getPosition(),
                new Dimension2D(40, 30),
                "debris_03.png",
                new double[]{2,0});
        Debris small2 = new SmallDebris(getPosition(),
                new Dimension2D(40, 30),
                "debris_03.png",
                new double[]{0,-2});
        result.add(small1);
        result.add(small2);

        return result;
    }

    public boolean isBrokenDown() {
        return brokenDown;
    }
}
