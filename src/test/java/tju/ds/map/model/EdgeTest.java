package tju.ds.map.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tju.ds.map.dao.MongoController;

class EdgeTest {
    Vertex u;
    Vertex v;
    Edge edge;

    @BeforeEach
    void setUp() {
        MongoController mongoController = MongoController.getInstance();
        mongoController.connect();
        u = mongoController.retrieveVertex("节点1");
        v = mongoController.retrieveVertex("节点2");
        edge = new Edge("id", "e1", u.id, v.id, 100, 50, EdgeCondition.WELL);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void fromDocument() {
    }

    @Test
    void toDocument() {
    }

    @Test
    void timeCost() {
    }

    @Test
    void testToString() {
    }

    @Test
    void getId() {
    }

    @Test
    void getName() {
    }

    @Test
    void getUId() {
    }

    @Test
    void getVId() {
    }

    @Test
    void getDistance() {
    }

    @Test
    void getLimit() {
    }

    @Test
    void getCondition() {
    }

    @Test
    void setId() {
    }

    @Test
    void setName() {
    }

    @Test
    void setUId() {
    }

    @Test
    void setVId() {
    }

    @Test
    void setDistance() {
    }

    @Test
    void setLimit() {
    }

    @Test
    void setCondition() {
    }

    @Test
    void testEquals() {
    }

    @Test
    void canEqual() {
    }

    @Test
    void testHashCode() {
    }

}