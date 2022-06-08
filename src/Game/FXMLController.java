package Game;
/*
Put header here


 */

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class FXMLController implements Initializable {

    //necessary
    private Stage stage;
    private Scene scene;
    private Parent root;
    private LeaderboardControl control = new LeaderboardControl();

    //switching scenes
    public void switchToPrimary(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/media/primary.fxml"));
        root = fxmlLoader.load();
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private Label lblOut;
    @FXML private TableView<Participant> tableView;
    @FXML private TableColumn<Participant, Integer> placeColumn;
    @FXML private TableColumn<Participant, String> nameColumn;
    @FXML private TableColumn<Participant, Integer> highscoreColumn;


    @FXML
    private void goBackButton(ActionEvent event) {}
    @FXML
    private void toScoreButton(ActionEvent event) throws IOException {
        switchToPrimary(event);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        placeColumn.setCellValueFactory(new PropertyValueFactory<>("place"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        highscoreColumn.setCellValueFactory(new PropertyValueFactory<>("highscore"));

        ObservableList<Participant> l = FXCollections.observableArrayList();
        l.addAll(control.getSortedList());

        tableView.setItems(l);
    }

    public LeaderboardControl getControl() {
        return control;
    }
}
