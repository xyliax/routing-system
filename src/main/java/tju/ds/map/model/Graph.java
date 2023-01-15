package tju.ds.map.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;

@Data
public class Graph {
    HashMap<String, Vertex> vertices; //节点id映射到对应节点的哈希表
    HashMap<String, Edge> edges; //道路id映射到对应道路的哈希表
    /*
    graph为二重哈希表，第一层是节点对其所连接的道路集合的映射，第二层是其连接的道路集合，
    道路集合是道路的另一个节点映射到对应道路的集合
     */
    HashMap<Vertex, HashMap<String, Edge>> graph;

    public Graph(ArrayList<Vertex> vertexArrayList, ArrayList<Edge> edgeArrayList) {
        vertices = new HashMap<>();
        edges = new HashMap<>();
        graph = new HashMap<>();
        vertexArrayList.forEach(vertex -> {
            vertices.put(vertex.id, vertex);
            graph.put(vertex, new HashMap<>());
        });
        edgeArrayList.forEach(edge -> {
            edges.put(edge.id, edge);
            HashMap<String, Edge> uMap = graph.get(vertices.get(edge.uId));
            uMap.put(edge.vId, edge);
            HashMap<String, Edge> vMap = graph.get(vertices.get(edge.vId));
            vMap.put(edge.uId, edge);
        });
    }
}
