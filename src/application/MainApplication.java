package application;


import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.application.Application;

public class MainApplication extends Application {
	public static Stage primaryStage;

	@Override
	public void start(Stage stage) throws Exception {
		primaryStage = stage;
		Parent root = FXMLLoader.load(getClass().getResource("/view/WelcomeScreen.fxml"));
//	        Parent root = FXMLLoader.load(getClass().getResource("Canvas.fxml"));  

		Scene scene = new Scene(root);

		primaryStage.setScene(scene);
		primaryStage.setTitle("Max Flow Algorithm Visualization");
		primaryStage.show();
		
		
	}

	public static void main(String[] args) {
		launch(args);
	}

}
