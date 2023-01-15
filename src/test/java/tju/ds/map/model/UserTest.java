package tju.ds.map.model;

import org.bson.Document;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tju.ds.map.dao.MongoController;

class UserTest {

    User user;

    @BeforeEach
    void setUp() {
        user = new User("id", "user", "0000", UserType.NORMAL, "1", "2", "3");
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void fromDocument() {
        MongoController mongoController = MongoController.getInstance();
        mongoController.connect();
        User user1 = mongoController.retrieveUser("xyz");
        User user2 = mongoController.retrieveUser("x");
        assert user1.getUsername().equals("xyz");
        assert user2 == null;
    }

    @Test
    void toDocument() {
        Document document = user.toDocument();
        assert document.get("username").equals(user.getUsername());
    }

    @Test
    void getId() {
        assert user.getId().equals("id");
    }

    @Test
    void getUsername() {
        assert user.getUsername().equals("user");
    }

    @Test
    void getPassword() {
        assert user.getPassword().equals("0000");
    }

    @Test
    void getType() {
        assert user.getType().equals(UserType.NORMAL);
    }

    @Test
    void getMobile() {
        assert user.getMobile().equals("1");
    }

    @Test
    void getEmail() {
        assert user.getEmail().equals("2");
    }

    @Test
    void getBio() {
        assert user.getBio().equals("3");
    }

    @Test
    void setId() {
        user.setId("newId");
        assert user.getId().equals("newId");
    }

    @Test
    void setUsername() {
        user.setUsername("username");
        assert user.getUsername().equals("username");
    }

    @Test
    void setPassword() {
        user.setPassword("password");
        assert user.getPassword().equals("password");
    }

    @Test
    void setType() {
        user.setType(UserType.ADMIN);
        assert user.getType() == UserType.ADMIN;
    }

    @Test
    void setMobile() {
        user.setMobile("mobile");
        assert user.getMobile().equals("mobile");
    }

    @Test
    void setEmail() {
        user.setEmail("email");
        assert user.getEmail().equals("email");
    }

    @Test
    void setBio() {
        user.setBio("bio");
        assert user.getBio().equals("bio");
    }

    @Test
    void testEquals() {
        User user1 = null;
        assert !user.equals(user1);
    }

    @Test
    void canEqual() {
        assert true;
    }

    @Test
    void testHashCode() {
        int h = user.hashCode();
        assert true;
    }

    @Test
    void testToString() {
        assert !user.toString().isEmpty();
    }
}