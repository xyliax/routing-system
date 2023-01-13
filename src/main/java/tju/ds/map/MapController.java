package tju.ds.map;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import lombok.SneakyThrows;
import tju.ds.map.dao.MongoController;
import tju.ds.map.model.Graph;
import tju.ds.map.model.User;
import tju.ds.map.model.Vertex;

import java.util.ArrayList;

public class MapController {
    public static User user;
    private final MongoController mongoController = MongoController.getInstance();
    @FXML
    private Label usernameLabel;
    @FXML
    private Canvas canvas;

    @SneakyThrows
    public static Scene scene() {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginController.class.getResource("map.fxml"));
        return new Scene(fxmlLoader.load());
    }

    @FXML
    public void initialize() {
        usernameLabel.setText(user.getUsername());
        Vertex v1 = new Vertex(null, "v1", 100, 200);
        Vertex v2 = new Vertex(null, "v2", 350, 400);
        Vertex v3 = new Vertex(null, "v3", 400, 700);
        Vertex v4 = new Vertex(null, "v4", 700, 300);
        Vertex v5 = new Vertex(null, "v5", 550, 150);
//        mongoController.insertVertex(v1);
//        mongoController.insertVertex(v2);
//        mongoController.insertVertex(v3);
//        mongoController.insertVertex(v4);
//        mongoController.insertVertex(v5);
        ArrayList<Vertex> vertexArrayList = mongoController.retrieveAllVertices();
        Graph graph = new Graph(vertexArrayList, new ArrayList<>());
        GraphicsContext context = canvas.getGraphicsContext2D();
        //context.strokeOval(v1.getX(), v1.getY(), 10, 10);
        Circle circle = new Circle(10, Color.DARKGREY);
        circle.setCenterX(v1.getX());
        circle.setCenterY(v1.getY());
        Group group = new Group(circle);
        context.strokeOval(500, 500, 10, 10);
    }
}
