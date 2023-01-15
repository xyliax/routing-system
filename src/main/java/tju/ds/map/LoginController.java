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
import lombok.SneakyThrows;
import tju.ds.map.dao.MongoController;
import tju.ds.map.model.User;
import tju.ds.map.model.UserType;

import static tju.ds.map.MapApplication.stage;

public class LoginController {
    static User user = null;
    private final MongoController mongoController = MongoController.getInstance();
    @FXML
    private ImageView loginLogo;
    @FXML
    private Button loginButton;
    @FXML
    private Button registerButton;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;

    @SneakyThrows
    public static Scene scene() {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginController.class.getResource("login.fxml"));
        return new Scene(fxmlLoader.load());
    }

    @FXML
    public void initialize() {
        registerButton.setOnMouseClicked(event -> stage.setScene(RegisterController.scene()));
        registerButton.setOnMouseEntered(event -> registerButton.setText("点我注册！"));
        registerButton.setOnMouseExited(event -> registerButton.setText("新用户？"));
        usernameField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                if (passwordField.getText().length() != 0)
                    onLoginButtonClicked();
                else passwordField.requestFocus();
            }
        });
        passwordField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER)
                onLoginButtonClicked();
        });
    }

    @FXML
    protected void onLoginButtonClicked() {
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
                passwordField.clear();
                passwordField.setPromptText("密码错误！！！");
                new Wobble(loginLogo).play();
            } else {
                LoginController.user = user;
                if (user.getType() == UserType.ADMIN)
                    stage.setScene(AdminController.scene());
                else stage.setScene(MapController.scene());
            }
            loginButton.setText(text);
            loginLogo.requestFocus();
        });
        loginAnimation.play();
    }
}