package tju.ds.map;

import animatefx.animation.FlipInX;
import animatefx.animation.Wobble;
import javafx.animation.Animation;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import lombok.SneakyThrows;
import tju.ds.map.dao.MongoController;
import tju.ds.map.model.User;
import tju.ds.map.model.UserType;

import static tju.ds.map.MapApplication.stage;


/*
RegisterController控制register.fxml页面，为注册界面
能够实现注册新用户功能，包括用户名、密码、确认密码、邮箱、手机、个人简介等信息的输入保存功能
用户名不能与现有用户一致，密码必须大于三位，不能注册空用户
能够返回至登陆界面
 */
public class RegisterController {
   //fxml中需要控制的控件
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
    private TextField mobileField;
    @FXML
    private TextField emailField;
    @FXML
    private TextArea bioArea;
    @FXML
    private ImageView loginLogo;

    //连接register.fxml界面
    @SneakyThrows
    public static Scene scene() {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginController.class.getResource("register.fxml"));
        return new Scene(fxmlLoader.load());
    }


    //初始化页面。完成简单的控件事件绑定与属性设置
    @FXML
    public void initialize() {
        backButton.setOnMouseClicked(event -> stage.setScene(LoginController.scene()));
        usernameField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                if (passwordField.getText().length() != 0)
                    onRegisterButtonClicked();
                else passwordField.requestFocus();
            }
        });
        passwordField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER)
                confirmField.requestFocus();
        });
        confirmField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER)
                onRegisterButtonClicked();
        });
    }

    @FXML
    public void onRegisterButtonClicked() {
        String text = registerButton.getText();
        registerButton.setText("正在注册...");
        //注册按钮的动画
        Animation registerAnimation = new FlipInX(registerButton).getTimeline();
        registerAnimation.setOnFinished(event -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            String confirm = confirmField.getText();
            String mobile = mobileField.getText();
            String email = emailField.getText();
            String bio = bioArea.getText();
            if (username.length() < 1) {
                usernameField.clear();
                passwordField.clear();
                confirmField.clear();
                usernameField.setPromptText("用户名不能为空！");
                new Wobble(loginLogo).play();//logo动画
            } else if (password.length() < 3) {
                passwordField.clear();
                confirmField.clear();
                usernameField.setPromptText("密码至少为3个字符！");
                new Wobble(loginLogo).play();//logo动画
            } else if (!password.equals(confirm)) {
                confirmField.clear();
                confirmField.setPromptText("请输入相同密码！");
                new Wobble(loginLogo).play();//logo动画
            } else if (mongoController.retrieveUser(username) != null) {
                usernameField.clear();
                passwordField.clear();
                confirmField.clear();
                usernameField.setPromptText("该用户已存在！！！");
                new Wobble(loginLogo).play();
            } else {
                User newUser = new User(null, username, password, UserType.NORMAL, mobile, email, bio);
                mongoController.insertUser(newUser);
                backButton.setText("注册成功！点我去登录");
            }
            registerButton.setText(text);
            loginLogo.requestFocus();
        });
        registerAnimation.play();
    }
}
