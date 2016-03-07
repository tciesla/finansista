package pl.tciesla.finansista;

import java.io.IOException;
import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import pl.tciesla.finansista.view.AssetsOverviewController;
import pl.tciesla.finansista.view.ExpensesOverviewController;
import pl.tciesla.finansista.view.IncomesOverviewController;

public class FinansistaApplication extends Application {

	private static final String WINDOW_TITLE = "Finansista";
	private static final String INCOMES_TAB_TITLE = "Incomes";
	private static final String EXPENSES_TAB_TITLE = "Expenses";
	private static final String ASSETS_TAB_TITLE = "Assets";

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		BorderPane rootLayoutPane = loadRootLayout();
		TabPane tabPane = createTabPaneInRootLayout(rootLayoutPane);
		loadIncomesOverviewTab(primaryStage, tabPane);
		loadExpensesOverviewTab(primaryStage, tabPane);
		loadAssetsOverviewTab(primaryStage, tabPane);
		createWindowAndShow(primaryStage, rootLayoutPane);
	}

	private BorderPane loadRootLayout() throws IOException {
		FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/RootLayout.fxml"));
		return loader.load();
	}

	private TabPane createTabPaneInRootLayout(BorderPane rootLayoutPane) {
		TabPane tabPane = new TabPane();
		tabPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
		rootLayoutPane.setCenter(tabPane);
		return tabPane;
	}
	
	private void loadIncomesOverviewTab(Stage stage, TabPane tabPane) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/IncomesOverview.fxml"));
		AnchorPane incomesOverviewPane = loader.load();

		Tab tab = new Tab(INCOMES_TAB_TITLE);
		tab.setContent(incomesOverviewPane);
		tabPane.getTabs().add(tab);

		IncomesOverviewController controller = loader.getController();
		controller.setStage(stage);
	}
	
	private void loadExpensesOverviewTab(Stage stage, TabPane tabPane) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/ExpensesOverview.fxml"));
		AnchorPane expensesOverviewPane = loader.load();

		Tab tab = new Tab(EXPENSES_TAB_TITLE);
		tab.setContent(expensesOverviewPane);
		tabPane.getTabs().add(tab);

		ExpensesOverviewController controller = loader.getController();
		controller.setStage(stage);
	}

	private void loadAssetsOverviewTab(Stage stage, TabPane tabPane) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/AssetsOverview.fxml"));
		AnchorPane assetsOverviewPane = loader.load();

		Tab tab = new Tab(ASSETS_TAB_TITLE);
		tab.setContent(assetsOverviewPane);
		tabPane.getTabs().add(tab);
		tabPane.getSelectionModel().select(tab);

		AssetsOverviewController controller = loader.getController();
		controller.setStage(stage);
	}

	private void createWindowAndShow(Stage primaryStage, BorderPane rootLayout) {
		Scene scene = new Scene(rootLayout);
		primaryStage.setScene(scene);
		primaryStage.setTitle(WINDOW_TITLE);
		primaryStage.show();
	}

}
