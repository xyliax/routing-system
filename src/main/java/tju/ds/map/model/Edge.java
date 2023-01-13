package tju.ds.map.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bson.Document;

@Data
@AllArgsConstructor
public class Edge {
    String id;
    String name;
    String uId;
    String vId;

    public static Edge fromDocument(Document document) {
        try {
            if (document == null) return null;
            return new Edge(document.get("_id").toString(),
                    (String) document.get("name"),
                    (String) document.get("uid"),
                    (String) document.get("vid"));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Document toDocument() {
        return new Document("name", name)
                .append("uid", uId)
                .append("vid", vId);
    }
}
