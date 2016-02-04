package pl.tciesla.finansista;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class FinansistaApplication extends Application {
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader rootLayoutLoader = new FXMLLoader();
		rootLayoutLoader.setLocation(FinansistaApplication.class.getResource("view/RootLayout.fxml"));
		BorderPane rootLayout = rootLayoutLoader.load();
	
		FXMLLoader assetOverviewLoader = new FXMLLoader();
		assetOverviewLoader.setLocation(FinansistaApplication.class.getResource("view/AssetOverview.fxml"));
		AnchorPane assetOverview = assetOverviewLoader.load();
		rootLayout.setCenter(assetOverview);
		
		Scene scene = new Scene(rootLayout);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Finansista");
		primaryStage.show();
	}
}
