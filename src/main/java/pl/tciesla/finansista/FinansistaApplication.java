package pl.tciesla.finansista;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import pl.tciesla.finansista.view.AssetOverviewController;

public class FinansistaApplication extends Application {

	private static final String WINDOW_TITLE = "Finansista";
	private static final String ASSETS_TAB_TITLE = "Assets";

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		BorderPane rootLayoutPane = loadRootLayout();
		TabPane tabPane = createTabPaneInRootLayout(rootLayoutPane);
		loadAssetsOverviewTab(primaryStage, tabPane);
		createWindowAndShow(primaryStage, rootLayoutPane);
	}

	private BorderPane loadRootLayout() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(FinansistaApplication.class.getResource("view/RootLayout.fxml"));
		BorderPane rootLayout = loader.load();
		return rootLayout;
	}

	private TabPane createTabPaneInRootLayout(BorderPane rootLayoutPane) {
		TabPane tabPane = new TabPane();
		rootLayoutPane.setCenter(tabPane);
		return tabPane;
	}

	private void loadAssetsOverviewTab(Stage stage, TabPane tabPane) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(FinansistaApplication.class.getResource("view/AssetsOverview.fxml"));
		AnchorPane assetsOverviewPane = loader.load();

		Tab tab = new Tab(ASSETS_TAB_TITLE);
		tab.setContent(assetsOverviewPane);
		tabPane.getTabs().add(tab);

		AssetOverviewController controller = loader.getController();
		controller.setStage(stage);
	}

	private void createWindowAndShow(Stage primaryStage, BorderPane rootLayout) {
		Scene scene = new Scene(rootLayout);
		primaryStage.setScene(scene);
		primaryStage.setTitle(WINDOW_TITLE);
		primaryStage.show();
	}

}
