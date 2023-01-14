package tju.ds.map.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

@Data
public class Graph {
    HashMap<String, Vertex> vertices;
    HashMap<String, Edge> edges;
    HashMap<Vertex, LinkedList<Edge>> graph;

    public Graph(ArrayList<Vertex> vertexArrayList, ArrayList<Edge> edgeArrayList) {
        vertices = new HashMap<>();
        edges = new HashMap<>();
        graph = new HashMap<>();
        vertexArrayList.forEach(vertex -> {
            vertices.put(vertex.id, vertex);
            graph.put(vertex, new LinkedList<>());
        });
        edgeArrayList.forEach(edge -> {
            edges.put(edge.id, edge);
            LinkedList<Edge> uLinkedList = graph.get(vertices.get(edge.uId));
            uLinkedList.add(edge);
            LinkedList<Edge> vLinkedList = graph.get(vertices.get(edge.vId));
            vLinkedList.add(edge);
        });
        System.out.println(edges);
    }
}
