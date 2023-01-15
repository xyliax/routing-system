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


/*
LoginController控制login.fxml页面，为登陆界面
能够实现登陆功能，判断用户是否存在、密码是否正确等功能
普通用户跳转至地图界面，管理员用户跳转至管理员界面，并能够进入注册界面
 */
public class LoginController {
    static User user = null;
    //获得实例
    private final MongoController mongoController = MongoController.getInstance();
    //fxml中需要控制的控件
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

    //连接login.fxml界面
    @SneakyThrows
    public static Scene scene() {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginController.class.getResource("login.fxml"));
        return new Scene(fxmlLoader.load());
    }

    //初始化页面。完成简单的控件事件绑定与属性设置
    @FXML
    public void initialize() {
        registerButton.requestFocus();
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

    //登陆，检验是否存在该用户，检验密码是否错误
    @FXML
    protected void onLoginButtonClicked() {
        String text = loginButton.getText();
        loginButton.setText("正在登录..");
        //登陆按钮的动画
        Animation loginAnimation = new FlipInX(loginButton).getTimeline();
        loginAnimation.setOnFinished(event -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            User user = mongoController.retrieveUser(username);
            if (user == null) {
                usernameField.clear();
                passwordField.clear();
                usernameField.setPromptText("找不到该用户！！！");
                new Wobble(loginLogo).play();//loginlogo动画
            } else if (!password.equals(user.getPassword())) {
                passwordField.clear();
                passwordField.setPromptText("密码错误！！！");
                new Wobble(loginLogo).play();//loginlogo动画
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