package Model;

import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;

public class Moon extends Obstacle {

	private final Planet planet;
	private final static float DENSITY = 0.2F;

	public Moon(Point2D position, Dimension2D size, String iconLocation, Planet planet, double[] dirV) {
		super(position, size, iconLocation, dirV);
		this.planet = planet;
		this.mass = (int) (DENSITY * size.getHeight() * size.getWidth());
	}

	public Moon(Planet planet) {
		super();
		this.planet = planet;
	}

}
