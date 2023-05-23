package entity;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.Label;
import javafx.scene.shape.Circle;

public class Vertex {

	private Label id;
	private List<Vertex> adjacentVertex;
	private List<Edge> listEdge;
	private Circle vertextUI;
	private Vertex parent = null;
	private boolean isVisited = false;

	public Vertex() {
		adjacentVertex = new ArrayList<Vertex>();
		listEdge = new ArrayList<Edge>();
	}

	public Vertex(Label id, Circle vertextUI) {
		this.id = id;
		this.vertextUI = vertextUI;
		adjacentVertex = new ArrayList<Vertex>();
		listEdge = new ArrayList<Edge>();
	}

	/**
	 * @return the id
	 */
	public Label getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Label id) {
		this.id = id;
	}

	/**
	 * @return the forwardVertex
	 */
	public List<Vertex> getAdjacentVertex() {
		return adjacentVertex;
	}

	/**
	 * @param forwardVertex the forwardVertex to set
	 */
	public void setAdjacentVertex(List<Vertex> forwardVertex) {
		this.adjacentVertex = forwardVertex;
	}

	/**
	 * @return the vertextUI
	 */
	public Circle getVertextUI() {
		return vertextUI;
	}

	/**
	 * @param vertextUI the vertextUI to set
	 */
	public void setVertextUI(Circle vertextUI) {
		this.vertextUI = vertextUI;
	}

	/**
	 * @return the isVisited
	 */
	public boolean isVisited() {
		return isVisited;
	}

	/**
	 * @param isVisited the isVisited to set
	 */
	public void setVisited(boolean isVisited) {
		this.isVisited = isVisited;
	}

	/**
	 * @return the parent
	 */
	public Vertex getParent() {
		return parent;
	}

	/**
	 * @param parent the parent to set
	 */
	public void setParent(Vertex parent) {
		this.parent = parent;
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

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Vertex)) {
			throw new ClassCastException("Not instance of Vertex class.");
		}
		Vertex vertex = (Vertex) obj;
		if (vertex.getId().getText().equals(this.getId().getText())) {
			return true;
		}
		return false;
	}
}
