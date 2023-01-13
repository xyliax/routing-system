package tju.ds.map.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bson.Document;

@Data
@AllArgsConstructor
public class User {
    private String id;
    private String username;
    private String password;
    private UserType type;
    private String avatar;
    private String bio;

    public static User fromDocument(Document document) {
        try {
            if (document == null) return null;
            return new User(document.get("_id").toString(),
                    (String) document.get("username"),
                    (String) document.get("password"),
                    UserType.valueOf((String) document.getOrDefault("type", "UNKNOWN")),
                    (String) document.get("avatar"),
                    (String) document.get("bio"));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Document toDocument() {
        return new Document("username", username)
                .append("password", password)
                .append("type", type.name())
                .append("avatar", avatar)
                .append("bio", bio);
    }
}