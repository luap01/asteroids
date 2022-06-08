package Model;

import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;

public class Planet extends Obstacle {

	private final static float DENSITY = 0.3F;

	public Planet(Point2D position, Dimension2D size, String iconLocation, double[] dirV) {
		super(position, size, iconLocation, dirV);
		this.mass = (int) (DENSITY * size.getHeight() * size.getWidth());
	}

	public Planet() {
		super();
	}

}
