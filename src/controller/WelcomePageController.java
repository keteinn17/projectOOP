package controller;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class WelcomePageController implements Initializable {

	public static boolean automatic = true, manual = false;

	@FXML
	private Button nextButton;

	@FXML
	private RadioButton autoRadioButton;

	@FXML
	private RadioButton manualRadioButton;

	@FXML
	private AnchorPane panel1;
	
	public static Integer numberOfVertices;

	/**
	 * @return the numberOfVertices
	 */
	public Integer getNumberOfVertices() {
		return numberOfVertices;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		automatic = false;
		manual = false;
		manualRadioButton.setSelected(manual);
		nextButton.setDisable(false);
		
//		autoRadioButton.setOnAction(e -> {
//			automatic = true;
//			manual = false;
//			System.out.println("Choosed auto mode");
//		});
//
//		manualRadioButton.setOnAction(e -> {
//			automatic = false;
//			manual = true;
//			System.out.println("Choosed manual mode");
//		});

		nextButton.setOnAction(e -> {
			if(automatic == false && manual == false) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Alert");
				alert.setHeaderText("Message:");
				alert.setContentText("Please choosing the available mode.");
				alert.showAndWait();
				return;
			}
			FadeOut();
		});
	}

	private void FadeOut() {
		FadeTransition ft = new FadeTransition();
		ft.setDuration(Duration.millis(500));
		ft.setNode(panel1);
		ft.setFromValue(1);
		ft.setToValue(0);
		ft.setOnFinished(e -> {
			LoadNextScene();
		});
		ft.play();
		System.out.println("Moving to main screen");
	}

	private void LoadNextScene() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/VisualizeScreen.fxml"));
		try {
			Parent root = loader.load();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/view/Styling.css").toExternalForm());

			Stage primaryStage = (Stage) panel1.getScene().getWindow();
			primaryStage.setScene(scene);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@FXML
	public void AutomaticallyButtonHandle(ActionEvent event) {
		manual = false;
		automatic = true;
		
		System.out.println("Automatical mode.");
		
		TextInputDialog dialog = new TextInputDialog("7");
		dialog.setTitle("Input number of vertices");
		dialog.setHeaderText("Enter the number of vertices (max 15):");
		dialog.setContentText(null);
		
		Optional<String> result = dialog.showAndWait();
		String input = result.get();
		try {
			numberOfVertices = Integer.valueOf(input);
			if(numberOfVertices > 15) {
				throw new NumberFormatException();
			}
		} catch (NumberFormatException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Input error");
			alert.setContentText("Number of vertices is not correct.");
		}
	}
	
	@FXML 
	public void ManuallyButtonHandle(ActionEvent event) {
		manual = true;
		automatic = false;
		
		System.out.println("Manual mode.");
	}
	/**
	 * @return the automatic
	 */
	public boolean isAutomatic() {
		return automatic;
	}

	/**
	 * @param automatic the automatic to set
	 */
	public void setAutomatic(boolean automatic) {
		WelcomePageController.automatic = automatic;
	}

	/**
	 * @return the manual
	 */
	public boolean isManual() {
		return manual;
	}

	/**
	 * @param manual the manual to set
	 */
	public void setManual(boolean manual) {
		WelcomePageController.manual = manual;
	}
}
