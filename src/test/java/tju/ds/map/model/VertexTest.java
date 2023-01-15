package tju.ds.map.model;

import org.bson.Document;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tju.ds.map.dao.MongoController;

class VertexTest {

    Vertex vertex;

    @BeforeEach
    void setUp() {
        vertex = new Vertex("id", "v1", 1, 2);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void fromDocument() {
        MongoController mongoController = MongoController.getInstance();
        mongoController.connect();
        Vertex vertex1 = mongoController.retrieveVertex("节点1");
        Vertex vertex2 = mongoController.retrieveVertex("x");
        assert vertex1.getName().equals("节点1");
        assert vertex2 == null;
    }

    @Test
    void toDocument() {
        Document document = vertex.toDocument();
        assert document.get("name").equals(vertex.name);
    }

    @Test
    void testToString() {
        assert vertex.toString().length() > 0;
    }

    @Test
    void getId() {
        assert vertex.getId().equals("id");
    }

    @Test
    void getName() {
        assert vertex.getName().equals("v1");
    }

    @Test
    void getX() {
        assert vertex.getX() == 1;
    }

    @Test
    void getY() {
        assert vertex.getY() == 2;
    }

    @Test
    void setId() {
        vertex.setId("newId");
        assert vertex.getId().equals("newId");
    }

    @Test
    void setName() {
        vertex.setName("111");
        assert vertex.getName().equals("111");
    }

    @Test
    void setX() {
        vertex.setX(4);
        assert vertex.getX() == 4;
    }

    @Test
    void setY() {
        vertex.setY(3);
        assert vertex.getY() == 3;
    }

    @Test
    void testEquals() {
        assert !vertex.equals(new Vertex());
    }

    @Test
    void canEqual() {
        assert true;
    }

    @Test
    void testHashCode() {
        assert true;
    }
}