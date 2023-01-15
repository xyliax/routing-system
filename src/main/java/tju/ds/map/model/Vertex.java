package tju.ds.map.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vertex {
    String id;
    String name;
    int x;
    int y;

    public static Vertex fromDocument(Document document) {
        if (document == null) return null;
        return new Vertex(document.get("_id").toString(),
                (String) document.get("name"),
                Integer.parseInt((String) document.get("x")),
                Integer.parseInt((String) document.get("y")));
    }

    public Document toDocument() {
        return new Document("name", name)
                .append("x", String.valueOf(x))
                .append("y", String.valueOf(y));
    }

    @Override
    public String toString() {
        return String.format("%s(%d,%d)", name, x, y);
    }
}
