package tju.ds.map;

import animatefx.animation.FlipInX;
import javafx.animation.Animation;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import lombok.SneakyThrows;
import tju.ds.map.dao.MongoController;
import tju.ds.map.model.*;

import java.util.ArrayList;

import static tju.ds.map.MapApplication.stage;

/*
AdminController控制admin.fxml页面，为管理员界面
能够实现对用户、节点、道路的查找、新建、删除、修改等功能
若输入用户、节点、道路名称为新名称，则新建，若已存在，则保存修改。
 */
public class AdminController {
    private final MongoController mongoController = MongoController.getInstance();
    private Graph graph;
    private User user;
    private Vertex vertex;
    private Edge edge;
    private RadioButton radioButtonSelected = null;
    //fxml中需要控制的控件
    @FXML
    private TextField usernameField1;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField mobileField;
    @FXML
    private TextField emailField;
    @FXML
    private TextArea bioArea;
    @FXML
    private CheckBox adminButton;
    @FXML
    private Button userSaveButton;
    @FXML
    private Button userSearchButton;
    @FXML
    private Button vertexSearchButton;
    @FXML
    private Button edgeSearchButton;
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

    //连接admin.fxml界面
    @SneakyThrows
    public static Scene scene() {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginController.class.getResource("admin.fxml"));
        return new Scene(fxmlLoader.load());
    }

    //初始化页面。完成简单的控件事件绑定与属性设置
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
        //5个radioButton只能选中一个
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
        userSearchButton.setOnMouseClicked(event -> userSearch());
        vertexSearchButton.setOnMouseClicked(event -> vertexSearch());
        edgeSearchButton.setOnMouseClicked(event -> edgeSearch());
    }

    //刷新地图
    private void refreshGraph() {
        ArrayList<Vertex> vertexArrayList = mongoController.retrieveAllVertices();
        ArrayList<Edge> edgeArrayList = mongoController.retrieveAllEdges();
        this.graph = new Graph(vertexArrayList, edgeArrayList);
    }

   //在用户名输入框键入回车键，实现用户搜索功能
    @FXML
    protected void userEnter(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER)
            userSearch();
    }

    @FXML
    private void userSearch() {
        user = mongoController.retrieveUser(usernameField1.getText());
        if (user == null) {
            usernameField1.clear();
            passwordField.clear();
            mobileField.clear();
            emailField.clear();
            bioArea.clear();
            usernameField1.setPromptText("找不到该用户！！！");
            backButton.requestFocus();
            adminButton.setSelected(false);
        } else {
            passwordField.setText(user.getPassword());
            mobileField.setText(user.getMobile());
            emailField.setText(user.getEmail());
            bioArea.setText(user.getBio());
            adminButton.setSelected(user.getType() == UserType.ADMIN);
        }
    }


    //用户保存，若存在该用户则保存修改，若不存在用户则新建用户保存
    @FXML
    protected void onUserSaveButtonClicked() {
        Animation loginAnimation = new FlipInX(userSaveButton).getTimeline();
        loginAnimation.setOnFinished(event -> {
            if (user != null) {
                user.setUsername(usernameField1.getText());
                user.setPassword(passwordField.getText());
                user.setEmail(emailField.getText());
                user.setMobile(mobileField.getText());
                user.setBio(bioArea.getText());
                if (adminButton.isSelected())
                    user.setType(UserType.ADMIN);
                else user.setType(UserType.NORMAL);
                mongoController.updateUser(user);
                userSaveButton.setText("保存成功!");
            } else {
                User newUser = new User(null, usernameField1.getText(), passwordField.getText(), UserType.NORMAL, mobileField.getText(), emailField.getText(), bioArea.getText());
                if (adminButton.isSelected())
                    newUser.setType(UserType.ADMIN);
                else newUser.setType(UserType.NORMAL);
                mongoController.insertUser(newUser);
                userSaveButton.setText("新建成功!");
            }
        });
        loginAnimation.play();
    }

    //在定点名输入框键入回车键，实现定点搜索功能
    @FXML
    public void vertexEnter(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER)
            vertexSearch();
    }

    @FXML
    private void vertexSearch() {
        vertex = mongoController.retrieveVertex(vertexField.getText());
        if (vertex == null) {
            vertexField.clear();
            x_vertexField.clear();
            y_vertexField.clear();
            vertexField.setPromptText("不存在该节点！！！");
            backButton.requestFocus();
        } else {
            x_vertexField.setText(String.valueOf(vertex.getX()));
            y_vertexField.setText(String.valueOf(vertex.getY()));
        }
    }

    //节点保存，若存在该节点则保存修改，若不存在节点则新建节点保存
    @FXML
    protected void onVertexSaveButtonClicked() {
        Animation loginAnimation = new FlipInX(vertexSaveButton).getTimeline();
        loginAnimation.setOnFinished(event -> {
            String name = vertexField.getText();
            try {
                int x = Integer.parseInt(x_vertexField.getText());
                int y = Integer.parseInt(y_vertexField.getText());
                vertex = mongoController.retrieveVertex(name);
                if (vertex != null) {
                    vertex.setName(vertexField.getText());
                    vertex.setX(x);
                    vertex.setY(y);
                    mongoController.updateVertex(vertex);
                    vertexSaveButton.setText("保存成功!");
                } else {
                    Vertex newVertex = new Vertex(null, name, x, y);
                    mongoController.insertVertex(newVertex);
                    vertexSaveButton.setText("新建成功!");
                }
            } catch (Exception exception) {
                x_vertexField.clear();
                y_vertexField.clear();
            }
        });
        loginAnimation.play();
    }

    //在道路名输入框键入回车键，实现道路搜索功能
    @FXML
    protected void edgeEnter(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER)
            edgeSearch();
        if (radioButtonSelected != null) {
            radioButtonSelected.setSelected(false);
            radioButtonSelected = null;
        }
    }

    //道路搜索，根据是否存在该道路，保存按钮更改为"新建"或"更新"
    @FXML
    private void edgeSearch() {
        edge = mongoController.retrieveEdge(edgeField.getText());
        if (edge == null) {
            u_edgeField.clear();
            v_edgeField.clear();
            distanceField.clear();
            limitField.clear();
            radioButtonSelected = null;
            //新建道路才能输入两节点，现有道路不能更改节点
            u_edgeField.setEditable(true);
            v_edgeField.setEditable(true);
            u_edgeField.requestFocus();
            edgeSaveButton.setText("新建");
        } else {
            u_edgeField.setText(graph.getVertices().get(edge.getUId()).getName());
            v_edgeField.setText(graph.getVertices().get(edge.getVId()).getName());
            u_edgeField.setEditable(false);
            v_edgeField.setEditable(false);
            distanceField.setText(String.valueOf(edge.getDistance()));
            limitField.setText(String.valueOf(edge.getLimit()));
            //五个radioButton与路况绑定，并保证只能同时选一个按钮
            if (edge.getCondition() == EdgeCondition.BAD) {
                radioButton1.setSelected(true);
                radioButtonSelected = radioButton1;
            } else if (edge.getCondition() == EdgeCondition.CROWDED) {
                radioButton2.setSelected(true);
                radioButtonSelected = radioButton2;
            } else if (edge.getCondition() == EdgeCondition.MEDIUM) {
                radioButton3.setSelected(true);
                radioButtonSelected = radioButton3;
            } else if (edge.getCondition() == EdgeCondition.WELL) {
                radioButton4.setSelected(true);
                radioButtonSelected = radioButton4;
            } else if (edge.getCondition() == EdgeCondition.PERFECT) {
                radioButton5.setSelected(true);
                radioButtonSelected = radioButton5;
            }
            edgeSaveButton.setText("更新");
        }
    }

    //道路保存，若存在该道路则保存修改（不能更改节点），若不存在道路则新建道路保存
    //检验节点是否存在、距离为正数、限速为正数
    @FXML
    protected void onEdgeSaveButtonClicked() {
        Animation loginAnimation = new FlipInX(edgeSaveButton).getTimeline();
        loginAnimation.setOnFinished(event -> {
            EdgeCondition eCondition = EdgeCondition.UNKNOWN;
            if (radioButtonSelected == radioButton1) eCondition = EdgeCondition.BAD;
            else if (radioButtonSelected == radioButton2) eCondition = EdgeCondition.CROWDED;
            else if (radioButtonSelected == radioButton3) eCondition = EdgeCondition.MEDIUM;
            else if (radioButtonSelected == radioButton4) eCondition = EdgeCondition.WELL;
            else if (radioButtonSelected == radioButton5) eCondition = EdgeCondition.PERFECT;
            Vertex u = mongoController.retrieveVertex(u_edgeField.getText());
            Vertex v = mongoController.retrieveVertex(v_edgeField.getText());
            if (u == null) {
                u_edgeField.setPromptText("找不到" + u_edgeField.getText());
                u_edgeField.clear();
                edge = null;
                return;
            }
            if (v == null) {
                v_edgeField.setPromptText("找不到" + v_edgeField.getText());
                v_edgeField.clear();
                edge = null;
                return;
            }
            if (Double.parseDouble(distanceField.getText()) <= 0) {
                distanceField.clear();
                distanceField.setPromptText("距离不能为负！");
                edge = null;
                return;
            }
            if (Double.parseDouble(limitField.getText()) <= 0) {
                limitField.clear();
                limitField.setPromptText("限速不能为负！");
                edge = null;
                return;
            }
            if (edge != null) {
                edge.setLimit(Double.parseDouble(limitField.getText()));
                edge.setDistance(Double.parseDouble(distanceField.getText()));
                edge.setCondition(eCondition);
                mongoController.updateEdge(edge);
                edgeSaveButton.setText("更新成功!");
            } else {
                Edge newEdge = new Edge(null, edgeField.getText(), u.getId(), v.getId(),
                        Double.parseDouble(distanceField.getText()), Double.parseDouble(limitField.getText()), eCondition);
                mongoController.insertEdge(newEdge);
                edgeSaveButton.setText("新建成功!");
            }
        });
        loginAnimation.play();
    }
}