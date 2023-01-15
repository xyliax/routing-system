package tju.ds.map.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.Document;

//节点类，包括系统自动生成ID、节点名、节点横纵坐标（整型）
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vertex {
    String id;
    String name;
    int x;
    int y;

    //从文档里取出节点
    public static Vertex fromDocument(Document document) {
        if (document == null) return null;
        return new Vertex(document.get("_id").toString(),
                (String) document.get("name"),
                Integer.parseInt((String) document.get("x")),
                Integer.parseInt((String) document.get("y")));
    }

    //保存至数据库文档
    public Document toDocument() {
        return new Document("name", name)
                .append("x", String.valueOf(x))
                .append("y", String.valueOf(y));
    }

    //地图页面显示的节点信息文本
    @Override
    public String toString() {
        return String.format("%s(%d,%d)", name, x, y);
    }
}
