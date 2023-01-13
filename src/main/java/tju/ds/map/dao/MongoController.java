package tju.ds.map.dao;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.types.ObjectId;
import tju.ds.map.model.Edge;
import tju.ds.map.model.User;
import tju.ds.map.model.Vertex;

import java.util.ArrayList;

@Slf4j
public final class MongoController {
    private static final String CONNECT_STRING = "mongodb://xylia:POLYpyx2020@peiyuxing.xyz/?authSource=test";
    private static final String DB_NAME = "test";
    private MongoDatabase database;
    private MongoCollection<Document> users;
    private MongoCollection<Document> vertices;
    private MongoCollection<Document> edges;

    private MongoController() {
    }

    public static MongoController getInstance() {
        return MongoControllerHolder.mongoController;
    }

    public void connect() {
        MongoClient mongoClient = MongoClients.create(CONNECT_STRING);
        this.database = mongoClient.getDatabase(DB_NAME);
        this.users = database.getCollection("users");
        this.vertices = database.getCollection("vertices");
        this.edges = database.getCollection("edges");
    }

    public void insertUser(User user) {
        Document userDoc = user.toDocument();
        users.insertOne(userDoc);
    }

    public User retrieveUser(String username) {
        Document userDoc = users.find(new Document("username", username)).first();
        return User.fromDocument(userDoc);
    }

    public void updateUser(User user) {
        ObjectId userId = new ObjectId(user.getId());
        users.replaceOne(new Document("_id", userId), user.toDocument());
    }

    public void insertVertex(Vertex vertex) {
        Document vertexDoc = vertex.toDocument();
        vertices.insertOne(vertexDoc);
    }

    public ArrayList<Vertex> retrieveAllVertices() {
        ArrayList<Document> documents = vertices.find().into(new ArrayList<>());
        ArrayList<Vertex> vertexArrayList = new ArrayList<>();
        documents.forEach(document -> vertexArrayList.add(Vertex.fromDocument(document)));
        return vertexArrayList;
    }

    public void insertEdge(Edge edge) {
        Document edgeDoc = edge.toDocument();
        edges.insertOne(edgeDoc);
    }

    public ArrayList<Edge> retrieveAllEdges() {
        ArrayList<Document> documents = vertices.find().into(new ArrayList<>());
        ArrayList<Edge> edgeArrayList = new ArrayList<>();
        documents.forEach(document -> edgeArrayList.add(Edge.fromDocument(document)));
        return edgeArrayList;
    }

    private static final class MongoControllerHolder {
        private static final MongoController mongoController = new MongoController();
    }
}
