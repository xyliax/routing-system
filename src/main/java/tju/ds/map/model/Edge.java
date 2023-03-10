package tju.ds.map.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bson.Document;



@Data
@AllArgsConstructor
public class Edge {
    //边，包括系统自动生成id、名称、两节点id、距离、限速和路况
    String id;
    String name;
    String uId;
    String vId;
    double distance;
    double limit;
    EdgeCondition condition;

    //从文档里取出边
    public static Edge fromDocument(Document document) {
        if (document == null) return null;
        return new Edge(document.get("_id").toString(),
                (String) document.get("name"),
                (String) document.get("uid"),
                (String) document.get("vid"),
                (double) document.get("distance"),
                (double) document.get("limit"),
                EdgeCondition.valueOf((String) document.getOrDefault("condition", "MEDIUM")));
    }

    //保存至数据库文档
    public Document toDocument() {
        return new Document("name", name)
                .append("uid", uId)
                .append("vid", vId)
                .append("distance", distance)
                .append("limit", limit)
                .append("condition", condition.name());
    }

    //不同路况下的所需时间：畅通时能按照限速行驶，较差时只能行驶限速的1/6
    public double timeCost() {
        return distance / ((condition.ratio + 1) / 6.0 * limit);
    }

    //道路信息在地图界面上的显示文本
    @Override
    public String toString() {
        return String.format("%s-全长%.1f-限速%.1f(km/h)%n路况%s", name, distance, limit, condition.info);
    }
}
