package algorithms;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import controller.VisualizePageController;
import entity.Edge;
import entity.Graph;
import entity.Vertex;
import javafx.animation.FadeTransition;
import javafx.animation.FillTransition;
import javafx.animation.SequentialTransition;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class EdmondsKarp extends MaxFlowAlgorithms {
	private SequentialTransition st;
	private int time = 500;
	
	public EdmondsKarp(Graph graph, Group graphField) {
		super(graph, graphField);
		st = new SequentialTransition();
	}

	@Override
	protected void setInitialEdgeValue() {
		// TODO Auto-generated method stub
		List<Edge> listEdge = getGraph().getListEdge();

		for (Edge edge : listEdge) {
			edge.setFlow(0);

			edge.getCapacityLabel().setText(edge.getFlow() + "/" + edge.getCapacity());
		}
	}

	@Override
	// using Breath First Search
	protected int findAugmentPaths(Vertex source, Vertex sink) {
		System.out.println("-------Edmonds Karp-------: finding augment path.");
		long startTime = System.currentTimeMillis();
		VisualizePageController.textFlow.appendText("\nFinding path by Edmonds Karp:");
		int maxFlow = 0;
		while (true) {
			sink.setParent(null);

			// before using bfs
			refreshVertex(source, sink);

			bfs(source, sink);

			// when no path found anymore.
			if (sink.getParent() == null) {
				break;
			}

			// update edge
			int bottleNeck = updateEdges(sink);
			maxFlow += bottleNeck;

		}
		
		long endTime = System.currentTimeMillis();
		long totalAlgorithmTime = endTime - startTime; 
		
		st.setOnFinished(e -> {
			for (Vertex vertex : getGraph().getListVertex()) {
				if (!(vertex.equals(source) || vertex.equals(sink))) {
					FillTransition ft = new FillTransition(Duration.millis(1000), vertex.getVertextUI());
					ft.setFromValue(Color.DARKBLUE);
					ft.setToValue(Color.LIGHTSKYBLUE);
					ft.play();
				}

				vertex.setVisited(false);
				vertex.setParent(null);
			}

			for (Edge edge : getGraph().getListEdge()) {
				FillTransition ft = new FillTransition(Duration.millis(200), edge.getArrow());
				ft.setToValue(Color.BLACK);
				ft.play();
			}
		});

		String maxFlowResult = "\nMax Flow: " + maxFlow;
		FadeTransition fd = new FadeTransition(Duration.millis(10), VisualizePageController.textFlow);
		fd.setOnFinished(e -> {
			VisualizePageController.textFlow.appendText(maxFlowResult);
		});
		fd.onFinishedProperty();
		
		FadeTransition fd2 = new FadeTransition(Duration.millis(10), VisualizePageController.textFlow);
		fd2.setOnFinished(e -> {
			VisualizePageController.textFlow.appendText("\nRun time: " + totalAlgorithmTime+"ms");
		});
		fd2.onFinishedProperty();
		
		st.getChildren().addAll(fd, fd2);
		
		st.onFinishedProperty();
		st.play();

		return maxFlow;
	}

	@Override
	protected int updateEdges(Vertex sink) {
//		st = new SequentialTransition();
		StringBuilder sb = new StringBuilder();
		// find bottleNeck
		int bottleNeck = Integer.MAX_VALUE;
		Vertex tmp = sink;
		List<Edge> directEdges = new ArrayList<Edge>();
		List<Edge> reverseEdges = new ArrayList<Edge>();

		// find value of bottle neck
		while (true) {
			sb.append(tmp.getId().getText() + "-");

			// add animation on screen
			FillTransition ft = new FillTransition(Duration.millis(time), tmp.getVertextUI());
			ft.setToValue(Color.RED);
			st.getChildren().add(ft);

			if (tmp.getParent() == null) {
				break;
			}

			Edge edge = getEdgeBetweenTwoVertex(tmp, tmp.getParent());

			FillTransition ft2 = new FillTransition(Duration.millis(time), edge.getArrow());
			ft.setToValue(Color.RED);
			st.getChildren().add(ft2);

			// correct direction of edge
			if (edge.getFromVertex().equals(tmp.getParent())) {

				int cap = edge.getCapacity() - edge.getFlow();
				if (cap != 0 && bottleNeck > cap) {
					bottleNeck = cap;
				}
				directEdges.add(edge);
			} else if (edge.getToVertex().equals(tmp.getParent())) { // reverse edge
				int cap = edge.getFlow() - 0;
				if (cap != 0 && bottleNeck > cap) {
					bottleNeck = cap;
				}
				reverseEdges.add(edge);
			}
			tmp = tmp.getParent();

		}
		System.out.println(sb.toString() + "bottleNeck: " + bottleNeck);

		// update value of flow
		for (Edge edge : directEdges) {
			edge.setFlow(edge.getFlow() + bottleNeck);
			String updateValue = edge.getFlow() + "/" + edge.getCapacity();

			FadeTransition fd = new FadeTransition(Duration.millis(100), edge.getCapacityLabel());
			fd.setOnFinished(e -> {
				edge.getCapacityLabel().setText(updateValue);
			});
			fd.onFinishedProperty();
			st.getChildren().add(fd);
		}

		for (Edge edge : reverseEdges) {
			edge.setFlow(edge.getFlow() - bottleNeck);
			String updateValue = edge.getFlow() + "/" + edge.getCapacity();

			FadeTransition fd = new FadeTransition(Duration.millis(100), edge.getCapacityLabel());
			fd.setOnFinished(e -> {
				edge.getCapacityLabel().setText(updateValue);
			});
			fd.onFinishedProperty();
			st.getChildren().add(fd);
		}

		// update text to screen
		String path = sb.toString() + "BottleNeck = " + bottleNeck;
		FadeTransition fd = new FadeTransition(Duration.millis(10), VisualizePageController.textFlow);
		fd.setOnFinished(e -> {
			VisualizePageController.textFlow.appendText("\nFind path: " + path);
		});
		fd.onFinishedProperty();
		st.getChildren().add(fd);

		return bottleNeck;
	}

	protected void bfs(Vertex source, Vertex sink) {
		System.out.println("----------Edmonds karp--------: bfs");
//		source.setVisited(true);
		Queue<Vertex> queue = new ArrayDeque<Vertex>();
		queue.add(source);
		source.setVisited(true);

		while (!queue.isEmpty()) {
			Vertex examinedVertex = queue.poll();

			if (examinedVertex.equals(sink)) {
				break;
			}

			// change color of examined vertex in screen
			if (!examinedVertex.equals(source)) {
				FillTransition ft = new FillTransition(Duration.millis(time), examinedVertex.getVertextUI());
				ft.setToValue(Color.ORANGE);
				st.getChildren().add(ft);
			} else {
				FillTransition ft = new FillTransition(Duration.millis(time), examinedVertex.getVertextUI());
				ft.setFromValue(Color.ORANGE);
				ft.setToValue(Color.INDIANRED);
				st.getChildren().add(ft);
			}

//			System.out.println("Vertex - " + examinedVertex.getId().getText() + "--number of edges: " + examinedVertex.getListEdge());

			for (Edge edge : examinedVertex.getListEdge()) {
//				System.out.println("...");
				int remainingCapacity = 0;
				Vertex nextVertex = null;
				if (edge.getFromVertex().equals(examinedVertex)) {
					remainingCapacity = edge.getCapacity() - edge.getFlow();
					nextVertex = edge.getToVertex();
				} else if (edge.getToVertex().equals(examinedVertex)) {
					remainingCapacity = edge.getFlow() - 0;
					nextVertex = edge.getFromVertex();
				}
				if (remainingCapacity > 0 && !nextVertex.isVisited()) {
					nextVertex.setVisited(true);
					queue.add(nextVertex);
					nextVertex.setParent(examinedVertex);
				}
			}

		}

	}

	private Edge getEdgeBetweenTwoVertex(Vertex vertex1, Vertex vertex2) {
		for (Edge edge : vertex1.getListEdge()) {
			if (edge.getFromVertex().equals(vertex2) || edge.getToVertex().equals(vertex2)) {
				return edge;
			}
		}
		return null;
	}

	private void refreshVertex(Vertex source, Vertex sink) {

		for (Vertex vertex : getGraph().getListVertex()) {
			if (!(vertex.equals(source) || vertex.equals(sink))) {
				FillTransition ft = new FillTransition(Duration.millis(10), vertex.getVertextUI());
				ft.setToValue(Color.LIGHTSKYBLUE);
//				ft.play();
				st.getChildren().add(ft);
			}

			vertex.setVisited(false);
			vertex.setParent(null);
		}

		for (Edge edge : getGraph().getListEdge()) {
			FillTransition ft = new FillTransition(Duration.millis(200), edge.getArrow());
			ft.setToValue(Color.BLACK);
//			ft.play();
			st.getChildren().add(ft);
		}

		System.out.println("-------Edmonds Karp-------: refresh");
	}

	/**
	 * @return the st
	 */
	public SequentialTransition getSt() {
		return st;
	}

	/**
	 * @param st the st to set
	 */
	public void setSt(SequentialTransition st) {
		this.st = st;
	}

}
