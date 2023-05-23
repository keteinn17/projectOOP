package controller;

import java.awt.geom.Line2D;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.controlsfx.control.HiddenSidesPane;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXNodesList;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXToggleButton;

import algorithms.EdmondsKarp;
import algorithms.FordFulkerson;
import algorithms.MaxFlowAlgorithms;
import entity.Arrow;
import entity.Edge;
import entity.Graph;
import entity.Vertex;
import javafx.animation.FillTransition;
import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

public class VisualizePageController implements Initializable{

	private Graph graph = new Graph(new ArrayList<Vertex>());

	@FXML
	private AnchorPane panel1, anchorRoot;

	@FXML
	private Group canvasGroup; // contains pane and other components of graph(vertexUI, edgeUI)

	@FXML
	private Pane viewer, border;

	@FXML
	private JFXButton returnButton, clearButton, resetButton, startButton, pauseButton;

	@FXML
	private JFXToggleButton addNodeButton, addEdgeButton, removeNodeButton, removeEdgeButton, EKAButton, FFAButton;

	@FXML
	private HiddenSidesPane hiddenPane;

	@FXML
	private ImageView openHidden;

	@FXML
	private JFXSlider slider = new JFXSlider();

	@FXML
	private JFXNodesList nodeList;

	public AnchorPane hiddenRoot = new AnchorPane();

	public static TextArea textFlow = new TextArea();

	public ScrollPane textContainer = new ScrollPane();

	public SequentialTransition st = new SequentialTransition();

	boolean addNode = true, addEdge = false, removeNode = false, removeEdge = false, start = false, ffaMode = false,
			ekaMode = false, pinned = false, isPause = false;

	int time = 500;

	private int numberOfVertex = 0, numberOfEdge = 0;

	private Circle selectedVertex;

	private EventHandler<MouseEvent> mouseEventHandler = new EventHandler<MouseEvent>() {

		@Override
		public void handle(MouseEvent event) {
			// if in mode of add new edge
			if (addEdge) {
				if (numberOfEdge >= 1) {
					FFAButton.setDisable(false);
					EKAButton.setDisable(false);

				}
				Circle clickedCircle = (Circle) event.getSource();
				// add new edge
				if (event.getEventType() == MouseEvent.MOUSE_PRESSED && event.getButton() == MouseButton.PRIMARY) {

					// no vertex is clicked before
					if (selectedVertex == null) {
						selectedVertex = clickedCircle;
						FillTransition ft = new FillTransition(Duration.millis(300), clickedCircle, Color.LIGHTSKYBLUE,
								Color.RED);
						ft.play();
						// return;
					} else {
						// Click the same vertex --> no new edge
						if (selectedVertex.getId().equals(clickedCircle.getId())) {
							FillTransition ft = new FillTransition(Duration.millis(300), clickedCircle, Color.RED,
									Color.LIGHTSKYBLUE);
							ft.play();
							selectedVertex = null;
							// return;
						} else // if click a new circle --> add new edge
						{
							// input value of edge
							Label capacityLabel = new Label();
							capacityLabel.setTextFill(Color.BLACK);
							capacityLabel.setLayoutX((selectedVertex.getCenterX() + clickedCircle.getCenterX()) / 2);
							capacityLabel.setLayoutY((selectedVertex.getCenterY() + clickedCircle.getCenterY()) / 2);

							TextInputDialog dialog = new TextInputDialog("1");
							dialog.setTitle(null);
							dialog.setHeaderText("Enter capacity of the Edge :");
							dialog.setContentText(null);

							Optional<String> result = dialog.showAndWait();
							if (result.isPresent()) {
								capacityLabel.setText(result.get());
							} else {
								capacityLabel.setText("1");
							}

							// new Edge
							try {

								int capacity = Integer.valueOf(result.get()); // can throw numberFormatException
								if (capacity <= 0) {
									throw new NumberFormatException();
								}

								Vertex fromVertex = findVertexbyCircle(selectedVertex);
								Vertex toVertex = findVertexbyCircle(clickedCircle);

								if (fromVertex == null || toVertex == null) {
									System.out.println("Can not find vertex from clicked circle");
									return;
								} else {
									// check if this edge existed or not
									if (checkExistEdge(graph, fromVertex, toVertex)) {
										System.out.println("Edge is already exist.");
										showAlert(AlertType.ERROR, "Error", "Insert edge error:",
												"This Edge is already exist.");
										return;
									}

								}

								Arrow arrow = new Arrow(selectedVertex.getCenterX(), selectedVertex.getCenterY(),
										clickedCircle.getCenterX(), clickedCircle.getCenterY());

								Edge newEdge = new Edge(fromVertex, toVertex, arrow, capacity, capacityLabel);

								// add to graph model
								graph.addNewEdge(newEdge);
								System.out.println("Number of edge: " + graph.getListEdge().size());

								// add weight and edge label to screen
								canvasGroup.getChildren().add(capacityLabel);
								canvasGroup.getChildren().add(arrow);

								numberOfEdge += 1;
								if (numberOfEdge >= 1) {
									removeEdgeButton.setDisable(false);
								}
							} catch (NumberFormatException e) {
								System.out.println("Wrong weight input");
								showAlert(AlertType.ERROR, "Error", "Insert edge error:", "Wrong input weight.");
								return;
							}

							FillTransition ft = new FillTransition(Duration.millis(300), selectedVertex, Color.RED,
									Color.LIGHTSKYBLUE);
							selectedVertex = null;
							ft.play();

						}
					}

				}
			} else if (removeNode) { // in mode of remove node
				Circle clickedCircle = (Circle) event.getSource();
				if (event.getEventType() == MouseEvent.MOUSE_PRESSED && event.getButton() == MouseButton.PRIMARY) {
					Vertex clickedVertex = findVertexbyCircle(clickedCircle);
					String clickedVertexID = clickedVertex.getVertextUI().getId();
					// remove all edge linked to this vertex on screen
					for (Edge edge : graph.getListEdge()) {
						if (edge.getFromVertex().getVertextUI().getId().equals(clickedVertexID)
								|| edge.getToVertex().getVertextUI().getId().equals(clickedVertexID)) {
							canvasGroup.getChildren().remove(edge.getCapacityLabel());
							canvasGroup.getChildren().remove(edge.getArrow());
						}
					}
					// remove veretx on screen
					canvasGroup.getChildren().remove(clickedVertex.getId());
					canvasGroup.getChildren().remove(clickedCircle);

					// remove vertex in graph model
					graph.removeVertex(clickedVertex);
					System.out.println("Number of vertex : " + graph.getListVertex().size());
					System.out.println("Number of edge now: " + graph.getListEdge().size());

				}
			}
		}
	};

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		System.out.println("In init");

		hiddenPane.setContent(canvasGroup);

		viewer.prefHeightProperty().bind(border.heightProperty());
		viewer.prefWidthProperty().bind(border.widthProperty());

		// addNode default
		addNode = true;
		addNodeButton.setSelected(true);
		addEdgeButton.setDisable(true);
		removeNodeButton.setDisable(true);
		removeEdgeButton.setDisable(true);

		FFAButton.setDisable(true);
		EKAButton.setDisable(true);

		returnButton.setOnAction(e -> {
			try {
				ResetHandle(null);
				Parent root = FXMLLoader.load(getClass().getResource("/view/WelcomeScreen.fxml"));

				Scene scene = new Scene(root);
				Stage primaryStage = (Stage) panel1.getScene().getWindow();
				primaryStage.setScene(scene);
			} catch (IOException ex) {
				Logger.getLogger(VisualizePageController.class.getName()).log(Level.SEVERE, null, ex);
			}
		});

		// if user chose automatic mode
		if (WelcomePageController.automatic) {
			int numberOfRandomVertices = WelcomePageController.numberOfVertices;
			System.out.println("Number of vertices: " + numberOfRandomVertices);

			graph = Graph.generateGraph(numberOfRandomVertices);
			numberOfVertex = graph.getListVertex().size();
			numberOfEdge = graph.getListEdge().size();
			addEdgeButton.setDisable(false);
			removeNodeButton.setDisable(false);
			removeEdgeButton.setDisable(false);
			FFAButton.setDisable(false);
			EKAButton.setDisable(false);

			System.out.println("Number of vertices in graph: " + graph.getListVertex().size());
			System.out.println("Number of edge in graph:" + graph.getListEdge().size());
			System.out.println("Adjacent vertex of vertex 1: " + graph.getVertexById(1).getAdjacentVertex().size());
			for (Vertex vertex : graph.getListVertex()) {
				// handle mouse action
				vertex.getVertextUI().setOnMousePressed(mouseEventHandler);

				canvasGroup.getChildren().add(vertex.getVertextUI());
				canvasGroup.getChildren().add(vertex.getId());

				ScaleTransition tr = new ScaleTransition(Duration.millis(1100), vertex.getVertextUI());
				tr.setByX(12f);
				tr.setByY(12f);
				tr.setInterpolator(Interpolator.EASE_OUT);
				tr.play();
			}

			for (Edge edge : graph.getListEdge()) {
				canvasGroup.getChildren().add(edge.getArrow());
				canvasGroup.getChildren().add(edge.getCapacityLabel());

			}
		}
		System.out.println("finish init");

		hiddenRoot.setPrefWidth(220);
		hiddenRoot.setPrefHeight(581);

		hiddenRoot.setCursor(Cursor.DEFAULT);
		// Set Label "Detail"
		Label detailLabel = new Label("Detail");
		detailLabel.setPrefSize(hiddenRoot.getPrefWidth() - 20, 38);
		detailLabel.setAlignment(Pos.CENTER);
		detailLabel.setFont(new Font("Roboto", 20));
		detailLabel.setPadding(new Insets(7, 40, 3, -10));
		detailLabel.setStyle("-fx-background-color: #dcdde1;");
		detailLabel.setLayoutX(35);

		// Set TextFlow pane properties
		textFlow.setPrefSize(hiddenRoot.getPrefWidth(), hiddenRoot.getPrefHeight() - 2);
//        textFlow.prefHeightProperty().bind(hiddenRoot.heightProperty());
		textFlow.setStyle("-fx-background-color: #dfe6e9;");
		textFlow.setLayoutY(39);
		textContainer.setLayoutY(textFlow.getLayoutY());
		textFlow.setPadding(new Insets(5, 0, 0, 5));
		textFlow.setEditable(false);
		textContainer.setContent(textFlow);

		// Set Pin/Unpin Button
		JFXButton pinUnpin = new JFXButton();
		pinUnpin.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

		ImageView imgPin = new ImageView(new Image(getClass().getResourceAsStream("/image/pinned.png")));
		imgPin.setFitHeight(20);
		imgPin.setFitWidth(20);
		ImageView imgUnpin = new ImageView(new Image(getClass().getResourceAsStream("/image/unpinned.png")));
		imgUnpin.setFitHeight(20);
		imgUnpin.setFitWidth(20);
		pinUnpin.setGraphic(imgPin);

		pinUnpin.setPrefSize(20, 39);
		pinUnpin.setButtonType(JFXButton.ButtonType.FLAT);
		pinUnpin.setStyle("-fx-background-color: #dcdde1;");
		pinUnpin.setOnMouseClicked(e -> {
			if (pinned) {
				pinUnpin.setGraphic(imgPin);
				hiddenPane.setPinnedSide(null);
				pinned = false;
			} else {
				pinUnpin.setGraphic(imgUnpin);
				hiddenPane.setPinnedSide(Side.RIGHT);
				pinned = true;
			}
		});

		// Add Label and TextFlow to hiddenPane
		hiddenRoot.getChildren().addAll(pinUnpin, detailLabel, textContainer);
		hiddenPane.setRight(hiddenRoot);
		hiddenRoot.setOnMouseEntered(e -> {
			hiddenPane.setPinnedSide(Side.RIGHT);
			openHidden.setVisible(false);
			e.consume();
		});
		hiddenRoot.setOnMouseExited(e -> {
			if (!pinned) {
				hiddenPane.setPinnedSide(null);
				openHidden.setVisible(true);
			}
			e.consume();
		});
		hiddenPane.setTriggerDistance(60);
	}

	@FXML
	private void handle(MouseEvent mouseEvent) {
		// add new vertex
		if (addNode) {
			if (numberOfVertex >= 1) {
				removeNodeButton.setDisable(false);
			}
			if (numberOfVertex >= 2) {
				addEdgeButton.setDisable(false);
			}
			System.out.println("Source: " + mouseEvent.getSource());

			if (!mouseEvent.getSource().equals(canvasGroup)) {
				if (mouseEvent.getEventType() == MouseEvent.MOUSE_RELEASED
						&& mouseEvent.getButton() == MouseButton.PRIMARY) {

					numberOfVertex += 1;

					// new circle vertex
					Circle circle = new Circle(mouseEvent.getX(), mouseEvent.getY(), 1.2, Color.LIGHTSKYBLUE);
					System.out.println("X = " + mouseEvent.getX());
					System.out.println("Y = " + mouseEvent.getY());

					circle.setId(String.valueOf(numberOfVertex));
					// label for this circle
					Label vertexName = new Label(String.valueOf(numberOfVertex));
					vertexName.setTextFill(Color.BLACK);
					vertexName.setLayoutX(mouseEvent.getX());
					vertexName.setLayoutY(mouseEvent.getY());

					// show new vertex in screen
					canvasGroup.getChildren().add(circle);
					canvasGroup.getChildren().add(vertexName);

					// create new actual vertex
					Vertex newVertex = new Vertex(vertexName, circle);
					// add vertex to graph
					graph.addNewVertex(newVertex);
					System.out.println("Number of vertex: " + graph.getListVertex().size());

					circle.setOnMousePressed(mouseEventHandler);

					ScaleTransition tr = new ScaleTransition(Duration.millis(1100), circle);
					tr.setByX(12f);
					tr.setByY(12f);
					tr.setInterpolator(Interpolator.EASE_OUT);
					tr.play();

				}
			}
		} else if (removeEdge) {
			if (!mouseEvent.getSource().equals(canvasGroup)) {
				if (mouseEvent.getEventType() == MouseEvent.MOUSE_CLICKED
						&& mouseEvent.getButton() == MouseButton.PRIMARY) {
					double x = mouseEvent.getX();
					double y = mouseEvent.getY();
					double removedRadius = 15;
					Edge deletedEdge = null;

					// take the closest edge of clicked point
					for (Edge edge : graph.getListEdge()) {
						double distance = distanceBetweenEdgeAndClickedPoint(edge, x, y);
						if (distance <= removedRadius) {
							removedRadius = distance;
							deletedEdge = edge;
						}
					}
					// if there is no edge is acceptable
					if (deletedEdge == null) {
						showAlert(AlertType.INFORMATION, "Alert", "Status:", "Click is not close any edge.");
						return;
					} else {
						// remove edge in screen
						canvasGroup.getChildren().remove(deletedEdge.getCapacityLabel());
						canvasGroup.getChildren().remove(deletedEdge.getArrow());

						// remove edge in graph model
						graph.removeEdge(deletedEdge);
						System.out.println("Number of edge: " + graph.getListEdge().size());
					}
				}
			}
		}

	}

	private Vertex findVertexbyCircle(Circle circle) {
		for (Vertex vertex : graph.getListVertex()) {
			if (vertex.getVertextUI().getId().equals(circle.getId())) {
				return vertex;
			}
		}
		return null;
	}

	@FXML
	private void StartAlgorithms() {
		addNode = false;
		addNodeButton.setDisable(true);
		start = true;
		String sourceIndex = null, sinkIndex = null;

		// add for source and sink
		try {
			TextInputDialog input = new TextInputDialog("1");
			input.setTitle(null);
			input.setHeaderText("Input source index: ");
			input.setContentText(null);
			Optional<String> result = input.showAndWait();
			if (result.isPresent()) {
				sourceIndex = result.get();
			} else {
				sourceIndex = "1";
			}

			input = new TextInputDialog(String.valueOf(numberOfVertex));
			input.setTitle(null);
			input.setHeaderText("Input sink index:");
			input.setContentText(null);
			result = input.showAndWait();
			if (result.isPresent()) {
				sinkIndex = result.get();
			} else {
				sinkIndex = String.valueOf(numberOfVertex);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		//
		if (ffaMode) {
			FordFulkerson ffAlgorithms = new FordFulkerson(graph, canvasGroup);
			ffAlgorithms.setSt(st);
			start(ffAlgorithms, sourceIndex, sinkIndex);
		} else if (ekaMode) {
			EdmondsKarp ekAlgorithms = new EdmondsKarp(graph, canvasGroup);
			ekAlgorithms.setSt(st);
			start(ekAlgorithms, sourceIndex, sinkIndex);
		}
	}

	@FXML
	private void AddNodeHandle(ActionEvent event) {
		addNode = true;
		addEdge = false;
		removeNode = false;
		removeEdge = false;
		addNodeButton.setSelected(true);
		addEdgeButton.setSelected(false);
		removeNodeButton.setSelected(false);
		removeEdgeButton.setSelected(false);
	}

	@FXML
	private void AddEdgeHandle(ActionEvent event) {
		addNode = false;
		addEdge = true;
		removeNode = false;
		removeEdge = false;
		addNodeButton.setSelected(false);
		addEdgeButton.setSelected(true);
		removeNodeButton.setSelected(false);
		removeEdgeButton.setSelected(false);
	}

	@FXML
	private void RemoveNodeHandle(ActionEvent event) {
		addNode = false;
		addEdge = false;
		removeNode = true;
		removeEdge = false;
		addNodeButton.setSelected(false);
		addEdgeButton.setSelected(false);
		removeNodeButton.setSelected(true);
		removeEdgeButton.setSelected(false);
	}

	@FXML
	private void RemoveEdgeHandle(ActionEvent event) {
		addNode = false;
		addEdge = false;
		removeNode = false;
		removeEdge = true;
		addNodeButton.setSelected(false);
		addEdgeButton.setSelected(false);
		removeNodeButton.setSelected(false);
		removeEdgeButton.setSelected(true);
	}

	@FXML
	private void ClearHandle(ActionEvent event) {
		addNode = true;
		addNodeButton.setDisable(false);
		ffaMode = false;
		ekaMode = false;
		start = false;
		FFAButton.setSelected(false);
		EKAButton.setSelected(false);
		textFlow.clear();
		if (st != null) {
			st.stop();
			st.getChildren().clear();
		}
		for (Vertex vertex : graph.getListVertex()) {
			vertex.setVisited(false);
			FillTransition ft1 = new FillTransition(Duration.millis(300), vertex.getVertextUI());
			ft1.setToValue(Color.LIGHTSKYBLUE);
			ft1.play();
		}

		for (Edge edge : graph.getListEdge()) {
			edge.getCapacityLabel().setText(String.valueOf(edge.getCapacity()));
		}

		textFlow.clear();

		System.out.println("Finish clear");
	}

	@FXML
	private void ResetHandle(ActionEvent event) {
		ClearHandle(event);
		addNode = true;
		addEdge = false;
		removeNode = false;
		removeEdge = false;
		ffaMode = false;
		ekaMode = false;
		start = false;

		addNodeButton.setSelected(true);
		addEdgeButton.setDisable(true);
		removeNodeButton.setDisable(true);
		removeEdgeButton.setDisable(true);
		FFAButton.setDisable(true);
		EKAButton.setDisable(true);
		startButton.setDisable(true);

		numberOfEdge = 0;
		numberOfVertex = 0;
		canvasGroup.getChildren().clear();
		canvasGroup.getChildren().addAll(viewer);
		selectedVertex = null;

		hiddenPane.setPinnedSide(null);

		graph.setListEdge(new ArrayList<Edge>());
		graph.setListVertex(new ArrayList<Vertex>());

		System.out.println("Finish reset");
	}

	@FXML
	private void FFAHandle(ActionEvent event) {
		startButton.setDisable(false);
		ekaMode = false;
		ffaMode = true;

	}

	@FXML
	private void EKAHandle(ActionEvent event) {
		startButton.setDisable(false);
		ekaMode = true;
		ffaMode = false;

	}

	@FXML
	private void PauseSequentialTransition(ActionEvent event) {
		if (st != null) {
			if (!isPause) {
				if (st != null) {
					st.pause();
					isPause = true;
					pauseButton.setText("Continue");
				}
			} else {
				isPause = false;
				st.play();
				pauseButton.setText("Pause");
			}
		}
	}

	// call to method run algorithms
	private void start(MaxFlowAlgorithms algorithms, String sourceIndex, String sinkIndex) {

		Vertex sourceVertex = null, sinkVertex = null;
		System.out.println("Source: " + sourceIndex);
		System.out.println("Sink: " + sinkIndex);
		// find vertex by index
		for (Vertex vertex : graph.getListVertex()) {
			if (vertex.getId().getText().equals(sourceIndex)) {
				sourceVertex = vertex;
			}
			if (vertex.getId().getText().equals(sinkIndex)) {
				sinkVertex = vertex;
			}
		}

		if (sourceVertex == null || sinkVertex == null) {
			showAlert(AlertType.ERROR, "Internal Error", "Message: ", "Vertex not found.");
			return;
		}

		FillTransition ft = new FillTransition(Duration.millis(500), sourceVertex.getVertextUI());
		ft.setToValue(Color.RED);
		ft.play();
		ft = new FillTransition(Duration.millis(500), sinkVertex.getVertextUI());
		ft.setToValue(Color.RED);
		ft.play();

		algorithms.execute(sourceVertex, sinkVertex);
	}

	private boolean checkExistEdge(Graph graph, Vertex fromVertex, Vertex toVertex) {
//		for (Edge edge : graph.getListEdge()) {
//			if (edge.getFromVertex().getId().getText().equals(fromVertex.getId().getText())) {
//				if (edge.getToVertex().getId().getText().equals(toVertex.getId().getText())) {
//					return true;
//				}
//			}
//		}
		for (Edge edge : fromVertex.getListEdge()) {
			if (edge.getFromVertex().equals(toVertex) || edge.getToVertex().equals(toVertex)) {
				return true;
			}
		}
		return false;
	}

	private void showAlert(AlertType alertType, String title, String header, String contentText) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(contentText);
		alert.showAndWait();
	}

	private double distanceBetweenEdgeAndClickedPoint(Edge edge, double x, double y) {
		double startx = edge.getArrow().getStartX();
		double startY = edge.getArrow().getStartY();
		double endX = edge.getArrow().getEndX();
		double endY = edge.getArrow().getEndY();
		Line2D line = new Line2D.Double(startx, startY, endX, endY);

		return line.ptLineDist(x, y);
	}

}
