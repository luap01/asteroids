package View;
import Game.App;
import Game.Observer;
import Model.GameBoardEntity;
import Model.GameOutcome;
import Model.Laser;
import Model.PhysicalObject;
import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.HashMap;

public class GameBoardUI extends Observer {

	private final GameBoardEntity gameBoardEntity;
	private final Canvas canvas;
	private HashMap<String, Image> imageCache;
	private static final String BACKGROUND_IMAGE = "background_01.png";
	private App app;

	public GameBoardUI(GameBoardEntity gameBoardEntity, Canvas canvas, App app) {
		this.gameBoardEntity = gameBoardEntity;
		this.canvas = canvas;
		this.app = app;
		setup();
	}

	public void setup() {
		setupImageCache();
		paint();
	}

	public void updateGame() {

	}

	@Override
	public void update() {
		paint();
		if (gameBoardEntity.getGameOutcome() == GameOutcome.LOST) {
			showAsyncAlert("Oh... you lost");
		}
		else if (gameBoardEntity.getGameOutcome() == GameOutcome.WON) {
			showAsyncAlert("You won!");
//			app.switchToLeaderboardScene(gameBoardEntity.getScore());
		}
	}

	private void setupImageCache() {
		this.imageCache = new HashMap<>();
		for (PhysicalObject object : this.gameBoardEntity.getEntities()) {
			String imageLocation = object.getIconLocation();
			this.imageCache.computeIfAbsent(imageLocation, this::getImage);
		}
		String playerImageLocation = this.gameBoardEntity.getSpaceCraft().getIconLocation();
		this.imageCache.put(playerImageLocation, getImage(playerImageLocation));

		this.imageCache.put(BACKGROUND_IMAGE, getImage(BACKGROUND_IMAGE));
	}

	private Image getImage(String objectImageFilePath) {
		URL objectImageUrl = getClass().getClassLoader().getResource(objectImageFilePath);
		if (objectImageUrl == null) {
			throw new IllegalArgumentException(
					"Please ensure that your resources folder contains the appropriate files for this exercise.");
		}
		return new Image(objectImageUrl.toExternalForm());
	}

	public void paint() {
		GraphicsContext graphicsContext = canvas.getGraphicsContext2D();

		graphicsContext.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

		graphicsContext.drawImage(this.imageCache.get(BACKGROUND_IMAGE), 0, 0, canvas.getWidth(), canvas.getHeight());
//		graphicsContext.setFill(Color.valueOf("#222222"));
//		graphicsContext.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

		paintPhysicalObject(gameBoardEntity.getSpaceCraft());

		for(PhysicalObject object: gameBoardEntity.getEntities()) {
			paintPhysicalObject(object);
		}
	}

	private void paintPhysicalObject(PhysicalObject object) {
		Point2D position = object.getPosition();
		GraphicsContext graphicsContext = canvas.getGraphicsContext2D();

		if(object instanceof Laser) {
			graphicsContext.setLineWidth(10.0);
			graphicsContext.setStroke(Color.RED);
			graphicsContext.moveTo(position.getX(), position.getY());
			double dirV[] = object.getDirV();
			double laserLength = Math.sqrt(Math.pow(dirV[0],2) + Math.pow(dirV[1],2));
			double laserEndX;
			double laserEndY;
			if (laserLength>0) {
				laserEndX = position.getX() + dirV[0] / laserLength * 10;
				laserEndY = position.getY() + dirV[1] / laserLength * 10;
			} else {
				laserEndX = position.getX();
				laserEndY = position.getY() - 100;
			}
			graphicsContext.strokeLine(position.getX(), position.getY(), laserEndX, laserEndY);

		} else {
			graphicsContext.drawImage(this.imageCache.get(object.getIconLocation()), position.getX(),
					position.getY(), object.getSize().getWidth(), object.getSize().getHeight());
		}
	}

	private void showAsyncAlert(String message) {
		Platform.runLater(() -> {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setHeaderText(message);
			alert.showAndWait();
			System.exit(0);
		});
	}
}
