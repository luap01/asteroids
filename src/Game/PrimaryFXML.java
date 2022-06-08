package Game;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class PrimaryFXML implements Initializable {

    //necessary
    private Stage stage;
    private Scene scene;
    private Parent root;

    public void switchToSecondary(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/media/secondary.fxml"));
        root = fxmlLoader.load();
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void toLeaderBoardButton(ActionEvent event) throws IOException, ExecutionException, InterruptedException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/media/secondary.fxml"));
        root = fxmlLoader.load();

        FXMLController contr = fxmlLoader.getController();
        contr.getControl().addParticipant(new Participant(nameField.getText(), Integer.parseInt(reachedScore.getText())));

        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private TextField nameField;
    @FXML
    private Label reachedScore;
    @FXML
    private Button submitButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
