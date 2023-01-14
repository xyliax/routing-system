package tju.ds.map.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

@Data
public class Graph {
    HashMap<String, Vertex> vertices;
    HashMap<Vertex, LinkedList<Edge>> graph;

    public Graph(ArrayList<Vertex> vertexArrayList, ArrayList<Edge> edgeArrayList) {
        vertices = new HashMap<>();
        graph = new HashMap<>();
        vertexArrayList.forEach(vertex -> {
            vertices.put(vertex.id, vertex);
            graph.put(vertex, new LinkedList<>());
        });
        edgeArrayList.forEach(edge -> {
            LinkedList<Edge> edgeLinkedList = graph.get(vertices.get(edge.uId));
            System.out.println(vertices);
            System.out.println(edge);
            System.out.println(edgeLinkedList);
            edgeLinkedList.add(edge);
        });
    }
}
