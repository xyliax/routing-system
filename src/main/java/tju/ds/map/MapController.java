package tju.ds.map;

import animatefx.animation.Pulse;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import lombok.SneakyThrows;
import tju.ds.map.dao.MongoController;
import tju.ds.map.model.Edge;
import tju.ds.map.model.Graph;
import tju.ds.map.model.UserType;
import tju.ds.map.model.Vertex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import static tju.ds.map.MapApplication.stage;

public class MapController {
    private final MongoController mongoController = MongoController.getInstance();
    private HashMap<String, Polygon> edgeIdShapeMap;
    private HashMap<String, Circle> vertexIdShapeMap;
    @FXML
    private Label usernameLabel;
    @FXML
    private AnchorPane mapPane;
    @FXML
    private ImageView refreshLogo;

    @SneakyThrows
    public static Scene scene() {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginController.class.getResource("map.fxml"));
        return new Scene(fxmlLoader.load());
    }

    @FXML
    public void initialize() {
        usernameLabel.setText(LoginController.user.getUsername());
        Vertex v1 = new Vertex(null, "v1", 100, 200);
        Vertex v2 = new Vertex(null, "v2", 350, 400);
        Vertex v3 = new Vertex(null, "v3", 400, 700);
        Vertex v4 = new Vertex(null, "v4", 700, 300);
//        Vertex v5 = new Vertex(null, "v5", 550, 150);
//        Edge edge = new Edge(null, "e1", "63bffdb4778efe71ee796cc6", "63bffdb4778efe71ee796cc7", 100,
//                60, EdgeCondition.WELL);
//        mongoController.insertEdge(edge);
        ArrayList<Vertex> vertexArrayList = mongoController.retrieveAllVertices();
        ArrayList<Edge> edgeArrayList = mongoController.retrieveAllEdges();
        Graph graph = new Graph(vertexArrayList, edgeArrayList);
        refreshLogo.setOnMouseClicked(event -> {
            if (LoginController.user.getType() == UserType.ADMIN)
                stage.setScene(AdminController.scene());
            else this.initialize();
        });
        refreshLogo.setCursor(Cursor.HAND);
        refreshLogo.setOnMouseEntered(event -> new Pulse(refreshLogo).play());
        mapPane.getChildren().clear();
        mapPane.getChildren().add(refreshLogo);
        render(graph);
    }

    private void render(Graph graph) {
        edgeIdShapeMap = new HashMap<>();
        vertexIdShapeMap = new HashMap<>();
        for (Vertex vertex : graph.getGraph().keySet()) {
            Circle vertexShape = new Circle(vertex.getX(), vertex.getY(), 6, Color.NAVY);
            Text vertexText = new Text(vertex.getX() + 6, vertex.getY() + 6, vertex.toString());
            mapPane.getChildren().addAll(vertexShape, vertexText);
            vertexIdShapeMap.put(vertex.getId(), vertexShape);
        }
        HashSet<Edge> paintedEdge = new HashSet<>();
        for (Edge edge : graph.getEdges().values()) {
            Vertex u = graph.getVertices().get(edge.getUId());
            Vertex v = graph.getVertices().get(edge.getVId());
            if (paintedEdge.contains(edge)) continue;
            paintedEdge.add(edge);
            Double[] points = new Double[]{
                    (double) u.getX(), (double) (u.getY() - 4),
                    (double) u.getX(), (double) (u.getY() + 4),
                    (double) v.getX(), (double) (v.getY() + 4),
                    (double) v.getX(), (double) (v.getY() - 4)
            };
            Polygon edgeShape = new Polygon();
            edgeShape.getPoints().addAll(points);
            edgeShape.setFill(Color.DARKGRAY);
            Circle edgeMid = new Circle(
                    (u.getX() + v.getX()) / 2.0,
                    (u.getY() + v.getY()) / 2.0,
                    4, Color.BLACK);
            Text edgeText = new Text(
                    (u.getX() + v.getX()) / 2.0 + 5, (u.getY() + v.getY()) / 2.0 + 5,
                    edge.getName());
            edgeShape.setOnMouseEntered(event -> edgeText.setText(edge.toString()));
            edgeShape.setOnMouseExited(event -> edgeText.setText(edge.getName()));
            edgeMid.setOnMouseEntered(event -> edgeText.setText(edge.toString()));
            edgeMid.setOnMouseExited(event -> edgeText.setText(edge.getName()));
            mapPane.getChildren().addAll(edgeShape, edgeMid, edgeText);
            edgeIdShapeMap.put(edge.getId(), edgeShape);
        }
        edgeIdShapeMap.values().forEach(Node::toBack);
    }

    private ArrayList<Edge> findNearestDistance(Vertex start, Vertex stop) {
        ArrayList<Edge> result = new ArrayList<>();
        return result;
    }

    private ArrayList<Edge> findShortestTime(Vertex start, Vertex stop) {
        ArrayList<Edge> result = new ArrayList<>();
        return result;
    }
}
