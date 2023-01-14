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
    double distance;
    double limit;
    EdgeCondition condition;

    public static Edge fromDocument(Document document) {
        try {
            if (document == null) return null;
            return new Edge(document.get("_id").toString(),
                    (String) document.get("name"),
                    (String) document.get("uid"),
                    (String) document.get("vid"),
                    (double) document.get("distance"),
                    (double) document.get("limit"),
                    EdgeCondition.valueOf((String) document.getOrDefault("condition", "UNKNOWN")));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Document toDocument() {
        return new Document("name", name)
                .append("uid", uId)
                .append("vid", vId)
                .append("distance", distance)
                .append("limit", limit)
                .append("condition", condition.name());
    }

    @Override
    public String toString() {
        return String.format("%s-全长%.1f-限速%.1f-路况%s", name, distance, limit, condition.info);
    }
}
