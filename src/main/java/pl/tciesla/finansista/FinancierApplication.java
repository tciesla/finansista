package pl.tciesla.finansista;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import pl.tciesla.finansista.view.AssetsOverviewController;

import java.io.IOException;

public class FinancierApplication extends Application {

	private static final String WINDOW_TITLE = "Financier";

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
        Pane assetsPane = loadAssetsPane(primaryStage);
        createWindowAndShow(primaryStage, assetsPane);
	}

	private Pane loadAssetsPane(Stage stage) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/AssetsOverview.fxml"));
		Pane assetsPane = loader.load();

		AssetsOverviewController controller = loader.getController();
		controller.setStage(stage);

        return assetsPane;
	}

	private void createWindowAndShow(Stage stage, Pane pane) {
		Scene scene = new Scene(pane);
		stage.setScene(scene);
		stage.setTitle(WINDOW_TITLE);
		stage.show();
	}

}
