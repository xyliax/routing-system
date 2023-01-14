package tju.ds.map;

import animatefx.animation.FlipInX;
import javafx.animation.Animation;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import lombok.SneakyThrows;
import tju.ds.map.dao.MongoController;
import tju.ds.map.model.*;

import java.util.ArrayList;

import static tju.ds.map.MapApplication.stage;

public class AdminController {
    private final MongoController mongoController = MongoController.getInstance();
    private Graph graph;
    private User user;
    private Vertex vertex;
    private Edge edge;
    private RadioButton radioButtonSelected = null;
    @FXML
    private TextField usernameField1;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField mobileField;
    @FXML
    private TextField emailField;
    @FXML
    private CheckBox adminButton;
    @FXML
    private Button userSaveButton;
    @FXML
    private Button backButton;
    @FXML
    private Button mapButton;
    @FXML
    private Button userDeleteButton;
    @FXML
    private TextField vertexField;
    @FXML
    private TextField x_vertexField;
    @FXML
    private TextField y_vertexField;
    @FXML
    private Button vertexDeleteButton;
    @FXML
    private Button vertexSaveButton;
    @FXML
    private TextField edgeField;
    @FXML
    private TextField u_edgeField;
    @FXML
    private TextField v_edgeField;
    @FXML
    private TextField distanceField;
    @FXML
    private TextField limitField;
    @FXML
    private Button edgeDeleteButton;
    @FXML
    private Button edgeSaveButton;
    @FXML
    private RadioButton radioButton1;
    @FXML
    private RadioButton radioButton2;
    @FXML
    private RadioButton radioButton3;
    @FXML
    private RadioButton radioButton4;
    @FXML
    private RadioButton radioButton5;

    @SneakyThrows
    public static Scene scene() {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginController.class.getResource("admin.fxml"));
        return new Scene(fxmlLoader.load());
    }

    @FXML
    public void initialize() {
        refreshGraph();
        backButton.setOnMouseClicked(event -> stage.setScene(LoginController.scene()));
        mapButton.setOnMouseClicked(event -> stage.setScene(MapController.scene()));
        userDeleteButton.setOnMouseClicked(event -> {
            Animation loginAnimation = new FlipInX(userDeleteButton).getTimeline();
            loginAnimation.setOnFinished(actionEvent -> mongoController.deleteUser(user));
            loginAnimation.play();
            userDeleteButton.setText("已删除");
        });
        vertexDeleteButton.setOnMouseClicked(event -> {
            Animation loginAnimation = new FlipInX(vertexDeleteButton).getTimeline();
            loginAnimation.setOnFinished(actionEvent -> mongoController.deleteVertex(vertex));
            loginAnimation.play();
            vertexDeleteButton.setText("已删除");
        });
        edgeDeleteButton.setOnMouseClicked(event -> {
            Animation loginAnimation = new FlipInX(edgeDeleteButton).getTimeline();
            loginAnimation.setOnFinished(actionEvent -> mongoController.deleteEdge(edge));
            loginAnimation.play();
            edgeDeleteButton.setText("已删除");
        });
        EventHandler<ActionEvent> radioHandler = event -> {
            if (radioButtonSelected != null)
                radioButtonSelected.setSelected(false);
            if (((RadioButton) event.getSource()).isSelected())
                radioButtonSelected = (RadioButton) event.getSource();
            else radioButtonSelected = null;
        };
        radioButton1.setOnAction(radioHandler);
        radioButton2.setOnAction(radioHandler);
        radioButton3.setOnAction(radioHandler);
        radioButton4.setOnAction(radioHandler);
        radioButton5.setOnAction(radioHandler);
    }

    private void refreshGraph() {
        ArrayList<Vertex> vertexArrayList = mongoController.retrieveAllVertices();
        ArrayList<Edge> edgeArrayList = mongoController.retrieveAllEdges();
        this.graph = new Graph(vertexArrayList, edgeArrayList);
    }

    @FXML
    public void userEnter(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            user = mongoController.retrieveUser(usernameField1.getText());
            if (user == null) {
                usernameField1.clear();
                passwordField.clear();
                usernameField1.setPromptText("找不到该用户！！！");
                backButton.requestFocus();
                adminButton.setSelected(false);
            } else {
                passwordField.setText(user.getPassword());
                mobileField.setText(user.getMobile());
                emailField.setText(user.getEmail());
                adminButton.setSelected(user.getType() == UserType.ADMIN);
            }
        }
    }

    @FXML
    protected void OnUserSaveButtonClicked(MouseEvent mouseEvent) {
        Animation loginAnimation = new FlipInX(userSaveButton).getTimeline();
        loginAnimation.setOnFinished(event -> {
            if (user != null) {
                user.setUsername(usernameField1.getText());
                user.setPassword(passwordField.getText());
                user.setEmail(emailField.getText());
                user.setMobile(mobileField.getText());
                if (adminButton.isSelected()) {
                    user.setType(UserType.ADMIN);
                } else {
                    user.setType(UserType.NORMAL);
                }
                mongoController.updateUser(user);
                userSaveButton.setText("保存成功!");
            } else {
                User newUser = new User(null, usernameField1.getText(), passwordField.getText(), UserType.NORMAL, mobileField.getText(), emailField.getText(), null);
                if (adminButton.isSelected()) {
                    newUser.setType(UserType.ADMIN);
                } else {
                    newUser.setType(UserType.NORMAL);
                }
                mongoController.insertUser(newUser);
                userSaveButton.setText("新建成功!");
            }
        });
        loginAnimation.play();
    }

    @FXML
    public void vertexEnter(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            vertex = mongoController.retrieveVertex(vertexField.getText());
            if (vertex == null) {
                vertexField.clear();
                x_vertexField.clear();
                y_vertexField.clear();
                vertexField.setPromptText("不存在该节点！！！");
                backButton.requestFocus();
            } else {
                x_vertexField.setText("" + vertex.getX());
                y_vertexField.setText("" + vertex.getY());
            }
        }
    }

    @FXML
    public void OnVertexSaveButtonClicked(MouseEvent mouseEvent) {
        Animation loginAnimation = new FlipInX(vertexSaveButton).getTimeline();
        loginAnimation.setOnFinished(event -> {
            if (vertex != null) {
                vertex.setName(vertexField.getText());
                vertex.setX(Integer.parseInt(x_vertexField.getText()));
                vertex.setY(Integer.parseInt(y_vertexField.getText()));
                mongoController.updateVertex(vertex);
                vertexSaveButton.setText("保存成功!");
            } else {
                Vertex newVertex = new Vertex(null, vertexField.getText(), Integer.parseInt(x_vertexField.getText()), Integer.parseInt(y_vertexField.getText()));
                mongoController.insertVertex(newVertex);
                vertexSaveButton.setText("新建成功!");
            }
        });
        loginAnimation.play();
    }

    @FXML
    public void edgeEnter(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            edge = mongoController.retrieveEdge(edgeField.getText());
            if (edge == null) {
                edgeField.clear();
                u_edgeField.clear();
                v_edgeField.clear();
                distanceField.clear();
                limitField.clear();
                edgeField.setPromptText("不存在这条路！！！");
                backButton.requestFocus();
            } else {
                u_edgeField.setText(graph.getVertices().get(edge.getUId()).getName());
                v_edgeField.setText(graph.getVertices().get(edge.getVId()).getName());
                distanceField.setText("" + edge.getDistance());
                limitField.setText("" + edge.getLimit());
                //道路情况
            }
        }
    }

    @FXML
    public void OnEdgeSaveButtonClicked(MouseEvent mouseEvent) {
        Animation loginAnimation = new FlipInX(edgeSaveButton).getTimeline();
        loginAnimation.setOnFinished(event -> {
            if (edge != null) {
                edge.setName(edgeField.getText());
                edge.setUId(u_edgeField.getText());
                edge.setVId(v_edgeField.getText());
                edge.setDistance(Double.parseDouble(distanceField.getText()));
                edge.setLimit(Double.parseDouble(limitField.getText()));//状况!!
                mongoController.updateEdge(edge);
                edgeSaveButton.setText("保存成功!");
            } else {
                Edge newEdge = new Edge(null, edgeField.getText(), u_edgeField.getText(), v_edgeField.getText(), Double.parseDouble(distanceField.getText()), Double.parseDouble(limitField.getText()), EdgeCondition.UNKNOWN);
                mongoController.insertEdge(newEdge);
                edgeSaveButton.setText("新建成功!");
            }
        });
        loginAnimation.play();
    }
}