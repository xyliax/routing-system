package tju.ds.map.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;

@Data
public class Graph {
    HashMap<String, Vertex> vertices;
    HashMap<String, Edge> edges;
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
