package tju.ds.map.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bson.Document;

@Data
@AllArgsConstructor
public class Vertex {
    String id;
    String name;
    int x;
    int y;

    public static Vertex fromDocument(Document document) {
        try {
            if (document == null) return null;
            return new Vertex(document.get("_id").toString(),
                    (String) document.get("name"),
                    Integer.parseInt((String) document.get("x")),
                    Integer.parseInt((String) document.get("y")));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Document toDocument() {
        return new Document("name", name)
                .append("x", String.valueOf(x))
                .append("y", String.valueOf(y));
    }
}