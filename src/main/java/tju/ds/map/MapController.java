package tju.ds.map;

import animatefx.animation.FlipInX;
import animatefx.animation.FlipInY;
import animatefx.animation.Pulse;
import javafx.animation.Animation;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lombok.SneakyThrows;
import tju.ds.map.dao.MongoController;
import tju.ds.map.model.Edge;
import tju.ds.map.model.Graph;
import tju.ds.map.model.UserType;
import tju.ds.map.model.Vertex;

import java.util.*;

import static tju.ds.map.LoginController.user;
import static tju.ds.map.MapApplication.stage;


/*
MapController控制map.fxml页面，为地图导航界面
能够实现显示地图，显示、修改保存用户信息，输入导航起始点和终点、指定寻路方式（最快/最短），并在地图上显示寻路结果等功能
寻路结束后能够导出生成文字寻路结果报告
 */

public class MapController {
    //获取实例
    private final MongoController mongoController = MongoController.getInstance();
    //道路id、道路信息映射到对应道路的哈希表
    private final HashMap<String, Polygon> edgeIdShapeMap = new HashMap<>();
    private final HashMap<String, Text> edgeIdTextMap = new HashMap<>();
    //节点id、节点信息映射到对应道路的哈希表
    private final HashMap<String, Circle> vertexIdShapeMap = new HashMap<>();
    private final HashMap<String, Text> vertexIdTextMap = new HashMap<>();
    private Graph formedGraph;
    //fxml中需要控制的控件
    @FXML
    private Label usernameLabel;
    @FXML
    private TextField mobileField;
    @FXML
    private TextField emailField;
    @FXML
    private TextArea bioArea;
    @FXML
    private Button confirmButton;
    @FXML
    private Button verboseButton;
    @FXML
    private Button verboseAllButton;
    @FXML
    private TextField startField;
    @FXML
    private TextField stopField;
    @FXML
    private CheckBox checkbox1;
    @FXML
    private CheckBox checkbox2;
    @FXML
    private Button searchButton;
    @FXML
    private Button saveButton;
    @FXML
    private AnchorPane mapPane;
    @FXML
    private ImageView refreshLogo;
    @FXML
    private ImageView mapLogo;

    //连接map.fxml界面
    @SneakyThrows
    public static Scene scene() {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginController.class.getResource("map.fxml"));
        return new Scene(fxmlLoader.load());
    }

    //初始化页面。完成简单的控件事件绑定与属性设置
    @FXML
    public void initialize() {
        mapLogo.setOnMouseClicked(event -> stage.setScene(LoginController.scene()));
        usernameLabel.setText(user.getUsername());
        mobileField.setText(user.getMobile());
        emailField.setText(user.getEmail());
        bioArea.setText(user.getBio());
        confirmButton.setOnMouseClicked(event -> {
            confirmButton.setText("正在保存...");
            Animation loginAnimation = new FlipInX(confirmButton).getTimeline();
            loginAnimation.setOnFinished(actionEvent -> {
                user.setMobile(mobileField.getText());
                user.setEmail(emailField.getText());
                user.setBio(bioArea.getText());
                mongoController.updateUser(user);
            });
            loginAnimation.play();
            confirmButton.setText("点我保存");
        });
        refreshLogo.setOnMouseClicked(event -> {
            if (user.getType() == UserType.ADMIN)
                stage.setScene(AdminController.scene());
            else render();
        });
        refreshLogo.setOnMouseEntered(event -> new Pulse(refreshLogo).play());
        render();
        verboseButton.setOnMouseClicked(event -> {
            if (verboseButton.getText().equals("显示路径详细信息")) {
                for (String eId : edgeIdTextMap.keySet())
                    edgeIdTextMap.get(eId).setText(formedGraph.getEdges().get(eId).toString());
                for (String vId : vertexIdTextMap.keySet())
                    vertexIdTextMap.get(vId).setText("");
                verboseButton.setText("显示节点详细信息");
            } else {
                for (String eId : edgeIdTextMap.keySet())
                    edgeIdTextMap.get(eId).setText("");
                for (String vId : vertexIdTextMap.keySet())
                    vertexIdTextMap.get(vId).setText(formedGraph.getVertices().get(vId).toString());
                verboseButton.setText("显示路径详细信息");
            }
        });
        verboseAllButton.setText("显示所有");
        verboseAllButton.setOnMouseClicked(event -> {
            if (verboseAllButton.getText().equals("显示所有")) {
                for (String eId : edgeIdTextMap.keySet())
                    edgeIdTextMap.get(eId).setText(formedGraph.getEdges().get(eId).toString());
                for (String vId : vertexIdTextMap.keySet())
                    vertexIdTextMap.get(vId).setText(formedGraph.getVertices().get(vId).toString());
                verboseAllButton.setText("隐藏所有");
            } else {
                for (String eId : edgeIdTextMap.keySet())
                    edgeIdTextMap.get(eId).setText("");
                for (String vId : vertexIdTextMap.keySet())
                    vertexIdTextMap.get(vId).setText("");
                verboseAllButton.setText("显示所有");
            }
        });
        startField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER)
                stopField.requestFocus();
        });
        stopField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER)
                search();
        });
        checkbox1.setSelected(true);
        checkbox1.setOnMouseClicked(event -> checkbox2.setSelected(!checkbox2.isSelected()));
        checkbox2.setSelected(false);
        checkbox2.setOnMouseClicked(event -> checkbox1.setSelected(!checkbox1.isSelected()));
        Platform.runLater(() -> startField.requestFocus());
    }

    // 当用户完成起点、终点、搜索方式的输入后，调用search方法，
    // 再由用户输入决定所使用的寻路算法，并在算法返回后提供生成详细文字报告的选项
    @FXML
    protected void search() {
        searchButton.setText("正在搜索");
        Animation searchAnimation = new FlipInY(searchButton).getTimeline();
        searchAnimation.setOnFinished(event -> {
            Vertex start = mongoController.retrieveVertex(startField.getText());
            Vertex stop = mongoController.retrieveVertex(stopField.getText());
            if (start == null) {
                startField.setPromptText("找不到" + startField.getText());
                startField.clear();
                searchButton.requestFocus();
                return;
            }
            if (stop == null) {
                stopField.setPromptText("找不到" + stopField.getText());
                stopField.clear();
                searchButton.requestFocus();
                return;
            }
            if (start == stop) {
                stopField.setPromptText("起点和终点不能相同！");
                stopField.clear();
                searchButton.requestFocus();
            }
            searchButton.setText("开始搜寻");
            ArrayList<Edge> path;
            if (checkbox1.isSelected())
                path = findNearestDistance(start, stop);
            else path = findShortestTime(start, stop);
            render();
            vertexIdShapeMap.get(start.getId()).setFill(Color.YELLOW);
            vertexIdShapeMap.get(stop.getId()).setFill(Color.RED);
            path.forEach(edge -> edgeIdShapeMap.get(edge.getId()).setFill(Color.GREEN));
            saveButton.setDisable(false);
            saveButton.setOnMouseClicked(mouseEvent -> {
                Stage newStage = new Stage();
                HBox hBox = new HBox();
                Scene resScene = new Scene(hBox);
                TextArea textArea = new TextArea();
                ImageView imageView = new ImageView(mapPane.snapshot(new SnapshotParameters(), null));
                hBox.getChildren().add(imageView);
                hBox.getChildren().add(textArea);
                newStage.setScene(resScene);
                StringBuilder text = new StringBuilder(String.format("""
                        寻路报告 %s
                        起点: %s
                        终点: %s%n""", new Date(), start, stop));
                double totalDistance = 0;
                double totalTimeCost = 0;
                for (int i = 0; i < path.size(); i++) {
                    Edge edge = path.get(i);
                    text.append(String.format("第%s步: %s路 全长%.1f公里 [路况%s, 限速%.1f] 预计耗时%.1f小时 %n", i + 1,
                            edge.getName(), edge.getDistance(), edge.getCondition().info, edge.getLimit(),
                            edge.timeCost()));
                    totalDistance += edge.getDistance();
                    totalTimeCost += edge.timeCost();
                }
                text.append(String.format("总路长为%.1f公里, 预计耗时%.1f小时%n", totalDistance, totalTimeCost));
                textArea.setText(text.toString());
                textArea.setStyle("-fx-font-size: 16;-fx-font-family: 'Microsoft Sans Serif'");
                newStage.setTitle("寻路结果显示");
                newStage.show();
            });
        });
        searchAnimation.play();
    }

    /*
    用迪杰斯特拉算法，求单源最短路
    本方法采用最短距离，算法详情见报告
 */
    private ArrayList<Edge> findNearestDistance(Vertex start, Vertex stop) {
        ArrayList<Edge> result = new ArrayList<>(); //所求的最短路
        HashMap<Vertex, Double> distance = new HashMap<>(); //维护局部最短距离
        HashMap<Vertex, Vertex> precursor = new HashMap<>(); //维护当前前驱节点
        HashSet<Vertex> visited = new HashSet<>(); //当前以访问节点
        //优先队列，将节点按distance排序
        PriorityQueue<Vertex> priorityQueue = new PriorityQueue<>(Comparator.comparing(distance::get));
        formedGraph.getVertices().values().forEach(vertex -> distance.put(vertex, Double.POSITIVE_INFINITY));
        distance.put(start, 0.0);
        priorityQueue.add(start);
        while (!priorityQueue.isEmpty()) {
            Vertex current = priorityQueue.poll();
            if (visited.contains(current)) continue;
            visited.add(current);
            HashMap<String, Edge> currentEdges = formedGraph.getGraph().get(current);
            for (String nextId : currentEdges.keySet()) {
                Vertex next = formedGraph.getVertices().get(nextId);
                Edge toNext = currentEdges.get(nextId);
                if (distance.get(current) + toNext.getDistance() < distance.get(next)) {
                    distance.put(next, distance.get(current) + toNext.getDistance());
                    precursor.put(next, current);
                    priorityQueue.add(next);
                }
            }
        }
        Vertex next = stop;
        Vertex pre = precursor.get(stop);
        while (next != start) {
            result.add(formedGraph.getGraph().get(pre).get(next.getId()));
            next = pre;
            pre = precursor.get(next);
        }
        Collections.reverse(result);
        return result;
    }

    /*
用迪杰斯特拉算法，求单源时间最短路
本方法采用最快时间，算法详情见报告
*/
    private ArrayList<Edge> findShortestTime(Vertex start, Vertex stop) {
        ArrayList<Edge> result = new ArrayList<>(); //所求的最快路
        HashMap<Vertex, Double> time = new HashMap<>(); //维护局部最快时间
        HashMap<Vertex, Vertex> precursor = new HashMap<>(); //维护当前的前驱节点
        HashSet<Vertex> visited = new HashSet<>(); //当前以访问的节点
        //优先队列，将节点按time排序
        PriorityQueue<Vertex> priorityQueue = new PriorityQueue<>(Comparator.comparing(time::get));
        formedGraph.getVertices().values().forEach(vertex -> time.put(vertex, Double.POSITIVE_INFINITY));
        time.put(start, 0.0);
        priorityQueue.add(start);
        while (!priorityQueue.isEmpty()) {
            Vertex current = priorityQueue.poll();
            if (visited.contains(current)) continue;
            visited.add(current);
            HashMap<String, Edge> currentEdges = formedGraph.getGraph().get(current);
            for (String nextId : currentEdges.keySet()) {
                Vertex next = formedGraph.getVertices().get(nextId);
                Edge toNext = currentEdges.get(nextId);
                if (time.get(current) + toNext.timeCost() < time.get(next)) {
                    time.put(next, time.get(current) + toNext.timeCost());
                    precursor.put(next, current);
                    priorityQueue.add(next);
                }
            }
        }
        Vertex next = stop;
        Vertex pre = precursor.get(stop);
        while (next != start) {
            result.add(formedGraph.getGraph().get(pre).get(next.getId()));
            next = pre;
            pre = precursor.get(next);
        }
        Collections.reverse(result);
        return result;
    }
    /*
   由内存中的graph数据生成javafx组件，在窗口中形成可视化结构图
   调用一次需要重新访问数据库
 */
    @FXML
    private void render() {
        mapPane.getChildren().clear();
        mapPane.getChildren().add(refreshLogo);
        ArrayList<Vertex> vertexArrayList = mongoController.retrieveAllVertices();
        ArrayList<Edge> edgeArrayList = mongoController.retrieveAllEdges();
        formedGraph = new Graph(vertexArrayList, edgeArrayList);
        edgeIdShapeMap.clear();
        edgeIdTextMap.clear();
        vertexIdShapeMap.clear();
        vertexIdTextMap.clear();
        for (Vertex vertex : formedGraph.getGraph().keySet()) {
            Circle vertexShape = new Circle(vertex.getX(), vertex.getY(), 6, Color.NAVY);
            Text vertexText = new Text(vertex.getX() + 6, vertex.getY() + 6, vertex.toString());
            vertexText.setFill(Color.GREEN);
            mapPane.getChildren().addAll(vertexShape, vertexText);
            //将vertex和vertex所对应的图形绑定
            vertexIdShapeMap.put(vertex.getId(), vertexShape);
            vertexIdTextMap.put(vertex.getId(), vertexText);
        }
        //为了避免重复渲染，重复边只会显示一次
        HashSet<Edge> paintedEdge = new HashSet<>();
        for (Edge edge : formedGraph.getEdges().values()) {
            Vertex u = formedGraph.getVertices().get(edge.getUId());
            Vertex v = formedGraph.getVertices().get(edge.getVId());
            if (paintedEdge.contains(edge)) continue;
            paintedEdge.add(edge);
            double dx = Math.sqrt(9 / (Math.pow(((double) (u.getX() - v.getX())) / (u.getY() - v.getY()), 2) + 1));
            double dy = ((double) (u.getX() - v.getX())) / (u.getY() - v.getY()) * dx;
            dx = Math.abs(dx);
            dy = Math.abs(dy);
            if (dx * (u.getY() - v.getY()) == dy * (u.getX() - v.getX())) dx = -dx;
            if (Double.isNaN(dy)) {
                dy = 3;
                dx = 3;
            }
            Double[] points = new Double[]{
                    (double) u.getX() - dx, (double) u.getY() - dy,
                    (double) u.getX() + dx, (double) u.getY() + dy,
                    (double) v.getX() + dx, (double) v.getY() + dy,
                    (double) v.getX() - dx, (double) v.getY() - dy
            };
            Polygon edgeShape = new Polygon();
            edgeShape.getPoints().addAll(points);
            edgeShape.setFill(Color.DARKGRAY);
            Circle edgeMid = new Circle((u.getX() + v.getX()) / 2.0,
                    (u.getY() + v.getY()) / 2.0, 3, Color.BLACK);
            Text edgeText = new Text((u.getX() + v.getX()) / 2.0 + 5, (u.getY() + v.getY()) / 2.0 + 5, "");
            edgeText.setFill(Color.GREEN);
            edgeShape.setOnMouseEntered(event -> edgeText.setText(edge.toString()));
            edgeShape.setOnMouseExited(event -> edgeText.setText(""));
            edgeMid.setOnMouseEntered(event -> edgeText.setText(edge.toString()));
            edgeMid.setOnMouseExited(event -> edgeText.setText(""));
            mapPane.getChildren().addAll(edgeShape, edgeMid, edgeText);
            //将edge和edge所代表的图形绑定
            edgeIdShapeMap.put(edge.getId(), edgeShape);
            edgeIdTextMap.put(edge.getId(), edgeText);
        }
        edgeIdShapeMap.values().forEach(Node::toBack);
    }
}
