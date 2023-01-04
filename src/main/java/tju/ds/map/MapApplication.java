package tju.ds.map;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class MapApplication extends Application {
    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle("路面导航系统");
        Image logoImage = new Image("tju-logo.png");
        primaryStage.getIcons().add(logoImage);
        primaryStage.show();
        new LoginController().show(primaryStage);
    }

    @Override
    public void init() throws Exception {
        super.init();
        System.out.println(this + "init()...");
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        System.out.println(this + "stop()...");
    }
}