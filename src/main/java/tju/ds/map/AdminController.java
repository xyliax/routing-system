package tju.ds.map;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import lombok.SneakyThrows;
import tju.ds.map.dao.MongoController;
import tju.ds.map.model.User;
import tju.ds.map.model.UserType;

import java.io.IOException;

import static tju.ds.map.MapApplication.stage;

public class AdminController {
    private final MongoController mongoController = MongoController.getInstance();
    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;
    @FXML
    private CheckBox adminButton;
    @FXML
    private Button SaveButton;
    @FXML
    private Button BackButton;
    private User user;

    @SneakyThrows
    public static Scene scene() {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginController.class.getResource("admin.fxml"));
        return new Scene(fxmlLoader.load());
    }

    @FXML
    public void initialize() {
    }

    @FXML
    public void enter(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            user = mongoController.retrieveUser(usernameField.getText());
            if (user == null) {
                usernameField.clear();
                passwordField.clear();
                BackButton.requestFocus();
                usernameField.setPromptText("找不到该用户！！！");
                adminButton.setSelected(false);
            } else {
                passwordField.setText(user.getPassword());
                adminButton.setSelected(user.getType() == UserType.ADMIN);
            }
        }
    }

    @FXML
    protected void OnSaveButtonClicked(MouseEvent mouseEvent) {
        user.setUsername(usernameField.getText());
        user.setPassword(passwordField.getText());
        if (adminButton.isSelected()) {
            user.setType(UserType.ADMIN);
        } else {
            user.setType(UserType.NORMAL);
        }
        mongoController.updateUser(user);
    }

    @FXML
    protected void OnBackButtonClicked(MouseEvent mouseEvent) throws IOException {
        stage.setScene(LoginController.scene());
    }
}