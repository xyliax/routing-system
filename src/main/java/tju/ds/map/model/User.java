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
    private String mobile;
    private String email;
    private String bio;

    public static User fromDocument(Document document) {
        if (document == null) return null;
        return new User(document.get("_id").toString(),
                (String) document.get("username"),
                (String) document.get("password"),
                UserType.valueOf((String) document.getOrDefault("type", "UNKNOWN")),
                (String) document.get("mobile"),
                (String) document.get("email"),
                (String) document.get("bio"));
    }

    public Document toDocument() {
        return new Document("username", username)
                .append("password", password)
                .append("type", type.name())
                .append("mobile", mobile)
                .append("email", email)
                .append("bio", bio);
    }
}
