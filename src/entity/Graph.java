package entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Graph {
	
	private List<Vertex> listVertex;
	private List<Edge> listEdge;

	public Graph(List<Vertex> listVertex, List<Edge> listEdge) {
		this.listVertex = listVertex;
		this.listEdge = listEdge;
	}

	public Graph(List<Vertex> listVertex) {
		this.listVertex = listVertex;
		listEdge = new ArrayList<Edge>();
	}

	public void addNewVertex(Vertex vertex) {
		this.listVertex.add(vertex);
	}

	public void addNewEdge(Edge edge) {
		// add edge
		this.listEdge.add(edge);
		// add toVertex to adjacent list of fromVertex
		edge.getFromVertex().getAdjacentVertex().add(edge.getToVertex());
		
		edge.getFromVertex().getListEdge().add(edge);
		edge.getToVertex().getListEdge().add(edge);
	}

	public void removeVertex(Vertex vertex) {
		// remove all edge link this vertex
		List<Edge> listDeletedEdge = new ArrayList<Edge>();
		for (Edge edge : listEdge) {
			if (edge.getFromVertex().getId().getText().equals(vertex.getId().getText())
					|| edge.getToVertex().getId().getText().equals(vertex.getId().getText())) {
				listDeletedEdge.add(edge);
			}
		}
		for (Edge edge : listDeletedEdge) {
			listEdge.remove(edge);
		}
		// remove vertex
		this.listVertex.remove(vertex);
	}

	public void removeEdge(Edge edge) {
		// remove vertex in adjacent vertes list of fromVertex
		edge.getFromVertex().getAdjacentVertex().remove(edge.getToVertex());
		edge.getFromVertex().getListEdge().remove(edge);
		edge.getToVertex().getListEdge().remove(edge);
		// remove edge
		this.listEdge.remove(edge);
	}

	public static Graph generateGraph(Integer numberOfVertices) {
		// horizontal coordinates [9;500]
		// vertical coordinates [5;640]
		double minX = 20, maxX = 500;
		double minY = 20, maxY = 550;
		// generate vertices
		double x;
		double y;
		Random random = new Random();
		List<Vertex> listVertex = new ArrayList<Vertex>();
		for (int i = 1; i <= numberOfVertices; i++) {
			x = minX + (maxX - minX) * random.nextFloat();
			y = minY + (maxY - minY) * random.nextFloat();

			// new circle
			Circle newCircle = new Circle(x, y, 1.2, Color.LIGHTSKYBLUE);
			newCircle.setId(String.valueOf(i));

			// id label
			Label id = new Label(String.valueOf(i));
			id.setTextFill(Color.BLACK);
			id.setLayoutX(x);
			id.setLayoutY(y);

			Vertex newVertex = new Vertex(id, newCircle);
			listVertex.add(newVertex);
		}

		Graph graph = new Graph(listVertex);
		// Generate edge
		int numberOfEdgePerVertex = numberOfVertices / 2;
		for (Vertex vertex : listVertex) {
			if (vertex.getId().getText().equals("1")) {
				for (int i = 1; i <= numberOfEdgePerVertex; i++) {
					int index = 2 + (int) ((numberOfVertices - 1 - 2) * random.nextDouble());
					int randomCapacity = random.nextInt(15) + 1;

					Vertex toVertex = listVertex.get(index);
					if (!checkExistingEdge(vertex, toVertex, graph)) {
						addEdge(vertex, toVertex, randomCapacity, graph);
					}
				}
			} else if (!vertex.getId().getText().equals(numberOfVertices.toString())) {
				for (int i = 2; i <= numberOfEdgePerVertex; i++) {
					int index = 2 + (int) ((numberOfVertices - 2) * random.nextDouble());
					int randomCapacity = random.nextInt(15)+1;

					Vertex toVertex = listVertex.get(index);
					if (!toVertex.equals(vertex)) {
						if (!checkExistingEdge(vertex, toVertex, graph)) {
							addEdge(vertex, toVertex, randomCapacity, graph);
						}
					}
				}
			}
		}
		return graph;
	}

	private static void addEdge(Vertex vertex1, Vertex vertex2, int capacity, Graph graph) {
		Edge newEdge = new Edge(vertex1, vertex2, capacity);
		graph.addNewEdge(newEdge);
	}

	public static boolean checkExistingEdge(Vertex startVertex, Vertex targetVertex, Graph graph) {
		for (Edge edge : graph.getListEdge()) {
			Vertex fromvertex = edge.getFromVertex();
			Vertex toVertex = edge.getToVertex();

			if (fromvertex.getId().getText().equals(startVertex.getId().getText())
					&& toVertex.getId().getText().equals(targetVertex.getId().getText())) {
				return true;
			} else if(fromvertex.getId().getText().equals(targetVertex.getId().getText())
					&& toVertex.getId().getText().equals(startVertex.getId().getText())) {
				return true;
			}
		}
		return false;
	}
	
	public Vertex getVertexById(int index) {
		for (Vertex vertex : listVertex) {
			if(vertex.getId().getText().equals(String.valueOf(index))) {
				return vertex;
			}
		}
		return null;
	}
	
	/**
	 * @return the listVertex
	 */
	public List<Vertex> getListVertex() {
		return listVertex;
	}

	/**
	 * @param listVertex the listVertex to set
	 */
	public void setListVertex(List<Vertex> listVertex) {
		this.listVertex = listVertex;
	}

	/**
	 * @return the listEdge
	 */
	public List<Edge> getListEdge() {
		return listEdge;
	}

	/**
	 * @param listEdge the listEdge to set
	 */
	public void setListEdge(List<Edge> listEdge) {
		this.listEdge = listEdge;
	}

}
