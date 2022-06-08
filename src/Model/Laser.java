package Model;

import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;

public class Laser extends PhysicalObject {
	private boolean disappeared;

	public Laser(Point2D position, double[] dirV) {
		super(position, new Dimension2D(1, 1), "dummy", dirV);
	}

	public Laser() {
		super();
	}

	public void disappear() {
		disappeared=true;
	}

	public boolean hasDisappeared() {
		return disappeared;
	}

	@Override
	public void fly(Dimension2D gameBoardSize) {
		double maxX = gameBoardSize.getWidth();
		double maxY = gameBoardSize.getHeight();

		//update position
		double newX = this.getPosition().getX() + getDirV()[0];
		double newY = this.getPosition().getY() + getDirV()[1];

		// calculate position in case the boarder of the game board has been reached
		if (newX < 0) {
			disappeared=true;
		} else if (newX + this.getSize().getWidth() > maxX) {
			disappeared=true;
		}

		if (newY < 0) {
			disappeared=true;
		} else if (newY + this.getSize().getHeight() > maxY) {
			disappeared=true;
		}

		//update position with new vector
		this.setPosition(new Point2D(newX, newY));
	}
}
