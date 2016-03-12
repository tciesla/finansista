package pl.tciesla.finansista;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import pl.tciesla.finansista.view.AssetsWindowController;

import java.io.IOException;
import java.util.ResourceBundle;

public class JustAssetsApplication extends Application {

	private static final String APPLICATION_NAME = "JustAssets";

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
        Pane assetsPane = loadAssetsPane(primaryStage);
        createWindowAndShow(primaryStage, assetsPane);
	}

	private Pane loadAssetsPane(Stage stage) throws IOException {
        FXMLLoader loader = createLoader();
        Pane assetsPane = loader.load();
		AssetsWindowController controller = loader.getController();
		controller.setStage(stage);
        return assetsPane;
	}

    private FXMLLoader createLoader() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/AssetsWindow.fxml"));
        loader.setResources(ResourceBundle.getBundle("Bundle"));
        return loader;
    }

    private void createWindowAndShow(Stage stage, Pane pane) {
		Scene scene = new Scene(pane);
		stage.setScene(scene);
		stage.setTitle(APPLICATION_NAME);
		stage.show();
	}

}
