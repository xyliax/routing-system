package tju.ds.map.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bson.Document;

//用户类，包含数据库自动生成的id、用户名、密码、用户类型（普通用户/管理员）、手机号码、邮箱、简介等
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

    //从文档里取出用户
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

    //保存用户至数据库文档
    public Document toDocument() {
        return new Document("username", username)
                .append("password", password)
                .append("type", type.name())
                .append("mobile", mobile)
                .append("email", email)
                .append("bio", bio);
    }
}
