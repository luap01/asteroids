package Game;

import Controller.GameBoardController;
import Model.GameBoardEntity;
import View.GameBoardUI;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.concurrent.ExecutionException;


public class App extends Application {

    private static final int PREF_HEIGHT = 1000;
    private static final int PREF_WIDTH = 1500;
    private Stage st;

    public static void startApp(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.st = stage;

        stage.setTitle("Galactic Garbagemen");

        startScreen();
    }

    public void switchToLeaderboardScene(int score) {
        //ask to enter name
        final String[] name = new String[1];

        TextField textfield = new TextField();

        Button btn = new Button();
        btn.setText("Submit");

        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                name[0] = textfield.getText();
                Participant part = new Participant(name[0], score);
                LeaderboardControl con = new LeaderboardControl();
                try {
                    con.addParticipant(part);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Scene nextScene = createLeaderBoardScene();
                st.setScene(nextScene);
                st.show();
            }
        });

        TilePane tilePane = new TilePane();
        tilePane.getChildren().add(textfield);
        tilePane.getChildren().add(btn);

        Scene scene = new Scene(tilePane, PREF_WIDTH, PREF_HEIGHT);
        st.setScene(scene);
        st.show();
    }

    private Scene createLeaderBoardScene() {
        Button btn = new Button();
        btn.setText("Restart!");
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    startScreen();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        TableView<Participant> table;
        //Columns
        TableColumn<Participant, Integer> placeColumn = new TableColumn<>("Place");
        placeColumn.setMinWidth(200);
        placeColumn.setMaxWidth(200);
        placeColumn.setCellValueFactory(new PropertyValueFactory<>("place"));

        TableColumn<Participant, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setMinWidth(200);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Participant, Integer> highscoreColumn = new TableColumn<>("Highscore");
        highscoreColumn.setMinWidth(200);
        highscoreColumn.setCellValueFactory(new PropertyValueFactory<>("highscore"));


        LeaderboardControl control = new LeaderboardControl();
        //Table
        table = new TableView<>();
        ObservableList<Participant> l = FXCollections.observableArrayList();
        l.addAll(control.getSortedList());
        table.setItems(l);
        table.getColumns().addAll(placeColumn, nameColumn, highscoreColumn);

        VBox vBox = new VBox();
        vBox.getChildren().addAll(table, btn);

        return new Scene(vBox);
    }

    private Scene getLeaderBoardScene() throws IOException {
        Parent root = loadFXML("primary");
        return new Scene(root);
    }

    public void startScreen() {
        RadioButton mouseSteeringOption = new RadioButton("Mouse steering");
        RadioButton keyboardSteeringOption = new RadioButton("Keyboard steering");
        ToggleGroup steeringRadioGroup = new ToggleGroup();
        mouseSteeringOption.setToggleGroup(steeringRadioGroup);
        keyboardSteeringOption.setToggleGroup(steeringRadioGroup);
        steeringRadioGroup.selectToggle(mouseSteeringOption);

        Button btn = new Button();
        btn.setText("Start Game");

        btn.setOnAction(event -> {
            System.out.println("Started a Game!");

            Canvas canvas = new Canvas();
            canvas.setHeight(PREF_HEIGHT);
            canvas.setWidth(PREF_WIDTH);

            canvas.setFocusTraversable(true);

            VBox vbox = new VBox(canvas);
            Scene gameScene = new Scene(vbox);
            st.setScene(gameScene);

            GameBoardEntity gameBoardEntity = new GameBoardEntity();
            GameBoardController gameBoardController = new GameBoardController(gameBoardEntity, canvas);
            GameBoardUI gameBoardUI = new GameBoardUI(gameBoardEntity, canvas, this);

            gameBoardEntity.attach(gameBoardController);
            gameBoardEntity.attach(gameBoardUI);

            Toggle steeringOption = steeringRadioGroup.getSelectedToggle();
            if (steeringOption == mouseSteeringOption) {
                gameBoardController.setMouseSteering();
            } else {
                gameBoardController.setKeyboardSteering();
            }

            st.setOnCloseRequest(closeEvent -> gameBoardController.stopGame());
            st.show();

            gameBoardController.startGame();
        });

        Button btn3 = new Button();
        btn3.setText("Exit");
        btn3.setOnAction(e -> Platform.exit());

        VBox vbox = new VBox(5); // 5 is the spacing between elements in the VBox
        vbox.getChildren().addAll(btn, mouseSteeringOption, keyboardSteeringOption, btn3);

        btn.setMaxWidth(300D);
        btn3.setMaxWidth(300D);
        vbox.setFillWidth(true);

        StackPane root = new StackPane();
        root.getChildren().add(vbox);
        vbox.setAlignment(Pos.CENTER);
        st.setScene(new Scene(root, PREF_WIDTH, PREF_HEIGHT));
        st.show();
    }


    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/"+fxml + ".fxml"));
        return fxmlLoader.load();
    }

}
