package tju.ds.map;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import tju.ds.map.dao.MongoController;


//程序的启动类

public final class MapApplication extends Application {
    public static Stage stage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        //创建初始窗口，设置不可放缩，初始界面为登陆界面
        primaryStage.requestFocus();
        primaryStage.setTitle("路面导航系统");
        Image logoImage = new Image("tju-logo.png");
        primaryStage.getIcons().add(logoImage);
        primaryStage.setScene(LoginController.scene());
        primaryStage.setResizable(false);
        primaryStage.show();
        stage = primaryStage;
    }

    @Override
    public void init() throws Exception {
        super.init();
        new Thread(() -> MongoController.getInstance().connect()).start();
    }
}