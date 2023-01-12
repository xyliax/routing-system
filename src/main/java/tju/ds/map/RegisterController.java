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
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import tju.ds.map.dao.MongoController;
import tju.ds.map.model.User;
import tju.ds.map.model.UserType;

import java.io.IOException;

import static tju.ds.map.MapApplication.stage;

public class RegisterController {
    private final MongoController mongoController = MongoController.getInstance();
    @FXML
    private Button registerButton;
    @FXML
    private Button backButton;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmField;
    @FXML
    private ImageView loginLogo;

    public static Scene scene() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginController.class.getResource("register.fxml"));
        return new Scene(fxmlLoader.load());
    }

    @FXML
    public void onBackButtonClicked(MouseEvent mouseEvent) throws IOException {
        stage.setScene(LoginController.scene());
    }

    @FXML
    public void onRegisterButtonClicked(MouseEvent mouseEvent) {
        String text = registerButton.getText();
        registerButton.setText("正在注册...");
        Animation registerAnimation = new FlipInX(registerButton).getTimeline();
        registerAnimation.setOnFinished(event -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            String confirm = confirmField.getText();
            if (username.length() < 1) {
                usernameField.clear();
                passwordField.clear();
                confirmField.clear();
                usernameField.setPromptText("用户名不能为空！");
                new Wobble(loginLogo).play();
            } else if (!password.equals(confirm)) {
                confirmField.clear();
                usernameField.setPromptText("请输入相同密码！");
                new Wobble(loginLogo).play();
            } else if (mongoController.retrieveUser(username) != null) {
                usernameField.clear();
                passwordField.clear();
                confirmField.clear();
                usernameField.setPromptText("该用户已存在！！！");
                new Wobble(loginLogo).play();
            } else {
                User newUser = new User(null, username, password, UserType.NORMAL, null, null);
                mongoController.insertUser(newUser);
            }
            registerButton.setText(text);
        });
        registerAnimation.play();
    }

    @FXML
    protected void onUsernameFieldKeyPressed(KeyEvent keyEvent) {
    }

    @FXML
    protected void onPasswordFieldKeyPressed(KeyEvent keyEvent) {
    }

}
