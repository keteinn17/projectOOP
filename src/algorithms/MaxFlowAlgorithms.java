package algorithms;

import entity.Graph;
import entity.Vertex;
import javafx.scene.Group;

public abstract class MaxFlowAlgorithms {

	private Graph graph;
	private Group graphField;
//	private Label resultLabel;
	
	public MaxFlowAlgorithms(Graph graph, Group graphField) {
		super();
		this.graph = graph;
		this.graphField = graphField;
	}
//	resultLabel = new Label(String.valueOf(result));
//	resultLabel.setTextFill(javafx.scene.paint.Color.RED);
//	resultLabel.setStyle("-fx-font-weight: bold");
//	resultLabel.setLayoutX(sink.getVertextUI().getCenterX() + 10);
//	resultLabel.setLayoutY(sink.getVertextUI().getCenterY());
//	graphField.getChildren().add(resultLabel);
	
	public final void execute(Vertex source, Vertex sink) {
		setInitialEdgeValue();
		int result = findAugmentPaths(source, sink);
		
		System.out.println("Max flow: " + result);
	}

	protected abstract void setInitialEdgeValue();

	protected abstract int findAugmentPaths(Vertex source, Vertex sink);

	protected abstract int updateEdges(Vertex sink);

	/**
	 * @return the graph
	 */
	public Graph getGraph() {
		return graph;
	}

	/**
	 * @param graph the graph to set
	 */
	public void setGraph(Graph graph) {
		this.graph = graph;
	}

	/**
	 * @return the graphField
	 */
	public Group getGraphField() {
		return graphField;
	}

	/**
	 * @param graphField the graphField to set
	 */
	public void setGraphField(Group graphField) {
		this.graphField = graphField;
	}

}
