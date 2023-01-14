package tju.ds.map.dao;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
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
        MongoDatabase database = mongoClient.getDatabase(DB_NAME);
        this.users = database.getCollection("users");
        this.vertices = database.getCollection("vertices");
        this.edges = database.getCollection("edges");
        users.createIndex(Indexes.ascending("username"), new IndexOptions().unique(true));
    }

    public void insertUser(User user) {
        Document userDoc = user.toDocument();
        users.insertOne(userDoc);
    }

    public User retrieveUser(String username) {
        Document userDoc = users.find(new Document("username", username)).first();
        return User.fromDocument(userDoc);
    }

    public boolean updateUser(User user) {
        ObjectId userId = new ObjectId(user.getId());
        UpdateResult result = users.replaceOne(new Document("_id", userId), user.toDocument());
        return result.getModifiedCount() > 0;
    }

    public boolean deleteUser(User user) {
        ObjectId userId = new ObjectId(user.getId());
        DeleteResult result = users.deleteOne(new Document("_id", userId));
        return result.getDeletedCount() > 0;
    }

    public void insertVertex(Vertex vertex) {
        Document vertexDoc = vertex.toDocument();
        vertices.insertOne(vertexDoc);
    }

    public Vertex retrieveVertex(String name) {
        Document vertexDoc = vertices.find(new Document("name", name)).first();
        return Vertex.fromDocument(vertexDoc);
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

    public Edge retrieveEdge(String name) {
        Document edgeDoc = edges.find(new Document("name", name)).first();
        return Edge.fromDocument(edgeDoc);
    }

    public ArrayList<Edge> retrieveAllEdges() {
        ArrayList<Document> documents = edges.find().into(new ArrayList<>());
        ArrayList<Edge> edgeArrayList = new ArrayList<>();
        documents.forEach(document -> edgeArrayList.add(Edge.fromDocument(document)));
        return edgeArrayList;
    }

    private static final class MongoControllerHolder {
        private static final MongoController mongoController = new MongoController();
    }
}
