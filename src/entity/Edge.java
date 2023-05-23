package entity;

import javafx.scene.control.Label;
import javafx.scene.paint.Color;

public class Edge {

	private Vertex fromVertex;
	private Vertex toVertex;
	private Arrow arrow;
	private int flow = 0;
	private int capacity;
	private Label capacityLabel;

	
	public Edge(Vertex fromVertex, Vertex toVertex, Arrow arrow, int capacity, Label capacityLabel) {
		super();
		this.fromVertex = fromVertex;
		this.toVertex = toVertex;
		this.arrow = arrow;
		this.capacity = capacity;
		this.capacityLabel = capacityLabel;
	}
	
	public Edge(Vertex fromVertex, Vertex toVertex, int capcacity) {
		
		Label capacityLabel = new Label();
		capacityLabel.setText(String.valueOf(capcacity));
		capacityLabel.setTextFill(Color.BLACK);
		capacityLabel.setLayoutX((fromVertex.getVertextUI().getCenterX() + toVertex.getVertextUI().getCenterX()) / 2);
		capacityLabel.setLayoutY((fromVertex.getVertextUI().getCenterY() + toVertex.getVertextUI().getCenterY()) / 2);
		
		
		double fromCenterX = fromVertex.getVertextUI().getCenterX();
		double fromCenterY = fromVertex.getVertextUI().getCenterY();
		double toCenterX = toVertex.getVertextUI().getCenterX();
		double toCenterY = toVertex.getVertextUI().getCenterY();
		
		Arrow arrow = new Arrow(fromCenterX, fromCenterY, toCenterX, toCenterY);
		
		this.capacity = capcacity;
		this.capacityLabel = capacityLabel;
		this.arrow = arrow;
		this.fromVertex = fromVertex;
		this.toVertex = toVertex;
		
	}
	/**
	 * @return the fromVertex
	 */
	public Vertex getFromVertex() {
		return fromVertex;
	}

	/**
	 * @param fromVertex the fromVertex to set
	 */
	public void setFromVertex(Vertex fromVertex) {
		this.fromVertex = fromVertex;
	}

	/**
	 * @return the toVertex
	 */
	public Vertex getToVertex() {
		return toVertex;
	}

	/**
	 * @param toVertex the toVertex to set
	 */
	public void setToVertex(Vertex toVertex) {
		this.toVertex = toVertex;
	}

	/**
	 * @return the arrow
	 */
	public Arrow getArrow() {
		return arrow;
	}

	/**
	 * @param arrow the arrow to set
	 */
	public void setArrow(Arrow arrow) {
		this.arrow = arrow;
	}

	/**
	 * @return the flow
	 */
	public int getFlow() {
		return flow;
	}

	/**
	 * @param flow the flow to set
	 */
	public void setFlow(int flow) {
		this.flow = flow;
	}

	/**
	 * @return the capacity
	 */
	public int getCapacity() {
		return capacity;
	}

	/**
	 * @param capacity the capacity to set
	 */
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	/**
	 * @return the capacityLabel
	 */
	public Label getCapacityLabel() {
		return capacityLabel;
	}

	/**
	 * @param capacityLabel the capacityLabel to set
	 */
	public void setCapacityLabel(Label capacityLabel) {
		this.capacityLabel = capacityLabel;
	}

}
