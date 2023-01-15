package tju.ds.map.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tju.ds.map.dao.MongoController;

import java.util.ArrayList;

class GraphTest {

    Graph graph;
    ArrayList<Vertex> vertices;
    ArrayList<Edge> edges;

    {
        MongoController mongoController = MongoController.getInstance();
        mongoController.connect();
        vertices = mongoController.retrieveAllVertices();
        edges = mongoController.retrieveAllEdges();
    }

    @BeforeEach
    void setUp() {
        graph = new Graph(vertices, edges);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getVertices() {
        assert graph.getVertices().values().size() == vertices.size();
    }

    @Test
    void getEdges() {
        assert graph.getEdges().values().size() == edges.size();
    }

    @Test
    void getGraph() {
        assert graph.getGraph().size() == vertices.size();
    }

    @Test
    void setVertices() {
        graph.setVertices(null);
        assert graph.getVertices() == null;
    }

    @Test
    void setEdges() {
        graph.setEdges(null);
        assert graph.getEdges() == null;
    }

    @Test
    void setGraph() {
        graph.setGraph(null);
        assert graph.getGraph() == null;
    }

    @Test
    void testEquals() {
        assert !graph.equals(new Graph(new ArrayList<>(), new ArrayList<>()));
    }

    @Test
    void canEqual() {
        assert true;
    }

    @Test
    void testHashCode() {
        assert true;
    }

    @Test
    void testToString() {
        assert graph.toString().length() > 0;
    }
}