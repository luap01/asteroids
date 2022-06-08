package Model;

import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;

public class Obstacle extends PhysicalObject {

	public Obstacle(Point2D position, Dimension2D size, String iconLocation, double[] dirV) {
		super(position, size, iconLocation, dirV);
	}

	public Obstacle() {
		super();
	}
}
