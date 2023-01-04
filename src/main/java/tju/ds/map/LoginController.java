package tju.ds.map;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    private Label loginLabel;

    @FXML
    protected void onHelloButtonClick() {
    }

    @FXML
    protected void onLoginLabelClick() {
        loginLabel.setLayoutX(loginLabel.getLayoutX() + 10);
    }

    public void initialize() {
        System.out.println(this + " initialize()...");
    }

    public void show(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
        Scene loginScene = new Scene(fxmlLoader.load(), 400, 400);
        stage.centerOnScreen();
        stage.setScene(loginScene);
    }
}