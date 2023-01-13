package tju.ds.map;

import animatefx.animation.FlipInX;
import animatefx.animation.Wobble;
import javafx.animation.Animation;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import tju.ds.map.dao.MongoController;
import tju.ds.map.model.User;

import java.io.IOException;

import static tju.ds.map.MapApplication.stage;

public class LoginController {
    private final MongoController mongoController = MongoController.getInstance();
    @FXML
    private Button loginButton;
    @FXML
    private Button registerButton;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private ImageView loginLogo;

    public static Scene scene() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginController.class.getResource("login.fxml"));
        return new Scene(fxmlLoader.load());
    }

    @FXML
    protected void onLoginButtonClicked(MouseEvent mouseEvent) {
        String text = loginButton.getText();
        loginButton.setText("正在登录..");
        Animation loginAnimation = new FlipInX(loginButton).getTimeline();
        loginAnimation.setOnFinished(event -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            User user = mongoController.retrieveUser(username);
            if (user == null) {
                usernameField.clear();
                passwordField.clear();
                usernameField.setPromptText("找不到该用户！！！");
                new Wobble(loginLogo).play();
            } else if (!password.equals(user.getPassword())) {
                usernameField.clear();
                passwordField.clear();
                usernameField.setPromptText("密码错误！！！");
                new Wobble(loginLogo).play();
            } else {
                //TODO: success
                MapController.user = user;
                stage.setScene(AdminController.scene());
                //stage.setScene(MapController.scene());
            }
            loginButton.setText(text);
        });
        loginAnimation.play();
    }

    @FXML
    protected void onRegisterButtonClicked(MouseEvent mouseEvent) throws IOException {
        stage.setScene(RegisterController.scene());
    }

    @FXML
    protected void onUsernameFieldKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            if (passwordField.getText().length() != 0)
                onLoginButtonClicked(null);
            else passwordField.requestFocus();
        }
    }

    @FXML
    protected void onPasswordFieldKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER)
            onLoginButtonClicked(null);
    }
}