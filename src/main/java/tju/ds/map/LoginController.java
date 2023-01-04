package tju.ds.map;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Label loginLabel;

    @FXML
    protected void onHelloButtonClick() {
        Label label2 = new Label();
        label2.setText("我是label2");
        label2.setLayoutX(120);
        label2.setLayoutY(120);
        anchorPane.getChildren().add(label2);
    }

    @FXML
    protected void onLoginLabelClick() {
        loginLabel.setLayoutX(loginLabel.getLayoutX() + 10);
    }

    public void show(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
        Scene loginScene = new Scene(fxmlLoader.load(), 400, 400);
        stage.centerOnScreen();
        stage.setScene(loginScene);
    }
}